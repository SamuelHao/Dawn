package com.android.scowluga.dawn;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

/** The complete class for bluetooth management
 * Follows singleton design pattern (to a degree)
 */
public class mBluetoothManager {

    // The Arduino name
    private static final String DEVICE_NAME = "HC-06";

    // Singleton object
    private static mBluetoothManager manager;

    // Attributes of the mBluetoothManager instance
    private BluetoothSocket socket;
    private Context context;

    // Private constructor
    private mBluetoothManager() {}

    // Creates (if null) and returns the instance
    public static mBluetoothManager getInstance(Context c) {
        if (manager == null) {
            Log.d("TAG", "Setting up mBluetoothManager");
            manager = new mBluetoothManager();
            manager.context = c;
            connectSocket();
        }
        return manager;
    }

    /** Tries to connect to the bluetooth via socket */
    private static boolean connectSocket() {

        Log.d("TAG", "Trying to connect to bluetooth device");
        // Finding the device itself
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
                if (bondedDevices.size() > 0) {
                    Object[] devices = (Object[]) bondedDevices.toArray();

                    // Loop to find the device
                    int position = -1;
                    for (int i = 0; i < devices.length; i++)
                        if (((BluetoothDevice) devices[i]).getName().equals(DEVICE_NAME))
                            position = i;

                    if (position == -1) {
                        Log.d("TAG", "Not Found");
                        return false;
                    }
                    Log.d("TAG", "Found");

                    BluetoothDevice device = (BluetoothDevice) devices[position];
                    ParcelUuid[] uuids = device.getUuids();

                    // Trying to connect
                    try {
                        Log.d("TAG", "Creating Socket");
                        manager.socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    } catch (IOException e) {
                        Log.e("TAG", "Creation Failed: " + e.getMessage());
                    }

                    try {
                        Log.d("TAG", "Connecting...");
                        manager.socket.connect();
                        Log.d("TAG", "Connected");
                    } catch (IOException e) {
                        Log.e("TAG", "Connection Failed: " + e.getMessage());

                        try {
                            Log.d("TAG", "trying fallback...");
                            manager.socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                            manager.socket.connect();
                        } catch (Exception e2) {
                            Log.e("TAG", "Fallback failed. No value sent." + e2.getMessage());
                            return false;
                        }
                    }
                } else {
                    Log.d("TAG", "No Bonded Devices");
                }
            }
        }
        return true;
    }

    /** Sends value over */
    public void sendValue(int value) {
        if (socket == null && !connectSocket()) {
            Log.d("TAG", "Device is not connected");
            Toast.makeText(context, "Device is not connected", Toast.LENGTH_SHORT).show();
        }
        try {
            // Sends value to outputstream
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(value);

            Toast.makeText(context, "Sending value: " + value, Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Successfully wrote to output: " + value);
        } catch (IOException e) {
            Toast.makeText(context, "Failed to send value: " + value, Toast.LENGTH_SHORT).show();
            Log.e("TAG", "Failed to write: " + e.getMessage());
        }
    }

    /** Static method. If device is connected */
    public static boolean isConnected() {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter == null) return false;
        if (!blueAdapter.isEnabled()) return false;

        Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

        if (bondedDevices.size() <= 0) return false;

        Object[] devices = (Object[]) bondedDevices.toArray();

        int position = -1;
        for (int i = 0; i < devices.length; i++)
            if (((BluetoothDevice) devices[i]).getName().equals("HC-06"))
                position = i;

        if (position == -1) return false;
        return true;
    }
}
