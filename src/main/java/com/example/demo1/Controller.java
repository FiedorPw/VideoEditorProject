package com.example.demo1;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.LinkedList;
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
    Queue queue = new Queue();

    @FXML
    private MediaView mediaView;
    @FXML
    private Label time;
    @FXML
    private Label currentTime, mds1l, mds2l, warningLabel;

    @FXML
    private Button playButton, pauseButton, resetButton, fileChoose, okButton, projectLoader;

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

    /**
     * Zdefiniowana na nowo metoda initialize na potrzeby wywołania okna mediaPlayera
     */
    @Override

    public void initialize(URL arg0, ResourceBundle arg1) {



        file = new File("filmik.mp4");
        Modifyer modifyer = new Modifyer(file);
        modifyer.setRate(1.0);
        modifyer.setVolume(1.0);
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
                Duration d = mediaPlayer.getCurrentTime();
                timeSlider.setMax(mediaPlayer.getTotalDuration().toMinutes());
                mds1.setMax(mediaPlayer.getTotalDuration().toMinutes());
                mds2.setMax(mediaPlayer.getTotalDuration().toMinutes());
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

    /**
     * Seria metod obsługujących guziki GUI i ewentualne inne elementy, takie jak informacyjne pole tekstowe label
     */
    public void playMedia() {
        mediaPlayer.play();
    }

    public void pauseMedia() {
        mediaPlayer.pause();
    }
    public void fileChooser(){
        warningLabel.setText("Wpisz nazwę pliku w polu tekstowym i zatwierdź klikając 'OK'");
        operation = 5;
    }
    public void loadProject() throws FileNotFoundException {
        invokeMetods();
    }

    /**
     * metoda obsługująca guziki odpowiedzialne w GUI za operacje zmieniające parametry pliku lub jego części
     * jako switch implementuje polecenia zmiany szybkości odtwarzania filmu, jego głośności i balansu kolorów
     * tworzy wiele plików pośrednich, ale po zakończeniu pracy "sprząta po sobie"
     * w kolejnych case'ach realizuje kompresję i eksport pliku oraz konkatenację video
     */
    public void operationChooser(){
        long[] k = sliderDif();
        double val = mds1.getMax();
        Duration duration =  new Duration(val * 60 * 1000);
        long maks = (long)duration.toSeconds();
        warningLabel.setText("");
        switch (operation){
            case 1:
                mediaPlayer.pause();
                numberInput = Double.parseDouble(typo.getText());
                //implementacja
                if ((k[0] == 0 && k[1]==maks) || k[1]==k[0]){
                    Vid.volumeManipulation(file.getName(),numberInput);
                    playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4"));
                }
                else {
                    if (k[0] == 0 || k[1] == 0 || k[0] == maks || k[1] == maks) {
                        if (mds1.getValue() == mds1.getMin() || mds1.getValue() == mds1.getMax()) {
                            double val2 = mds2.getValue();
                            Duration duration1 = new Duration(val2 * 60 * 1000);
                            System.out.println(val2);
                            Vid.split(file.toString(), (long) duration1.toSeconds());
                            Vid.volumeManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4", numberInput);
                            Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "1edit" + ".mp4");
                            Vid.append(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                            File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                            del2.deleteOnExit();
                            File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                            del4.deleteOnExit();
                            playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                        } else {
                            val = mds1.getValue();
                            duration = new Duration(val * 60 * 1000);
                            System.out.println(val);
                            Vid.split(file.toString(), (long) duration.toSeconds());
                            Vid.volumeManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", numberInput);
                            Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                            Vid.append(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                            File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                            del2.deleteOnExit();
                            File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                            del4.deleteOnExit();
                            playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                        }
                    } else {
                        Vid.cutPass(file.toString(), k[0], k[1]);
                        Vid.volumeManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", numberInput);
                        Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                        Vid.concatenateFin(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                        File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                        del2.deleteOnExit();
                        File del3=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "3" + ".mp4");
                        del3.deleteOnExit();
                        File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                        del4.deleteOnExit();
                        playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                    }
                }
                break;
            case 2:
                mediaPlayer.pause();
                numberInput = Double.parseDouble(typo.getText());
                //implementacja
                if ((k[0] == 0 && k[1]==maks)||k[0]==k[1]){
                    Vid.speedManipulation(file.getName(),numberInput);
                    File file1 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4");
                    playAfterChange(file1);
                }
                else {
                    if (k[0] == 0 || k[1] == 0 || k[0] == maks || k[1] == maks || k[0] == k[1]) {
                        if (mds1.getValue() == mds1.getMin() || mds1.getValue() == mds1.getMax()) {
                            double val2 = mds2.getValue();
                            Duration duration1 = new Duration(val2 * 60 * 1000);
                            Vid.split(file.toString(), (long) duration1.toSeconds());
                            Vid.speedManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4", numberInput);
                            Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "1edit" + ".mp4");
                            Vid.append(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                            File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                            del2.deleteOnExit();
                            File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                            del4.deleteOnExit();
                            playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                        } else {
                            val = mds1.getValue();
                            duration = new Duration(val * 60 * 1000);
                            Vid.split(file.toString(), (long) duration.toSeconds());
                            Vid.speedManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", numberInput);
                            Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                            Vid.append(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                            File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                            del2.deleteOnExit();
                            File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                            del4.deleteOnExit();
                            playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                        }
                    } else {
                        Vid.cutPass(file.toString(), k[0], k[1]);
                        Vid.speedManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", numberInput);
                        Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                        Vid.concatenateFin(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                        File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                        del2.deleteOnExit();
                        File del3=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "3" + ".mp4");
                        del3.deleteOnExit();
                        File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                        del4.deleteOnExit();
                        playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                    }
                }
                break;
            case 3:
                mediaPlayer.pause();
                textInput = typo.getText();
                String[] inputs = textInput.split(" ");
                //ColorBalance byl bardzo problematyczny, ale kosztem pamieci i czasu sie z nim uporalismy
                    if(k[0]==0 || k[1]==maks) {
                        k[0] = 1;
                        k[1] = (long) mediaPlayer.getTotalDuration().toSeconds();
                        Vid.buffer(file.toString());
                        Vid.simpleAppend("buffer.mp4", file.toString(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4");
                        Vid.simpleAppend(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4", "buffer.mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit1" + ".mp4");
                        File del1 = new File("buffer.mp4");
                        File del2 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4");
                        File del3 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit1" + ".mp4");
                        file = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit1" + ".mp4");
                        Vid.cutPass(file.toString(), k[0], k[1]);
                        File del4 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                        File del6 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "3" + ".mp4");
                        Vid.colorBalance(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", Double.parseDouble(inputs[2]), inputs[0], inputs[1]);
                        //Wczytany plik colorbalance w tym miejscu by sie nie zaladowal - z nieznanych lecz niezaleznych od nas przyczyn
                        Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                        File del5 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                        Vid.concatenateFin(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                        Vid.cutPass(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4", k[0], k[1]);
                        File del7 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output1" + ".mp4");
                        File del8 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output3" + ".mp4");
                        File del9 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                        playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output2" + ".mp4"));
                        del1.deleteOnExit();
                        del2.deleteOnExit();
                        del3.deleteOnExit();
                        del4.deleteOnExit();
                        del5.deleteOnExit();
                        del6.deleteOnExit();
                        del7.deleteOnExit();
                        del8.deleteOnExit();
                        del9.deleteOnExit();
                    }
                    else {
                        Vid.cutPass(file.toString(), k[0], k[1]);
                        File del1 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                        File del2 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "3" + ".mp4");
                        Vid.colorBalance(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4",Double.parseDouble(inputs[2]),inputs[0],inputs[1]);
                        Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4",file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                        Vid.concatenateFin(file.getName(),file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                        playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                        del1.deleteOnExit();
                        del2.deleteOnExit();
                    }
                    break;
            case 4:
                mediaPlayer.pause();
                textInput = typo.getText();
                Vid.compress(file.getName(), textInput);
                break;
            case 5:
                mediaPlayer.pause();
                file = new File(typo.getText());
                playAfterChange(file);
                break;
            case 6:
                mediaPlayer.pause();
                textInput = typo.getText().split(" ")[1];
                numberInput = Integer.parseInt(typo.getText().split(" ")[0]);
                if(numberInput==1)
                    Vid.simpleAppend(file.getName(),textInput,file.getName().substring(0, file.getName().lastIndexOf('.')) + "merge.mp4");
                if(numberInput==0)
                    Vid.simpleAppend(textInput,file.getName(),file.getName().substring(0, file.getName().lastIndexOf('.')) + "merge.mp4");
                file = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "merge.mp4");
                playAfterChange(file);
                break;
        }
        operation = 0;
    }

    /**
     * Metoday wywołująca pobrane z listy metody bez użycia GUI
     * Wykorzystywana w odtwarzaniu zmian z projektu
     * @throws FileNotFoundException
     */
    public void invokeMetods() throws FileNotFoundException {
        queue.load();
        ArrayList<Integer> listOfmetods = queue.listOfmetods;
        ArrayList<long[]> kList = queue.kList;
        ArrayList<String> textInputList =queue.textInputList;
        ArrayList<Double> numberInputList = queue.numberInputList;


        for (int i = 0; i < listOfmetods.size(); i++) {
            operation = listOfmetods.get(i);
            long[] k = kList.get(i);
            typo.setText(textInputList.get(i));
            operationChooser(k);
        }

    }

    /**
     * Switch odpowiadający za wykonywanie metod zmieniających plik w wersji przyjmującej parametry
     * Wykorzystywany do odtworzenia pliku projektu
     * @param k tablica pozycji sliderów
     */
    public void operationChooser(long[] k){

        double val = mds1.getMax();
        Duration duration =  new Duration(val * 60 * 1000);
        long maks = (long)duration.toSeconds();
        warningLabel.setText("");
        switch (operation){
            case 1:
                mediaPlayer.pause();
                numberInput = Double.parseDouble(typo.getText());
                //implementacja
                if ((k[0] == 0 && k[1]==maks) || k[1]==k[0]){
                    Vid.volumeManipulation(file.getName(),numberInput);
                    playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4"));
                }
                else {
                    if (k[0] == 0 || k[1] == 0 || k[0] == maks || k[1] == maks) {
                        if (mds1.getValue() == mds1.getMin() || mds1.getValue() == mds1.getMax()) {
                            double val2 = mds2.getValue();
                            Duration duration1 = new Duration(val2 * 60 * 1000);
                            System.out.println(val2);
                            Vid.split(file.toString(), (long) duration1.toSeconds());
                            Vid.volumeManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4", numberInput);
                            Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "1edit" + ".mp4");
                            Vid.append(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                            File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                            del2.deleteOnExit();
                            File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                            del4.deleteOnExit();
                            playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                        } else {
                            val = mds1.getValue();
                            duration = new Duration(val * 60 * 1000);
                            System.out.println(val);
                            Vid.split(file.toString(), (long) duration.toSeconds());
                            Vid.volumeManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", numberInput);
                            Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                            Vid.append(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                            File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                            del2.deleteOnExit();
                            File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                            del4.deleteOnExit();
                            playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                        }
                    } else {
                        Vid.cutPass(file.toString(), k[0], k[1]);
                        Vid.volumeManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", numberInput);
                        Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                        Vid.concatenateFin(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                        File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                        del2.deleteOnExit();
                        File del3=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "3" + ".mp4");
                        del3.deleteOnExit();
                        File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                        del4.deleteOnExit();
                        playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                    }
                }
                break;
            case 2:
                mediaPlayer.pause();
                numberInput = Double.parseDouble(typo.getText());
                //implementacja
                if ((k[0] == 0 && k[1]==maks)||k[0]==k[1]){
                    Vid.speedManipulation(file.getName(),numberInput);
                    File file1 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4");
                    playAfterChange(file1);
                }
                else {
                    if (k[0] == 0 || k[1] == 0 || k[0] == maks || k[1] == maks || k[0] == k[1]) {
                        if (mds1.getValue() == mds1.getMin() || mds1.getValue() == mds1.getMax()) {
                            double val2 = mds2.getValue();
                            Duration duration1 = new Duration(val2 * 60 * 1000);
                            Vid.split(file.toString(), (long) duration1.toSeconds());
                            Vid.speedManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4", numberInput);
                            Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "1edit" + ".mp4");
                            Vid.append(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                            File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                            del2.deleteOnExit();
                            File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                            del4.deleteOnExit();
                            playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                        } else {
                            val = mds1.getValue();
                            duration = new Duration(val * 60 * 1000);
                            Vid.split(file.toString(), (long) duration.toSeconds());
                            Vid.speedManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", numberInput);
                            Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                            Vid.append(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                            File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                            del2.deleteOnExit();
                            File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                            del4.deleteOnExit();
                            playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                        }
                    } else {
                        Vid.cutPass(file.toString(), k[0], k[1]);
                        Vid.speedManipulation(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", numberInput);
                        Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                        Vid.concatenateFin(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                        File del2=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                        del2.deleteOnExit();
                        File del3=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "3" + ".mp4");
                        del3.deleteOnExit();
                        File del4=new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                        del4.deleteOnExit();
                        playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                    }
                }
                break;
            case 3:
                mediaPlayer.pause();
                textInput = typo.getText();
                String[] inputs = textInput.split(" ");
                //ColorBalance byl bardzo problematyczny, ale kosztem pamieci i czasu sie z nim uporalismy
                if(k[0]==0 || k[1] == maks) {
                    k[0] = 1;
                    k[1] = (long) mediaPlayer.getTotalDuration().toSeconds();
                    Vid.buffer(file.toString());
                    Vid.simpleAppend("buffer.mp4", file.toString(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4");
                    Vid.simpleAppend(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4", "buffer.mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit1" + ".mp4");
                    File del1 = new File("buffer.mp4");
                    File del2 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4");
                    File del3 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit1" + ".mp4");
                    file = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit1" + ".mp4");
                    Vid.cutPass(file.toString(), k[0], k[1]);
                    File del4 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                    File del6 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "3" + ".mp4");
                    Vid.colorBalance(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", Double.parseDouble(inputs[2]), inputs[0], inputs[1]);
                    //Wczytany plik colorbalance w tym miejscu by sie nie zaladowal - z nieznanych lecz niezaleznych od nas przyczyn
                    Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4", file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                    File del5 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4");
                    Vid.concatenateFin(file.getName(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                    Vid.cutPass(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4", k[0], k[1]);
                    File del7 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output1" + ".mp4");
                    File del8 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output3" + ".mp4");
                    File del9 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                    playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output2" + ".mp4"));
                    del1.deleteOnExit();
                    del2.deleteOnExit();
                    del3.deleteOnExit();
                    del4.deleteOnExit();
                    del5.deleteOnExit();
                    del6.deleteOnExit();
                    del7.deleteOnExit();
                    del8.deleteOnExit();
                    del9.deleteOnExit();
                }
                else {
                    Vid.cutPass(file.toString(), k[0], k[1]);
                    File del1 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4");
                    File del2 = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "3" + ".mp4");
                    Vid.colorBalance(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4",Double.parseDouble(inputs[2]),inputs[0],inputs[1]);
                    Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4",file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                    Vid.concatenateFin(file.getName(),file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                    playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
                    del1.deleteOnExit();
                    del2.deleteOnExit();
                }
                break;
            case 4:
                mediaPlayer.pause();
                textInput = typo.getText();
                Vid.compress(file.getName(), textInput);
                break;
            case 5:
                mediaPlayer.pause();
                file = new File(typo.getText());
                playAfterChange(file);
                break;
            case 6:
                mediaPlayer.pause();
                textInput = typo.getText().split(" ")[1];
                numberInput = Integer.parseInt(typo.getText().split(" ")[0]);
                if(numberInput==1)
                    Vid.simpleAppend(file.getName(),textInput,file.getName().substring(0, file.getName().lastIndexOf('.')) + "merge.mp4");
                if(numberInput==0)
                    Vid.simpleAppend(textInput,file.getName(),file.getName().substring(0, file.getName().lastIndexOf('.')) + "merge.mp4");
                file = new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "merge.mp4");
                playAfterChange(file);
                break;
        }
        operation = 0;
    }

    /**
     * seria metod zapisujących wykonywane czynności do plików projektu
     * zajmuje się też wyrzucaniem komunikatów na label informacyjny
     * @throws IOException
     */
    public void colorBalance() throws IOException {
        queue.save(3,sliderDif(),typo.getText(),numberInput);
        warningLabel.setText("Wpisz kolor (red/blue/green), ton (shadows/midtones/highlights) oraz wartość z przedziału [-1.0, 1.0] w polu tekstowym i zatwierdź klikając 'OK'");
        operation = 3;
    }
    public void eksport(ActionEvent event){
        warningLabel.setText("Wpisz nazwę pliku (z rozszerzeniem) w poniższym polu tekstowym i zatwierdź klikając 'OK'");
        operation = 4;

    }

    public void changeVolume(ActionEvent event) throws IOException {
        warningLabel.setText("Wpisz wartość (domyślnie 1.0) w polu tekstowym i zatwierdź klikając 'OK'");
        queue.save(1,sliderDif(),typo.getText(),numberInput);
        operation = 1;
    }

    public String getTime(Duration time){
     int minutes = (int)time.toMinutes();
     int seconds = (int)time.toSeconds();
     if (seconds>=60)seconds=seconds%60;
     return String.format("%02d:%02d",
             minutes, seconds);
    }
    public void changeSpeed(ActionEvent event) throws IOException {
        warningLabel.setText("Wpisz wartość z przedziału [0.5, 100.0] w polu tekstowym i zatwierdź klikając 'OK'");
        queue.save(2,sliderDif(),typo.getText(),numberInput);
        operation = 2;
    }

    /**
     * metoda pozwalająca na odtworzenie nowego pliku po zastosowaniu zmian na starym pliku
     * @param file1
     */
    public void playAfterChange(File file1){
        Modifyer modifyer = new Modifyer(file1);
        file = file1;
        modifyer.setRate(1.0);
        modifyer.setVolume(1.0);
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

    /**
     * metoda odpowiadająca za wyblurowanie obrazu
     * w miarę rozwoju projektu możliwa do sparametryzowania
     */
    public void blur(){
        long[] k = sliderDif();
        double val = mds1.getMax();
        Duration duration =  new Duration(val * 60 * 1000);
        long maks = (long)duration.toSeconds();
        if (k[0] == 0 && k[1]==maks || k[0]==k[1]){
            Vid.blur(file.getName().substring(0, file.getName().lastIndexOf('.')) + ".mp4",20);
            playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "edit" + ".mp4"));
        }else
        if (k[0]==0 || k[1] == 0 || k[0] == maks || k[1] == maks ){
            if (mds1.getValue() == mds1.getMin() || mds1.getValue() == mds1.getMax()){
                double val2 = mds2.getValue();Duration duration1 =  new Duration(val2 * 60 * 1000);
                Vid.split(file.toString(), (long)duration1.toSeconds());
                Vid.blur(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4",20);
                Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "1" + ".mp4",file.getName().substring(0, file.getName().lastIndexOf('.')) + "1edit" + ".mp4");
                Vid.append(file.getName(),file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
            }else{
                val = mds1.getValue();duration =  new Duration(val * 60 * 1000);
                Vid.split(file.toString(), (long)duration.toSeconds());
                Vid.blur(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4",20);
                Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4",file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
                Vid.append(file.getName(),file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
                playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));            }
        }else{
            Vid.cutPass(file.toString(), k[0], k[1]);
            Vid.blur(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4",20);
            Vid.replace(file.getName().substring(0, file.getName().lastIndexOf('.')) + "2" + ".mp4",file.getName().substring(0, file.getName().lastIndexOf('.')) + "2edit" + ".mp4");
            Vid.concatenateFin(file.getName(),file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4");
        playAfterChange(new File(file.getName().substring(0, file.getName().lastIndexOf('.')) + "output" + ".mp4"));
        }

    }

    /**
     * metoda ustalająca, który slider znajduje się bardziej z prawej strony i zamyka obrys zaznaczenia, a który z lewej i go otwiera
     */
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

    /**
     * dwie metody dzielące i łączące pliki video
     */
    public void cutting(){
        long[] k = sliderDif();
        double val = mds1.getMax();Duration duration =  new Duration(val * 60 * 1000);
        long maks = (long)duration.toSeconds();
        if (k[0] == 0 && k[1]==maks || k[0]==k[1]){
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

    public void merge() throws IOException {
        warningLabel.setText("Wpisz 0 by dołączyć nowy plik przed " + file.getName() + ", lub 1 by dołączyć go po nim, oraz wpisz nazwę dołączanego pliku (z rozszerzeniem) w poniższym polu tekstowym i zatwierdź klikając 'OK'");
        queue.save(6,sliderDif(),typo.getText(),numberInput);
        operation = 6;
    }

    /**
     * metody do obsługi pliku projektowego
     */
    public void saveChanges(){

    }

    public void deleteChanges() throws FileNotFoundException {

            queue.load();
            ArrayList<Integer> listOfmetods = queue.listOfmetods;
            ArrayList<long[]> kList = queue.kList;
            ArrayList<String> textInputList =queue.textInputList;
            ArrayList<Double> numberInputList = queue.numberInputList;


            for (int i = 0; i < listOfmetods.size()-1; i++) {
                operation = listOfmetods.get(i);
                long[] k = kList.get(i);
                typo.setText(textInputList.get(i));
                operationChooser(k);
            }


    }

    /**
     * cofa slider odtwarzania na początek osi czasu
     */
    public void resetMedia() {
        if(mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
            mediaPlayer.seek(Duration.seconds(0.0));
        }
    }
}