package com.teamness.smane.bluetooth;

import java.io.IOException;

/**
 * Created by samtebbs on 04/02/2018.
 */

public interface BluetoothStream {

    int available() throws IOException;
    byte[] read() throws IOException;

}
