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

    public Turn(User player_one, User player_two, ArrayList<Card> player_one_cards, ArrayList<Card> player_two_cards) {
        this.player_one = player_one;
        this.player_two = player_two;
        this.player_one_cards = player_one_cards;
        this.player_two_cards = player_two_cards;
    }

    public String processTurn(User current_player, Block[][] board,
            Round round) {

        int turn_index = round.getTurns().indexOf(this);
                System.out.println("YTRRTSGSFD" + " " + turn_index);
        boolean cond = false;
        Scanner sc = new Scanner(System.in);
        ConsoleBoard.printBoard(board, current_player, current_player,
                round.getPlayerOneDamage(),
                round.getPlayerTwoDamage());

        printCurrentUserHand(current_player);
        if (current_player == player_one) {
            System.out.println(player_one.getUsername() + "'s turn, choose a card!");

        } else {
            System.out.println(player_two.getUsername() + "'s turn, choose a card!");
        }

        while (!cond) {
            String input = sc.nextLine();
            if (input.equals("Print board")) {
                ConsoleBoard.printBoard(board, current_player, current_player,
                        round.getPlayerOneDamage(),
                        round.getPlayerTwoDamage());
            } else if (input.equals("Print my cards")) {
                this.printCurrentUserHand(current_player);
            } else if (input.equals("Print opponent cards")) {
                if (current_player == player_one) {
                    ConsoleCard.printUserCards(new ArrayList<Card>(player_two_cards.subList(0, 5)), player_two);
                } else {
                    ConsoleCard.printUserCards(new ArrayList<Card>(player_one_cards.subList(0, 5)), player_one);
                }
            }

            else if (input.matches("^Select card number [0-9] player [1-2]$")) {
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
            else if (input.matches("^Placing card number [0-9] in block [0-9]$")) {
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

                    if (selected_card.getCardType().equals("Regular")) {
                        if (selected_card.getPower() > 0) {
                            // board[turn_index % 2][block_number].setBlockCard(selected_card);
                            // board[turn_index % 2][block_number].setCardHidden(false);
                            // selected_card.setPower(selected_card.getPower() - 1);
                            // cond = true;
                        } else {
                            // ConsoleGame.printCardIsOutOfPower();
                        }
                    } else {
                        // SPELL action
                    }

                } else {
                    ConsoleGame.printInvalidBlockNumber();
                }
            } else {
                System.out.println("Invalid input");
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

}
