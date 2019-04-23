package edu.wpi.cs3733.d19.teamM.utilities;

// Java code to convert
// text to speech

import com.sun.javafx.application.PlatformImpl;
import com.sun.speech.freetts.VoiceManager;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.midi.SysexMessage;
import javax.speech.Central;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.google.api.client.util.Lists;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import io.grpc.Context;
import io.opencensus.metrics.export.Distribution;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

// Java code to convert
// text to speech
import java.util.Locale;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

public class TextSpeech
{
    public static void main(String[] args) {
        TextSpeech textSpeech = new TextSpeech();
        textSpeech.speakToUser();
    }
    Media direction = new Media(new File("src/resources/output.mp3").toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(direction);


    public MediaPlayer getMediaPlayer()
    {
        return mediaPlayer;
    }
    int tracker = 0;

    int working = 0;

    //Thread voiceThread =  voiceThread =
    public void speakToUser()
    {

       if(working == 0)
       {
           if(mediaPlayer!=null)
           {
               quitSpeaking();
           }
           new Thread(new Thread(() -> {
               try
               {

                   tracker = 0;
                   working = 1;

                   CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new FileInputStream("src/resources/My_First_Project-2c9e8d24c91a.json")));

                   TextToSpeechSettings settings = TextToSpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
                   TextToSpeechClient speechClient = TextToSpeechClient.create(settings);
                 /*  System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
                   Voice voice;
                   VoiceManager voiceManager = VoiceManager.getInstance();

                   voice = voiceManager.getVoice("kevin");
                   voice.allocate();
*/
                   VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                           .setLanguageCode("en-US")
                           .setSsmlGender(SsmlVoiceGender.FEMALE)
                           .build();

                   AudioConfig audioConfig = AudioConfig.newBuilder()
                           .setAudioEncoding(AudioEncoding.MP3)
                           .build();

                   File file = new File("resource.txt");
                   Scanner scanner = new Scanner(file);
                   String content = readFile();


                   boolean Speaking = true;
                  // while (Speaking && tracker==0)
                   //{
                    //   System.out.println(tracker);
                      // String nextLine = scanner.nextLine();

                    //   if(nextLine.length()>0)
                      // {
                           SynthesisInput input = SynthesisInput.newBuilder()
                                   .setText(content)
                                   .build();

                           SynthesizeSpeechResponse response = speechClient.synthesizeSpeech(input, voice,
                                   audioConfig);

                           ByteString audioContents = response.getAudioContent();

                           try (OutputStream out = new FileOutputStream("src/resources/output.mp3")) {
                               out.write(audioContents.toByteArray());
                               System.out.println("Audio content written to file \"output.mp3\"");
                           }
                           speechClient.shutdown();
                        //   Media direction = new Media(new File("src/resources/output.mp3").toURI().toString());
                           PlatformImpl.startup(()->{});
                         //  mediaPlayer = new MediaPlayer(direction);
                           mediaPlayer.play();
                     //  }

                     //  Media direction = new Media(new File("src/resources/output.mp3").toURI().toString());
                      // MediaPlayer mediaPlayer = new MediaPlayer(direction);
                       //mediaPlayer.play();
                       // System.out.println(nextLine);
                     //  voice.speak(nextLine);

                  // }
                   working = 0;
               }
               catch (Exception e)
               {
                   e.printStackTrace();
               }
           })).start();
       }

    }

     String readFile() throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get("resource.txt"));
        return new String(encoded, StandardCharsets.UTF_8);
    }
    public void quitSpeaking() {
        mediaPlayer.stop();
       tracker = 1;
       working = 0;
    }
}
