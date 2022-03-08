package com.example.solaroptimization;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class SkyBoxController implements Initializable{

    @FXML
    private AnchorPane skyboxPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void setSkyboxPane(AnchorPane skyboxPane) throws ParseException {
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


    //    skybox.setFill(new ImagePattern(SkyBoxApplication.skyboxImage));
    //    camera = new PerspectiveCamera(true);
    //    camera.setNearClip(0.1);
    //    camera.setFarClip(30000.0);


    }

}
