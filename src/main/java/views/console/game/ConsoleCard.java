package views.console.game;

import java.util.ArrayList;

import models.User;
import models.card.Card;

public class ConsoleCard {

    public static void printUserCards(ArrayList<Card> cards, User player) {
        System.out.println("\n" + player.getUsername() + "'s cards:");
        if (player.getShouldCardsBeHidden()) {
            player.setShouldCardsBeHidden(false);
            System.out.println("Cards are hidden!");
            return;
        }

        for (int i = 0; i < cards.size(); i++) {
            if (i >= 4) {
                if (!player.getCardsAreStolen())
                    printCard(i + 1, cards.get(i), "normal");
            } else {
                printCard(i + 1, cards.get(i), "normal");
            }
        }
        System.out.println("\n");
    }

    public static void printCard(int index, Card card, String type) {
        int name_len = 16;
        int other_len = 8;
        System.out.println(index + ". " + ConsoleBoard.padString(card.getName(), name_len) + "- " + "type: "
                + ConsoleBoard.padString(String.valueOf(card.getCardType()), name_len) + "- " +
                "damage: " + ConsoleBoard.padString(String.valueOf(card.getDamage()), other_len) + "- " +
                "power: " + ConsoleBoard.padString(String.valueOf(card.getPower()), other_len) + "- " + "duration: " +
                card.getDuration());

        switch (type) {
            case "complete":
                System.out.println("description: " + card.getDesc());
                break;

            default:
                break;
        }
    }

}
