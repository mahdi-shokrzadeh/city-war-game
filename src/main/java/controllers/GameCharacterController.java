package controllers;

import models.Response;
import models.GameCharacter;
import database.DBs.GameCharacterDB;

public class GameCharacterController {
    private static final GameCharacterDB gcDB = new GameCharacterDB();
    public static Response createGameCharacter(String name){

        try{
            GameCharacter c = new GameCharacter(name);
            gcDB.create(c);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while creating character",-500);
        }

        return new Response("character created successfully",200);
    }

    public static Response getGameCharacter(String name){
        GameCharacter character = null;
        try{
            character = gcDB.getByName(name);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exceptio occurred while getting character",-500);
        }
        return new Response("fetched character successfully",200,"character",character);
    }

}
