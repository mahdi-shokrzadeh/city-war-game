package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Stream;

import database.DBs.UserDB;
import database.DBs.ClanDB;
import database.DBs.ClanBattleDB;
import models.*;

public class ClanController {
    private static final int clanCreationCost = 1000;
    private static final UserDB userDB = new UserDB();
    private static final ClanDB clanDB = new ClanDB();
    private static final ClanBattleDB cbDB = new ClanBattleDB();
    public static Response getCLanById(int id){
        Clan clan;
        try {
            clan = clanDB.getOne(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception has occurred while fetching the clan",-500);
        }
        return new Response("clan fetched successfully",200,"clan",clan);
    }
    public static Response getMyClan(User user){

        if( user.getClanID() == null ){
            return new Response("you are not a member of any clan",-400);
        }

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

        return new Response("clan fetched successfully",200,"myClan",myCLan);
    }
    public static Response createClan(User user, String name){

        if( user.getCoins() < 1000 ){
            return  new Response("the user does not have enough coins to create a clan",-400);
        }

        Clan existingClan;
        try {
            existingClan = clanDB.getByName(name);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while checking for double clan name",-500);
        }
        if( existingClan != null ){
            return  new Response("a clan with this name already exists",-400);
        }

        Clan clan = new Clan(user.getID(), name);
        int id = clanDB.create(clan);
        clan.setJoiningKey(name+"/"+ id);
        clan.setBattleKey(name);
        clanDB.create(clan);

        UserDB userDB = new UserDB();
        try{
            user.setClanID(id);
            userDB.update(user, user.getID());
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while saving user's clan id",-500);
        }

        return new Response("clan created successfully",201);
    }

    public static Response addMember(User user,String name, String key){

        Clan clan;
        try{
            clan = clanDB.getByName(name);
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

        try{
            user.setClanID(clan.getID());
            userDB.update(user, user.getID());
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while saving user's clan id",-500);
        }

        clan.addMember(user);

        try{
            clanDB.update(clan, clan.getID());
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while saving the clan",-500);
        }
        return new Response("user joined the clan successfully",200);
    }

    public static Response startBattle(User leader,Clan attackerClan, String key){

        Clan defenderClan;
        try{
            defenderClan = clanDB.getByBattleKey(key);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching the defender clan",-500);
        }
        if( defenderClan == null ){
            return new Response("the defender clan was not found",-400);
        }

        if( attackerClan.getLeaderID() != leader.getID()){
            return new Response("only the leader can start the battle",-401);
        }

        // SAVE DEFENDER AND ATTACKER CLANS IN THE DATABASE
        ClanBattle battle = new ClanBattle(attackerClan.getID(), defenderClan.getID(), Math.min(attackerClan.getMembers().size(), defenderClan.getMembers().size()));
        cbDB.create(battle);
        return new Response("battle started successfully",0);

    }

    public static Response playAGame(User user,Clan userClan){

        ClanBattle battle;
        try{
            battle = cbDB.firstWhereEqualsOr(userClan.getID());
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
            if( user.getID() == id ){
                userHasPlayed = true;
                break;
            }
        }
        if( userHasPlayed ){
            return new Response("user has already played",-403);
        }

        User opponent;
        if( user.getClanID() == battle.getAttackerID() ){
            Clan defenderClan = ((Clan)getCLanById(battle.getDefenderID()).body.get("clan"));
            //List<Integer> defenderIDS = ((Clan) getCLanById(battle.getDefenderId()).body.get("clan")).getMembers();
            // reduce....
            //defenderIDS.removeAll(battle.getPlayedDefendersIDs());
        }else if( user.getClanID() == battle.getDefenderID()){

        }
        //ClanGame game = new ClanGame();

        // SAVE ALL THE CHANGED DATA
        return new Response("",0);
    }



}
