package models.card;

public class Spell extends CardStruct {

    private SpellType spellType;

    public Spell(String _name, int _price, int _duration, String _type, String _desc, SpellType _spellType) {
        super(_name, _price, _duration, _type, _desc);
        spellType = _spellType;
    }

    public SpellType getSpellType() {
        return spellType;
    }

}
