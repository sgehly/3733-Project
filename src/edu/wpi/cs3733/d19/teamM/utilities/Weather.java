package edu.wpi.cs3733.d19.teamM.utilities;

import javafx.scene.control.Label;
import org.json.JSONObject;

import java.net.URL;
import java.util.Scanner;

public class Weather {

    static JSONObject obj;
    String DEGREE = "\u00b0"; // unicode character for degree

    /**
     * Gets the weather of the city of Boston.
     *
     * @param currTemp Label for the current temperature
     * @param minTemp Label for the min temperature for the day
     * @param maxTemp Label for the max temperature for the day
     * @param description Label for the physical description of the current weather
     */
    public Weather(Label currTemp, Label minTemp, Label maxTemp, Label description) {

        try{
            geocoding("Boston"); // gets info for this city or place

            JSONObject j = obj.getJSONObject("main");

            currTemp.setText(j.getInt("temp") + DEGREE + "F"); // gets current temperature
            minTemp.setText(j.getInt("temp_min") + DEGREE + "F"); // min temp for day
            maxTemp.setText(j.getInt("temp_max") + DEGREE + "F"); // high temp for day

            // go to different part of api link
            j = obj.getJSONArray("weather").getJSONObject(0);

            // may want to add a picture or gif of the weather
            // String weathDesc = j.getString("main"); and then use switch statement

            description.setText(j.getString("description")); // textual description of weather


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

        // JSON object created from new string which has all necessary info for extracting weather information
        obj = new JSONObject(str);
    }
}
