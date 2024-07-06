package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import models.User;
import models.card.Card;
import models.Response;
import models.UserCard;
import database.DBs.UserDB;
import database.DBs.UserCardDB;
import database.DBs.CardDB;

public class UserCardsController {
    private static final UserDB userDB = new UserDB();
    private static final UserCardDB ucDB = new UserCardDB();
    private static final CardDB cardDB = new CardDB();

    public static Response buyCard(int userID, Card card) {
        User user;
        try {
            user = userDB.getOne(userID);
        } catch (Exception e) {
            return new Response("an exception occurred while fetching user", -500, e);
        }
        if (user == null) {
            return new Response("could not find any user with this id", -400);
        }
        if (user.getCoins() < card.getPrice()) {
            return new Response("user does not have enough coins to buy this card", -400);
        }

        UserCard userCard = new UserCard(userID, card.getID());
        int id;
        try {
            id = ucDB.create(userCard);
        } catch (Exception e) {
            return new Response("an exception happened while creating user card", -500, e);
        }
        user.addUserCardID(id);
        try {
            userDB.update(user, user.getID());
        } catch (Exception e) {
            return new Response("ane xception happened while updating user", -500, e);
        }

        return new Response("card purchased successfully", 200);
    }

    public static Response getUserCard(int userID, int cardID) {
        UserCard userCard = null;
        try {
            userCard = ucDB.firstWhereEquals(userID, cardID);
        } catch (Exception e) {
            return new Response("an exception happened while fetching user card", -500, e);
        }
        if (userCard == null) {
            return new Response("no user card was found", -400);
        }
        return new Response("user card fetched successfully", 200, "userCard", userCard);
    }

    public static Response getUsersCards(User user) {
        List<UserCard> allUsersCards;

        try {
            allUsersCards = ucDB.whereEquals(user.getID());
        } catch (Exception e) {
            return new Response("an exception occurred while fetching user cards", -500, e);
        }
        if (allUsersCards == null) {
            return new Response("could not find users cards", -400);
        }

        List<Card> cards = new ArrayList<>();

        try {
            for (UserCard userCard : allUsersCards) {
                Card card = cardDB.getOne(userCard.getCardID());
                if (card == null) {
                    return new Response("a deep error happened while fetching one of the cards", -400);
                }
                ;
                card.setLevel(userCard.getLevel());
                card.applyLevel();
                cards.add(card);
            }
        } catch (Exception e) {
            return new Response("a deep exception happened while fetching cards", -500, e);
        }

        return new Response("all users' cards fetched successfully", 200, "cards", cards);
    }

    public static Response getUnleveledCards(User user) {
        List<UserCard> allUsersCards;

        try {
            allUsersCards = ucDB.whereEquals(user.getID());
        } catch (Exception e) {
            return new Response("an exception occurred while fetching user cards", -500, e);
        }
        if (allUsersCards == null) {
            return new Response("could not find users cards", -400);
        }

        List<Card> cards = new ArrayList<>();

        try {
            for (UserCard userCard : allUsersCards) {
                Card card = cardDB.getOne(userCard.getCardID());
                if (card == null) {
                    return new Response("a deep error happened while fetching one of the cards", -400);
                }
                cards.add(card);
            }
        } catch (Exception e) {
            return new Response("a deep exception happened while fetching cards", -500, e);
        }

        return new Response("all users' cards fetched successfully", 200, "cards", cards);
    }

    public static Response removeUserCard(int userID, Card card) {
        User user;
        try {
            user = userDB.getOne(userID);
        } catch (Exception e) {
            return new Response("an exception occurred while fetching user", -500, e);
        }
        if (user == null) {
            return new Response("no user found with this id", -400);
        }

        UserCard userCard = null;

        try {
            userCard = ucDB.firstWhereEquals(user.getID(), card.getID());
        } catch (Exception e) {
            return new Response("an exception happened while deleting user card", -500, e);
        }
        if (userCard == null) {
            return new Response("no user card was found", -400);
        }

        user.removeCardID(userCard.getID());

        return new Response("user card successfully deleted", 200);
    }

    public static Response upgradeCard(User user, Card card) {
        UserCard userCard = null;

        if (card.getCardType().toString().equals("Spell")) {
            return new Response("only regular cards can be upgraded", -400);
        }

        try {
            userCard = ucDB.firstWhereEquals(user.getID(), card.getID());
        } catch (Exception e) {
            return new Response("an exception happened while finding card", -500, e);
        }
        if (userCard == null) {
            return new Response("the user does not own this card", -401);
        }

        if (user.getCoins() < userCard.getLevel() * card.getUpgradeCost()) {
            return new Response("you does not have enough coins to upgrade this card", -400);
        }
        if (user.getLevel() < card.getUpgradeLevel()) {
            return new Response("you haven't reached the required level to upgrade this card", -400);
        }
        if (userCard.getLevel() == 3) {
            return new Response("maximum card level reached", -400);
        }
        if (userCard.getLevel() < 3) {
            userCard.setLevel(userCard.getLevel() + 1);
        }

        try {
            ucDB.update(userCard, userCard.getID());
        } catch (Exception e) {
            return new Response("an exception happened while saving user card", -500, e);
        }

        return new Response("card upgraded successfully", 200);
    }

}
