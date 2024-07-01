package controllers.game;

import models.User;
import database.Database;
import models.game.Game;
import models.Response;

import java.util.ResourceBundle;

public class GameController {


    public static Response createGame(int p1ID, int p2ID, String mode, String createdAt, int numberOfRounds){
        Database<Game> gameDB = new Database<>("games");
        Game game;
        try{
            //game = new Game(p1ID, p2ID, mode);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while creating game",-500);
        }
        return new Response("",0);
    }

}
