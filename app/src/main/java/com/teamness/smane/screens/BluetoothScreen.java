package com.teamness.smane.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.teamness.smane.R;
import com.teamness.smane.bluetooth.Bluetooth;

import java.io.IOException;
import java.util.ArrayList;

public class BluetoothScreen extends AppCompatActivity {

    static final String PI_NAME = "£$£$£$£$Smanepiness";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_screen);
        Bluetooth bt = new Bluetooth();
        ArrayList<String> a = new ArrayList<>();
        try {
            final boolean connected = bt.connect(PI_NAME);
            System.out.printf("Con status: %b\n", connected);
            if(connected) {
                bt.start();
                bt.send("memes\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
