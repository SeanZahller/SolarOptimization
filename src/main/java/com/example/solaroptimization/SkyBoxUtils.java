package com.example.solaroptimization;


import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

public class SkyBoxUtils {

    static void constructWorld(Group root) {

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
        Models.models();
        Sun.sunCreation();
        root.getChildren().addAll(skybox, Sun.sun, Models.panelsWHouse);

    }


}
