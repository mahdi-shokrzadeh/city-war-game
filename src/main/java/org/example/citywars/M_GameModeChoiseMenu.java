package org.example.citywars;

import models.P_PlayMode;

public class M_GameModeChoiseMenu extends Menu{
    public M_GameModeChoiseMenu(){
        super("M_GameModeChoiseMenu");
    }
    public Menu myMethods(){
        String input = consoleScanner.nextLine();
        if (input.toLowerCase().matches("^ *-clan *$")){
            playMode = P_PlayMode.clan;
            return new M_ClanMainMenu();
        }
        else if (input.toLowerCase().matches("^ *-mono *$")) {
            playMode = P_PlayMode.mono;
            return new M_LoginMenu();
        }
        else if (input.toLowerCase().matches("^ *-duel *$")) {
            playMode = P_PlayMode.duel;
            return new M_LoginMenu();
        }
        else if (input.toLowerCase().matches("^ *-gamble *$")) {
            playMode = P_PlayMode.gamble;
            return new M_LoginMenu();
        }

        System.out.println("invalid command!");
        return this;
    }
}
