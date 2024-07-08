package org.example.citywars;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.AI;
import models.User;
import models.card.Card;
import models.game.Block;
import models.game.Turn;
import views.console.game.ConsoleGame;

public class M_Round extends Menu {
    private User player_one;
    private User player_two;
    private boolean game_is_finished = false;
    private String winner;
    private ArrayList<Turn> turns = new ArrayList<Turn>();
    private int current_turn = 1;
    private int number_of_round_turns = 4;
    private ArrayList<Card> player_one_cards = new ArrayList<Card>();
    private ArrayList<Card> player_two_cards = new ArrayList<Card>();
    private M_Game game;
    private boolean is_player_one_turn = true;
    private int player_one_remaining_turns = number_of_round_turns / 2;
    private int player_two_remaining_turns = number_of_round_turns / 2;

    private Block[][] board = new Block[2][21];

    // java fx vars

    @FXML
    private Pane rootElement;

    public M_Round() {
        super("M_Round", new String[] { "BG-Videos\\GameBGs\\bg1.png", "BG-Videos\\GameBGs\\bg2.png",
                "BG-Videos\\GameBGs\\bg3.png" });
    }

    public Menu myMethods() {
        return new M_Game();
    }

    public void handleBotInitiation() {
        // Boss cards
        Card card_one = new Card("boss_one", 0, 21, "Regular", 20, 30, 0, 0, winner, null);
        // Card card_two = new Card("two", 0, 10, "Regular", 20, 30, 0, 0, winner,
        // null);
        // Card card_three = new Card("three", 0, 7, "Regular", 20, 30, 0, 0, winner,
        // null);

        int total_damage = 0;
        for (int i = 0; i <= 20; i++) {
            int random = (int) (Math.random() * 10) + 20;
            this.board[0][i].setBlockCard(card_one);
            this.board[0][i].setBlockEmpty(false);
            this.board[0][i].setCardHidden(true);
            this.board[0][i].setBlockPower(random);
            this.board[0][i].setBlockDamage(random);
            total_damage += random;
        }

        this.player_one.setDamage(total_damage);
    }

    public boolean processGraphicRound() {

        return false;
    }

