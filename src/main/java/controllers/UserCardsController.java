package controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import database.Database;
import models.User;
import models.card.Card;
import models.Response;
import models.UserCard;

import models.card.CardType;

public class UserCardsController {

    public static Response buyCard(int userID, Card card){
        Database<User> userDB = new Database<>("users");
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
        if( user.getCoins() < card.getPrice() ){
            return new Response("user does not have enough coins to buy this card",-400);
        }

        Database<UserCard> userCardsDB = new Database<>("userCards");
        UserCard userCard = new UserCard(userID, card.getId());
        int id = userCardsDB.create(userCard);
        user.addUserCardID(id);
        userDB.update(user, user.getId());

        return new Response("card purchased successfully",200);
    }

    public static Response getUsersCards(int userID){
        Database<UserCard> userCardDatabase = new Database<>("userCards");
        Database<User> usersDB = new Database<>("users");
        Database<Card> cardDB = new Database<>("cards");
        User user;
        List<UserCard> allUsersCards;

        try{
            user = usersDB.getOne(userID);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching user",-500);
        }
        if( user == null ){
            return new Response("no user found with this id",-400);
        }

        try {
            allUsersCards = userCardDatabase.whereEquals("userID", String.valueOf(userID));
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching user cards",-500);
        }
        if( allUsersCards == null ){
            return new Response("could not find users cards",-400);
        }

        List<Card> cards = null;

        try{
            for(UserCard userCard: allUsersCards){
                Card card = cardDB.getOne(userCard.getCardID());
                if( card == null ) { return new Response("a deep error happened while fetching one of the cards",-400); };
                cards.add(card);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Response("a deep exception happened while fetching cards",-500);
        }

        if( cards == null ){
            return new Response("no cards where found for this user",-400);
        }

        return new Response("all users' cards fetched successfully",200,cards);
    }

    public static Response removeUserCard(int userID, Card card){
        Database<UserCard> userCardDatabase = new Database<>("userCards");
        Database<User> usersDB = new Database<>("users");
        User user;

        try{
            user = usersDB.getOne(userID);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while fetching user",-500);
        }
        if( user == null ){
            return new Response("no user found with this id",-400);
        }

        UserCard userCard = null;

        try{
            Map<String, String> conditions = new HashMap<>();
            conditions.put("userID",String.valueOf(user.getId()));
            conditions.put("cardID",String.valueOf(card.getId()));
            userCard = userCardDatabase.firstWhereEquals(conditions);
            userCardDatabase.firstDeleteWhereEquals(conditions);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happend while deleting user card",-500);
        }
        if( userCard == null ){
            return new Response("no user card was found",-400);
        }

        user.removeCardID(userCard.getID());

        return new Response("user card successfully deleted",200);
    }

    public static Response upgradeCard(int userID, Card card){
        return new Response("",0);
    }

}
