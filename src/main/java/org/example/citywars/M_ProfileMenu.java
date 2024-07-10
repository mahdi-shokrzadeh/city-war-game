package org.example.citywars;

import controllers.UserController;
import database.DBs.UserDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.AA_Captcha;
import models.Response;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static controllers.UserController.sudoGetAllUsers;
import static org.example.citywars.M_SignUpMenu.*;

public class M_ProfileMenu extends Menu{
    AA_Captcha captchaCode;
    static int profileIndex;
    @FXML
    private Label HP;

    @FXML
    private Label XP;
    @FXML
    private Label coin;

    @FXML
    private Label level;

    @FXML
    private Label name;

    @FXML
    private ImageView profileIm;
    @FXML
    Label error;
    @FXML
    Label captcha;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    PasswordField passwordConfirmationField;
    @FXML
    ChoiceBox<String> questionChoice;
    @FXML
    TextField questionAnswerField;
    @FXML
    TextField captchaField;
    @FXML
    TextField emailField;
    @FXML
    TextField nicknameField;
    private int captchaCountLeft = 3;
    public M_ProfileMenu(){
        super("M_ProfileMenu", new String[]{"BG-Videos\\BG_GameMain.png","BG-Videos\\lightmode.png"});
        captchaCode=new AA_Captcha();
    }

    private void printMenu() {
        System.out.println("PROFILE MENU");
        System.out.println("Options: ");
        System.out.println("    show my profile");
        System.out.println("    Back");
        System.out.println("Information: ");
        System.out.println("    You can change your profile info using one the following commands: ");
        System.out.println("    Profile change -u <username>");
        System.out.println("    Profile change password -o <old-password> -n <new-password>");
        System.out.println("    Profile change -n <nickname>");
        System.out.println("    Profile change -e <email>");
    }

    private void revalidate() {
        Response res = UserController.getByID(loggedInUser.getID());
        if (res.ok) {
            loggedInUser = (User) res.body.get("user");
        } else {
            System.out.println(res.message);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }
    }

    public Menu myMethods() {
        printMenu();
        do {
            String input = consoleScanner.nextLine().trim();
            if (input.matches("^show my profile$")) {
                System.out.println("username: " + loggedInUser.getUsername());
                System.out.println("nickname: " + loggedInUser.getNickname());
                System.out.println("email: " + loggedInUser.getEmail());
                System.out.println("recovery question: " + loggedInUser.getPassRecoveryQuestion());
                System.out.println("role: " + loggedInUser.getRole());
                System.out.println("level: " + loggedInUser.getLevel());
                System.out.println("experience: " + loggedInUser.getExperience());
                System.out.println("coins: " + loggedInUser.getCoins());
                System.out.println("hit points: " + loggedInUser.getHitPoints());
                System.out.println("progress level: " + loggedInUser.getProgress());
            } else if (input.matches("^Profile change password -o (?<oldPass>\\S+) -n (?<newPass>\\S+)$")) {
                matcher = Pattern.compile("^Profile change password -o (?<oldPass>\\S+) -n (?<newPass>\\S+)$")
                        .matcher(input);
                matcher.find();
                String newPass = matcher.group("newPass").trim();
                String oldPass = matcher.group("oldPass").trim();
                if (!oldPass.equals(loggedInUser.getPassword())) {
                    System.out.println("the old password that you entered does not match your current pass word");
                    printMenu();
                    continue;
                }
                System.out.println("Please enter your new password again: ");
                input = consoleScanner.nextLine().trim();
                if (!input.equals(newPass)) {
                    System.out.println("password confirmation failed");
                    printMenu();
                    continue;
                }
                if (!checkCaptcha()) {
                    printMenu();
                    continue;
                }
                // Response res = UserController.editPassword(loggedInUser.getID(), newPass);
                // System.out.println(res.message);
                // if( res.exception != null ){
                // System.out.println(res.exception.getMessage());
                // }
                // if( res.ok ){
                // loggedInUser.setPassword(newPass);
                // }
                // revalidate();
            } else if (input.matches("^Profile change -u (?<username>\\S+)$")) {
                matcher = Pattern.compile("^Profile change -u (?<username>\\S+)$").matcher(input);
                matcher.find();
                String newUsername = matcher.group("username").trim();
                if (loggedInUser.getUsername().equals(newUsername)) {
                    System.out.println("new username can not be the same as the last username");
                    continue;
                }
                // Response res = UserController.editUsername(loggedInUser.getID(),newUsername);
                // System.out.println(res.message);
                // if(res.exception != null){
                // System.out.println(res.exception.getMessage());
                // }
                // if( res.ok ){
                // loggedInUser.setUsername(newUsername);
                // }
            } else if (input.matches("^Profile change -n (?<nickname>\\S+)$")) {
                matcher = Pattern.compile("^Profile change -n (?<nickname>\\S+)$").matcher(input);
                matcher.find();
                // Response res = UserController.editNickname(loggedInUser.getID(),
                // matcher.group("nickname").trim());
                // System.out.println(res.message);
                // if(res.exception != null){
                // System.out.println(res.exception.getMessage());
                // }
                // if( res.ok ){
                // loggedInUser.setNickname(matcher.group("nickname").trim());
                // }
            } else if (input.matches("^Profile change -e (?<email>\\S+)$")) {
                matcher = Pattern.compile("^Profile change -e (?<email>\\S+)$").matcher(input);
                matcher.find();
                // Response res = UserController.editEmail(loggedInUser.getID(),
                // matcher.group("email").trim());
                // System.out.println(res.message);
                // if(res.exception != null){
                // System.out.println(res.exception.getMessage());
                // }
                // if( res.ok ){
                // loggedInUser.setEmail(matcher.group("email").trim());
                // }
            } else if (input.matches("^show current menu$")) {
                System.out.println("You are currently in " + getName());
            } else if (input.matches("^Back$")) {
                return new M_GameMainMenu();
            } else {
                System.out.println("invalid command!");
            }
        } while (true);
    }

