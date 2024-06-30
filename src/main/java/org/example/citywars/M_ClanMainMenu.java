package org.example.citywars;

public class M_ClanMainMenu extends Menu{
    public M_ClanMainMenu(){
        super("M_ClanMainMenu");
    }
    public Menu myMethods(String input){
        if (input.toLowerCase().matches("^ *-my +clan *$"))
            return new M_MyClanMenu();
        else if (input.toLowerCase().matches("^ *-join +clan *$"))
            return new M_JoinClanMenu();
        else if (input.toLowerCase().matches("^ *-creat +a +clan *$"))
            return new M_CreatClanMenu();

        System.out.println("invalid command!");
        return this;
    }
}
