package models.card;

public abstract class CardStruct {

    private int id;
    private String name;
    private int price;
    private int duration;
    private CardType cardType;
    private int upgradeLevel;
    private int upgradeCost;
    private String desc;



    public CardStruct(String _name, int _price, int _duration, String _type, int _upgradeLevel, int _upgradeCost, String _desc) {
        name = _name;
        price = _price;
        duration = _duration;
        cardType = CardType.valueOf(_type);
        upgradeLevel = _upgradeLevel;
        upgradeCost = _upgradeCost;
        desc = _desc;
    }

    public CardStruct(){}

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public CardType getCardType() {
        return cardType;
    }

    public int getUpgradeLevel(){ return upgradeLevel;}

    public int getUpgradeCost(){ return upgradeCost;}

    public int getDuration() {
        return duration;
    }

    public int getID() {
        return id;
    }
    public void setID(int _id){ id = _id;}

    public String getDesc(){return desc;}

}
