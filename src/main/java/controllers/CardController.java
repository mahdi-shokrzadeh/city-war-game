package controllers;

import java.util.List;

import models.card.*;
import models.User;
import models.Response;
import database.Database;

public class CardController {

    public static Response createRegularCard(User user, String name, int price, int duration, String type,int power,int damage, int upgradeLevel, int upgradeCost, String desc) {

        Response res;

        if (!user.getRole().equals("admin")) {
            res = new Response("only admins can add cards", -401);
            return res;
        }

        if (!CardType.includes(type)) {
            res = new Response("invalid card type", -422);
            return res;
        }
        if (name.isEmpty()) {
            res = new Response("invalid card name", -422);
            return res;
        }
        if (price < 0) {
            res = new Response("card price cannot be negative", -422);
            return res;
        }
        if (duration < 1 || duration > 5) {
            res = new Response("card duration cannot be negative", -422);
            return res;
        }
        if (desc.isEmpty()) {
            res = new Response("invalid card description", -422);
            return res;
        }
        if(damage < 10 || damage > 50){
            res = new Response("invalid card power", -422);
            return res;
        }
        if (power < 10 || power > 100) {
            res = new Response("invalid card power", -422);
            return res;
        }
        if(upgradeLevel <=0 ){
            res = new Response("card upgrade level can not be none-positive",-422);
            return res;
        }
        if(upgradeCost <=0 ){
            res = new Response("card upgrade cost can not be none-positive",-422);
            return res;
        }
        Response existingCard = getCardByName(name);
        if (existingCard.ok) {
            res = new Response("card with this name already exists", -409);
            return res;
        }

        Card card = new Card(name, price, duration, type, power, damage, upgradeLevel, upgradeCost, desc);
        Database<Card> cardDB = new Database<Card>("cards");
        cardDB.create(card);

        res = new Response("Card created successfully", 201, card);
        return res;
    }

    public static Response getCardByName(String name) {
        Card card = null;
        Response res;
        try {
            Database<Card> cardDB = new Database<Card>("cards");
            card = cardDB.firstWhereEquals("name", name);
            res = new Response("Card found", 200, card);
        } catch (Exception e) {
            e.printStackTrace();
            card = null;
            res = new Response("Card not found", -404);
        }
        return res;
    }

    public static Response removeCard(Card card) {
        Response res;

        try {
            Database<Card> cardDB = new Database<>("cards");
            cardDB.delete(card.getId());
            res = new Response("card deleted successfully", 200);
        } catch (Exception e) {
            e.printStackTrace();
            res = new Response("an error has occured while deleting card", -500);
        }

        return res;

    }

    public static Response editCard(User user, String name, int price, int duration,int power,int damage, int upgradeLevel, int upgradeCost, String desc){
        Response res;

        if (!user.getRole().equals("admin")) {
            res = new Response("only admins can add cards", -401);
            return res;
        }
        if (name.isEmpty()) {
            res = new Response("invalid card name", -422);
            return res;
        }
        if (price < 0) {
            res = new Response("card price cannot be negative", -422);
            return res;
        }
        if (duration < 1 || duration > 5) {
            res = new Response("card duration cannot be negative", -422);
            return res;
        }
        if (desc.isEmpty()) {
            res = new Response("invalid card description", -422);
            return res;
        }
        if(damage < 10 || damage > 50){
            res = new Response("invalid card power", -422);
            return res;
        }
        if (power < 10 || power > 100) {
            res = new Response("invalid card power", -422);
            return res;
        }
        if(upgradeLevel <=0 ){
            res = new Response("card upgrade level can not be none-positive",-422);
            return res;
        }
        if(upgradeCost <=0 ){
            res = new Response("card upgrade cost can not be none-positive",-422);
            return res;
        }

        Response existingCard = getCardByName(name);
        if (!existingCard.ok) {
            res = new Response("card with this does not exist exists", -409);
            return res;
        }

        Card card = new Card(name, price, duration,((Card) existingCard.body.get("card")).getCardType().toString(), power, damage, upgradeLevel, upgradeCost, desc);
        Database<Card> cardDB = new Database<Card>("cards");
        cardDB.update(card, card.getId());

        res = new Response("card updated successfully", 200);
        return res;

    }

    public static Response getAllCards(){
        Database<Card> cardDB = new Database<>("cards");
        List<Card> allCards;
        try{
            allCards = cardDB.getAll();
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching all cards",-500);
        }

        return  new Response("fetched all cards successfully",200,allCards);

    }

}
