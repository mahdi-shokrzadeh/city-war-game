package models.game;

import java.util.ArrayList;

import models.GameCharacter;
import models.card.Card;
import models.card.Spell;

public class AddCard {

        public AddCard() {

        }

        public static void addCard(ArrayList<Card> cards) {

                cards.add(new Card("KnifeSlash", 0, 3, "Regular", 28, 27, 1, 0,
                                "A panda should not rely on his magic only",
                                new GameCharacter("panda")));

                cards
                                .add(new Card("SwordOfHonor", 0, 3, "Regular", 30, 35, 1, 0,
                                                "Legends say this sword will bring justice to our village",
                                                new GameCharacter("warrior")));
                cards
                                .add(new Card("AwesomePunch", 0, 5, "Regular", 34, 50, 1, 0, "An Awesome Punch",
                                                new GameCharacter("warrior")));
                cards
                                .add(new Card("JumpScare", 0, 1, "Regular", 33, 25, 1, 0,
                                                "Surprizingly opponents take damage when jump scared",
                                                new GameCharacter("wolf")));
                cards
                                .add(new Card("MagicSlash", 0, 4, "Regular", 30, 40, 1, 0,
                                                "Multiple magical slashes with a dragons paw",
                                                new GameCharacter("dragon")));
                cards.add(
                                new Card("Teleportation", 0, 1, "Regular", 34, 40, 1, 0,
                                                "Teleport through another demension and attack your enemy from behind",
                                                new GameCharacter("warrior")));
                cards
                                .add(new Card("BackToTheWall", 0, 4, "Regular", 29, 44, 1, 0,
                                                "A wolf is nevet back to the wall unless it wants to",
                                                new GameCharacter("wolf")));
                cards
                                .add(new Card("UndercoverDrone", 0, 4, "Regular", 34, 28, 1, 0,
                                                "A stleathy drone that suddenly appears, strikes and then vanishes",
                                                new GameCharacter("warrior")));
                cards
                                .add(new Card("SpearOfLight", 0, 1, "Regular", 27, 50, 1, 0,
                                                "A Spear that shines when thrown and destroyes all the darkness",
                                                new GameCharacter("panda")));
                cards
                                .add(new Card("FistOfJustice", 0, 3, "Regular", 30, 33, 1, 0,
                                                "This fist came from justice league as a gift",
                                                new GameCharacter("panda")));
                cards.add(new Card("MegaKnight", 0, 5, "Regular", 30, 40, 1, 0,
                                "The bot turns into gigantic knight and attaks with its sword",
                                new GameCharacter("warrior")));
                cards
                                .add(new Card("Sparks", 0, 2, "Regular", 28, 28, 1, 0, "Sparks are over the enemy",
                                                new GameCharacter("dragon")));
                cards
                                .add(new Card("VortexOfPower", 0, 4, "Regular", 29, 40, 1, 0, "giant description",
                                                new GameCharacter("panda")));
                cards
                                .add(new Card("SharpBite", 0, 3, "Regular", 30, 30, 1, 0, "goblin description",
                                                new GameCharacter("wolf")));
                cards
                                .add(new Card("WingSlap", 0, 2, "Regular", 29, 28, 1, 0, "minion description",
                                                new GameCharacter("dragon")));
                cards
                                .add(new Card("MagicSlash", 0, 5, "Regular", 30, 40, 1, 0, "pekka description",
                                                new GameCharacter("dragon")));
                cards.add(
                                new Card("FireBall", 0, 4, "Regular", 45, 36, 1, 0, "hog rider description",
                                                new GameCharacter("dragon")));
                cards.add(
                                new Card("Valkyrie", 0, 3, "Regular", 30, 25, 1, 0, "valkyrie description",
                                                new GameCharacter("c1")));
                cards
                                .add(new Card("Witch", 0, 2, "Regular", 25, 20, 1, 0, "witch description",
                                                new GameCharacter("c1")));
                cards.add(new Card("CavalrySupport", 0, 5, "Regular", 32, 30, 1, 0,
                                "lava hound description",
                                new GameCharacter("warrior")));
                cards.add(
                                new Card("PawOfSpeed", 0, 4, "Regular", 27, 28, 1, 0, "balloon description",
                                                new GameCharacter("wolf")));
                cards.add(new Card("LazyAttack", 0, 3, "Regular", 30, 48, 1, 0,
                                "baby dragon description",
                                new GameCharacter("panda")));
                cards.add(
                                new Card("HowlToMoon", 0, 2, "Regular", 29, 50, 1, 0, "giantskeletondescription",
                                                new GameCharacter("wolf")));
                cards.add(new Card("BarbarianAttack", 0, 5, "Regular", 30, 30, 1, 0,
                                "barbarians description",
                                new GameCharacter("warrior")));

                cards.add(new Spell("AddSpace", 0, 0, "Spell", "AddSpace", 0, 0, "Adds unavailalbe block"));

                cards.add(new Spell("RoundReduce", 0, 0, "Spell", "RoundReduce", 0, 0, "Reduces round by one"));
                cards.add(new Spell("Repair", 0, 0, "Spell", "Repair", 0, 0, "Repairs an unavailable block"));

        }
}
