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
import java.util.Random;

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

        System.out.println(randomPassword());
        System.out.println(randomPassword());
        System.out.println(randomPassword());

        return null;
    }

    private String randomPassword(){
        Random random = new Random();
        int length = random.nextInt(10) + 8;
        String s1="0123456789";
        String s2="!@#$%^&*";
        String s3="qwertyuiopasdfghjklzxcvbnm";
        String s4= s3.toUpperCase();
        String s5 = s1+s2+s3+s4;

        StringBuilder output= new StringBuilder();
        output.append(s4.charAt(random.nextInt(s4.length() - 1)));
        output.append(s3.charAt(random.nextInt(s3.length() - 1)));
        output.append(s2.charAt(random.nextInt(s2.length() - 1)));
        output.append(s1.charAt(random.nextInt(s1.length() - 1)));

        for (int i = 3; i < length; i++) {
            output.append(s5.charAt(random.nextInt(s5.length() - 1)));
        }
        return output.toString();
    }

    //Control Methods
    @FXML
    protected void GoToLoginButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_LoginMenu();
        switchMenus(event);
    }

}
