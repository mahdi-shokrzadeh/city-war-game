package models.card;

public class Card extends CardStruct {

    private int power;
    private int damage;

    public Card(String _name, int _price, int _duration, String _type, String _desc, int _power, int _damage) {
        super(_name, _price, _duration, _type, _desc);
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
