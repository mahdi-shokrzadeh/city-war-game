package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class M_Intro extends Menu{
    public M_Intro() {
        super("M_Intro","BGIntro.mp4");
    }
    public Menu myMethods(String input){
        return null;
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
