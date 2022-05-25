package com.example.demo1;



import java.io.File;

import java.net.URL;

import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Button;

import javafx.scene.control.Slider;
import javafx.scene.media.Media;

import javafx.scene.media.MediaPlayer;

import javafx.scene.media.MediaView;

import javafx.util.Duration;



public class Controller implements Initializable{



    @FXML

    private MediaView mediaView;



    @FXML

    private Button playButton, pauseButton, resetButton;

    @FXML
    private Slider timeSlider;

    private File file;

    private Media media;

    private MediaPlayer mediaPlayer;



    @Override

    public void initialize(URL arg0, ResourceBundle arg1) {



        file = new File("filmik.mp4");

        media = new Media(file.toURI().toString());

        mediaPlayer = new MediaPlayer(media);
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
                }
                else{
//                    timeSlider.setValue(mediaPlayer.getCurrentTime().toSeconds());
//                    System.out.println(mediaPlayer.getCurrentTime());
                }
            }
        });timeSlider.setShowTickLabels(true);
        timeSlider.setShowTickMarks(true);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.fitHeightProperty();mediaView.fitWidthProperty();
        //volumeSlider.setValueChanging();


    }

    public void bindCurrentTime(){

    }

    public void playMedia() {
        mediaPlayer.play();
    }



    public void pauseMedia() {
        mediaPlayer.pause();
    }



    public void resetMedia() {
        if(mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
            mediaPlayer.seek(Duration.seconds(0.0));
        }
    }
}