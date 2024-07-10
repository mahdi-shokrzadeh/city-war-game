package org.example.citywars;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import models.game.Round;

import controllers.GameCharacterController;
import controllers.UserCardsController;
import controllers.game.GameController;
import models.AI;
import models.Clan;
import models.ClanBattle;
import models.GameCharacter;
import models.Response;
import models.User;
import models.card.Card;
import models.game.AddCard;
import views.console.game.ConsoleGame;

public class M_Game extends Menu {

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
        static int bet_amount;
        private ArrayList<Round> rounds = new ArrayList<Round>();
        private User player_one;
        private User player_two;

        private ArrayList<Card> player_one_cards = new ArrayList<Card>();
        private ArrayList<Card> player_two_cards = new ArrayList<Card>();

        private Round current_round;
        private ClanBattle battle;
        private Clan attackerClan;
        private Clan defenderClan;

        @FXML
        ImageView timeLineWalker;

        public M_Game() {
                super("M_Game", new String[] { "BG-Videos\\GameBGs\\bg1.png", "BG-Videos\\GameBGs\\bg2.png",
                                "BG-Videos\\GameBGs\\bg3.png" });
                this.player_one = loggedInUser;
                if (secondPersonNeeded) {
                        this.player_two = secondUser;
                        if (is_bet) {
                                mode = "bet";
                        } else {
                                mode = "duel";
                        }
                } else
                        mode = "AI";
        }

        public M_Game(User player_one, User player_two, String mode) {
                super("M_Game");
                this.player_one = player_one;
                this.player_two = player_two;
                this.mode = mode;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime ldt = LocalDateTime.now();
                this.created_at = ldt.format(formatter);

                rounds.add(new Round(player_one, player_two, player_one_cards, player_two_cards));
                this.current_round = rounds.get(0);
                this.player_one_id = player_one.getID();
                this.player_two_id = player_two.getID();

                ConsoleGame.printGreetings();

                switch (mode) {

                        case "duel":
                                // this.handleAddCardsToPlayers();
                                break;

                        case "AI":

                                // this.handleAddCardsToPlayers();
                                break;

                        case "bet":
                                // this.handleAddCardsToPlayers();
                                this.handleGetBetAmount();
                                break;

                        default:
                                break;
                }

        }

        public M_Game(User player_one, User player_two, String mode, ClanBattle battle,
                        Clan attackerClan, Clan defenderClan) {
                super("M_Game");
                this.player_one = player_one;
                this.player_two = player_two;
                this.mode = mode;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime ldt = LocalDateTime.now();
                this.created_at = ldt.format(formatter);

                rounds.add(new Round(player_one, player_two, player_one_cards, player_two_cards));
                this.current_round = rounds.get(0);
                this.player_one_id = player_one.getID();
                this.player_two_id = player_two.getID();
                this.battle = battle;
                this.defenderClan = defenderClan;
                this.attackerClan = attackerClan;
                ConsoleGame.printGreetings();
                // this.handleAddCardsToPlayers();
        }

