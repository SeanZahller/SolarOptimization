package com.example.solaroptimization;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.luckycatlabs.sunrisesunset.*;
import com.luckycatlabs.sunrisesunset.dto.Location;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SkyBoxApplication extends Application {

    private static Image skyboxImage;
    static Group root = new Group();
    {
        try {
            skyboxImage = new Image(new FileInputStream("C:\\skyboxExample.png"));
            //TODO confirm if I need this, I THINK it helps with blending the photo together for the skybox corners .
//           final double width = skyboxImage.getWidth();
//            final double height = skyboxImage.getHeight();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //camera controls and scene settings declarations
    private PerspectiveCamera camera;
    private Group cameraDolly;
    private final double cameraQuantity = 10.0;
    private static final int WIDTH = 680;
    private static final int HEIGHT = 849;
    private static final int DEPTH = 700;

    //Mouse control variable declarations
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    //setting up for the folding of our image into a skybox
    private static final Affine affine = new Affine();
    private static final ImageView top   = new ImageView();
    private static final ImageView bottom= new ImageView();
    private static final ImageView left  = new ImageView();
    private static final ImageView right = new ImageView();
    private static final ImageView back  = new ImageView();
    private static final ImageView front = new ImageView();
    private static double size;
    {
        top.setId("top ");
        bottom.setId("bottom ");
        left.setId("left ");
        right.setId("right ");
        back.setId("back ");
        front.setId("front ");
    }

    //aggregating these views into a list
    private static final ImageView[] views = new ImageView[]
            {
                    top, left, back, right, front, bottom
            };
    private Image topImg;
    private Image bottomImg;
    private Image leftImg;
    private Image rightImg;
    private static ImageView frontImg;
    private Image backImg;
    private Image singleImg;


    //private static final double depth = skyboxImage.getDepth(); //MAY NOT NEED FOR cube since shoudl scale evenly
    private static final ObservableIntegerArray faces = FXCollections.observableIntegerArray();
    private static final ObservableFloatArray texCords = FXCollections.observableFloatArray();
    private static final ObservableFloatArray points = FXCollections.observableFloatArray();

    //Model Import Declaration
    private static final File house = new File("C:\\House.3ds");
    private static final File solarPanel = new File("C:\\SolarPanel(Export).3ds");
    private static final File groundSolarPanel = new File("C:\\GroundSolarPanel.3ds");
    private static Group solarPanelImport;
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

    //Location and Dates
    static String sunriseTime;
    static String sunsetTime;
    static Calendar cal;
    static String theDate = "20220310";
    static String theLocation;
    static String timeZone = "GMT-8";
    static Location location;
    static Double latitude = 47.6588;
    static Double longitude = -117.4260;
    static Date date;

    //Sun movement variables
    static boolean box1closest;
    static boolean box2closest;
    static boolean box3closest;
    static boolean box4closest;
    static boolean gbox1closest;
    static boolean gbox2closest;
    static PhongMaterial optimal = new PhongMaterial(Color.GREEN);
    static PhongMaterial subOptimal = new PhongMaterial(Color.RED);

    private static void recalculateSize(double size) {
        double factor = Math.floor(getSize()/size);
        setSize(size * factor);
    }
    //build the box through translations and folding
    protected static ImageView[] layoutViews()
    {
        for(ImageView view : views)
        {
            view.setFitWidth(getSize());
            view.setFitHeight(getSize());
        }


        back.setTranslateX(-0.5 * getSize());
        back.setTranslateY(-0.5 * getSize());
        back.setTranslateZ(-0.5 * getSize());


        front.setTranslateX(-0.5 * getSize());
        front.setTranslateY(-0.5 * getSize());
        front.setTranslateZ(0.5 * getSize());
        front.setRotationAxis(Rotate.Z_AXIS);
        front.setRotate(-180);
        front.getTransforms().add(new Rotate(180,front.getFitHeight() / 2, 0,0, Rotate.X_AXIS));
        front.setTranslateY(front.getTranslateY() - getSize());

        top.setTranslateX(-0.5 * getSize());
        top.setTranslateY(-1 * getSize());
        top.setRotationAxis(Rotate.X_AXIS);
        top.setRotate(-90);

        bottom.setTranslateX(-0.5 * getSize());
        bottom.setTranslateY(0);
        bottom.setRotationAxis(Rotate.X_AXIS);
        bottom.setRotate(90);

        left.setTranslateX(-1 * getSize());
        left.setTranslateY(-0.5 * getSize());
        left.setRotationAxis(Rotate.Y_AXIS);
        left.setRotate(90);

        right.setTranslateX(0);
        right.setTranslateY(-0.5 * getSize());
        right.setRotationAxis(Rotate.Y_AXIS);
        right.setRotate(-90);


        //  layout.getChildren().addAll(views)
        return views;
    }

    public static final double getSize()
    {
        return size;
    }

    public static final void setSize(double value)
    {
        size = value;
    }

    @Override
    public void start(Stage stage) throws IOException, ParseException {

        FXMLLoader fxmlLoader = new FXMLLoader(SkyBoxApplication.class.getResource("skybox-viewUI.fxml"));
        Pane entireFrame = new Pane();
        //Pane skyboxPane = new Pane();

        try {
            SkyBoxController.skyboxPane = SkyBoxController.setSkyboxPane();
        //    cameraDolly = new Group();
        //    cameraDolly.setTranslateZ(-1500);
        //    cameraDolly.setTranslateY(400);
       //     cameraDolly.setTranslateX(500);
        //    camera = new PerspectiveCamera(true); //TODO could make into skyboxCamera and regular perspective camera to make sure it is adjusting visuals corretly
        //    camera.setNearClip(0.1);
         //   camera.setFarClip(30000.0);
           // skyboxPane.getChildren().addAll(cameraDolly);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        entireFrame.getChildren().addAll(SkyBoxController.skyboxPane);

        Group turn = new Group();
        Rotate xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
       // cameraDolly.getTransforms().addAll(xRotate);
       // turn.getTransforms().addAll(yRotate);

        Scene scene = new Scene(root, 1024, 620); // Make the whole scene with everything


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
            if (keycode == KeyCode.Q) {                     //rotate camera clockwise
                r = new Rotate(-1, Rotate.Y_AXIS);
                t = t.createConcatenation(r);
                camera.getTransforms().addAll(t);
            }
            //TODO make a path for the camera to follow in a sphere around the house based on current distance, opposite direction of Q
            if (keycode == KeyCode.E) {                     //rotate camera counterclockwise
                r = new Rotate(+1, Rotate.Y_AXIS);
                t = t.createConcatenation(r);
                camera.getTransforms().addAll(t);
            }
            if (keycode == KeyCode.M) { //Rotate house and all solar panels Right
                t = t.createConcatenation(l);
                panelsWHouse.getTransforms().addAll(t);
            }
            if (keycode == KeyCode.N) { //Rotate house and all solar panels Left
                t = t.createConcatenation(r);
                panelsWHouse.getTransforms().addAll(t);
            }
            if(keycode == KeyCode.DIGIT0){ //Clears selected panels
                clearSelected();
            }
            if(keycode == KeyCode.DIGIT1){ // Selects ground panel number 1
                gPanelOneSelected();
            }
            if(keycode == KeyCode.DIGIT2) //selects ground panel number 2
            {
                gPanelTwoSelected();
            }
            if (keycode == KeyCode.RIGHT) { //Move selected ground panel to the right in the screen
                if(oneSelected == true) {
                    gPanelOneBox.setTranslateX(gPanelOneBox.getTranslateX() + 1);
                }
                else if(twoSelected == true){
                    gPanelTwoBox.setTranslateX(gPanelTwoBox.getTranslateX() + 1);
                }
            }

            if (keycode == KeyCode.LEFT) { //Move selected ground panel to the left in the screen
                if(oneSelected == true) {
                    gPanelOneBox.setTranslateX(gPanelOneBox.getTranslateX() - 1);
                }
                else if(twoSelected == true){
                    gPanelTwoBox.setTranslateX(gPanelTwoBox.getTranslateX() - 1);
                }
            }

            if (keycode == KeyCode.UP) { // Move selected ground panel back
                if(oneSelected == true) {
                    gPanelOneBox.setTranslateZ(gPanelOneBox.getTranslateZ() + 1);
                }
                else if(twoSelected == true) {
                    gPanelTwoBox.setTranslateZ(gPanelTwoBox.getTranslateZ() + 1);
                }
            }

            if (keycode == KeyCode.DOWN) { // Move selected ground panel forward
                if(oneSelected == true) {
                    gPanelOneBox.setTranslateZ(gPanelOneBox.getTranslateZ() - 1);
                }
                else if(twoSelected == true) {
                    gPanelTwoBox.setTranslateZ(gPanelTwoBox.getTranslateZ() - 1);
                }
            }

            if (keycode == KeyCode.SPACE) {  //Rotate selected ground solar panel
                if(oneSelected == true) {
                    t = t.createConcatenation(n);
                    gPanelOneBox.getTransforms().addAll(t);
                }
                else if(twoSelected == true) {
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
/*
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
*/
       // root.getScene().setCamera(camera);
       // root.getChildren().add(cameraDolly);
       // cameraDolly.getChildren().add(turn);
       // turn.getChildren().add(camera);

        entireFrame.getChildren().add(fxmlLoader.load());

        root.getChildren().addAll(entireFrame);
        scene.setRoot(root);

        stage.setTitle("Solar Optimization Simulator!");
        stage.setScene(scene);
        stage.show();
    }



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

    protected static ImageView[] loadImageViews(ImageView[] views) {
        top.setImage(views[0].getImage());
        left.setImage(views[1].getImage());
        back.setImage(views[2].getImage());
        right.setImage(views[3].getImage());
        front.setImage(views[4].getImage());
        bottom.setImage(views[5].getImage());
        //   Group skyboxViews = new Group();

        for (ImageView imageView : views) {


            imageView.setScaleX(10);
            imageView.setScaleY(10);
            imageView.setScaleZ(10);
            imageView.setSmooth(true);
            imageView.setPreserveRatio(true);
              //skyboxViews.getChildren().add(imageView);
        }

        return views;


    }
    public static Group createSkybox(ImageView[] setViews)
    {
        TriangleMesh cube = new TriangleMesh();

        PhongMaterial skyboxMaterial = new PhongMaterial();

        Box box = new Box(WIDTH, HEIGHT, DEPTH);

        layoutViews();
        loadImageViews(setViews);

        skyboxMaterial.setDiffuseMap(skyboxImage);

        box.setTranslateX(WIDTH);
        box.setTranslateY(HEIGHT);
        box.setTranslateZ(18500);
        box.setScaleX(10);
        box.setScaleY(10);
        box.setScaleZ(10);
        box.setMaterial(skyboxMaterial);
        box.toBack();

        box.setCullFace(CullFace.FRONT);

        Group skybox = new Group();
        skybox.getChildren().add(box);

        return skybox;
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

    static void startParams() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd"); //Formatter
        date = formatter.parse(theDate); //Parse string to create Date object
        cal = Calendar.getInstance(); //Calendar object created
        cal.setTime(date); //Calender object given corresponding date

        location = new Location(latitude.doubleValue(), longitude.doubleValue()); // Will be entered in coordinates
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, timeZone); // Creates calculator for sun times

        sunriseTime = calculator.getOfficialSunriseForDate(cal); // Gets sunrise based on date and calculator created
        sunsetTime = calculator.getOfficialSunsetForDate(cal); // Gets sunset based on date and calculator created
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
        panelsWHouse.setTranslateY(500); // puts house at ground level.. If you comment this it removes models on screen
        panelsWHouse.setTranslateX(400); // puts house at ground level.. If you comment this out it removes models on screen
        panelsWHouse.setTranslateZ(-400);
    }

    static void sunCreation() {
        Sphere sphere = new Sphere(80.0f);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.YELLOW);
        sphere.setMaterial(material);

        // create a point light
        PointLight pointlight = new PointLight();
        AmbientLight light = new AmbientLight(Color.rgb(255, 255, 255));


        // create a Group
        sun.getChildren().addAll(sphere, pointlight, light);

        sphere.setTranslateX(100);
        sphere.setTranslateY(-200);

        pointlight.setTranslateZ(-1000);
        pointlight.setTranslateX(+1000);
        pointlight.setTranslateY(+10);
        pointlight.setColor(Color.rgb(255, 255, 255));

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


    public static void main(String[] args) {
        launch(args);
    }

}
