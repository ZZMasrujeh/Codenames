import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class Multiplayer {

    private JarFile jar;
    private ArrayList<Buttons> buttons;
    private StringProperty counterText = new SimpleStringProperty();
    private int redRemaining;
    private int totalRed;
    private int byRemaining = 7;
    private int blueRemaining;
    private int totalBlue;
    private int column;
    private int row;

    Multiplayer(double width, double height, Stage main, Button restart, JarFile jar) {
        setJar(jar);
        BorderPane bp = new BorderPane();
        Sounds sounds = new Sounds();

        Button showKey = new Button("Show Key");
        Button done = new Button("Switch");

        if (Math.random() >= 0.5) plays = "blue";
        else plays = "red";
        GridPane grid = new GridPane();
        try {
            bp.setBackground(Images.setBgWPhoto(this.getClass().getResource("IMAGES/wood.jpeg").toURI().toString(), Color.WHITE, BackgroundRepeat.REPEAT));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        Background redBG = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
        Background blueBG = new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY));
        setButtons(plays);

        String firstTeam;
        Label topL = new Label();
        Label bottomL = new Label();
        Label leftL = new Label();
        Label rightL = new Label();

        if (plays.equalsIgnoreCase("blue")) {
            topL.setBackground(blueBG);
            firstTeam = "blue";
        } else {
            topL.setBackground(redBG);
            firstTeam = "red";
        }
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.1), event -> {
            int index = getRow() * 5 + getColumn();
            buttons.get(index).button = new Button();
            buttons.get(index).button.setFont(new Font("TAHOMA", 30));
            buttons.get(index).button.setText(buttons.get(index).name);
            buttons.get(index).button.setAlignment(Pos.CENTER);
            GridPane.setConstraints(buttons.get(index).button, getColumn(), getRow());
            grid.getChildren().add(buttons.get(index).button);
            buttons.get(index).button.setPrefSize(width / 6.5245, height / 6.9333);    //305,150

            buttons.get(index).button.setOnAction(event1 -> {
                buttons.get(index).clicked = true;
                switch (buttons.get(index).property) {
                    case "red":
                        try {
                            buttons.get(index).button.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/red_man.jpg").toURI().toString(), null, BackgroundRepeat.NO_REPEAT, width / 6.5245, height / 6.9333, height));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        counterText.set("Cards remaining:\nRed: " + (--redRemaining) + "/" + totalRed +
                                "\nBlue: " + blueRemaining + "/" + totalBlue
                                + "\nBystanders: " + byRemaining + "/7");
                        break;
                    case "blue":
                        try {
                            buttons.get(index).button.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/blue_man.png").toURI().toString(), null, BackgroundRepeat.NO_REPEAT, width / 6.5245, height / 6.9333, height));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        counterText.set("Cards remaining:\nRed: " + redRemaining + "/" + totalRed +
                                "\nBlue: " + (--blueRemaining) + "/" + totalBlue
                                + "\nBystanders: " + byRemaining + "/7");
                        break;
                    case "bystanders":
                        try {
                            buttons.get(index).button.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/bystander_man.png").toURI().toString(), null, BackgroundRepeat.NO_REPEAT, width / 6.5245, height / 6.9333, height));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        counterText.set("Cards remaining:\nRed: " + redRemaining + "/" + totalRed +
                                "\nBlue: " + blueRemaining + "/" + totalBlue
                                + "\nBystanders: " + (--byRemaining) + "/7");
                        break;
                    case "assassin":
                        try {
                            buttons.get(index).button.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/assassin.png").toURI().toString(), null, BackgroundRepeat.NO_REPEAT, width / 6.5245, height / 6.9333, height));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                if (buttons.get(index).property.equals(plays)) {
                    if (!Main.mMuteS) sounds.yesSound();
                } else if (buttons.get(index).property.equalsIgnoreCase("assassin")) {
                    if (!Main.mMuteS)sounds.assassin();
                } else if (buttons.get(index).property.equalsIgnoreCase("bystanders")) {
                    done.fire();
                    if (!Main.mMuteS)sounds.bystanderSound();
                } else {
                    done.fire();
                    if (!Main.mMuteS) sounds.noSound();
                }
                buttons.get(index).button.setTextFill(Color.WHITE);
                buttons.get(index).button.setAlignment(Pos.BOTTOM_CENTER);
                gameCheck(buttons.get(index), blueRemaining, redRemaining, main, primaryStage, width, height,restart);
            });
            buttons.get(index).button.setOnMouseEntered(event2 -> {
                if (!Main.mMuteS) sounds.mouseEnteredSound();
            });

            if (getColumn() == 4) {
                setColumn(0);
                setRow(getRow() + 1);
            } else setColumn(getColumn() + 1);
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(buttons.size());
        timeline.play();

        topL.setMinSize(width / 19.9, height / 34.6666);   //100,30
        bottomL.setMinSize(width / 19.9, height / 34.6666);
        leftL.setMinSize(width / 66.3333, height / 10.4);  //30,100
        rightL.setMinSize(width / 66.3333, height / 10.4);
        bottomL.setBackground(topL.getBackground());
        bottomL.backgroundProperty().bind(topL.backgroundProperty());
        leftL.setBackground(topL.getBackground());
        leftL.backgroundProperty().bind(topL.backgroundProperty());
        rightL.setBackground(topL.getBackground());
        rightL.backgroundProperty().bind(topL.backgroundProperty());

        Label counter = new Label();
        counter.setTextFill(Color.WHITE);
        counterText.set("Cards remaining:\nRed: " + redRemaining + "/" + totalRed +
                "\nBlue: " + blueRemaining + "/" + totalBlue
                + "\nBystanders: " + byRemaining + "/7");
        counter.textProperty().bind(counterText);
        Tooltip counterTooltip = new Tooltip();
        counterTooltip.textProperty().bind(counterText);
        counterTooltip.setFont(new Font(40));
        counterTooltip.setHideDelay(Duration.ZERO);
        counter.setTooltip(counterTooltip);
        counter.setMinWidth(width / 39.8);    //50

        done.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        showKey.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));

        done.setOnAction(event -> {
            if (topL.getBackground().equals(redBG)) topL.setBackground(blueBG);
            else topL.setBackground(redBG);

            if (plays.equals("blue")) {
                plays = "red";
                primaryStage.setTitle(primaryStage.getTitle().replace("blue", "red"));
            } else {
                plays = "blue";
                primaryStage.setTitle(primaryStage.getTitle().replace("red", "blue"));
            }
        });

        showKey.setOnAction(event -> {
            key(buttons, firstTeam, width, height);
        });

        grid.setHgap(width / 106.1333);   //30
        grid.setVgap(height / 52);    //20
        grid.setPadding(new Insets(height / 20.8, width / 39.8, height / 20.8, width / 19.9));     //50,50,50,100

        Button newGame = new Button("New Game");
        newGame.setFont(Font.font(showKey.getFont().getSize()-1));
        newGame.setOnAction(event -> {
            if (alertCloseBoxYesNo()) {
                bp.setDisable(true);
                restart.fire();
                primaryStage.close();
            }
        });
        VBox vBox = new VBox(height / 52, counter, newGame,showKey, done, leftL);     //20

        bp.setCenter(grid);
        bp.setTop(topL);
        bp.setRight(rightL);
        topL.setTranslateX(width / 2 - topL.getMinWidth() / 2);
        Button muteM = new Button("Mute Music");
        muteM.setOnAction(event -> {
            if (Main.mSong.isMute()) {
                Main.mSong.setMute(false);
            }else Main.mSong.setMute(true);
        });
        Button muteS = new Button("Mute Sounds");
        muteS.setOnAction(event -> {
            if (Main.mMuteS) {
                Main.mMuteS = false;
            } else Main.mMuteS = true;
        });
        bottomL.setTranslateX(width / 2 - topL.getMinWidth() / 2);
        HBox hBox = new HBox(muteM, muteS,bottomL );
        muteS.setTranslateX(20);
        bp.setBottom(hBox);
        rightL.setTranslateY(height / 2 - height / 10.4);
        leftL.setTranslateY(height/5.7777);

        bp.setPadding(new Insets(height / 104, width / 199, height / 104, width / 199));
        bp.setLeft(vBox);

        Scene scene = new Scene(bp);
        primaryStage.setX(Screen.getPrimary().getVisualBounds().getMinX());
        primaryStage.setY(Screen.getPrimary().getVisualBounds().getMinY());
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Playing field: -> ");
        if (plays.equals("blue")) {
            primaryStage.setTitle(primaryStage.getTitle().concat("blue"));
        } else primaryStage.setTitle(primaryStage.getTitle().concat("red"));
        primaryStage.show();
        bottomL.setTranslateX(bottomL.getTranslateX()-muteM.getWidth()-muteS.getWidth());
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            if (alertCloseBoxYesNo()) {
                primaryStage.close();
                main.show();
            }
        });
    }

    private int getColumn() {
        return column;
    }
    private void setColumn(int column) {
        this.column = column;
    }
    private int getRow() {
        return row;
    }
    private void setRow(int row) {
        this.row = row;
    }
    private void setJar(JarFile jar) {
        this.jar = jar;
    }
    private void setButtons(String plays) {
        ArrayList<Buttons> buttons = new ArrayList<>();
        if (Main.mRunningFromJar) {
            ArrayList<String> allFolder = new ArrayList<>();
            Enumeration<JarEntry> enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();
                if (entry.getName().endsWith(".txt") && !entry.isDirectory() && entry.getName().startsWith("W2/")) {
                    allFolder.add(entry.getName());
                }
            }

            int filesAdded = 0;
            while (filesAdded < 25) {
                int random = (int) (Math.random() * allFolder.size());
                buttons.add(new Buttons());
                buttons.get(buttons.size() - 1).name = allFolder.get(random).replace(".txt", "").substring(3);
                buttons.get(buttons.size() - 1).filePath = allFolder.get(random);
                allFolder.remove(random);
                filesAdded++;
            }
        }else {
            File path = new File(Main.mClassPath +"/W2/");
            File[] folder = path.listFiles();
            int filesAdded = 0;
            while (filesAdded < 25) {
                int random = (int) (Math.random() * folder.length);
                if (folder[random] != null) {
                    buttons.add(new Buttons());
                    buttons.get(buttons.size() - 1).file = folder[random];
                    buttons.get(buttons.size() - 1).name = buttons.get(buttons.size() - 1).file.getName().replace(".txt", "");
                    buttons.get(buttons.size() - 1).filePath = buttons.get(buttons.size() - 1).file.getPath();
                    folder[random] = null;
                    filesAdded++;
                }
            }
        }


        if (plays.equalsIgnoreCase("blue")) {
            //blue first
            blueRemaining = 9;
            totalBlue = 9;
            redRemaining = 8;
            totalRed = 8;
            for (int i = 0; i <= 8; i++) {
                buttons.get(i).property = "blue";
            }
            for (int i = 9; i <= 16; i++) {
                buttons.get(i).property = "red";
            }
        } else {
            //red first
            redRemaining = 9;
            totalRed = 9;
            blueRemaining = 8;
            totalBlue = 8;

            for (int i = 0; i <= 8; i++) {
                buttons.get(i).property = "red";
            }
            for (int i = 9; i <= 16; i++) {
                buttons.get(i).property = "blue";
            }
        }
        for (int i = 17; i <= 23; i++) {
            buttons.get(i).property = "bystanders";
        }
        buttons.get(24).property = "assassin";

