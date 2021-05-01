import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class SinglePlayer{
    private ArrayList<Buttons> buttons = new ArrayList<>();
    private ArrayList<String> otherCards = new ArrayList<>();
    private ArrayList<String> guessFolder = new ArrayList<>();
    private ArrayList<String> clues = new ArrayList<>();
    private ArrayList<String> tempClues = new ArrayList<>();
    private boolean gameOver;
    private int letterIndex;
    private String plays;
    private int row = 0;
    private int column = 0;
    private String clue;
    private int clicker;
    private int addedNumber;
    private boolean physicalPlayer;
    private boolean badClue;
    private String firstTeam;
    private boolean blink;
    private boolean clueIsCardName;
    private int oppHasZero;
    private ArrayList<Group> grid = new ArrayList<>();
    private ArrayList<Label> labels = new ArrayList<>();
    private double positionX = 0;
    private double positionY = 0;
    private Label hand = new Label();
    private Label handOfGod = new Label();
    private int blueRemaining;
    private int redRemaining;
    private int byRemaining = 7;
    private int totalBlue;
    private int totalRed;
    private StringProperty counterText = new SimpleStringProperty();
    private double handX;
    private double handY;
    private Button b;
    private int endX;
    private int endY;
    private String buttonNameTemp;
    private Button newGame;
    private Menu menu = new Menu("Settings");
    private JarFile jar;

    SinglePlayer(Stage main, String player, String playersName, boolean first, double width, double height, boolean cleverMaster, Button restart, String jarPath,JarFile jar) {
        setJar(jar);
        newGame = restart;
        Sounds correct = new Sounds();
        Sounds incorrect = new Sounds();
        Sounds assassin = new Sounds();
        Sounds bystander = new Sounds();
        Sounds mouseEntered = new Sounds();
        Sounds message = new Sounds();

        Image image = null;
        try {
            image = new Image(this.getClass().getResource("IMAGES/hand2.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width / 13.7142); //140
        imageView.setFitHeight(height / 5.2);    //200
        hand.setGraphic(imageView);

        Stage playingWindow = new Stage();
        String opponent;
        if (player.equalsIgnoreCase("red")) opponent = "blue";
        else opponent = "red";

        if (first) {
            setFirstTeam(player);
            setPlays(opponent);
        } else {
            if (player.equalsIgnoreCase("red")) {
                setFirstTeam("blue");
                setPlays("red");
            } else {
                setFirstTeam("red");
                setPlays("blue");
            }
        }

        setButtons(getFirstTeam());     //details bellow ↓↓↓
        playingWindow.setTitle(playersName + " YOU PLAY AS " + player.toUpperCase() + "       Playing field for ->" + firstTeam);

        GridPane left = new GridPane();
        GridPane right = new GridPane();
        Pane bp = new Pane();

        Label redLabel = new Label();
        Label blueLabel = new Label();
        Label redText = new Label();
        Label blueText = new Label();
        Label side = new Label();
        side.setText(playersName + "\nthis is your side.\nThe spymasters\nare ");
        if (cleverMaster) side.setText(side.getText().concat("clever."));
        else side.setText(side.getText().concat("NOT clever."));

        Label counter = new Label();    //shows the number of remaining cards and their property
        counter.setTextFill(Color.WHITE);
        counterText.set("Cards remaining:\nRed: " + redRemaining + "/" + totalRed +
                "\nBlue: " + blueRemaining + "/" + totalBlue
                + "\nBystanders: " + byRemaining + "/7");
        counter.textProperty().bind(counterText);
        Tooltip counterTooltip = new Tooltip();
        counterTooltip.textProperty().bind(counterText);
        counterTooltip.setFont(new Font(width / 48));
        counterTooltip.setHideDelay(Duration.ZERO);
        counter.setTooltip(counterTooltip);



        side.setTextFill(Color.WHITE);
        side.setFont(new Font(width / 64)); //30

        Button done = new Button("Done");   //when there are no more guesses, click to switch play
        done.setFont(new Font(height / 86.6666));   //12

        GridPane.setConstraints(redLabel, 1, 1);
        GridPane.setConstraints(redText, 1, 1);
        GridPane.setConstraints(blueLabel, 1, 1);
        GridPane.setConstraints(blueText, 1, 1);
        GridPane.setConstraints(done, 1, 0);
        GridPane.setConstraints(side, 1, 2);
        GridPane.setConstraints(counter, 1, 3);

        redText.setTranslateX(width / 54.8571);  //35
        redText.setTranslateY(-1 * height / 16); //-65
        redText.setMaxWidth(width / 10.9714);   //175

        blueText.setTranslateX(width / 56.8571);
        blueText.setTranslateY(-1 * height / 16);   //-65
        blueText.setMaxWidth(width / 10.9714);

        double textSize = redText.getMaxWidth() / 10; //size 17 looks ok in a 21in screen
        Font textFont = Font.font("Arial", textSize);
        redText.setFont(textFont);  //17
        blueText.setFont(textFont);



        Tooltip auxRedText = new Tooltip();     //mouse over the phone image to show its content in case the fonts are too small
        Tooltip auxBlueText = new Tooltip();
        auxBlueText.setShowDelay(Duration.seconds(0.5));
        auxRedText.setShowDelay(Duration.seconds(0.5));
        auxBlueText.setHideDelay(Duration.seconds(0));
        auxRedText.setHideDelay(Duration.seconds(0));
        auxRedText.textProperty().bind(redText.textProperty());
        auxBlueText.textProperty().bind(blueText.textProperty());
        auxRedText.setFont(new Font(height / 20.8));   //50
        auxBlueText.setFont(new Font(height / 20.8));
        redText.setTooltip(auxRedText);
        blueText.setTooltip(auxBlueText);
        redText.setAlignment(Pos.TOP_LEFT);
        blueText.setAlignment(Pos.TOP_LEFT);
        redText.setTextOverrun(OverrunStyle.LEADING_WORD_ELLIPSIS);
        redText.setWrapText(true);
        blueText.setTextOverrun(OverrunStyle.LEADING_WORD_ELLIPSIS);
        blueText.setWrapText(true);
        redText.setMaxHeight(height / 4.72);
        blueText.setMaxHeight(height / 4.72);//421
        redLabel.setMinSize(width / 7.68, height / 1.8909);  //250,550
        blueLabel.setMinSize(width / 7.68, height / 1.8909);


        try {   //phones and wooden board backgrounds
            redLabel.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/mobilePhone.png").toURI().toString(), Color.RED, BackgroundRepeat.NO_REPEAT, width / 7.68, height / 1.8909, height));
            blueLabel.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/mobilePhone.png").toURI().toString(), Color.BLUE, BackgroundRepeat.NO_REPEAT, width / 7.68, height / 1.8909, height));
            bp.setBackground(Images.setBgWPhoto(this.getClass().getResource("IMAGES/wood.jpeg").toURI().toString(), null, BackgroundRepeat.REPEAT));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        done.setTranslateX(width / 19.2);   //100
        done.setDisable(true);
        done.setMinSize(width / 21.333, height / 26); //90,40

        left.setVgap(height / 20.8);
        right.setVgap(height / 20.8);
        left.setOpacity(1);
        right.setOpacity(1);
        right.setLayoutX(width - blueLabel.getMinWidth());


        bp.getChildren().add(counter);
        handOfGod.setVisible(false);   //this is visible only when there is a free card awarded
        if (player.equalsIgnoreCase("red")) {   //buttons are assigned to left or right according to preference or chance
            left.getChildren().addAll(redLabel, redText, done, side);
            right.getChildren().addAll(blueLabel, blueText);
            right.setTranslateY(height / 20.8);    //50
            counter.setLayoutX(width / 1.1034);   //1740
            counter.setLayoutY(height / 26); //40
            setHandX(width - width / 106.6666 - imageView.getFitWidth()); //18
            setHandY(height / 1.4857);  //700
            hand.setLayoutX(getHandX());
            hand.setLayoutY(getHandY());
        } else {
            left.getChildren().addAll(redLabel, redText);
            right.getChildren().addAll(blueLabel, blueText, done, side);
            left.setTranslateY(height / 20.8);    //50
            counter.setLayoutY(height / 26);     //40
            counter.setLayoutX(width / 21.3333);     //90
            setHandX(width / width / 106.6666);   //18
            setHandY(height / 1.4857);    //700
            hand.setLayoutX(getHandX());
            hand.setLayoutY(getHandY());
        }


        double xSpacing = width / 71.1111;    //27
        double ySpacing = height / 52;    //20
        left.setLayoutX(width / 128);    //15
        left.setLayoutY(height / 26);    //40
        right.setLayoutX(width - width / 128 - blueLabel.getMinWidth());    //15
        right.setLayoutY(height / 26);   //40
        setPositionY(height / 26);   //26
        setPositionX(width / 79.6);  //25

        Image cursorFile = null;    //mouse cursor when the mouse pointer is hovering over a card
        try {
            cursorFile = new Image(this.getClass().getResource("IMAGES/hand2.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageCursor ic = new ImageCursor(cursorFile);

        Text text = new Text();
        Timeline timeline = new Timeline();     //animation for laying over the cards one by one
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.1), event -> {
            int index = getRow() * 5 + getColumn();

            Label label = new Label(buttons.get(index).name);  //naming them and spacing them
            int num = 28;
            label.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, num));    //30   height/34.6666
            label.setTextAlignment(TextAlignment.LEFT);
            label.setAlignment(Pos.CENTER_LEFT);
            label.setContentDisplay(ContentDisplay.TEXT_ONLY);
            label.setTranslateY(height / 10.4);   //100
            label.setTranslateX(width / 83.4782);   //23
            label.setMaxWidth(width / 10.1052); //190

            text.setText(label.getText());
            text.setFont(label.getFont());  //adjust the size of the font to fit the card
            while (text.getBoundsInLocal().getWidth() > label.getMaxWidth()) {
                num--;
                label.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, num));
                text.setFont(label.getFont());
            }
            labels.add(label);     //labels are added to a list to reference them when the game is over

            //using a tooltip for cards in case the fonts turn out to be very small
            // i.e when the word is too big to fit and will be significantly resized
            Tooltip tooltip = new Tooltip(label.getText());
            label.setTooltip(tooltip);
            tooltip.setShowDelay(Duration.seconds(0.5));
            tooltip.setHideDelay(Duration.ZERO);
            tooltip.setFont(Font.font(height / 20.8));
            buttons.get(index).button = new Button();
            buttons.get(index).button.setMinSize(width / 7.8688, height / 6.07831); //253,171.1
            try {
                buttons.get(index).button.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/unknown.jpg").toURI().toString(), null, BackgroundRepeat.NO_REPEAT, width/7.8688, height / 6.07831, height));  //250
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            buttons.get(index).button.setTooltip(tooltip);

            setPositionX(getPositionX() + buttons.get(index).button.getMinWidth() + xSpacing);
            //grouping each label to its corresponding button, in order to function as one, for example when 1 of the 2 is clicked
            Group group = new Group(buttons.get(index).button, label);

            //laying the cards as a 5x5 grid like the actual board game
            if (index % 5 == 0 && index > 0) {
                setPositionY(getPositionY() + buttons.get(index).button.getMinHeight() + ySpacing);
                setPositionX(grid.get(0).getLayoutX());
            }
            grid.add(group);
            bp.getChildren().add(group);
            group.setDisable(true);
            group.setOpacity(0.9);
            group.setLayoutX(getPositionX());
            group.setLayoutY(getPositionY());
            group.setCursor(ic);

            label.opacityProperty().bind(group.opacityProperty());

            Effect origEff = group.getEffect(); //to reset the effect that will be used when onMouseEntered()
            group.setOnMouseExited(event1 -> {
                group.setEffect(origEff);
            });
            group.setOnMouseEntered(event1 -> {
                if (!Main.mMuteS) mouseEntered.mouseEnteredSound();
                //distinguish the card the mouse is over, from the other cards
                Light.Distant distant = new Light.Distant();
                distant.setColor(Color.WHITE);
                Lighting lighting = new Lighting(distant);
                group.setEffect(lighting);
            });

            buttons.get(index).button.opacityProperty().bind(group.opacityProperty());

            buttons.get(index).button.setOnAction(event1 -> {
                label.setTextFill(Color.WHITE); //white text looks better on black and blue cards
                buttons.get(index).clicked = true;
                switch (buttons.get(index).property) {
                    case "red":
                        String path;
                        if (Math.random() > 0.5) path = "IMAGES/red_man.jpg"; //randomly choose the male or female avatar of the card
                        else path = "IMAGES/red_woman.jpg";                   //to load
                        try {
                            buttons.get(index).button.setBackground(Images.setPhotoBG(this.getClass().getResource(path).toURI().toString(), Color.RED, BackgroundRepeat.NO_REPEAT,width / 7.8688, height / 6.07831,height));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        counterText.set("Cards remaining:\nRed: " + (--redRemaining) + "/" + totalRed +
                                "\nBlue: " + blueRemaining + "/" + totalBlue
                                + "\nBystanders: " + byRemaining + "/7");
                        break;
                    case "blue":
                        if (Math.random() > 0.5) path = "IMAGES/blue_man.png";
                        else path = "IMAGES/blue_woman.png";
                        try {
                            buttons.get(index).button.setBackground(Images.setPhotoBG(this.getClass().getResource(path).toURI().toString(), Color.BLUE, BackgroundRepeat.NO_REPEAT,width / 7.8688, height / 6.07831,height));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        counterText.set("Cards remaining:\nRed: " + redRemaining + "/" + totalRed +
                                "\nBlue: " + (--blueRemaining) + "/" + totalBlue
                                + "\nBystanders: " + byRemaining + "/7");
                        break;
                    case "bystanders":
                        if (Math.random() > 0.5) path = "IMAGES/bystander_man.png";
                        else path = "IMAGES/bystander_woman.png";
                        try {
                            buttons.get(index).button.setBackground(Images.setPhotoBG(this.getClass().getResource(path).toURI().toString(), Color.WHITE, BackgroundRepeat.NO_REPEAT,width / 7.8688, height / 6.07831,height));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        counterText.set("Cards remaining:\nRed: " + redRemaining + "/" + totalRed +
                                "\nBlue: " + blueRemaining + "/" + totalBlue
                                + "\nBystanders: " + (--byRemaining) + "/7");
                        break;
                    case "assassin":
                        try {
                            buttons.get(index).button.setBackground(Images.setPhotoBG(this.getClass().getResource("IMAGES/assassin.png").toURI().toString() , Color.BLACK, BackgroundRepeat.NO_REPEAT,width / 7.8688, height / 6.07831,height));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                buttons.get(index).button.setTextFill(Color.WHITE);
                buttons.get(index).button.setAlignment(Pos.BOTTOM_CENTER);


                if (buttons.get(index).property.equals(getPlays())) {
                    //correct sound
                    if (!Main.mMuteS) correct.yesSound();
                    gameCheck(getFirstTeam(), player, playersName, main, playingWindow, done);
                } else if (buttons.get(index).property.equalsIgnoreCase("assassin")) {
                    //shotgun sound
                    if (!Main.mMuteS) assassin.assassin();
                    gameCheck(getFirstTeam(), player, playersName, main, playingWindow, done);
                } else if (buttons.get(index).property.equalsIgnoreCase("bystanders")) {

                    //innocent sound
                    if (!Main.mMuteS) bystander.bystanderSound();
                    if (isPhysicalPlayer()) {  //when the human player selects a bystander, a window will pop up
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                String message = "You hit a bystander";
                                infoBox(message);
                                done.setDisable(false);
                                done.fire();
                            }
                        });
                    }
                } else {
                    //wrong sound
                    gameCheck(getFirstTeam(), player, playersName, main, playingWindow, done);
                    if (!isGameOver()) {
                        if (isPhysicalPlayer()) {
                            if (!Main.mMuteS) incorrect.noSound();
                            gameCheck(getFirstTeam(), player, playersName, main, playingWindow, done);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() { //when the human player selects an opponent's, a window will pop up
                                    String message = "You hit an opposing agent";
                                    infoBox(message);
                                    done.setDisable(false);
                                    done.fire();
                                }
                            });
                        }
                    }
                }

                setClicker(getClicker() + 1);
                flowCheck(done); //see method's source code for details ↓↓↓
                group.setEffect(origEff);
                group.setDisable(true);
            });
            group.setOnMouseClicked(event1 -> {
                buttons.get(index).button.fire();
            });
            if (getColumn() == 4) { //laying the cards as a 5x5 grid like the actual board game
                setColumn(0);
                setRow(getRow() + 1);
            } else setColumn(getColumn() + 1);
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(buttons.size());
        timeline.play();

        timeline.setOnFinished(event -> { //when the card placing animation is finished the second player set as first
            done.setDisable(false);       //in the beginning, will be switched by the first player
            done.fire();
        });


        done.setOnAction(event -> {     //when the button is pressed
            done.setDisable(true);      //it is disabled to avoid double clicking
            setBlink(false);            //stop the blinking of that button if its happening
            setClicker(0);              //resetting how many times a card have been chosen
            setAddedNumber(0);          //and set how many clicks the next player is allowed
            if (getPlays().equalsIgnoreCase("blue")) {
                blueLabel.setDisable(true);
                redLabel.setDisable(false);
                setPlays("red");
                playingWindow.setTitle(playingWindow.getTitle().replace("blue", "red"));
            } else {
                redLabel.setDisable(true);
                blueLabel.setDisable(false);
                setPlays("blue");
                playingWindow.setTitle(playingWindow.getTitle().replace("red", "blue"));
            }
            //if the computer is about to select cards, the layout is disabled to prevent the human player from clicking any cards
            if (getPlays().equalsIgnoreCase(opponent)) {
                pauseGame();
            }
            if (getPlays().equalsIgnoreCase(player)) setPhysicalPlayer(true);
            else setPhysicalPlayer(false);    //this boolean variable will help with pop ups ↑↑↑

            setClues(getPlays(), cleverMaster);     //see method  ↓↓↓

            String substring = "";   //clues in the mobile phone image are separated by new lines,that separation will
            if (!isGameOver()) {     //cause problems if left in the string which will be used for searching the files
                if (getPlays().equalsIgnoreCase("blue")) {
                    if (clues.isEmpty()) {  //if this is the first clue there is no separation
                        setClue("\nCan't give anymore\nclues, I GIVE UP");
                        gameCheck("blueGaveup", player, playersName, main, playingWindow, done);
                        //in case something is wrong with finding that clue in the file, the team will surrender
                        //so the program doesn't crash
                    } else {//if it is not the first, there will be a '\n' in the beginning of the string
                        substring = clues.get(0).substring(0, clues.get(0).length() - 2);
                        while (blueText.getText().contains(substring)) {
                            clues.remove(0);  //removing used clues will prevent spymaster from repeating himself
                            //(however, if he is set as an idiot, he might repeat the opposing spymaster)
                            substring = clues.get(0).substring(0, clues.get(0).length() - 2);
                        }
                        if (blueText.getText().equalsIgnoreCase("")) setClue(clues.get(0));
                        else setClue("\n" + clues.get(0));
                    }
                } else {
                    if (clues.isEmpty()) {
                        setClue("\nCan't give anymore\nclues, I GIVE UP");
                        gameCheck("redGaveUp", player, playersName, main, playingWindow, done);

                    } else {
                        substring = clues.get(0).substring(0, clues.get(0).length() - 2);
                        while (redText.getText().contains(substring)) {
                            clues.remove(0);
                            substring = clues.get(0).substring(0, clues.get(0).length() - 2);
                        }
                        if (redText.getText().equalsIgnoreCase("")) setClue(clues.get(0));
                        else setClue("\n" + clues.get(0));
                    }
                }
            }

            //as a rule of the game the spymaster might give infinity or zero as a number of guesses, which means unlimited choices
            //even though this is implemented, the chance of it actually happening is very low.
            //This will happen only if more than 1 remaining cards have a common clue
            try {
                if (getClue().charAt(getClue().length() - 1) == '∞') {
                    setAddedNumber(getOppHasZero());
                }else if (Integer.parseInt(Character.toString(getClue().charAt(getClue().length() - 1))) == 0) {
                    setAddedNumber(getOppHasZero());
                } else setAddedNumber(Integer.parseInt(Character.toString(getClue().charAt(clue.length() - 1))));
            } catch (NumberFormatException e) {
                System.out.println("Exception with 'give up'");
            }


            setClueIsCardName(false);   //it is against the rules of the game to refer to cards by naming them or saying,
            String clueCheck = "";      //for example, "rome" when there is a card "romeo" on the table
            String badClue = "";
            String cardMatcing = "";
            for (int i = 0; i < buttons.size(); i++) {
                if (getClue().charAt(0) == '\n') clueCheck = getClue().substring(1, getClue().length() - 2);
                else clueCheck = getClue().substring(0, getClue().length() - 2);
                if (clueCheck.equalsIgnoreCase(buttons.get(i).name) || clueCheck.startsWith(buttons.get(i).name) || clueCheck.endsWith(buttons.get(i).name)) {
                    badClue = clueCheck;
                    cardMatcing = buttons.get(i).name;
                    setClueIsCardName(true);
                }
            }

            setBadClue(false);   //checking whether the clue has been given before
            if ((redText.getText().contains(clueCheck)) || blueText.getText().contains(clueCheck))
                setBadClue(true);

            if (!Main.mMuteS) {
                message.messageSound();
            }

            setLetterIndex(0);
            Timeline letterAnimation = new Timeline();      //animation of letters appearing one by one
            KeyFrame letterKeyFrame = new KeyFrame(
                    Duration.seconds(0.1),
                    event1 -> {
                        if (getPlays().equalsIgnoreCase("blue")) {
                            blueText.setText(blueText.getText().concat(Character.toString(getClue().charAt(getLetterIndex()))));
                            setLetterIndex(getLetterIndex() + 1);
                        } else {
                            redText.setText(redText.getText().concat(Character.toString(getClue().charAt(getLetterIndex()))));
                            setLetterIndex(getLetterIndex() + 1);
                        }
                    });
            letterAnimation.getKeyFrames().add(letterKeyFrame);
            letterAnimation.setCycleCount(getClue().length());
            letterAnimation.setDelay(Duration.seconds(1));
            letterAnimation.play();

            String finalBadClue = badClue;
            String finalCardMatcing = cardMatcing;
            letterAnimation.setOnFinished(new javafx.event.EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) { //in case there is a mistake with the clues a pop up will explain
                    Platform.runLater(new Runnable() {  //the mistake
                        @Override
                        public void run() {
                            if (isGameOver()) bp.setDisable(true);
                            else {
                                String otherTeam;
                                if (getPlays().equalsIgnoreCase("red")) otherTeam = "blue";
                                else otherTeam = "red";

                                if (isClueIsCardName()) {
                                    String message = "The " + getPlays() + " spymaster gave " + finalBadClue +
                                            " as a clue, which \n" +
                                            " resembles the card " + finalCardMatcing + " on the table. " +
                                            "Free \ncard is awarder to the " + otherTeam + " team.";
                                    infoBox(message);
                                    badCluePlayed(plays, buttons, done, player, playersName, main, playingWindow);
                                } else if (isBadClue()) {
                                    String message = "The " + getPlays() + " spymaster made a mistake. He gave a clue\n" +
                                            " that was already given. Free Card is awarded\n to the " + otherTeam + " team.";
                                    infoBox(message);
                                    badCluePlayed(plays, buttons, done, player, playersName, main, playingWindow);
                                } else {
                                    if (opponent.equalsIgnoreCase(plays)) {
                                        opponent(buttons, clue, done);
                                    } else {
                                        done.setDisable(false);
                                        resumeGame();
                                    }
                                }
                            }
                        }
                    });
                }
            });
        });

        Scene scene = new Scene(bp);

        MenuItem muteM = new MenuItem("Mute / Unmute Music");
        MenuItem muteS = new MenuItem("Mute / Unmute Sounds");
        muteM.setOnAction(event -> {
            if (Main.mSong.isMute()) Main.mSong.setMute(false);
            else Main.mSong.setMute(true);
        });
        muteS.setOnAction(event -> {
            if (!Main.mMuteS) {
                Main.mMuteS =true;
            } else Main.mMuteS= false;

        });
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(event -> {
            bp.setDisable(true);
            restart.fire();
            playingWindow.close();
        });
        menu.getItems().add(newGame);
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(muteM, muteS);
        MenuBar menuBar = new MenuBar(menu);

        menuBar.setLayoutY(0);
        menuBar.setLayoutX(0);
        playingWindow.setWidth(width);
        playingWindow.setHeight(height);
//        playingWindow.setX(120);
//        playingWindow.setY(80);
        playingWindow.setMaximized(true);

        bp.getChildren().addAll(hand, handOfGod, left, right, menuBar);
        playingWindow.setScene(scene);
        try {
            playingWindow.getIcons().add(new Image(this.getClass().getResource("IMAGES/blue_man.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        playingWindow.show();
        playingWindow.setOnCloseRequest(event -> {
            event.consume();     //when clicking the close button
            if (isGameOver()) {  //if the user is already looking at the finished game no confirmation is asked
                bp.setDisable(true);
                playingWindow.close();
                main.show();
            } else {
                if (alertCloseBoxYesNo("Are you sure you want to exit")) {  //otherwise, the user's confirmation to exit
                    bp.setDisable(true);                                    //is taken by clicking yes
                    setGameOver(true); //necessary to stop the "opponent keyframe"
                    playingWindow.close();
                    main.show();
                }
            }
        });
    }

    private void setJar(JarFile jar) {
        this.jar = jar;
    }
    private int getRow() {
        return row;
    }
    private void setRow(int row) {
        this.row = row;
    }
    private int getColumn() {
        return column;
    }
    private void setColumn(int column) {
        this.column = column;
    }
    private int getClicker() {
        return clicker;
    }
    private void setClicker(int clicker) {
        this.clicker = clicker;
    }
    private int getAddedNumber() {
        return addedNumber;
    }
    private void setAddedNumber(int addedNumber) {
        this.addedNumber = addedNumber;
    }
    private boolean isPhysicalPlayer() {
        return physicalPlayer;
    }
    private void setPhysicalPlayer(boolean physicalPlayer) {
        this.physicalPlayer = physicalPlayer;
    }
    private String getPlays() {
        return plays;
    }
    private void setPlays(String plays) {
        this.plays = plays;
    }
    private String getClue() {
        return clue;
    }
    private void setClue(String clue) {
        this.clue = clue;
    }
    private int getLetterIndex() {
        return letterIndex;
    }
    private void setLetterIndex(int letterIndex) {
        this.letterIndex = letterIndex;
    }
    private boolean isBadClue() {
        return badClue;
    }
    private void setBadClue(boolean badClue) {
        this.badClue = badClue;
    }
    private String getFirstTeam() {
        return firstTeam;
    }
    private void setFirstTeam(String firstTeam) {
        this.firstTeam = firstTeam;
    }
    private boolean isBlink() {
        return blink;
    }
    private void setBlink(boolean blink) {
        this.blink = blink;
    }
    private boolean isGameOver() {
        return gameOver;
    }
    private void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    private boolean isClueIsCardName() {
        return clueIsCardName;
    }
    private void setClueIsCardName(boolean clueIsCardName) {
        this.clueIsCardName = clueIsCardName;
    }
    private int getOppHasZero() {
        return oppHasZero;
    }
    private void setOppHasZero(int oppHasZero) {
        this.oppHasZero = oppHasZero;
    }
    private void setButtons(String firstPlayer) {           //randomly choose 25 cards and giving them colors according to who
        ArrayList<String> allFolder = new ArrayList<>();    //will play first
        if (Main.mRunningFromJar) {     //when the program runs from a jar file
            Enumeration<JarEntry> enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();
                if (entry.getName().endsWith(".txt") && !entry.isDirectory() && entry.getName().startsWith("W/")) {
                    allFolder.add(entry.getName());
                }
            }
            int filesAdded = 0;
            while (filesAdded < 25) {
                int random = (int) (Math.random() * allFolder.size());
                buttons.add(new Buttons());
                // FIXME: 14/08/2018 
                buttons.get(buttons.size() - 1).name = allFolder.get(random).replace(".txt", "").substring(2);
                buttons.get(buttons.size() - 1).filePath = allFolder.get(random);
                allFolder.remove(random);
                filesAdded++;
            }
        }
        else {   //when the program runs from the IDE
            File path = new File(Main.mClassPath+"/W/");
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



        if (firstPlayer.equalsIgnoreCase("blue")) {
            //blue first
            blueRemaining=9;
            totalBlue=9;
            redRemaining=8;
            totalRed=8;
            for (int i = 0; i <= 8; i++) {
                buttons.get(i).property = "blue";
            }
            for (int i = 9; i <= 16; i++) {
                buttons.get(i).property = "red";
            }
        } else {
            //red first
            redRemaining=9;
            totalRed=9;
            blueRemaining=8;
            totalBlue=8;
            for (int i = 0; i <= 8; i++) {
                buttons.get(i).property = "red";
            }
            for (int i = 9; i <= 16; i++) {
                buttons.get(i).property = "blue";
            }
        }
        for (int i = 17; i <= 23; i++) {
            buttons.get(i).property = "bystanders";   //bystanders are always 7
        }
        buttons.get(24).property = "assassin";        //there is always only 1 assassin

//shuffle
        for (int i = 0; i < buttons.size(); i++) {
            int random = (int) (Math.random() * buttons.size() - 1);
            Buttons temp = buttons.get(i);
            Buttons randTemp = buttons.get(random);
            buttons.set(i, randTemp);
            buttons.set(random, temp);
        }

        System.out.println();
        System.out.println("new game");
        for (int i = 0; i < buttons.size(); i++) {
            System.out.print(i + " " + buttons.get(i).property + "-" + buttons.get(i).name + "\t \t");
            switch (i) {
                case 4: case 9: case 14:case  19:
                    System.out.println();
                    break;
            }
        }
        System.out.println();
//        this.buttons = buttons;
    }
    private void setClues(String plays, boolean cleverMaster) {  // making a list of possible clues
        otherCards.clear();
        clues.clear();
        guessFolder.clear();
        tempClues.clear();

        int cardsLeft = 0;
        for (int i = 0; i < buttons.size(); i++) { //separate valid with invalid cards
            if (!buttons.get(i).clicked) {
                if (buttons.get(i).property.equalsIgnoreCase(plays)) {
                    guessFolder.add(buttons.get(i).filePath);
                    cardsLeft++;
                } else {
                    otherCards.add(buttons.get(i).filePath);
                }
            }
        }

        for (int i = 0; i < guessFolder.size(); i++) {  //all valid clues, unchecked and unsorted
            tempClues.addAll(readFile(guessFolder.get(i)));
        }

        for (int i = 0; i < tempClues.size(); i++) {
            if (tempClues.get(i).equalsIgnoreCase("")) { //remove blank lines, when using the IDE
                tempClues.remove(i);                                //the files were saved and read with the last line blank
                i--;
            }
        }
        for (int i = 0; i < tempClues.size(); i++) {
            tempClues.set(i, tempClues.get(i).toUpperCase());
        }
        tempClues.sort(null);


        while (!tempClues.isEmpty()) {            //using the occurrence of a clue to get the number
            String clueWord = tempClues.get(0);
            tempClues.remove(0);
            int match = 1;
            int position = Collections.binarySearch(tempClues, clueWord, null);
            while (position >= 0) {
                match++;
                tempClues.remove(position);
                position = Collections.binarySearch(tempClues, clueWord, null);
            }
            clues.add(clueWord + " " + match);
        }

        Collections.shuffle(clues);     //shuffled because they were sorted for binary and it doesn't look good when clues
        //are printed alphabetically

        //bring the clues with many matches to the front of the list
        for (int i = 0; i < clues.size() - 1; i++) {
            for (int j = i + 1; j < clues.size(); j++) {
                if (Integer.parseInt(Character.toString(clues.get(i).charAt(clues.get(i).length() - 1))) < Integer.parseInt(Character.toString(clues.get(j).charAt(clues.get(j).length() - 1)))) {
                    Collections.swap(clues, i, j);
                }
            }
        }

        for (int i = 0; i < clues.size(); i++) {      //implementation of 0 or ∞
            if (Integer.parseInt(Character.toString(clues.get(i).charAt(clues.get(i).length() - 1))) == cardsLeft && cardsLeft > 1) {
                setOppHasZero(Integer.parseInt(Character.toString(clues.get(i).charAt(clues.get(i).length() - 1))));
                clues.set(i, clues.get(i).substring(0, clues.get(i).length() - 1));
                if (Math.random()>=0.5)clues.set(i, clues.get(i).concat("0"));
                else
                    clues.set(i, clues.get(i).concat("∞"));
                break;
            }
        }

        if (cleverMaster) checkOtherCards();

        System.out.println();
        System.out.println("clues are finished");
        System.out.println(plays);
        for (int i = 0; i < clues.size(); i++) {
            System.out.print(clues.get(i) + "\t");
            if (i % 9 == 0 && i != 0) System.out.println();
        }
        System.out.println();
    }
    private ArrayList<Buttons> getButtons() {
        return buttons;
    }
    private double getPositionX() {
        return positionX;
    }
    private void setPositionX(double positionX) {
        this.positionX = positionX;
    }
    private double getPositionY() {
        return positionY;
    }
    private void setPositionY(double positionY) {
        this.positionY = positionY;
    }
    private double getHandX() {
        return handX;
    }
    private void setHandX(double handX) {
        this.handX = handX;
    }
    private double getHandY() {
        return handY;
    }
    private void setHandY(double handY) {
        this.handY = handY;
    }
    private void gameCheck(String firstTeam, String player, String playersName, Stage primaryStage, Stage playingWindow, Button done) {
        int blue = 0;   //if a card is clicked, this method is called to check wheter the game has ended

        int red = 0;
        String message = "";
        for (int i = 0; i < buttons.size(); i++) {      //clicking the assassin
            if (buttons.get(i).property.equalsIgnoreCase("assassin") && buttons.get(i).clicked) {
                setGameOver(true);
                if (player.equalsIgnoreCase(plays)) message = "Game Over, You lost " + playersName + "";
                else message = "Congratulations ! You won " + playersName + " !";
                break;
            }
            if (buttons.get(i).property.equalsIgnoreCase("red") && buttons.get(i).clicked) red++;
            if (buttons.get(i).property.equalsIgnoreCase("blue") && buttons.get(i).clicked) blue++;
        }
        if (firstTeam.equalsIgnoreCase("blue") && blue == 9) {  //blue plays first and finds all cards
            if (player.equalsIgnoreCase(getFirstTeam())) {
                message = "Congratulations ! You won " + playersName + " !";
            } else message = "Game Over, You lost " + playersName + "";
            setGameOver(true);
        }
        if (firstTeam.equalsIgnoreCase("red") && red == 9) {     //red plays first and finds all cards
            if (player.equalsIgnoreCase(getFirstTeam())) {
                message = "Congratulations ! You won " + playersName + " !";
            } else message = "Game Over, You lost " + playersName + "";
            setGameOver(true);
        }
        if (getFirstTeam().equalsIgnoreCase("red") && blue == 8) { //blue plays second and finds all cards
            if (player.equalsIgnoreCase("blue")) {
                message = "Congratulations ! You won " + playersName + " !";
            } else message = "Game Over, You lost " + playersName + "";
            setGameOver(true);
        }
        if (firstTeam.equalsIgnoreCase("blue") && red == 8) {    //red plays second and finds all cards
            if (player.equalsIgnoreCase("red")) {
                message = "Congratulations ! You won " + playersName + " !";
            } else message = "Game Over, You lost " + playersName + "";
            setGameOver(true);
        }
        //in case there is something wrong and there are no available clues
        if (firstTeam.equalsIgnoreCase("blueGaveUp") && !isGameOver()) {
            if (player.equalsIgnoreCase("blue")) message = "Your spymaster ran out of clues and gave up";
            else message = "The opponent spymaster ran out of clues and gave up";
            setGameOver(true);
        }
        if (firstTeam.equalsIgnoreCase("redGaveUp") && !isGameOver()) {
            if (player.equalsIgnoreCase("red")) message = "Your spymaster ran out of clues and gave up";
            else message = "The opponent spymaster ran out of clues and gave up";
            setGameOver(true);
        }
        if (isGameOver()) {
            String finalMessage = message;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < buttons.size(); i++) {
                        buttons.get(i).button.setOnAction(event -> {
                            event.consume();
                        });
                    }

                    done.setOnAction(event -> event.consume());
                    setBlink(false);
                    alertGameOverBox(primaryStage, playingWindow, finalMessage);

                    for (int i = 0; i < grid.size(); i++) {
                        if (!buttons.get(i).clicked) {
                            switch (buttons.get(i).property) {
                                case "red":
                                    String path;
                                    if (Math.random() > 0.5) path = "IMAGES/red_man.jpg";
                                    else path = "IMAGES/red_woman.jpg";
                                    buttons.get(i).button.setBackground(Images.setPhotoBGsingle(path, Color.RED, BackgroundRepeat.NO_REPEAT));
                                    break;
                                case "blue":
                                    if (Math.random() > 0.5) path = "IMAGES/blue_man.png";
                                    else path = "IMAGES/blue_woman.png";
                                    buttons.get(i).button.setBackground(Images.setPhotoBGsingle(path, Color.BLUE, BackgroundRepeat.NO_REPEAT));
                                    break;
                                case "bystanders":
                                    if (Math.random() > 0.5) path = "IMAGES/bystander_man.png";
                                    else path = "IMAGES/bystander_woman.png";
                                    buttons.get(i).button.setBackground(Images.setPhotoBGsingle(path, Color.WHITE, BackgroundRepeat.NO_REPEAT));
                                    break;
                                case "assassin":
                                    buttons.get(i).button.setBackground(Images.setPhotoBGsingle("IMAGES/assassin.png", Color.BLACK, BackgroundRepeat.NO_REPEAT));
                                    break;
                            }
                            grid.get(i).setDisable(false);
                            labels.get(i).setTextFill(Color.GREENYELLOW);
                            grid.get(i).setOpacity(0.6);    //keeping the unclicked buttons faded
                        }else grid.get(i).setDisable(false);  //re-enabling clicked buttons to show the tooltip when reviewing
                        grid.get(i).setOnMouseClicked(event -> event.consume());
                        grid.get(i).setOnMouseEntered(event -> event.consume());
                        menu.setDisable(false);
                    }
                }
            });
        }
    }
    private void flowCheck(Button done) {       //when a card is clicked this method is called to count the clicks
        if (getClicker() > getAddedNumber()) {  //and prevent the user from clicking more than what is allowed
            pauseGame();
            blink(done);
        }
    }
    private void pauseGame() {          //disables the cards,so they cannot be clicked
        for (int i = 0; i < grid.size(); i++) {
            grid.get(i).setDisable(true);
            grid.get(i).setOpacity(0.9);
        }
    }
    private void resumeGame() {                  //enables the cards to be clicked
        for (int i = 0; i < grid.size(); i++) {
            if (buttons.get(i).clicked) {
                grid.get(i).setDisable(true);
            } else
                grid.get(i).setDisable(false);
            grid.get(i).setOpacity(1);
        }
    }
    private void checkOtherCards() {  // if the spymaster is set to be clever, he will not ignore
        //similarities of other cards, with his own team's
        System.out.println("before removal");
        for (int i = 0; i < clues.size(); i++) {
            System.out.print(clues.get(i) + "\t");
            if (i>0 && i%10==0) System.out.println();
        }
        System.out.println();


        //remove words visible on the cards
        for (int i = 0; i < clues.size(); i++) {
            for (int j = 0; j < buttons.size(); j++) {
                if (clues.get(i).substring(0, clues.get(i).length() - 2).equalsIgnoreCase(buttons.get(j).name) || clues.get(i).contains(buttons.get(j).name)) {
                    System.out.println("removing "+clues.get(i)+" -> it matches with "+buttons.get(j).name);
                    clues.remove(i);
                    if (i > 0)
                        i--;
                }
            }
        }

//remove words that pertain to other cards
        ArrayList<String> otherClues = new ArrayList<>();
        for (int i = 0; i < otherCards.size(); i++) {
            otherClues.addAll(readFile(otherCards.get(i)));
        }
        otherClues.sort(null);
        System.out.println("other clues");
        for (int i = 0; i < otherClues.size(); i++) {
            System.out.print(otherClues.get(i) + "\t");
            if (i>0&&i%10==0) System.out.println();
        }
        System.out.println();
        for (int i = 0; i < clues.size(); i++) {
            String clueWord = clues.get(i).substring(0, clues.get(i).length() - 2);
            if (Collections.binarySearch(otherClues, clueWord, null) >= 0) {
                System.out.println("removing "+clues.get(i));
                clues.remove(i);
                i--;
            }
        }
    }
    private void badCluePlayed(String plays, ArrayList<Buttons> buttons, Button done,String player,String playersName,Stage primaryStage,Stage playingWindow) {
        setPhysicalPlayer(false);       //When a bad clue is given, a free card is awarded to the opponent team.
        Button b = null;                //The card is chosen at random
        String opponent;
        if (plays.equalsIgnoreCase("blue")) {
            opponent = "red";
        } else opponent = "blue";
        int i = 0;
        while (i < buttons.size()) {
            Random random = new Random();
            int rand = random.nextInt(10);
            if (!buttons.get(i).clicked && buttons.get(i).property.equalsIgnoreCase(opponent)) {
                if (b == null) b = buttons.get(i).button;
                else if (rand >= 5) {
                    b = buttons.get(i).button;
                    if (random.nextInt(2)>0) {
                        break;
                    }
                }
            }
            i++;
        }
        handOfGod.setMaxSize(140, 180);
        Image image = null;
        try {
            image = new Image(this.getClass().getResource("IMAGES/hand2.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        handOfGod.setGraphic(imageView);
        handOfGod.setLayoutY(0);
        handOfGod.setLayoutX(0);
        handOfGod.setVisible(true);
        setEndY((int) (b.getParent().getLayoutY() + (b.getMinHeight() / 2)));
        setEndX((int) (b.getParent().getLayoutX() + (b.getMinWidth() / 2)));
        int[] x = {(int) handOfGod.getLayoutX()};
        int[] y = {(int) handOfGod.getLayoutY()};
        boolean[] xReached = {false};
        boolean[] yReached = {false};
        Timeline godTimeLine = new Timeline();  //animation of the neutral hand giving a random free card
        Button finalB = b;
        KeyFrame godKeyframe = new KeyFrame(Duration.seconds(0.0003), event -> {
            if (x[0] < getEndX()) {
                x[0]++;
                if (x[0] < getEndX()) {
                    handOfGod.setLayoutX(x[0]);
                } else xReached[0] = true;
            } else {
                x[0]--;
                if (x[0] > getEndX()) {
                    handOfGod.setLayoutX(x[0]);
                } else xReached[0] = true;
            }
            if (y[0] < getEndY()) {
                y[0]++;

                if (y[0] < getEndY()) {
                    handOfGod.setLayoutY(y[0]);
                } else yReached[0] = true;
            } else {
                y[0]--;

                if (y[0] > getEndY()) {
                    handOfGod.setLayoutY(y[0]);
                } else yReached[0] = true;

//                System.out.println("Button: "+buttonNameTemp+"     "+"x: "+ xReached[0] +"   y: "+ yReached[0]);
                if (xReached[0] && yReached[0]) {
                    godTimeLine.stop();
                    handOfGod.setVisible(false);
                    handOfGod.setLayoutY(0);
                    handOfGod.setLayoutX(0);
                    resumeGame();
                    finalB.fire();
                    pauseGame();
                    gameCheck(getFirstTeam(),player,playersName,primaryStage,playingWindow,done);
                    if (!isGameOver()){
                        done.setDisable(false);
                        done.fire();
                    }
                }
            }
        });
        godTimeLine.getKeyFrames().add(godKeyframe);
        godTimeLine.setCycleCount(Animation.INDEFINITE);
        godTimeLine.play();

    }
    private Button getB() {
        return b;
    }
    private int getEndX() {
        return endX;
    }
    private void setEndX(int endX) {
        this.endX = endX;
    }
    private int getEndY() {
        return endY;
    }
    private void setEndY(int endY) {
        this.endY = endY;
    }
    private void setB(String clue,Button done) {    //choosing the card for the computer
        String clueToCheck;
        if (clue.charAt(0) == '\n') {
            clueToCheck = clue.substring(1, clue.length() - 2);
        } else clueToCheck = clue.substring(0, clue.length() - 2);

        Button button= null;
        for (int i = 0; i < getButtons().size(); i++) {
            if (!getButtons().get(i).clicked) {
                ArrayList<String> fc = readFile(getButtons().get(i).filePath);
                fc.sort(null);
                if (fc.contains(clueToCheck)) {
                    button = getButtons().get(i).button;
                    buttonNameTemp = getButtons().get(i).name;
                    break;
                }
            }
        }
        this.b = button;
        try {
            setEndY((int) (getB().getParent().getLayoutY()+ (getB().getMinHeight() / 2)));
            setEndX((int) (getB().getParent().getLayoutX()+ (getB().getMinWidth() / 2)));
        } catch (Exception e) {
            System.out.println("setEndX setEndY exception");
            for (int i = 0; i < 5; i++) {
                System.out.println("EXCEPTION IN setB()");
            }
            hand.setLayoutY(getPositionY());
            hand.setLayoutX(getPositionX());
            done.fire();
        }
    }
    private  void opponent(ArrayList<Buttons> buttons, String clue, Button done) {
        int[] times = {getAddedNumber()};   //the computer "clicks" the chosen card
        setB(clue,done);
        boolean[] xReached = {false};
        boolean[] yReached = {false};
        Timeline delay = new Timeline();

        //empty keyframe is played when the actual keyframe finishes. This way, when
        //when empty keyframe is done, the timiline will call onFinished(). Otherwise
        //it calls nothing.
        KeyFrame finished = new KeyFrame(Duration.seconds(0.1),event -> {
//            System.out.println("keyframe finished");
        });

        int[] x = {(int) getHandX()};
        int[] y = {(int) getHandY()};
        KeyFrame handK = new KeyFrame(Duration.seconds(0.003),event -> {
            if (x[0] < getEndX()) {
//                if (getEndX()-x[0]>900) x[0]+=9;
//                else   if (getEndX()-x[0]>800) x[0]+=8;
//                else  if (getEndX()-x[0]>700) x[0]+=7;
//                else  if (getEndX()-x[0]>600) x[0]+=6;
//                else  if (getEndX()-x[0]>500) x[0]+=5;
//                else  if (getEndX()-x[0]>400) x[0]+=4;
//                else  if (getEndX()-x[0]>300) x[0]+=3;
                if (getEndX()-x[0]>10) x[0]+=5;
                else  x[0]++;

//                x[0]++;
                if (x [0]< getEndX()) {
                    hand.setLayoutX(x[0]);
                } else xReached[0] = true;
            }else {
//                x[0]--;
//                if (x[0]-getEndX()>900) x[0]-=9;
//                else if (x[0]-getEndX()>800) x[0]-=8;
//                else if (x[0]-getEndX()>700) x[0]-=7;
//                else  if (x[0]-getEndX()>600) x[0]-=6;
//                else  if (x[0]-getEndX()>500) x[0]-=5;
//                else  if (x[0]-getEndX()>400) x[0]-=4;
//                else if (x[0]-getEndX()>300) x[0]-=3;
                if (x[0]-getEndX()>10) x[0]-=5;
                else x[0]--;

                if (x[0] > getEndX()) {
                    hand.setLayoutX(x[0]);
                } else xReached[0] = true;
            }
            if (y[0] < getEndY()) {
//                y[0]++;
//                if (getEndY()-y[0]>900) y[0]+=9;
//                else if (getEndY()-y[0]>800) y[0]+=8;
//                else  if (getEndY()-y[0]>700) y[0]+=7;
//                else if (getEndY()-y[0]>600) y[0]+=6;
//                else if (getEndY()-y[0]>500) y[0]+=5;
//                else if (getEndY()-y[0]>400) y[0]+=4;
//                else  if (getEndY()-y[0]>300) y[0]+=3;
                if (getEndY()-y[0]>10) y[0]+=5;
                else y[0]++;

                if (y[0] < getEndY()) {
                    hand.setLayoutY(y[0]);
                } else yReached[0] = true;
            }else {
//                y[0]--;
//                if (y[0]-getEndY()>900) y[0]-=9;
//                else if (y[0]-getEndY()>800) y[0]-=8;
//                else if (y[0]-getEndY()>700) y[0]-=7;
//                else if (y[0]-getEndY()>600) y[0]-=6;
//                else if (y[0]-getEndY()>500) y[0]-=5;
//                else if (y[0]-getEndY()>400) y[0]-=4;
//                else if (y[0]-getEndY()>300) y[0]-=3;
                if (y[0]-getEndY()>10) y[0]-=5;
                else  y[0]--;

                if (y[0] > getEndY()) {
                    hand.setLayoutY(y[0]);
                } else yReached[0] = true;

//                System.out.println("Button: "+buttonNameTemp+"     "+"x: "+ xReached[0] +"   y: "+ yReached[0]);
                if (xReached[0] && yReached[0]) {
//                    System.out.println();

                    delay.pause();
                    xReached[0] = false;
                    yReached[0] = false;
                    getB().getParent().setDisable(false);
                    getB().fire();

                    String property = null;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).button == getB()) {
                            property = buttons.get(i).property;
                        }
                    }
                    if (!getPlays().equalsIgnoreCase(property)) {
                        hand.setLayoutX(getHandX());
                        hand.setLayoutY(getHandY());
                        delay.stop();
                        String finalProperty = property;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {     //when the computer chooses a wrong card, it will happen if the
                                //spymasters do no check the other cards for similar clues
                                if (!finalProperty.equalsIgnoreCase("assassin")) {
                                    String message = "Your opponent made a mistake\nand loses his turn";
                                    if (finalProperty.equalsIgnoreCase("bystanders")) {
                                        message = "Your opponent hit a bystander\nand loses his turn";
                                    }
                                    infoBox(message);
                                    done.setDisable(false);
                                    done.fire();
                                }
                            }
                        });
                    } else {
                        if (times[0] == 0) {
                            times[0] = getOppHasZero();
                        }
                        if (getClicker() == times[0]) {
                            hand.setLayoutX(getHandX());
                            hand.setLayoutY(getHandY());
                            delay.stop();
//                            System.out.println("switching keyframes");
                            delay.getKeyFrames().set(0, finished);
                            delay.setCycleCount(1);
                            delay.play();
                        } else {
                            setB(clue,done);
//                            System.out.println("restarting keyframe");
                            if (isGameOver()) {
                                delay.stop();
//                                System.out.println("stopped");
                            }
                            else delay.playFromStart();
                        }
                    }
                }
            }
        });
        delay.setCycleCount(Animation.INDEFINITE);
        delay.getKeyFrames().add(handK);
        delay.play();
        delay.setOnFinished(event -> {
            Timeline timeline = new Timeline();
            KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(1),event1 -> {});
            timeline.getKeyFrames().add(keyFrame1);
            timeline.setCycleCount(1);
            timeline.play();
            timeline.setOnFinished(event1 -> {
                done.setDisable(false);
                done.fire();
            });
        });
    }
    private  ArrayList<String> readFile(String filepath){
        Scanner in = null;
        try {
            if (Main.mRunningFromJar) in = new Scanner(jar.getInputStream(jar.getEntry(filepath)));
            else {
                File file = new File(filepath);
                in = new Scanner(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> fileContents = new ArrayList<>();
        while (in.hasNextLine()){
            String string= in.nextLine();
            if (string.split(" - ").length>1) {
                String[] strings = string.split(" - ");
                fileContents.add(strings[0]);
            }else fileContents.add(string);
        }
        return fileContents;
    }
    private void blink(Button done){    //called when the player is not allowed to click more cards, flashes the done button
        Background red = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
        Background blue = new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY));
        Background originalBg = new Background(done.getBackground().getFills(), done.getBackground().getImages());
        done.setBackground(red);
        setBlink(true);
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5),event -> {
            if (isBlink()){
                if (done.getBackground().equals(red)) done.setBackground(blue);
                else done.setBackground(red);
            }else {
                timeline.stop();
                done.setBackground(originalBg);
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    private  boolean alertCloseBoxYesNo(String message){   //asks the user for confirmation, when the close button is clicked
        boolean[] close = new boolean[1];
        Stage alert = new Stage();
        VBox vBox = new VBox(20);
        Label label = new Label(message);
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
    private void alertGameOverBox(Stage primaryStage,Stage window, String message){
        Stage alert = new Stage();
        VBox vBox = new VBox(10);
        Label label = new Label(message);
        Button ok = new Button("Back to the main window");
        Button review = new Button("Stay in this window");
        Button res = new Button("Start a new game");
//        vBox.autosize();
        ok.setOnAction(event -> {
            alert.close();
            window.close();
            primaryStage.show();
        });
        review.setOnAction(event -> {
            alert.close();
        });
        res.setOnAction(event -> {
            alert.close();
            newGame.fire();
            window.close();
        });
        label.setFont(Font.font(25));
        ok.setPrefSize(250,50);
        review.setPrefSize(250,50);
        res.setPrefSize(250,50);
        ok.setFont(Font.font(20));
        review.setFont(Font.font(20));
        res.setFont(Font.font(20));
        vBox.getChildren().addAll(label,ok,review,res);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 600, 350);

        alert.setScene(scene);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
    private  void infoBox(String message){   //pop up messages
        Stage info = new Stage();
        VBox vBox = new VBox(10);
        Label label = new Label(message);
        label.setFont(new Font(20));
        Button ok = new Button("Ok");
//        label.autosize();
//        vBox.autosize();
        ok.setOnAction(event -> {
            info.close();
        });
        vBox.getChildren().addAll(label,ok);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 500, 250);
        info.setScene(scene);
        info.initModality(Modality.APPLICATION_MODAL);
        info.showAndWait();
    }
}