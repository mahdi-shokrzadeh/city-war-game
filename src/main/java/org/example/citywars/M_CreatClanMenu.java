package org.example.citywars;

import controllers.ClanController;
import models.Response;

import java.util.regex.Pattern;

public class M_CreatClanMenu extends Menu {
    public M_CreatClanMenu() {
        super("M_ClanMainMenu");
        // patterns.add(Pattern.compile("^ *-Create +a +clan +(?<clanName>[\\S ]+)

    }

    private void printMenu() {
        System.out.println("CREATE CLAN");
        System.out.println("Options: ");
        System.out.println("    Back");
        System.out.println("Information: ");
        System.out.println("    You can create a clan using the following command: ");
        System.out.println("        create clan -n <name>");
    }

    public Menu myMethods() {
        printMenu();
        do {
            String input = consoleScanner.nextLine().trim();
            if (input.matches("^create clan -n (?<name>[a-zA-Z]+)$")) {
                matcher = Pattern.compile("^create clan -n (?<name>[a-zA-Z]+)$").matcher(input);
                matcher.find();
                try {
                    Response res = ClanController.createClan(loggedInUser, matcher.group("name"));
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
