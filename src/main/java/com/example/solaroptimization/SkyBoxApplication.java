package com.example.solaroptimization;


import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.shape.Box;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class SkyBoxApplication extends Application {

    //camera controls and scene settings declarations
    private PerspectiveCamera camera;
    private Group cameraDolly;
    private final double cameraQuantity = 10.0;

    //Mouse control variable declarations
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;


    private static Boolean pm = false;
    static Slider slider = new Slider();


    static BorderPane pane = new BorderPane();
    static Stage stage;

    Image skyboxImage;

    {
        try {
            skyboxImage = new Image(new FileInputStream("C:\\skyboxDesert.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage stage) throws InterruptedException, ParseException {

        Group sceneRoot = new Group();
        startParams();
        SkyBoxUtils.constructWorld(sceneRoot);
        uiBuild();

        //-------------Scene and Camera set up----------------------------//
        //TODO change to 1024, 600
        double sceneWidth = 600;
        //TODO change to 768, 600
        double sceneHeight = 600;
        SubScene Subscene = new SubScene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        Subscene.setFill(new ImagePattern(skyboxImage));
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(30000.0);
        Subscene.setCamera(camera);
        // translations through dolly
        cameraDolly = new Group();
        cameraDolly.setTranslateZ(-1000);
        cameraDolly.setTranslateX(200);
        // rotation transforms
        Group turn = new Group();
        Rotate xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        camera.getTransforms().addAll(xRotate);
        turn.getTransforms().addAll(yRotate);

        sceneRoot.getChildren().add(cameraDolly);
        cameraDolly.getChildren().add(turn);
        turn.getChildren().add(camera);

        pane.setCenter(Subscene);
        Scene scene = new Scene(pane);
        //-------------END of Scene and Camera set up----------------------------//


        //----------------Controls Section----------------------------//

        // Use keyboard to control camera position
        scene.setOnKeyPressed(event -> {
            double change = cameraQuantity;
            KeyCode keycode = event.getCode();

            Rotate r = new Rotate(-1, Rotate.Y_AXIS); //rotate house right
            Rotate l = new Rotate(1, Rotate.Y_AXIS); //rotate house left
            Rotate n = new Rotate(45, Rotate.Y_AXIS); //rotate Ground Panel One
            Rotate n1 = new Rotate(45, Rotate.Y_AXIS); //rotate Ground Panel One

            ModelUtils.setCenters(r, Models.houseImport); //Get centers to rotate from center
            ModelUtils.setCenters(l, Models.houseImport);
            ModelUtils.setCenters(n, Models.gPanelOne);
            ModelUtils.setCenters(n1, Models.gPanelTwo);

            Transform t = new Rotate();

            Point3D delta = null;

            if (keycode == KeyCode.COMMA) {
                delta = new Point3D(0, 0, change);
            }
            if (keycode == KeyCode.PERIOD) {
                delta = new Point3D(0, 0, -change);
            }
            if (keycode == KeyCode.A) {
                delta = new Point3D(-change, 0, 0);
            }
            if (keycode == KeyCode.D) {
                delta = new Point3D(change, 0, 0);
            }
            if (keycode == KeyCode.W) {
                delta = new Point3D(0, -change, 0);
            }
            if (keycode == KeyCode.S) {
                delta = new Point3D(0, change, 0);
            }
            if (keycode == KeyCode.M) { //Rotate house and all solar panels Right
                t = t.createConcatenation(l);
                Models.panelsWHouse.getTransforms().addAll(t);
            }
            if (keycode == KeyCode.N) { //Rotate house and all solar panels Left
                t = t.createConcatenation(r);
                Models.panelsWHouse.getTransforms().addAll(t);
            }
            if (keycode == KeyCode.DIGIT0) { //Clears selected panels
                ModelUtils.clearSelected();
            }
            if (keycode == KeyCode.DIGIT1) { // Selects ground panel number 1
                ModelUtils.gPanelOneSelected();
            }
            if (keycode == KeyCode.DIGIT2) //selects ground panel number 2
            {
                ModelUtils.gPanelTwoSelected();
            }
            if (keycode == KeyCode.RIGHT) { //Move selected ground panel to the right in the screen
                if (ModelUtils.oneSelected == true) {
                    Models.gPanelOneBox.setTranslateX(Models.gPanelOneBox.getTranslateX() + 1);
                } else if (ModelUtils.twoSelected == true) {
                    Models.gPanelTwoBox.setTranslateX(Models.gPanelTwoBox.getTranslateX() + 1);
                }
            }

            if (keycode == KeyCode.LEFT) { //Move selected ground panel to the left in the screen
                if (ModelUtils.oneSelected == true) {
                    Models.gPanelOneBox.setTranslateX(Models.gPanelOneBox.getTranslateX() - 1);
                } else if (ModelUtils.twoSelected == true) {
                    Models.gPanelTwoBox.setTranslateX(Models.gPanelTwoBox.getTranslateX() - 1);
                }
            }

            if (keycode == KeyCode.UP) { // Move selected ground panel back
                if (ModelUtils.oneSelected == true) {
                    Models.gPanelOneBox.setTranslateZ(Models.gPanelOneBox.getTranslateZ() + 1);
                } else if (ModelUtils.twoSelected == true) {
                    Models.gPanelTwoBox.setTranslateZ(Models.gPanelTwoBox.getTranslateZ() + 1);
                }
            }

            if (keycode == KeyCode.DOWN) { // Move selected ground panel forward
                if (ModelUtils.oneSelected == true) {
                    Models.gPanelOneBox.setTranslateZ(Models.gPanelOneBox.getTranslateZ() - 1);
                } else if (ModelUtils.twoSelected == true) {
                    Models.gPanelTwoBox.setTranslateZ(Models.gPanelTwoBox.getTranslateZ() - 1);
                }
            }

            if (keycode == KeyCode.SPACE) {  //Rotate selected ground solar panel
                if (ModelUtils.oneSelected == true) {
                    t = t.createConcatenation(n);
                    Models.gPanelOneBox.getTransforms().addAll(t);
                } else if (ModelUtils.twoSelected == true) {
                    t = t.createConcatenation(n1);
                    Models.gPanelTwoBox.getTransforms().addAll(t);
                }
            }

            if (delta != null) {
                Point3D delta2 = camera.localToParent(delta);
                cameraDolly.setTranslateX(cameraDolly.getTranslateX() + delta2.getX());
                cameraDolly.setTranslateY(cameraDolly.getTranslateY() + delta2.getY());
                cameraDolly.setTranslateZ(cameraDolly.getTranslateZ() + delta2.getZ());

            }
        });

        // Use mouse to control camera rotation
        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });

        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            yRotate.setAngle(((yRotate.getAngle() - mouseDeltaX * 0.2) % 360 + 540) % 360 - 180); // +
            xRotate.setAngle(((xRotate.getAngle() + mouseDeltaY * 0.2) % 360 + 540) % 360 - 180); // -
        });

        stage.setTitle("Skybox");
        stage.setScene(scene);
        stage.show();
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


    public void showControls() {
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

    public void showSunTimes() {

        String sunTimes =
                "On " + TimeUtils.date.toString() + "\n"
                        + "at latitude: " + TimeUtils.latitude + ", and longitude: " + TimeUtils.longitude + ":\n"
                        + "\n"
                        + "Sunrise: " + TimeUtils.sunriseTime + "\n"
                        + "Sunset: " + TimeUtils.sunsetTime;

        JOptionPane.showMessageDialog(null, sunTimes, "Sunrise/Sunset Times", JOptionPane.PLAIN_MESSAGE);
    }

    public void showPP() {
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


    public void getLocation() {
        TimeUtils.theLocation = TimeUtils.locationPicker.getText();
        String[] coords = TimeUtils.theLocation.split(",");
        TimeUtils.latitude = Double.parseDouble(coords[0]);
        TimeUtils.longitude = Double.parseDouble(coords[1]);

        SunUtils.recalculateSunTimes();
    }

    private void uiBuild(){
        //Top UI
        TimeUtils.datePicker.setLayoutX(14.0);
        TimeUtils.datePicker.setLayoutY(13.0);
        TimeUtils.datePicker.setPrefHeight(25.0);
        TimeUtils.datePicker.setPrefWidth(166.0);
        TimeUtils.datePicker.setPromptText("03/10/2022");
        TimeUtils.datePicker.setOnAction(e->{
            try {
                TimeUtils.getDate();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        });

        slider.setLayoutX(193.0);
        slider.setLayoutY(7.0);
        slider.setMajorTickUnit(1.0);
        slider.setMax(12.0);
        slider.setMinorTickCount(1);
        slider.setPrefHeight(38.0);
        slider.setPrefWidth(590.0);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        slider.setValue(6.0);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                SunUtils.sunMovement();
            }
        });

        ToolBar sliderAndDate = new ToolBar(TimeUtils.datePicker, slider);
        sliderAndDate.setOrientation(Orientation.HORIZONTAL);
        sliderAndDate.setLayoutY(39.0);
        sliderAndDate.setPrefHeight(51.0);
        sliderAndDate.setPrefWidth(1024.0);
        sliderAndDate.setStyle("-fx-background-color: linear-gradient(to right, indigo 20%, red 70%, indigo 100%)");

        pane.setTop(sliderAndDate);


        //Side UI
        Label solarLabel = new Label("Solar Panels");
        solarLabel.setLayoutX(14.0);
        solarLabel.setLayoutY(232.0);
        solarLabel.setPrefHeight(17.0);
        solarLabel.setPrefWidth(149.0);
        solarLabel.setAlignment(Pos.CENTER);

        Label location = new Label("Location");
        location.setLayoutX(13.0);
        location.setLayoutY(98.0);
        location.setPrefHeight(17.0);
        location.setPrefWidth(149.0);
        location.setAlignment(Pos.CENTER);


        TimeUtils.locationPicker.setLayoutX(13.0);
        TimeUtils.locationPicker.setLayoutY(123.0);
        TimeUtils.locationPicker.setText("47.6588, -117.4260");
        TimeUtils.locationPicker.setAlignment(Pos.CENTER);
        TimeUtils.locationPicker.setOnAction(e->{
            getLocation();
        });


        Label time = new Label("Current Time:");
        time.setLayoutX(14.0);
        time.setLayoutY(14.0);
        time.setPrefHeight(17.0);
        time.setPrefWidth(149.0);
        time.setAlignment(Pos.CENTER);

        TimeUtils.currentTimeLabel.setLayoutX(13.0);
        TimeUtils.currentTimeLabel.setLayoutY(41.0);
        TimeUtils.currentTimeLabel.setPrefHeight(17.0);
        TimeUtils.currentTimeLabel.setPrefWidth(149.0);
        TimeUtils.currentTimeLabel.setAlignment(Pos.CENTER);

        Button addAll = new Button("Add All");
        addAll.setOnAction(e->{
            ModelUtils.addAllPanels();
        });

        addAll.setLayoutX(9.0);
        addAll.setLayoutY(265.0);
        addAll.setPrefHeight(38.0);
        addAll.setPrefWidth(75.0);
        addAll.setAlignment(Pos.BASELINE_LEFT);
        addAll.setTextAlignment(TextAlignment.CENTER);

        Button removeAll = new Button("Remove All");
        removeAll.setOnAction(e->{
            ModelUtils.removeAllPanels();
        });

        removeAll.setLayoutX(94.0);
        removeAll.setLayoutY(265.0);
        removeAll.setPrefHeight(38.0);
        removeAll.setPrefWidth(75.0);
        removeAll.setAlignment(Pos.BASELINE_RIGHT);

        Button paybackPeriod = new Button("Payback Period");
        paybackPeriod.setOnAction(e->{
            showPP();
        });

        paybackPeriod.setLayoutX(9.0);
        paybackPeriod.setLayoutY(557.0);
        paybackPeriod.setPrefHeight(38.0);
        paybackPeriod.setPrefWidth(159.0);

        Button optimalPanels = new Button("Optimal Panels");
        optimalPanels.setOnAction(e->{
            ModelUtils.highlightOptimalPanels();
        });

        optimalPanels.setLayoutX(9.0);
        optimalPanels.setLayoutY(456.0);
        optimalPanels.setPrefHeight(38.0);
        optimalPanels.setPrefWidth(159.0);

        Button intensityLevels = new Button("Light Intensity Levels");
        intensityLevels.setOnAction(e->{
            ModelUtils.showIntensityLevels();
        });

        intensityLevels.setLayoutX(9.0);
        intensityLevels.setLayoutY(510.0);
        intensityLevels.setPrefHeight(38.0);
        intensityLevels.setPrefWidth(159.0);

        Button sunTimes = new Button("Sunrise/Sunset Times");
        sunTimes.setOnAction(e->{
            showSunTimes();
        });

        sunTimes.setLayoutX(9.0);
        sunTimes.setLayoutY(404.0);
        sunTimes.setPrefHeight(38.0);
        sunTimes.setPrefWidth(159.0);

        Button controls = new Button("Controls");
        controls.setOnAction(e->{
            showControls();
        });

        controls.setLayoutX(9.0);
        controls.setLayoutY(354.0);
        controls.setPrefHeight(38.0);
        controls.setPrefWidth(159.0);

        Button quit = new Button("Quit");
        quit.setOnAction(e->{
            stage.close();
        });

        quit.setLayoutX(8.0);
        quit.setLayoutY(613.0);
        quit.setPrefHeight(38.0);
        quit.setPrefWidth(159.0);

        Label timeZoneLabel = new Label("Timezone");
        timeZoneLabel.setLayoutX(14.0);
        timeZoneLabel.setLayoutY(163.0);
        timeZoneLabel.setPrefHeight(17.0);
        timeZoneLabel.setPrefWidth(149.0);
        timeZoneLabel.setAlignment(Pos.CENTER);


        TimeUtils.tz.setText("GMT-8");
        TimeUtils.tz.setLayoutX(14.0);
        TimeUtils.tz.setLayoutY(188.0);
        TimeUtils.tz.setAlignment(Pos.CENTER);
        TimeUtils.tz.setOnAction(e->{
            TimeUtils.getTimeZone();
        });


        ToolBar sideUI = new ToolBar(time, TimeUtils.currentTimeLabel, location, TimeUtils.locationPicker, timeZoneLabel, TimeUtils.tz, solarLabel, addAll, removeAll, controls, sunTimes, optimalPanels, intensityLevels, paybackPeriod, quit);
        sideUI.setOrientation(Orientation.VERTICAL);
        sideUI.setLayoutY(89.0);
        sideUI.setPrefHeight(678.0);
        sideUI.setPrefWidth(177.0);
        pane.setLeft(sideUI);
        pane.setPrefSize(600,600);

    }
/*
    public void getTimeZone() {
        theLocation = locationPicker.getText();
        timeZone = tz.getText();
        recalculateSunTimes();
    }

 */


    public static void main(String[] args) {
        launch();
    }
}