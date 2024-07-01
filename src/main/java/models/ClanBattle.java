package models;

import java.util.ArrayList;
import java.util.List;

public class ClanBattle {
    private int id;
    private final int attackerID;
    private final int defenderID;
    private boolean hasEnded;
    private int numberOfRemainingGames;
    private List<Integer> playedAttackersIDs;
    private List<Integer> playedDefendersIDs;
    private List<Integer> gameIDS;
    public ClanBattle(int c1, int c2, int _nrg){
        attackerID = c1;
        defenderID = c2;
        hasEnded = false;
        numberOfRemainingGames = _nrg;
        playedAttackersIDs = new ArrayList<>();
        playedDefendersIDs = new ArrayList<>();
        gameIDS = new ArrayList<>();
    }
    public int getID(){ return id; }
    public int getAttackerId(){ return attackerID; }
    public int getDefenderId(){ return defenderID; }
    public boolean getStatus(){ return hasEnded; }
    public int getNumberOfRemainingGames(){ return  numberOfRemainingGames;}
    public void endBattle(){ hasEnded = true; }
    public List<Integer> getPlayedAttackersIDS(){ return playedAttackersIDs; }
    public List<Integer> getPlayedDefendersIDs(){ return playedDefendersIDs; }
    public List<Integer> getGameIDS(){ return gameIDS; }
    public void playAGame(int attackerID, int defenderID,int gameID){
        playedAttackersIDs.add(attackerID);
        playedDefendersIDs.add(defenderID);
        gameIDS.add(gameID);
    } // when two players of two teams play a game against each other
}
