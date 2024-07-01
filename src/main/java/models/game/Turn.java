package models.game;

import models.User;

public class Turn {
    private User player_one;
    private User player_two;

    public Turn(User player_one, User player_two) {
        this.player_one = player_one;
        this.player_two = player_two;
    }
}
