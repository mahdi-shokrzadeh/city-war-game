package models.card;

import models.GameCharacter;

public class Spell extends Card {

    private SpellType spellType;

    public Spell(String _name, int _price, int _duration, String _type, String _spellType, int _upgradeLevel,
            int _upgradeCost, String _desc) {
        super(_name, _price, _duration, _type, _upgradeLevel, _upgradeCost, _upgradeCost, _upgradeCost, _desc,
                new GameCharacter("char"));
        spellType = SpellType.valueOf(_spellType);
    }

    public SpellType getSpellType() {
        return spellType;
    }

}
