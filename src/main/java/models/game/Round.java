package models.game;

import java.util.ArrayList;

import models.User;
import models.card.Card;

public class Round {
    private User player_one;
    private User player_two;
    private boolean game_is_finished = false;
    private String winner;
    private ArrayList<Turn> turns = new ArrayList<Turn>();
    private Turn current_turn;
    private User current_player;
    private int player_one_damage = 0;
    private int player_two_damage = 0;

    private ArrayList<Card> player_one_cards = new ArrayList<Card>();
    private ArrayList<Card> player_two_cards = new ArrayList<Card>();

    private Block[][] board = new Block[2][21];

    public Round(User player_one, User player_two, ArrayList<Card> player_one_cards, ArrayList<Card> player_two_cards) {
        turns.add(new Turn(player_one, player_two, player_one_cards, player_two_cards));
        this.current_turn = turns.get(0);
        this.player_one = player_one;
        this.player_two = player_two;
        this.current_player = player_one;

        // fill the board with block
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 21; j++) {
                board[i][j] = new Block();
            }
        }

    }

    public String processRound() {

        boolean con = true;
        while (con) {
            String result = current_turn.processTurn(current_player, board,
                    this);
            if (result.equals("turn_is_finished")) {
                if (this.turns.size() < 4) {
                    if (current_player == player_one) {
                        current_player = player_two;
                    } else {
                        current_player = player_one;
                    }
                    this.turns.add(new Turn(player_one, player_two, player_one_cards, player_two_cards));
                    this.current_turn = turns.get(turns.size() - 1);
                } else {
                    // round is over!
                }
            }
        }

        return "game_is_finished";
    }

    // getters and setters
    public User getPlayer_one() {
        return player_one;
    }

    public void setPlayer_one(User player_one) {
        this.player_one = player_one;
    }

    public User getPlayer_two() {
        return player_two;
    }

    public void setPlayer_two(User player_two) {
        this.player_two = player_two;
    }

    public boolean isGame_is_finished() {
        return game_is_finished;
    }

    public void setGame_is_finished(boolean game_is_finished) {
        this.game_is_finished = game_is_finished;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }

    public void setTurns(ArrayList<Turn> turns) {
        this.turns = turns;
    }

    public Turn getCurrent_turn() {
        return current_turn;
    }

    public void setCurrent_turn(Turn current_turn) {
        this.current_turn = current_turn;
    }

    public Block[][] getBoard() {
        return board;
    }

    public void setBoard(Block[][] board) {
        this.board = board;
    }

    public int getPlayerOneDamage() {
        return player_one_damage;
    }

    public void setPlayerOneDamage(int player_one_damage) {
        this.player_one_damage = player_one_damage;
    }

    public int getPlayerTwoDamage() {
        return player_two_damage;
    }

    public void setPlayerTwoDamage(int player_two_damage) {
        this.player_two_damage = player_two_damage;
    }
}
