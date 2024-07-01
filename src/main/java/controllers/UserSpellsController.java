package controllers;

import java.util.List;

import database.DBs.UserSpellDB;
import database.DBs.UserDB;
import models.card.Spell;
import models.UserSpell;
import models.User;
import models.Response;

import models.card.SpellType;

public class UserSpellsController {
    private static final UserSpellDB usDB =new UserSpellDB();
    private static final UserDB userDB = new UserDB();
    public static Response buySpell(int userID, Spell spell){
        User user;

        try {
            user = userDB.getOne(userID);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching user",-500);
        }
        if( user == null ){
            return new Response("could not find any user with this id",-400);
        }
        if(!SpellType.includes(spell.getSpellType().toString()) ){
            return  new Response("the provided spell does not have a valid type",-400);
        }

        UserSpell userSpell = new UserSpell(userID, spell);
        usDB.create(userSpell);

        return new Response("spell purchased successfully",200);
    }

    public static Response getUsersSpells(int userID){
        User user;
        List<UserSpell> allUsersSpells;


        try{
            user = userDB.getOne(userID);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching user",-500);
        }
        if( user == null ){
            return new Response("no user found with this id",-400);
        }

        try {
            allUsersSpells = usDB.whereEquals(userID);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching user spells",-500);
        }
        if( allUsersSpells == null ){
            return new Response("could not find users spells",-400);
        }

        return new Response("all users' spells fetched successfully",200,allUsersSpells);
    }

    public static Response removeUserSpell(int userID, Spell spell){
        User user;

        try{
            user = userDB.getOne(userID);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching user",-500);
        }
        if( user == null ){
            return new Response("no user found with this id",-400);
        }

        try{
            usDB.delete(spell.getID());
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while deleting user spell",-500);
        }

        return new Response("user spell successfully deleted",200);


    }

    public static Response upgradeSpell(int userID, Spell spell){
        return new Response("",0);
    }

}
