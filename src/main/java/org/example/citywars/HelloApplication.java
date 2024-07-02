package org.example.citywars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class HelloApplication extends Application {
    public static Menu menu;

    @Override
    public void start(Stage stage) throws IOException {

        menu = new M_Intro();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(menu.getName()+".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(menu.getName());
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
/*
        menu = new M_SignUpMenu();

        Scanner sc = new Scanner(System.in);//for console version
        String input;//for console version

        boolean isEnd = false;
        while (!isEnd) {
            input = sc.nextLine();
            if (input.matches("^ *show +current +menu *$")) {
                System.out.println(menu.getName().substring(1));//for console version
            } else {
                menu = menu.myMethods(input);
                if (menu == null) {
                    System.out.println("the End!");
                    isEnd = true;
                }
            }
        }

 */
    }


}