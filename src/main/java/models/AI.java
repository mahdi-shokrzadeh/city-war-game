package models;

import java.util.ArrayList;
import java.util.Random;

import com.almasb.fxgl.dev.Console;

import models.card.Card;
import models.game.Block;
import views.console.game.ConsoleGame;

public class AI extends User {

    private boolean is_AI = true;
    private int ai_level;
    private Card chosenCard;

    public AI(int ai_level) {
        super("AI", "password", "AI", "email", "role", "recovery_pass_question", "recovery_pass_answer");
        this.ai_level = ai_level;
    }

    public String chooseTheMove(Block[][] board, ArrayList<Card> AICards) {
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
            }
        }
    }

    private String chooseRandomCardFrontMove(Block[][] board, ArrayList<Card> AICards) {
        Random random = new Random();
        while (true) {
            int cardIndex = random.nextInt(this.getIsBonusActive() ? 6 : 5);
            Card card = AICards.get(cardIndex);
            for (int i = 0; i <= board[0].length - card.getDuration(); i++) {
                if (isPlaceValid(board, card, i) && card.getCardType().toString().equals("Regular")) {
                    return String.format("Placing card number %d in block %d", cardIndex + 1, i + 1);
                }
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

    public void handleBoss(Block[][] board) {
        int i = 0;
        while (i < 2) {
            Random random = new Random();
            int random1 = random.nextInt(21);
            if (!board[0][random1].isBlockDestroyed()) {
                int random_power = random.nextInt(2) + 3;
                board[0][random1].setBlockPower(board[0][random1].getBlockPower() + random_power);
                i++;
                ConsoleGame.printBossDecision(random1 + 1, random_power);
            }
        }

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
