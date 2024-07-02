package models.game;

import java.util.ArrayList;
import java.util.Scanner;

import models.GameCharacter;
import models.User;
import models.card.Card;
import views.console.game.ConsoleBoard;
import views.console.game.ConsoleCard;

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
                    ConsoleCard.printUserCards(player_two_cards, player_two);
                } else {
                    ConsoleCard.printUserCards(player_one_cards, player_one);
                }
            } else {
                System.out.println("Invalid command");
            }
        }

        return "turn_is_finished";
    }

    public void printCurrentUserHand(User player) {
        if (player == player_one) {
            ConsoleCard.printUserCards(player_one_cards, player);
        } else {
            ConsoleCard.printUserCards(player_two_cards, player);
        }
    }

}
