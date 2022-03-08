package com.example.solaroptimization;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;

import java.text.ParseException;

public class SkyBoxController {

    @FXML
    private AnchorPane skyboxPane;

    public SkyBoxController(AnchorPane skyboxPane){

        this.skyboxPane = skyboxPane; //I Dont know if this is connecting to the FXML or not
    }

    @FXML
    private void initialize() throws ParseException {
        setSkyboxPane();
    }


    @FXML
    protected void setSkyboxPane() throws ParseException {
        Group skybox = new Group();
        SkyBoxApplication.constructWorld(skybox); // Construct the empty SkyBox group

        Group sun = SkyBoxApplication.sunCreation(); //creating sun
        SkyBoxApplication.startParams(); // Setting start date, location, sunset/sunrise times
        Group panelsWHouse = SkyBoxApplication.models(); //Creating all models for the scene

        skybox.getChildren().addAll(sun, panelsWHouse);

        skyboxPane.getChildren().add(skybox);

        //Heres where we do the set up camera and background?
        //Could we move it into constructWorld?
        //How to convert the things happening from a Scene to A AnchorPane or how to add a new scene into the Pane?

        /*
        skybox.setFill(new ImagePattern(SkyBoxApplication.skyboxImage));
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(30000.0);

         */




    }


}