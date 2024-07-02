package controllers;

import java.util.*;
import java.util.stream.Stream;

import database.DBs.GameDB;
import database.DBs.UserDB;
import database.DBs.ClanDB;
import database.DBs.ClanBattleDB;
import models.*;
import models.game.Game;

public class ClanController {
    private static final int clanCreationCost = 1000;
    private static final UserDB userDB = new UserDB();
    private static final ClanDB clanDB = new ClanDB();
    private static final ClanBattleDB cbDB = new ClanBattleDB();
    private static final GameDB gameDB = new GameDB();
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
            System.out.println(e.getMessage());
            return new Response("an exception occurred while checking for double clan name",-500);
        }
        if( existingClan != null ){
            return  new Response("a clan with this name already exists",-400);
        }

        Clan clan = new Clan(user.getID(), name);
        int id = clanDB.create(clan);
        clan.setJoiningKey(name+"/"+ id);
        clan.setBattleKey(name);
        clan.addMember(user);
        clanDB.update(clan, clan.getID());

        UserDB userDB = new UserDB();
        try{
            user.setClanID(id);
            userDB.update(user, user.getID());
        }catch (Exception e){
            System.out.println(e.getMessage());
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

        if( attackerClan.getBattleID() != null ){
            return new Response("you already have a battle",-400);
        }

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

        ClanBattle battle = new ClanBattle(attackerClan.getID(), defenderClan.getID(), Math.min(attackerClan.getMembers().size(), defenderClan.getMembers().size()));
        defenderClan.startBattle(battle.getID());
        attackerClan.startBattle(battle.getID());
        clanDB.update(defenderClan, defenderClan.getID());
        clanDB.update(attackerClan, attackerClan.getID());
        cbDB.create(battle);
        return new Response("battle started successfully",0);
    }

    public static Response getBttles(int clanID){
        Clan clan = null;
        try{
            clan = clanDB.getOne(clanID);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        if( clan == null ){
            return new Response("no clans were found with this id",-400);
        }

        List<ClanBattle> battles = new ArrayList<>();
        try{
            battles = cbDB.whereEqualsOr(clanID);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        if ( battles == null ){
            return new Response("no battles were found",-400);
        }

        int numberOfWins = 0;
        int numberOfLoses = 0;
        for(ClanBattle b: battles){
            if( b.getWinnerClanID() == clanID )
                numberOfWins++;
            else if( b.getLoserClanID() == clanID )
                numberOfLoses++;
        }
        Map<String , Object> result = new HashMap<>();
        result.put("numberOfWins",numberOfWins);
        result.put("numberOfLoses",numberOfLoses);
        result.put("battles",battles);
        return new Response("battles fetched successfully",200,result);
    }


}
