package sample.controller;

import com.jfoenix.controls.JFXSlider;
import com.sun.xml.internal.fastinfoset.util.DuplicateAttributeVerifier;
import javafx.animation.PauseTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.model.MPlayer;

import javax.script.Bindings;
import java.io.IOException;

public class FullScreenController {
    @FXML
    private AnchorPane pane;

    @FXML
    private MediaView mv;

    @FXML
    private JFXSlider slider;

    @FXML
    private JFXSlider volumeslider;

    private Stage stage;

    @FXML
    void initialize(MPlayer player) {
        volumeslider.setValue(player.getMediaPlayer().getVolume() * 100);
        mv.setOnMouseMoved(e->{
            slider.setVisible(true);
            volumeslider.setVisible(true);

            PauseTransition pt=new PauseTransition(Duration.seconds(10));
            pt.play();
            pt.setOnFinished(ee->{
                slider.setVisible(false);
                volumeslider.setVisible(false);
            });
        });





            mv.fitWidthProperty().bind(pane.maxWidthProperty());
            mv.fitHeightProperty().bind(pane.maxHeightProperty());
            mv.setMediaPlayer(player.getMediaPlayer());
            player.getMediaPlayer().setAutoPlay(true);



            player.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    slider.setValue(newValue.toSeconds());
                }
            });

             volumeslider.setOnMouseReleased(e->{
                 player.getMediaPlayer().setVolume(volumeslider.getValue() /100);
             });


            volumeslider.valueFactoryProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    volumeslider.setValue(player.getMediaPlayer().getVolume() * 100);
                }
            });


            slider.setOnMousePressed(e->{
                player.getMediaPlayer().seek(Duration.seconds(slider.getValue()));
            });









            pane.setOnKeyReleased(e->{
                stage = (Stage) slider.getScene().getWindow();
                if(e.getCode().equals(KeyCode.ESCAPE) || e.getCode().equals(KeyCode.F11)){
                    stage.hide();
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/sample/view/mediaview.fxml"));
                    Stage s=new Stage();
                    Scene scene=stage.getScene();

                    try {
                        s.setScene(new Scene(loader.load()));

                        MediaController controller=loader.getController();
                        controller.getPlayer(player);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    s.show();

                    Node root=scene.getRoot();
                    Bounds rootBounds=root.getBoundsInLocal();

                    double width=s.getWidth()-rootBounds.getWidth();
                    double height=s.getHeight()-rootBounds.getHeight();

                    Bounds prefBound=getPrfBounds(root);
                    stage.setMinWidth(prefBound.getWidth() + width);
                    stage.setMinHeight(prefBound.getHeight() + height);
                }
            });

    }                   //   End of Initialize

    //
    //
    //


    private Bounds getPrfBounds(Node root) {

        double prefWidth;
        double prefHeight;

        Orientation bias=root.getContentBias();

        if(bias== Orientation.VERTICAL){
            prefHeight=root.prefHeight(-1);
            prefWidth=root.prefWidth(prefHeight);
        }else if(bias== Orientation.HORIZONTAL){
            prefWidth=root.prefWidth(-1);
            prefHeight=root.prefHeight(prefWidth);
        }

        else {
            prefHeight=root.prefHeight(-1);
            prefWidth=root.prefWidth(-1);
        }
        return new BoundingBox(0,0,prefWidth,prefHeight);
    }

}
