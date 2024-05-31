package org.example.citywars;

import com.almasb.fxgl.entity.action.Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class M_SignUpMenu extends Menu {
    Label error;
    TextField usernameField;
    PasswordField passwordField;
    Label password;
    PasswordField passwordConfirmationField;
    ChoiceBox<String> questionChoice;
    TextField questionAnswerField;
    TextField captchaField;
    TextField emailField;
    TextField nicknameField;

    public M_SignUpMenu(){
        super("M_SignUpMenu");
    }
    public Menu myMethods(String input){
        return null;
    }

    //Control Methods
    @FXML
    protected void GoToLoginButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_LoginMenu();
        switchMenus(event);
    }

}
