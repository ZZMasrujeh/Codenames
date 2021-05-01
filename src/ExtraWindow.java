import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.beans.EventHandler;
import java.nio.charset.CoderResult;
import java.time.Duration;


public class ExtraWindow {
     static void show(String path){
        Label label = new Label(path);

        label.setFont(Font.font(20));
        StackPane stackPane = new StackPane(label);
        Scene scene = new Scene(stackPane, 800, 250);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
    }

     static void show(String path, Image image){ //used for debugging when running the jar file... to show the exceptions
        Label label = new Label(path);
        label.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        label.setFont(Font.font(20));
        StackPane stackPane = new StackPane(label);
        Scene scene = new Scene(stackPane, 800, 250);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
     static void video(MediaView view, Double width, Double height) {
        view.setFitWidth(width);
        view.setFitHeight(height);

        StackPane stackPane = new StackPane(view);
        stackPane.setBackground(new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(stackPane);
        scene.addEventFilter(KeyEvent.KEY_PRESSED,event -> {
            if (event.getCode() == KeyCode.SPACE) {
                if (view.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PAUSED)) {
                    view.getMediaPlayer().play();
                }else view.getMediaPlayer().pause();
            }
        });


        Stage stage = new Stage();
        ObjectProperty<javafx.util.Duration> mills = new SimpleObjectProperty<>();
        mills.bind(view.getMediaPlayer().currentTimeProperty());
        StringProperty op = new SimpleStringProperty();
        int[] minutes = {0};
        int[] seconds = {0};
        mills.addListener(observable -> {
            int time = (int) mills.getValue().toSeconds();
            minutes[0] = time / 60;
                if (time>0) {
                    if (time % 60 == 0) {
                        seconds[0]=0;
                    }else seconds[0]=time%60;
                }

            String sTimer;
            if (seconds[0]<10) sTimer = "0" + seconds[0];
            else sTimer = "" + seconds[0];
            op.set("Click left to go back 10 seconds, right to go forward 10 seconds, hit space to pause/resume  \t " + minutes[0]+":"+sTimer+" / 3:17");
        });
        view.setOnMouseClicked(event -> {
            if (event.getX() > view.getBoundsInLocal().getWidth() / 2) {
                view.getMediaPlayer().seek(view.getMediaPlayer().getCurrentTime().add(javafx.util.Duration.seconds(10)));
            }else   view.getMediaPlayer().seek(view.getMediaPlayer().getCurrentTime().subtract(javafx.util.Duration.seconds(10)));
        });
        stage.titleProperty().bind(op);
        stage.setScene(scene);
        view.getMediaPlayer().play();
        stage.setMaximized(true);
        view.requestFocus();
        view.setOnMouseEntered(event -> view.requestFocus());
        stage.showAndWait();
        view.getMediaPlayer().stop();
    }
}
