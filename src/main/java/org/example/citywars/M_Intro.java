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

    public Menu myMethods(String input) {
        if (input.toLowerCase().matches("^ *log +in *$"))
            return new M_LoginMenu();
        else if (input.toLowerCase().matches("^ *sign +up *$"))
            return new M_SignUpMenu();

        System.out.println("invalid command!");
        return this;
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
