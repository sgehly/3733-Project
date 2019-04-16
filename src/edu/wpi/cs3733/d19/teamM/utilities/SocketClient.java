package edu.wpi.cs3733.d19.teamM.utilities;

import java.net.*;
import java.io.*;
import java.util.List;

public class SocketClient {

    public void toConnString(List<List<Double>> input){
        String output = "";
        for(int i = 0; i < input.size(); i++){
            System.out.println(input.get(i));
            if(i == 0){
                if(input.get(i).get(0) < 0){
                    output+="R" + Math.abs(input.get(i).get(0));
                }
                else if(input.get(i).get(0) > 0){
                    output+="L" + Math.abs(input.get(i).get(0);
                }
                else{
                    output+="S";
                }
                output+=input.get(i).get(1)+",";
            }
            else{
                System.out.println(output);
                System.out.println(input.get(i));
            }

        }
        connect(output + "-");
    }

    public void connect(String inputString) {
        try {
            String sentence = inputString;
            char[] myArr = sentence.toCharArray();
            for(int i = 0; i < sentence.length(); i++) {
                String modifiedSentence;
                Socket clientSocket = new Socket("130.215.126.45", 9999);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToServer.writeBytes(Character.toString(myArr[i]));
                System.out.println(myArr[i]);
                modifiedSentence = inFromServer.readLine();
                System.out.println(modifiedSentence);
                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}




