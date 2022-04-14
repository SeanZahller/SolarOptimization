package com.example.solaroptimization;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import java.io.File;

public class ModelUtils {

    static Boolean oneSelected = false;
    static Boolean twoSelected = false;
    private static Boolean box1closest = false;
    private static Boolean box2closest = false;
    private static Boolean box3closest = false;
    private static Boolean box4closest = false;
    private static Boolean gbox1closest = false;
    private static Boolean gbox2closest = false;
    private static Boolean pressed = false;
    private static Boolean pressedTwo = false;
    static PhongMaterial clear = new PhongMaterial(Color.TRANSPARENT);
    static PhongMaterial optimal = new PhongMaterial(Color.GREEN);
    static PhongMaterial subOptimal = new PhongMaterial(Color.GREEN);

    public static void highlightOptimalPanels() {

        if(pressed == false)
        {
            colorSetOpt(); //Need to figure out a way to toggle on/off. Just toggles on
            pressed = true;
        }
        else if(pressed = true) {
            ((Box) Models.solarPanelOnewR.getChildren().get(1)).setMaterial(clear);
            ((Box) Models.solarPanelTwowR.getChildren().get(1)).setMaterial(clear);
            ((Box) Models.solarPanelThreewR.getChildren().get(1)).setMaterial(clear);
            ((Box) Models.solarPanelFourwR.getChildren().get(1)).setMaterial(clear);
            ((Box) Models.gPanelOneBox.getChildren().get(1)).setMaterial(clear);
            ((Box) Models.gPanelTwoBox.getChildren().get(1)).setMaterial(clear);

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
            SunUtils.sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) Models.solarPanelOnewR.getChildren().get(1), Sun.sun);
        }
        averageP1 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            SunUtils.sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) Models.solarPanelTwowR.getChildren().get(1), Sun.sun);
        }
        averageP2 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            SunUtils.sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) Models.solarPanelThreewR.getChildren().get(1), Sun.sun);
        }
        averageP3 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            SunUtils.sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) Models.solarPanelFourwR.getChildren().get(1), Sun.sun);
        }
        averageP4 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            SunUtils.sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) Models.gPanelOneBox.getChildren().get(1), Sun.sun);
        }
        averageGP1 = total / 12;

        total = 0;
        for(int i = 0; i < totalHours.length; i++){
            SunUtils.sunTrajectory(totalHours[i]);
            total += calculateLightIntesity((Box) Models.gPanelTwoBox.getChildren().get(1), Sun.sun);
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
            ((Box) Models.solarPanelOnewR.getChildren().get(1)).setMaterial(optimal);
            ((Box) Models.solarPanelTwowR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) Models.solarPanelThreewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) Models.solarPanelFourwR.getChildren().get(1)).setMaterial(subOptimal);
        }
        if (box2closest = true) {
            ((Box) Models.solarPanelOnewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) Models.solarPanelTwowR.getChildren().get(1)).setMaterial(optimal);
            ((Box) Models.solarPanelThreewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) Models.solarPanelFourwR.getChildren().get(1)).setMaterial(subOptimal);

        }
        if (box3closest = true) {
            ((Box) Models.solarPanelOnewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) Models.solarPanelTwowR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) Models.solarPanelThreewR.getChildren().get(1)).setMaterial(optimal);
            ((Box) Models.solarPanelFourwR.getChildren().get(1)).setMaterial(subOptimal);
        }
        if (box4closest = true) {
            ((Box) Models.solarPanelOnewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) Models.solarPanelTwowR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) Models.solarPanelThreewR.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) Models.solarPanelFourwR.getChildren().get(1)).setMaterial(optimal);
        }
        if (gbox1closest = true) {
            ((Box) Models.gPanelOneBox.getChildren().get(1)).setMaterial(optimal);
            ((Box) Models.gPanelTwoBox.getChildren().get(1)).setMaterial(subOptimal);
        }
        if (gbox2closest = true) {
            ((Box) Models.gPanelOneBox.getChildren().get(1)).setMaterial(subOptimal);
            ((Box) Models.gPanelTwoBox.getChildren().get(1)).setMaterial(optimal);
        }
    }

    public static void addAllPanels() {

        if(Models.panelsWHouse.getChildren().size() < 2) {
            Models.panelsWHouse.getChildren().add(Models.solarPanelOnewR);
            Models.panelsWHouse.getChildren().add(Models.solarPanelTwowR);
            Models.panelsWHouse.getChildren().add(Models.solarPanelThreewR);
            Models.panelsWHouse.getChildren().add(Models.solarPanelFourwR);
            Models.panelsWHouse.getChildren().add(Models.gPanelOneBox);
            Models.panelsWHouse.getChildren().add(Models.gPanelTwoBox);
        }
    }

    public static void removeAllPanels() {

        if(Models.panelsWHouse.getChildren().contains(Models.solarPanelThreewR)) {
            Models.panelsWHouse.getChildren().remove(6);
            Models.panelsWHouse.getChildren().remove(5);
            Models.panelsWHouse.getChildren().remove(4);
            Models.panelsWHouse.getChildren().remove(3);
            Models.panelsWHouse.getChildren().remove(2);
            Models.panelsWHouse.getChildren().remove(1);
        }
    }

    static Group setHouse() {
        TdsModelImporter modelImporter = new TdsModelImporter(); //Model Importer

        modelImporter.read(Models.house); //Read in the house model
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
        Models.houseImport = new Group(oneStoryHouse); //create new group with the house
        return Models.houseImport;
    }

    static Group setAllSolarPanels(File solar, int pX, int pY, int pZ, int AY, int AX, int AZ) //----Model Helper Method----//
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

    static Box createsolar(Group group1, double height, double depth, double width, double rax, double raz, double ray) {
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

    static void setCenters(Rotate r, Group beingRotated) {
        r.setPivotX(beingRotated.getBoundsInLocal().getCenterX());
        r.setPivotY(beingRotated.getBoundsInLocal().getCenterY());
        r.setPivotZ(beingRotated.getBoundsInLocal().getCenterZ());
    }

    static void gPanelOneSelected() {
        oneSelected = true;
        twoSelected = false;
    }

    static void gPanelTwoSelected() {
        oneSelected = false;
        twoSelected = true;
    }

    static void clearSelected() {
        oneSelected = false;
        twoSelected = false;
    }

    public static double distancecalc(Box box, Group sun) {
        Point3D point1 = new Point3D(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
        Point3D point2 = new Point3D(sun.getTranslateX(), sun.getTranslateY(), sun.getTranslateZ());
        Double distance = Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2) + Math.pow(point1.getZ() - point2.getZ(), 2));
        return distance;
    }

    static double calculateLightIntesity(Box box, Group Sun){
        double distance = Math.abs(distancecalc(box, Sun));
        double intesity = 1/((distance)*(distance));
        intesity = intesity*10000000;
        return intesity;
    }

    public static void showIntensityLevels() {
        double solarP1Intensity, solarP2Intensity, solarP3Intensity, solarP4Intensity, solarGP1Intensity, solarGP2Intensity;
        solarP1Intensity = calculateLightIntesity((Box) Models.solarPanelOnewR.getChildren().get(1), Sun.sun);
        solarP2Intensity = calculateLightIntesity((Box) Models.solarPanelTwowR.getChildren().get(1), Sun.sun);
        solarP3Intensity = calculateLightIntesity((Box) Models.solarPanelThreewR.getChildren().get(1), Sun.sun);
        solarP4Intensity = calculateLightIntesity((Box) Models.solarPanelFourwR.getChildren().get(1), Sun.sun);
        solarGP1Intensity = calculateLightIntesity((Box) Models.gPanelOneBox.getChildren().get(1), Sun.sun);
        solarGP2Intensity = calculateLightIntesity((Box) Models.gPanelTwoBox.getChildren().get(1), Sun.sun);

        if(pressedTwo == false)
        {
            getLevels(solarP1Intensity, (Box) Models.solarPanelOnewR.getChildren().get(1));
            getLevels(solarP2Intensity, (Box) Models.solarPanelTwowR.getChildren().get(1));
            getLevels(solarP3Intensity, (Box) Models.solarPanelThreewR.getChildren().get(1));
            getLevels(solarP4Intensity, (Box) Models.solarPanelFourwR.getChildren().get(1));
            getLevels(solarGP1Intensity, (Box) Models.gPanelOneBox.getChildren().get(1));
            getLevels(solarGP2Intensity, (Box) Models.gPanelTwoBox.getChildren().get(1));
            pressedTwo = true;
        }

        else if(pressedTwo == true) {
            ((Box) Models.solarPanelOnewR.getChildren().get(1)).setMaterial(clear);
            ((Box) Models.solarPanelTwowR.getChildren().get(1)).setMaterial(clear);
            ((Box) Models.solarPanelThreewR.getChildren().get(1)).setMaterial(clear);
            ((Box) Models.solarPanelFourwR.getChildren().get(1)).setMaterial(clear);
            ((Box) Models.gPanelOneBox.getChildren().get(1)).setMaterial(clear);
            ((Box) Models.gPanelTwoBox.getChildren().get(1)).setMaterial(clear);

            pressedTwo = false;
        }


    }

    private static void getLevels(double intensity, Box box){

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
