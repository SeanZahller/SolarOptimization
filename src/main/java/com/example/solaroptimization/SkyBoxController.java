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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class SkyBoxController {

    @FXML
    private static Pane skyboxPane;
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
    private TextField theLocationPicker;
    @FXML
    private Button returnOnInvestment;
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




    @FXML
    protected void initialize() throws ParseException, IOException {
        setSkyboxPane();
        setEntireFrame();

    }

    @FXML
    private void setEntireFrame() {
        entireFrame.getChildren().addAll(new Pane(skyboxPane), new AnchorPane(uiPane), new AnchorPane(sliderAndDate));
    }


    @FXML
    protected Pane setSkyboxPane() throws ParseException {
        Group skybox = new Group();
        Group sun = SkyBoxApplication.sunCreation(); //creating sun
        skyboxPane = new Pane(sun, skybox);
        SkyBoxApplication.startParams(); // Setting start date, location, sunset/sunrise times
        Group panelsWHouse = SkyBoxApplication.models(); //Creating all models for the scene

        skybox.getChildren().addAll(sun, panelsWHouse);
        SkyBoxApplication.constructWorld(skybox); // Construct the empty SkyBox group

        //THIS causes an error: Duplicate children added so i commented it out. Its being out in skyboxPane above
        //skyboxPane.getChildren().add(skybox);


        //Heres where we do the set up camera and background?
        //Could we move it into constructWorld?
        //How to convert the things happening from a Scene to A AnchorPane or how to add a new scene into the Pane?

        /*
        skybox.setFill(new ImagePattern(SkyBoxApplication.skyboxImage));
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(30000.0);

         */


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
        SkyBoxApplication.latitude = BigDecimal.valueOf(Double.parseDouble(coords[0]));
        SkyBoxApplication.longitude = BigDecimal.valueOf(Double.parseDouble(coords[1]));
        //SkyBoxApplication.location = new Location(coords[0], coords[1]);

        recalculateSunTimes();
    }

    public void showROI(ActionEvent actionEvent) {
    }

    public void removeAllPanels(ActionEvent actionEvent) {

        SkyBoxApplication.panelsWHouse.getChildren().remove(6);
        SkyBoxApplication.panelsWHouse.getChildren().remove(5);
        SkyBoxApplication.panelsWHouse.getChildren().remove(4);
        SkyBoxApplication.panelsWHouse.getChildren().remove(3);
        SkyBoxApplication.panelsWHouse.getChildren().remove(2);
        SkyBoxApplication.panelsWHouse.getChildren().remove(1);
    }

    public void showIntensityLevels(ActionEvent actionEvent) {
    }

    public void showSunTimes(ActionEvent actionEvent) {

        String sunTimes =
                "On " + SkyBoxApplication.date.toString() + "\n"
                        + "at latitude: " + SkyBoxApplication.latitude.toString() + ", and longitude: " + SkyBoxApplication.latitude.toString() + ":\n"
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
        SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelOnewR);
        SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelTwowR);
        SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelThreewR);
        SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.solarPanelFourwR);
        SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.gPanelOneBox);
        SkyBoxApplication.panelsWHouse.getChildren().add(SkyBoxApplication.gPanelTwoBox);
    }

    public void exitProgram(ActionEvent aE) {
        Stage stage = (Stage) uiPane.getScene().getWindow();
        stage.close();
    }

    public void highlightOptimalPanels(ActionEvent actionEvent) {
    }

    public void getTimeZone(ActionEvent actionEvent) {
        SkyBoxApplication.timeZone = actionEvent.getTarget().toString();
        recalculateSunTimes();
    }

    //Helper Methods

    private void recalculateSunTimes() {

        SkyBoxApplication.cal.setTime(SkyBoxApplication.date); //Calender object given corresponding date

        SkyBoxApplication.location = new Location(SkyBoxApplication.latitude.doubleValue(), SkyBoxApplication.longitude.doubleValue()); // Will be entered in coordinates
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(SkyBoxApplication.location, SkyBoxApplication.timeZone); // Creates calculator for sun times

        SkyBoxApplication.sunriseTime = calculator.getOfficialSunriseForDate(SkyBoxApplication.cal); // Gets sunrise based on date and calculator created
        SkyBoxApplication.sunsetTime = calculator.getOfficialSunsetForDate(SkyBoxApplication.cal); // Gets sunset based on date and calculator created
    }
}