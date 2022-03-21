package com.example.solaroptimization;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import static com.example.solaroptimization.SkyBoxApplication.clear;
import static com.example.solaroptimization.SkyBoxApplication.sunTrajectory;


public class SkyBoxController {

    @FXML
    static Pane skyboxPane;
    @FXML
    private Pane entireFrame;
    @FXML
    private AnchorPane uiPane;
    @FXML
    private AnchorPane sliderAndDate;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Slider slider;
    @FXML
    private Label locationLabel;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private TextField theLocationPicker;
    @FXML
    private TextField tz;
    @FXML
    private Button paybackPeriod;
    @FXML
    private Button optimalPanels;
    @FXML
    private Button quit;
    @FXML
    private Button removeAll;
    @FXML
    private Button intensityLevels;
    @FXML
    private Button sunriseSunsetTimes;
    @FXML
    private Button controls;
    @FXML
    private Button addAll;

    private static String currentTime;
    private static String convertedSunrise;
    private static String convertedSunset;
    private static Boolean pressed = false;
    private static Boolean pressedTwo = false;
    private static Boolean pm = false;


    @FXML
    protected void initialize() throws ParseException, IOException {
        setSkyboxPane();
        setEntireFrame();

        //Initialize Slider Ticks
        LocalTime start = LocalTime.parse(SkyBoxApplication.sunriseTime);
        LocalTime end =  LocalTime.parse(SkyBoxApplication.sunsetTime);
        Long hoursBetweenRiseSet = ChronoUnit.HOURS.between(start, end);
        slider.setMax(hoursBetweenRiseSet + 1);

        //Initialize currentTime and current sun position with the time
        changeTime(6, 0);
        sunTrajectory(6.0);

        //Create variables to show converted sunrise and sunset strings
       // convertTimes();

    }

    @FXML
    private void setEntireFrame() {
        entireFrame.getChildren().addAll(new Pane(skyboxPane), new AnchorPane(uiPane), new AnchorPane(sliderAndDate));
    }

    @FXML
    protected static Pane setSkyboxPane() throws ParseException {

        Group skyBox = new Group();

        SkyBoxApplication.sunCreation(); //creating sun

        SkyBoxApplication.startParams(); // Setting start date, location, sunset/sunrise times
        SkyBoxApplication.models(); //Creating all models for the scene

        ImageView[] views = SkyBoxApplication.layoutViews();
        ImageView[] setViews = SkyBoxApplication.loadImageViews(views);
        Group skyboxOut = SkyBoxApplication.createSkybox(setViews);
        skyBox.getChildren().addAll(SkyBoxApplication.sun, SkyBoxApplication.panelsWHouse, skyboxOut);
        //skyBox.getChildren().addAll(SkyBoxApplication.sun, SkyBoxApplication.panelsWHouse);

        skyboxPane = new Pane(skyBox);

        return skyboxPane;
    }

    //Methods for buttons
    public void getDate(ActionEvent actionEvent) throws ParseException {
        String newDate = datePicker.getValue().toString();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //Formatter
        SkyBoxApplication.date = formatter.parse(newDate);

        recalculateSunTimes();
    }

    public void getLocation(ActionEvent actionEvent) {
        SkyBoxApplication.theLocation = theLocationPicker.getText();
        String[] coords = SkyBoxApplication.theLocation.split(",");
        SkyBoxApplication.latitude = Double.parseDouble(coords[0]);
        SkyBoxApplication.longitude = Double.parseDouble(coords[1]);

        //recalculateSunTimes();
    }

    public void showPP(ActionEvent actionEvent) {
        DecimalFormat dc = new DecimalFormat("0.00");
        int panelCost = 550; //Price of each solar panel
        int groundPanelCost = 1100; //Price of each Ground solar panel
        int maintenance = 200; //Cost of maintenance, should me serviced 2x a year
        int installCost = 4000; //Labor cost
        int costPerMonth = 100; //Cost per month to spend
        double averageMonthlyElectricBill = 115.49; // Price of average US Electric bill

        double pp = ((panelCost * 4) + (groundPanelCost * 2) + installCost + (costPerMonth * 12) + (maintenance * 2)) / (averageMonthlyElectricBill * 12);

        String result = "The Payback Period is: " + dc.format(pp) + " Years";
        JOptionPane.showMessageDialog(null, result, "Payback Period", JOptionPane.PLAIN_MESSAGE);

    }

