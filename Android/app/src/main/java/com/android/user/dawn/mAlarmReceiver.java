package com.android.scowluga.dawn;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/** The complete class for alarm management */
public class mAlarmReceiver extends BroadcastReceiver {

    // Constants for what to send to Arduino
    public static final int SNOOZE_VALUE = 69;   // :D
    public static final int DISMISS_VALUE = 101; // named after SE 101

    /** Received set alarm for blind opening */
    @Override
    public void onReceive(final Context context, Intent intent) {
        // Just tactile feedback :)
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        // Send the "snooze" value: start the blind opening
        Log.d("TAG", "Alarm Received, sending snooze");
        mBluetoothManager.getInstance(context).sendValue(SNOOZE_VALUE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // after 50 seconds, we check if the user has snoozed/dismissed
                // we account for multiple alarms by simply saying another alarm means they "snoozed"
                // If there's no more alarms, finish up. Dismiss value
                // if there's more alarms, rinse and repeat

                boolean isSnooze = scheduleAlarm(context);
                if (isSnooze) {
                    Log.d("TAG", "Response: Snooze, next alarm set");
                } else {
                    Log.d("TAG", "Response: Dismiss. sending dismiss");
                    mBluetoothManager.getInstance(context).sendValue(DISMISS_VALUE);
                }

            }
        }, 1000*50); // we wait 50 seconds, within time limit of next potential alarm
    }

    /** Scheduling an alarm for the next one */
    public static boolean scheduleAlarm (Context c) {
        long nextAlarm = mAlarmReceiver.getNextAlarm(c);
        if (nextAlarm == -1) {
            // no alarm
            Toast.makeText(c, "No next alarm", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            // next alarm exists. Schedule

            Intent i = new Intent(c, mAlarmReceiver.class);
            PendingIntent p = PendingIntent.getBroadcast(c, 0, i, 0);

            AlarmManager manager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
            manager.setExact(AlarmManager.RTC_WAKEUP, nextAlarm, p);
    //        manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, p);

            Toast.makeText(c, "Blind opening set for: " + mAlarmReceiver.getNextAlarmFormatted(c), Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    /** Cancels alarm */
    public static void cancelAlarm (Context c) {
        Intent i = new Intent(c, mAlarmReceiver.class);
        PendingIntent p = PendingIntent.getBroadcast(c, 0, i, 0);

        AlarmManager manager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        try {
            manager.cancel(p);
        } catch (NullPointerException e) {
            Log.e("TAG", "null pointer cancelling: " + e.getMessage());
        }
    }

    /** get next formatted */
    public static String getNextAlarmFormatted(Context context) {
        String nextAlarm = Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
        return nextAlarm;
    }

    /** get next alarm */
    public static long getNextAlarm(Context context) {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.AlarmClockInfo info = manager.getNextAlarmClock();
        try {
            return info.getTriggerTime();
        } catch (Exception e) {
            return -1;
        }
    }
}
