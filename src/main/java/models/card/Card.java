package models.card;

import models.GameCharacter;

public class Card extends CardStruct {

    private int power;
    private int damage;
    private GameCharacter character;
    private int level;
    private SpellType spellType;
    private String imageURL;

    public Card() {
    }

    public Card(String _name, int _price, int _duration, String _type, int _power, int _damage, int _upgradeLevel,
            int _upgradeCost, String _desc, GameCharacter _character) {
        super(_name, _price, _duration, _type, _upgradeLevel, _upgradeCost, _desc);
        power = _power;
        damage = _damage;
        character = _character;
        level = 1;
    }

    public Card(String _name, int _price, int _duration, String _type, int _power, int _damage, int _upgradeLevel,
            int _upgradeCost, String _desc, GameCharacter _character, String img) {
        super(_name, _price, _duration, _type, _upgradeLevel, _upgradeCost, _desc);
        power = _power;
        damage = _damage;
        character = _character;
        level = 1;
        imageURL = img;
    }

    public int getPower() {
        return power;
    }

    public int getDamage() {
        return damage;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int lvl) {
        level = lvl;
    }

    public void applyLevel() {
        damage += getDuration() * (level - 1);
        power += 3 * (level - 1);
    }

    public SpellType getSpellType() {
        return spellType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURl(String s) {
        imageURL = s;
    }

}
