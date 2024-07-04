package controllers;

import models.card.*;
import models.User;
import models.Response;
import database.DBs.SpellDB;
public class SpellController {
    private static  final SpellDB spellDB = new SpellDB();
    public static Response createSpell(User user, String name, int price, int duration, String type, String spellType, int upgradeLevel, int upgradeCost, String desc) {

        Response res;

        if (!user.getRole().equals("admin")) {
            res = new Response("only admins can add spells", -401);
            return res;
        }
        if(!CardType.includes(type)){
            res = new Response("invalid card type",-422);
            return res;
        }
        if (!SpellType.includes(type)) {
            res = new Response("invalid spell type", -422);
            return res;
        }
        if (name.isEmpty()) {
            res = new Response("invalid spell name", -422);
            return res;
        }
        if (price < 0) {
            res = new Response("spell price cannot be negative", -422);
            return res;
        }
        if (duration < 1 || duration > 5) {
            res = new Response("spell duration cannot be negative", -422);
            return res;
        }
        if (desc.isEmpty()) {
            res = new Response("invalid spell description", -422);
            return res;
        }
        if(upgradeLevel <=0 ){
            res = new Response("spell upgrade level can not be none-positive",-422);
            return res;
        }
        if(upgradeCost <=0 ){
            res = new Response("spell upgrade cost can not be none-positive",-422);
            return res;
        }
        Response existingSpell = getSpellByName(name);
        if (existingSpell.ok) {
            res = new Response("spell with this name already exists", -409);
            return res;
        }

        Spell spell = new Spell(name, price, duration, type, spellType, upgradeLevel, upgradeCost, desc);
        try {
            spellDB.create(spell);
        }catch (Exception e){
            return new Response("an exception happened while creating spell",-500,e);
        }

        res = new Response("Spell created successfully", 201,"spell", spell);
        return res;
    }

    public static Response getSpellByName(String name) {
        Spell spell = null;
        try {
            spell = spellDB.getByName(name);
        } catch (Exception e) {
            return new Response("an exception happened while fetching spell",-500,e);
        }
        if( spell == null ){
            return new Response("no spell was found with this name",-400);
        }
        return new Response("spell fetched successfully",200,"spell",spell);
    }

    public static Response removeSpell(Spell spell) {
        Response res;

        try {
            spellDB.delete(spell.getID());
            res = new Response("spell deleted successfully", 200);
        } catch (Exception e) {
            res = new Response("an error has occurred while deleting spell", -500,e);
        }

        return res;

    }

    public static Response editSpell(User user, String name, int price, int duration, String spellType, int upgradeLevel, int upgradeCost, String desc) {

        Response res;

        if (!user.getRole().equals("admin")) {
            res = new Response("only admins can add spells", -401);
            return res;
        }
        if (!SpellType.includes(spellType)) {
            res = new Response("invalid spell type", -422);
            return res;
        }
        if (name.isEmpty()) {
            res = new Response("invalid spell name", -422);
            return res;
        }
        if (price < 0) {
            res = new Response("spell price cannot be negative", -422);
            return res;
        }
        if (duration < 1 || duration > 5) {
            res = new Response("spell duration cannot be negative", -422);
            return res;
        }
        if (desc.isEmpty()) {
            res = new Response("invalid spell description", -422);
            return res;
        }
        if(upgradeLevel <=0 ){
            res = new Response("spell upgrade level can not be none-positive",-422);
            return res;
        }
        if(upgradeCost <=0 ){
            res = new Response("spell upgrade cost can not be none-positive",-422);
            return res;
        }

        Response existingSpell = getSpellByName(name);
        if (!existingSpell.ok) {
            res = new Response("spell with this does not exists", -409);
            return res;
        }

        Spell spell = new Spell(name, price, duration, ((Spell) existingSpell.body.get("spell")).getCardType().toString(), spellType, upgradeLevel, upgradeCost, desc);
        try {
            spellDB.create(spell);
        }catch (Exception e){
            return new Response("an exception happened while editing spell",-500,e);
        }

        return new Response("Spell edited successfully", 200);
    }

}
