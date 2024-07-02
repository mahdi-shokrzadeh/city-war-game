package models.game;

import java.util.ArrayList;
import java.util.Scanner;
import models.User;
import models.card.Card;
import views.console.game.ConsoleBoard;
import views.console.game.ConsoleCard;
import views.console.game.ConsoleGame;

public class Turn {
    private User player_one;
    private User player_two;
    private ArrayList<Card> player_one_cards;
    private ArrayList<Card> player_two_cards;
    private User current_player;
    private Block[][] board = new Block[2][21];

    public Turn(User player_one, User player_two, ArrayList<Card> player_one_cards, ArrayList<Card> player_two_cards,
            Block[][] board) {
        this.player_one = player_one;
        this.player_two = player_two;
        this.player_one_cards = player_one_cards;
        this.player_two_cards = player_two_cards;
        this.board = board;
    }

    public String processTurn(Block[][] board,
            Round round) {

        int turn_index = round.getTurns().indexOf(this);
        if (turn_index % 2 == 0) {
            current_player = player_one;
        } else {
            current_player = player_two;
        }
        ConsoleGame.printTurnIsStarted(turn_index + 1);
        boolean cond = false;
        Scanner sc = new Scanner(System.in);
        ConsoleBoard.printBoard(board, player_one, player_two,
                player_one.getDamage(),
                player_two.getDamage());

        if (current_player == player_one) {
            printCurrentUserHand(player_two);
            printCurrentUserHand(current_player);
            System.out.println(player_one.getUsername() + "'s turn, choose a card!");

        } else {
            printCurrentUserHand(player_one);
            printCurrentUserHand(current_player);
            System.out.println(player_two.getUsername() + "'s turn, choose a card!");
        }

        while (!cond) {
            String input = sc.nextLine();
            if (input.equals("Print board")) {
                ConsoleBoard.printBoard(board, player_one, player_two,
                        player_one.getDamage(),
                        player_two.getDamage());
            } else if (input.equals("Print my cards")) {
                this.printCurrentUserHand(current_player);
            } else if (input.equals("Print opponent cards")) {
                if (current_player == player_one) {
                    ConsoleCard.printUserCards(new ArrayList<Card>(player_two_cards.subList(0, 5)), player_two);
                } else {
                    ConsoleCard.printUserCards(new ArrayList<Card>(player_one_cards.subList(0, 5)), player_one);
                }
            }

            else if (input.matches("^Select card number ([1-5]) player ([1-2])$")) {
                String[] parts = input.split(" ");
                int card_number = Integer.parseInt(parts[3]);
                int player_number = Integer.parseInt(parts[5]);
                if (player_number == 1) {
                    if (card_number > 0 && card_number <= 5) {
                        Card selected_card = player_one_cards.get(card_number - 1);
                        ConsoleCard.printCard(card_number, selected_card);
                    } else {
                        ConsoleGame.printInvalidCardNumber();
                    }
                } else if (player_number == 2) {
                    if (card_number > 0 && card_number <= 5) {
                        Card selected_card = player_two_cards.get(card_number - 1);
                        ConsoleCard.printCard(card_number, selected_card);
                    } else {
                        ConsoleGame.printInvalidCardNumber();
                    }
                } else {
                    ConsoleGame.printInvalidPlayerNumber();
                }
            }
            // regex for -Placing card number n in block i
            else if (input.matches("^Placing card number ([1-5]) in block ([1-9]|1[0-9]|2[0-1])$")) {
                String[] parts = input.split(" ");
                int card_number = Integer.parseInt(parts[3]);
                int block_number = Integer.parseInt(parts[6]);
                if (card_number > 0 && card_number <= 5 && block_number >= 0 && block_number <= 21) {
                    Card selected_card;
                    if (current_player == player_one) {
                        selected_card = player_one_cards.get(card_number - 1);
                    } else {
                        selected_card = player_two_cards.get(card_number - 1);
                    }

                    if (selected_card.getCardType().toString().equals("Regular")) {
                        if (handlePutCardInBoard((turn_index) % 2, selected_card, block_number)) {
                            // Turn is finished
                            ConsoleGame.printTurnIsFinished(turn_index + 1);
                            cond = true;
                        }
                    } else {
                        // SPELL action
                    }
                    // ConsoleBoard.printBoard(board, player_one, player_two,
                    // player_one.getDamage(),
                    // player_two.getDamage());

                } else {
                    ConsoleGame.printInvalidBlockNumber();
                }
            } else {
                ConsoleGame.printInvaidInput();
            }

        }

        return "turn_is_finished";
    }

    public void printCurrentUserHand(User player) {
        if (player == player_one) {
            ConsoleCard.printUserCards(new ArrayList<>(player_one_cards.subList(0, 5)), player);
        } else {
            ConsoleCard.printUserCards(new ArrayList<>(player_two_cards.subList(0, 5)), player);
        }
    }

    public boolean handlePutCardInBoard(int des_index, Card card, int starting_block_number) {
        starting_block_number--;
        boolean cond = true;
        for (int i = 0; i < card.getDuration(); i++) {
            if (!checkBlockIsValidForCard(des_index, starting_block_number + i)) {
                cond = false;
                break;
            }
        }
        if (cond) {
            for (int i = 0; i < card.getDuration(); i++) {
                this.board[des_index][starting_block_number + i].setBlockCard(card);
                this.board[des_index][starting_block_number + i].setBlockEmpty(false);
                handleAffection(des_index, starting_block_number + i);
            }
        } else {
            return false;
        }
        ConsoleGame.printSuccessfulCardPlacement();
        if (des_index == 0) {
            player_one_cards.remove(card);
        } else {
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

    public void handleAffection(int des_index, int block_number) {
        Block bl = this.board[des_index][block_number];
        Block opponent_block = this.board[(des_index + 1) % 2][block_number];
        if (bl.getBlockCard().getCardType().toString().equals("Regular")) {
            this.current_player.setDamage(this.current_player.getDamage() + bl.getBlockCard().getDamage());
        }
        if (!opponent_block.isBlockEmpty() && !opponent_block.isBlockUnavailable()) {
            User op = getOpponent(this.current_player);
            if (bl.getBlockPower() > opponent_block.getBlockPower()) {
                opponent_block.setBlockDestroyed(true);
                // reduce the damage
                op.setDamage(op.getDamage() - opponent_block.getBlockCard().getDamage());

            } else if (bl.getBlockPower() < opponent_block.getBlockPower()) {
                bl.setBlockDestroyed(true);
                // reduce the damage
                this.current_player.setDamage(this.current_player.getDamage()
                        - bl.getBlockCard().getDamage());
            } else {
                // powers are equal
                bl.setBlockDestroyed(true);
                opponent_block.setBlockDestroyed(true);
                // reduce the damage
                this.current_player.setDamage(this.current_player.getDamage()
                        - bl.getBlockCard().getDamage());

                op.setDamage(op.getDamage() - opponent_block.getBlockCard().getDamage());

            }
        }
    }

    public User getOpponent(User current_player) {
        if (current_player == player_one) {
            return player_two;
        } else {
            return player_one;
        }
    }

}
