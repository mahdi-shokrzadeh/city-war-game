package org.example.citywars;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import controllers.GameCharacterController;
import controllers.UserCardsController;
import controllers.game.GameController;
import models.AI;
import models.Clan;
import models.ClanBattle;
import models.GameCharacter;
import models.Response;
import models.User;
import models.card.Card;
import models.game.AddCard;
import views.console.game.ConsoleGame;

public class M_Game extends Menu {

    private int id;
    private int player_one_id;
    private int player_two_id;
    private String mode;
    private String created_at;
    private String ended_at;
    private String winner;
    private User winner_user;
    private String reward;
    private int number_of_rounds;
    // private String winner_reward;
    // private String looser_reward;

    // @FXML
    // Button bt;

    // Only Class Vars
    static int bet_amount;
    private ArrayList<M_Round> rounds = new ArrayList<M_Round>();
    private User player_one;
    private User player_two;

    private ArrayList<Card> player_one_cards = new ArrayList<Card>();
    private ArrayList<Card> player_two_cards = new ArrayList<Card>();

    private M_Round current_round;
    private ClanBattle battle;
    private Clan attackerClan;
    private Clan defenderClan;
    private Stage st;

    @FXML
    ImageView timeLineWalker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ldt = LocalDateTime.now();
            this.created_at = ldt.format(formatter);
            this.player_one_id = player_one.getID();
            this.player_two_id = player_two.getID();
            this.handleAddCardsToPlayers();
            Random random = new Random();
            int i = random.nextInt(BGims.size());
            backGroundIm.setImage(BGims.get(i));
            System.out.println("User1 char: " + this.player_one.getGameCharacter().getName());
            System.out.println("User2 char: " + this.player_two.getGameCharacter().getName());
            this.startGraphicGame();
        });
        // bt.setOnAction(event -> {
        // this.handleAddCardsToPlayers();
        // this.startGraphicGame();
        // });
    }

    public M_Game() {
        super("M_Game", new String[] { "BG-Videos\\GameBGs\\bg1.png", "BG-Videos\\GameBGs\\bg2.png",
                "BG-Videos\\GameBGs\\bg3.png" });
        this.player_one = loggedInUser;
        if (secondPersonNeeded) {
            System.out.print("Please enter the second player's username: ");
            this.player_two = secondUser;
            if (is_bet) {
                mode = "bet";
            } else {
                mode = "duel";
            }
        } else {
            this.mode = "AI";
            this.player_two = loggedInUser;
            AI AI = new AI(loggedInUser.getProgress());
            AI.setGameCharacter(new GameCharacter("BOT"));
            this.player_one = AI;
        }
    }

    public M_Game(User player_one, User player_two, String mode) {
        super("M_Game");
        this.player_one = player_one;
        this.player_two = player_two;
        this.mode = mode;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        this.created_at = ldt.format(formatter);

        // rounds.add(new M_Round(player_one, player_two, player_one_cards,
        // player_two_cards));
        this.current_round = rounds.get(0);
        this.player_one_id = player_one.getID();
        this.player_two_id = player_two.getID();

        ConsoleGame.printGreetings();

        switch (mode) {

            case "duel":

                break;

            case "AI":
                break;

            case "bet":

                break;

            default:
                break;
        }

    }

    public M_Game(User player_one, User player_two, String mode, ClanBattle battle,
            Clan attackerClan, Clan defenderClan) {
        super("M_Game");
        this.player_one = player_one;
        this.player_two = player_two;
        this.mode = mode;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        this.created_at = ldt.format(formatter);

        // rounds.add(new M_Round(player_one, player_two, player_one_cards,
        // player_two_cards));
        // this.current_round = rounds.get(0);
        this.player_one_id = player_one.getID();
        this.player_two_id = player_two.getID();
        this.battle = battle;
        this.defenderClan = defenderClan;
        this.attackerClan = attackerClan;
        // this.handleAddCardsToPlayers();
    }

    // not related to me :)
    public M_Game(User player_one, String mode) {
        super("M_Game");
        this.player_one = player_one;
        this.mode = mode;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        this.created_at = ldt.format(formatter);
    }

    @Override
    public Menu myMethods() {

        return this;
    }

    public void handleAddCardsToPlayers() {

        if (this.mode.equals("AI")) {
            AddCard.addCard(this.player_one_cards);
            // AddCard.addCard(this.player_two_cards);
        }

        // from database
        if (!this.mode.equals("AI")) {
            Response res_1 = UserCardsController.getUsersCards(this.player_one);
            if (res_1.ok) {
                Object obj = res_1.body.get("cards");
                if (obj instanceof ArrayList<?>) {
                    for (Object o : (ArrayList<?>) obj) {
                        if (o instanceof Card) {
                            this.player_one_cards.add((Card) o);
                        }
                    }
                }
                Collections.shuffle(player_one_cards);

            } else {
                System.out.println(res_1.exception.getMessage());
            }
        }

        Response res_2 = UserCardsController.getUsersCards(this.player_two);
        if (res_2.ok) {
            Object obj = res_2.body.get("cards");
            if (obj instanceof ArrayList<?>) {
                for (Object o : (ArrayList<?>) obj) {
                    if (o instanceof Card) {
                        this.player_two_cards.add((Card) o);
                    }
                }
            }
            Collections.shuffle(player_two_cards);
            System.out.println("DOM!");
        } else {
            System.out.println(res_2.message);
        }

    }

    //

    // getter and setter methods

    public int getID() {
        return id;
    }

    public void setID(int _id) {
        id = _id;
    }

    public int getPlayer_one_id() {
        return player_one_id;
    }

    public void setPlayer_one_id(int player_one_id) {
        this.player_one_id = player_one_id;
    }

    public int getPlayer_two_id() {
        return player_two_id;
    }

    public void setPlayer_two_id(int player_two_id) {
        this.player_two_id = player_two_id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEnded_at() {
        return ended_at;
    }

    public void setEnded_at(String ended_at) {
        this.ended_at = ended_at;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public int getNumber_of_rounds() {
        return number_of_rounds;
    }

    public void setNumber_of_rounds(int number_of_rounds) {
        this.number_of_rounds = number_of_rounds;
    }

    public int getBet_amount() {
        return bet_amount;
    }

    public void setBet_amount(int bet_amount) {
        this.bet_amount = bet_amount;
    }

    public ArrayList<M_Round> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<M_Round> rounds) {
        this.rounds = rounds;
    }

    public String getWinnerReward() {
        return winner_reward;
    }

    public void setWinnerReward(String r) {
        winner_reward = r;
    }

    public String getLoserReward() {
        return looser_reward;
    }

    public void setLoserReward(String r) {
        looser_reward = r;
    }

    public void findWinner() {
        if (player_one.getHitPoints() > player_two.getHitPoints()) {
            this.winner = player_one.getNickname();
            this.winner_user = player_one;
            ConsoleGame.printWinner(player_one.getUsername());
        } else {
            this.winner = player_two.getNickname();
            this.winner_user = player_two;
            ConsoleGame.printWinner(player_two.getUsername());
        }
    }

    // graphic related!
    public boolean startGraphicGame() {
        if (((this.mode.equals("duel"))
                && (this.player_one.getGameCharacter() == null ||
                        this.player_two.getGameCharacter() == null))
                || (this.player_two.getGameCharacter() == null)) {
            System.out.println("Please select character first!");
            return false;
        } else if (this.mode.equals("bet") && this.bet_amount == 0) {
            ConsoleGame.printBetNotSet();
            return false;
        }

        System.out.println("User1:" + this.player_one.getUsername() + " User2:" + this.player_two.getUsername());
        // Platform.runLater(() -> this.handleAddCardsToPlayers());
        showRound();
        return true;
    }

    public void showRound() {
        try {
            // FXMLLoader loader = new FXMLLoader(getClass().getResource("M_Round.fxml"));
            // Parent root = loader.load();
            // Scene scene = new Scene(root);
            // this.st.setScene(scene);
            // M_Round controller = loader.getController();
            // M_Round x = new M_Round();
            // HelloApplication.menu = x;
            // switchMenus();
            // System.out.println("HEY ROUND IS OPENED!");
            // x.setGame(this);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("M_Round.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = HelloApplication.primaryStage;
            stage.setScene(scene);
            M_Round controller = loader.getController();
            controller.setGame(this);
            this.rounds.add(new M_Round());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleRoundResult(String result) {
        switch (result) {
            case "game_is_finished":
                showAlert("Game Over", "The game has finished.");
                findWinner();
                // handleRewards();
                break;

            case "need_more_rounds":
                // Start a new round
                Platform.runLater(() -> startGraphicGame());
                break;

            default:
                break;
        }
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void handleGameOver() {
        String w = this.winner.equals(this.player_one.getNickname()) ? "player_one" : "player_two";
        Response res;
        try {

            switch (this.mode) {

                case "duel":
                    res = GameController.createGame(this, player_one, player_two,
                            this.rounds.size(),
                            w, this.player_one_cards, this.player_two_cards);
                    if (res.ok) {
                        winner_reward = (String) res.body.get("winner");
                        looser_reward = (String) res.body.get("loser");
                    } else {
                        System.out.println(res.message);
                    }
                    break;

                case "AI":
                    res = GameController.createBotGame(this, player_two, this.rounds.size(), w,
                            player_two_cards);
                    if (res.ok) {
                        winner_reward = (String) res.body.get("winner");
                        looser_reward = (String) res.body.get("loser");
                    } else {
                        System.out.println(res.message);
                    }
                    break;

                case "bet":
                    res = GameController.createGambleGame(this, player_one, player_two,
                            this.rounds.size(),
                            w,
                            bet_amount);
                    if (res.ok) {
                        winner_reward = (String) res.body.get("winner");
                        looser_reward = (String) res.body.get("loser");
                    } else {
                        System.out.println(res.message);
                    }
                    break;

                default:
                    break;

            }
            // game over menu

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            // HelloApplication.menu = new M_GameOverMenu();
            // switchMenus();

            // System.out.println("win: " + winner_reward);
            // System.out.println("los: " + looser_reward);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("M_GameOverMenu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = HelloApplication.primaryStage;
            stage.setScene(scene);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public User getPlayerOne() {
        return player_one;
    }

    public User getPlayerTwo() {
        return player_two;
    }

    public ArrayList<Card> getPlayerOneCards() {
        return player_one_cards;
    }

    public ArrayList<Card> getPlayerTwoCards() {
        return player_two_cards;
    }

    public void setPlayerOneCards(ArrayList<Card> cards) {
        player_one_cards = cards;
    }

    public void setPlayerTwoCards(ArrayList<Card> cards) {
        player_two_cards = cards;
    }

    public void setWinnerUser(User user) {
        winner_user = user;
    }

    public User getWinnerUser() {
        return winner_user;
    }
}