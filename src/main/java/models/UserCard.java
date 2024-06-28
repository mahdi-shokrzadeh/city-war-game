package models;
import models.card.Card;
import models.card.CardType;

public class UserCard extends Card{
    private final int userID;
    public int getUserID(){ return  userID;}
    public UserCard(int _userID,Card card) {
        super(card.getName(), card.getPrice(), card.getDuration(), card.getCardType().toString(), card.getPower(), card.getDamage(), card.getUpgradeLevel(), card.getUpgradeCost(), card.getDesc());
        userID = _userID;
    }

}
