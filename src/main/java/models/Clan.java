package models;

import java.util.List;
import java.util.Map;

public class Clan {
    private int id;
    private final String name;
    private String joiningKey;
    private String battleKey;
    private int leaderID;
    private List<User> members;
    private ClanBattle battle;
    public Clan(int id,String _name){
        name = _name;
        battle = null;
        leaderID = id;
    }
    public int getId(){ return id; }
    public void setID(int _id){ id = _id;}
    public String getName(){return name;}
    public String getJoiningKey(){ return joiningKey;}
    public String getBattleKey(){ return  battleKey;}
    public List<User> getMembers(){ return members;}
    public int getLeaderID(){ return leaderID;}
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
    public void startBattle(Clan clan){
        battle = new ClanBattle(this.getId(), clan.getId(), Math.min(this.getMembers().size(),clan.getMembers().size()));
    }
    public void endBattle(){ battle.endBattle(); battle = null; }



}
