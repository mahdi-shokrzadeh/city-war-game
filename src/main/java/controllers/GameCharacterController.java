package controllers;

import database.Database;
import models.Response;
import models.GameCharacter;

public class GameCharacterController {

    public static Response createGameCharacter(String name){

        Database<GameCharacter> gameCDB = new Database<>("gameCharacters");
        try{
            GameCharacter c = new GameCharacter(name);
            gameCDB.create(c);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while creating character",-500);
        }

        return new Response("character created successfully",200);
    }

    public static Response getGameCharacter(String name){
        Database<GameCharacter> gameCDB = new Database<>("gameCharacters");
        GameCharacter character = null;
        try{
            character = gameCDB.firstWhereEquals("name",name);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exceptio occurred while getting character",-500);
        }
        return new Response("fetched character successfully",200,character);
    }

}
