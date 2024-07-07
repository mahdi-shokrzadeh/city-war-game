package org.example.citywars;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import controllers.game.GameController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.AI;
import models.Clan;
import models.ClanBattle;
import models.GameCharacter;
import models.Response;
import models.User;
import views.console.menu.ConsoleGameMenu;

public class M_GamePlayMenu extends Menu {
    boolean gameOver;

    File[] imageFiles;
    Image[] charsImages;
    @FXML
    ImageView imv1;
    @FXML
    ImageView imv2;
    @FXML
    ImageView imv3;
    @FXML
    Label name;

    public M_GamePlayMenu() {
        super("M_GamePlayMenu", new String[]{"BG-Videos\\BG-signUp.png"});
        gameOver = false;
        ConsoleGameMenu.printGameMenu();

        imageFiles = new File[3];
        imageFiles[0]=  new File("src/main/resources/BG-Videos/GameModeIcons/icon1.png");
        imageFiles[1]=  new File("src/main/resources/BG-Videos/GameModeIcons/icon2.png");
        imageFiles[2]=  new File("src/main/resources/BG-Videos/GameModeIcons/icon3.png");
        charsImages=new Image[imageFiles.length];
        for (int i = 0; i < imageFiles.length; i++) {
            charsImages[i] = new Image(imageFiles[i].toURI().toString());
        }
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
                Menu temp_men2 = new M_Game(AI, loggedInUser, "AI");
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
                            return new M_Game(loggedInUser, opponent,
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backGroundIm.setImage(BGims.get(themeIndex));

        imv1.setImage(charsImages[0]);
        imv2.setImage(charsImages[1]);
        imv3.setImage(charsImages[2]);
    }
    @FXML
    protected void choiceCh1 (MouseEvent event) throws IOException {
        secondPersonNeeded = true;
        HelloApplication.menu = new M_LoginMenu();
        switchMenus(event);
    }
    @FXML
    protected void mouseInterCh1 (MouseEvent event) throws IOException {
        imv1.setFitHeight(430.0);
        imv1.setFitWidth(430.0);
        imv2.setOpacity(0.5);
        imv3.setOpacity(0.5);
        name.setText("Duel");
    }
    @FXML
    protected void mouseExitCh1 (MouseEvent event) throws IOException {
        imv1.setFitHeight(400.0);
        imv1.setFitWidth(400.0);
        imv2.setOpacity(1);
        imv3.setOpacity(1);
        name.setText(" ");
    }

    @FXML
    protected void choiceCh2 (MouseEvent event) throws IOException {
        is_bet=true;
        secondPersonNeeded = true;
        HelloApplication.menu = new M_LoginMenu();
        switchMenus(event);
    }
    @FXML
    protected void mouseInterCh2 (MouseEvent event) throws IOException {
        imv2.setFitHeight(430.0);
        imv2.setFitWidth(430.0);
        imv1.setOpacity(0.5);
        imv3.setOpacity(0.5);
        name.setText("Gamble");
    }
    @FXML
    protected void mouseExitCh2 (MouseEvent event) throws IOException {
        imv2.setFitHeight(400.0);
        imv2.setFitWidth(400.0);
        imv1.setOpacity(1);
        imv3.setOpacity(1);
        name.setText(" ");
    }

    @FXML
    protected void choiceCh3 (MouseEvent event) throws IOException {
        int ai_level = loggedInUser.getProgress();
        AI AI = new AI(ai_level);
        AI.setGameCharacter(new GameCharacter("BOT"));
        HelloApplication.menu = new M_Game();

        switchMenus(event);
    }
    @FXML
    protected void mouseInterCh3 (MouseEvent event) throws IOException {
        imv3.setFitHeight(430.0);
        imv3.setFitWidth(430.0);
        imv2.setOpacity(0.5);
        imv1.setOpacity(0.5);
        name.setText("Mono");
    }
    @FXML
    protected void mouseExitCh3 (MouseEvent event) throws IOException {
        imv3.setFitHeight(400.0);
        imv3.setFitWidth(400.0);
        imv2.setOpacity(1);
        imv1.setOpacity(1);
        name.setText(" ");
    }

}
