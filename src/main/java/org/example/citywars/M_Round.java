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

    @FXML
    private Pane rootElement;

    private ImageView selectedCard = null;

    private ImageView followMouseImage = new ImageView(
            new Image(new File("src\\main\\resources\\GameElements\\f1.png").toURI().toString()));

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
        initializeFollowMouseImage();
        initializeMouseMoveListener();
    }

    public void initializeFollowMouseImage() {
        followMouseImage.setFitHeight(100);
        followMouseImage.setFitWidth(50);
        followMouseImage.setVisible(false);
        rootElement.getChildren().add(followMouseImage);

        rootElement.setOnMouseMoved(event -> {
            if (followMouseImage.isVisible()) {
                followMouseImage.setLayoutX(event.getSceneX() + 20);
                followMouseImage.setLayoutY(event.getSceneY() + 20);
            }

            Label mouse_x_label = (Label) rootElement.lookup("#mouse_x_label");
            Label mouse_y_label = (Label) rootElement.lookup("#mouse_y_label");

            mouse_x_label.setText("X: " + event.getSceneX());
            mouse_y_label.setText("Y: " + event.getSceneY());
        });
    }

    public void initializeMouseMoveListener() {
        // rootElement.setOnMouseReleased(event -> {
        // if (selectedCard != null) {
        // int x = (int) event.getSceneX();
        // int y = (int) event.getSceneY();

        // int block_index = (x - 300) / 100;
        // int user_index = selectedCard.getId().charAt(9) - '0';
        // int card_index = selectedCard.getId().charAt(11) - '0';

        // if (user_index == 1) {
        // if (player_one_remaining_turns == 0) {
        // return;
        // }
        // player_one_remaining_turns--;
        // } else {
        // if (player_two_remaining_turns == 0) {
        // return;
        // }
        // player_two_remaining_turns--;
        // }
        // }
        // });
    }

    private void initialUserCards() {
        this.addImage(1, this.player_one.getIsBonusActive() ? 6 : 2);
        this.addImage(2, this.player_two.getIsBonusActive() ? 6 : 2);
    }

    private void addImage(int user_index, int number_of_cards) {
        File file = new File("src\\main\\resources\\SampleCards\\1.png");
        Image image = new Image(file.toURI().toString());

        for (int i = 0; i < number_of_cards; i++) {
            ImageView imageView = new ImageView(image);
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

            addCardEventHandlers(imageView);
            rootElement.getChildren().add(imageView);
        }
    }

    private void addCardEventHandlers(ImageView imageView) {
        imageView.setOnMousePressed(event -> {
            handleCardSelection(imageView);
            event.consume();
        });

        imageView.setOnMouseDragged(event -> {
            if (selectedCard == imageView) {
                followMouseImage.setVisible(true);
                followMouseImage.setLayoutX(event.getSceneX() + 20);
                followMouseImage.setLayoutY(event.getSceneY() + 20);
                imageView.setVisible(false);
            }
        });

        imageView.setOnMouseReleased(event -> {

            int x = (int) event.getSceneX();
            int y = (int) event.getSceneY();

            if (selectedCard == imageView) {
                if (checkValidForBoard(x, y)) {
                    // Remove the card if dropped on the board
                    rootElement.getChildren().remove(imageView);
                    followMouseImage.setVisible(false);
                } else {
                    // Reset visibility if not dropped on the board
                    imageView.setVisible(true);
                    followMouseImage.setVisible(false);
                }
                selectedCard.setStyle(null);
                selectedCard = null;
            }
        });
    }

    public void handleCardSelection(ImageView imageView) {
        if (selectedCard != null) {
            selectedCard.setStyle(null);
        }
        imageView.setStyle("-fx-effect: dropshadow(gaussian, green, 11, 0, 0, 0);");
        selectedCard = imageView;
        followMouseImage.setVisible(true);
    }

    public boolean checkValidForBoard(int x, int y) {
        int min_y;
        int max_y;

        if (is_player_one_turn) {
            min_y = 80;
            max_y = 180;
        } else {
            min_y = 190;
            max_y = 290;
        }

        if (y < min_y || y > max_y) {
            return false;
        }

        int block_index = (int) ((x - 160) / 75);

        if (is_player_one_turn) {
            return handlePutCardInBoard(0, player_one_cards.get(0), block_index + 1);
        } else {
            return handlePutCardInBoard(1, player_two_cards.get(0), block_index + 1);
        }

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

    public void putRemainingTurns() {

        Label mouse_x_label = new Label();
        Label mouse_y_label = new Label();
        mouse_x_label.setLayoutX(1500);
        mouse_x_label.setLayoutY(1000);
        mouse_x_label.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        mouse_y_label.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        mouse_y_label.setLayoutX(1200);
        mouse_y_label.setLayoutY(1000);

        mouse_x_label.setId("mouse_x_label");
        mouse_y_label.setId("mouse_y_label");

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

        rootElement.getChildren().add(mouse_x_label);
        rootElement.getChildren().add(mouse_y_label);
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
        label_1.setLayoutY(410);

        label_1.setId("hp_label_1");
        label_2.setId("hp_label_2");

        label_2.setLayoutX(1730);
        label_2.setLayoutY(410);

        label_1.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");
        label_2.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        rootElement.getChildren().add(label_1);
        rootElement.getChildren().add(label_2);
    }

    public void updateRemainingTurns() {
        Label label_1 = (Label) rootElement.lookup("#turn_label_1");
        Label label_2 = (Label) rootElement.lookup("#turn_label_2");

        label_1.setText(String.valueOf(this.player_one_remaining_turns));
        label_2.setText(String.valueOf(this.player_two_remaining_turns));
    }

    public void updateTotalDameges() {
        Label label_1 = (Label) rootElement.lookup("#damage_label_1");
        Label label_2 = (Label) rootElement.lookup("#damage_label_2");

        label_1.setText(String.valueOf(this.player_one.getDamage()));
        label_2.setText(String.valueOf(this.player_two.getDamage()));
    }

    public void updateHitPoints() {
        Label label_1 = (Label) rootElement.lookup("#hp_label_1");
        Label label_2 = (Label) rootElement.lookup("#hp_label_2");

        label_1.setText(String.valueOf(this.player_one.getHitPoints()));
        label_2.setText(String.valueOf(this.player_two.getHitPoints()));
    }

    public boolean handlePutCardInBoard(int turn_number, Card card, int starting_block_number) {
        starting_block_number--;
        boolean cond = true;
        for (int i = 0; i < card.getDuration(); i++) {
            if (!checkBlockIsValidForCard(turn_number, starting_block_number + i)) {
                cond = false;
                break;
            }
        }
        if (cond) {
            for (int i = 0; i < card.getDuration(); i++) {
                this.board[turn_number][starting_block_number + i].setBlockCard(card);
                this.board[turn_number][starting_block_number + i].setBlockEmpty(false);
                try {
                    handleAffection(turn_number, starting_block_number + i);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            // place f1 graphicaly in the borad

            // create copy image of f1
            ImageView im = new ImageView(
                    new Image(new File("src\\main\\resources\\GameElements\\f1.png").toURI().toString()));
            im.setFitHeight(100);
            im.setFitWidth(50);
            im.setLayoutX(160 + (starting_block_number) * 75);
            if (turn_number == 0) {
                im.setLayoutY(80);
            } else {
                im.setLayoutY(190);
            }

            rootElement.getChildren().add(im);

        } else {
            return false;
        }
        // ConsoleGame.printSuccessfulCardPlacement();
        // Check for Bonous
        // this.checkBonous();
        // card.getCharacter().getPFactor()
        if (turn_number == 0) {
            if (Math.random() < card.getCharacter().getPFactor() && !this.player_one.getIsBonusActive()
                    && card.getCardType().toString().equals("Regular")) {

                if (card.getCardType().toString().equals(player_one_cards.get(2).getCardType().toString())) {
                    player_one_cards.get(2).setPower(player_one_cards.get(2).getPower() + 2);
                    ConsoleGame.printBuffCard(3, 2);
                }
            }

            player_one_cards.remove(card);
        } else {
            if (Math.random() < card.getCharacter().getPFactor() && !this.player_two.getIsBonusActive()
                    && card.getCardType().toString().equals("Regular")) {
                if (card.getCardType().toString().equals(player_two_cards.get(2).getCardType().toString())) {
                    player_two_cards.get(2).setPower(player_two_cards.get(2).getPower() + 2);
                    ConsoleGame.printBuffCard(3, 2);
                }
            }
            player_two_cards.remove(card);
        }

        return true;
    }

    public boolean checkBlockIsValidForCard(int des_index, int block_number) {
        if (block_number < 0 || block_number > 20) {
            ConsoleGame.printInvalidBlockNumber();
            return false;
        }
        Block bl = this.board[des_index][block_number];

        if (bl.isBlockUnavailable()) {
            ConsoleGame.printBlockIsUnavailable();
            return false;
        } else if (!bl.isBlockEmpty()) {
            ConsoleGame.printBlockIsNotEmpty();
            return false;
        }
        return true;
    }

    public void handleAffection(int des_index, int block_number) throws Exception {
        // if (this.board[des_index][block_number].getBlockCard() == null) {
        // // should return if the crad is spell and has no duration!
        // return;
        // }
        // Block bl = this.board[des_index][block_number];
        // Block opponent_block = this.board[(des_index + 1) % 2][block_number];
        // if (bl.getBlockCard().getCardType().toString().equals("Regular")) {
        // this.current_player.setDamage(this.current_player.getDamage() +
        // bl.getBlockDamage());
        // }
        // if (!opponent_block.isBlockEmpty() && !opponent_block.isBlockUnavailable()) {
        // User op = getOpponent(this.current_player);
        // if (bl.getBlockPower() > opponent_block.getBlockPower()) {
        // opponent_block.setBlockDestroyed(true);
        // opponent_destroyed_blocks.add(opponent_block);
        // // reduce the damage
        // op.setDamage(op.getDamage() - opponent_block.getBlockDamage());

        // } else if (bl.getBlockPower() < opponent_block.getBlockPower()) {
        // bl.setBlockDestroyed(true);
        // // reduce the damage
        // this.current_player.setDamage(this.current_player.getDamage()
        // - bl.getBlockDamage());
        // } else {
        // // powers are equal
        // bl.setBlockDestroyed(true);
        // opponent_block.setBlockDestroyed(true);
        // // reduce the damage
        // this.current_player.setDamage(this.current_player.getDamage()
        // - bl.getBlockDamage());

        // op.setDamage(op.getDamage() - opponent_block.getBlockDamage());

        // }
        // }
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
