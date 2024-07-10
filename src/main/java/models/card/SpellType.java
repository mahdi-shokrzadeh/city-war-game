package models.card;

public enum SpellType {
    Shield,
    Heal,
    PowerBoost,
    SpaceShift,
    Repair,
    RoundReduce,
    Steal,
    Attenuate,
    Copy,
    Hide,
    AddSpace,
    Mirror;

    public static boolean includes(String type) {
        boolean result = false;
        for (CardType _type : CardType.values()) {
            if (_type.name().equals(type)) {
                result = true;
                break;
            }
        }
        return result;
    }

}