    private boolean checkCaptcha() {

        while (captchaCountLeft > 0) {

            AA_Captcha captcha = new AA_Captcha();
            System.out.println(captcha.showEquation());

            int ans = -1000;
            System.out.print("Answer = ");

            try {
                ans = Integer.parseInt(consoleScanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Type Number!");
            }

            if (ans == captcha.getAnswer()) {
                return true;
            }
            captchaCountLeft--;
        }
        System.out.println("You Are Robot!!!");
        captchaCountLeft = 3;
        return false;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        captcha.setText(captchaCode.showEquation());
        questionChoice.getItems().addAll(Questions);

        backGroundIm.setImage(BGims.get(themeIndex));
        HP.setText("HP: "+Integer.toString(loggedInUser.getHitPoints()));
        name.setText("Hello "+loggedInUser.getNickname()+" !");
        coin.setText("Coin: "+Integer.toString(loggedInUser.getCoins()));
        level.setText("Level: "+Integer.toString(loggedInUser.getLevel()));
        XP.setText("XP: "+Integer.toString(loggedInUser.getExperience()));
        profileIm.setImage(charsImagesProfile[profileIndex]);

        usernameField.setText(loggedInUser.getUsername());
        nicknameField.setText(loggedInUser.getNickname());
        emailField.setText(loggedInUser.getEmail());
        passwordField.setText(loggedInUser.getPassword());
        passwordConfirmationField.setText(loggedInUser.getPassword());
        questionChoice.setValue(loggedInUser.getPassRecoveryQuestion());
        questionAnswerField.setText(loggedInUser.getPassRecoveryAnswer());


    }
    @FXML
    protected void changeProfile (MouseEvent event) throws IOException {
        profileIndex++;
        if (profileIndex== CharImageFilesProfile.length)
            profileIndex=0;
        profileIm.setImage(charsImagesProfile[profileIndex]);
    }
    @FXML
    protected void randPassButton(ActionEvent event) throws IOException {
        String randPass= randomPassword();
        passwordField.setText(randPass);
        error.setText("Random Password Created!");
    }
    @FXML
    protected void okButton(ActionEvent event) throws IOException {
        if (usernameField.getText().isBlank()) {
            error.setText("Username Field is blank!");
            return;
        }
        else if (passwordField.getText().isBlank()) {
            error.setText("Password Field is blank!");
            return;
        }
        else if (passwordConfirmationField.getText().isBlank()) {
            error.setText("Confirmation Field is blank!");
            return;
        }else if (emailField.getText().isBlank()) {
            error.setText("Email Field is blank!");
            return;
        }else if (nicknameField.getText().isBlank()) {
            error.setText("Nickname Field is blank!");
            return;
        }else if (questionAnswerField.getText().isBlank()) {
            error.setText("Answer Field is blank!");
            return;
        }else if (captchaField.getText().isBlank()) {
            error.setText("Enter captcha!");
            return;
        }else if (questionChoice.getValue()==null) {
            error.setText("Choose your question!");
            return;
        }else if (captchaField.getText().matches("\\D")) {
            error.setText("Captcha answer must be number!");
            return;
        }
        else if (Integer.parseInt(captchaField.getText())!=(captchaCode.getAnswer())) {
            error.setText("Wrong answer for captcha!");
            return;
        }

        String s = checkAll2(usernameField.getText(),
                passwordField.getText(),
                passwordConfirmationField.getText(),
                emailField.getText(),
                nicknameField.getText());

        if (!s.equals(securityQuestions) ) {
            error.setText(s);
            return;
        }
        loggedInUser.setUsername(usernameField.getText());
        loggedInUser.setEmail(emailField.getText());
        loggedInUser.setNickname(nicknameField.getText());
        loggedInUser.setPassword(passwordField.getText());
        loggedInUser.setPassRecoveryQuestion(questionChoice.getValue());
        loggedInUser.setPassRecoveryAnswer(questionAnswerField.getText());
        error.setText("Your profile edited successfully!");
    }
    @FXML
    protected void showPassButton(ActionEvent event) throws IOException {
        if (passwordField.getText().isBlank())
            error.setText("Password Field is blank!");
        else
            error.setText("Your Password is "+passwordField.getText());
    }
    @FXML
    protected void captchaButton(ActionEvent event) throws IOException {
        captchaCode =new AA_Captcha();
        captcha.setText(captchaCode.showEquation());
    }
}
