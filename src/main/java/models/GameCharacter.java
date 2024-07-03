package models;

public class GameCharacter {
    private int id;
    private String name;
    public GameCharacter(String _name){
        name = _name;
    }
    public GameCharacter(){};
    public String getName(){ return name;}
    public int getID(){ return id; }
    public void setID(int _id){ id = _id;}
    public void setName(String _name){ name = _name; }

}
