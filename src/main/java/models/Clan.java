package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Clan {
    private int id;
    private String name;
    private String joiningKey;
    private String battleKey;
    private int leaderID;
    private List<Integer> membersIDS;
    private Integer battleID;

    public Clan(int id, String _name) {
        name = _name;
        battleID = null;
        leaderID = id;
        membersIDS = new ArrayList<>();
    }

    public Clan() {
    }

    public int getID() {
        return id;
    }

    public void setID(int _id) {
        id = _id;
    }

    public String getName() {
        return name;
    }

    public String getJoiningKey() {
        return joiningKey;
    }

    public String getBattleKey() {
        return battleKey;
    }

    public List<Integer> getMembersIDS() {
        return membersIDS;
    }

    public int getLeaderID() {
        return leaderID;
    }

    public Integer getBattleID() {
        return battleID;
    }

    public void setJoiningKey(String key) {
        joiningKey = key;
    }

    public void setBattleKey(String key) {
        battleKey = key;
    }

    public void addMemberID(int id) {
        membersIDS.add(id);
    }

    public void removeMembeID(int id) {
        membersIDS.remove(id);
    }

    public void startBattle(Integer _battleID) {
        battleID = _battleID;
    }

    public void endBattle() {
        battleID = null;
    }

}
