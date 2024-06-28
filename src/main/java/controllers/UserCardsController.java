package controllers;

import java.util.List;

import database.Database;
import models.User;
import models.card.Card;
import models.Response;
import models.UserCard;

import models.card.CardType;

public class UserCardsController {

    public static Response buyCard(int userID, Card card){
        Database<User> userDB = new Database<User>("users");
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
        if(!CardType.includes(card.getCardType().toString()) ){
            return  new Response("the provided card does not have a valid type",-400);
        }

        Database<UserCard> userCardsDB = new Database<>("userCards");
        UserCard userCard = new UserCard(userID, card);
        userCardsDB.create(userCard);

        return new Response("card purchased successfully",200);
    }

    public static Response getUsersCards(int userID){
        Database<UserCard> userCardDatabase = new Database<>("userCards");
        Database<User> usersDB = new Database<>("users");
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

        return new Response("all users' cards fetched successfully",200,allUsersCards);
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

        try{
            userCardDatabase.delete(card.getId());
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happend while deleting user card",-500);
        }

        return new Response("user card successfully deleted",200);


    }

    public static Response upgradeCard(int userID, Card card){
        return new Response("",0);
    }

}
