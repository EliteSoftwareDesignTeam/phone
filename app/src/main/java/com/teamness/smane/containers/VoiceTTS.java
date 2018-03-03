package com.teamness.smane.containers;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.teamness.smane.interfaces.IDirectionOutput;

/**
 * Created by bianca on 17/02/2018.
 */

public class VoiceTTS implements IDirectionOutput{

    public TextToSpeech myTTS;

    private String FORWARD = "Continue forward";
    private String SLIGHT_LEFT = "Make a slight left";
    private String SLIGHT_RIGHT = "Make a slight right";
    private String SHARP_LEFT = "Make a sharp left";
    private String SHARP_RIGHT = "Make a sharp right";
    private String U_TURN = "Make a U-turn";

    public VoiceTTS(Context context, TextToSpeech.OnInitListener listener){
        this.myTTS = new TextToSpeech(context, listener);
    }

    public void speakWords(String speech) {
        //myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        myTTS.speak(speech, TextToSpeech.QUEUE_ADD, null);
        //while(true) myTTS.speak("aaa", TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void giveDirection(double angle, double strength) {
        // -180 -> 0 -> 180
        if(angle == 0)
            myTTS.speak(FORWARD, TextToSpeech.QUEUE_ADD, null);
        else if(angle > 0) {
            if(angle <= 10)
                myTTS.speak(FORWARD, TextToSpeech.QUEUE_ADD, null);
            else if (angle <= 45)
                myTTS.speak(SLIGHT_LEFT, TextToSpeech.QUEUE_ADD, null);
            else if(angle <= 135)
                myTTS.speak(SHARP_LEFT, TextToSpeech.QUEUE_ADD, null);
            else
                myTTS.speak(U_TURN, TextToSpeech.QUEUE_ADD, null);
        }
        else{
            if(angle >= -10)
                myTTS.speak(FORWARD, TextToSpeech.QUEUE_ADD, null);
            else if(angle >= -45)
                myTTS.speak(SLIGHT_RIGHT, TextToSpeech.QUEUE_ADD, null);
            else if(angle >= -135)
                myTTS.speak(SHARP_RIGHT, TextToSpeech.QUEUE_ADD, null);
            else
                myTTS.speak(U_TURN, TextToSpeech.QUEUE_ADD, null);
        }
    }
}
