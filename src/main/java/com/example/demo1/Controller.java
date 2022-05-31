package com.example.demo1;



import java.io.File;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;

import javafx.scene.media.MediaPlayer;

import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;



public class Controller implements Initializable{
    //vid eff
    VideoEffectsTemp Vid = new VideoEffectsTemp();


    @FXML
    private MediaView mediaView;
    @FXML
    private Label time;
    @FXML
    private Label currentTime;

    @FXML
    private Button playButton, pauseButton, resetButton, fileChoose;

    @FXML
    private Slider timeSlider, mds1, mds2;

    private File file;

    private Media media;

    private MediaPlayer mediaPlayer;

    private Stage stage;


    @Override

    public void initialize(URL arg0, ResourceBundle arg1) {



        file = new File("output.mp4");
        Modifyer modifyer = new Modifyer(file);
        modifyer.setRate(1.0);
        modifyer.setVolume(0.0);
        media = modifyer.getMedia();
        mediaPlayer = modifyer.getMediaPlayer();
        mediaPlayer.setAutoPlay(false);
        mds2.setShowTickLabels(true);
        mds2.setShowTickMarks(true);
        mds1.setShowTickLabels(true);
        mds1.setShowTickMarks(true);
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                //coding...
                Duration d = mediaPlayer.getCurrentTime();timeSlider.setMax(mediaPlayer.getTotalDuration().toMinutes());mds1.setMax(mediaPlayer.getTotalDuration().toMinutes());mds2.setMax(mediaPlayer.getTotalDuration().toMinutes());
                timeSlider.setValue(d.toMinutes());
                currentTime.setText(getTime(mediaPlayer.getCurrentTime()));
            }
        });
        System.out.println(mediaPlayer.getTotalDuration().toSeconds());
        //timeSlider.setMax(mediaPlayer.getTotalDuration().toMinutes());
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (timeSlider.isPressed()) {
                    double val = timeSlider.getValue();
                    mediaPlayer.seek(new Duration(val * 60 * 1000));
                }
            }
        });
        timeSlider.setShowTickLabels(true);
        timeSlider.setShowTickMarks(true);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    public void playMedia() {
        mediaPlayer.play();
    }

    public void pauseMedia() {
        mediaPlayer.pause();
    }
    public void fileChooser(){

    }
    public void colorBalance(){

    }
    public void eksport(){

    }

    public void changeVolume(){

    }

    public String getTime(Duration time){
     int minutes = (int)time.toMinutes();
     int seconds = (int)time.toSeconds();
     if (seconds>60)seconds=seconds%60;
     return String.format("%02d:%02d",
             minutes, seconds);
    }
    public void changeSpeed(){
    }

    public void blur(){
        mediaPlayer.pause();
        Vid.blur(file.getName(),20);Vid.replace(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.'))+"edit" + ".mp4");
        file = new File(file.getName().substring(0, file.getName().lastIndexOf('.'))+"edit" + ".mp4");
        Modifyer modifyer = new Modifyer(file);
        modifyer.setRate(1.0);
        modifyer.setVolume(0.0);
        media = modifyer.getMedia();
        mediaPlayer = modifyer.getMediaPlayer();
        mediaPlayer.setAutoPlay(false);
        mds2.setShowTickLabels(true);
        mds2.setShowTickMarks(true);
        mds1.setShowTickLabels(true);
        mds1.setShowTickMarks(true);

        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                //coding...
                Duration d = mediaPlayer.getCurrentTime();timeSlider.setMax(mediaPlayer.getTotalDuration().toMinutes());mds1.setMax(mediaPlayer.getTotalDuration().toMinutes());mds2.setMax(mediaPlayer.getTotalDuration().toMinutes());
                timeSlider.setValue(d.toMinutes());
                currentTime.setText(getTime(mediaPlayer.getCurrentTime()));
            }
        });
        System.out.println(mediaPlayer.getTotalDuration().toSeconds());
        //timeSlider.setMax(mediaPlayer.getTotalDuration().toMinutes());
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (timeSlider.isPressed()) {
                    double val = timeSlider.getValue();
                    mediaPlayer.seek(new Duration(val * 60 * 1000));
                }
            }
        });
        timeSlider.setShowTickLabels(true);
        timeSlider.setShowTickMarks(true);
        mediaPlayer.setAutoPlay(false);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    public void cutting(){

    }

    public void merge(){

    }

    public void saveChanges(){

    }

    public void deleteChanges(){

    }

    public void resetMedia() {
        if(mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
            mediaPlayer.seek(Duration.seconds(0.0));
        }
    }
}