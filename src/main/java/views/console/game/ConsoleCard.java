package views.console.game;

import java.util.ArrayList;

import models.card.Card;

public class ConsoleCard {

    public static void printUserCards(ArrayList<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + "- " + cards.get(i).getName());
        }
    }

    public static void printCard(Card card) {

    }
    
}
