package models.game;

import java.util.ArrayList;

import models.GameCharacter;
import models.card.Card;

public class AddCard {

    public AddCard() {

    }

    public static void addCard(ArrayList<Card> cards) {

        cards.add(new Card("Fire", 0, 1, "Regular", 20, 15, 1, 0, "desc1",
                new GameCharacter("c1")));

        cards
                .add(new Card("Water", 0, 4, "Regular", 45, 35, 1, 0, "water description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Earth", 0, 3, "Regular", 30, 25, 1, 0, "earth description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Air", 0, 2, "Regular", 25, 20, 1, 0, "air description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Fire324", 0, 1, "Regular", 20, 15, 1, 0, "fire description",
                        new GameCharacter("c1")));
        cards.add(
                new Card("Waterdsff", 0, 3, "Regular", 55, 15, 1, 0, "water description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Dragon", 0, 5, "Regular", 70, 50, 1, 0, "dragon description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Wizard", 0, 4, "Regular", 45, 35, 1, 0, "wizard description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Knight", 0, 3, "Regular", 30, 25, 1, 0, "knight description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Archer", 0, 2, "Regular", 25, 20, 1, 0, "archer description",
                        new GameCharacter("c1")));
        cards.add(new Card("Mega Knight", 0, 5, "Regular", 70, 50, 1, 0,
                "mega knight description",
                new GameCharacter("c1")));
        cards
                .add(new Card("Sparky", 0, 6, "Regular", 100, 70, 1, 0, "sparky description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Giant", 0, 4, "Regular", 45, 35, 1, 0, "giant description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Goblin", 0, 3, "Regular", 30, 25, 1, 0, "goblin description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Minion", 0, 2, "Regular", 25, 20, 1, 0, "minion description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Pekka", 0, 5, "Regular", 70, 50, 1, 0, "pekka description",
                        new GameCharacter("c1")));
        cards.add(
                new Card("Hog Rider", 0, 4, "Regular", 45, 35, 1, 0, "hog rider description",
                        new GameCharacter("c1")));
        cards.add(
                new Card("Valkyrie", 0, 3, "Regular", 30, 25, 1, 0, "valkyrie description",
                        new GameCharacter("c1")));
        cards
                .add(new Card("Witch", 0, 2, "Regular", 25, 20, 1, 0, "witch description",
                        new GameCharacter("c1")));
        cards.add(new Card("Lava Hound", 0, 5, "Regular", 70, 50, 1, 0,
                "lava hound description",
                new GameCharacter("c1")));
        cards.add(
                new Card("Balloon", 0, 4, "Regular", 45, 35, 1, 0, "balloon description",
                        new GameCharacter("c1")));
        cards.add(new Card("Baby Dragon", 0, 3, "Regular", 30, 25, 1, 0,
                "baby dragon description",
                new GameCharacter("c1")));
        cards.add(
                new Card("Giant Skeleton", 0, 2, "Regular", 25, 20, 1, 0, "giantskeletondescription",
                        new GameCharacter("c1")));
        cards.add(new Card("Barbarians", 0, 5, "Regular", 70, 50, 1, 0,
                "barbarians description",
                new GameCharacter("c1")));
    }
}
