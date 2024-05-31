package models.card;

public abstract class CardStruct {

    private String name;
    private int price;
    private int duration;
    private CardType cardType;
    private String description;

    public CardStruct(String _name, int _price, int _duration, String _type, String _desc) {
        name = _name;
        price = _price;
        duration = _duration;
        cardType = CardType.valueOf(_type);
        description = _desc;
    }

    public String getName() {
        return name;
    }

    public int pirce() {
        return price;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }
}
