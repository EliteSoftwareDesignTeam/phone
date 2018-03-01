package com.teamness.smane.prototype;

import com.teamness.smane.interfaces.IDirectionOutput;
import com.teamness.smane.interfaces.ITextOutput;

/**
 * Created by mac15001900 on 3/1/18.
 */

public class CommandOutput implements IDirectionOutput, ITextOutput {


    @Override
    public void sendMessage(String message) {
        System.out.println("Instructions received: "+message);
    }

    @Override
    public void giveDirection(double angle, double strength) {
        System.out.println("Directions received "+angle+", "+strength);
    }
}
