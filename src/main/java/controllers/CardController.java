package controllers;

import models.card.*;
import models.User;
import models.Response;
import database.Database;

public class CardController {

    public static Response createRegularCard(User user, String name, int price, int duration, String type, String desc,
            int power,
            int damage) {

        Response res;

        if (!user.getRole().equals("admin")) {
            res = new Response("only admins can add cards", -401);
            return res;
        }

        if (!CardType.includes(type)) {
            res = new Response("invalid card type", -422);
            return res;
        }
        if (name.equals("") || name == null) {
            res = new Response("invalid card name", -422);
            return res;
        }
        if (price < 0) {
            res = new Response("card price cannot be negative", -422);
            return res;
        }
        if (duration < 0) {
            res = new Response("card duration cannot be negative", -422);
            return res;
        }
        if (desc.equals("") || desc == null) {
            res = new Response("invalid card description", -422);
            return res;
        }
        if (power < 0) {
            res = new Response("card power cannot be negative", -422);
            return res;
        }

        Response existingCard = getCardByName(name);
        if (existingCard.ok) {
            res = new Response("card with this name already exists", -409);
            return res;
        }

        Card card = new Card(name, price, duration, type, desc, power, damage);
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

    public static Response removeCard(String name) {
        Response res;

        try {
            Database<Card> cardDB = new Database<>("cards");
            // Card card = cardDb.firstWhereEquals()
            // cardDB.delete(0);
            res = new Response("card deleted successfully", 200);
        } catch (Exception e) {
            e.printStackTrace();
            res = new Response("an error has occured while deleting card", -500);
        }

        return res;

    }

}
