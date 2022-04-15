package com.example.solaroptimization;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;

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
        TimeUtils.startParams();
        SkyBoxUtils.constructWorld(sceneRoot);
        UI.uiBuild();

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

    public static void main(String[] args) {
        launch();
    }
}