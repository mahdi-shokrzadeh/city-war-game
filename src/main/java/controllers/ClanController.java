package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Stream;

import database.Database;
import models.*;

public class ClanController {
    private static final int clanCreationCost = 1000;

    public static Response getCLanById(int id){
        Database<Clan> clanDB = new Database<>("clans");
        Clan clan;
        try {
            clan = clanDB.getOne(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception has occurred while fetching the clan",-500);
        }
        return new Response("clan fetched successfully",200,clan);
    }
    public static Response getMyClan(User user){

        if( user.getClanID() == null ){
            return new Response("you are not a member of any clan",-400);
        }

        Database<Clan> clanDB = new Database<>("clans");
        Clan myCLan;
        try{
            myCLan = clanDB.getOne(user.getClanID());
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while fetching the clan",-500);
        }
        if( myCLan == null ){
            return new Response("clan was not found",-400);
        }

        return new Response("clan fetched successfully",200,myCLan);
    }
    public static Response createClan(User user, String name){

        if( user.getCoins() < 1000 ){
            return  new Response("the user does not have enough coins to create a clan",-400);
        }

        Database<Clan> clansDB = new Database<>("clans");
        Clan existingClan;
        try {
            existingClan = clansDB.firstWhereEquals("name", name);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while checking for double clan name",-500);
        }
        if( existingClan != null ){
            return  new Response("a clan with this name already exists",-400);
        }

        Clan clan = new Clan(user.getId(), name);
        int id = clansDB.create(clan);
        clan.setJoiningKey(name+"/"+ id);
        clan.setBattleKey(name);
        clansDB.create(clan);

        Database<User> userDB = new Database<>("users");
        try{
            user.setClanID(id);
            userDB.update(user, user.getId());
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while saving user's clan id",-500);
        }

        return new Response("clan created successfully",201);
    }

    public static Response addMember(User user,String name, String key){

        Database<Clan> clanDB = new Database<>("clans");
        Clan clan;
        try{
            clan = clanDB.firstWhereEquals("name",name);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching the clan with the given name",-500);
        }

        if( clan == null ) {
            return new Response("no clan was found with the given name", -400);
        }

        if( !clan.getJoiningKey().equals(key) ){
            return new Response("invalid joining key",-401);
        }

        Database<User> userDB = new Database<>("users");
        try{
            user.setClanID(clan.getId());
            userDB.update(user, user.getId());
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while saving user's clan id",-500);
        }

        clan.addMember(user);

        try{
            clanDB.update(clan, clan.getId());
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while saving the clan",-500);
        }
        return new Response("user joined the clan successfully",200);
    }

    public static Response startBattle(User leader,Clan attackerClan, String key){

        Database<Clan> clanDB = new Database<>("clans");

        Clan defenderClan;
        try{
            defenderClan = clanDB.firstWhereEquals("battleKey",key);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching the defender clan",-500);
        }
        if( defenderClan == null ){
            return new Response("the defender clan was not found",-400);
        }

        if( attackerClan.getLeaderID() != leader.getId()){
            return new Response("only the leader can start the battle",-401);
        }

        // SAVE DEFENDER AND ATTACKER CLANS IN THE DATABASE
        Database<ClanBattle> clanBattleDB = new Database<>("clanBattles");
        ClanBattle battle = new ClanBattle(attackerClan.getId(), defenderClan.getId(), Math.min(attackerClan.getMembers().size(), defenderClan.getMembers().size()));
        clanBattleDB.create(battle);
        return new Response("battle started successfully",0);

    }

    public static Response playAGame(User user,Clan userClan){

        Database<ClanBattle> clanBattleDB = new Database<>("clanBattles");
        ClanBattle battle;
        try{
            Map<String, String> conditions = new HashMap<>();
            conditions.put("attackerID",String.valueOf(userClan.getId()));
            conditions.put("defenderID",String.valueOf(userClan.getId()));
            battle = clanBattleDB.firstWhereEqualsOr(conditions);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while fetching battle",-500);
        }
        if( battle == null ){
            return new Response("no battle was found for your clan",-400);
        }

        List<Integer> playedUserIDS = Stream.concat(battle.getPlayedAttackersIDS().stream(), battle.getPlayedDefendersIDs().stream()).toList();
        boolean userHasPlayed = false;
        for(int id: playedUserIDS) {
            if( user.getId() == id ){
                userHasPlayed = true;
                break;
            }
        }
        if( userHasPlayed ){
            return new Response("user has already played",-403);
        }

        User opponent;
        if( user.getClanID() == battle.getAttackerId() ){
            Clan defenderClan = ((Clan)getCLanById(battle.getDefenderId()).body.get("clan"));
            //List<Integer> defenderIDS = ((Clan) getCLanById(battle.getDefenderId()).body.get("clan")).getMembers();
            // reduce....
            //defenderIDS.removeAll(battle.getPlayedDefendersIDs());
        }else if( user.getClanID() == battle.getDefenderId()){

        }
        //ClanGame game = new ClanGame();

        // SAVE ALL THE CHANGED DATA
        return new Response("",0);
    }



}
