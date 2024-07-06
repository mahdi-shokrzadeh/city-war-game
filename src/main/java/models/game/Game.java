package models.game;

import java.util.ArrayList;
import java.util.Scanner;

import org.example.citywars.M_GameOverMenu;
import org.example.citywars.M_Intro;
import org.example.citywars.Menu;

import controllers.CardController;
import controllers.UserCardsController;
import controllers.game.GameController;
import models.AI;
import models.GameCharacter;
import models.Response;
import models.User;
import models.card.Card;
import views.console.game.ConsoleGame;

public class Game extends Menu {
        private int id;
        private int player_one_id;
        private int player_two_id;
        private String mode;
        private String created_at;
        private String ended_at;
        private String winner;
        private User winner_user;
        private String reward;
        private int number_of_rounds;
        private String winner_reward;
        private String looser_reward;

        // Only Class Vars
        private int bet_amount;
        private ArrayList<Round> rounds = new ArrayList<Round>();
        private User player_one;
        private User player_two;

        private ArrayList<Card> player_one_cards = new ArrayList<Card>();
        private ArrayList<Card> player_two_cards = new ArrayList<Card>();

        private Round current_round;

        public Game() {
        }

        public Game(User player_one, User player_two, String mode) {
                super("GameProcess");
                this.player_one = player_one;
                this.player_two = player_two;
                this.mode = mode;
                this.created_at = new java.util.Date().toString();

                rounds.add(new Round(player_one, player_two, player_one_cards, player_two_cards));
                this.current_round = rounds.get(0);
                this.player_one_id = player_one.getID();
                this.player_two_id = player_two.getID();

                ConsoleGame.printGreetings();

                switch (mode) {

                        case "duel":
                                this.handleAddCardsToPlayers();
                                break;

                        case "AI":

                                this.handleAddCardsToPlayers();
                                break;

                        case "bet":
                                this.handleAddCardsToPlayers();
                                this.handleGetBetAmount();
                                break;

                        default:
                                break;
                }

        }

        // not related to me :)
        public Game(User player_one, String mode) {
                super("GameProcess");
                this.player_one = player_one;
                this.mode = mode;
                this.created_at = new java.util.Date().toString();

                // rounds.add(new Round(player_one, player_two, player_one_cards,
                // player_two_cards));
                // this.current_round = rounds.get(0);

                // ConsoleGame.printGreetings();

                // switch (mode) {

                // case "duel":

                // this.handleAddCardsToPlayers();
                // break;

                // case "AI":

                // this.handleAddCardsToPlayers();
                // break;

                // case "bet":
                // this.handleAddCardsToPlayers();
                // break;

                // default:

                // break;
                // }
        }

        @Override
        public Menu myMethods() {
                String input = consoleScanner.nextLine();
                if (input.equals("select character")) {
                        if (!(player_one instanceof AI)) {
                                this.handleChooseCharacter(this.player_one);
                        }

                        this.handleChooseCharacter(this.player_two);

                } else if (input.equals("set bet amount"))

                {
                        if (this.mode.equals("bet")) {
                                this.handleGetBetAmount();
                        } else {
                                System.out.println("This mode does not require a bet amount");
                        }
                }

                else if (input.equals("start game")) {
                        if (this.startGame()) {
                                // handle game over
                                Menu menu = new M_GameOverMenu(this.winner_user, this.winner_reward,
                                                this.looser_reward);
                                return menu;
                        } else {

                                return this;
                        }
                } else {
                        ConsoleGame.printInvaidInput();
                }

                return this;
        }
        // handlers

        public void handleUserTwoLogin() {

        }

        public void handleChooseCharacter(User player) {
                ConsoleGame.printCharacterMenu(player.getNickname());
                boolean cond = false;
                Scanner sc = new Scanner(System.in);
                while (!cond) {
                        String input = sc.nextLine();
                        if (input.matches("[1-3]")) {
                                cond = true;
                                switch (input) {
                                        case "1":
                                                player.setGameCharacter(new GameCharacter("1"));
                                                cond = true;
                                                break;
                                        case "2":
                                                player.setGameCharacter(new GameCharacter("2"));
                                                cond = true;
                                                break;
                                        case "3":
                                                player.setGameCharacter(new GameCharacter("3"));
                                                cond = true;
                                                break;

                                        default:
                                                break;
                                }
                        } else {
                                System.out.println("Please enter a valid number between 1 to 3");
                        }
                }

                ConsoleGame.printSuccessfulcharacterChoice("");

        }

