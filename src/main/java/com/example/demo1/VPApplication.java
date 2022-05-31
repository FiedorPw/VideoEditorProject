package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class VPApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //System.out.println("java version: "+System.getProperty("java.version"));  System.out.println("javafx.version: " + System.getProperty("javafx.version"));
        FXMLLoader fxmlLoader = new FXMLLoader(VPApplication.class.getResource("VP.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Media Editor");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}