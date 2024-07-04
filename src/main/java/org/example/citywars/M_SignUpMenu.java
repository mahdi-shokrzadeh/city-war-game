package org.example.citywars;

import controllers.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.AA_Captcha;
import models.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M_SignUpMenu extends Menu {

    final String securityQuestions = "Please choose a security question :\n" +
            "• 1-What is your father’s name ?\n" +
            "• 2-What is your favourite color ?\n" +
            "• 3-What was the name of your first pet?";
    @FXML
    Label error;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    Label password;
    PasswordField passwordConfirmationField;
    ChoiceBox<String> questionChoice;
    TextField questionAnswerField;
    TextField captchaField;
    TextField emailField;
    TextField nicknameField;
    int captchaCountLeft;
    private String securityQuestion;
    private String securityQuestionAnswer;

    public M_SignUpMenu() {
        super("M_SignUpMenu",true, "BG-Videos/BG-signUp.png");
        captchaCountLeft = 3;
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("^*user +create +-u (?<username>[\\S ]+) -p (?<password>[\\S ]+) +(?<passwordConf>[\\S ]+) -email (?<email>[\\S ]+) -n (?<nickname>[\\S ]+)*$"));
        patterns.add(Pattern.compile("^*user +create +-u (?<username>[\\S ]+) -p +random *-email (?<email>[\\S ]+) -n (?<nickname>[\\S ]+)*$"));
        patterns.add(Pattern.compile("^*question +pick +-q *(?<qNumber>[1-3]) *-a (?<answer>[\\S ]+) -c (?<answerConf>[\\S ]+) *$"));
    }

    private void printMenu(){
        System.out.println("Signup menu");
        System.out.println("Options: ");
        System.out.println("    Back");
        System.out.println("Information: ");
        System.out.println("    You can signup in this menu using the one of the two following formats: ");
        System.out.println("        user create -u username -p password password_confirmation -email email -n nickname");
        System.out.println("        user create -u username -p random -email email -n nickname");
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

                System.out.println(s);

                Menu menu = securityQuestionAndCaptcha(matcher.group("username").trim());
                if( menu == null ){
                    printMenu();
                    continue;
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
                Menu menu =  securityQuestionAndCaptcha(matcher.group("username").trim());
                if( menu == null ){
                    printMenu();
                    continue;
                }else{
                    return menu;
                }
            }else if(Pattern.compile("^show current menu$").matcher(input).find()){
                System.out.println("you are currently in " + getName());
            }else {
                System.out.println("Invalid command!");
            }


//            System.out.println(randomPassword());
//            System.out.println(randomPassword());
//            System.out.println(randomPassword());
//
//            AA_Captcha a = new AA_Captcha();
//            AA_Captcha b = new AA_Captcha();
//            AA_Captcha c = new AA_Captcha();
//
//            System.out.println(a.showEquation());
//            System.out.println(a.getAnswer());
//
//            System.out.println(b.showEquation());
//            System.out.println(b.getAnswer());
//
//            System.out.println(c.showEquation());
//            System.out.println(c.getAnswer());
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
        }  /*else if (getIndexFromUsername(matcher.group("username").trim()) != -1) {
                System.out.println("Username already exists!");
            } */ else if (passwordProblem(password.trim()) != null) {
            System.out.println("|" + password.trim() + "|");
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
                ans = consoleScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Type Number!");
            }

            if (ans == captcha.getAnswer()) {
                Response res = UserController.createUser(matcher.group("username"),matcher.group("password"),matcher.group("nickname"),matcher.group("email"),"admin",securityQuestion, securityQuestionAnswer);
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

}
