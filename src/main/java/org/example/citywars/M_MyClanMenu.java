package org.example.citywars;

import controllers.ClanController;
import models.Clan;
import models.Response;

import java.util.Map;
import java.util.regex.Pattern;

public class M_MyClanMenu extends Menu {
    private Clan myClan = null;
    private Map<String, Object> battles = null;

    public M_MyClanMenu() {
        super("M_ClanMainMenu");
    }

    public void printMenu(boolean shouldPlay) {
        System.out.println("MY CLAN MENU");
        System.out.println("Options: ");
        System.out.println("    take me to battle");
        System.out.println("Information:");
        System.out.println("    (leader only) You can start battling with another clan using the following command: ");
        System.out.println("        start battle -k <key>");
        System.out.println();
        System.out.println("My clan");
        System.out.println("name: " + myClan.getName());
        System.out.println("joining key: " + myClan.getJoiningKey());
        System.out.println("battle key: " + myClan.getBattleID());
        System.out.println("number of members: " + myClan.getMembersIDS().size());
        System.out.println("number of wins: " + battles.get("numberOfWins"));
        System.out.println("number of losses: " + battles.get("numberOfLosses"));
        if (shouldPlay) {
            System.out.println("your clan needs you! Dive into battle asap!!");
        } else {
            System.out.println("you have served your duty well.");
        }
    }

    public Menu myMethods() {
        Response res = ClanController.getMyClan(loggedInUser);
        if (res.ok) {
            myClan = (Clan) res.body.get("myClan");
        } else {
            System.out.println(res.message);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }
        if (myClan == null) {
            System.out.println("you don't have a clan yet, join one first");
            return new M_ClanMainMenu();
        }
        res = ClanController.getBattles(myClan.getID());
        if (res.ok) {
            battles = res.body;
        } else {
            System.out.println(res.message);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }
        boolean shouldPlay;
        res = ClanController.getShouldPlay(loggedInUser);
        shouldPlay = (boolean) res.body.get("shouldPlay");
        if (res.exception != null) {
            System.out.println(res.message);
            System.out.println(res.exception.getMessage());
        }
        printMenu(shouldPlay);

        do {
            String input = consoleScanner.nextLine().trim();
            if (input.matches("start battle -k (?<key>[a-zA-Z]+)")) {
                matcher = Pattern.compile("start battle -k (?<key>[a-zA-Z]+)").matcher(input);
                matcher.find();
                String key = matcher.group("key");
                if (myClan.getLeaderID() != loggedInUser.getID()) {
                    System.out.println("only leader can start battle");
                }
                try {
                    res = ClanController.startBattle(loggedInUser, myClan, key);
                    System.out.println(res.message);
                    if (res.ok) {
                        shouldPlay = true;
                    }
                    if (res.exception != null) {
                        System.out.println(res.exception.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (input.matches("^take me to battle$")) {
                if (shouldPlay) {
                    return new M_GamePlayMenu();
                } else {
                    System.out.println("you have served your duty well.");
                }
            } else if (input.matches("show current menu")) {
                System.out.println("You are currently at " + getName());
            } else if (input.matches("^Back$")) {
                return new M_ClanMainMenu();
            } else {
                System.out.println("invalid command!");
            }
        } while (true);
    }
}
