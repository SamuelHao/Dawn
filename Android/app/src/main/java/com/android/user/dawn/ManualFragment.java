package com.android.scowluga.dawn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;


public class ManualFragment extends Fragment {

    public ManualFragment() {
        // Required empty public constructor
    }

    public static ManualFragment newInstance() {
        Bundle args = new Bundle();

        ManualFragment fragment = new ManualFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /** UI */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manual, container, false);

        final MaterialNumberPicker picker = (MaterialNumberPicker)v.findViewById(R.id.numberPicker);
        picker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return "" + (value - 10);
            }
        });


        Button sendBtn = (Button)v.findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = picker.getValue() - 10;
                mBluetoothManager.getInstance(getActivity()).sendValue(value);
            }
        });

        Button snoozeBtn = (Button)v.findViewById(R.id.snoozeBtn);
        snoozeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothManager.getInstance(getActivity()).sendValue(mAlarmReceiver.SNOOZE_VALUE);
            }
        });

        Button dismissBtn = (Button)v.findViewById(R.id.dismissBtn);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothManager.getInstance(getActivity()).sendValue(mAlarmReceiver.DISMISS_VALUE);
            }
        });

        return v;
    }

}
