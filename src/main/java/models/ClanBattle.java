package models;

import models.game.Game;

import java.util.ArrayList;
import java.util.List;

public class ClanBattle {
    private int id;
    private int attackerID;
    private int defenderID;
    private boolean hasEnded;
    private int numberOfRemainingGames;
    private List<Integer> playedAttackersIDs;
    private List<Integer> playedDefendersIDs;
    private List<Integer> gameIDS;
    private int attackerWins;
    private int defenderWins;
    private int winnerClanID;
    private int loserClanID;
    private boolean hasFinale;
    public ClanBattle(int c1, int c2, int _nrg){
        attackerID = c1;
        defenderID = c2;
        hasEnded = false;
        numberOfRemainingGames = _nrg;
        playedAttackersIDs = new ArrayList<>();
        playedDefendersIDs = new ArrayList<>();
        gameIDS = new ArrayList<>();
        attackerWins = 0;
        defenderWins = 0;
        hasFinale = false;
    }
    public ClanBattle(){}
    public int getID(){ return id; }
    public int getAttackerID(){ return attackerID; }
    public int getDefenderID(){ return defenderID; }
    public boolean hasEnded(){ return hasEnded; }
    public boolean hasFinale(){ return hasFinale; }
    public int getWinnerClanID(){ return winnerClanID; }
    public int getLoserClanID(){ return loserClanID; }
    public int getNumberOfRemainingGames(){ return  numberOfRemainingGames;}
    public boolean endBattle(){
        if( attackerWins > defenderWins ){
            winnerClanID = attackerID;
            loserClanID = defenderID;
            hasEnded = false;
        }else if( attackerWins < defenderWins ){
            winnerClanID = defenderID;
            loserClanID = attackerID;
            hasEnded = true;
        }else{
            hasFinale = true;
            hasEnded = false;
        }
        return hasFinale;
    }
    public List<Integer> getPlayedAttackersIDS(){ return playedAttackersIDs; }
    public List<Integer> getPlayedDefendersIDs(){ return playedDefendersIDs; }
    public List<Integer> getGameIDS(){ return gameIDS; }
    public void setID(int _id){ id = _id;}
    public void playAGame(Game game){
        playedAttackersIDs.add(game.getPlayer_one_id());
        playedDefendersIDs.add(game.getPlayer_two_id());
        gameIDS.add(game.getID());
        numberOfRemainingGames--;
        if( game.getWinner().equals("p1") ) attackerWins++;
        else if( game.getWinner().equals("p2") ) defenderWins++;
    }
}
