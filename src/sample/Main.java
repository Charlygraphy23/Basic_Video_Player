package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("view/mediaview.fxml")));

        primaryStage.setScene(scene);
        primaryStage.show();




        Node root = scene.getRoot();
        Bounds rootBouds = root.getBoundsInLocal();

        double width = primaryStage.getWidth() - rootBouds.getWidth();
        double height = primaryStage.getHeight() - rootBouds.getHeight();

        Bounds prefBounds = getPreBounds(root);
        primaryStage.setMinWidth(prefBounds.getWidth() + width);
        primaryStage.setMinHeight(prefBounds.getHeight() + height);
    }

    private Bounds getPreBounds(Node root) {

        double preWidth;
        double preHeight;

        Orientation bias=root.getContentBias();
        if(bias == Orientation.HORIZONTAL){
            preWidth=root.prefWidth(-1);
            preHeight=root.prefHeight(preWidth);
        }else if(bias ==Orientation.VERTICAL){
            preHeight=root.prefHeight(-1);
            preWidth=root.prefWidth(preHeight);
        }else {
            preHeight=root.prefHeight(-1);
            preWidth=root.prefWidth(-1);
        }

        return new BoundingBox(0,0,preWidth,preHeight);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
