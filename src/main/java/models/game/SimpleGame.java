package models.game;

public class SimpleGame {
    private int id;
    private int playerOneID;
    private int playerTwoID;
    private String mode;
    private String createdAt;
    private String winner;
    private int numberOfRounds;
    private String winnerReward;
    private String looserReward;
    private int betAmount;

    public SimpleGame() {

    }

    public SimpleGame(Game game) {
        playerOneID = game.getPlayer_one_id();
        playerTwoID = game.getPlayer_two_id();
        mode = game.getMode();
        createdAt = game.getCreated_at();
        winner = game.getWinner();
        numberOfRounds = game.getNumber_of_rounds();
        winnerReward = game.getWinnerReward();
        looserReward = game.getLoserReward();
        betAmount = game.getBet_amount();
    }

    public int getID() {
        return id;
    }

    public int getPlayerOneID() {
        return playerOneID;
    }

    public int getPlayerTwoID() {
        return playerTwoID;
    }

    public String getMode() {
        return mode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getWinner() {
        return winner;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public String getWinnerReward() {
        return winnerReward;
    }

    public String getLoserReward() {
        return looserReward;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setID(int _id) {
        id = _id;
    }

    public void setPlayerOneID(int _id) {
        id = _id;
    }

    public void setPlayerTwoID(int _id) {
        id = _id;
    }

    public void setMode(String _mode) {
        mode = _mode;
    }

    public void setCreatedAt(String time) {
        createdAt = time;
    }

    public void setWinner(String s) {
        winner = s;
    }

    public void setNumberOfRounds(int n) {
        numberOfRounds = n;
    }

    public void winnerReward(String reward) {
        winnerReward = reward;
    }

    public void setLoserReward(String reward) {
        looserReward = reward;
    }

    public void setBetAmount(int n) {
        betAmount = n;
    }

}
