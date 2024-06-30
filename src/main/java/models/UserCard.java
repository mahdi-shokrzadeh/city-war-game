package models;
import models.card.Card;
import models.card.CardType;

public class UserCard{
    private int id;
    private final int userID;
    private final int cardID;
    public UserCard(int _userID,int _cardID) {
        userID = _userID;
        cardID = _cardID;
    }
    public int getID(){ return id;}
    public int getUserID(){ return  userID; }
    public int getCardID(){ return cardID; }
    public void upgrade(){}

}
