package models;
import models.card.Card;
import models.card.CardType;

public class UserCard{
    private int id;
    private int userID;
    private int cardID;
    private int experience;
    private int level;
    public UserCard(int _userID,int _cardID) {
        userID = _userID;
        cardID = _cardID;
        experience = 0;
        level = 1;
    }
    public UserCard(){}
    public int getID(){ return id;}
    public void setID(int _id){ id = _id;}

    public int getUserID(){ return  userID; }
    public int getCardID(){ return cardID; }
    public int getExperience(){ return experience; }
    public void setExperience(int xp){ experience = xp; }
    public int getLevel(){ return level; }
    public void setLevel(int lvl){ level = lvl;}
}