    public void removeAllPanels(ActionEvent actionEvent) {

        if(SkyBoxApplication.panelsWHouse.getChildren().contains(SkyBoxApplication.solarPanelThreewR)) {
            SkyBoxApplication.panelsWHouse.getChildren().remove(6);
            SkyBoxApplication.panelsWHouse.getChildren().remove(5);
            SkyBoxApplication.panelsWHouse.getChildren().remove(4);
            SkyBoxApplication.panelsWHouse.getChildren().remove(3);
            SkyBoxApplication.panelsWHouse.getChildren().remove(2);
            SkyBoxApplication.panelsWHouse.getChildren().remove(1);
        }
    }

    public void showIntensityLevels(ActionEvent actionEvent) {
        double solarP1Intensity, solarP2Intensity, solarP3Intensity, solarP4Intensity, solarGP1Intensity, solarGP2Intensity;
        solarP1Intensity = SkyBoxApplication.calculateLightIntesity((Box) SkyBoxApplication.solarPanelOnewR.getChildren().get(1), SkyBoxApplication.sun);
        solarP2Intensity = SkyBoxApplication.calculateLightIntesity((Box) SkyBoxApplication.solarPanelTwowR.getChildren().get(1), SkyBoxApplication.sun);
        solarP3Intensity = SkyBoxApplication.calculateLightIntesity((Box) SkyBoxApplication.solarPanelThreewR.getChildren().get(1), SkyBoxApplication.sun);
        solarP4Intensity = SkyBoxApplication.calculateLightIntesity((Box) SkyBoxApplication.solarPanelFourwR.getChildren().get(1), SkyBoxApplication.sun);
        solarGP1Intensity = SkyBoxApplication.calculateLightIntesity((Box) SkyBoxApplication.gPanelOneBox.getChildren().get(1), SkyBoxApplication.sun);
        solarGP2Intensity = SkyBoxApplication.calculateLightIntesity((Box) SkyBoxApplication.gPanelTwoBox.getChildren().get(1), SkyBoxApplication.sun);

        if(pressedTwo == false)
        {
            getLevels(solarP1Intensity, (Box) SkyBoxApplication.solarPanelOnewR.getChildren().get(1));
            getLevels(solarP2Intensity, (Box) SkyBoxApplication.solarPanelTwowR.getChildren().get(1));
            getLevels(solarP3Intensity, (Box) SkyBoxApplication.solarPanelThreewR.getChildren().get(1));
            getLevels(solarP4Intensity, (Box) SkyBoxApplication.solarPanelFourwR.getChildren().get(1));
            getLevels(solarGP1Intensity, (Box) SkyBoxApplication.gPanelOneBox.getChildren().get(1));
            getLevels(solarGP2Intensity, (Box) SkyBoxApplication.gPanelTwoBox.getChildren().get(1));
            pressedTwo = true;
        }

        else if(pressedTwo == true) {
            ((Box) SkyBoxApplication.solarPanelOnewR.getChildren().get(1)).setMaterial(clear);
            ((Box) SkyBoxApplication.solarPanelTwowR.getChildren().get(1)).setMaterial(clear);
            ((Box) SkyBoxApplication.solarPanelThreewR.getChildren().get(1)).setMaterial(clear);
            ((Box) SkyBoxApplication.solarPanelFourwR.getChildren().get(1)).setMaterial(clear);
            ((Box) SkyBoxApplication.gPanelOneBox.getChildren().get(1)).setMaterial(clear);
            ((Box) SkyBoxApplication.gPanelTwoBox.getChildren().get(1)).setMaterial(clear);

            pressedTwo = false;
        }


    }