        public boolean startGame() {
                if ((this.mode.equals("duel") && (this.player_one.getGameCharacter() == null ||
                                this.player_two.getGameCharacter() == null))
                                || (this.player_two.getGameCharacter() == null)) {
                        System.out.println("Please select character first!");
                        return false;
                } else if (this.mode.equals("bet") && this.bet_amount == 0) {
                        ConsoleGame.printBetNotSet();
                        return false;
                }

                boolean con = true;
                while (con) {
                        String result = this.current_round.processRound();
                        switch (result) {
                                case "game_is_finished":
                                        con = false;
                                        break;

                                case "need_more_rounds":
                                        this.rounds.add(new Round(this.player_one, this.player_two,
                                                        this.player_one_cards, this.player_two_cards));
                                        this.current_round = this.rounds.get(this.rounds.size() - 1);
                                        break;
                                default:
                                        break;
                        }
                }
                ConsoleGame.printGameIsFinished();
                this.findWinner();
                // handle rewards

                String w = this.winner.equals(this.player_one.getNickname()) ? "player_one" : "player_two";
                Response res;
                switch (this.mode) {

                        case "duel":
                                res = GameController.createGame(player_one, player_two, this.rounds.size(),
                                                w, this.player_one_cards, this.player_two_cards);
                                if (res.ok) {
                                        this.winner_reward = (String) res.body.get("winner");
                                        this.looser_reward = (String) res.body.get("looser");
                                } else {
                                        System.out.println(res.message);
                                }
                                break;

                        case "AI":
                                res = GameController.createBotGame(player_two, this.rounds.size(), w,
                                                player_two_cards);
                                if (res.ok) {
                                        this.winner_reward = (String) res.body.get("winner");
                                        this.looser_reward = (String) res.body.get("looser");
                                } else {
                                        System.out.println(res.message);
                                }
                                break;

                        case "bet":
                                res = GameController.createGambleGame(player_one, player_two, this.rounds.size(), w,
                                                this.bet_amount);
                                if (res.ok) {
                                        this.winner_reward = (String) res.body.get("winner");
                                        this.looser_reward = (String) res.body.get("looser");
                                } else {
                                        System.out.println(res.message);
                                }
                                break;

                        default:
                                break;
                }

                return true;

        }

