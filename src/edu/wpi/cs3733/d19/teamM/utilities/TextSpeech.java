package edu.wpi.cs3733.d19.teamM.utilities;

// Java code to convert
// text to speech

import com.sun.speech.freetts.VoiceManager;

import javax.sound.midi.SysexMessage;
import javax.speech.Central;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


// Java code to convert
// text to speech
import java.util.Locale;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

public class TextSpeech
{

    int tracker = 0;

    int working = 0;

    //Thread voiceThread =  voiceThread =
    public void speakToUser()
    {
       if(working == 0)
       {
           new Thread(new Thread(() -> {
               try
               {
                   tracker = 0;
                   working = 1;
                   System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
                   Voice voice;
                   VoiceManager voiceManager = VoiceManager.getInstance();

                   voice = voiceManager.getVoice("kevin");
                   voice.allocate();

                   File file = new File("resource.txt");
                   Scanner scanner = new Scanner(file);

                   while (scanner.hasNextLine() && tracker==0)
                   {
                       System.out.println(tracker);
                       String nextLine = scanner.nextLine();
                       System.out.println(nextLine);
                       voice.speak(nextLine);
                   }
                   working = 0;
               }

               catch (Exception e)
               {
                   e.printStackTrace();
               }
           })).start();
       }

    }

    public void quitSpeaking() {
       tracker = 1;
       working = 0;
    }
}
