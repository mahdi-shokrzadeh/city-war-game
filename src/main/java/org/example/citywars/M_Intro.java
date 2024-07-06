package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class M_Intro extends Menu {
    public M_Intro() {
        super("M_Intro", false, "BG-Videos\\Bgintro.m4v");
        secondPersonNeeded = false;
        playMode = null;
    }

    private void printMenu(){
        System.out.println("INTRO MENU");
        System.out.println("Options: ");
        System.out.println("    signup");
        System.out.println("    login");
    }

    public Menu myMethods() {
        printMenu();
        String input = null;
        do{
            input = consoleScanner.nextLine().trim();
            if (input.toLowerCase().matches("^login$"))
                return new M_LoginMenu();
            else if (input.toLowerCase().matches("^signup$"))
                return new M_SignUpMenu();
            else if (input.toLowerCase().matches("^exit$"))
                return null;
            else
                System.out.println("invalid command!");
        } while(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (!BGisImage) {
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            backGround.setMediaPlayer(mediaPlayer);

            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(-1);
        }

        File file2 = new File("src\\main\\resources\\BG-Videos\\BGIn.png");
        BGim = new Image(file2.toURI().toString());
        backGroundIm.setImage(BGim);
    }
}
