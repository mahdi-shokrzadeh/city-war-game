package controllers;

import java.util.*;
import java.util.regex.Pattern;

import models.Response;
import models.User;
import models.UserCard;
import models.card.Card;
import database.DBs.CardDB;
import database.DBs.UserDB;
import database.DBs.UserCardDB;
import org.example.citywars.M_LoginMenu;


public class UserController {
    private static final UserDB userDB = new UserDB();
    private static final CardDB cardDB = new CardDB();
    private static final UserCardDB ucDB = new UserCardDB();

    public static Response sudoGetAllUsers() {

        List<User> allUsers;
        try {
            allUsers = userDB.getAll();
        } catch (Exception e) {
            return new Response("an exception happened while fetching all users", -500, e);
        }

        return new Response("all users fetched successfully", 200, "allUsers", allUsers);

    }

    public static Response getAllUsers(int userId) {
        User admin;
        try {
            admin = userDB.getOne(userId);
        } catch (Exception e) {
            return new Response("an exception happened while fetching the user", -500, e);
        }
        if (admin == null) {
            return new Response("could not find any user with the given id", -400);
        }
        if (!admin.getRole().equals("admin")) {
            return new Response("user is not an admin", -401);
        }

        List<User> allUsers;
        try {
            allUsers = userDB.getAll();
        } catch (Exception e) {
            return new Response("an exception happened while fetching all users", -500, e);
        }
        if (allUsers == null) {
            return new Response("no user was found", -400);
        }

        return new Response("all users fetched successfully", 200, "allUsers", allUsers);

    }

    public static Response createUser(String username, String password, String nickname, String email, String role, String passRecoveryQuestion, String passRecoveryAnswer) {
        if (username.isBlank()) {
            return new Response("username can not be blank", -422);
        }
        if (!Pattern.compile("^[a-zA-Z0-9_]+$").matcher(username).find()) {
            return new Response("username should only contain lower case letters, upper case letters, numbers and under score", -422);
        }
        Response res = sudoGetAllUsers();
        List<User> allUsers = null;
        if (res.ok) {
            allUsers = (List<User>) res.body.get("allUsers");
        }
        boolean duplicateUserName = false;
        if (allUsers != null) {
            for (User u : allUsers) {
                if (u.getUsername().equals(username)) {
                    duplicateUserName = true;
                    break;
                }
            }
        }
        if (duplicateUserName) {
            return new Response("this username has already been taken", -422);
        }

        if (password.isBlank()) {
            return new Response("password can not be blank", -422);
        }
        if (password.length() < 8) {
            return new Response("password must be at least 8 characters long", -422);
        }
        if (!Pattern.compile("[a-z]+").matcher(password).find()) {
            return new Response("password must have at least one lower case letter", -422);
        }
        if (!Pattern.compile("[A-Z]+").matcher(password).find()) {
            return new Response("password must have at least one upper case letter", -422);
        }
        if (!Pattern.compile("[0-9]+").matcher(password).find()) {
            return new Response("password must have at least one digit", -422);
        }
        if (!Pattern.compile("[!%@#$^*&\\-+=_/;'.,~]+").matcher(password).find()) {
            return new Response("password must contain at least one special character", -422);
        }

        if (nickname.isBlank()) {
            return new Response("nickname can not be blank", -422);
        }

        if (email.isBlank()) {
            return new Response("email can not be blank", -422);
        }
        if (!Pattern.compile("^[a-zA-Z0-9._]+@[a-z]+\\.[a-z]+$").matcher(email).find()) {
            return new Response("invalid email format", -422);
        }

        if (passRecoveryQuestion.isBlank()) {
            return new Response("password recovery question can not be blank", -422);
        }

        if (passRecoveryAnswer.isBlank()) {
            return new Response("password recovery answer can not be blank", -422);
        }

        try {
            User user = new User(username, password, nickname, email, role, passRecoveryQuestion, passRecoveryAnswer);
            userDB.create(user);
        } catch (Exception e) {
            return new Response("an exception occurred while creating user", -500, e);
        }
        return new Response("user successfully created", 201);

    }

    public static Response testCreateUser(String username, String password, String nickname, String email, String role, String passRecoveryQuestion, String passRecoveryAnswer) {


        User user;
        try {
            user = new User(username, password, nickname, email, role, passRecoveryQuestion, passRecoveryAnswer);
            userDB.create(user);
        } catch (Exception e) {
            return new Response("an exception occurred while creating user", -500, e);
        }

        return new Response("user successfully created", 201);

    }

