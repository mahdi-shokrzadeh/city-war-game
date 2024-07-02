package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Clan {
    private int id;
    private final String name;
    private String joiningKey;
    private String battleKey;
    private final int leaderID;
    private List<User> members;
    private Integer battleID;
    public Clan(int id,String _name){
        name = _name;
        battleID = null;
        leaderID = id;
        members = new ArrayList<>();
    }
    public int getID(){ return id; }
    public void setID(int _id){ id = _id;}
    public String getName(){return name;}
    public String getJoiningKey(){ return joiningKey;}
    public String getBattleKey(){ return  battleKey;}
    public List<User> getMembers(){ return members;}
    public int getLeaderID(){ return leaderID;}
    public Integer getBattleID(){ return battleID; }
    public void setJoiningKey(String key){ joiningKey = key; }
    public void setBattleKey(String key){
        battleKey = key;
    }
    public void addMember(User user){
        members.add(user);
    }
    public void removeMember(User user){
        members.remove(user);
    }
    public void startBattle(Integer _battleID){
        battleID = _battleID;
    }
    public void endBattle(){
        battleID = null;
    }

}
