package models.game;

import java.util.ArrayList;

import models.User;

public class Round {
    private User player_one;
    private User player_two;
    private boolean game_is_finished = false;
    private String winner;
    
    private ArrayList<Turn> turns = new ArrayList<Turn>();

    public Round(User player_one, User player_two) {
        turns.add(new Turn(player_one, player_two));
        this.player_one = player_one;
        this.player_two = player_two;

    }



    public String processRound(){
        
        return "game_is_finished";
    }
}
