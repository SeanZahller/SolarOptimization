package com.example.solaroptimization;

import com.luckycatlabs.sunrisesunset.dto.Location;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    static String theLocation;
    static TextField locationPicker = new TextField();
    static String timeZone = "GMT-8";
    static TextField tz = new TextField();
    static Date date;
    static DatePicker datePicker = new DatePicker();
    static String sunriseTime;
    static String sunsetTime;
    private static String currentTime;
    private static String convertedSunrise;
    private static String convertedSunset;
    static Label currentTimeLabel = new Label();
    static Calendar cal;
    static String theDate = "20220310";
    static Location location;
    static Double latitude = 47.6588;
    static Double longitude = -117.4260;

    public static void getTimeZone() {
        theLocation = locationPicker.getText();
        timeZone = tz.getText();
        SunUtils.recalculateSunTimes();
    }

    //Methods for buttons
    public static void getDate() throws ParseException {
        String newDate = datePicker.getValue().toString();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //Formatter
        date = formatter.parse(newDate);

        SunUtils.recalculateSunTimes();
    }

    public static void changeTime(int currentHour, int currentMin){
        String startTime = sunriseTime;
        String[] wholeTime = startTime.split(":");

        int hour = Integer.parseInt(wholeTime[0]);
        int min = Integer.parseInt(wholeTime[1]);
        int newHour = hour + currentHour;
        int newMin = min + currentMin;

        StringBuilder timeMaker = new StringBuilder(newHour + ":" + newMin);
        currentTime = timeMaker.toString();

        if(currentTime.length() < 5)
        {
            String str = String.format("%1s",currentTime);
            str = str.replace(' ','0');

            currentTimeLabel.setText(str);
        }
        else
            currentTimeLabel.setText(currentTime);

        System.out.println(currentTimeLabel);
    }

}
