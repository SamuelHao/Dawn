package com.android.scowluga.dawn;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AlarmFragment extends Fragment {


    public AlarmFragment() {
    }

    public static AlarmFragment newInstance() {

        Bundle args = new Bundle();

        AlarmFragment fragment = new AlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Button refreshBtn;
    ToggleButton toggleBtn;

    /** UI */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_alarm, container, false);

        final TextView nextTV = (TextView)v.findViewById(R.id.nextTV);
        final TextView connectTV = (TextView)v.findViewById(R.id.connectTV);

        refreshBtn = (Button)v.findViewById(R.id.refreshDisplay);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alarm = mAlarmReceiver.getNextAlarmFormatted(getActivity());
                if (alarm.isEmpty())
                    nextTV.setText("Next Alarm: N/A");
                else
                    nextTV.setText("Next Alarm: " + alarm);

                connectTV.setText(mBluetoothManager.isConnected() ? "Bluetooth: Connected" : "Bluetooth: Not Connected");
            }
        });
        refreshBtn.performClick();

        toggleBtn = (ToggleButton) v.findViewById(R.id.toggleSwitch);
        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // ON
                    LinearLayout layout = (LinearLayout) v.findViewById(R.id.alarm_ll);
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        View child = layout.getChildAt(i);
                        child.setEnabled(true);
                    }
                    mAlarmReceiver.scheduleAlarm(getActivity());

                } else { // OFF
                    LinearLayout layout = (LinearLayout) v.findViewById(R.id.alarm_ll);
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        View child = layout.getChildAt(i);
                        child.setEnabled(false);
                    }
                    mAlarmReceiver.cancelAlarm(getActivity());
                }
            }
        });
        boolean toggled = getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getBoolean("TOGGLE", false);
        toggleBtn.setChecked(toggled);

        final Button resetBtn = (Button) v.findViewById(R.id.resetAlarm);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarmReceiver.scheduleAlarm(getActivity());
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshBtn != null) {
            refreshBtn.performClick();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit().putBoolean("TOGGLE", toggleBtn.isChecked()).apply();
    }

}
