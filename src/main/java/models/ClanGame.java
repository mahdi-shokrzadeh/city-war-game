package models;

public class ClanGame {
    private int id;
    private final User attacker;
    private final User defender;
    private User winner;
    private User loser;
    private boolean hasEnded;
    public ClanGame(User u1, User u2){
        attacker = u1;
        defender = u2;
        winner = null;
        loser = null;
        hasEnded = false;
    }
    public int getId(){ return id; }
    public User getAttacker(){ return attacker; }
    public User getDefender(){ return defender; }
    public User getWinner(){ return winner; }
    public User getLoser(){ return loser; }
    public boolean getHasEnded(){ return hasEnded; }
    public  void endGame(User u1, User u2){
        winner = u1;
        loser = u2;
        hasEnded = true;
    }



}
