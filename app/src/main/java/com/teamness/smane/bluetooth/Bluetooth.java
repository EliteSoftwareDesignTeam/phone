package com.teamness.smane.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.teamness.smane.Converter;
import com.teamness.smane.Handleable;
import com.teamness.smane.Handler;
import com.teamness.smane.event.Event;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by samtebbs on 04/02/2018.
 */

public class Bluetooth extends Handleable<byte[], String> {

    public static final String UUID_STRING = "94f39d29-7d6d-437d-973b-fba39e49d4ee";
    public static final java.util.UUID UUID = java.util.UUID.fromString(UUID_STRING);

    private BluetoothSocket socket;
    private BluetoothDevice device;
    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private Thread workerThread;
    private OutputStream os;
    private final Converter<byte[], String> defaultConverter = new Converter<byte[], String>() {
        @Override
        public String convert(byte[] t) {
            return new String(t);
        }
    };

    public boolean connect(String deviceName) throws IOException {
        if(adapter.isEnabled()) {
            for(BluetoothDevice d : adapter.getBondedDevices()) if(d.getName().equals(deviceName)) {
                device = d;
                socket = device.createRfcommSocketToServiceRecord(UUID);
                socket.connect();
                if(socket.isConnected()) {
                    os = socket.getOutputStream();
                    return true;
                }
            }
        }
        return false;
    }

    public void send(byte[] bytes) throws IOException {
        os.write(bytes);
    }

    public void addHandler(Handler<String> handler) {
        super.addHandler(defaultConverter, handler);
    }

    public void start(final BluetoothStream stream) {
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!workerThread.isInterrupted()) {
                    try {
                        if (stream.available() > 0) handle(stream.read());
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
            }
        });
        workerThread.start();
    }

    public void start() throws IOException {
        start(new DefaultBluetoothStream(socket));
    }

}
