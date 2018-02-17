package com.teamness.smane.containers;

import android.speech.tts.TextToSpeech;

/**
 * Created by bianca on 17/02/2018.
 */

public class VoiceTTS {

    public TextToSpeech myTTS;
    public int MY_DATA_CHECK_CODE = 0;

    public void speakWords(String speech) {
        //myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        myTTS.speak(speech, TextToSpeech.QUEUE_ADD, null);
        //while(true) myTTS.speak("aaa", TextToSpeech.QUEUE_ADD, null);
    }
}
