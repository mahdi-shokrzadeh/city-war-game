package models.game;

import models.User;

public class Game {
    private int id;
    private int player_one_id;
    private int player_two_id;
    private String mode;
    private String created_at;
    private String ended_at;
    private String winner;
    private String reward;
    private int number_of_rounds;
    private int bet_amount;

    public Game(User player_one, User player_two, String mode) {

        switch (mode) {

            case "bet":

                break;

            case "normal":

                break;

            default:

                break;
        }

    }

    Game(User player_one, User player_two, String mode, String clan) {

    }

    // getter and setter methods

    public int getID(){ return id; }
    public void setID(int _id){ id = _id;}
    public int getPlayer_one_id() {
        return player_one_id;
    }

    public void setPlayer_one_id(int player_one_id) {
        this.player_one_id = player_one_id;
    }

    public int getPlayer_two_id() {
        return player_two_id;
    }

    public void setPlayer_two_id(int player_two_id) {
        this.player_two_id = player_two_id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEnded_at() {
        return ended_at;
    }

    public void setEnded_at(String ended_at) {
        this.ended_at = ended_at;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public int getNumber_of_rounds() {
        return number_of_rounds;
    }

    public void setNumber_of_rounds(int number_of_rounds) {
        this.number_of_rounds = number_of_rounds;
    }

    public int getBet_amount() {
        return bet_amount;
    }

    public void setBet_amount(int bet_amount) {
        this.bet_amount = bet_amount;
    }

}