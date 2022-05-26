package com.example.demo1;



import java.io.File;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Slider;
import javafx.scene.media.Media;

import javafx.scene.media.MediaPlayer;

import javafx.scene.media.MediaView;

import javafx.stage.Stage;
import javafx.util.Duration;



public class Controller implements Initializable{



    @FXML
    private MediaView mediaView;

//    @FXML
//    private Button playButton, pauseButton, resetButton;

    @FXML
    private Slider timeSlider;

    private File file;

    private Media media;

    private MediaPlayer mediaPlayer;

    private Stage stage;



    @Override

    public void initialize(URL arg0, ResourceBundle arg1) {



        file = new File("filmik.mp4");
        Modifyer modifyer = new Modifyer(file);
        modifyer.setRate(1.0);
        modifyer.setVolume(1.0);
        media = modifyer.getMedia();
        mediaPlayer = modifyer.getMediaPlayer();
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                //coding...
                Duration d = mediaPlayer.getCurrentTime();
                timeSlider.setValue(d.toMinutes());
            }
        });
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (timeSlider.isPressed()) {
                    double val = timeSlider.getValue();
                    mediaPlayer.seek(new Duration(val * 60 * 1000));
                    System.out.println(val * 60 * 1000);
                }
            }
        });
        timeSlider.setShowTickLabels(true);
        timeSlider.setShowTickMarks(true);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.fitHeightProperty();mediaView.fitWidthProperty();
    }

    public void playMedia() {
        mediaPlayer.play();
    }



    public void pauseMedia() {
        mediaPlayer.pause();
    }

    public void turnOnVideoEditor(ActionEvent event) throws IOException {
        mediaPlayer.pause();
        FXMLLoader fxmlLoader = new FXMLLoader(VPApplication.class.getResource("VE.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Edytor wideo");
        stage.setScene(scene);
        stage.show();
    }
    public void turnOnVP(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VPApplication.class.getResource("VM.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Edytor wideo");
        stage.setScene(scene);
        stage.show();
    }

    public void resetMedia() {
        if(mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
            mediaPlayer.seek(Duration.seconds(0.0));
        }
    }
}