    public void setGame(M_Game game) {
        this.game = game;
        this.player_one = game.getPlayerOne();
        this.player_two = game.getPlayerTwo();
        this.player_one_cards = game.getPlayerOneCards();
        this.player_two_cards = game.getPlayerTwoCards();
        Collections.shuffle(player_one_cards);
        Collections.shuffle(player_two_cards);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 21; j++) {
                board[i][j] = new Block();
            }
        }
        if ((this.player_one instanceof AI) && ((AI) this.player_one).getAiLevel() == 5) {
            this.changeBlocksForBoss();
            this.handleBotInitiation();
        }

        // reseting the stolen cards
        this.player_one.setCardsAreStolen(false);
        this.player_two.setCardsAreStolen(false);

        // put 1 destroyed block in board
        if (!(player_one instanceof AI) || (player_one instanceof AI && player_one.getProgress() != 5)) {
            int rand_1 = (int) (Math.random() * 21);
            int rand_2 = (int) (Math.random() * 21);

            this.board[0][rand_1].setBlockUnavailable(true);
            this.board[1][rand_2].setBlockUnavailable(true);
        }

        // add user cards
        initialUserCards();
        putRemainingTurns();
        putTotalDameges();
        putHitPoints();
    }

    public boolean timeLine() {
        // System.out.println("HEY here!");
        for (int i = 0; i <= 20; i++) {

            Block player_one_block = this.board[0][i];
            Block player_two_block = this.board[1][i];

            if (player_one_block.isBlockEmpty() && player_two_block.isBlockEmpty()) {

            } else if (player_one_block.isBlockEmpty() && !player_two_block.isBlockEmpty()) {
                this.player_one
                        .setHitPoints(this.player_one.getHitPoints() - player_two_block.getBlockDamage());
                // reduce the damage of the player
                this.player_two.setDamage(this.player_two.getDamage() - player_two_block.getBlockDamage());
            } else if (!player_one_block.isBlockEmpty() && player_two_block.isBlockEmpty()) {
                this.player_two
                        .setHitPoints(this.player_two.getHitPoints() - player_one_block.getBlockDamage());

                // reduce the damage of the player
                this.player_one.setDamage(this.player_one.getDamage() - player_one_block.getBlockDamage());
            } else {
                if (player_one_block.getBlockPower() > player_two_block.getBlockPower()) {
                    this.player_two
                            .setHitPoints(this.player_two.getHitPoints() - player_one_block.getBlockDamage());
                    this.player_one
                            .setDamage(this.player_one.getDamage() - player_one_block.getBlockDamage());

                } else if (player_one_block.getBlockPower() < player_two_block.getBlockPower()) {
                    this.player_one
                            .setHitPoints(this.player_one.getHitPoints() - player_two_block.getBlockDamage());
                    this.player_two
                            .setDamage(this.player_two.getDamage() - player_two_block.getBlockDamage());
                }

            }
            ConsoleGame.printBlockIndex(i + 1);
            ConsoleGame.printBlocksStatus(player_one_block, player_two_block);
            ConsoleGame.printDamageStatus(this.player_one, this.player_two);
            ConsoleGame.printHPStatus(this.player_one, this.player_two);

            if (this.checkGameIsFinished()) {
                return true;
            }

        }

        return false;
    }

    public boolean checkGameIsFinished() {
        if (this.player_one.getHitPoints() <= 0) {
            this.game_is_finished = true;
            this.winner = this.player_two.getUsername();
            return true;
        } else if (this.player_two.getHitPoints() <= 0) {
            this.game_is_finished = true;
            this.winner = this.player_one.getUsername();
            return true;
        }
        return false;
    }

    public void changeBlocksForBoss() {

    }

    private void initialUserCards() {
        // Example: Add images based on player's cards
        this.addImage(1, this.player_one.getIsBonusActive() ? 6 : 2);
        this.addImage(2, this.player_two.getIsBonusActive() ? 6 : 2);
    }

    private void addImage(int user_index, int number_of_cards) {
        File file = new File("src\\main\\resources\\SampleCards\\1.png");
        Image image = new Image(file.toURI().toString());
        // Image image = new Image("src/main/resources/SampleCards/1.png");

        for (int i = 0; i < number_of_cards; i++) {
            ImageView imageView = new ImageView(image);

            // Set the ID for the ImageView
            imageView.setId("cardImage_" + user_index + "_" + i);
            imageView.setFitHeight(250);
            imageView.setFitWidth(200);

            if (user_index == 1) {
                imageView.setX(300 + i * 300);

            } else {
                imageView.setX(900 + i * 300);
            }

            if (i <= 3) {
                imageView.setY(350);
            } else {
                imageView.setY(650);
            }

            rootElement.getChildren().add(imageView);
        }
    }

    public void putRemainingTurns() {
        // create a label
        Label label_1 = new Label(String.valueOf(this.player_one_remaining_turns));
        Label label_2 = new Label(String.valueOf(this.player_two_remaining_turns));

        label_1.setLayoutX(100);
        label_1.setLayoutY(120);

        label_1.setId("turn_label_1");
        label_2.setId("turn_label_2");

        label_2.setLayoutX(100);
        label_2.setLayoutY(220);

        // set color and size
        label_1.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        label_2.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

        rootElement.getChildren().add(label_1);
        rootElement.getChildren().add(label_2);
    }

    public void putTotalDameges() {
        // create a label

        Label label_1 = new Label(String.valueOf(this.player_one.getDamage()));
        Label label_2 = new Label(String.valueOf(this.player_two.getDamage()));

        label_1.setLayoutX(1800);
        label_1.setLayoutY(120);

        label_1.setId("damage_label_1");
        label_2.setId("damage_label_2");

        label_2.setLayoutX(1800);
        label_2.setLayoutY(220);

        // set color and size
        label_1.setStyle("-fx-text-fill: white; -fx-font-size: 25px; -fx-font-weight: bold;");
        label_2.setStyle("-fx-text-fill: white; -fx-font-size: 25px; -fx-font-weight: bold;");

        rootElement.getChildren().add(label_1);
        rootElement.getChildren().add(label_2);
    }

    public void putHitPoints() {

        Label label_1 = new Label(String.valueOf(this.player_one.getHitPoints()));
        Label label_2 = new Label(String.valueOf(this.player_two.getHitPoints()));

        label_1.setLayoutX(170);
        label_1.setLayoutY(440);

        label_1.setId("hp_label_1");
        label_2.setId("hp_label_2");

        label_2.setLayoutX(1730);
        label_2.setLayoutY(440);

        label_1.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");
        label_2.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        rootElement.getChildren().add(label_1);
        rootElement.getChildren().add(label_2);
    }

    // getters and setters
    public User getPlayer_one() {
        return player_one;
    }

    public void setPlayer_one(User player_one) {
        this.player_one = player_one;
    }

    public User getPlayer_two() {
        return player_two;
    }

    public void setPlayer_two(User player_two) {
        this.player_two = player_two;
    }

    public boolean isGame_is_finished() {
        return game_is_finished;
    }

    public void setGame_is_finished(boolean game_is_finished) {
        this.game_is_finished = game_is_finished;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }

    public void setTurns(ArrayList<Turn> turns) {
        this.turns = turns;
    }

    public Block[][] getBoard() {
        return board;
    }

    public ArrayList<Card> getPlayer_one_cards() {
        return player_one_cards;
    }

    public void setPlayer_one_cards(ArrayList<Card> player_one_cards) {
        this.player_one_cards = player_one_cards;
    }

    public ArrayList<Card> getPlayer_two_cards() {
        return player_two_cards;
    }

    public void setPlayer_two_cards(ArrayList<Card> player_two_cards) {
        this.player_two_cards = player_two_cards;
    }

    public void setBoard(Block[][] board) {
        this.board = board;
    }

    public int getNumberOfRoundTurns() {
        return number_of_round_turns;
    }

    public void setNumberOfRoundTurns(int number_of_round_turns) {
        this.number_of_round_turns = number_of_round_turns;
    }

    public M_Game getGame() {
        return game;
    }

    public String getResult() {
        return this.winner;
    }
}
