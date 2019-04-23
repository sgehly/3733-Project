package edu.wpi.cs3733.d19.teamM.utilities;

import java.net.*;
import java.io.*;
import java.util.List;

public class SocketClient {

    public void toConnString(List<List<Double>> input) {
        String outputString = "";
        System.out.println(input.size());
        for (int i = 0; i < input.size(); i++) {
            System.out.println(input.get(i));
            if (input.get(i).get(0) < 0) {
                outputString += ",R" + (int)Math.abs(input.get(i).get(0));
            } else if (input.get(i).get(0) > 0) {
                outputString += ",L" + (int)Math.abs(input.get(i).get(0));
            }
            outputString += ",S" + (int)Math.abs(input.get(i).get(1));
        }
        System.out.println(outputString);
        connect(outputString + ",-");
        //connect(",MHello ken this is a test,-");
        //connect(outputString + ",-");
        connect(",MRoomba Test,-");

    }

    public void connect(String inputString) {
        try {
            String sentence = inputString;
            char[] myArr = sentence.toCharArray();
            for (int i = 0; i < sentence.length(); i++) {
                String modifiedSentence;
                Socket clientSocket = new Socket("130.215.122.95", 9999);
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