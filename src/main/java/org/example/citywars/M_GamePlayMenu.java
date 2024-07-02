package org.example.citywars;

import controllers.UserController;
import controllers.game.GameController;
import models.User;
import models.game.Game;
import views.console.menu.ConsoleGameMenu;

public class M_GamePlayMenu extends Menu {
    boolean gameOver;

    public M_GamePlayMenu() {
        super("M_GamePlayMenu");
        gameOver = false;
        ConsoleGameMenu.printGameMenu();
    }

    public Menu myMethods() {
        String input = consoleScanner.nextLine();
        // public User(String username, String password, String nickname, String email,
        // String role,
        // String recovery_pass_question, String recovery_pass_answer) {
        User player_one = new User("username1", "password", "nickname1", "email", "role", "recovery_pass_question",
                "recovery_pass_answer");
        loggedInUser = player_one;//temporary

        User player_two = new User("player_two", "password",
                "nickname2", "email", "role", "recovery_pass_question", "recovery_pass_answer");

        switch (input) {
            case "2":
                System.out.println("\nSecond person login:\n");
                secondPersonNeeded =true;
                return new M_LoginMenu();

            default:

                break;
        }

        if (gameOver) {
            return new M_GameOverMenu();
        }

        return this;
    }
}
