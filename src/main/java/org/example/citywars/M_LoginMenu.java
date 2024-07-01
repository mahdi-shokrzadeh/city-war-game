package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class M_LoginMenu extends Menu {
    public M_LoginMenu(){
        super("M_LoginMenu","BG1.mp4");
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("^ *user +login +-u(?<username>[\\S ]+)-p(?<password>[\\S ]+) *$"));
        patterns.add(Pattern.compile("^ *Forgot +my +password +-u(?<username>[\\S ]+) *$"));
    }
    public Menu myMethods(String input){
        if (input.matches("^ *Back *$")) {
            if (M_GameModeChoiseMenu.secondPersonNeeded)
                return new M_GameModeChoiseMenu();
            else
                return new M_Intro();
        }

        matcher = patterns.get(0).matcher(input);
        if (matcher.find()) {
            //.... log in
            if (M_GameModeChoiseMenu.secondPersonNeeded)
                return new M_GamePlayMenu();
            else
                return new M_GameMainMenu();
        }

        matcher = patterns.get(1).matcher(input);
        if (matcher.find()) {
            //.... forgot password
        }
        return this;
    }

    //Control Methods
    @FXML
    protected void GoToSignUpButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_SignUpMenu();
        switchMenus(event);
    }
}
