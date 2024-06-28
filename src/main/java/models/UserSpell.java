package models;
import models.card.Spell;
import models.card.SpellType;

public class UserSpell extends Spell{
    private final int userID;
    public UserSpell(int _userID, Spell spell) {
        super(spell.getName(), spell.getPrice(), spell.getDuration(), spell.getCardType().toString() , spell.getSpellType(), spell.getUpgradeLevel(), spell.getUpgradeCost(), spell.getDesc());
        userID = _userID;
    }
    public int getUserID(){return userID;}
}
