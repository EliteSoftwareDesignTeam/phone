package com.teamness.smane.bluetooth;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by samtebbs on 04/02/2018.
 */

public class DefaultBluetoothStream implements BluetoothStream {

    private final InputStream bluetoothIn;

    public DefaultBluetoothStream(BluetoothSocket socket) throws IOException {
        this.bluetoothIn = socket.getInputStream();
    }

    @Override
    public int available() throws IOException {
        return bluetoothIn.available();
    }

    @Override
    public byte[] read() throws IOException {
        int bytesAvailable = available();
        byte[] bytes = new byte[bytesAvailable];
        if(bytesAvailable > 0) bluetoothIn.read(bytes);
        return bytes;
    }
}
