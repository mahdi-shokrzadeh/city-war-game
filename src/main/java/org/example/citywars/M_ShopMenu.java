package org.example.citywars;

import controllers.CardController;
import controllers.UserCardsController;
import controllers.UserController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import models.GameCharacter;
import models.Response;
import models.User;
import models.UserCard;
import models.card.Card;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.almasb.fxgl.core.collection.Array;

public class M_ShopMenu extends Menu {

    @FXML
    private Pane pane;
    @FXML
    private Text coinsText;

    public M_ShopMenu() {
        super("M_ShopMenu", new String[] { "BG-Videos\\shopMenu.png" });
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
        System.out.println("character: " + user.getGameCharacter().getName());
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
            System.out.println("character: " + card.getCharacter().getName());
            System.out.println("price:" + card.getPrice());
            System.out.println("upgrade level: " + card.getUpgradeLevel());
            System.out.println("upgrade cost (upgrade cost increases with each upgrade): " + card.getUpgradeCost());
            System.out.println();
        } else if (card.getCardType().toString().equals("Spell")) {
            System.out.println("name: " + card.getName());
            System.out.println("type: " + card.getCardType());
            System.out.println("duration: " + card.getDuration());
            System.out.println("price:" + card.getPrice());
            System.out.println("description: " + card.getDesc());
            System.out.println();
        }
    }

    private void printCard(Card card) {
        if (card.getCardType().toString().equals("Regular")) {
            System.out.println("name: " + card.getName());
            System.out.println("type: " + card.getCardType());
            System.out.println("power: " + card.getPower());
            System.out.println("damage: " + card.getDamage());
            System.out.println("duration: " + card.getDuration());
            System.out.println("character: " + card.getCharacter().getName());
            System.out.println("price:" + card.getPrice());
            System.out.println("upgrade level: " + card.getUpgradeLevel());
            System.out.println("upgrade cost (upgrade cost increases with each upgrade): " + card.getUpgradeCost());
            System.out.println("description: " + card.getDesc());
            System.out.println();
        } else if (card.getCardType().toString().equals("Spell")) {
            System.out.println("name: " + card.getName());
            System.out.println("type: " + card.getCardType());
            System.out.println("duration: " + card.getDuration());
            System.out.println("price:" + card.getPrice());
            System.out.println("description: " + card.getDesc());
            System.out.println();
        }
    }

    private void printUpCard(Card card, UserCard uc) {
        System.out.println("name: " + card.getName());
        System.out.println("power after upgrade: " + (card.getDamage() + 10 * (uc.getLevel())));
        System.out.println("damage after upgrade: " + (card.getPower() + 3 * (uc.getLevel())));
        System.out.println("upgrade cost: " + card.getUpgradeCost() * uc.getLevel());
        System.out.println("minimum user level required to upgrade: " + card.getUpgradeLevel());
        System.out.println();
    }

    private void revalidate() {
        res = UserController.getByID(loggedInUser.getID());
        if (res.ok) {
            loggedInUser = (User) res.body.get("user");
        } else {
            System.out.println(res.message);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }
        res = CardController.getAllCards();
        if (!res.ok) {
            System.out.println(res.message);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }
        allCards = (List<Card>) res.body.get("allCards");
        res = UserCardsController.getUnleveledCards(loggedInUser);
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
        revalidate();
        do {
            String input = consoleScanner.nextLine().trim();
            if (input.matches("^show available cards$")) {
                for (Card card : notOwnedCards) {
                    res = UserCardsController.getUserCard(loggedInUser.getID(), card.getID());
                    if (!res.ok && res.status != -404) {
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
                    boolean found = false;
                    for (Card card : notOwnedCards) {
                        if (card.getName().equals(name)) {
                            res = UserCardsController.buyCard(loggedInUser, card);
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        System.out.println(res.message);
                        if (res.exception != null) {
                            System.out.println(res.exception.getMessage());
                        }
                    } else {
                        System.out.println("card not found");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                revalidate();
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
                    boolean found = false;
                    for (Card card : cards) {
                        if (card.getName().equals(name)) {
                            found = true;
                            res = UserCardsController.upgradeCard(loggedInUser, card);
                            break;
                        }
                    }
                    if (found) {
                        System.out.println(res.message);
                        if (res.exception != null) {
                            System.out.println(res.exception.getMessage());
                        }
                    } else {
                        System.out.println("card not found");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                revalidate();
            } else if (input.matches("^show all cards$")) {
                if (!loggedInUser.getRole().equals("admin")) {
                    System.out.println("admins only!");
                    continue;
                }
                if (allCards.isEmpty()) {
                    System.out.println("there are no cards yet");
                }
                for (int i = 0; i < allCards.size(); i++) {
                    printNumberedCard(allCards.get(i), i + 1);
                }
            } else if (input.matches(
                    "^add card -name (?<name>[a-zA-Z]+) -prc (?<price>[0-9]+) -dur (?<duration>[0-9]+) -pow (?<power>[0-9]+) -dmg (?<damage>[0-9]+) -upl (?<upgradeLevel>[0-9]+) -upc (?<upgradeCost>[0-9]+) -chr (?<gameCharacter>[a-zA-Z]+)$")) {
                if (!loggedInUser.getRole().equals("admin")) {
                    System.out.println("admins only!");
                    continue;
                }
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
                revalidate();
            } else if (input.matches(
                    "^edit card -num (?<number>[0-9]+) -name (?<name>[a-zA-Z]+) -prc (?<price>[0-9]+) -dur (?<duration>[0-9]+) -pow (?<power>[0-9]+) -dmg (?<damage>[0-9]+) -upl (?<upgradeLevel>[0-9]+) -upc (?<upgradeCost>[0-9]+) -chr (?<gameCharacter>[a-zA-Z]+)$")) {
                if (!loggedInUser.getRole().equals("admin")) {
                    System.out.println("admins only!");
                    continue;
                }
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
                            allCards.get((Integer.parseInt(matcher.group("number"))) - 1).getID(),
                            matcher.group("name"),
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
                revalidate();
            } else if (input.matches("^remove card -num (?<number>[0-9]+)$")) {
                if (!loggedInUser.getRole().equals("admin")) {
                    System.out.println("admins only!");
                    continue;
                }
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
                revalidate();
            } else if (input.matches("^show all users$")) {
                if (!loggedInUser.getRole().equals("admin")) {
                    System.out.println("admins only!");
                    continue;
                }
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

    public void Back(Event event) throws IOException {
        HelloApplication.menu = new M_GameMainMenu();
        switchMenus(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Card> allCards = null;
        List<Card> userCards = null;
        List<TextFlow> tfs = new ArrayList<>();
        Response res = CardController.getAllCards();
        Alert alert = new Alert(AlertType.NONE);
        if (res.ok) {
            allCards = (List<Card>) res.body.get("allCards");
        } else {
            System.out.println(res.message);
        }
        User _user = new User("Drew", "UserUser2@", "admin", "admin@gmail.com",
                "admin", "recQuestion", "recAnswer");
        _user.setID(1);
        _user.setCoins(10000);
        _user.setGameCharacter(new GameCharacter("panda"));
        _user.setLevel(3);
        coinsText.setText("Coins: " + _user.getCoins());
        res = UserCardsController.getUsersCards(_user);
        if (res.ok) {
            userCards = (List<Card>) res.body.get("cards");
        } else {
            System.out.println(res.message);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }

        allCards.removeIf(s -> {
            if (s.getCardType().toString().equals("Spell")) {
                if (s.getSpellType().toString().equals("Copy")) {
                    return true;
                } else if (s.getSpellType().toString().equals("Hide")) {
                    return true;
                } else if (s.getSpellType().toString().equals("AddSpace")) {
                    return true;
                } else if (s.getSpellType().toString().equals("Mirror")) {

                    return true;
                } else if (s.getSpellType().toString().equals("Steal")) {
                    return true;

                } else {
                    return false;
                }
            } else {
                return false;
            }
        });

        for (int i = 0; i < allCards.size(); i++) {
            Card card = allCards.get(i);

            boolean hasCard = false;
            for (Card c : userCards) {
                if (c.getName().equals(card.getName())) {
                    hasCard = true;
                }
            }

            Integer level = null;
            UserCard uc = null;
            if (hasCard) {
                res = UserCardsController.getUserCard(_user.getID(), card.getID());
                if (res.ok) {
                    uc = ((UserCard) res.body.get("userCard"));
                    level = (int) uc.getLevel();
                }
            } else {
                level = 1;
            }

            res = CardController.getCardImage(card, level);
            TextFlow tf = null;
            if (res.ok) {
                tf = (TextFlow) res.body.get("textFlow");
            } else {
                System.out.println(res.message);
            }
            // tf.setBackground(Background.fill(Paint.valueOf("blue")));
            tf.setPrefWidth(180);
            tf.setPrefHeight(50);
            tf.setTranslateX(400 + 230 * (i % 5));
            tf.setTranslateY(100 + 350 * (i / 5));
            pane.getChildren().add(tf);

            for (Node node : tf.getChildren()) {
                if (node.getId() != null) {
                    if (node.getId() == "hBox") {
                        node.setTranslateX(19);
                        node.setTranslateY(-145);
                    } else if (node.getId() == "power") {
                        if (hasCard && uc.getLevel() < 3) {
                            ((Text) node).setText(((char) '\u2191')
                                    + String.valueOf(Integer.parseInt(((Text) node).getText()) + card.getDuration()));
                            ((Text) node).setFont(Font.font(25));
                            ((Text) node).setFill(Paint.valueOf("#07d104"));
                            node.setTranslateX(5);
                            node.setTranslateY(-248);
                        } else {
                            ((Text) node).setFont(Font.font(25));
                            node.setTranslateX(27.5);
                            node.setTranslateY(-248);
                        }
                    } else if (node.getId() == "damage") {
                        if (hasCard && uc.getLevel() < 3) {
                            ((Text) node).setText(
                                    ((char) '\u2191') + String.valueOf(Integer.parseInt(((Text) node).getText()) + 3));
                            ((Text) node).setFont(Font.font(14));
                            ((Text) node).setFill(Paint.valueOf("#07d104"));
                            node.setTranslateX(87);
                            node.setTranslateY(-65);
                        } else {
                            ((Text) node).setFont(Font.font(14));
                            node.setTranslateX(97);
                            node.setTranslateY(-65);
                        }
                    }
                }
            }

            if (hasCard) {
                Button button = null;
                if (uc.getLevel() >= 3) {
                    button = new Button("Max level");
                } else {
                    button = new Button("Upgrade (" + (uc.getLevel() + 1) * card.getUpgradeCost() + "$)");

                }
                button.setTextFill(Paint.valueOf("white"));
                if (uc.getLevel() < 3 && card.getCardType().toString().equals("Regular")) {
                    button.setCursor(Cursor.HAND);
                    button.setBackground(Background.fill(Paint.valueOf("#8900AC")));
                    button.setOnMouseClicked((event) -> {
                        Response _res = UserCardsController.upgradeCard(_user, card);
                        alert.setContentText(_res.message);
                        alert.setAlertType(AlertType.INFORMATION);
                        alert.show();
                        int coins = ((User) UserController.getByID(_user.getID()).body.get("user")).getCoins();
                        coinsText.setText("Coins: " + coins);
                    });
                } else {
                    button.setBackground(Background.fill(Paint.valueOf("#e8c400")));
                }
                button.setPrefWidth(160);
                button.setMaxHeight(30);
                button.setTranslateX(tf.getTranslateX() + 25);
                button.setTranslateY(tf.getTranslateY() + 285);
                pane.getChildren().add(button);
            } else {
                for (Node node : tf.getChildren()) {
                    if (node.getId() != null && node.getId().equals("imageView")) {
                        ColorAdjust colorAdjust = new ColorAdjust();
                        colorAdjust.setSaturation(-0.95);
                        node.setEffect(colorAdjust);
                    }
                }
                Button button = new Button("Purchase (" + card.getPrice() + "$)");
                button.setCursor(Cursor.HAND);
                button.setBackground(Background.fill(Paint.valueOf("green")));
                button.setTextFill(Paint.valueOf("white"));
                button.setPrefWidth(160);
                button.setMaxHeight(30);
                button.setTranslateX(tf.getTranslateX() + 25);
                button.setTranslateY(tf.getTranslateY() + 285);
                button.setOnMouseClicked((event) -> {
                    Response _res = UserCardsController.buyCard(_user, card);
                    System.out.println(_res.message);
                    alert.setAlertType(AlertType.INFORMATION);
                    alert.setContentText("card purchased successfully");
                    alert.show();
                    int coins = ((User) UserController.getByID(_user.getID()).body.get("user")).getCoins();
                    coinsText.setText("Coins: " + coins);
                });
                pane.getChildren().addAll(button);
            }

        }

        pane.setPrefHeight(allCards.size() * 400 + 250);
        pane.setPrefWidth(1600);

    }

}
