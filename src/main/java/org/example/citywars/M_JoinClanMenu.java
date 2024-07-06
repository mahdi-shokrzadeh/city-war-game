package org.example.citywars;

import controllers.ClanController;
import models.Clan;
import models.Response;

import java.util.List;
import java.util.regex.Pattern;

public class M_JoinClanMenu extends Menu {
    private List<Clan> allClans = null;

    public M_JoinClanMenu() {
        super("M_ClanMainMenu");
        // patterns.add(Pattern.compile("^ *-Join +(?<clanName>[\\S ]+) *$"));
    }

    private void printMenu() {
        System.out.println("JOIN CLAN MENU");
        System.out.println("Options: ");
        System.out.println("    show all clans");
        System.out.println("    Back");
        System.out.println("Information: ");
        System.out.println("    You can join a clan using the following command: ");
        System.out.println("        join clan -k <key>");
    }

    public Menu myMethods() {
        printMenu();
        Response res = ClanController.getAllClans();
        if (!res.ok) {
            System.out.println(res.message);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }
        allClans = (List<Clan>) res.body.get("clans");
        do {
            String input = consoleScanner.nextLine().trim();
            if (input.matches("^show all clans$")) {
                if (allClans.isEmpty()) {
                    System.out.println("there are no clans");
                }
                for (Clan clan : allClans) {
                    System.out.println("name: " + clan.getName());
                    System.out.println("number of members: " + clan.getMembersIDS().size());
                    System.out.println("joining key: " + clan.getJoiningKey());
                }
            } else if (input.matches("^join clan -k (?<key>[a-zA-Z0-9/]+)$")) {
                matcher = Pattern.compile("^join clan -k (?<key>[a-zA-Z0-9/]+)$").matcher(input);
                matcher.find();
                String key = matcher.group("key");
                try {
                    res = ClanController.addMember(loggedInUser, key);
                    System.out.println(res.message);
                    if (res.exception != null) {
                        System.out.println(res.exception.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
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
