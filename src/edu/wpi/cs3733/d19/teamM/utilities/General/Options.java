package edu.wpi.cs3733.d19.teamM.utilities.General;

import edu.wpi.cs3733.d19.teamM.Main;

public class Options {
    private static Options options;
    private static int timeout;
    private Options(){}
    public static Options getOptions(){
        if(options == null){
            options = new Options();
        }
        return options;
    }
    public static int getTimeout(){
        return timeout;
    }
    public static void setTimeout(int time){
        options.timeout = time;
        Main.updateTimer();
    }
}
