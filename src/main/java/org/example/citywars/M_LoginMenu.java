package org.example.citywars;

import controllers.UserController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import models.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.regex.Pattern;

public class M_LoginMenu extends Menu {
    public static Timer timer;
    public static boolean timerIsOn;
    public static int lockTime;
    public static int failureCount;
    @FXML
    Label error;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button back;
    @FXML
    Button forgotPass;
    @FXML
    Button ok;
    @FXML
    CheckBox isAdmin;
    @FXML
    ProgressBar ErrorTimer;

    public M_LoginMenu() {
        super("M_LoginMenu", true, "BG-Videos\\BG-login.png");
        lockTime = 0;
        failureCount = 0;
        M_LoginMenu.timerIsOn = false;
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("^ *user +login +-u(?<username>[\\S ]+)-p(?<password>[\\S ]+) *$"));
        patterns.add(Pattern.compile("^ *Forgot +my +password +-u(?<username>[\\S ]+) *$"));
    }

    private void printMenu() {
        System.out.println("Login menu");
        System.out.println("Options: ");
        System.out.println("    Back");
        System.out.println("Information: ");
        System.out.println("    You can login in using following format: ");
        System.out.println("        user login -u <username> -p <password>");
        System.out.println("    if you forget your password login using your security question by typing: ");
        System.out.println("        Forgot my password -u <username>");
    }

    public Menu myMethods() {
        printMenu();
        String input;

        do {
            input = consoleScanner.nextLine();

            if (input.toLowerCase().matches("^ *back *$")) {
                if (M_GameModeChoiseMenu.secondPersonNeeded)
                    return new M_GameModeChoiseMenu();
                else
                    return new M_Intro();
            } else if (patterns.get(0).matcher(input).find()) {
                matcher = patterns.get(0).matcher(input);
                matcher.find();
                Response s = UserController.login(matcher.group("username").trim(), matcher.group("password").trim());
                System.out.println(s.message);
                if (s.ok) {
                    if (M_GameModeChoiseMenu.secondPersonNeeded)
                        return new M_GamePlayMenu();
                    else
                        return new M_GameMainMenu();
                }
            } else if (patterns.get(1).matcher(input).find()) {
                matcher = patterns.get(1).matcher(input);
                matcher.find();
                Response s = UserController.forgotPassword(matcher.group("username").trim());
                System.out.println(s.message);
                if (s.ok) {
                    if (M_GameModeChoiseMenu.secondPersonNeeded)
                        return new M_GamePlayMenu();
                    else
                        return new M_GameMainMenu();
                }
            } else if (Pattern.compile("^show current menu$").matcher(input).find()) {
                System.out.println("you are currently in " + getName());
            } else {
                System.out.println("Invalid command!");
            }
        } while (true);
    }

    @FXML
    protected void loginButton(ActionEvent event) throws IOException {

        if (usernameField.getText().isBlank()){
            error.setText("Username Field is blank!");
            return;
        }
        if (passwordField.getText().isBlank()){
            error.setText("Password Field is blank!");
            return;
        }
        Response s = UserController.login(usernameField.getText(), passwordField.getText());
        error.setText(s.message);

        if (timerIsOn && s.message.contains("Wrong Password")){
            ErrorTimer.progressProperty().setValue(1);

            Timeline timeline=new Timeline();
            KeyValue key = new KeyValue(ErrorTimer.progressProperty(), 0 );
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(5*failureCount),key);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

        }
        if (s.ok) {
            if (M_GameModeChoiseMenu.secondPersonNeeded) {
                HelloApplication.menu = new M_GamePlayMenu();
                switchMenus(event);
            }
            else {
                HelloApplication.menu = new M_GameMainMenu();
                switchMenus(event);
            }
        }
    }
}
