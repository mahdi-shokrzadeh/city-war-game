package org.example.citywars;

import com.almasb.fxgl.entity.action.Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import models.AA_Captcha;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class M_SignUpMenu extends Menu  {

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

    public M_SignUpMenu() {
        super("M_SignUpMenu","BG1.mp4");
    }
    public Menu myMethods(String input){

        System.out.println(randomPassword());
        System.out.println(randomPassword());
        System.out.println(randomPassword());

        AA_Captcha a = new AA_Captcha();
        AA_Captcha b = new AA_Captcha();
        AA_Captcha c = new AA_Captcha();

        System.out.println(a.showEquation());
        System.out.println(a.getAnswer());

        System.out.println(b.showEquation());
        System.out.println(b.getAnswer());

        System.out.println(c.showEquation());
        System.out.println(c.getAnswer());

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

//        mediaPlayer.play();
    }

}
