package controllers;

import java.util.List;

import database.Database;
import models.Response;
import models.User;

public class UserController extends User {

    public UserController(String username, String password, String nickname, String email, String role,
            String recovery_pass_question, String recovery_pass_answer, int level, int experience, int hitPoints)

    {
        super(username, password, nickname, email, role, recovery_pass_question, recovery_pass_answer, level,
                experience,
                hitPoints);
    }

    // other methods will be here if needed!

    public static Response getAllUsers(int userId){
        Database<User> usersDB = new Database<>("users");

        User admin;
        try {
            admin = usersDB.getOne(userId);
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
            allUsers = usersDB.getAll();
        }catch (Exception e){
            e.printStackTrace();
            return new Response("an exception happened while fetching all users",-500);
        }
        if( allUsers == null ){
            return new Response("no user was found",-400);
        }

        return new Response("all userse fetched successfully",200,allUsers);

    }

}
