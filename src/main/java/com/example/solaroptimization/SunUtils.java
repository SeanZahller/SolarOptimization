package com.example.solaroptimization;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import static com.example.solaroptimization.TimeUtils.changeTime;

public class SunUtils {

    public static void sunMovement() {
        double sliderValue = SkyBoxApplication.slider.getValue();
        if(sliderValue == 0)
        {
            changeTime(0, 0);
            sunTrajectory(0.0);
        }
        else if(sliderValue == .5)
        {
            changeTime(0, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 1.5)
        {
            changeTime(1, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 1)
        {
            changeTime(1, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 2.5)
        {
            changeTime(2, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 2)
        {
            changeTime(2, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 3.5)
        {
            changeTime(3, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 3)
        {
            changeTime(3, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 4.5)
        {
            changeTime(4, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 4)
        {
            changeTime(4, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 5.5)
        {
            changeTime(5, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 5)
        {
            changeTime(5, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 6.5)
        {
            changeTime(6, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 6)
        {
            changeTime(6, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 7.5)
        {
            changeTime(7, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 7)
        {
            changeTime(7, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 8.5)
        {
            changeTime(8, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 8)
        {
            changeTime(8, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 9.5)
        {
            changeTime(9, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 9)
        {
            changeTime(9, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 10.5)
        {
            changeTime(10, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 10)
        {
            changeTime(10, 0);
            sunTrajectory(sliderValue);

        }
        else if(sliderValue == 11)
        {
            changeTime(11, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 11.5)
        {
            changeTime(11, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 12)
        {
            changeTime(12, 0);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 12.5)
        {
            changeTime(12, 30);
            sunTrajectory(sliderValue);
        }
        else if(sliderValue == 13)
        {
            changeTime(13, 0);
            sunTrajectory(sliderValue);
        }
    }

    static void sunTrajectory(Double sliderValue) {
        double x;
        double y;
        double angle;
        double angleRadians;

        if(sliderValue == 0)
        {
            //Suns start position
            angle = 0;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 0.5)
        {
            angle = 7.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 1.0)
        {
            angle = 15;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 1.5)
        {
            angle = 22.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 2.0)
        {
            angle = 30;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 2.5) {
            angle = 37.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 3.0)
        {
            angle = 45;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 3.5)
        {
            angle = 52.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 4.0)
        {
            angle = 60;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 4.5)
        {
            angle = 67.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 5.0)
        {
            angle = 75;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 5.5)
        {
            angle = 82.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 6.0)
        {
            angle = 90;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 6.5)
        {
            angle = 97.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 7.0)
        {
            angle = 105;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 7.5)
        {
            angle = 112.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 8.0)
        {
            angle = 120;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 8.5)
        {
            angle = 127.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 9.0)
        {
            angle = 135;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 9.5)
        {
            angle = 142.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 10.0)
        {
            angle = 150;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 10.5)
        {
            angle = 157.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 11.0)
        {
            angle = 165;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 11.5)
        {
            angle = 172.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 12.0)
        {
            angle = 178;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 12.5)
        {
            angle = 179;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }
        else if(sliderValue == 13)
        {
            angle = 180;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            Sun.sun.setTranslateX(x * 1000);
            Sun.sun.setTranslateY(-y * 1000);
            Sun.sun.setTranslateZ(0);
        }

    }

    static void recalculateSunTimes() {

        TimeUtils.cal.clear();
        TimeUtils.cal.setTime(TimeUtils.date); //Calender object given corresponding date

        TimeUtils.location = new Location(TimeUtils.latitude, TimeUtils.longitude); // Will be entered in coordinates
        System.out.println(TimeUtils.location.getLatitude());
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(TimeUtils.location, TimeUtils.timeZone); // Creates calculator for sun times
        System.out.println(TimeUtils.timeZone.toString());

        TimeUtils.sunriseTime = calculator.getOfficialSunriseForDate(TimeUtils.cal); // Gets sunrise based on date and calculator created
        TimeUtils.sunsetTime = calculator.getOfficialSunsetForDate(TimeUtils.cal); // Gets sunset based on date and calculator created

        //Initialize currentTime and current sun position with the time
        changeTime(6, 0);
        sunTrajectory(6.0);

    }
}