        public void handleAddCardsToPlayers() {

                // player_one_cards.add(new Card("Fire", 0, 1, "Regular", 20, 15, 1, 0, "desc1",
                // new GameCharacter("c1")));

                // player_one_cards
                // .add(new Card("Water", 0, 4, "Regular", 45, 35, 1, 0, "water description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Earth", 0, 3, "Regular", 30, 25, 1, 0, "earth description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Air", 0, 2, "Regular", 25, 20, 1, 0, "air description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Fire324", 0, 1, "Regular", 20, 15, 1, 0, "fire description",
                // new GameCharacter("c1")));
                // player_one_cards.add(
                // new Card("Waterdsff", 0, 3, "Regular", 55, 15, 1, 0, "water description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Dragon", 0, 5, "Regular", 70, 50, 1, 0, "dragon description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Wizard", 0, 4, "Regular", 45, 35, 1, 0, "wizard description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Knight", 0, 3, "Regular", 30, 25, 1, 0, "knight description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Archer", 0, 2, "Regular", 25, 20, 1, 0, "archer description",
                // new GameCharacter("c1")));
                // player_one_cards.add(new Card("Mega Knight", 0, 5, "Regular", 70, 50, 1, 0,
                // "mega knight description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Sparky", 0, 6, "Regular", 100, 70, 1, 0, "sparky description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Giant", 0, 4, "Regular", 45, 35, 1, 0, "giant description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Goblin", 0, 3, "Regular", 30, 25, 1, 0, "goblin description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Minion", 0, 2, "Regular", 25, 20, 1, 0, "minion description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Pekka", 0, 5, "Regular", 70, 50, 1, 0, "pekka description",
                // new GameCharacter("c1")));
                // player_one_cards.add(
                // new Card("Hog Rider", 0, 4, "Regular", 45, 35, 1, 0, "hog rider description",
                // new GameCharacter("c1")));
                // player_one_cards.add(
                // new Card("Valkyrie", 0, 3, "Regular", 30, 25, 1, 0, "valkyrie description",
                // new GameCharacter("c1")));
                // player_one_cards
                // .add(new Card("Witch", 0, 2, "Regular", 25, 20, 1, 0, "witch description",
                // new GameCharacter("c1")));
                // player_one_cards.add(new Card("Lava Hound", 0, 5, "Regular", 70, 50, 1, 0,
                // "lava hound description",
                // new GameCharacter("c1")));
                // player_one_cards.add(
                // new Card("Balloon", 0, 4, "Regular", 45, 35, 1, 0, "balloon description",
                // new GameCharacter("c1")));
                // player_one_cards.add(new Card("Baby Dragon", 0, 3, "Regular", 30, 25, 1, 0,
                // "baby dragon description",
                // new GameCharacter("c1")));
                // player_one_cards.add(
                // new Card("Giant Skeleton", 0, 2, "Regular", 25, 20, 1, 0, "giant skeleton
                // description",
                // new GameCharacter("c1")));
                // player_one_cards.add(new Card("Barbarians", 0, 5, "Regular", 70, 50, 1, 0,
                // "barbarians description",
                // new GameCharacter("c1")));

                // player_two_cards
                // .add(new Card("Fire", 0, 1, "Regular", 20, 15, 1, 0, "fire description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Water", 0, 4, "Regular", 45, 35, 1, 0, "water description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Earth", 0, 3, "Regular", 30, 25, 1, 0, "earth description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Air", 0, 2, "Regular", 25, 20, 1, 0, "air description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Fire324", 0, 1, "Regular", 20, 15, 1, 0, "fire description",
                // new GameCharacter("c1")));
                // player_two_cards.add(
                // new Card("Waterdsff", 0, 3, "Regular", 55, 15, 1, 0, "water description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Dragon", 0, 5, "Regular", 70, 50, 1, 0, "dragon description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Wizard", 0, 4, "Regular", 45, 35, 1, 0, "wizard description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Knight", 0, 3, "Regular", 30, 25, 1, 0, "knight description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Archer", 0, 2, "Regular", 25, 20, 1, 0, "archer description",
                // new GameCharacter("c1")));
                // player_two_cards.add(new Card("Mega Knight", 0, 5, "Regular", 70, 50, 1, 0,
                // "mega knight description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Sparky", 0, 6, "Regular", 100, 70, 1, 0, "sparky description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Giant", 0, 4, "Regular", 45, 35, 1, 0, "giant description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Goblin", 0, 3, "Regular", 30, 25, 1, 0, "goblin description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Minion", 0, 2, "Regular", 25, 20, 1, 0, "minion description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Pekka", 0, 5, "Regular", 70, 50, 1, 0, "pekka description",
                // new GameCharacter("c1")));
                // player_two_cards.add(
                // new Card("Hog Rider", 0, 4, "Regular", 45, 35, 1, 0, "hog rider description",
                // new GameCharacter("c1")));
                // player_two_cards.add(
                // new Card("Valkyrie", 0, 3, "Regular", 30, 25, 1, 0, "valkyrie description",
                // new GameCharacter("c1")));
                // player_two_cards
                // .add(new Card("Witch", 0, 2, "Regular", 25, 20, 1, 0, "witch description",
                // new GameCharacter("c1")));
                // player_two_cards.add(new Card("Lava Hound", 0, 5, "Regular", 70, 50, 1, 0,
                // "lava hound description",
                // new GameCharacter("c1")));
                // player_two_cards.add(
                // new Card("Balloon", 0, 4, "Regular", 45, 35, 1, 0, "balloon description",
                // new GameCharacter("c1")));
                // player_two_cards.add(new Card("Baby Dragon", 0, 3, "Regular", 30, 25, 1, 0,
                // "baby dragon description",
                // new GameCharacter("c1")));
                // player_two_cards.add(
                // new Card("Giant Skeleton", 0, 2, "Regular", 25, 20, 1, 0, "giant skeleton
                // description",
                // new GameCharacter("c1")));
                // player_two_cards.add(new Card("Barbarians", 0, 5, "Regular", 70, 50, 1, 0,
                // "barbarians description",
                // new GameCharacter("c1")));

                // from database
                Response res_1 = UserCardsController.getUsersCards(this.player_one);
                if (res_1.ok) {
                        // this.player_one_cards = (ArrayList<Card>) res_1.body.get("userCard");
                        Object obj = res_1.body.get("cards");
                        if (obj instanceof ArrayList<?>) {
                                for (Object o : (ArrayList<?>) obj) {
                                        if (o instanceof Card) {
                                                this.player_one_cards.add((Card) o);
                                        }
                                }
                        }
                } else {
                        System.out.println(res_1.message);
                }

                Response res_2 = UserCardsController.getUsersCards(this.player_two);
                if (res_1.ok) {
                        // this.player_two_cards = (ArrayList<Card>) res_2.body.get("userCard");
                        Object obj = res_2.body.get("cards");
                        if (obj instanceof ArrayList<?>) {
                                for (Object o : (ArrayList<?>) obj) {
                                        if (o instanceof Card) {
                                                this.player_two_cards.add((Card) o);
                                        }
                                }
                        }
                } else {
                        System.out.println(res_2.message);
                }

        }

