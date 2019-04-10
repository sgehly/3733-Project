package edu.wpi.cs3733.d19.teamM.utilities;

// Java code to convert
// text to speech

import com.sun.speech.freetts.VoiceManager;

import javax.sound.midi.SysexMessage;
import javax.speech.Central;
import java.io.File;
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

    public void speakToUser()
    {

        try
        {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Voice voice;
            VoiceManager voiceManager = VoiceManager.getInstance();

            voice = voiceManager.getVoice("kevin");
            voice.allocate();

            File file = new File("resource.txt");
            Scanner scanner = new Scanner(file);

          while (scanner.hasNextLine())
            {
                String nextLine = scanner.nextLine();
                System.out.println(nextLine);
                voice.speak(nextLine);


            }
            /**
            // set property as Kevin Dictionary



            // Register Engine
            Central.registerEngineCentral
                    ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");

            // Create a Synthesizer
            Synthesizer synthesizer =
                    Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));

            // Allocate synthesizer
            synthesizer.allocate();

            // Resume Synthesizer
            synthesizer.resume();

            synthesizer.getSynthesizerProperties().setVoice();
            // speaks the given text until queue is empty.
            File file = new File("C:\\Users\\Vishn\\IdeaProjects\\3733-Project\\src\\resources\\TextPath.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine())
            {
                String nextLine = scanner.nextLine();
                System.out.println(nextLine);
                synthesizer.speakPlainText(nextLine, null);

            }
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

            // Deallocate the Synthesizer.
            synthesizer.deallocate();*/
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
