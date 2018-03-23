package com.teamness.smane.bluetooth;

import android.util.Base64;

import com.teamness.smane.Base64Provider;
import com.teamness.smane.Handler;
import com.teamness.smane.Serialisation;
import com.teamness.smane.event.CaneEvents;
import com.teamness.smane.event.Event;
import com.teamness.smane.event.EventChannel;

import java.io.IOException;

/**
 * Created by samtebbs on 21/02/2018.
 */

public class CaneBluetooth {

    static final String PI_NAME = "£$£$£$£$Smanepiness";
    private static Bluetooth bt;

<<<<<<< HEAD
    static {
        try {
            new CaneBluetooth().init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

=======
>>>>>>> dev
    public boolean init() throws IOException {
        bt = new Bluetooth();
        if(bt.connect(PI_NAME)) {
            Serialisation.base64Provider = new Base64Provider() {
                @Override
                public byte[] decode(String base64Str) {
                    return Base64.decode(base64Str, Base64.NO_WRAP);
                }

                @Override
                public String encode(byte[] bytes) {
                    return Base64.encodeToString(bytes, Base64.NO_WRAP);
                }
            };
            bt.start();
            CaneEvents.BT_OUT.onAny(EventChannel.EventPriority.HIGH, "onBtEvent", this);
            bt.addHandler(new Handler<String>() {
                @Override
                public void handle(String s) {
                    try {
                        CaneEvents.BT_IN.trigger((Event) Serialisation.toObject(s));
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return false;
    }

    public void onBtEvent(Event e) {
        try {
            bt.send(Serialisation.fromObject(e).getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
