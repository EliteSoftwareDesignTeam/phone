package com.teamness.smane.prototype;

import android.util.Log;

import com.teamness.smane.interfaces.IDirectionOutput;

/**
 * Gives directions based on buttons being pressed
 */

public class ButtonDirectionController implements Runnable{

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private IDirectionOutput output = null;

    public ButtonDirectionController(IDirectionOutput output) {
        this.output = output;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }
    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    @Override
    public void run() {
        try{
            while (true){
                int direction = 0;
                if (this.leftPressed)
                    direction--;
                if (this.rightPressed)
                    direction++;
                this.output.giveDirection(direction, 1);
                Thread.sleep(10);
            }
        }catch(InterruptedException e){
            Log.i("Smane", "ButtonDirectionController interrupted for some reason");
        }


    }
}
