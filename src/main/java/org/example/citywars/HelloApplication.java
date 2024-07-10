package org.example.citywars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.example.citywars.Menu.*;

public class HelloApplication extends Application {
    public static Menu menu;
    public static Image icon = new Image("logo.png");
    public static Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {

        loadFiles();
        this.primaryStage = stage;
        menu = new M_Intro();
        outPutMusic = new MediaPlayer(BGMusicMedias.get(0));
        outPutMusic.setCycleCount(-1);
        outPutMusic.setVolume(0.5);
        outPutMusic.play();


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(menu.getName() + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(menu.getName());
        stage.getIcons().add(icon);
        // stage.setMaximized(true);
        // stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    private void loadFiles() {
        CharImageFiles = new File[12];
        CharImageFiles[0]=  new File("src/main/resources/Characters/Igoribuki.png");
        CharImageFiles[1]=  new File("src/main/resources/Characters/Master_Masher.png");
        CharImageFiles[2]=  new File("src/main/resources/Characters/Nahane.png");
        CharImageFiles[3]=  new File("src/main/resources/Characters/Sensei_Pandaken.png");
        CharImageFiles[4]=  new File("src/main/resources/Characters/DmanNormal.gif");
        CharImageFiles[5]=  new File("src/main/resources/Characters/wolfNormal.gif");
        CharImageFiles[6]=  new File("src/main/resources/Characters/dragonNormal.gif");
        CharImageFiles[7]=  new File("src/main/resources/Characters/pandaNormal.gif");
        CharImageFiles[8]=  new File("src/main/resources/Characters/DmanAttack.gif");
        CharImageFiles[9]=  new File("src/main/resources/Characters/wolfAttack.gif");
        CharImageFiles[10]=  new File("src/main/resources/Characters/dragonAttack.gif");
        CharImageFiles[11]=  new File("src/main/resources/Characters/pandaAttack.gif");
        charsImages=new Image[CharImageFiles.length];
        for (int i = 0; i < CharImageFiles.length; i++) {
            charsImages[i] = new Image(CharImageFiles[i].toURI().toString());
        }

        CharImageFilesProfile = new File[5];
        CharImageFilesProfile[0]=  new File("src/main/resources/Profile/Chi_Ao_Loong.png");
        CharImageFilesProfile[1]=  new File("src/main/resources/Profile/Igoribuki_1.png");
        CharImageFilesProfile[2]=  new File("src/main/resources/Profile/Master_Masher_1.png");
        CharImageFilesProfile[3]=  new File("src/main/resources/Profile/Nahane_1.png");
        CharImageFilesProfile[4]=  new File("src/main/resources/Profile/Sensei_Pandaken_1.png");

        charsImagesProfile=new Image[CharImageFilesProfile.length];
        for (int i = 0; i < CharImageFilesProfile.length; i++) {
            charsImagesProfile[i] = new Image(CharImageFilesProfile[i].toURI().toString());
        }

        BGMusicFiles = new ArrayList<>();
        BGMusicFiles.add(new File("src/main/resources/Musics/01 - Main Title (The Godfather Waltz).mp3"));
        BGMusicFiles.add(new File("src/main/resources/Musics/01 - Prologue.mp3"));
        BGMusicFiles.add(new File("src/main/resources/Musics/London Music Works - Theme From Agatha Christies Poirot.mp3"));
        BGMusicMedias=new ArrayList<>();
//        BGMusics = new ArrayList<>();
        for (int i = 0; i < BGMusicFiles.size(); i++) {

            BGMusicMedias.add(new Media(BGMusicFiles.get(i).toURI().toString()));
//            BGMusics.add(new MediaPlayer(BGMusicMedias.get(i)));
        }
    }

    public static void main(String[] args) {
        launch();

        // // menu = new M_SignUpMenu();
        // menu = new M_GamePlayMenu();
        //
        // Scanner sc = new Scanner(System.in);//for console version
        // String input;//for console version
        //
        // boolean isEnd = false;
        // while (!isEnd) {
        // menu = menu.myMethods();
        // if (menu == null) {
        // System.out.println("the End!");
        // isEnd = true;
        // }
        // }
    }
}