    public void showSunTimes(ActionEvent actionEvent) {

        String sunTimes =
                "On " + SkyBoxApplication.date.toString() + "\n"
                        + "at latitude: " + SkyBoxApplication.latitude + ", and longitude: " + SkyBoxApplication.longitude + ":\n"
                        + "\n"
                        + "Sunrise: " + SkyBoxApplication.sunriseTime + "\n"
                        + "Sunset: " + SkyBoxApplication.sunsetTime;

        JOptionPane.showMessageDialog(null, sunTimes, "Sunrise/Sunset Times", JOptionPane.PLAIN_MESSAGE);
    }

    public void showControls(ActionEvent actionEvent) {
        String controls =
                "Controls:\n"
                        + "\n"
                        + "Rotate Camera: click and drag\n"
                        + "Move Camera Up: w\n"
                        + "Move Camera Left: a\n"
                        + "Move Camera Down: s\n"
                        + "Move Camera Right: d\n"
                        + "Zoom In: ,\n"
                        + "Zoom out: .\n"
                        + "Rotate Model Right: m\n"
                        + "Rotate Model Left: n\n"
                        + "\n"
                        + "Select Solar Panel 1: 1\n"
                        + "Select Solar Panel 2: 2\n"
                        + "Clear Selection: 0\n"
                        + "\n"
                        + "If Panel Is Selected:\n"
                        + "Move Backward: Up Arrow\n"
                        + "Move Forawrd: Down Arrow\n"
                        + "Move Left: Right Arrow\n"
                        + "Move Right: Left Arrow\n"
                        + "Rotate 45 degrees: Spacebar";

        JOptionPane.showMessageDialog(null, controls, "Controls", JOptionPane.PLAIN_MESSAGE);
    }

