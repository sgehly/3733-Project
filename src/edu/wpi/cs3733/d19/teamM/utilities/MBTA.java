package edu.wpi.cs3733.d19.teamM.utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MBTA {

    public long getNextTrain(){

        int mins = LocalTime.now().getMinute();
        int hrs = LocalTime.now().getHour();
        String sURL = "https://api-v3.mbta.com/schedules?filter[stop]=place-brmnl&include=stop&page[limit]=1&sort=arrival_time&filter[min_time]="+hrs+":"+(mins < 10 ? '0'+mins : mins); //just a string
        System.out.println(sURL);
        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            JsonElement root = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.

            JsonObject firstThing = rootObj.getAsJsonArray("data").get(0).getAsJsonObject();
            System.out.println(firstThing);

            LocalDateTime time = LocalDateTime.parse(firstThing.getAsJsonObject("attributes").get("arrival_time").getAsString(), DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime now = LocalDateTime.now();

            return Duration.between(now, time).toMinutes();
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

}
