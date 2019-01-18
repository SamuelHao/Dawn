There are 5 Java files that run the Dawn Android app. Below is a rough guide to what each does. 

<h1>Back End</h1> 

<h2>1. mAlarmReceiver </h2>
Everything to do with alarms is here. Setting alarms, getting alarms, cancelling alarms, etc. 
mAlarmReceiver extends BroadcastReceiver, so it listens for when it's called. 


<h2>2. mBluetoothManager </h2>
Everything to do with bluetooth is here. You can check the connection, and send values to the device. 
mBluetoothReceiver supports the Singleton design pattern, implementing "getInstance()" and having a private constructor. 


<h1>Front End</h1>
<h2>1. AlarmFragment </h2>
This is one of the tabs on the home screen. AlarmFragment provides information on the status of alarms, and let's the user "schedule" a blind raising. 

<h2>2. ManualFragment </h2>
This is the other tab on the home screen. ManualFragment provides ways to debug the code without having to set an alarm each time. 

<h2>3. MainActivity </h2>
This is the activity that holds Alarm and Manual Fragments. Not much is done here, other than setting up the fragments, and a few lifecycle things. 





