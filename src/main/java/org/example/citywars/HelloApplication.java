package org.example.citywars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.M_SignUpMenu;
import models.Menu;

import java.io.IOException;
import java.util.Scanner;

public class HelloApplication extends Application {
    Menu menu;

    @Override
    public void start(Stage stage) throws IOException {

        menu = new M_SignUpMenu();

        Scanner sc = new Scanner(System.in);//for console version
        String input;//for console version

//        boolean isEnd = false;
//        while (!isEnd) {
//            input = sc.nextLine();
//            if (input.matches("^ *show +current +menu *$")) {
//                menu.myNameIs();
//            } else {
//                menu = menu.myMethods(input);
//                if (menu == null) {
//                    isEnd = true;
//                }
//            }
//        }

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(menu.background.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}