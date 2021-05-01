import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Sounds {

    public  MediaPlayer playMusic(){
        Media music= null;
        try {
            music= new Media(this.getClass().getResource("SOUNDS/bensound-enigmatic.mp3").toURI().toString());

//            soundFile = new File(this.getClass().getClassLoader().getResource("SOUNDS/bensound-enigmatic.mp3").getFile());

        } catch (Exception e) {
            System.out.println("Song failed");
        }
        MediaPlayer player;
        player = new MediaPlayer(music);
        player.setVolume( player.getVolume()/2);
        return player;
    }

    public void mouseEnteredSound() {
//        File file = new File("SOUNDS/tone.wav");
//        File file = new File(this.getClass().getClassLoader().getResource("SOUNDS/tone.wav").getFile());
        AudioClip audioClip = null;
        try {
            audioClip = new AudioClip(this.getClass().getResource("SOUNDS/tone.wav").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        audioClip.play(0.4);

//        MediaPlayer player = null;
//        try {
//            File soundFile = new File("SOUNDS/tone.wav");
//            Media music = new Media(soundFile.toURI().toString());
//            player = new MediaPlayer(music);
//            ;
//        } catch (Exception e) {
//            System.out.println("mouse entered sound failed");
//        }
//        player.setVolume(0.2);
//        player.play();
//        return player;
    }


    public  void noSound(){
//        File file = new File("SOUNDS/Wrong-1.m4a");
//        File file = new File(this.getClass().getClassLoader().getResource("SOUNDS/Wrong-1.m4a").getFile());
        AudioClip audioClip = null;
        try {
            audioClip = new AudioClip(this.getClass().getResource("SOUNDS/Wrong-1.m4a").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        audioClip.play(0.4);

//        File soundFile = null;
//        try {
//            soundFile = new File("SOUNDS/Wrong-1.m4a");
//        } catch (Exception e) {
//            System.out.println("Song failed");
//        }
//        MediaPlayer player;
//        Media music = new Media(soundFile.toURI().toString());
//        player = new MediaPlayer(music);
//        player.setVolume(0.4);
//        player.play();
    }
    public  void yesSound(){
//        File file = new File("SOUNDS/Correct-1.m4a");
//        File file = new File(this.getClass().getClassLoader().getResource("SOUNDS/Correct-1.m4a").getFile());
        AudioClip audioClip = null;
        try {
            audioClip = new AudioClip(this.getClass().getResource("SOUNDS/Correct-1.m4a").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        audioClip.play(0.4);
//
//        File soundFile = null;
//        try {
//            soundFile = new File("SOUNDS/Correct-1.m4a");
//        } catch (Exception e) {
//            System.out.println("Song failed");
//        }
//        MediaPlayer player;
//        Media music = new Media(soundFile.toURI().toString());
//        player = new MediaPlayer(music);
//        player.setVolume(0.4);
//        player.play();
    }
    public  void bystanderSound(){
//        File file = new File("SOUNDS/Bysr-1.m4a");

//        File file = new File(this.getClass().getClassLoader().getResource("SOUNDS/Bysr-1.m4a").getFile());
        AudioClip audioClip = null;
        try {
            audioClip = new AudioClip(this.getClass().getResource("SOUNDS/Bysr-1.m4a").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        audioClip.play(0.4);
//
//        File soundFile = null;
//        try {
//            soundFile = new File("SOUNDS/Bysr-1.m4a");
//        } catch (Exception e) {
//            System.out.println("Song failed");
//        }
//        MediaPlayer player;
//        Media music = new Media(soundFile.toURI().toString());
//        player = new MediaPlayer(music);
//        player.setVolume(0.4);
//        player.play();
    }
    public  void messageSound(){
//        File file = new File("SOUNDS/Message-1.m4a");

//        File file = new File(this.getClass().getClassLoader().getResource("SOUNDS/Message-1.m4a").getFile());
        AudioClip audioClip = null;
        try {
            audioClip = new AudioClip(this.getClass().getResource("SOUNDS/Message-1.m4a").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        audioClip.play(0.4);
//        File soundFile = null;
//        try {
//            soundFile = new File("SOUNDS/Message-1.m4a");
//        } catch (Exception e) {
//            System.out.println("Song failed");
//        }
//        MediaPlayer player;
//        Media music = new Media(soundFile.toURI().toString());
//        player = new MediaPlayer(music);
//        player.setVolume(0.4);
//        player.play();

    }
    public  void assassin(){
//        File file = new File("SOUNDS/assassin.wav");
//        File file = new File(this.getClass().getClassLoader().getResource("SOUNDS/assasin.wav").getFile());
        AudioClip audioClip = null;
        try {
            audioClip = new AudioClip(this.getClass().getResource("SOUNDS/assassin.wav").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        audioClip.play(0.4);
//
//        File soundFile = null;
//        try {
//            soundFile = new File("SOUNDS/assassin.wav");
//        } catch (Exception e) {
//            System.out.println("Song failed");
//        }
//        MediaPlayer player;
//        Media music = new Media(soundFile.toURI().toString());
//        player = new MediaPlayer(music);
//        player.setVolume(0.4);
//        player.play();
    }

}

