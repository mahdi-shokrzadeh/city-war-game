package org.example.citywars;

import models.P_PlayMode;

public class M_GamePlayMenu extends Menu{
    boolean gameOver;
    public M_GamePlayMenu(){
        super("M_GamePlayMenu");
        gameOver=false;
    }
    public Menu myMethods(String input){
        if (playMode == P_PlayMode.mono) {
            //...
        } else if (playMode == P_PlayMode.duel) {
            //...
        } else if (playMode == P_PlayMode.clan) {
            //...
        } else if (playMode == P_PlayMode.gamble) {
            //...
        }

        if (gameOver) {
            //...
            return new M_GameOverMenu();
        }

        return this;
    }
}
