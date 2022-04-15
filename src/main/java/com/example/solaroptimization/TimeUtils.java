package com.example.solaroptimization;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import static com.example.solaroptimization.SkyBoxApplication.slider;

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

    public static void getLocation() {
        TimeUtils.theLocation = TimeUtils.locationPicker.getText();
        String[] coords = TimeUtils.theLocation.split(",");
        TimeUtils.latitude = Double.parseDouble(coords[0]);
        TimeUtils.longitude = Double.parseDouble(coords[1]);

        SunUtils.recalculateSunTimes();
    }

    static void startParams() throws ParseException {
        //Choosing Spokane 3/10/22 as starting place and time
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd"); //Formatter
        TimeUtils.date = formatter.parse(TimeUtils.theDate); //Parse string to create Date object
        TimeUtils.cal = Calendar.getInstance(); //Calendar object created
        TimeUtils.cal.setTime(TimeUtils.date); //Calender object given corresponding date

        TimeUtils.location = new Location(TimeUtils.latitude.doubleValue(), TimeUtils.longitude.doubleValue()); // Will be entered in coordinates
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(TimeUtils.location, TimeUtils.timeZone); // Creates calculator for sun times

        TimeUtils.sunriseTime = calculator.getOfficialSunriseForDate(TimeUtils.cal); // Gets sunrise based on date and calculator created
        TimeUtils.sunsetTime = calculator.getOfficialSunsetForDate(TimeUtils.cal); // Gets sunset based on date and calculator created

        //Initialize Slider Ticks
        LocalTime start = LocalTime.parse(TimeUtils.sunriseTime);
        LocalTime end =  LocalTime.parse(TimeUtils.sunsetTime);
        Long hoursBetweenRiseSet = ChronoUnit.HOURS.between(start, end);
        slider.setMax(hoursBetweenRiseSet + 1);

        //Initialize currentTime and current sun position with the time
        TimeUtils.changeTime(6, 0);
        SunUtils.sunTrajectory(6.0);
    }

}
