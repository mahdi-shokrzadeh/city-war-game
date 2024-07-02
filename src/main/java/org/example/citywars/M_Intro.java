package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class M_Intro extends Menu {
    public M_Intro() {
        super("M_Intro", "Bgintro.m4v");
        secondPersonNeeded = false;
        playMode = null;
    }

    private void printMenu(){
        System.out.println("Intro menu");
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
            else
                System.out.println("invalid command!");
        } while(true);
    }

    @FXML
    protected void GoToLoginButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_LoginMenu();
        switchMenus(event);

//        mediaPlayer.play();
    }

    @FXML
    protected void GoToSignUpButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_SignUpMenu();
        switchMenus(event);
    }
}
