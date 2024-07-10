package org.example.citywars;

import controllers.UserController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.util.Duration;
import models.Response;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.regex.Pattern;

import static controllers.UserController.sudoGetAllUsers;

public class M_LoginMenu extends Menu {
    boolean forgotPassBool;
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
    @FXML
    Label title;
    @FXML
    Label password;

    public M_LoginMenu() {
        super("M_LoginMenu", new String[]{"BG-Videos\\BG-login.png","BG-Videos\\lightmode.png"});
        forgotPassBool=false;
        lockTime = 0;
        failureCount = 0;
        M_LoginMenu.timerIsOn = false;
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("^ *user +login +-u(?<username>[\\S ]+)-p(?<password>[\\S ]+) *$"));
        patterns.add(Pattern.compile("^ *Forgot +my +password +-u(?<username>[\\S ]+) *$"));

    }

    private void printMenu() {
        System.out.println("LOGIN MENU");
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
                if (secondPersonNeeded)
                    return new M_GamePlayMenu();
                else
                    return new M_Intro();
            } else if (patterns.get(0).matcher(input).find()) {
                matcher = patterns.get(0).matcher(input);
                matcher.find();
                Response s = UserController.login(matcher.group("username").trim(), matcher.group("password").trim());
                System.out.println(s.message);
                if (s.ok) {
                    if (secondPersonNeeded) {
                        if (((User) s.body.get("user")).getID() == loggedInUser.getID()) {
                            System.out.println("\n!!! Same user, login again !!!\n");
                            return this;
                        } else {
                            if (!is_bet) {
                                return new M_Game(loggedInUser, (User) s.body.get("user"), "duel");
                            } else {
                                return new M_Game(loggedInUser, (User) s.body.get("user"), "bet");
                            }
                        }
                    } else {
                        loggedInUser = (User) s.body.get("user");
                        return new M_GameMainMenu();
                    }
                }
            } else if (patterns.get(1).matcher(input).find()) {
                matcher = patterns.get(1).matcher(input);
                matcher.find();
                Response s = UserController.forgotPassword(matcher.group("username").trim());
                System.out.println(s.message);
                if (s.ok) {
                    if (secondPersonNeeded) {
                        if (((User) s.body.get("user")).getID() == loggedInUser.getID()) {
                            System.out.println("\n!!! Same user, login again !!!\n");
                            return this;
                        } else {
                            if (!is_bet) {
                                return new M_Game(loggedInUser, (User) s.body.get("user"), "duel");
                            } else {
                                return new M_Game(loggedInUser, (User) s.body.get("user"), "bet");
                            }
                        }
                    } else {
                        if (!is_bet) {
                            return new M_Game(loggedInUser, (User) s.body.get("user"), "duel");
                        } else {
                            return new M_Game(loggedInUser, (User) s.body.get("user"), "bet");
                        }
                    }
                }
            } else if (Pattern.compile("^show current menu$").matcher(input).find()) {
                System.out.println("you are currently in " + getName());
            } else {
                System.out.println("Invalid command!");
            }
        } while (true);
    }

    // Control Methods
    @FXML
    protected void loginButton(ActionEvent event) throws IOException {

        if (usernameField.getText().isBlank()) {
            error.setText("Username Field is blank!");
            return;
        }
        if (passwordField.getText().isBlank() && !passwordField.isDisable()) {
            error.setText("Password Field is blank!");
            return;
        }

        if (!forgotPassBool) {
            Response s = UserController.login(usernameField.getText(), passwordField.getText());
            error.setText(s.message);

            if (timerIsOn && s.message.contains("Wrong Password")) {
                ErrorTimer.progressProperty().setValue(1);

                Timeline timeline = new Timeline();
                KeyValue key = new KeyValue(ErrorTimer.progressProperty(), 0);
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(5 * failureCount), key);
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();

            }
            if (s.ok) {
                if (secondPersonNeeded) {
                    if (((User) s.body.get("user")).getID() == loggedInUser.getID()) {
                        error.setText("\n!!! Same user, login again !!!\n");
                        return;
                    } else {
                        secondUser = (User) s.body.get("user");
                        HelloApplication.menu = new M_CharacterChoice();
                        switchMenus(event);
                    }
                } else {
                    M_ProfileMenu.profileIndex = 0;/////////////////////
                    loggedInUser = (User) s.body.get("user");
                    HelloApplication.menu = new M_GameMainMenu();
                    switchMenus(event);
                }
            }
        } else {
            User temp;
            if (passwordField.isDisable()){
                int index = M_SignUpMenu.getIndexFromUsername(usernameField.getText());
                if (index==-1){
                    error.setText("Username doesn't exist!");
                    return;
                }

                List<User> allUsers = (List<User>) sudoGetAllUsers().body.get("allUsers");
                temp=allUsers.get(index);
                error.setText(temp.getPassRecoveryQuestion());
                usernameField.setDisable(true);
                passwordField.setDisable(false);
                password.setDisable(false);
                password.setText("Answer :");

            }else {
                int index = M_SignUpMenu.getIndexFromUsername(usernameField.getText());
                if (index==-1){
                    error.setText("Username doesn't exist!");
                    return;
                }

                List<User> allUsers = (List<User>) sudoGetAllUsers().body.get("allUsers");
                temp=allUsers.get(index);
                if (passwordField.getText().equals(temp.getPassRecoveryAnswer())){
                    if (secondPersonNeeded) {
                        if (temp.getID() == loggedInUser.getID()) {
                            error.setText("\n!!! Same user, login again !!!\n");
                            return;
                        } else {
                            secondUser = temp;
                            HelloApplication.menu = new M_CharacterChoice();
                            switchMenus(event);
                        }
                    } else {
                        M_ProfileMenu.profileIndex = 0;/////////////////////
                        loggedInUser = temp;
                        HelloApplication.menu = new M_GameMainMenu();
                        switchMenus(event);
                    }
                }
                else {
                    error.setText("Wrong Answer!");
                }
            }
        }
    }
    @FXML
    protected void loginBack(ActionEvent event) throws IOException {
        if (secondPersonNeeded) {
            if (is_bet){
                HelloApplication.menu = new M_Bet();
                switchMenus(event);
                return;
            }
            HelloApplication.menu = new M_GamePlayMenu();
            switchMenus(event);
        }
        else{
            HelloApplication.menu = new M_Intro();
            switchMenus(event);
        }
    }
    @FXML
    protected void forgotPassButton(ActionEvent event) throws IOException {
        if (forgotPassBool){
            error.setText("");
            forgotPassBool=false;
            password.setText("Password :");
            password.setDisable(false);
            passwordField.setDisable(false);

            forgotPass.setText("Forgot your password?");

        }else {
            forgotPassBool=true;
            error.setText("At first inter your username and hit ok");
            password.setDisable(true);
            passwordField.setDisable(true);

            forgotPass.setText("I remember my password!");
        }


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (secondPersonNeeded)
            title.setText("Second Person Login");
        else
            title.setText("Login");

        backGroundIm.setImage(BGims.get(themeIndex));
    }
}
