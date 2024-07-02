package controllers;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import models.Response;
import models.User;
import models.UserCard;
import models.card.Card;
import database.DBs.CardDB;
import database.DBs.UserDB;
import database.DBs.UserCardDB;

public class UserController {
    private static final UserDB userDB = new UserDB();
    private static final  CardDB cardDB = new CardDB();
    private static final UserCardDB ucDB = new UserCardDB();
    public static Response sudoGetAllUsers(){

        List<User> allUsers;
        try{
            allUsers = userDB.getAll();
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while fetching all users",-500);
        }

        return new Response("all users fetched successfully",200,"allUsers",allUsers);

    }

    public static Response getAllUsers(int userId){
        User admin;
        try {
            admin = userDB.getOne(userId);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while fetching the user",-500);
        }
        if( admin == null ){
            return new Response("could not find any user with the given id",-400);
        }
        if( !admin.getRole().equals("admin") ){
            return  new Response("user is not an admin",-401);
        }

        List<User> allUsers;
        try{
            allUsers = userDB.getAll();
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while fetching all users",-500);
        }
        if( allUsers == null ){
            return new Response("no user was found",-400);
        }

        return new Response("all users fetched successfully",200,"allUsers",allUsers);

    }

    public static Response createUser(String username, String password, String nickname, String email, String role, String passRecoveryQuestion, String passRecoveryAnswer ){
        if( username.isBlank() ){
            return new Response("username can not be blank",-422);
        }
        if( !Pattern.compile("^[a-zA-Z0-9_]+$").matcher(username).find() ){
            return new Response("username should only contain lower case letters, upper case letters, numbers and under score",-422);
        }
        Response res = sudoGetAllUsers();
        List<User> allUsers = null;
        if(res.ok) {
            allUsers = (List<User>) res.body.get("allUsers");
        }
        boolean duplicateUserName = false;
        if( allUsers != null ) {
            for (User u : allUsers) {
                if (u.getUsername().equals(username)) {
                    duplicateUserName = true;
                    break;
                }
            }
        }
        if( duplicateUserName ){
            return new Response("this username has already been taken",-422);
        }

        if( password.isBlank() ){
            return new Response("password can not be blank",-422);
        }
        if( password.length() < 8 ){
            return new Response("password must be at least 8 characters long",-422);
        }
        if( !Pattern.compile("[a-z]+").matcher(password).find() ){
            return new Response("password must have at least one lower case letter",-422);
        }
        if( !Pattern.compile("[A-Z]+").matcher(password).find() ){
            return new Response("password must have at least one upper case letter",-422);
        }
        if( !Pattern.compile("[0-9]+").matcher(password).find() ){
            return new Response("password must have at least one digit",-422);
        }
        if( !Pattern.compile("[!%@#$^*&\\-+=_/;'.,~]+").matcher(password).find() ){
            return new Response("password must contain at least one special character",-422);
        }

        if( nickname.isBlank() ){
            return new Response("nickname can not be blank",-422);
        }

        if( email.isBlank() ){
            return new Response("email can not be blank",-422);
        }
        if( Pattern.compile("^[a-zA-Z0-9]+@[a-z]+\\.[a-z]+$").matcher(username).find() ){
            return new Response("invalid email format",-422);
        }

        if( passRecoveryQuestion.isBlank() ){
            return new Response("password recovery question can not be blank",-422);
        }

        if( passRecoveryAnswer.isBlank() ){
            return new Response("password recovery answer can not be blank",-422);
        }

        try{
            User user = new User(username, password, nickname, email, role, passRecoveryQuestion, passRecoveryAnswer);
            userDB.create(user);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while creating user",-500);
        }
        return new Response("user successfully created",201);

    }

    public static Response testCreateUser(String username, String password, String nickname, String email, String role, String passRecoveryQuestion, String passRecoveryAnswer){


        User user;
        try{
            user = new User(username, password, nickname, email, role, passRecoveryQuestion, passRecoveryAnswer);
            userDB.create(user);
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception occurred while creating user",-500);
        }

        return new Response("user successfully created",201);

    }

    public static Response login(String username, String password){

        User user = null;
        boolean usernameExists = false;
        boolean passwordIsCorrect = false;
        try{
            List<User> allUsers  = (List<User>) sudoGetAllUsers().body.get("allUsers");
            for(User u: allUsers){
                if( u.getUsername().equals(username) ){
                    usernameExists = true;
                    if( u.getPassword().equals(password) ){
                        user = u;
                        passwordIsCorrect = true;
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while fetching the passwords",-500);
        }

        if( !usernameExists ){
            return new Response("no user was found with this username",-400);
        }

        if( !passwordIsCorrect ){
            return new Response("the password is not correct",-401);
        }

        if( user == null ){
            return new Response("no user was found",-400);
        }

        if( !user.getFirstLogin()){
            Response res = CardController.getAllCards();
            List<Card> allCards = null;
            if( res.ok ){
                allCards = (List<Card>) res.body.get("allCards");
            }
            if( allCards == null ){
                return new Response("a deep error occurred while fetching all cards",-500);
            }
            Random random = new Random();
            for(int i=0;i<20;i++){
                Card card = allCards.get(random.nextInt(allCards.size()));
                UserCard userCard = new UserCard(user.getID(), card.getID());
                int id;
                try {
                    id = ucDB.create(userCard);
                }catch (Exception e){
                    e.printStackTrace();
                    return new Response("a deep error occurred while craeting user card",-500);
                }
                user.addUserCardID(id);
            }

            try{
                userDB.update(user, user.getID());
            }catch (Exception e){
                e.printStackTrace();
                return new Response("an exception happened while saving user",-500);
            }

            user.firstLogin();
        }

        return new Response("user logged in successfully",200,"user",user);
    }

}
