package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import database.DBs.CardDB;
import database.DBs.GameCharacterDB;
import models.GameCharacter;
import models.card.*;
import models.User;
import models.Response;
import database.DBs.UserDB;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CardController {
    private static final UserDB userDB = new UserDB();
    private static final GameCharacterDB gcDB = new GameCharacterDB();
    private static final CardDB cardDB = new CardDB();

    public static Response createCard(User user, String name, int price, int duration, String type, int power,
            int damage, int upgradeLevel, int upgradeCost, String desc, String characterName) {
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
        if (damage < 10 || damage > 50) {
            res = new Response("invalid card power", -422);
            return res;
        }
        if (power < 10 || power > 100) {
            res = new Response("invalid card power", -422);
            return res;
        }
        if (upgradeLevel <= 0) {
            res = new Response("card upgrade level can not be none-positive", -422);
            return res;
        }
        if (upgradeCost <= 0) {
            res = new Response("card upgrade cost can not be none-positive", -422);
            return res;
        }
        Response existingCard = getCardByName(name);
        if (existingCard.ok) {
            res = new Response("card with this name already exists", -409);
            return res;
        }
        if (characterName.isBlank()) {
            return new Response("character name can not be blank", -422);
        }

        GameCharacter gameCharacter;
        try {
            gameCharacter = gcDB.getByName(characterName);
        } catch (Exception e) {
            return new Response("a deep exception happened while fetching the character", -500, e);
        }
        if (gameCharacter == null) {
            return new Response("no character was found with this name", -400);
        }

        Card card = new Card(name, price, duration, type, power, damage, upgradeLevel, upgradeCost, "desc",
                gameCharacter);
        try {
            cardDB.create(card);
        } catch (Exception e) {
            return new Response("an exception happened while saving card", -500, e);
        }

        res = new Response("Card created successfully", 201, "card", card);
        return res;
    }

    public static Response createCard(User user, String name, int price, int duration, String type, int power,
            int damage, int upgradeLevel, int upgradeCost, String desc, String characterName, String imageURL) {
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
        if (damage < 10 || damage > 50) {
            res = new Response("invalid card power", -422);
            return res;
        }
        if (power < 10 || power > 100) {
            res = new Response("invalid card power", -422);
            return res;
        }
        if (upgradeLevel <= 0) {
            res = new Response("card upgrade level can not be none-positive", -422);
            return res;
        }
        if (upgradeCost <= 0) {
            res = new Response("card upgrade cost can not be none-positive", -422);
            return res;
        }
        Response existingCard = getCardByName(name);
        if (existingCard.ok) {
            res = new Response("card with this name already exists", -409);
            return res;
        }
        if (characterName.isBlank()) {
            return new Response("character name can not be blank", -422);
        }

        GameCharacter gameCharacter;
        try {
            gameCharacter = gcDB.getByName(characterName);
        } catch (Exception e) {
            return new Response("a deep exception happened while fetching the character", -500, e);
        }
        if (gameCharacter == null) {
            return new Response("no character was found with this name", -400);
        }

        Card card = new Card(name, price, duration, type, power, damage, upgradeLevel, upgradeCost, "desc",
                gameCharacter);
        try {
            cardDB.create(card);
        } catch (Exception e) {
            return new Response("an exception happened while saving card", -500, e);
        }

        res = new Response("Card created successfully", 201, "card", card);
        return res;
    }

    public static Response getCardByName(String name) {
        Card card = null;
        try {
            card = cardDB.getByName(name);
        } catch (Exception e) {
            return new Response("an exception happened while fetching card", -500, e);
        }
        if (card == null) {
            return new Response("no card with this name was found", -400);
        }
        return new Response("card fetched successfully", 200, "card", card);
    }

    public static Response removeCard(User user, Card card) {
        Response res;

        if (!user.getRole().equals("admin")) {
            return new Response("only admins can remove cards", -401);
        }

        try {
            cardDB.delete(card.getID());
            res = new Response("card deleted successfully", 200);
        } catch (Exception e) {
            res = new Response("an error has occured while deleting card", -500, e);
        }

        return res;

    }

    public static Response editCard(User user, int cardID, String name, int price, int duration, int power, int damage,
            int upgradeLevel, int upgradeCost, String desc, String characterName) {
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
        if (damage < 10 || damage > 50) {
            res = new Response("invalid card power", -422);
            return res;
        }
        if (power < 10 || power > 100) {
            res = new Response("invalid card power", -422);
            return res;
        }
        if (upgradeLevel <= 0) {
            res = new Response("card upgrade level can not be none-positive", -422);
            return res;
        }
        if (upgradeCost <= 0) {
            res = new Response("card upgrade cost can not be none-positive", -422);
            return res;
        }

        GameCharacter gameCharacter = null;
        try {
            gameCharacter = gcDB.getByName(characterName);
        } catch (Exception e) {
            return new Response("a deep exception happened while fetching the character", -500, e);
        }
        if (gameCharacter == null) {
            return new Response("no character was found with this name", -400);
        }

        Card existingCard = cardDB.getOne(cardID);
        if (existingCard == null) {
            res = new Response("card with this does name not exist exists", -409);
            return res;
        }

        Card card = new Card(name, price, duration, existingCard.getCardType().toString(), power, damage, upgradeLevel,
                upgradeCost, desc, gameCharacter);
        try {
            cardDB.update(card, card.getID());
        } catch (Exception e) {
            return new Response("an exception happened while updating card", -500, e);
        }

        res = new Response("card updated successfully", 200);
        return res;

    }

    public static Response getAllCards() {
        List<Card> allCards;
        try {
            allCards = cardDB.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("an exception occurred while fetching all cards", -500);
        }

        return new Response("fetched all cards successfully", 200, "allCards", allCards);

    }

    public static Response getCardImage(Card card, int level) {
        if (card.getCardType().equals("Spell")) {
            return new Response("", 200);
        }
        File file = new File(
                "src/main/resources/Cards/" + card.getCharacter().getName() + "_d" + card.getDuration() + "_l" + level
                        + ".png");
        if (!file.isFile()) {
            return new Response("image not found", -404);
        }

        Image image = null;
        ImageView imageView = null;
        try {
            image = new Image(new FileInputStream(file));
            imageView = new ImageView(image);
            imageView.setFitWidth(209);
            imageView.setFitHeight(278.7);
        } catch (Exception e) {
            return new Response("an exception happened while loading image", -500, e);
        }

        Text power = new Text(String.valueOf(card.getPower()));
        Text damage = new Text(String.valueOf(card.getDamage()));
        power.setFill(Color.WHITE);
        power.setFont(Font.font(36));
        power.setTranslateX(-200);
        power.setTranslateY(-221);
        damage.setFill(Color.WHITE);
        damage.setFont(Font.font(22));
        damage.setTranslateX(-119);
        damage.setTranslateY(-35);
        TextFlow textFlow = new TextFlow(imageView, damage, power);
        textFlow.setTranslateX(150);

        return new Response("card image generated successfully", 200, "textFlow", textFlow);
    }

}
