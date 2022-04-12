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

    //Model Import Declaration
    private static final File house = new File("C:\\House.3ds");
    private static final File solarPanel = new File("C:\\SolarPanel(Export).3ds");
    private static final File groundSolarPanel = new File("C:\\GroundSolarPanel.3ds");
    private static Group gPanelOne;
    private static Group gPanelTwo;
    private static Group houseImport;
    static Group solarPanelOnewR;
    static Group solarPanelTwowR;
    static Group solarPanelThreewR;
    static Group solarPanelFourwR;
    static Group gPanelOneBox;
    static Group gPanelTwoBox;
    static Group panelsWHouse;
    static Group sun = new Group();
    private Boolean oneSelected = false;
    private Boolean twoSelected = false;
    static PhongMaterial clear = new PhongMaterial(Color.TRANSPARENT);
    private static Boolean pressed = false;
    private static Boolean pressedTwo = false;
    static boolean box1closest;
    static boolean box2closest;
    static boolean box3closest;
    static boolean box4closest;
    static boolean gbox1closest;
    static boolean gbox2closest;
    static PhongMaterial optimal = new PhongMaterial(Color.GREEN);
    static PhongMaterial subOptimal = new PhongMaterial(Color.RED);

    private static String currentTime;
    private static String convertedSunrise;
    private static String convertedSunset;
    private static Boolean pm = false;
    private static Label currentTimeLabel = new Label();
    private static Slider slider = new Slider();
    static String sunriseTime;
    static String sunsetTime;

    //Location and Dates
    static Calendar cal;
    static String theDate = "20220310";
    static String theLocation;
    static String timeZone = "GMT-8";
    static Location location;
    static Double latitude = 47.6588;
    static Double longitude = -117.4260;
    static Date date;
    static DatePicker datePicker = new DatePicker();
    static TextField locationPicker = new TextField();
    static BorderPane pane = new BorderPane();
    static Stage stage;
    static TextField tz = new TextField();


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
        constructWorld(sceneRoot);
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

            setCenters(r, houseImport); //Get centers to rotate from center
            setCenters(l, houseImport);
            setCenters(n, gPanelOne);
            setCenters(n1, gPanelTwo);

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
                panelsWHouse.getTransforms().addAll(t);
            }
            if (keycode == KeyCode.N) { //Rotate house and all solar panels Left
                t = t.createConcatenation(r);
                panelsWHouse.getTransforms().addAll(t);
            }
            if (keycode == KeyCode.DIGIT0) { //Clears selected panels
                clearSelected();
            }
            if (keycode == KeyCode.DIGIT1) { // Selects ground panel number 1
                gPanelOneSelected();
            }
            if (keycode == KeyCode.DIGIT2) //selects ground panel number 2
            {
                gPanelTwoSelected();
            }
            if (keycode == KeyCode.RIGHT) { //Move selected ground panel to the right in the screen
                if (oneSelected == true) {
                    gPanelOneBox.setTranslateX(gPanelOneBox.getTranslateX() + 1);
                } else if (twoSelected == true) {
                    gPanelTwoBox.setTranslateX(gPanelTwoBox.getTranslateX() + 1);
                }
            }

            if (keycode == KeyCode.LEFT) { //Move selected ground panel to the left in the screen
                if (oneSelected == true) {
                    gPanelOneBox.setTranslateX(gPanelOneBox.getTranslateX() - 1);
                } else if (twoSelected == true) {
                    gPanelTwoBox.setTranslateX(gPanelTwoBox.getTranslateX() - 1);
                }
            }

            if (keycode == KeyCode.UP) { // Move selected ground panel back
                if (oneSelected == true) {
                    gPanelOneBox.setTranslateZ(gPanelOneBox.getTranslateZ() + 1);
                } else if (twoSelected == true) {
                    gPanelTwoBox.setTranslateZ(gPanelTwoBox.getTranslateZ() + 1);
                }
            }

            if (keycode == KeyCode.DOWN) { // Move selected ground panel forward
                if (oneSelected == true) {
                    gPanelOneBox.setTranslateZ(gPanelOneBox.getTranslateZ() - 1);
                } else if (twoSelected == true) {
                    gPanelTwoBox.setTranslateZ(gPanelTwoBox.getTranslateZ() - 1);
                }
            }

            if (keycode == KeyCode.SPACE) {  //Rotate selected ground solar panel
                if (oneSelected == true) {
                    t = t.createConcatenation(n);
                    gPanelOneBox.getTransforms().addAll(t);
                } else if (twoSelected == true) {
                    t = t.createConcatenation(n1);
                    gPanelTwoBox.getTransforms().addAll(t);
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


    //------------------------Helper Methods----------------------------------------//
    private static Group setHouse() {
        TdsModelImporter modelImporter = new TdsModelImporter(); //Model Importer

        modelImporter.read(house); //Read in the house model
        Node[] oneStoryHouse = modelImporter.getImport(); //create House object with Node[]
        modelImporter.clear(); // clear the importer

        for (Node node : oneStoryHouse) {
            node.setScaleX(1);
            node.setScaleY(1);
            node.setScaleZ(1);
            node.getTransforms().setAll(new Rotate(25, Rotate.Y_AXIS), new Rotate(-90, Rotate.X_AXIS));
            node.setTranslateX(0); // These place the house towards the ground and to the right of the view
            node.setTranslateY(200); // ^^^^^^^^^^^^^^^
        }
        houseImport = new Group(oneStoryHouse); //create new group with the house
        return houseImport;
    }

    private static Group setAllSolarPanels(File solar, int pX, int pY, int pZ, int AY, int AX, int AZ) //----Model Helper Method----//
    {
        TdsModelImporter modelImporter = new TdsModelImporter(); //Model Importer
        modelImporter.read(solar);
        Node[] model = modelImporter.getImport();

        for (Node node : model) {
            node.setScaleX(1);
            node.setScaleY(1);
            node.setScaleZ(1);                                                         //Slope of roof
            node.getTransforms().setAll(new Rotate(AY, Rotate.Y_AXIS), new Rotate(AX, Rotate.X_AXIS), new Rotate(AZ, Rotate.Z_AXIS));
            node.setTranslateX(pX); // Move right or left
            node.setTranslateY(pY); // Move Up or down ... Height of roof
            node.setTranslateZ(pZ); // Move forward or backward
        }
        Group panel = new Group(model);
        return panel;
    }

    private static Box createsolar(Group group1, double height, double depth, double width, double rax, double raz, double ray) {
        Box box = new Box();
        Bounds cord = group1.getBoundsInLocal();
        box.getTransforms().setAll(new Rotate(ray, Rotate.Y_AXIS), new Rotate(rax, Rotate.X_AXIS), new Rotate(raz, Rotate.Z_AXIS));
        box.setTranslateX(cord.getCenterX());
        box.setTranslateZ(cord.getCenterZ());
        box.setTranslateY(cord.getCenterY());
        box.setHeight(height);
        box.setDepth(depth);
        box.setWidth(width);
        box.setMaterial(clear);
        return box;
    }

    private void setCenters(Rotate r, Group beingRotated) {
        r.setPivotX(beingRotated.getBoundsInLocal().getCenterX());
        r.setPivotY(beingRotated.getBoundsInLocal().getCenterY());
        r.setPivotZ(beingRotated.getBoundsInLocal().getCenterZ());
    }

    private void gPanelOneSelected() {
        oneSelected = true;
        twoSelected = false;
    }

    private void gPanelTwoSelected() {
        oneSelected = false;
        twoSelected = true;
    }

    private void clearSelected() {
        oneSelected = false;
        twoSelected = false;
    }

    static void models() {

        int rightSideAngles[] = {-68, -68, 0};

        int panelOneCoordinates[] = {300, -74, 190};
        int panelTwoCoordinates[] = {395, -74, 400};

        int leftSideAngles[] = {-68, -113, 0};
        int panelThreeCoordinates[] = {190, -43, 250};
        int panelFourCoordinates[] = {275, -43, 440};

        int gPanelOneCoordinates[] = {0, 180, 190};
        int gPanelTwoCoordinates[] = {460, 180, 100};
        int gPanelLeftAngles[] = {115, -90, 0};
        int gPanelRightAngles[] = {-65, -90, 0};
//                                105
        //Sets house and panels into scene
        Group houseImport = setHouse();
        Group solarPanelOne = setAllSolarPanels(solarPanel, panelOneCoordinates[0], panelOneCoordinates[1], panelOneCoordinates[2], rightSideAngles[0], rightSideAngles[1], rightSideAngles[2]); //4 roof panels
        Group solarPanelTwo = setAllSolarPanels(solarPanel, panelTwoCoordinates[0], panelTwoCoordinates[1], panelTwoCoordinates[2], rightSideAngles[0], rightSideAngles[1], rightSideAngles[2]);
        Group solarPanelThree = setAllSolarPanels(solarPanel, panelThreeCoordinates[0], panelThreeCoordinates[1], panelThreeCoordinates[2], leftSideAngles[0], leftSideAngles[1], leftSideAngles[2]);
        Group solarPanelFour = setAllSolarPanels(solarPanel, panelFourCoordinates[0], panelFourCoordinates[1], panelFourCoordinates[2], leftSideAngles[0], leftSideAngles[1], leftSideAngles[2]);
        gPanelOne = setAllSolarPanels(groundSolarPanel, gPanelOneCoordinates[0], gPanelOneCoordinates[1], gPanelOneCoordinates[2], gPanelLeftAngles[0], gPanelLeftAngles[1], gPanelLeftAngles[2]); //2 ground panels
        gPanelTwo = setAllSolarPanels(groundSolarPanel, gPanelTwoCoordinates[0], gPanelTwoCoordinates[1], gPanelTwoCoordinates[2], gPanelRightAngles[0], gPanelRightAngles[1], gPanelRightAngles[2]);

        //sets boxes with panels
        Box boxers = createsolar(solarPanelOne, 39, 3.64, 65, rightSideAngles[1], rightSideAngles[2], rightSideAngles[0]);
        Box boxers2 = createsolar(solarPanelTwo, 39, 3.64, 65, rightSideAngles[1], rightSideAngles[2], rightSideAngles[0]);
        Box boxers3 = createsolar(solarPanelThree, 39, 3.64, 65, -rightSideAngles[1], -rightSideAngles[2], rightSideAngles[0]);
        Box boxers4 = createsolar(solarPanelFour, 39, 3.64, 65, -rightSideAngles[1], -rightSideAngles[2], rightSideAngles[0]);
        Box boxers5 = createsolar(gPanelOne, 39, 3.64, 130, 55, 0, -65);
        Box boxers6 = createsolar(gPanelTwo, 39, 3.64, 130, -55, 0, -65);

        //Grouping together solar panel w/ respective box
        solarPanelOnewR = new Group(solarPanelOne, boxers);
        solarPanelTwowR = new Group(solarPanelTwo, boxers2);
        solarPanelThreewR = new Group(solarPanelThree, boxers3);
        solarPanelFourwR = new Group(solarPanelFour, boxers4);
        gPanelOneBox = new Group(gPanelOne, boxers5);
        gPanelTwoBox = new Group(gPanelTwo, boxers6);

        panelsWHouse = new Group(houseImport, solarPanelOnewR, solarPanelTwowR, solarPanelThreewR, solarPanelFourwR, gPanelOneBox, gPanelTwoBox);
    }

    static void sunCreation() {
        Sphere sphere = new Sphere(80.0f);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.YELLOW);
        sphere.setMaterial(material);

        // create a point light
        PointLight pointlight = new PointLight();

        // create a Group
        sun.getChildren().addAll(sphere, pointlight);

        sphere.setTranslateX(100);
        sphere.setTranslateY(-200);

        pointlight.setTranslateZ(-1000);
        pointlight.setTranslateX(+1000);
        pointlight.setTranslateY(+10);
        pointlight.setColor(Color.rgb(255, 255, 255));

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

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 0.5)
        {
            angle = 7.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 1.0)
        {
            angle = 15;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 1.5)
        {
            angle = 22.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 2.0)
        {
            angle = 30;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 2.5) {
            angle = 37.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 3.0)
        {
            angle = 45;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 3.5)
        {
            angle = 52.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 4.0)
        {
            angle = 60;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 4.5)
        {
            angle = 67.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 5.0)
        {
            angle = 75;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 5.5)
        {
            angle = 82.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 6.0)
        {
            angle = 90;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 6.5)
        {
            angle = 97.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 7.0)
        {
            angle = 105;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 7.5)
        {
            angle = 112.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 8.0)
        {
            angle = 120;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 8.5)
        {
            angle = 127.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 9.0)
        {
            angle = 135;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 9.5)
        {
            angle = 142.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 10.0)
        {
            angle = 150;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 10.5)
        {
            angle = 157.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 11.0)
        {
            angle = 165;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 11.5)
        {
            angle = 172.5;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 12.0)
        {
            angle = 178;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 12.5)
        {
            angle = 179;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }
        else if(sliderValue == 13)
        {
            angle = 180;
            angleRadians = Math.toRadians(angle);
            x = Math.cos(angleRadians);
            y = Math.sin(angleRadians);

            sun.setTranslateX(x * 1000);
            sun.setTranslateY(-y * 1000);
            sun.setTranslateZ(0);
        }

    }

    //helper methods for most optimal
    public static double distancecalc(Box box, Group sun) {
        Point3D point1 = new Point3D(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
        Point3D point2 = new Point3D(sun.getTranslateX(), sun.getTranslateY(), sun.getTranslateZ());
        Double distance = Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2) + Math.pow(point1.getZ() - point2.getZ(), 2));
        return distance;
    }

    static void startParams() throws ParseException {
        //Choosing Spokane 3/10/22 as starting place and time
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd"); //Formatter
        date = formatter.parse(theDate); //Parse string to create Date object
        cal = Calendar.getInstance(); //Calendar object created
        cal.setTime(date); //Calender object given corresponding date

        location = new Location(latitude.doubleValue(), longitude.doubleValue()); // Will be entered in coordinates
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, timeZone); // Creates calculator for sun times

        sunriseTime = calculator.getOfficialSunriseForDate(cal); // Gets sunrise based on date and calculator created
        sunsetTime = calculator.getOfficialSunsetForDate(cal); // Gets sunset based on date and calculator created

        //Initialize Slider Ticks
        LocalTime start = LocalTime.parse(sunriseTime);
        LocalTime end =  LocalTime.parse(sunsetTime);
        Long hoursBetweenRiseSet = ChronoUnit.HOURS.between(start, end);
        slider.setMax(hoursBetweenRiseSet + 1);

        //Initialize currentTime and current sun position with the time
        changeTime(6, 0);
        sunTrajectory(6.0);
    }

    public void highlightOptimalPanels() {

        if(pressed == false)
        {
            colorSetOpt(); //Need to figure out a way to toggle on/off. Just toggles on
            pressed = true;
        }
        else if(pressed = true) {
            ((Box) solarPanelOnewR.getChildren().get(1)).setMaterial(clear);
            ((Box) solarPanelTwowR.getChildren().get(1)).setMaterial(clear);
            ((Box) solarPanelThreewR.getChildren().get(1)).setMaterial(clear);
            ((Box) solarPanelFourwR.getChildren().get(1)).setMaterial(clear);
            ((Box) gPanelOneBox.getChildren().get(1)).setMaterial(clear);
            ((Box) gPanelTwoBox.getChildren().get(1)).setMaterial(clear);

            pressed = false;
        }
    }

    public static void colorSetOpt() {
        double total = 0.0;
        double averageP1 = 0.0;
        double averageP2 = 0.0;
        double averageP3 = 0.0;
        double averageP4 = 0.0;
        double averageGP1 = 0.0;
        double averageGP2 = 0.0;
        double[] totalHours = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0};

        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) solarPanelOnewR.getChildren().get(1), sun);
        }
        averageP1 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) solarPanelTwowR.getChildren().get(1), sun);
        }
        averageP2 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) solarPanelThreewR.getChildren().get(1), sun);
        }
        averageP3 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) solarPanelFourwR.getChildren().get(1), sun);
        }
        averageP4 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) gPanelOneBox.getChildren().get(1), sun);
        }
        averageGP1 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) gPanelTwoBox.getChildren().get(1), sun);
        }
        averageGP2 = total / 12;

        box1closest = false;
        box2closest = false;
        box3closest = false;
        box4closest = false;
        gbox1closest = false;
        gbox2closest = false;

        if (averageP1 > averageP2 && averageP1 > averageP3 && averageP1 > averageP4) {
            box1closest = true;
            box2closest = false;
            box3closest = false;
            box4closest = false;
        }
        if (averageP2 > averageP1 && averageP2 > averageP3 && averageP2 > averageP4) {
            box1closest = false;
            box2closest = true;
            box3closest = false;
            box4closest = false;
        }
        if (averageP3 > averageP1 && averageP3 > averageP2 && averageP3 > averageP4) {
            box1closest = false;
            box2closest = false;
            box3closest = true;
            box4closest = false;
        }
        if (averageP4 > averageP1 && averageP4 > averageP2 && averageP4 > averageP3) {
            box1closest = false;
            box2closest = false;
            box3closest = false;
            box4closest = true;
        }
        if (averageGP1 > averageGP2) {
            gbox1closest = true;
            gbox2closest = false;
        }
        if (averageGP2 > averageGP1) {
            gbox1closest = false;
            gbox2closest = true;
        }

        if (box1closest = true) {
            ((Box) solarPanelOnewR.getChildren().get(1)).setMaterial(optimal);
            ((Box) solarPanelTwowR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelThreewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelFourwR.getChildren().get(1)).setMaterial(subOptimal);
        }
        if (box2closest = true) {
            ((Box) solarPanelOnewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelTwowR.getChildren().get(1)).setMaterial(optimal);
            ((Box) solarPanelThreewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelFourwR.getChildren().get(1)).setMaterial(subOptimal);

        }
        if (box3closest = true) {
            ((Box) solarPanelOnewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelTwowR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelThreewR.getChildren().get(1)).setMaterial(optimal);
            ((Box) solarPanelFourwR.getChildren().get(1)).setMaterial(subOptimal);
        }
        if (box4closest = true) {
            ((Box) solarPanelOnewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelTwowR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelThreewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) solarPanelFourwR.getChildren().get(1)).setMaterial(optimal);
        }
        if (gbox1closest = true) {
            ((Box) gPanelOneBox.getChildren().get(1)).setMaterial(optimal);
            ((Box) gPanelTwoBox.getChildren().get(1)).setMaterial(subOptimal);
        }
        if (gbox2closest = true) {
            ((Box) gPanelOneBox.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) gPanelTwoBox.getChildren().get(1)).setMaterial(optimal);
        }
    }

    static double calculateLightIntesity(Box box, Group Sun){
        double distance = Math.abs(distancecalc(box, Sun));
        double intesity = 1/((distance)*(distance));
        intesity = intesity*10000000;
        return intesity;
    }

    public void showIntensityLevels() {
        double solarP1Intensity, solarP2Intensity, solarP3Intensity, solarP4Intensity, solarGP1Intensity, solarGP2Intensity;
        solarP1Intensity = calculateLightIntesity((Box) solarPanelOnewR.getChildren().get(1), sun);
        solarP2Intensity = calculateLightIntesity((Box) solarPanelTwowR.getChildren().get(1), sun);
        solarP3Intensity = calculateLightIntesity((Box) solarPanelThreewR.getChildren().get(1), sun);
        solarP4Intensity = calculateLightIntesity((Box) solarPanelFourwR.getChildren().get(1), sun);
        solarGP1Intensity = calculateLightIntesity((Box) gPanelOneBox.getChildren().get(1), sun);
        solarGP2Intensity = calculateLightIntesity((Box) gPanelTwoBox.getChildren().get(1), sun);

        if(pressedTwo == false)
        {
            getLevels(solarP1Intensity, (Box) solarPanelOnewR.getChildren().get(1));
            getLevels(solarP2Intensity, (Box) solarPanelTwowR.getChildren().get(1));
            getLevels(solarP3Intensity, (Box) solarPanelThreewR.getChildren().get(1));
            getLevels(solarP4Intensity, (Box) solarPanelFourwR.getChildren().get(1));
            getLevels(solarGP1Intensity, (Box) gPanelOneBox.getChildren().get(1));
            getLevels(solarGP2Intensity, (Box) gPanelTwoBox.getChildren().get(1));
            pressedTwo = true;
        }

        else if(pressedTwo == true) {
            ((Box) solarPanelOnewR.getChildren().get(1)).setMaterial(clear);
            ((Box) solarPanelTwowR.getChildren().get(1)).setMaterial(clear);
            ((Box) solarPanelThreewR.getChildren().get(1)).setMaterial(clear);
            ((Box) solarPanelFourwR.getChildren().get(1)).setMaterial(clear);
            ((Box) gPanelOneBox.getChildren().get(1)).setMaterial(clear);
            ((Box) gPanelTwoBox.getChildren().get(1)).setMaterial(clear);

            pressedTwo = false;
        }


    }

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

    public void addAllPanels() {

        if(panelsWHouse.getChildren().size() < 2) {
            panelsWHouse.getChildren().add(solarPanelOnewR);
            panelsWHouse.getChildren().add(solarPanelTwowR);
            panelsWHouse.getChildren().add(solarPanelThreewR);
            panelsWHouse.getChildren().add(solarPanelFourwR);
            panelsWHouse.getChildren().add(gPanelOneBox);
            panelsWHouse.getChildren().add(gPanelTwoBox);
        }
    }

    public void removeAllPanels() {

        if(panelsWHouse.getChildren().contains(solarPanelThreewR)) {
            panelsWHouse.getChildren().remove(6);
            panelsWHouse.getChildren().remove(5);
            panelsWHouse.getChildren().remove(4);
            panelsWHouse.getChildren().remove(3);
            panelsWHouse.getChildren().remove(2);
            panelsWHouse.getChildren().remove(1);
        }
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
                "On " + date.toString() + "\n"
                        + "at latitude: " + latitude + ", and longitude: " + longitude + ":\n"
                        + "\n"
                        + "Sunrise: " + sunriseTime + "\n"
                        + "Sunset: " + sunsetTime;

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

    //Methods for buttons
    public void getDate() throws ParseException {
        String newDate = datePicker.getValue().toString();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //Formatter
        date = formatter.parse(newDate);

        recalculateSunTimes();
    }

    public void getLocation() {
        theLocation = locationPicker.getText();
        String[] coords = theLocation.split(",");
        latitude = Double.parseDouble(coords[0]);
        longitude = Double.parseDouble(coords[1]);

        recalculateSunTimes();
    }

    private void recalculateSunTimes() {

        cal.clear();
        cal.setTime(date); //Calender object given corresponding date

        location = new Location(latitude, longitude); // Will be entered in coordinates
        System.out.println(location.getLatitude());
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, timeZone); // Creates calculator for sun times
        System.out.println(timeZone.toString());

        sunriseTime = calculator.getOfficialSunriseForDate(cal); // Gets sunrise based on date and calculator created
        sunsetTime = calculator.getOfficialSunsetForDate(cal); // Gets sunset based on date and calculator created

        //Initialize currentTime and current sun position with the time
        changeTime(6, 0);
        sunTrajectory(6.0);

    }

    private void constructWorld(Group root) {

        AmbientLight light = new AmbientLight(Color.rgb(160, 160, 160));

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.FORESTGREEN);
        greenMaterial.setSpecularColor(Color.LIMEGREEN);
        Box xAxis = new Box(500, 10, 10);
        xAxis.setMaterial(greenMaterial);
        Box yAxis = new Box(10, 200, 10);
        yAxis.setMaterial(greenMaterial);
        Box zAxis = new Box(10, 10, 200);
        zAxis.setMaterial(greenMaterial);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        redMaterial.setSpecularColor(Color.TOMATO);
        final Sphere sphere = new Sphere(30);
        sphere.setMaterial(redMaterial);

        sphere.setTranslateX(150);

        final PhongMaterial yellowMaterial = new PhongMaterial();
        yellowMaterial.setDiffuseColor(Color.rgb(200, 200, 0));
        // yellowMaterial.setDiffuseColor(Color.YELLOW);
        // yellowMaterial.setSpecularColor(Color.WHITE);
        final Sphere sphere2 = new Sphere(30);
        sphere2.setMaterial(yellowMaterial);
        // sphere2.setDrawMode(DrawMode.LINE);

        sphere2.setTranslateX(110);

        // Example from JavaFX for Dummies
        TriangleMesh pyramidMesh = new TriangleMesh();
        // define (a trivial) texture map
        pyramidMesh.getTexCoords().addAll(
                0.5f, 0,
                0, 0.5f,
                1, 0.5f,
                0, 1,
                1, 1
        );
        // define vertices
        float h = 100;                    // Height
        float s = 200;                    // Base hypotenuse
        pyramidMesh.getPoints().addAll(
                0, 0, 0,            // Point 0 - Top
                0, h, -s / 2,         // Point 1 - Front
                -s / 2, h, 0,            // Point 2 - Left
                s / 2, h, 0,            // Point 3 - Right
                0, h, s / 2           // Point 4 - Back
        );
        // define faces
        pyramidMesh.getFaces().addAll(
                0, 0, 2, 1, 1, 2,          // Front left face
                0, 0, 1, 1, 3, 1,          // Front right face
                0, 0, 3, 1, 4, 2,          // Back right face
                0, 0, 4, 1, 2, 2,          // Back left face
                4, 1, 1, 4, 2, 2,          // Bottom left face
                4, 1, 3, 3, 1, 4           // Bottom right face
        );
        pyramidMesh.getFaceSmoothingGroups().addAll(
                1, 2, 3, 4, 5, 5);
        MeshView pyramid = new MeshView(pyramidMesh);
        //pyramid.setDrawMode(DrawMode.LINE);
        final PhongMaterial pyrMaterial = new PhongMaterial();
        //pyrMaterial.setDiffuseMap(new Image("pyr_tex.png")); //TODO missing this image, need to determine the diffuse map
        pyrMaterial.setDiffuseColor(Color.BLUE);
        pyrMaterial.setSpecularColor(Color.WHITE);
        pyramid.setMaterial(pyrMaterial);
        pyramid.setTranslateX(-50);
        pyramid.setTranslateY(-200);
        pyramid.setTranslateZ(0);
        //  root.getChildren().add(pyramid);


        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);
        blueMaterial.setSpecularColor(Color.WHITE);
        Box box = new Box(40, 60, 80);
        box.setMaterial(blueMaterial);

        box.setTranslateX(-30);
        box.setTranslateY(-20);
        box.setTranslateZ(-20);


        root.getChildren().add(light);

        Image back = new Image("skyboxDesert.png");
        final PhongMaterial skyMaterial = new PhongMaterial();
        skyMaterial.setDiffuseMap(back);
        Box skybox = new Box(10000, 10000, 10000);
        skybox.setMaterial(skyMaterial);
        skybox.setCullFace(CullFace.NONE);
        //root.getChildren().add(skybox);
        models();
        sunCreation();
        root.getChildren().addAll(skybox, sun, panelsWHouse);

    }

    private void uiBuild(){
        //Top UI
        datePicker.setLayoutX(14.0);
        datePicker.setLayoutY(13.0);
        datePicker.setPrefHeight(25.0);
        datePicker.setPrefWidth(166.0);
        datePicker.setPromptText("03/10/2022");
        datePicker.setOnAction(e->{
            try {
                getDate();
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
                sunMovement();
            }
        });

        ToolBar sliderAndDate = new ToolBar(datePicker, slider);
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


        locationPicker.setLayoutX(13.0);
        locationPicker.setLayoutY(123.0);
        locationPicker.setText("47.6588, -117.4260");
        locationPicker.setAlignment(Pos.CENTER);
        locationPicker.setOnAction(e->{
            getLocation();
        });


        Label time = new Label("Current Time:");
        time.setLayoutX(14.0);
        time.setLayoutY(14.0);
        time.setPrefHeight(17.0);
        time.setPrefWidth(149.0);
        time.setAlignment(Pos.CENTER);

        currentTimeLabel.setLayoutX(13.0);
        currentTimeLabel.setLayoutY(41.0);
        currentTimeLabel.setPrefHeight(17.0);
        currentTimeLabel.setPrefWidth(149.0);
        currentTimeLabel.setAlignment(Pos.CENTER);

        Button addAll = new Button("Add All");
        addAll.setOnAction(e->{
            addAllPanels();
        });

        addAll.setLayoutX(9.0);
        addAll.setLayoutY(265.0);
        addAll.setPrefHeight(38.0);
        addAll.setPrefWidth(75.0);

        Button removeAll = new Button("Remove All");
        removeAll.setOnAction(e->{
            removeAllPanels();
        });

        removeAll.setLayoutX(94.0);
        removeAll.setLayoutY(265.0);
        removeAll.setPrefHeight(38.0);
        removeAll.setPrefWidth(75.0);

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
            highlightOptimalPanels();
        });

        optimalPanels.setLayoutX(9.0);
        optimalPanels.setLayoutY(456.0);
        optimalPanels.setPrefHeight(38.0);
        optimalPanels.setPrefWidth(159.0);

        Button intensityLevels = new Button("Light Intensity Levels");
        intensityLevels.setOnAction(e->{
            showIntensityLevels();
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


        tz.setText("GMT-8");
        tz.setLayoutX(14.0);
        tz.setLayoutY(188.0);
        tz.setAlignment(Pos.CENTER);
        tz.setOnAction(e->{
            getTimeZone();
        });


        ToolBar sideUI = new ToolBar(time, currentTimeLabel, location, locationPicker, timeZoneLabel, tz, solarLabel, addAll, removeAll, controls, sunTimes, optimalPanels, intensityLevels, paybackPeriod, quit);
        sideUI.setOrientation(Orientation.VERTICAL);
        sideUI.setLayoutY(89.0);
        sideUI.setPrefHeight(678.0);
        sideUI.setPrefWidth(177.0);
        pane.setLeft(sideUI);
        pane.setPrefSize(600,600);

    }

    public void getTimeZone() {
        theLocation = locationPicker.getText();
        timeZone = tz.getText();
        recalculateSunTimes();
    }


    public static void main(String[] args) {
        launch();
    }
}