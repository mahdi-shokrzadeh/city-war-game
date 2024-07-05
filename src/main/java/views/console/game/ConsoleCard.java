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
                    printCard(i + 1, cards.get(i));
            } else {
                printCard(i + 1, cards.get(i));
            }
        }
        System.out.println("\n");
    }

    public static void printCard(int index, Card card) {
        System.out.println(index + ". " + card.getName() + " - " + "type: " + card.getCardType() + " - " +
                "damage: " + card.getDamage() + " - " +
                "power: " + card.getPower() + " - " + "duration: " + card.getDuration());
    }

}