        //

        // getter and setter methods

        public int getID() {
                return id;
        }

        public void setID(int _id) {
                id = _id;
        }

        public int getPlayer_one_id() {
                return player_one_id;
        }

        public void setPlayer_one_id(int player_one_id) {
                this.player_one_id = player_one_id;
        }

        public int getPlayer_two_id() {
                return player_two_id;
        }

        public void setPlayer_two_id(int player_two_id) {
                this.player_two_id = player_two_id;
        }

        public String getMode() {
                return mode;
        }

        public void setMode(String mode) {
                this.mode = mode;
        }

        public String getCreated_at() {
                return created_at;
        }

        public void setCreated_at(String created_at) {
                this.created_at = created_at;
        }

        public String getEnded_at() {
                return ended_at;
        }

        public void setEnded_at(String ended_at) {
                this.ended_at = ended_at;
        }

        public String getWinner() {
                return winner;
        }

        public void setWinner(String winner) {
                this.winner = winner;
        }

        public String getReward() {
                return reward;
        }

        public void setReward(String reward) {
                this.reward = reward;
        }

        public int getNumber_of_rounds() {
                return number_of_rounds;
        }

        public void setNumber_of_rounds(int number_of_rounds) {
                this.number_of_rounds = number_of_rounds;
        }

        public int getBet_amount() {
                return bet_amount;
        }

        public void setBet_amount(int bet_amount) {
                this.bet_amount = bet_amount;
        }

        public ArrayList<Round> getRounds() {
                return rounds;
        }

        public void setRounds(ArrayList<Round> rounds) {
                this.rounds = rounds;
        }

        public void findWinner() {
                if (player_one.getHitPoints() > player_two.getHitPoints()) {
                        this.winner = player_one.getNickname();
                        this.winner_user = player_one;
                        ConsoleGame.printWinner(player_one.getUsername());
                } else {
                        this.winner = player_two.getNickname();
                        this.winner_user = player_two;
                        ConsoleGame.printWinner(player_two.getUsername());
                }
        }

        public void handleGetBetAmount() {
                Scanner sc = new Scanner(System.in);
                boolean cond = false;
                System.out.println("Please enter the bet amount:");
                while (!cond) {
                        String input = sc.nextLine();
                        if (input.matches("[0-9]+")) {

                                if (this.player_one.getCoins() < this.bet_amount
                                                || this.player_two.getCoins() < this.bet_amount) {
                                        System.out.println(
                                                        "One of the players does not have enough coins to bet this amount"
                                                                        + "\n" +
                                                                        "Please enter a valid amount");

                                } else {
                                        cond = true;
                                        this.bet_amount = Integer.parseInt(input);

                                }
                        } else {
                                System.out.println("Please enter a valid number");
                        }
                }
        }

}