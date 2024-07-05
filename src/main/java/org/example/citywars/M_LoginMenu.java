package org.example.citywars;

import controllers.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.Response;
import models.User;
import models.game.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.regex.Pattern;

public class M_LoginMenu extends Menu {
    public static Timer timer;
    public static boolean timerIsOn;
    public static int lockTime;
    public static int failureCount;

    public M_LoginMenu(){
        super("M_LoginMenu","BG1.mp4");
        lockTime = 0;
        failureCount = 0;
        M_LoginMenu.timerIsOn=false;
        patterns = new ArrayList<>();
        patterns.add(Pattern.compile("^ *user +login +-u(?<username>[\\S ]+)-p(?<password>[\\S ]+) *$"));
        patterns.add(Pattern.compile("^ *Forgot +my +password +-u(?<username>[\\S ]+) *$"));
    }
    private void printMenu(){
        System.out.println("LOGIN MENU");
        System.out.println("Options: ");
        System.out.println("    Back");
        System.out.println("Information: ");
        System.out.println("    You can login in using following format: ");
        System.out.println("        user login -u <username> -p <password>");
        System.out.println("    if you forget your password login using your security question by typing: ");
        System.out.println("        Forgot my password -u <username>");
    }
    public Menu myMethods(){
        printMenu();
        String input;

        do {
            input = consoleScanner.nextLine();

            if (input.toLowerCase().matches("^ *back *$")) {
                if (secondPersonNeeded)
                    return new M_GamePlayMenu();
                else
                    return new M_Intro();
            }else if (patterns.get(0).matcher(input).find()) {
                matcher = patterns.get(0).matcher(input);
                matcher.find();
                Response s=UserController.login(matcher.group("username").trim(),matcher.group("password").trim());
                System.out.println(s.message);
                if (s.ok){
                    if (secondPersonNeeded) {
                        if (((User)s.body.get("user")).getID()==loggedInUser.getID()){
                            System.out.println("\n!!! Same user, login again !!!\n");
                            return this;
                        } else
                            return new Game(loggedInUser,(User)s.body.get("user"),"duel");
                    }
                    else {
                        loggedInUser=(User)s.body.get("user");
                        return new M_GameMainMenu();
                    }
                }
            }else if (patterns.get(1).matcher(input).find()) {
                matcher = patterns.get(1).matcher(input);
                matcher.find();
                Response s = UserController.forgotPassword(matcher.group("username").trim());
                System.out.println(s.message);
                if (s.ok){
                    if (secondPersonNeeded) {
                        if (((User)s.body.get("user")).getID()==loggedInUser.getID()){
                            System.out.println("\n!!! Same user, login again !!!\n");
                            return this;
                        } else
                            return new Game(loggedInUser,(User)s.body.get("user"),"duel");
                    }
                    else {
                        loggedInUser=(User)s.body.get("user");
                        return new M_GameMainMenu();
                    }
                }
            }
            else if(Pattern.compile("^show current menu$").matcher(input).find()){
                System.out.println("you are currently in " + getName());
            }else {
                System.out.println("Invalid command!");
            }
        } while (true);
    }

    //Control Methods
    @FXML
    protected void GoToSignUpButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_SignUpMenu();
        switchMenus(event);
    }
}
