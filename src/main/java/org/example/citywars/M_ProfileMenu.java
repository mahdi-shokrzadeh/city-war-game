package org.example.citywars;

import controllers.UserController;
import database.DBs.UserDB;
import models.AA_Captcha;
import models.Response;
import models.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M_ProfileMenu extends Menu {
    private int captchaCountLeft = 3;

    public M_ProfileMenu() {
        super("M_ProfileMenu");
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
}
