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

    public static Response getCLanById(int id) {
        Clan clan;
        try {
            clan = clanDB.getOne(id);
        } catch (Exception e) {
            return new Response("an exception has occurred while fetching the clan", -500, e);
        }
        return new Response("clan fetched successfully", 200, "clan", clan);
    }

    public static Response getMyClan(User user) {

        if (user.getClanID() == null) {
            return new Response("you are not a member of any clan", -400);
        }

        Clan myCLan;
        try {
            myCLan = clanDB.getOne(user.getClanID());
        } catch (Exception e) {
            return new Response("an exception happened while fetching the clan", -500, e);
        }
        if (myCLan == null) {
            return new Response("your not in any clan", 200, "myClan", null);
        }

        return new Response("clan fetched successfully", 200, "myClan", myCLan);
    }

    public static Response getAllClans() {
        List<Clan> clans = null;
        try {
            clans = clanDB.getAll();
        } catch (Exception e) {
            return new Response("an exception happened while fetching clans", -500, e);
        }
        return new Response("all clans fetched successfully", 200, "clans", clans);
    }

    public static Response createClan(User user, String name) {

        if (user.getCoins() < 1000) {
            return new Response("the user does not have enough coins to create a clan", -400);
        }

        if (user.getClanID() == null) {
            return new Response("you are already in a clan", -400);
        }

        Clan existingClan;
        try {
            existingClan = clanDB.getByName(name);
        } catch (Exception e) {
            return new Response("an exception occurred while checking for double clan name", -500, e);
        }
        if (existingClan != null) {
            return new Response("a clan with this name already exists", -400);
        }

        Clan clan = new Clan(user.getID(), name);
        int id;
        try {
            id = clanDB.create(clan);
            clan.setJoiningKey(name + "/" + id);
            clan.setBattleKey(name);
            clan.addMember(user);
            clanDB.update(clan, clan.getID());
        } catch (Exception e) {
            return new Response("an exception happened while creating clan", -500, e);
        }

        UserDB userDB = new UserDB();
        try {
            user.setClanID(id);
            userDB.update(user, user.getID());
        } catch (Exception e) {
            return new Response("an exception happened while saving user's clan id", -500, e);
        }

        return new Response("clan created successfully", 201);
    }

    public static Response addMember(User user, String key) {

        if (user.getClanID() != null) {
            return new Response("you are already in a clan", -400);
        }

        Clan clan;
        try {
            clan = clanDB.getByJoiningKey(key);
        } catch (Exception e) {
            return new Response("an exception occurred while fetching the clan with the given name", -500, e);
        }

        if (clan == null) {
            return new Response("no clan was found with the given name", -404);
        }

        if (!clan.getJoiningKey().equals(key)) {
            return new Response("invalid joining key", -401);
        }

        try {
            user.setClanID(clan.getID());
            userDB.update(user, user.getID());
        } catch (Exception e) {
            return new Response("an exception happened while saving user's clan id", -500, e);
        }

        clan.addMember(user);

        try {
            clanDB.update(clan, clan.getID());
        } catch (Exception e) {
            return new Response("an exception happened while saving the clan", -500, e);
        }
        return new Response("user joined the clan successfully", 200);
    }

    public static Response startBattle(User leader, Clan attackerClan, String key) {

        if (attackerClan.getBattleID() != null) {
            return new Response("you already have a battle", -400);
        }

        Clan defenderClan;
        try {
            defenderClan = clanDB.getByBattleKey(key);
        } catch (Exception e) {
            return new Response("an exception occurred while fetching the defender clan", -500, e);
        }
        if (defenderClan == null) {
            return new Response("the defender clan was not found", -400);
        }

        if (attackerClan.getLeaderID() != leader.getID()) {
            return new Response("only the leader can start the battle", -401);
        }

        ClanBattle battle = new ClanBattle(attackerClan.getID(), defenderClan.getID(),
                Math.min(attackerClan.getMembers().size(), defenderClan.getMembers().size()));
        defenderClan.startBattle(battle.getID());
        attackerClan.startBattle(battle.getID());
        try {
            clanDB.update(defenderClan, defenderClan.getID());
            clanDB.update(attackerClan, attackerClan.getID());
            cbDB.create(battle);
        } catch (Exception e) {
            return new Response("an exception happened while creating battle", -500, e);
        }

        return new Response("battle started successfully", 200);
    }

    public static Response getBattles(int clanID) {
        Clan clan = null;
        try {
            clan = clanDB.getOne(clanID);
        } catch (Exception e) {
            return new Response("an exception happened while fetching clan", -500, e);
        }
        if (clan == null) {
            return new Response("no clans were found with this id", -400);
        }

        List<ClanBattle> battles = new ArrayList<>();
        try {
            battles = cbDB.whereEqualsOr(clanID);
        } catch (Exception e) {
            return new Response("an exception happened while fetching battles", -500, e);
        }
        if (battles == null) {
            return new Response("no battles were found", -400);
        }

        int numberOfWins = 0;
        int numberOfLoses = 0;
        for (ClanBattle b : battles) {
            if (b.getWinnerClanID() == clanID)
                numberOfWins++;
            else if (b.getLoserClanID() == clanID)
                numberOfLoses++;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("numberOfWins", numberOfWins);
        result.put("numberOfLoses", numberOfLoses);
        result.put("battles", battles);
        return new Response("battles fetched successfully", 200, result);
    }

    public static Response getShouldPlayed(User user) {
        Clan clan = null;
        try {
            clan = clanDB.getOne(user.getID());
        } catch (Exception e) {
            return new Response("an exception happened while fetching clan", -500, e);
        }
        if (clan == null) {
            return new Response("you are not in any clan", 200, "shouldPlay", false);
        }

        ClanBattle battle = null;
        if (clan.getBattleID() == null) {
            return new Response("your clan is not in a battle", 200, "shouldPlay", false);
        }
        try {
            battle = cbDB.getOne(clan.getBattleID());
        } catch (Exception e) {
            return new Response("an exception happened while fetching battle", -500, e);
        }
        if (battle == null) {
            return new Response("no battle is currently going on", 200, "shouldPlay", false);
        }
        boolean userShouldPlay = true;
        if (battle.getPlayedAttackersIDS().contains(user.getID())) {
            userShouldPlay = false;
        } else if (battle.getPlayedDefendersIDS().contains(user.getID())) {
            userShouldPlay = false;
        }
        return new Response("result fetched successfully", 200, "shouldPlay", userShouldPlay);
    }

}
