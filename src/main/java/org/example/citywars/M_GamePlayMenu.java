package org.example.citywars;

import java.util.HashMap;
import java.util.Scanner;

import controllers.UserController;
import controllers.game.GameController;

import models.AI;
import models.Clan;
import models.ClanBattle;
import models.GameCharacter;
import models.Response;
import models.User;
import models.game.Game;
import views.console.menu.ConsoleGameMenu;

public class M_GamePlayMenu extends Menu {
    boolean gameOver;

    public M_GamePlayMenu() {
        super("M_GamePlayMenu",true,"BG-Videos\\BG-signUp.png");
        gameOver = false;
        ConsoleGameMenu.printGameMenu();
    }

    public Menu myMethods() {
        String input = consoleScanner.nextLine();
        // public User(String username, String password, String nickname, String email,
        // String role,
        // String recovery_pass_question, String recovery_pass_answer) {
        // User player_one = new User("username1", "password", "nickname1", "email",
        // "role", "recovery_pass_question",
        // "recovery_pass_answer");

        // User player_two = new User("player_two", "password",
        // "nickname2", "email", "role", "recovery_pass_question",
        // "recovery_pass_answer");

        int ai_level = 1;
        // switch (loggedInUser.getProgress()) {
        // case 1:
        // ai_level = 1;
        // break;
        // case 2:
        // ai_level = 2;
        // break;
        // case 3:
        // ai_level = 3;
        // break;
        // case 4:
        // ai_level = 4;
        // break;

        // default:
        // // Boss!
        // ai_level = 5;
        // break;
        // }
        ai_level = loggedInUser.getProgress();

        switch (input) {
            case "1":
                AI AI = new AI(ai_level);
                AI.setGameCharacter(new GameCharacter("BOT"));
                Menu temp_men2 = new Game(AI, loggedInUser, "AI");
                return temp_men2;

            case "2":
                System.out.println("\nSecond person login:\n");
                secondPersonNeeded = true;
                return new M_LoginMenu();

            case "3":

                Response res = GameController.getGameEssentials(loggedInUser);
                if (res.ok) {
                    ClanBattle battle = (ClanBattle) res.body.get("battle");
                    Clan attackerClan = (Clan) res.body.get("attackerClan");
                    Clan defenderClan = (Clan) res.body.get("defenderClan");
                    User opponent = (User) res.body.get("opponent");

                    // hadnle second user login
                    System.out.println("Second player login!");
                    System.out.println(opponent.getNickname() + "! Please enter you password:");
                    boolean cond = false;
                    Scanner sc = new Scanner(System.in);
                    while (!cond) {
                        String k = sc.nextLine();
                        if (k.equals(opponent.getPassword())) {
                            cond = false;
                            return new Game(loggedInUser, opponent,
                                    "clan", battle, attackerClan, defenderClan);
                        } else {
                            System.out.println("Password is not true!");
                        }
                    }
                } else {
                    System.out.println(res.message);
                }

                break;

            case "4":
                System.out.println("\nSecond person login:\n");
                secondPersonNeeded = true;
                is_bet = true;
                return new M_LoginMenu();

            default:
                return new M_GameMainMenu();

        }

        // if (gameOver) {
        // return new M_GameOverMenu();
        // }

        return this;
    }
}
