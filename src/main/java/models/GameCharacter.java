package models;

public class GameCharacter {
    private int id;
    private String name;
    private double pFactor;

    public GameCharacter(String _name) {
        name = _name;
    }

    public GameCharacter() {
    };

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public double getPFactor() {
        return pFactor;
    }

    public void setID(int _id) {
        id = _id;
    }

    public void setName(String _name) {
        name = _name;
    }

    public void setPFactor(double p) {
        pFactor = p;
    }
}
