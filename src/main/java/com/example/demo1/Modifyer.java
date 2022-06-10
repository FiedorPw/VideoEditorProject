package com.example.demo1;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class Modifyer {
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;

    /**
     * klasa odpowiedzialna za najważniejsze atrybuty mediaPlayera, poniżej jej konstruktor
     * @param file
     */
    public Modifyer(File file) {
        this.file = file;
        this.media = new Media(file.toURI().toString());
        this.mediaPlayer = new MediaPlayer(media);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void setRate(Double rate){
        mediaPlayer.setRate(rate);
    }
    public void setVolume(Double rate){
        mediaPlayer.setVolume(rate);
    }

}