        // not related to me :)
        public M_Game(User player_one, String mode) {
                super("M_Game");
                this.player_one = player_one;
                this.mode = mode;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime ldt = LocalDateTime.now();
                this.created_at = ldt.format(formatter);

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
        public void initialize(URL url, ResourceBundle resourceBundle) {
                Random random = new Random();
                int i = random.nextInt(BGims.size());
                backGroundIm.setImage(BGims.get(i));
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

                ArrayList<GameCharacter> characters = new ArrayList<GameCharacter>();
                Response res = GameCharacterController.getAll();
                if (res.ok) {
                        Object obj = res.body.get("gameCharacters");
                        if (obj instanceof ArrayList<?>) {
                                for (Object o : (ArrayList<?>) obj) {
                                        if (o instanceof GameCharacter) {
                                                characters.add((GameCharacter) o);
                                        }
                                }
                        }
                } else {
                        System.out.println(res.message);
                }

                ConsoleGame.printCharacterMenu(player.getNickname(), characters);
                boolean cond = false;
                Scanner sc = new Scanner(System.in);
                String in = "";
                while (!cond) {
                        in = sc.nextLine();
                        if (in.matches("[1-6]")) {
                                cond = true;
                                try {
                                        player.setGameCharacter(characters.get(Integer.parseInt(in) - 1));
                                } catch (Exception e) {
                                        System.out.println("Please enter a valid number between 1 to "
                                                        + characters.size());
                                }
                        } else {
                                System.out.println("Please enter a valid number between 1 to " + characters.size());
                        }
                }
                ConsoleGame.printSuccessfulcharacterChoice(characters.get(Integer.parseInt(in) - 1).getName());

        }

        public boolean startGame() {
                if (((this.mode.equals("duel") || this.mode.equals("clan"))
                                && (this.player_one.getGameCharacter() == null ||
                                                this.player_two.getGameCharacter() == null))
                                || (this.player_two.getGameCharacter() == null)) {
                        System.out.println("Please select character first!");
                        return false;
                } else if (this.mode.equals("bet") && this.bet_amount == 0) {
                        ConsoleGame.printBetNotSet();
                        return false;
                }
                this.handleAddCardsToPlayers();

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
                                res = GameController.createGame(this, player_one, player_two, this.rounds.size(),
                                                w, this.player_one_cards, this.player_two_cards);
                                if (res.ok) {
                                        this.winner_reward = (String) res.body.get("winner");
                                        this.looser_reward = (String) res.body.get("loser");
                                } else {
                                        System.out.println(res.message);
                                }
                                break;

                        case "AI":
                                res = GameController.createBotGame(this, player_two, this.rounds.size(), w,
                                                player_two_cards);
                                if (res.ok) {
                                        this.winner_reward = (String) res.body.get("winner");
                                        this.looser_reward = (String) res.body.get("loser");
                                } else {
                                        System.out.println(res.message);
                                }
                                break;

                        case "bet":
                                res = GameController.createGambleGame(this, player_one, player_two, this.rounds.size(),
                                                w,
                                                this.bet_amount);
                                if (res.ok) {
                                        this.winner_reward = (String) res.body.get("winner");
                                        this.looser_reward = (String) res.body.get("loser");
                                } else {
                                        System.out.println(res.message);
                                }
                                break;

                        case "clan":
                                res = GameController.creatGameClan(this, this.battle, this.attackerClan,
                                                this.defenderClan,
                                                player_one, player_two, this.rounds.size(),
                                                w, this.player_one_cards, this.player_two_cards);
                                if (res.ok) {
                                        this.winner_reward = (String) res.body.get("winner");
                                        this.looser_reward = (String) res.body.get("loser");
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
                if (this.mode.equals("AI")) {
                        AddCard.addCard(this.player_one_cards);
                }

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

                // public Spell(String _name, int _price, int _duration, String _type, String
                // _spellType, int _upgradeLevel,
                // int _upgradeCost, String _desc) {

                // Shield,
                // Heal,
                // PowerBoost,
                // SpaceShift,
                // Repair,
                // RoundReduce,
                // Steal,
                // Attenuate,
                // Copy,
                // Hide;
                // player_two_cards.add(new Spell("Shield", 0, 1, "Spell", "Shield", 1, 0,
                // "shield description"));
                // player_two_cards.add(new Spell("Heal", 0, 1, "Spell", "Heal", 1, 0, "heal
                // description"));
                // player_two_cards.add(
                // new Spell("PowerBoost", 0, 1, "Spell", "PowerBoost", 1, 0, "power boost
                // description"));
                // player_two_cards.add(
                // new Spell("SpaceShift", 0, 1, "Spell", "SpaceShift", 1, 0, "space shift
                // description"));
                // player_two_cards.add(new Spell("Repair", 0, 1, "Spell", "Repair", 1, 0,
                // "repair description"));
                // player_two_cards.add(new Spell("RoundReduce", 0, 1, "Spell", "RoundReduce",
                // 1, 0,
                // "round reduce description"));
                // player_two_cards.add(new Spell("Steal", 0, 1, "Spell", "Steal", 1, 0, "steal
                // description"));
                // player_two_cards.add(new Spell("Attenuate", 0, 1, "Spell", "Attenuate", 1, 0,
                // "attenuate description"));
                // player_two_cards.add(new Spell("Copy", 0, 1, "Spell", "Copy", 1, 0, "copy
                // description"));
                // player_two_cards.add(new Spell("Hide", 0, 1, "Spell", "Hide", 1, 0, "hide
                // description"));

                // from database
                Response res_1 = UserCardsController.getUsersCards(this.player_one);
                if (res_1.ok) {
                        Object obj = res_1.body.get("cards");
                        if (obj instanceof ArrayList<?>) {
                                for (Object o : (ArrayList<?>) obj) {
                                        if (o instanceof Card) {
                                                this.player_one_cards.add((Card) o);
                                        }
                                }
                        }
                        Collections.shuffle(player_one_cards);

                } else {
                        System.out.println(res_1.exception.getMessage());
                }

                Response res_2 = UserCardsController.getUsersCards(this.player_two);
                if (res_2.ok) {
                        Object obj = res_2.body.get("cards");
                        if (obj instanceof ArrayList<?>) {
                                for (Object o : (ArrayList<?>) obj) {
                                        if (o instanceof Card) {
                                                this.player_two_cards.add((Card) o);
                                        }
                                }
                        }
                        Collections.shuffle(player_two_cards);

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

        public String getWinnerReward() {
                return winner_reward;
        }

        public void setWinnerReward(String r) {
                winner_reward = r;
        }

        public String getLoserReward() {
                return looser_reward;
        }

        public void setLoserReward(String r) {
                looser_reward = r;
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
                Scanner scanner = new Scanner(System.in);
                boolean cond = false;
                System.out.println("Please enter the bet amount:");
                while (!cond) {
                        String inputt = scanner.nextLine();
                        if (inputt.matches("[0-9]+")) {
                                int b = Integer.parseInt(inputt);
                                this.bet_amount = b;
                                if (this.player_one.getCoins() < b
                                                || this.player_two.getCoins() < b) {
                                        System.out.println(
                                                        "One of the players does not have enough coins to bet this amount"
                                                                        + "\n" +
                                                                        "Please enter a valid amount");

                                } else {
                                        cond = true;
                                        this.bet_amount = Integer.parseInt(inputt);
                                        ConsoleGame.printSuccessfulBetSet(b);
                                }
                        } else {
                                System.out.println("Please enter a valid number");
                        }
                }
        }

}