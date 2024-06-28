package controllers;

import models.card.*;
import models.User;
import models.Response;
import database.Database;

public class SpellController {

    public static Response createSpell(User user, String name, int price, int duration, String type, SpellType spellType, int upgradeLevel, int upgradeCost, String desc) {

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
        Database<Spell> spellDB = new Database<Spell>("spells");
        spellDB.create(spell);

        res = new Response("Spell created successfully", 201, spell);
        return res;
    }

    public static Response getSpellByName(String name) {
        Spell spell = null;
        Response res;
        try {
            Database<Spell> spellDB = new Database<Spell>("spells");
            spell = spellDB.firstWhereEquals("name", name);
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
            Database<Spell> spellDB = new Database<>("spells");
            spellDB.delete(spell.getId());
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

        //Spell spell = new Spell(name, price, duration, type, spellType, upgradeLevel, upgradeCost, desc);
        //Database<Spell> spellDB = new Database<Spell>("spells");
        //spellDB.create(spell);

        //res = new Response("Spell created successfully", 201, spell);
        return new Response("",0);
    }

}
