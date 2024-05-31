package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class M_LoginMenu extends Menu {
    public M_LoginMenu(){
        super("M_LoginMenu");
    }
    public Menu myMethods(String input){
        return null;
    }

    //Control Methods
    @FXML
    protected void GoToSignUpButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_SignUpMenu();
        switchMenus(event);
    }
}
