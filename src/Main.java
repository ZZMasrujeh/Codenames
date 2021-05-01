/*
Author: Zoher Zacharias Masrujeh
Email: zzmasrujeh@uclan.ac.uk

My implementation of the codenames board game, for the Summer Hacker Challenge 2018.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.jar.JarFile;

public class Main extends Application {
    private String mColor;
    private boolean mPlayFirst;
    private String mName;
    private boolean mCleverMaster;
    static MediaPlayer mSong;
    static boolean mMuteS = false;
    private String mJarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
    static boolean mRunningFromJar = false;
    static String mClassPath;
    private static JarFile mJar;
    {
        setPath(getPath().replace("%20"," "));
        try {
            mJar = new JarFile(mJarPath);
            mRunningFromJar = true;
        } catch (IOException e) {
            mRunningFromJar = false;
            mClassPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
            mClassPath = mClassPath.replace("%20", " ");
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double width = primaryScreenBounds.getWidth();
        double height = primaryScreenBounds.getHeight();
        VBox singleOptions = new VBox(height/69.33); //15

        mSong = new Sounds().playMusic();
        mSong.setVolume(0.5);
        mSong.setCycleCount(MediaPlayer.INDEFINITE);
        mSong.play();

        Button mute = new Button("Mute Music");
        Button muteSounds = new Button("Mute Sounds");
        mute.setOnAction(event -> {
            if (mSong.isMute()) {
                mSong.setMute(false);
            }
            else mSong.setMute(true);
        });
        muteSounds.setOnAction(event -> {
            if (mMuteS) {
                mMuteS = false;
            }
            else mMuteS = true;
        });

        Font descriptive = new Font(width/77.6); //25
        Font title = new Font(width/49.75); //40

        Label singleLabel = new Label("Single Player");
        singleLabel.setFont(new Font(width/24.875)); //80
        TextField textField = new TextField();
        textField.setMaxSize(width / 4.975,height/13); //400,80
        textField.setPromptText("Add your OPERATIVE name here !");
        textField.setFont(descriptive);

        Label chooseColor = new Label("Choose Color");
        chooseColor.setFont(title);
        RadioButton red = new RadioButton( "I want to be in the red team");
        RadioButton blue = new RadioButton( "I want to be in the blue team");
        red.setSelected(true);
        blue.setFont(descriptive);
        red.setFont(descriptive);
        ToggleGroup groupColor = new ToggleGroup();
        groupColor.getToggles().addAll(blue,red);

        Label chooseTurn = new Label("Start First ?");
        chooseTurn.setFont(title);
        RadioButton first = new RadioButton ("I want to play first");
        RadioButton second = new RadioButton( "I want to play second");
        first.setFont(descriptive);
        second.setFont(descriptive);
        first.setSelected(true);
        ToggleGroup turn = new ToggleGroup();
        turn.getToggles().addAll(first, second);

        Label difficulty = new Label("Spymaster IQ:");
        difficulty.setFont(title);
        RadioButton clever = new RadioButton("The spymasters are clever !");
        RadioButton idiotSpymaster = new RadioButton("The spymasters are complete idiots !");
        clever.setFont(descriptive);
        idiotSpymaster.setFont(descriptive);
        ToggleGroup diffSetUp = new ToggleGroup();
        diffSetUp.getToggles().addAll(clever, idiotSpymaster);
        clever.setSelected(true);

        Button singlePlayer = new Button("Start");
        Button restart = new Button();

        singlePlayer.setOnAction(event1 -> {
            if (red.isSelected())                 mColor = "red";
            else if (blue.isSelected())                mColor = "blue";
            else {
                if (Math.random() >= 0.5) mColor = "blue";
                else mColor = "red";
            }
            if (first.isSelected()) mPlayFirst = true;
            else if (second.isSelected()) mPlayFirst = false;
            else {
                if (Math.random()>=0.5) mPlayFirst = true;
                else mPlayFirst = false;
            }
            if (textField.getText().equalsIgnoreCase("")) mName = "Operative X";
            else mName = textField.getText();
            if (diffSetUp.getSelectedToggle().equals(clever)) mCleverMaster = true;
            else if (diffSetUp.getSelectedToggle().equals(idiotSpymaster)) mCleverMaster = false;
            restart.fire();
            primaryStage.close();
        });
        restart.setOnAction(event -> {
            try {
                new SinglePlayer(primaryStage, mColor, mName, mPlayFirst, width, height, mCleverMaster,restart, mJarPath, getmJar());
            } catch (Exception e) {
                e.printStackTrace();
//                ExtraWindow.show(e.toString());
            }
            System.gc();
        });

        Button multiplayer = new Button("Start");
        Button resMultiplayer = new Button();
        resMultiplayer.setOnAction(event -> {
            new Multiplayer(width,height,primaryStage,multiplayer, mJar);
            System.gc();
        });
        multiplayer.setOnAction(event -> {
            primaryStage.close();
            resMultiplayer.fire();
        });

        Button howTo = new Button("How to play");
        howTo.setOnAction(event -> {
            MediaView view = null;
            try {
                view = new MediaView(new MediaPlayer(new Media(this.getClass().getResource("video.mp4").toURI().toString())));
                if (!mSong.isMute()){
                    mSong.setMute(true);
                    ExtraWindow.video(view,width,height);
                    mSong.setMute(false);
                }else ExtraWindow.video(view,width,height);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

        singleOptions.setAlignment(Pos.CENTER_LEFT);
        singleOptions.setPadding(new Insets(height/26 ,width/19.9,height/26,width/19.9)); //40,100,40,100
        singleOptions.setBackground(new Background(new BackgroundFill(new Color(1,1,1,0.9), CornerRadii.EMPTY, Insets.EMPTY)));

        singlePlayer.setMinSize(width/9.6,height/14.8751);
        mute.setMinSize(width/9.6,height/14.8751);//   200,70
        muteSounds.setMinSize(width/9.6,height/14.8751);//   200,70
        multiplayer.setMinSize(width/9.6,height/14.8571);//  200,70

        singlePlayer.setMaxSize(width/9.6,height/14.8751);
        mute.setMaxSize(width/9.6,height/14.8751);//   200,70
        muteSounds.setMaxSize(width/9.6,height/14.8751);//   200,70
        multiplayer.setMaxSize(width/9.6,height/14.8571);//  200,70

        //fit the words in the buttons
        Text muteText = new Text(mute.getText());
        muteText.setFont(Font.font(50));
        while (muteText.getBoundsInLocal().getWidth() > width/9.6/2) {
            muteText.setFont(Font.font(muteText.getFont().getSize() - 1));
        }
        mute.setFont(Font.font(muteText.getFont().getSize()));

        Text muteSText = new Text(muteSounds.getText());
        muteSText.setFont(Font.font(50));
        while (muteSText.getBoundsInLocal().getWidth() > width/9.6/2) {
            muteSText.setFont(Font.font(muteSText.getFont().getSize() - 1));
        }
        muteSounds.setFont(Font.font(muteSText.getFont().getSize()));

        Text okText = new Text(singlePlayer.getText());
        okText.setFont(Font.font("Arial", FontWeight.BOLD,FontPosture.ITALIC,50));
        while (okText.getBoundsInLocal().getWidth() > width/9.6/2) {
            okText.setFont(Font.font("Arial", FontWeight.BOLD,FontPosture.ITALIC,okText.getFont().getSize()-1));
        }
        singlePlayer.setFont(okText.getFont());

        Text multText = new Text(multiplayer.getText());
        multText.setFont(Font.font("Arial",FontWeight.BOLD,FontPosture.ITALIC,50));
        while (multText.getBoundsInLocal().getWidth() > width/9.6/2) {
            multText.setFont(Font.font("Arial",FontWeight.BOLD,FontPosture.ITALIC,multText.getFont().getSize()-1));
        }
        multiplayer.setFont(multText.getFont());

        HBox hBox1 = new HBox(width/99.5,mute,muteSounds);//20

        singleOptions.getChildren().addAll(singleLabel, textField, howTo,chooseColor, red,blue,chooseTurn ,first, second,difficulty,clever,idiotSpymaster,hBox1,singlePlayer);

        Label multLabel = new Label("Multiplayer");
        multLabel.setFont(singleLabel.getFont());
        multLabel.setBackground(singleLabel.getBackground());

        VBox multiVBOX = new VBox(15,multLabel,multiplayer);
        multiVBOX.setAlignment(Pos.TOP_CENTER);
        multiVBOX.setPadding(singleOptions.getPadding());
        multiVBOX.setMaxHeight(Double.MIN_VALUE);
        multiVBOX.setBackground(singleOptions.getBackground());

        HBox options = new HBox(20,singleOptions,multiVBOX);
        FlowPane pane = new FlowPane(options);
        pane.setAlignment(Pos.CENTER);
        pane.setBackground(new Background(new BackgroundImage(new Image(this.getClass().getResource("IMAGES/bg.png").toURI().toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER, new BackgroundSize(width,height,false,false,false,true))));

        Scene scene = new Scene(pane);
        multiplayer.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image(this.getClass().getResource("IMAGES/assassin.png").toURI().toString()));
        primaryStage.setTitle("Created by Zoher Zacharias Masrujeh - ZZMasrujeh@uclan.ac.uk");
        primaryStage.show();
    }

    private String getPath() {
        return mJarPath;
    }
    private void setPath(String path) {
        this.mJarPath = path;
    }
    private void setJar(JarFile jar) {
        this.mJar = jar;
    }
    private static JarFile getmJar() {
        return mJar;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