//shuffle
        for (int i = 0; i < buttons.size(); i++) {
            int random = (int) (Math.random() * buttons.size() - 1);
            Buttons temp = buttons.get(i);
            Buttons randTemp = buttons.get(random);
            buttons.set(i, randTemp);
            buttons.set(random, temp);
        }
        this.buttons = buttons;
    }
    private static String plays = null;
    private void key(ArrayList<Buttons> buttons, String firstTeam, Double width, Double height) {
        GridPane grid = new GridPane();
        ArrayList<Label> labels = new ArrayList<>();
        CornerRadii radii = new CornerRadii(20);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int index = i * 5 + j;
                labels.add(new Label());
                labels.get(index).setText(buttons.get(i * 5 + j).name);
                labels.get(index).alignmentProperty().set(Pos.CENTER);

                switch (buttons.get(index).property) {
                    case "red":
                        labels.get(index).setBackground(new Background(new BackgroundFill(Color.RED, radii, Insets.EMPTY)));
                        break;
                    case "blue":
                        labels.get(index).setBackground(new Background(new BackgroundFill(Color.BLUE, radii, Insets.EMPTY)));
                        labels.get(index).setTextFill(Color.WHITE);
                        break;
                    case "bystanders":
                        labels.get(index).setBackground(new Background(new BackgroundFill(Color.BISQUE, radii, Insets.EMPTY)));
                        break;
                    case "assassin":
                        labels.get(index).setBackground(new Background(new BackgroundFill(Color.BLACK, radii, Insets.EMPTY)));
                        labels.get(index).setTextFill(Color.WHITE);
                        break;
                }
                GridPane.setConstraints(labels.get(index), j, i);
                grid.getChildren().add(labels.get(index));

                labels.get(index).setMaxSize(width/5.8181, height/6.9333);  //330,150
                labels.get(index).setMinSize(width/5.8181, height/6.9333);  //330,150
            }
        }

        grid.setHgap(width / 106.1333);   //30
        grid.setVgap(height / 52);    //20
        grid.setPadding(new Insets(height / 20.8, width / 19.9, height / 10.4, width / 39.8));     //50,100,100,50

        Stage primaryStage = new Stage();
        Label topL = new Label();
        Label bottomL = new Label();
        Label leftL = new Label();
        Label rightL = new Label();
        bottomL.backgroundProperty().bind(topL.backgroundProperty());
        leftL.backgroundProperty().bind(topL.backgroundProperty());
        rightL.backgroundProperty().bind(topL.backgroundProperty());

        if (firstTeam.equalsIgnoreCase("blue")) {
            primaryStage.setTitle("BLUE TEAM PLAYS FIRST");
            topL.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            topL.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            primaryStage.setTitle("RED TEAM PLAYS FIRST");
        }
        BorderPane bp = new BorderPane();
        bp.setCenter(grid);
        bp.setLeft(leftL);
        bp.setRight(rightL);
        bp.setTop(topL);
        bp.setBottom(bottomL);
        topL.setMinSize(width / 19.9, height / 34.6666);   //100,30
        bottomL.setMinSize(width / 19.9, height / 34.6666);
        leftL.setMinSize(width / 66.3333, height / 10.4);  //30,100
        rightL.setMinSize(width / 66.3333, height / 10.4);
        topL.setTranslateX(width / 2 - width / 23.41117);     //85
        bottomL.setTranslateX(topL.getTranslateX());
        rightL.setTranslateY(height / 2 - height / 8.6666);       //120
        leftL.setTranslateY(rightL.getTranslateY());
        bp.setPadding(new Insets(height / 104, width / 199, height / 104, width / 199));

        Scene scene = new Scene(bp);
        primaryStage.setScene(scene);
        try {
            primaryStage.getIcons().add(new Image(this.getClass().getResource("IMAGES/key.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        primaryStage.setMaximized(true);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();

        for (int i = 0; i < labels.size(); i++) {
            Text text = new Text(labels.get(i).getText());
            text.setFont(Font.font("Arial",FontWeight.BOLD,60));
            while (text.getBoundsInLocal().getWidth() > labels.get(i).getLayoutBounds().getWidth()) {
                text.setFont(Font.font("Arial",FontWeight.BOLD,text.getFont().getSize()-1));
            }
            labels.get(i).setFont(Font.font("Arial",FontWeight.BOLD,text.getFont().getSize()-5));
        }
    }
    private void gameCheck(Buttons button, int blue, int red, Stage main, Stage multiPlayer, Double width, Double height,Button restart) {
        if (button.property.equalsIgnoreCase("assassin") || blue == 0 || red == 0) {
            if(info(multiPlayer,main,restart)) {
                for (int i = 0; i < buttons.size(); i++) {
                    if (!buttons.get(i).clicked) {
                        switch (buttons.get(i).property) {
                            case "red":
                                try {
                                    buttons.get(i).button.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/red_man.jpg").toURI().toString(), null, BackgroundRepeat.NO_REPEAT, width / 6.5245, height / 6.9333, height));
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "blue":
                                try {
                                    buttons.get(i).button.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/blue_man.png").toURI().toString(), null, BackgroundRepeat.NO_REPEAT, width / 6.5245, height / 6.9333, height));
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "bystanders":
                                try {
                                    buttons.get(i).button.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/bystander_man.png").toURI().toString(), null, BackgroundRepeat.NO_REPEAT, width / 6.5245, height / 6.9333, height));
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "assassin":
                                try {
                                    buttons.get(i).button.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/assassin.png").toURI().toString(), null, BackgroundRepeat.NO_REPEAT, width / 6.5245, height / 6.9333, height));
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        buttons.get(i).button.setTextFill(Color.GREENYELLOW);
                        buttons.get(i).button.setAlignment(Pos.BOTTOM_CENTER);
                        buttons.get(i).button.setOpacity(0.6);
                    }
                    buttons.get(i).button.setOnAction(event -> event.consume());
                    buttons.get(i).button.setOnMouseEntered(event -> event.consume());
                }
            }
        }
    }
    private boolean info(Stage multiPlayer,Stage main, Button restart) {
        Stage alert = new Stage();
        VBox vBox = new VBox(10);
        Label label = new Label("Game over");
        Button ok = new Button("Back to the main window");
        Button review = new Button("Stay in this window");
        Button newGame = new Button("Start a new Game");
        boolean[] a = {false};
        ok.setOnAction(event -> {
            alert.close();
            multiPlayer.close();
            main.show();
        });
        review.setOnAction(event -> {
            alert.close();
            a[0] = true;
        });
        newGame.setOnAction(event -> {
            alert.close();
            multiPlayer.close();
            restart.fire();
        });
        vBox.getChildren().addAll(label, ok, review,newGame);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 250, 250);
        alert.setScene(scene);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        return a[0];
    }
    private  boolean alertCloseBoxYesNo() {
        boolean[] close = new boolean[1];
        Stage alert = new Stage();
        VBox vBox = new VBox(20);
        Label label = new Label("Are you sure you want to exit ?");
        Button yes = new Button("Yes");
        Button no = new Button("No");
        label.setFont(Font.font(25));
        yes.setFont(Font.font(20));
        no.setFont(Font.font(20));
//        vBox.autosize();
        no.setOnAction(event -> {
            alert.close();
            close[0] = false;
        });
        yes.setOnAction(event -> {
            alert.close();
            close[0] = true;
        });
        vBox.getChildren().addAll(label, yes, no);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 500, 350);
        alert.setScene(scene);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOnCloseRequest(event -> no.fire());
        alert.showAndWait();
        return close[0];
    }

}