package controllers;

import models.Response;
import models.GameCharacter;
import database.DBs.GameCharacterDB;

import java.util.List;

public class GameCharacterController {
    private static final GameCharacterDB gcDB = new GameCharacterDB();

    public static Response createGameCharacter(String name) {

        try {
            GameCharacter c = new GameCharacter(name);
            gcDB.create(c);
        } catch (Exception e) {
            return new Response("an exception happened while creating character", -500, e);
        }

        return new Response("character created successfully", 200);
    }

    public static Response getGameCharacter(String name) {
        GameCharacter character = null;
        try {
            character = gcDB.getByName(name);
        } catch (Exception e) {
            return new Response("an exceptio occurred while getting character", -500, e);
        }
        return new Response("fetched character successfully", 200, "character", character);
    }

    public static Response getAll() {
        List<GameCharacter> gcs = null;
        try {
            gcs = gcDB.getAll();
        } catch (Exception e) {
            return new Response("an exception happened while", -500, e);
        }
        return new Response("all game characters fetched successfully", 200, "gameCharacters", gcs);
    }

    public static Response getByName(String name) {
        GameCharacter gameCharacter = null;
        try {
            gameCharacter = gcDB.getByName(name);
        } catch (Exception e) {
            return new Response("an exception happened while fetching game character", -500, e);
        }
        if (gameCharacter == null) {
            return new Response("no character was found with this name", -400);
        }
        return new Response("game character fetched successfully", 200, "gameCharacter", gameCharacter);
    }

}
