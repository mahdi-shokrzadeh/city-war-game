package controllers.game;

import controllers.ClanController;
import database.DBs.GameDB;
import database.DBs.ClanBattleDB;
import database.DBs.ClanDB;
import models.Clan;
import models.ClanBattle;
import models.User;
import models.game.Game;
import models.Response;

import java.util.*;
import java.util.stream.Stream;

public class GameController {
    private static final GameDB gameDB = new GameDB();
    private static final ClanDB clanDB = new ClanDB();
    private static final ClanBattleDB cbDB = new ClanBattleDB();
    public static Response createGame(User p1, User p2, String mode, int numberOfRounds, String winner){
        Game game  = null;
        try{
            game = new Game(p1, p2, mode);
            game.setNumber_of_rounds(numberOfRounds);
            if( winner.equals("p1") ) {
                game.setWinner("p1");
            }else if( winner.equals("p2") ){
                game.setWinner("p2");
            }
            game.setEnded_at(new Date().toString());
            // set game reward
            // set game bet amount
            gameDB.create(game);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while creating game",-500);
        }
        if( game == null ){
            return new Response("unable to create game",-400,"game",game);
        }
        return new Response("game created successfully",200,"game",game);
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

    public static Response getGameEssentials(User user, Clan userClan){

        ClanBattle battle;
        Clan defenderClan = null;
        Clan attackerClan = null;
        User opponent = null;
        try{
            battle = cbDB.firstWhereEqualsOr(userClan.getID());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new Response("an exception happened while fetching battle",-500);
        }
        if( battle == null ){
            return new Response("no battle was found for your clan",-400);
        }

        attackerClan = ((Clan) ClanController.getCLanById(battle.getAttackerID()).body.get("clan"));
        defenderClan = ((Clan) ClanController.getCLanById(battle.getDefenderID()).body.get("clan"));

        if( battle.hasFinale() ){
            if( attackerClan.getLeaderID() != user.getID() ){
                return new Response("the battle is in finale phase, only leaders are allowed to play game",-401);
            }
            for(User u: defenderClan.getMembers()){
                if( u.getID() == defenderClan.getLeaderID() ){
                    opponent = u;
                }
            }

        }else {
            List<Integer> playedUserIDS = Stream.concat(battle.getPlayedAttackersIDS().stream(), battle.getPlayedDefendersIDs().stream()).toList();
            boolean userHasPlayed = false;
            for (int id : playedUserIDS) {
                if (user.getID() == id) {
                    userHasPlayed = true;
                    break;
                }
            }
            if (userHasPlayed) {
                return new Response("user has already played", -403);
            }

            Random random = new Random();
            if (user.getClanID() == battle.getAttackerID()) {
                List<User> defenders = defenderClan.getMembers();
                for (int id : battle.getPlayedDefendersIDs()) {
                    defenders.removeIf(o -> o.getID() == id);
                }
                opponent = defenders.get(random.nextInt(defenders.size()));
            } else if (user.getClanID() == battle.getDefenderID()) {
                List<User> attackers = attackerClan.getMembers();
                for (int id : battle.getPlayedAttackersIDS()) {
                    attackers.removeIf(o -> o.getID() == id);
                }
                opponent = attackers.get(random.nextInt(attackers.size()));
            }
            if (opponent == null) {
                return new Response("no opponents were found", -400);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("battle",battle);
        result.put("attackerClan",attackerClan);
        result.put("defenderClan",defenderClan);
        result.put("opponent",opponent);
        return new Response("clan game essentials fetched successfully",200,result);
    }

    public static Response creatGameClan(ClanBattle battle, Clan attackerClan, Clan defenderClan, User p1, User p2, String mode, int numberOfRounds, String winner){

        Game game= (Game)createGame(p1, p2, mode,numberOfRounds,winner).body.get("game");
        if( game == null ){
            return new Response("unable to create game",-400);
        }
        battle.playAGame(game);
        if( battle.getNumberOfRemainingGames() == 0 ){
            boolean hasFinale = battle.endBattle();
            if( !hasFinale ) {
                attackerClan.endBattle();
                defenderClan.endBattle();
            }
        }
        clanDB.update(attackerClan, attackerClan.getID());
        clanDB.update(defenderClan, defenderClan.getID());
        cbDB.update(battle,battle.getID());

        return new Response("game created successfully",200);
    }

}
