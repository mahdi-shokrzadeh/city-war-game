package controllers.game;

import database.DBs.GameDB;
import models.User;
import database.Database;
import models.game.Game;
import models.Response;

import java.util.List;
import java.util.ResourceBundle;

public class GameController {
    private static final GameDB gameDB = new GameDB();
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

    public static Response getAllUserGames(int id){
        List<Game> games = null;
        try{
            games = gameDB.getByUserID(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new Response("an exception happened while fetching users' games",-500);
        }
        if( games == null ){
            return new Response("no games were found for this user",-400);
        }
        return new Response("users' games fetched successfully",200,"games",games);
    }

}
