package controllers;

import database.Database;
import models.Character;
import models.Response;

public class CharacterController {

    public static Response createCharacter(String name){

        Database<Character> characterDB = new Database<>("characters");
        try{
            Character c = new Character(name);
            characterDB.create(c);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while creating character",-500);
        }

        return new Response("character created successfully",200);
    }

    public static Response getCharacter(String name){
        Database<Character> characterDB = new Database<>("characters");
        Character character = null;
        try{
            character = characterDB.firstWhereEquals("name",name);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exceptio occurred while getting character",-500);
        }
        return new Response("fetched character successfully",200,character);
    }

}