    public static Response getByID(int id) {
        User user = null;
        try {
            user = userDB.getOne(id);
        } catch (Exception e) {
            return new Response("an exception happened while fetching the user", -500, e);
        }
        if (user == null) {
            return new Response("no user was found with this id", -400);
        }
        return new Response("user fetched successfully", 200, "user", user);
    }

    public static Response getByUserName(String username) {
        User user = null;
        try {
            user = userDB.getByUserName(username);
        } catch (Exception e) {
            return new Response("an exception happened while fetching the user", -500, e);
        }
        if (user == null) {
            return new Response("no user was found with this id", -400);
        }
        return new Response("user fetched successfully", 200, "user", user);
    }

    public static Response login(String username, String password) {

        if (M_LoginMenu.timerIsOn) {
            return new Response("Try again in " + M_LoginMenu.lockTime + " seconds", -401);
        }

        User user = null;
        boolean passwordIsCorrect = false;
        try {
            List<User> allUsers = (List<User>) sudoGetAllUsers().body.get("allUsers");
            for (User u : allUsers) {
                if (u.getUsername().equals(username)) {
                    user = u;
                    if (u.getPassword().equals(password)) {
                        passwordIsCorrect = true;
                    }
                }
            }
        } catch (Exception e) {
            return new Response("an exception happened while fetching the passwords", -500, e);
        }

        if (user == null) {
            return new Response("Username doesn't exist!", -400);
        }

        if (!passwordIsCorrect) {
            M_LoginMenu.failureCount++;
            M_LoginMenu.lockTime = 5 * M_LoginMenu.failureCount;
            M_LoginMenu.timerIsOn = true;
            M_LoginMenu.timer = new Timer();
            int delay = 1000;
            int period = 1000;
            M_LoginMenu.timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    setInterval();
                }
            }, delay, period);
            return new Response("Password and Username don't match\nTry again in " + M_LoginMenu.lockTime + " seconds", -401);
        }

        if (!user.getFirstLogin() && !((List<Card>) CardController.getAllCards().body.get("allCards")).isEmpty()) {
            Response res = CardController.getAllCards();
            List<Card> allCards = null;
            if (res.ok) {
                allCards = (List<Card>) res.body.get("allCards");
            }
            if (allCards == null) {
                return new Response("no cards were found while creating starter pack", -500);
            }

            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                Card card = allCards.get(random.nextInt(allCards.size()));
                UserCard userCard = new UserCard(user.getID(), card.getID());
                int id;
                try {
                    id = ucDB.create(userCard);
                } catch (Exception e) {
                    return new Response("a deep error occurred while creating user card", -500, e);
                }
                user.addUserCardID(id);
            }

            user.setCoins(250);

            try {
                userDB.update(user, user.getID());
            } catch (Exception e) {
                return new Response("an exception happened while saving user", -500, e);
            }

            user.firstLogin();
        }

        return new Response("user logged in successfully", 200, "user", user);
    }

    private static final int setInterval() {
        if (M_LoginMenu.lockTime == 1) {
            M_LoginMenu.timer.cancel();
            M_LoginMenu.timerIsOn = false;
        }
        return --M_LoginMenu.lockTime;
    }


    public static Response forgotPassword(String username) {

        User user = null;
        try {
            List<User> allUsers = (List<User>) sudoGetAllUsers().body.get("allUsers");
            for (User u : allUsers) {
                if (u.getUsername().equals(username)) {
                    user = u;
                }
            }

        } catch (Exception e) {
            return new Response("an exception happened while fetching the passwords", -500, e);
        }

        if (user == null) {
            return new Response("Username doesn't exist!", -400);
        } else {
            Scanner sc = new Scanner(System.in);

            System.out.println(user.getPassRecoveryQuestion());

            String answer = "";
            while (answer.isBlank()) {
                answer = sc.nextLine();
                if (!answer.trim().toLowerCase().equals(user.getPassRecoveryAnswer().toLowerCase()))
                    return new Response("Wrong answer! Try again!", -401);
                //answer = "";
            }

            System.out.println("Enter you new password: ");
            String password = sc.nextLine().trim();

            if (password.isBlank()) {
                return new Response("password can not be blank", -422);
            }
            if (password.length() < 8) {
                return new Response("password must be at least 8 characters long", -422);
            }
            if (!Pattern.compile("[a-z]+").matcher(password).find()) {
                return new Response("password must have at least one lower case letter", -422);
            }
            if (!Pattern.compile("[A-Z]+").matcher(password).find()) {
                return new Response("password must have at least one upper case letter", -422);
            }
            if (!Pattern.compile("[0-9]+").matcher(password).find()) {
                return new Response("password must have at least one digit", -422);
            }
            if (!Pattern.compile("[!%@#$^*&\\-+=_/;'.,~]+").matcher(password).find()) {
                return new Response("password must contain at least one special character", -422);
            }

            try {
                user.setPassword(password);
                userDB.update(user, user.getID());
            } catch (Exception e) {
                return new Response("an exception happened while saving new password", -500);
            }

            return new Response("password changed successfully!", 201);
        }
    }

    public static Response editUsername(int id, String value) {

        if (value.isBlank()) {
            return new Response("username can not be blank", -422);
        }
        if (!Pattern.compile("^[a-zA-Z0-9_]+$").matcher(value).find()) {
            return new Response("username should only contain lower case letters, upper case letters, numbers and under score", -422);
        }

        User user;
        try {
            user = userDB.getOne(id);
        } catch (Exception e) {
            return new Response("an exception happened while fetching user", -500, e);
        }
        if (user == null) {
            return new Response("no user was found with this id", -400);
        }

        List<User> allUsers = userDB.getAll();
        for (User u : allUsers) {
            if (u.getUsername().equals(value)) {
                return new Response("a user with this username already exists", -400);
            }
        }

        user.setUsername(value);
        try {
            userDB.update(user, user.getID());
        } catch (Exception e) {
            return new Response("an exception happened while updating user", -500, e);
        }

        return new Response("username updated successfully", 200);
    }

    public static Response editPassword(int id, String value) {

        if (value.isBlank()) {
            return new Response("password can not be blank", -422);
        }
        if (value.length() < 8) {
            return new Response("password must be at least 8 characters long", -422);
        }
        if (!Pattern.compile("[a-z]+").matcher(value).find()) {
            return new Response("password must have at least one lower case letter", -422);
        }
        if (!Pattern.compile("[A-Z]+").matcher(value).find()) {
            return new Response("password must have at least one upper case letter", -422);
        }
        if (!Pattern.compile("[0-9]+").matcher(value).find()) {
            return new Response("password must have at least one digit", -422);
        }
        if (!Pattern.compile("[!%@#$^*&\\-+=_/;'.,~]+").matcher(value).find()) {
            return new Response("password must contain at least one special character", -422);
        }

        User user;
        try {
            user = userDB.getOne(id);
        } catch (Exception e) {
            return new Response("an exception happened while fetching user", -500, e);
        }
        if (user == null) {
            return new Response("no user was found with this id", -400);
        }

        user.setPassword(value);
        try {
            userDB.update(user, user.getID());
        } catch (Exception e) {
            return new Response("an exception happened while updating user", -500, e);
        }

            return new Response("password updated successfully", 200);
        }

    public static Response editNickname(int id, String value) {

        if (value.isBlank()) {
            return new Response("nickname can not be blank", -422);
        }

        User user;
        try {
            user = userDB.getOne(id);
        } catch (Exception e) {
            return new Response("an exception happened while fetching user", -500, e);
        }
        if (user == null) {
            return new Response("no user was found with this id", -400);
        }

        user.setNickname(value);
        try {
            userDB.update(user, user.getID());
        } catch (Exception e) {
            return new Response("an exception happened while updating user", -500, e);
        }

        return new Response("nickname updated successfully", 200);
    }

    public static Response editEmail(int id, String value) {

        if (value.isBlank()) {
            return new Response("email can not be blank", -422);
        }
        if (!Pattern.compile("^[a-zA-Z0-9._]+@[a-z]+\\.[a-z]+$").matcher(value).find()) {
            return new Response("invalid email format", -422);
        }

        User user;
        try {
            user = userDB.getOne(id);
        } catch (Exception e) {
            return new Response("an exception happened while fetching user", -500, e);
        }
        if (user == null) {
            return new Response("no user was found with this id", -400);
        }

        user.setEmail(value);
        try {
            userDB.update(user, user.getID());
        } catch (Exception e) {
            return new Response("an exception happened while updating user", -500, e);
        }

        return new Response("email updated successfully", 200);
    }

}


