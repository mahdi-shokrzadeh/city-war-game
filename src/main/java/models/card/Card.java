package models.card;

import models.GameCharacter;

public class Card extends CardStruct {

    private int power;
    private int damage;
    private final GameCharacter character;

    public Card(String _name, int _price, int _duration, String _type, int _power, int _damage, int _upgradeLevel,
            int _upgradeCost, String _desc, GameCharacter _character) {
        super(_name, _price, _duration, _type, _upgradeLevel, _upgradeCost, _desc);
        power = _power;
        damage = _damage;
        character = _character;
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

}
