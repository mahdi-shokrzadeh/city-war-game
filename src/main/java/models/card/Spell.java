package models.card;

public class Spell extends CardStruct {

    private SpellType spellType;

    public Spell(String _name, int _price, int _duration, String _type, SpellType _spellType, int _upgradeLevel, int _upgradeCost, String _desc) {
        super(_name, _price, _duration, _type, _upgradeLevel, _upgradeCost, _desc);
        spellType = _spellType;
    }

    public SpellType getSpellType() {
        return spellType;
    }

}
