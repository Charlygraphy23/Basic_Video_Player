package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import sample.model.MPlayer;

import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.EventListener;
import java.util.List;

public class MediaController extends Window {
    @FXML
    private MediaView mv;

    @FXML
    private JFXButton playButton;

    @FXML
    private JFXButton pauseButton;

    @FXML
    private JFXButton fastForwardButton;

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuBar toolBarPane;

    @FXML
    private AnchorPane buttomPane;

    @FXML
    private JFXSlider volumeSlider;

    @FXML
    private AnchorPane pane;

    @FXML
    private VBox mediaBoundary;

    @FXML
    private JFXSlider slider;

    @FXML
    private JFXButton fullScreenButton;




    private File file;
    private Media media;
    private MediaPlayer.Status status;
    private MPlayer mediaPlayer;
    private  Stage stage;

    @FXML
    void initialize() {

        mv.setPreserveRatio(false);
        slider.setValue(0);
        slider.maxWidthProperty().bind(buttomPane.widthProperty());

        pane.setOnKeyReleased(e->{

            FXMLLoader loader=new FXMLLoader(getClass().getResource("/sample/view/fullscreenview.fxml"));
            Stage s=new Stage();
            try {
                s.setScene(new Scene(loader.load()));
                FullScreenController  fullScreenController=loader.getController();
                fullScreenController.initialize(mediaPlayer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(e.getCode().equals(KeyCode.F11)){
               stage=(Stage) playButton.getScene().getWindow();
               stage.hide();
               s.show();
               s.setFullScreen(true);
           }

        });


//        if(mediaPlayer!=null)
//            mv.setMediaPlayer(mediaPlayer.getMediaPlayer());





        openFile.setOnAction(e->{

            FileChooser fileChooser=new FileChooser();
            file=fileChooser.showOpenDialog(this);
            if(file!=null){

                try {
                    media=new Media(file.toURI().toURL().toString());
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
                MediaPlayer mediaP=new MediaPlayer(media);
                mediaPlayer =new MPlayer(mediaP);
                mv.setMediaPlayer(mediaPlayer.getMediaPlayer());

                mv.fitWidthProperty().bind(mediaBoundary.widthProperty());
                mv.fitHeightProperty().bind(mediaBoundary.heightProperty());
            }

            mediaPlayer.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    slider.setValue(newValue.toSeconds());
                }

            });
            slider.setValue(0);
            volumeSlider.setValue(mediaPlayer.getMediaPlayer().getVolume() * 100);

        });

       slider.setOnMousePressed(e->{
           mediaPlayer.getMediaPlayer().seek(Duration.seconds(slider.getValue()));
       });

       mv.setOnDragOver(e->{
         if(e.getDragboard().hasFiles()){
             e.acceptTransferModes(TransferMode.ANY);
         }

       });

       mv.setOnDragDropped(e->{
           List<File> f=e.getDragboard().getFiles();
           if(f!=null) {
               try {
                   media = new Media(f.get(0).toURI().toURL().toString());
               } catch (MalformedURLException ex) {
                   ex.printStackTrace();
               }

               MediaPlayer m= new MediaPlayer(media);
               mediaPlayer =new MPlayer(m);
               mv.setMediaPlayer(mediaPlayer.getMediaPlayer());


               mv.fitWidthProperty().bind(mediaBoundary.widthProperty());
               mv.fitHeightProperty().bind(mediaBoundary.heightProperty());
           }


           mediaPlayer.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {
               @Override
               public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                   slider.setValue(newValue.toSeconds());
               }

           });
           slider.setValue(0);
           volumeSlider.setValue(mediaPlayer.getMediaPlayer().getVolume() * 100);
        });



        playButton.setOnAction(e->{
            mediaPlayer.getMediaPlayer().play();
        });

        pauseButton.setOnAction(e->{
            mediaPlayer.getMediaPlayer().pause();
        });

        fastForwardButton.setOnAction(e->{
            Duration t=mediaPlayer.getMediaPlayer().getCurrentTime().add(Duration.seconds(10.0));
            mediaPlayer.getMediaPlayer().seek(t);
            slider.setValue(mediaPlayer.getMediaPlayer().getCurrentTime().toSeconds());
        });


        volumeSlider.valueFactoryProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.getMediaPlayer().setVolume(volumeSlider.getValue() / 100);
            }
        });


        volumeSlider.setOnMouseReleased(e->{
            mediaPlayer.getMediaPlayer().setVolume(volumeSlider.getValue() / 100);
        });

        fullScreenButton.setOnAction(e->{

            Stage stage=(Stage) fullScreenButton.getScene().getWindow();
            if(stage.isFullScreen()){
                stage.setFullScreen(false);

                mv.fitWidthProperty().bind(mediaBoundary.widthProperty());
                mv.fitHeightProperty().bind(mediaBoundary.heightProperty());

            }
            else {

                mv.fitWidthProperty().bind(pane.widthProperty());
                mv.fitHeightProperty().bind(pane.heightProperty());
                stage.setFullScreen(true);
            }
        });

    }

    public void getPlayer(MPlayer player) {

        if(player!=null){
            mediaPlayer=new MPlayer();
            mediaPlayer.setMediaPlayer(player.getMediaPlayer());

            mv.setMediaPlayer(mediaPlayer.getMediaPlayer());
            mv.fitHeightProperty().bind(mediaBoundary.heightProperty());
            mv.fitWidthProperty().bind(mediaBoundary.widthProperty());

            mediaPlayer.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    slider.setValue(newValue.toSeconds());
                }
            });
        }
    }
}
