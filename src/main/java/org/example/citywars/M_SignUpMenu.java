package org.example.citywars;

import controllers.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.AA_Captcha;
import models.Response;
import models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static controllers.UserController.sudoGetAllUsers;

public class M_SignUpMenu extends Menu {

    final String securityQuestions = "Please choose a security question using the following format :\n" +
            "question pick -q question_number -a answer -c answer_confirmation\n"+
            "• 1-What is your father’s name ?\n" +
            "• 2-What is your favourite color ?\n" +
            "• 3-What was the name of your first pet?";
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
    @FXML
    ProgressBar ErrorTimer;
    int captchaCountLeft;
    private String securityQuestion;
    private String securityQuestionAnswer;
    private String pass; // for command line
    AA_Captcha captchaCode;

    public M_SignUpMenu() {
        super("M_SignUpMenu", new String[]{"BG-Videos\\BG-signUp.png"});
        captchaCountLeft = 3;
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("^*user +create +-u (?<username>[\\S ]+) -p (?<password>[\\S ]+) +(?<passwordConf>[\\S ]+) -email (?<email>[\\S ]+) -n (?<nickname>[\\S ]+)*$"));
        patterns.add(Pattern.compile("^*user +create +-u (?<username>[\\S ]+) -p +random *-email (?<email>[\\S ]+) -n (?<nickname>[\\S ]+)*$"));
        patterns.add(Pattern.compile("^*question +pick +-q *(?<qNumber>[1-3]) *-a (?<answer>[\\S ]+) -c (?<answerConf>[\\S ]+) *$"));
    }

    private void printMenu(){
        System.out.println("SIGN UP MENU");
        System.out.println("Options: ");
        System.out.println("    Back");
        System.out.println("Information: ");
        System.out.println("    You can signup in this menu using the one of the two following formats: ");
        System.out.println("        user create -u <username> -p <password> <password_confirmation> -email <email> -n <nickname>");
        System.out.println("        user create -u <username> -p random -email <email> -n <nickname>");
    }

    public Menu myMethods() {
        printMenu();
        String input;
        do {
            input = consoleScanner.nextLine().trim();
            if (input.toLowerCase().matches("^back$")){
                return new M_Intro();
            }else if (patterns.get(1).matcher(input).find()) {
                matcher = patterns.get(1).matcher(input);
                matcher.find();
                String randPass = randomPassword();
                String s = checkAll(matcher.group("username"),
                        randPass,
                        randPass,
                        matcher.group("email"),
                        matcher.group("nickname"));

                if (!s.equals(securityQuestions)) {
                    System.out.println(s);
                    continue;
                }

                System.out.println("Your random password: " + randPass + "\nPlease enter your password : ");
                String passConf = consoleScanner.nextLine().trim();
                if (!passConf.equals(randPass)) {
                    System.out.println("Wrong Answer; Sign Up again from beginning :( ");
                    continue;
                }
                pass =  passConf;

                System.out.println(s);

                Menu menu = securityQuestionAndCaptcha(matcher.group("username").trim());
                if( menu == null ){
                    printMenu();
                }else{
                    return menu;
                }
            }else if (patterns.get(0).matcher(input).find()) {
                matcher = patterns.get(0).matcher(input);
                matcher.find();
                String s = checkAll(matcher.group("username"),
                        matcher.group("password"),
                        matcher.group("passwordConf"),
                        matcher.group("email"),
                        matcher.group("nickname"));

                System.out.println(s);
                if (!s.equals(securityQuestions)) {
                    printMenu();
                    continue;
                }

                pass = matcher.group("password");

                Menu menu =  securityQuestionAndCaptcha(matcher.group("username").trim());
                if( menu != null ){
                    return menu;
                }
            }else if(Pattern.compile("^show current menu$").matcher(input).find()){
                System.out.println("you are currently in " + getName());
            }else {
                System.out.println("Invalid command!");
            }
        }while (true);
    }

    private String randomPassword() {
        Random random = new Random();
        int length = random.nextInt(10) + 8;
        String s1 = "0123456789";
        String s2 = "!@#$%^&*";
        String s3 = "qwertyuiopasdfghjklzxcvbnm";
        String s4 = s3.toUpperCase();
        String s5 = s1 + s2 + s3 + s4;

        StringBuilder output = new StringBuilder();
        output.append(s4.charAt(random.nextInt(s4.length() - 1)));
        output.append(s3.charAt(random.nextInt(s3.length() - 1)));
        output.append(s2.charAt(random.nextInt(s2.length() - 1)));
        output.append(s1.charAt(random.nextInt(s1.length() - 1)));

        for (int i = 3; i < length; i++) {
            output.append(s5.charAt(random.nextInt(s5.length() - 1)));
        }
        return output.toString();
    }

    private String checkAll(String username, String password, String passwordConf, String email, String nickname) {
        String out = "";

        if (username.isBlank() ||
                password.isBlank() ||
                passwordConf.isBlank() ||
                email.isBlank() ||
                nickname.isBlank())
            out = "Blank Field!";
        else if (!username.trim().matches("[a-zA-Z]+")) {
            out = "Incorrect format for username!";
        }  else if (getIndexFromUsername(matcher.group("username").trim()) != -1) {
                System.out.println("Username already exists!");
            } else if (passwordProblem(password.trim()) != null) {
//            System.out.println("|" + password.trim() + "|");
            out = passwordProblem(password.trim());
        } else if (!passwordConf.trim().equals(password.trim())) {
            out = "Password confirmation doesn't match!";
        } else if (!email.trim().matches("^[a-zA-Z]+@[a-zA-Z]+.com$")) {
            out = "Incorrect format for username!";
        } else {
            out = securityQuestions;
        }
        return out;
    }

    private void setSecurityQuestion() {
        String sqAnswer = "";
        while (sqAnswer.isBlank()) {
            sqAnswer = consoleScanner.nextLine().trim();
            Matcher _matcher = patterns.get(2).matcher(sqAnswer);
            if (_matcher.find()) {
               switch (_matcher.group("qNumber")){
                    case "1":{
                        securityQuestion = "What is your father’s name ?";
                        break;
                    } case "2":{
                        securityQuestion = "What is your favourite color ?";
                        break;
                    } case "3":{
                        securityQuestion = "What was the name of your first pet?";
                        break;
                    }
                }
                securityQuestionAnswer = _matcher.group("answer");
                System.out.println("Question and your answer saved successfully!");
            } else {
                System.out.println("Invalid input! :(");
                sqAnswer = "";
            }
        }
    }

    private Menu securityQuestionAndCaptcha(String username){
        setSecurityQuestion();

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
                Response res = UserController.createUser(matcher.group("username"),pass,matcher.group("nickname"),matcher.group("email"),"admin",securityQuestion, securityQuestionAnswer);
                if(res.ok) {
                    System.out.println("User " + username + " created successfully!");
                    return new M_LoginMenu();
                }else {
                    System.out.println(res.message);
                    return null;
                }
            }
            captchaCountLeft--;
        }
        System.out.println("You Are Robot!!!");

        captchaCountLeft = 3;
        return null;
    }

    private int getIndexFromUsername(String username){
        Response res = sudoGetAllUsers();
        List<User> allUsers = null;
        if(res.ok) {
            allUsers = (List<User>) res.body.get("allUsers");
        }
        boolean duplicateUserName = false;
        if( allUsers != null ) {
            for (int i = 0; i < allUsers.size() ; i++) {
                if (allUsers.get(i).getUsername().equals(username)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @FXML
    protected void signupButton(ActionEvent event) throws IOException {
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
        }else if (questionChoice.getSelectionModel().isEmpty()) {
            error.setText("Choose your question!");
            return;
        }

        String s = checkAll(usernameField.getText(),
                passwordField.getText(),
                passwordConfirmationField.getText(),
                emailField.getText(),
                nicknameField.getText());

        if (!s.equals(securityQuestions)) {
            error.setText(s);
            return;
        }

        pass = matcher.group("password");

        Menu menu =  securityQuestionAndCaptcha(matcher.group("username").trim());
        if( menu != null ){
            return menu;
        }
    }

    @FXML
    protected void randPassButton(ActionEvent event) throws IOException {
        String randPass= randomPassword();
        passwordField.setText(randPass);
        error.setText("Random Password Created!");
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
