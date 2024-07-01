package models.game;

import java.util.ArrayList;

import models.User;
import models.card.Card;

public class Turn {
    private User player_one;
    private User player_two;

    public Turn(User player_one, User player_two) {
        this.player_one = player_one;
        this.player_two = player_two;
    }

    public String processTurn(User current_player, Block[][] board,
            ArrayList<Card> player_one_cards, ArrayList<Card> player_two_cards) {

        System.out.println("jdsjfksjk s");

        return "turn_is_finished";
    }
}
