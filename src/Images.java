import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import jdk.jfr.Percentage;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Images {
    public static Background setBgWPhoto(String path, Color color, BackgroundRepeat backgroundRepeat) {
        BackgroundFill bpFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        List<BackgroundFill> bpFillList = new LinkedList<>();
        bpFillList.add(bpFill);
        Image bpImage = null;
        try {

            bpImage = new Image(path);
        } catch (Exception e) {
            System.out.println("Background Image failed");
        }
        BackgroundPosition bgp = new BackgroundPosition(null, -160, false, null, -20, false);
        BackgroundImage bpBgImage = new BackgroundImage(bpImage, backgroundRepeat,backgroundRepeat,bgp,BackgroundSize.DEFAULT);
        List<BackgroundImage> bpImageList = new LinkedList<>();
        bpImageList.add(bpBgImage);
        Background bpBg = new Background(bpFillList,bpImageList);
        return bpBg;
    }

    public static Background setPhotoBGsingle(String path, Color color, BackgroundRepeat backgroundRepeat) {
        BackgroundFill bpFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        List<BackgroundFill> bpFillList = new LinkedList<>();
        bpFillList.add(bpFill);
        Image bpImage = null;
        try {
            bpImage = new Image(path);
        } catch (Exception e) {
            System.out.println("Background Image failed");
        }
        BackgroundImage bpBgImage = new BackgroundImage(bpImage, backgroundRepeat,backgroundRepeat,BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,true,true,true,true));
        List<BackgroundImage> bpImageList = new LinkedList<>();
        bpImageList.add(bpBgImage);
        Background bpBg = new Background(bpFillList,bpImageList);
        return bpBg;
    }
    public static Background setPhotoBG(String path, Color color, BackgroundRepeat backgroundRepeat,double width,double height, double screenHeight) {
        BackgroundFill bpFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        List<BackgroundFill> bpFillList = new LinkedList<>();
        bpFillList.add(bpFill);
        Image bpImage = null;
        try {

            bpImage = new Image(path);
        } catch (Exception e) {
            System.out.println("Background Image failed");
        }
        BackgroundPosition bgp = new BackgroundPosition(Side.LEFT, 0, true, Side.TOP, 0, true);
        BackgroundImage bpBgImage = new BackgroundImage(bpImage, backgroundRepeat,backgroundRepeat,bgp,
                new BackgroundSize(width,height,false,false,false,false));
                List<BackgroundImage> bpImageList = new LinkedList<>();
        bpImageList.add(bpBgImage);
        Background bpBg = new Background(bpFillList,bpImageList);
        return bpBg;
    }
}

