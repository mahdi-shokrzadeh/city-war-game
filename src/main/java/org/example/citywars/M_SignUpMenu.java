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
import java.util.Scanner;
import java.util.regex.Pattern;

public class M_SignUpMenu extends Menu {

    private static final String ADMIN_PASSWORD = "Admin@1403";
    final String securityQuestions = "Please choose a security question :\n" +
            "• 1-What is your father’s name ?\n" +
            "• 2-What is your favourite color ?\n" +
            "• 3-What was the name of your first pet?";
    String securityQuestionNo;
    String securityQuestionAns;
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
    int captchaCountLeft;

    public M_SignUpMenu() {
        super("M_SignUpMenu", "BG1.mp4");
        captchaCountLeft = 3;
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("^ *user +create +-u(?<username>[\\S ]+)-p(?<password>[\\S ]+) +(?<passwordConf>[\\S ]+)-email(?<email>[\\S ]+)-n(?<nickname>[\\S ]+) *$"));
        patterns.add(Pattern.compile("^ *user +create +-u(?<username>[\\S ]+)-p +random *-email(?<email>[\\S ]+)-n(?<nickname>[\\S ]+) *$"));
        patterns.add(Pattern.compile("^ *question +pick +-q *(?<qNumber>[1-3]) *-a(?<answer>[\\S ]+)-c(?<answerConf>[\\S ]+) *$"));
    }

    public Menu myMethods(String input) {
        Scanner tempSc = new Scanner(System.in);

        if (input.matches("^ *Back *$"))
            return new M_Intro();

        matcher = patterns.get(1).matcher(input);
        if (matcher.find()) {
            String randPass = randomPassword();

            String s = checkAll(matcher.group("username"),
                    randPass,
                    randPass,
                    matcher.group("email"),
                    matcher.group("nickname"));

            if (!s.equals(securityQuestions)) {
                System.out.println(s);
                return this;
            }

            System.out.println("Your random password: " + randPass + "\nPlease enter your password : ");
            String passConf = tempSc.nextLine();
            if (!passConf.equals(randPass)) {
                System.out.println("Wrong Answer; Sign Up again from beginning :( ");
                return this;
            }

            System.out.println(s);

            return securityQuestionAndCaptcha(tempSc, matcher.group("username").trim(), randPass, matcher.group("nickname").trim(), matcher.group("email").trim(), "player");
        }

        matcher = patterns.get(0).matcher(input);
        if (matcher.find()) {

            String s = checkAll(matcher.group("username"),
                    matcher.group("password"),
                    matcher.group("passwordConf"),
                    matcher.group("email"),
                    matcher.group("nickname"));

            System.out.println(s);
            if (!s.equals(securityQuestions)) {
                return this;
            }
            String role = "player";
            if (matcher.group("password").trim().equals(ADMIN_PASSWORD))
                role = "admin";
            return securityQuestionAndCaptcha(tempSc, matcher.group("username").trim(), matcher.group("password").trim(), matcher.group("nickname").trim(), matcher.group("email").trim(), role);
        }


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
        } else if (getIndexFromUsername(matcher.group("username").trim()) != -1) {
            System.out.println("Username already exists!");
        } else if (passwordProblem(password.trim()) != null) {
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

    private void setSecurityQuestion(Scanner tempSc) {
        String sqAnswer = "";
        while (sqAnswer.isBlank()) {
            sqAnswer = tempSc.nextLine();

            matcher = patterns.get(2).matcher(sqAnswer);
            if (matcher.find()) {

                if (matcher.group("answer").trim().equals(matcher.group("answerConf").trim())) {
                    securityQuestionAns = matcher.group("answer").trim();
                    securityQuestionNo = matcher.group("qNumber").trim();
                    System.out.println("Question and your answer saved successfully!");
                    return;
                }
                System.out.println("Try Again!");

                setSecurityQuestion(tempSc);

            } else {
                System.out.println("Invalid input! :(");
                sqAnswer = "";
            }
        }
    }

    private Menu securityQuestionAndCaptcha(Scanner tempSc, String username, String password, String nickname, String email, String role) {
        setSecurityQuestion(tempSc);

        while (captchaCountLeft > 0) {

            AA_Captcha captcha = new AA_Captcha();
            System.out.println(captcha.showEquation());

            int ans = -1000;
            System.out.print("Answer = ");

            try {
                ans = tempSc.nextInt();
            } catch (Exception e) {
                System.out.println("Type Number!");
            }

            if (ans == captcha.getAnswer()) {
                System.out.println("From front: User " + username + " created successfully!");
                System.out.println("From back: " + UserController.createUser(username, password, nickname, email, role, securityQuestionNo, securityQuestionAns).message);
                return new M_LoginMenu();
            }
            captchaCountLeft--;
        }
        System.out.println("You Are Robot!!!");

        captchaCountLeft = 3;
        return this;
    }

    int getIndexFromUsername(String username) {
        Response res = UserController.sudoGetAllUsers();
        List<User> allUsers = null;
        if (res.ok) {
            System.out.println("OKOKOKOKOKOKOKOKOK");

            allUsers = (List<User>) res.body.get("allUsers");
        }
        if (allUsers != null) {

            System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGG@@@@@@@@@@@@@@@@@@@");

            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getUsername().equals(username)) {

                    System.out.println(i);
                    return i;
                }
            }
        }
        return -1;
    }

    //Control Methods
    @FXML
    protected void GoToLoginButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_LoginMenu();
        switchMenus(event);

//        mediaPlayer.play();
    }

}
