package org.example.citywars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class HelloApplication extends Application {
    public static Menu menu;
    public static Image icon = new Image("logo.png");

    @Override
    public void start(Stage stage) throws IOException {

        menu = new M_Intro();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(menu.getName() + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(menu.getName());
        stage.getIcons().add(icon);
        // stage.setMaximized(true);
        // stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
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