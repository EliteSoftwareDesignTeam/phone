package com.teamness.smane.bluetooth;

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

    public static boolean init() throws IOException {
        bt = new Bluetooth();
        if(bt.connect(PI_NAME)) {
            bt.start();
            CaneEvents.BT.onAny(EventChannel.EventPriority.HIGH, "onBtEvent", null);
            bt.addHandler(new Handler<String>() {
                @Override
                public void handle(String s) {
                    try {
                        CaneEvents.BT.trigger((Event) Serialisation.toObject(s));
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return false;
    }

    private static void onBtEvent(Event e) {
        try {
            bt.send(Serialisation.fromObject(e).getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
