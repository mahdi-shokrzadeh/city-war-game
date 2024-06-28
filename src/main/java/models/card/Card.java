package models.card;

public class Card extends CardStruct {

    private final int power;
    private final int damage;

    public Card(String _name, int _price, int _duration, String _type, int _power, int _damage, int _upgradeLevel, int _upgradeCost, String _desc) {
        super(_name, _price, _duration, _type, _upgradeLevel, _upgradeCost, _desc);
        power = _power;
        damage = _damage;
    }

    public int getPower() {
        return power;
    }

    public int getDamage() {
        return damage;
    }

}
