package com.teamness.smane.containers;

import android.speech.tts.TextToSpeech;

import com.teamness.smane.interfaces.IDirectionOutput;

/**
 * Created by bianca on 17/02/2018.
 */

public class VoiceTTS implements IDirectionOutput{

    public TextToSpeech myTTS;
    public int MY_DATA_CHECK_CODE = 0;

    private String LEFT = "LEFT";
    private String RIGHT = "RIGHT";

    public void speakWords(String speech) {
        //myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        myTTS.speak(speech, TextToSpeech.QUEUE_ADD, null);
        //while(true) myTTS.speak("aaa", TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void giveDirection(double direction, double strength) {
        if(direction < 0)
            myTTS.speak(LEFT, TextToSpeech.QUEUE_ADD, null);
        else
            myTTS.speak(RIGHT, TextToSpeech.QUEUE_ADD, null);
    }
}
