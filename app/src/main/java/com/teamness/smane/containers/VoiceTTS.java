package com.teamness.smane.containers;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.teamness.smane.interfaces.IDirectionOutput;
import com.teamness.smane.interfaces.ITextOutput;

/**
 * Created by bianca on 17/02/2018.
 */

public class VoiceTTS implements IDirectionOutput, ITextOutput {

    public TextToSpeech myTTS;

    private String LEFT = "Turn left";
    private String RIGHT = "Turn right";
    private String FORWARD = "Keep going forward";

    public VoiceTTS(Context context, TextToSpeech.OnInitListener listener) {
        this.myTTS = new TextToSpeech(context, listener);
    }

    public void speakWords(String speech) {
        //myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        myTTS.speak(speech, TextToSpeech.QUEUE_ADD, null);
        //while(true) myTTS.speak("aaa", TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void giveDirection(double angle, double strength) {
        if (angle == 0)
            myTTS.speak(FORWARD, TextToSpeech.QUEUE_ADD, null);
        else if (angle < 0)
            myTTS.speak(RIGHT, TextToSpeech.QUEUE_ADD, null);
        else
            myTTS.speak(LEFT, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void sendMessage(String message) {
        speakWords(message);
    }
}
