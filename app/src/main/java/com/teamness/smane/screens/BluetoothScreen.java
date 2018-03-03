package com.teamness.smane.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.teamness.smane.R;
import com.teamness.smane.bluetooth.Bluetooth;
import com.teamness.smane.bluetooth.CaneBluetooth;
import com.teamness.smane.event.CaneEvents;
import com.teamness.smane.event.Event;
import com.teamness.smane.event.TestEvent;

import java.io.IOException;
import java.util.ArrayList;

public class BluetoothScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_screen);
        try {
            new CaneBluetooth().init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        CaneEvents.BT_OUT.trigger(new TestEvent());
    }

}
