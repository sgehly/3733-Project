package edu.wpi.cs3733.d19.teamM.utilities;

import edu.wpi.cs3733.d19.teamM.Main;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;

import java.net.URL;
import java.util.Scanner;

public class Weather {

    static JSONObject obj;
    String DEGREE = "\u00b0"; // unicode character for degree

    /**
     * Gets the weather of the city of Boston.
     *
     */
    public Weather(Label desc, ImageView icon) {

        try{
            geocoding("Boston"); // gets info for this city or place

            JSONObject j = obj.getJSONObject("main");

            // go to different part of api link
            JSONObject detail = obj.getJSONArray("weather").getJSONObject(0);

            // may want to add a picture or gif of the weather
            // String weathDesc = j.getString("main"); and then use switch statement

            String main = detail.getString("main");

            //Clouds,Mist,Snow,Rain,Haze,

            Platform.runLater(() -> {

                desc.setText(j.getInt("temp")+"\u00b0F / "+detail.getString("main")); // textual description of weather

                if(main.equals("Rain") || main.equals("Thunderstorm") || main.equals("Drizzle")){
                    icon.setImage(new Image(Main.getResource("/resources/icons/icons8-rain.png")));
                }
                else if(main.equals("Snow")){
                    icon.setImage(new Image(Main.getResource("/resources/icons/snow.png")));
                }
                else if(main.equals("Clouds")){
                    icon.setImage(new Image(Main.getResource("/resources/icons/icons8-cloud.png")));
                }
                else if(main.equals("Clear")){
                    icon.setImage(new Image(Main.getResource("/resources/icons/icons8-sunny.png")));
                }else{
                    icon.setImage(new Image(Main.getResource("/resources/icons/icons8-error.png")));
                }
            });


        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets weather information for a specific city or place in the world
     *
     * @param city City that weather information is extracted from
     * @throws Exception Throws exception if the city does not exist or is invalid
     */
    public static void geocoding(String city) throws Exception{
        String apiString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=imperial&APPID=f509390a2b821e7910908f1e12f78a35";
        URL url = new URL(apiString);

        Scanner scan = new Scanner(url.openStream()); // traverses through input url string to get all information
        String str = new String();

        // extract all info from url and creates a new string to hold everything
        while(scan.hasNext()) {
            str = str + scan.nextLine();
        }
        scan.close();

        System.out.println(str);
        // JSON object created from new string which has all necessary info for extracting weather information
        obj = new JSONObject(str);
    }
}
