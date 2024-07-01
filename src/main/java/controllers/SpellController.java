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
        spellDB.create(spell);

        res = new Response("Spell created successfully", 201, spell);
        return res;
    }

    public static Response getSpellByName(String name) {
        Spell spell = null;
        Response res;
        try {
            spell = spellDB.getByName(name);
            res = new Response("Spell found", 200, spell);
        } catch (Exception e) {
            e.printStackTrace();
            spell = null;
            res = new Response("Spell not found", -404);
        }
        return res;
    }

    public static Response removeSpell(Spell spell) {
        Response res;

        try {
            spellDB.delete(spell.getID());
            res = new Response("spell deleted successfully", 200);
        } catch (Exception e) {
            e.printStackTrace();
            res = new Response("an error has occurred while deleting spell", -500);
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
        spellDB.create(spell);

        return new Response("Spell edited successfully", 200);
    }

}
