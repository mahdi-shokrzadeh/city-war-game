package models;

import java.util.ArrayList;
import java.util.Random;

import org.example.citywars.M_Round;

import com.almasb.fxgl.dev.Console;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.card.Card;
import models.game.Block;
import models.game.SpellAffect;
import views.console.game.ConsoleGame;

public class AI extends User {
    // Bots spell cards: SpaceShift , Repair , RoundReduce

    private boolean is_AI = true;
    private int ai_level;
    private Card chosenCard;
    private M_Round round;

    public AI(int ai_level) {
        super("AI", "password", "AI", "email", "role", "recovery_pass_question", "recovery_pass_answer");
        this.ai_level = ai_level;
    }

    public String chooseTheMove(Block[][] board, ArrayList<Card> AICards, M_Round round) {
        this.round = round;
        switch (ai_level) {
            case 1:
                return chooseRandomMove(board, AICards);
            case 2:
                return chooseRandomCardFrontMove(board, AICards);
            case 3:
                return chooseBestCardFrontMove(board, AICards);
            case 4:
                return chooseBestMove(board, AICards);
            default:
                return "Invalid AI level";
        }
    }

    private String chooseRandomMove(Block[][] board, ArrayList<Card> AICards) {
        Random random = new Random();
        while (true) {
            int cardIndex = random.nextInt(this.getIsBonusActive() ? 6 : 5);
            Card card = AICards.get(cardIndex);
            int startBlock = random.nextInt(board[0].length - card.getDuration() + 1);
            if (isPlaceValid(board, card, startBlock) && card.getCardType().toString().equals("Regular")) {
                return String.format("Placing card number %d in block %d", cardIndex + 1, startBlock + 1);
            } else if (card.getCardType().toString().equals("Spell")) {
                SpellAffect spe = new SpellAffect(card, 0, 5, board, this, round, AICards);
                try {
                    spe.spellHandler();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "Spell card " + card.getName() + " has been used!";
            }
        }
    }

    private String chooseRandomCardFrontMove(Block[][] board, ArrayList<Card> AICards) {
        Random random = new Random();
        while (true) {
            int cardIndex = random.nextInt(this.getIsBonusActive() ? 6 : 5);
            Card card = AICards.get(cardIndex);
            if (card.getCardType().toString().equals("Regular")) {

                for (int i = 0; i <= board[0].length - card.getDuration(); i++) {
                    if (isPlaceValid(board, card, i) && card.getCardType().toString().equals("Regular")) {
                        return String.format("Placing card number %d in block %d", cardIndex + 1, i + 1);
                    }
                }
            } else if (card.getCardType().toString().equals("Spell")) {
                SpellAffect spe = new SpellAffect(card, 0, 5, board, this, round, AICards);
                try {
                    spe.spellHandler();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "Spell card " + card.getName() + " has been used!";
            }
        }
    }

    private String chooseBestCardFrontMove(Block[][] board, ArrayList<Card> AICards) {
        Card bestCard = null;
        int bestPower = -1;

        for (int i = 0; i <= (this.getIsBonusActive() ? 5 : 4); i++) {
            Card card = AICards.get(i);
            if (card.getPower() > bestPower) {
                bestCard = card;
                bestPower = card.getPower();
            }
        }

        if (bestCard == null) {
            return "No valid card to place";
        }

        for (int i = 0; i <= board[0].length - bestCard.getDuration(); i++) {
            if (isPlaceValid(board, bestCard, i) && bestCard.getCardType().toString().equals("Regular")) {
                return String.format("Placing card number %d in block %d", AICards.indexOf(bestCard) + 1, i + 1);
            }
        }

        // find a spell card to use
        for (int i = 0; i <= (this.getIsBonusActive() ? 5 : 4); i++) {
            Card card = AICards.get(i);
            if (card.getCardType().toString().equals("Spell")) {
                SpellAffect spe = new SpellAffect(card, 0, 5, board, this, round, AICards);
                try {
                    spe.spellHandler();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "Spell card " + card.getName() + " has been used!";
            }
        }

        return "No available block to place the card";
    }

    private String chooseBestMove(Block[][] board, ArrayList<Card> AICards) {
        Card bestCard = null;
        int bestPower = -1;

        int maxCardsToConsider = this.getIsBonusActive() ? 5 : 4;
        ArrayList<Card> consideredCards = new ArrayList<>();

        for (int i = 0; i <= maxCardsToConsider; i++) {
            Card card = AICards.get(i);
            if (card.getCardType().toString().equals("Regular")) {
                consideredCards.add(card);
            }
        }

        while (!consideredCards.isEmpty()) {

            for (Card card : consideredCards) {
                if (card.getPower() > bestPower) {
                    bestCard = card;
                    bestPower = card.getPower();
                }
            }

            if (bestCard == null) {
                return "No valid card to place";
            }

            chosenCard = bestCard;

            for (int i = 0; i <= board[0].length - bestCard.getDuration(); i++) {
                if (isPlaceValid(board, bestCard, i) && isPowerValid(board, bestCard, i)
                        && bestCard.getCardType().toString().equals("Regular")) {
                    return String.format("Placing card number %d in block %d", AICards.indexOf(bestCard) + 1, i + 1);
                }
            }

            consideredCards.remove(bestCard);
            bestCard = null;
            bestPower = -1;
        }

        consideredCards.clear();
        for (int i = 0; i <= maxCardsToConsider; i++) {
            Card card = AICards.get(i);
            if (card.getCardType().toString().equals("Regular")) {
                consideredCards.add(card);
            }
        }

        while (!consideredCards.isEmpty()) {

            for (Card card : consideredCards) {
                if (card.getPower() > bestPower) {
                    bestCard = card;
                    bestPower = card.getPower();
                }
            }

            if (bestCard == null) {
                return "No valid card to place";
            }

            chosenCard = bestCard;

            for (int i = 0; i <= board[0].length - bestCard.getDuration(); i++) {
                if (isPlaceValid(board, bestCard, i) && bestCard.getCardType().toString().equals("Regular")) {
                    return String.format("Placing card number %d in block %d", AICards.indexOf(bestCard) + 1, i + 1);
                }
            }

            consideredCards.remove(bestCard);
            bestCard = null;
            bestPower = -1;
        }

        // find a spell card to use
        for (int i = 0; i <= maxCardsToConsider; i++) {
            Card card = AICards.get(i);
            if (card.getCardType().toString().equals("Spell")) {
                SpellAffect spe = new SpellAffect(card, 0, 5, board, this, round, AICards);
                try {
                    spe.spellHandler();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "Spell card " + card.getName() + " has been used!";
            }
        }

        return "No available block to place the card";
    }

    private boolean isPlaceValid(Block[][] board, Card card, int startBlock) {
        for (int i = startBlock; i < startBlock + card.getDuration(); i++) {
            if (board[0][i].isBlockUnavailable() || !board[0][i].isBlockEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean isPowerValid(Block[][] board, Card card, int startBlock) {
        for (int i = startBlock; i < startBlock + card.getDuration(); i++) {
            if (!board[1][i].isBlockEmpty() && board[1][i].getBlockPower() > card.getPower()) {
                return false;
            }
        }
        return true;
    }

    public void handleBoss(Block[][] board, @SuppressWarnings("exports") Pane rootElement) {
        int i = 0;

        // seacrch for label with id boss_label
        Label boss_label = (Label) rootElement.lookup("#boss_label");
        if (boss_label != null) {
            boss_label.setText("Boss bot is playing!!");
        } else {
            boss_label = new Label("Boss bot is playing!!");
            boss_label.setId("boss_label");
            boss_label.setLayoutX(120);
            boss_label.setLayoutY(540);
            // boss_label.setStyle("-fx-font-size: 29px; -fx-font-weight: bold;
            // -fx-text-fill: red;");
            // boss_label.setStyle(
            //         "-fx-effect: dropshadow(three-pass-box, blue, 10, 0, 0, 0); -fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: gray;");
            rootElement.getChildren().add(boss_label);

        }
        String s = "";
        while (i < 2) {
            Random random = new Random();
            int random1 = random.nextInt(21);
            if (!board[0][random1].isBlockDestroyed()) {
                int random_power = random.nextInt(2) + 3;
                board[0][random1].setBlockPower(board[0][random1].getBlockPower() + random_power);
                i++;
                s = s + "\nBoss bot increased the power of the card number " + random1 + " by " + random_power + "!!";
                // wait for 1.5 seconds
            }
        }

        // choose random color for the label
        Random random = new Random();
        int random_color = random.nextInt(6);
        switch (random_color) {
            case 0:
                boss_label.setStyle(
                        "-fx-effect: dropshadow(three-pass-box, blue, 10, 0, 0, 0); -fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: red;");
                break;
            case 1:
                boss_label.setStyle(
                        "-fx-effect: dropshadow(three-pass-box, blue, 10, 0, 0, 0); -fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: blue;");
                break;
            case 2:
                boss_label.setStyle(
                        "-fx-effect: dropshadow(three-pass-box, blue, 10, 0, 0, 0); -fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: green;");
                break;
            case 3:
                boss_label.setStyle(
                        "-fx-effect: dropshadow(three-pass-box, blue, 10, 0, 0, 0); -fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: yellow;");
                break;
            case 4:
                boss_label.setStyle(
                        "-fx-effect: dropshadow(three-pass-box, blue, 10, 0, 0, 0); -fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: orange;");
                break;
            case 5:
                boss_label.setStyle(
                        "-fx-effect: dropshadow(three-pass-box, blue, 10, 0, 0, 0); -fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: purple;");
                break;
            default:
                boss_label.setStyle(
                        "-fx-effect: dropshadow(three-pass-box, blue, 10, 0, 0, 0); -fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: gray;");
                break;
        }
        boss_label.setText(s);

    }

    // getter setter
    public boolean isAI() {
        return this.is_AI;
    }

    public void setIsAI(boolean is_AI) {
        this.is_AI = is_AI;
    }

    public int getAiLevel() {
        return this.ai_level;
    }

    public void setAiLevel(int ai_level) {
        this.ai_level = ai_level;
    }
}
