package org.example.citywars;

import controllers.CardController;
import controllers.UserCardsController;
import controllers.UserController;
import models.Response;
import models.User;
import models.UserCard;
import models.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class M_ShopMenu extends Menu {
    public M_ShopMenu() {
        super("M_ShopMenu");
    }

    private List<Card> allCards = null;
    private List<Card> cards = null;
    private List<Card> notOwnedCards = null;
    private Response res;

    private void printMenu() {
        System.out.println("SHOP MENU");
        System.out.println("Options: ");
        System.out.println("    show available cards");
        System.out.println("    show my cards");
        System.out.println("    (admins only) show all cards");
        System.out.println("    (admins only) show all users");
        System.out.println("    Back");
        System.out.println("Information: ");
        System.out.println("    You can buy a new card using the command: ");
        System.out.println("        buy card -n <name>");
        System.out.println("    You can upgrade one your cards using the command: ");
        System.out.println("        upgrade card -n <name>");
        System.out.println("    (admins only) You can add a new card using the command: ");
        System.out.println(
                "        add card -name <name> -prc <price> -dur <duration> -pow <power> -dmg <damage> -upl <upgradeLevel> -upc <upgradeCost> -chr <gameCharacter>");
        System.out.println("    (admins only) You can edit a card using the command: ");
        System.out.println(
                "        edit card -num <number> -name <name> -prc <price> -dur <duration> -pow <power> -dmg <damage> -upl <upgradeLevel> -upc <upgradeCost> -chr <gameCharacter>");
        System.out.println("    (admins only) You can remove a card using the command: ");
        System.out.println("        remove card -num <number>");

    }

    private void printUser(User user) {
        System.out.println("username: " + user.getUsername());
        System.out.println("nickname: " + user.getNickname());
        System.out.println("email: " + user.getEmail());
        System.out.println("role: " + user.getRole());
        System.out.println("character: " + user.getGameCharacter());
        System.out.println("progress: " + user.getProgress());
        System.out.println("hp: " + user.getHitPoints());
        System.out.println("level: " + user.getLevel());
        System.out.println("coins: " + user.getCoins());
    }

    private void printNumberedCard(Card card, int n) {
        if (card.getCardType().toString().equals("Regular")) {
            System.out.println("number: " + n);
            System.out.println("name: " + card.getName());
            System.out.println("type: " + card.getCardType());
            System.out.println("power: " + card.getPower());
            System.out.println("damage: " + card.getDamage());
            System.out.println("duration: " + card.getDuration());
            System.out.println("character: " + card.getCharacter());
            System.out.println("price:" + card.getPrice());
            System.out.println("upgrade level: " + card.getUpgradeLevel());
            System.out.println("upgrade cost (upgrade cost increases with each upgrade): " + card.getUpgradeCost());
        } else if (card.getCardType().toString().equals("Spell")) {
            System.out.println("name: " + card.getName());
            System.out.println("type: " + card.getCardType());
            System.out.println("duration: " + card.getDuration());
            System.out.println("price:" + card.getPrice());
            System.out.println("description: " + card.getDesc());
        }
    }

    private void printCard(Card card) {
        if (card.getCardType().toString().equals("Regular")) {
            System.out.println("name: " + card.getName());
            System.out.println("type: " + card.getCardType());
            System.out.println("power: " + card.getPower());
            System.out.println("damage: " + card.getDamage());
            System.out.println("duration: " + card.getDuration());
            System.out.println("character: " + card.getCharacter());
            System.out.println("price:" + card.getPrice());
            System.out.println("upgrade level: " + card.getUpgradeLevel());
            System.out.println("upgrade cost (upgrade cost increases with each upgrade): " + card.getUpgradeCost());
            System.out.println("description: " + card.getDesc());
        } else if (card.getCardType().toString().equals("Spell")) {
            System.out.println("name: " + card.getName());
            System.out.println("type: " + card.getCardType());
            System.out.println("duration: " + card.getDuration());
            System.out.println("price:" + card.getPrice());
            System.out.println("description: " + card.getDesc());
        }
    }

    private void printUpCard(Card card, UserCard uc) {
        System.out.println("name: " + card.getName());
        System.out.println("power after upgrade: " + (card.getDamage() + 15 * (uc.getLevel() + 1)));
        System.out.println("damage after upgrade: " + (card.getPower() + 3 * (uc.getLevel() + 1)));
        System.out.println("upgrade cost: " + card.getUpgradeCost() * uc.getLevel());
        System.out.println("minimum user level required to upgrade: " + card.getUpgradeLevel());
    }

    private void fetch() {
        res = CardController.getAllCards();
        if (!res.ok) {
            System.out.println(res.message);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }
        allCards = (List<Card>) res.body.get("allCards");
        res = UserCardsController.getUsersCards(loggedInUser);
        if (!res.ok) {
            System.out.println(res.message);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }
        cards = (List<Card>) res.body.get("cards");
        notOwnedCards = new ArrayList<>(allCards);
        if (cards != null)
            notOwnedCards.removeAll(cards);
    }

    public Menu myMethods() {
        printMenu();
        fetch();
        do {
            String input = consoleScanner.nextLine().trim();
            if (input.matches("^show available cards$")) {
                for (Card card : notOwnedCards) {
                    res = UserCardsController.getUserCard(loggedInUser.getID(), card.getID());
                    if (!res.ok) {
                        System.out.println(res.message);
                        if (res.exception != null) {
                            System.out.println(res.exception.getMessage());
                        }
                    }
                    printCard(card);
                }
            } else if (input.matches("^buy card -n (?<name>[a-zA-Z]+)$")) {
                matcher = Pattern.compile("^buy card -n (?<name>[a-zA-Z]+)$").matcher(input);
                matcher.find();
                try {
                    String name = matcher.group("name");
                    for (Card card : notOwnedCards) {
                        if (card.getName().equals(name)) {
                            res = UserCardsController.buyCard(loggedInUser.getID(), card);
                            break;
                        }
                    }
                    System.out.println(res.message);
                    if (res.exception != null) {
                        System.out.println(res.exception.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                fetch();
            } else if (input.matches("^show my cards$")) {
                if (cards.isEmpty()) {
                    System.out.println("you do not have any cards");
                }
                for (Card card : cards) {
                    try {
                        res = UserCardsController.getUserCard(loggedInUser.getID(), card.getID());
                        UserCard uc = (UserCard) res.body.get("userCard");
                        if (res.ok) {
                            if (card.getCardType().toString().equals("Regular")) {
                                printUpCard(card, uc);
                            } else if (card.getCardType().toString().equals("Spell")) {
                                printCard(card);
                            }
                        } else {
                            System.out.println(res.message);
                            if (res.exception != null) {
                                System.out.println(res.exception.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else if (input.matches("^upgrade card -n (?<name>[a-zA-Z]+)$")) {
                matcher = Pattern.compile("^upgrade card -n (?<name>[a-zA-Z]+)$").matcher(input);
                matcher.find();
                try {
                    String name = matcher.group("name");
                    for (Card card : cards) {
                        if (card.getName().equals(name)) {
                            res = UserCardsController.upgradeCard(loggedInUser, card);
                            break;
                        }
                    }
                    System.out.println(res.message);
                    if (res.exception != null) {
                        System.out.println(res.exception.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                fetch();
            } else if (input.matches("^show all cards$")) {
                if (allCards.isEmpty()) {
                    System.out.println("there are no cards yet");
                }
                for (int i = 0; i < allCards.size(); i++) {
                    printNumberedCard(allCards.get(i), i + 1);
                }
            } else if (input.matches(
                    "^add card -name (?<name>[a-zA-Z]+) -prc (?<price>[0-9]+) -dur (?<duration>[0-9]+) -pow (?<power>[0-9]+) -dmg (?<damage>[0-9]+) -upl (?<upgradeLevel>[0-9]+) -upc (?<upgradeCost>[0-9]+) -chr (?<gameCharacter>[a-zA-Z]+)$")) {
                matcher = Pattern.compile(
                        "^add card -name (?<name>[a-zA-Z]+) -prc (?<price>[0-9]+) -dur (?<duration>[0-9]+) -pow (?<power>[0-9]+) -dmg (?<damage>[0-9]+) -upl (?<upgradeLevel>[0-9]+) -upc (?<upgradeCost>[0-9]+) -chr (?<gameCharacter>[a-zA-Z]+)$")
                        .matcher(input);
                matcher.find();
                System.out.println("please enter one line as a description for card: ");
                String desc = consoleScanner.nextLine();
                if (desc == null || desc.isBlank()) {
                    System.out.println("invalid description!");
                    printMenu();
                    continue;
                }
                try {
                    res = CardController.createCard(loggedInUser, matcher.group("name"),
                            Integer.parseInt(matcher.group("price")), Integer.parseInt(matcher.group("duration")),
                            "Regular", Integer.parseInt(matcher.group("power")),
                            Integer.parseInt(matcher.group("damage")), Integer.parseInt(matcher.group("upgradeLevel")),
                            Integer.parseInt(matcher.group("upgradeCost")), desc, matcher.group("gameCharacter"));
                    System.out.println(res.message);
                    if (res.exception != null) {
                        System.out.println(res.exception.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                fetch();
            } else if (input.matches(
                    "^edit card -num (?<number>[0-9]+) -name (?<name>[a-zA-Z]+) -prc (?<price>[0-9]+) -dur (?<duration>[0-9]+) -pow (?<power>[0-9]+) -dmg (?<damage>[0-9]+) -upl (?<upgradeLevel>[0-9]+) -upc (?<upgradeCost>[0-9]+) -chr (?<gameCharacter>[a-zA-Z]+)$")) {
                matcher = Pattern.compile(
                        "^edit card -num (?<number>[0-9]+) -name (?<name>[a-zA-Z]+) -prc (?<price>[0-9]+) -dur (?<duration>[0-9]+) -pow (?<power>[0-9]+) -dmg (?<damage>[0-9]+) -upl (?<upgradeLevel>[0-9]+) -upc (?<upgradeCost>[0-9]+) -chr (?<gameCharacter>[a-zA-Z]+)$")
                        .matcher(input);
                matcher.find();
                System.out.println("please enter one line as a description for card: ");
                String desc = consoleScanner.nextLine();
                if (desc == null || desc.isBlank()) {
                    System.out.println("invalid description!");
                    printMenu();
                    continue;
                }
                try {
                    res = CardController.editCard(loggedInUser,
                            allCards.get(Integer.parseInt(matcher.group("number"))).getID(), matcher.group("name"),
                            Integer.parseInt(matcher.group("price")), Integer.parseInt(matcher.group("duration")),
                            Integer.parseInt(matcher.group("power")), Integer.parseInt(matcher.group("damage")),
                            Integer.parseInt(matcher.group("upgradeLevel")),
                            Integer.parseInt(matcher.group("upgradeCost")), desc, matcher.group("gameCharacter"));
                    System.out.println(res.message);
                    if (res.exception != null) {
                        System.out.println(res.exception.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                fetch();
            } else if (input.matches("^remove card -num (?<number>[0-9]+)$")) {
                matcher = Pattern.compile("^remove card -num (?<number>[0-9]+)$").matcher(input);
                matcher.find();
                Card card = allCards.get(Integer.parseInt(matcher.group("number")) - 1);
                try {
                    res = CardController.removeCard(loggedInUser, card);
                    System.out.println(res.message);
                    if (res.exception != null) {
                        System.out.println(res.exception.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println("here 3");
                    System.out.println(e.getMessage());
                }
                fetch();
            } else if (input.matches("^show all users$")) {
                try {
                    List<User> allUsers = (List<User>) UserController.getAllUsers(loggedInUser).body.get("allUsers");
                    for (User u : allUsers) {
                        printUser(u);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (input.matches("^Back$")) {
                return new M_GameMainMenu();
            } else if (input.matches("^show current menu$")) {
                System.out.println("You are currently at " + getName());
            } else {
                System.out.println("invalid command!");
            }
        } while (true);
    }
}
