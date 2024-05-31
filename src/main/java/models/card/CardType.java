package models.card;

public enum CardType {
    Spell,
    Regular;

    public static boolean includes(String type) {
        boolean result = false;
        for (CardType _type : CardType.values()) {
            if (_type.name().equals(type)) {
                result = true;
            }
        }
        return result;
    }

}