    public void addAllPanels(ActionEvent actionEvent) {

        if(SkyBoxApplication.panelsWHouse.getChildren().size() < 2) {
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelOnewR);
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelTwowR);
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelThreewR);
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelFourwR);
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.gPanelOneBox);
            SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.gPanelTwoBox);
        }
    }

    public void exitProgram(ActionEvent aE) {
        Stage stage = (Stage) uiPane.getScene().getWindow();
        stage.close();
    }

    public void highlightOptimalPanels(ActionEvent actionEvent) {

        if(pressed == false)
        {
            SkyBoxApplication.colorSetOpt(); //Need to figure out a way to toggle on/off. Just toggles on
            pressed = true;
        }
        else if(pressed = true) {
            ((Box) SkyBoxApplication.solarPanelOnewR.getChildren().get(1)).setMaterial(clear);
            ((Box) SkyBoxApplication.solarPanelTwowR.getChildren().get(1)).setMaterial(clear);
            ((Box) SkyBoxApplication.solarPanelThreewR.getChildren().get(1)).setMaterial(clear);
            ((Box) SkyBoxApplication.solarPanelFourwR.getChildren().get(1)).setMaterial(clear);
            ((Box) SkyBoxApplication.gPanelOneBox.getChildren().get(1)).setMaterial(clear);
            ((Box) SkyBoxApplication.gPanelTwoBox.getChildren().get(1)).setMaterial(clear);

            pressed = false;
        }
    }

    public void getTimeZone(ActionEvent actionEvent) {
        SkyBoxApplication.theLocation = theLocationPicker.getText();
        SkyBoxApplication.timeZone = tz.getText();
        recalculateSunTimes();
    }

    //Helper Methods

    private void recalculateSunTimes() {

        SkyBoxApplication.cal.clear();
        SkyBoxApplication.cal.setTime(SkyBoxApplication.date); //Calender object given corresponding date

        SkyBoxApplication.location = new Location(SkyBoxApplication.latitude, SkyBoxApplication.longitude); // Will be entered in coordinates
        System.out.println(SkyBoxApplication.location.getLatitude());
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(SkyBoxApplication.location, SkyBoxApplication.timeZone); // Creates calculator for sun times
        System.out.println(SkyBoxApplication.timeZone.toString());

        SkyBoxApplication.sunriseTime = calculator.getOfficialSunriseForDate(SkyBoxApplication.cal); // Gets sunrise based on date and calculator created
        SkyBoxApplication.sunsetTime = calculator.getOfficialSunsetForDate(SkyBoxApplication.cal); // Gets sunset based on date and calculator created

        //Initialize currentTime and current sun position with the time
        changeTime(6, 0);
        sunTrajectory(6.0);


    }

    public void sunMovement() {

        double sliderValue = slider.getValue();
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


    public void changeTime(int currentHour, int currentMin){
        String startTime = SkyBoxApplication.sunriseTime;
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

    }
/*
    public void changeHour(int currentHour, int minToAdd){
        String startTime = SkyBoxApplication.sunriseTime;
        String[] wholeTime = startTime.split(":");

        int hour = Integer.parseInt(wholeTime[0]);
        int current = hour + currentHour;

        StringBuilder timeMaker = new StringBuilder(current + ":" + wholeTime[1]);
        currentTime = timeMaker.toString();
        changeMinute(currentTime, minToAdd, false);
    }

    public void changeMinute(String currentTime, int n, boolean pm){
        String[] wholeTime = currentTime.split(":");
        int hour = Integer.parseInt(wholeTime[0]);
        if(hour > 11){
            pm = true;
        }
        int mins = Integer.parseInt(wholeTime[1]);
        int current = mins + n;
        if(!pm) {
            StringBuilder timeMaker = new StringBuilder(wholeTime[0] + ":" + current + " AM");
            currentTime = timeMaker.toString();
            currentTimeLabel.setText(currentTime);
        }
        else {
            if(hour > 12) { //Converting to clock time
                hour = hour - 12;
                StringBuilder timeMaker = new StringBuilder(hour + ":" + current + " PM");
                currentTime = timeMaker.toString();
                currentTimeLabel.setText(currentTime);
            }
            else {
                StringBuilder timeMaker = new StringBuilder(wholeTime[0] + ":" + current + " PM");
                currentTime = timeMaker.toString();
                currentTimeLabel.setText(currentTime);
            }
        }
    }

    private void convertTimes(){
        String startTime = SkyBoxApplication.sunriseTime;
        String[] wholeTime = startTime.split(":");

        String endTime = SkyBoxApplication.sunsetTime;
        String[] wholeTimeTwo = endTime.split(":");

        int hourStart = Integer.parseInt(wholeTime[0]);
        int minsStart = Integer.parseInt(wholeTime[1]);

        int hourEnd = Integer.parseInt(wholeTimeTwo[0]);
        int minsEnd = Integer.parseInt(wholeTimeTwo[1]);

        if(hourStart > 12) { //Converting to clock time
            hourStart = hourStart - 12;
            StringBuilder timeMaker = new StringBuilder(hourStart + ":" + minsStart + " AM");
            convertedSunrise = timeMaker.toString();
        }
        else{
            StringBuilder timeMaker = new StringBuilder(wholeTime[0] + ":" + minsStart + " AM");
            convertedSunrise = timeMaker.toString();
        }
        if(hourEnd > 12) { //Converting to clock time
            hourEnd = hourEnd - 12;
            StringBuilder timeMaker = new StringBuilder(hourEnd + ":" + minsEnd + " PM");
            convertedSunset = timeMaker.toString();
        }
        else{
            StringBuilder timeMaker = new StringBuilder(wholeTimeTwo[0] + ":" + minsEnd + " PM");
            convertedSunset = timeMaker.toString();
        }

    }

 */

    private void getLevels(double intensity, Box box){

        PhongMaterial low = new PhongMaterial(Color.GREEN);
        PhongMaterial medium = new PhongMaterial(Color.ORANGE);
        PhongMaterial high = new PhongMaterial(Color.RED);
        PhongMaterial veryHigh = new PhongMaterial(Color.DARKRED);

        if(intensity < 10){
            box.setMaterial(low);
        }
        else if(intensity > 10 && intensity < 20) {
            box.setMaterial(medium);
        }
        else if(intensity > 20 && intensity < 30) {
            box.setMaterial(high);
        }
        else if(intensity > 30) {
            box.setMaterial(veryHigh);
        }

    }
}