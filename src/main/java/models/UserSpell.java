package models;
import models.card.Spell;
import models.card.SpellType;

public class UserSpell extends Spell{
    private int id;
    private final int userID;
    public UserSpell(int _userID, Spell spell) {
        super(spell.getName(), spell.getPrice(), spell.getDuration(), spell.getCardType().toString() , spell.getSpellType().toString(), spell.getUpgradeLevel(), spell.getUpgradeCost(), spell.getDesc());
        userID = _userID;
    }
    public int getUserID(){return userID;}
    public void setID(int _id){ id = _id;}

}
