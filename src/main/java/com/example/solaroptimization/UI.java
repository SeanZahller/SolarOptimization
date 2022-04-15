package com.example.solaroptimization;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.text.TextAlignment;
import javax.swing.*;
import java.text.DecimalFormat;
import java.text.ParseException;

import static com.example.solaroptimization.SkyBoxApplication.*;

public class UI {
    static void uiBuild() {
        //Top UI
        TimeUtils.datePicker.setLayoutX(14.0);
        TimeUtils.datePicker.setLayoutY(13.0);
        TimeUtils.datePicker.setPrefHeight(25.0);
        TimeUtils.datePicker.setPrefWidth(166.0);
        TimeUtils.datePicker.setPromptText("03/10/2022");
        TimeUtils.datePicker.setOnAction(e -> {
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
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
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
        Label solarLabel = createLabel("Solar Panels", 14.0, 232.0, 17.0, 149.0);
        Label location = createLabel("Location", 13.0, 98.0, 17.0, 149.0);
        Label time = createLabel("Current Time", 14.0, 14.0, 17.0, 149.0);

        TimeUtils.locationPicker.setLayoutX(13.0);
        TimeUtils.locationPicker.setLayoutY(123.0);
        TimeUtils.locationPicker.setText("47.6588, -117.4260");
        TimeUtils.locationPicker.setAlignment(Pos.CENTER);
        TimeUtils.locationPicker.setOnAction(e -> {
            TimeUtils.getLocation();
        });

        TimeUtils.currentTimeLabel.setLayoutX(13.0);
        TimeUtils.currentTimeLabel.setLayoutY(41.0);
        TimeUtils.currentTimeLabel.setPrefHeight(17.0);
        TimeUtils.currentTimeLabel.setPrefWidth(149.0);
        TimeUtils.currentTimeLabel.setAlignment(Pos.CENTER);

        Button addAll = createButton("Add All", 9.0, 265.0, 38.0, 75.0);
        addAll.setOnAction(e -> {
            ModelUtils.addAllPanels();
        });

        Button removeAll = createButton("Remove All", 94.0, 265.0, 38.0, 75.0);
        removeAll.setOnAction(e -> {
            ModelUtils.removeAllPanels();
        });

        Button paybackPeriod = createButton("Payback Period", 9.0, 557.0, 38.0, 159.0);
        paybackPeriod.setOnAction(e -> {
            showPP();
        });

        Button optimalPanels = createButton("Optimal Panels", 9.0, 456.0, 38.0, 159.0);
        optimalPanels.setOnAction(e -> {
            ModelUtils.highlightOptimalPanels();
        });

        Button intensityLevels = createButton("Light Intensity Levels", 9.0, 510.0, 38.0, 159.0);
        intensityLevels.setOnAction(e -> {
            ModelUtils.showIntensityLevels();
        });

        Button sunTimes = createButton("Sunrise/Sunset Times", 9.0, 404.0, 38.0, 159.0);
        sunTimes.setOnAction(e -> {
            showSunTimes();
        });

        Button controls = createButton("Controls", 9.0, 354.0, 38.0, 159.0);
        controls.setOnAction(e -> {
            showControls();
        });

        Button quit = createButton("Quit", 9.0, 613.0, 38.0, 159.0);
        quit.setOnAction(e -> {
            stage.close();
        });

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
        TimeUtils.tz.setOnAction(e -> {
            TimeUtils.getTimeZone();
        });

        ToolBar sideUI = new ToolBar(time, TimeUtils.currentTimeLabel, location, TimeUtils.locationPicker, timeZoneLabel, TimeUtils.tz, solarLabel, addAll, removeAll, controls, sunTimes, optimalPanels, intensityLevels, paybackPeriod, quit);
        sideUI.setOrientation(Orientation.VERTICAL);
        sideUI.setLayoutY(89.0);
        sideUI.setPrefHeight(678.0);
        sideUI.setPrefWidth(177.0);
        pane.setLeft(sideUI);
        pane.setPrefSize(600, 600);

    }

    public static void showControls() {
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

    public static void showSunTimes() {

        String sunTimes =
                "On " + TimeUtils.date.toString() + "\n"
                        + "at latitude: " + TimeUtils.latitude + ", and longitude: " + TimeUtils.longitude + ":\n"
                        + "\n"
                        + "Sunrise: " + TimeUtils.sunriseTime + "\n"
                        + "Sunset: " + TimeUtils.sunsetTime;

        JOptionPane.showMessageDialog(null, sunTimes, "Sunrise/Sunset Times", JOptionPane.PLAIN_MESSAGE);
    }

    public static void showPP() {
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

    public static Button createButton(String name, double xLayout, double yLayout, double prefHeight, double prefWidth)
    {
        Button tempButton = new Button(name);
        tempButton.setLayoutX(xLayout);
        tempButton.setLayoutY(yLayout);
        tempButton.setPrefHeight(prefHeight);
        tempButton.setPrefWidth(prefWidth);

        tempButton.setTextAlignment(TextAlignment.CENTER);

        return tempButton;
    }

    public static Label createLabel(String name, double xLayout, double yLayout, double prefHeight, double prefWidth)
    {
        Label tempLabel = new Label(name);
        tempLabel.setLayoutX(xLayout);
        tempLabel.setLayoutY(yLayout);
        tempLabel.setPrefHeight(prefHeight);
        tempLabel.setPrefWidth(prefWidth);

        tempLabel.setTextAlignment(TextAlignment.CENTER);
        tempLabel.setAlignment(Pos.CENTER);

        return tempLabel;
    }
}
