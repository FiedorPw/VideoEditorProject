package com.example.demo1;



import java.io.File;
import java.net.URL;

import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;

import javafx.scene.media.MediaPlayer;

import javafx.scene.media.MediaView;
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
    private Label currentTime, mds1l, mds2l, warningLabel;

    @FXML
    private Button playButton, pauseButton, resetButton, fileChoose, okButton;

    @FXML
    private Slider timeSlider, mds1, mds2;

    private File file;

    private Media media;

    private MediaPlayer mediaPlayer;

    private Stage stage;
    @FXML
    private TextField typo;
    public String textInput;
    public double numberInput;
    public int operation; //te 3 zmienne są odpowiedzialne za guziki wymagające podaina parametrów w polu tekstowym,

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
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                typo.setText(typo.getText());
            }
        };
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                //coding...
                Duration d = mediaPlayer.getCurrentTime();timeSlider.setMax(mediaPlayer.getTotalDuration().toMinutes());mds1.setMax(mediaPlayer.getTotalDuration().toMinutes());mds2.setMax(mediaPlayer.getTotalDuration().toMinutes());
                timeSlider.setValue(d.toMinutes());
                double val = mds1.getValue();
                mds1l.setText(getTime(new Duration(val * 60 * 1000)));
                val = mds2.getValue();
                mds2l.setText(getTime(new Duration(val * 60 * 1000)));
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
    public void operationChooser(){
        warningLabel.setText("");
        switch (operation){
            case 1:
                numberInput = Double.parseDouble(typo.getText());
                warningLabel.setText(String.valueOf(numberInput));

                break;
            case 2:
                numberInput = Double.parseDouble(typo.getText());
                break;
            case 3:
                textInput = typo.getText();
                break;
            case 4:
                textInput = typo.getText();
                break;

        }
        operation = 0;
    }
    public void colorBalance(){
        warningLabel.setText("Wpisz wartość z przedziału (przedział) w polu tekstowym i zatwierdź klikając 'OK'");
        operation = 3;
    }
    public void eksport(ActionEvent event){
        warningLabel.setText("Wpisz nazwę pliku (z rozszerzeniem) w poniższym polu tekstowym i zatwierdź klikając 'OK'");
        operation = 4;

    }

    public void changeVolume(ActionEvent event){
        warningLabel.setText("Wpisz wartość z przedziału (przedział) w polu tekstowym i zatwierdź klikając 'OK'");
        operation = 1;
    }

    public String getTime(Duration time){
     int minutes = (int)time.toMinutes();
     int seconds = (int)time.toSeconds();
     if (seconds>=60)seconds=seconds%60;
     return String.format("%02d:%02d",
             minutes, seconds);
    }
    public void changeSpeed(ActionEvent event){
        warningLabel.setText("Wpisz wartość z przedziału (przedział) w polu tekstowym i zatwierdź klikając 'OK'");
        operation = 2;
    }
    public void playAfterChange(File file1){
        Modifyer modifyer = new Modifyer(file1);
        file = file1;
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
                double val = mds1.getValue();
                mds1l.setText(getTime(new Duration(val * 60 * 1000)));
                val = mds2.getValue();
                mds2l.setText(getTime(new Duration(val * 60 * 1000)));
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

    public void blur(){
        mediaPlayer.pause();
        Vid.blur(file.getName(),20);Vid.replace(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.'))+"edit" + ".mp4");
        playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4"));
    }
    public long[] sliderDif(){
        long[] output = new long[2];
        double val = mds1.getValue();Duration duration =  new Duration(val * 60 * 1000);
        double val2 = mds2.getValue();Duration duration1 =  new Duration(val2 * 60 * 1000);
        if (val<val2){
            output[0] = (long)duration.toSeconds();
            output[1] = (long)duration1.toSeconds();
        }else{
            output[0] = (long)duration1.toSeconds();
            output[1] = (long)duration.toSeconds();
        }
        return output;
    }
    public void cutting(){
        long[] k = sliderDif();
        double val = mds1.getMax();Duration duration =  new Duration(val * 60 * 1000);
        long maks = (long)duration.toSeconds();
        if (k[0] == 0 && k[1]==maks){
            System.out.println("im in");
            return;
        }
        if (k[0]==0 || k[1] == 0 || k[0] == maks || k[1] == maks ){
            if (mds1.getValue() == mds1.getMin() || mds1.getValue() == mds1.getMax()){
                double val2 = mds2.getValue();Duration duration1 =  new Duration(val2 * 60 * 1000);
                Vid.split(file.toString(), (long)duration1.toSeconds());
                playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4"));
            }else{val = mds1.getValue();duration =  new Duration(val * 60 * 1000);
                Vid.split(file.toString(), (long)duration.toSeconds());
                playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4"));
            }
        }else{
//            double val = mds1.getValue();Duration duration =  new Duration(val * 60 * 1000);
//            double val2 = mds2.getValue();Duration duration1 =  new Duration(val2 * 60 * 1000);
            Vid.cutPass(file.toString(), k[0], k[1]);
            playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4"));
        }


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