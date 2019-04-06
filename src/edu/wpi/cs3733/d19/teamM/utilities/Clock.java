package edu.wpi.cs3733.d19.teamM.utilities;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Clock {

    private int hrs;
    private int mins;
    private int secs;

   public Clock(Label clockLabel, Label dateLabel){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            secs = LocalTime.now().getSecond();
            mins = LocalTime.now().getMinute();
            hrs = LocalTime.now().getHour();

            clockLabel.setText(hrs + ":" + (mins < 10 ? "0"+mins : mins) + ":" + (secs < 10 ? "0"+secs : secs));
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        java.util.Date d = new Date();
        Calendar cal = Calendar.getInstance();
        dateLabel.setText(date.format(d));
    }
}
