package com.teamness.smane.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by samtebbs on 04/02/2018.
 */

public class Bluetooth {

    public static final String UUID_STRING = "94f39d29-7d6d-437d-973b-fba39e49d4ee";
    public static final java.util.UUID UUID = java.util.UUID.fromString(UUID_STRING);

    private BluetoothSocket socket;
    private BluetoothDevice device;
    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private Thread workerThread;
    private List<Consumer<byte[]>> handlers = new ArrayList<>();

    public boolean connect(String deviceName) throws IOException {
        if(adapter.isEnabled()) {
            Optional<BluetoothDevice> deviceOpt = adapter.getBondedDevices().stream().filter(d -> d.getName().equals(deviceName)).findFirst();
            if(deviceOpt.isPresent()) {
                device = deviceOpt.get();
                socket = device.createRfcommSocketToServiceRecord(UUID);
                return true;
            }
        }
        return false;
    }

    public void send(byte[] bytes) throws IOException {
        socket.getOutputStream().write(bytes);
    }

    public void start(final BluetoothStream stream) {
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!workerThread.isInterrupted()) {
                    try {
                        if(stream.available() > 0) onData(stream.read());
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

    private void onData(final byte[] bytes) {
        handlers.forEach(h -> h.accept(bytes));
    }

    public void addHandler(Consumer<byte[]> handler) {
        handlers.add(handler);
    }

}
