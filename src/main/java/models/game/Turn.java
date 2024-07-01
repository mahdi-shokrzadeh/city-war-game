package models.game;

import java.util.ArrayList;
import java.util.Scanner;

import models.GameCharacter;
import models.User;
import models.card.Card;
import views.console.game.ConsoleBoard;

public class Turn {
    private User player_one;
    private User player_two;

    public Turn(User player_one, User player_two) {
        this.player_one = player_one;
        this.player_two = player_two;
    }

    public String processTurn(User current_player, Block[][] board,
            ArrayList<Card> player_one_cards, ArrayList<Card> player_two_cards, Round round) {

        boolean cond = false;
        Scanner sc = new Scanner(System.in);
        ConsoleBoard.printBoard(board, current_player, current_player,
                round.getPlayerOneDamage(),
                round.getPlayerTwoDamage());

        while (!cond) {
            String input = sc.nextLine();
            if (input.equals("Print board")) {
                ConsoleBoard.printBoard(board, current_player, current_player,
                        round.getPlayerOneDamage(),
                        round.getPlayerTwoDamage());
            } else {
                System.out.println("Invalid command");
            }
        }

        return "turn_is_finished";
    }
}
