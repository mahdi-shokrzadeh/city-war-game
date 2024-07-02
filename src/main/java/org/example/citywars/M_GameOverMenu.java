package org.example.citywars;

public class M_GameOverMenu extends Menu{
    public M_GameOverMenu(){
        super("M_GameOverMenu");

        //game over methods
    }
    public Menu myMethods(){
        String input = consoleScanner.nextLine();
        if (input.toLowerCase().matches("^ *-back +to +main +menu *$"))
            return new M_GameMainMenu();

        System.out.println("invalid command!");
        return this;    }
}
