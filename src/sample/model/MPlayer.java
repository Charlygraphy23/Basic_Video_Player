package sample.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MPlayer {

  private MediaPlayer mediaPlayer;

    public MPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public MPlayer() {
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
}
