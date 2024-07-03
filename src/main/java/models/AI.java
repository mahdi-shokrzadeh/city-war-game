package models;

import java.util.ArrayList;
import models.card.Card;
import models.game.Block;

public class AI extends User {

    private boolean is_AI = true;
    private Card chosenCard;

    public AI() {
        super("AI", "password", "AI", "email", "role", "recovery_pass_question", "recovery_pass_answer");
    }

    public String chooseTheMove(Block[][] board, ArrayList<Card> AICards) {
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
                if (isPlaceValid(board, bestCard, i) && isPowerValid(board, bestCard, i)) {
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
                if (isPlaceValid(board, bestCard, i)) {
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

    // getter setter
    public boolean isAI() {
        return this.is_AI;
    }

    public void setIsAI(boolean is_AI) {
        this.is_AI = is_AI;
    }
}
