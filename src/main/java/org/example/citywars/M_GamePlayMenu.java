package org.example.citywars;

import controllers.UserController;
import controllers.game.GameController;
import models.AI;
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

        User player_two = new User("player_two", "password",
                "nickname2", "email", "role", "recovery_pass_question", "recovery_pass_answer");

        AI AI = new AI();

        switch (input) {
            case "2":
                Menu temp_men = new Game(player_one, player_two, "duel");
                return temp_men;

            case "1":
                Menu temp_men2 = new Game(AI, player_one, "AI");
                return temp_men2;

            default:

                break;
        }

        if (gameOver) {
            return new M_GameOverMenu();
        }

        return this;
    }
}
