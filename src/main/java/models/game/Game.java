package models.game;

import java.util.ArrayList;
import java.util.Scanner;

import org.example.citywars.Menu;

import models.GameCharacter;
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
    private String reward;
    private int number_of_rounds;

    // Only Class Vars
    private int bet_amount;
    private ArrayList<Round> rounds = new ArrayList<Round>();
    private User player_one;
    private User player_two;

    private ArrayList<Card> player_one_cards = new ArrayList<Card>();
    private ArrayList<Card> player_two_cards = new ArrayList<Card>();

    private Round current_round;

    public Game(User player_one, User player_two, String mode) {
        super("GameProcess");
        this.player_one = player_one;
        this.player_two = player_two;
        this.mode = mode;
        this.created_at = new java.util.Date().toString();

        rounds.add(new Round(player_one, player_two, player_one_cards, player_two_cards));
        this.current_round = rounds.get(0);

        ConsoleGame.printGreetings();

        switch (mode) {

            case "duel":
                // this.handleUserTwoLogin
                // this.handleChooseCharacter
                // this.startGame();
                this.handleAddCardsToPlayers();
                break;

            case "normal":

                break;

            default:

                break;
        }

    }

    public Game(User player_one, User player_two, String mode, String clan) {

    }

    @Override
    public Menu myMethods() {
        String input = consoleScanner.nextLine();
        if (input.equals("-Select character")) {
            this.handleChooseCharacter(this.player_one);
            this.handleChooseCharacter(this.player_two);
        } else if (input.equals("-Start game")) {
            this.startGame();
        }

        Menu temp_menu = this;
        return temp_menu;
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
    }

    public void startGame() {
        // if (this.player_one.getGameCharacter() == null || this.player_two.getGameCharacter() == null) {
        //     System.out.println("Please select characters for both players");
        //     return;
        // }

        boolean con = true;
        while (con) {
            String result = this.current_round.processRound();

            switch (result) {
                case "game_is_finished":
                    con = false;
                    break;

                case "new_round":
                    this.rounds.add(new Round(this.player_one, this.player_two,
                            this.player_one_cards, this.player_two_cards));
                    this.current_round = this.rounds.get(this.rounds.size() - 1);
                    break;
                default:
                    break;
            }
        }
    }

    public void handleAddCardsToPlayers() {

        player_one_cards.add(new Card("Fire", 0, 1, "Regular", 20, 15, 1, 0, "desc1", new GameCharacter("c1")));
        // add sample cards with different behaviors
        player_one_cards.add(new Card("Water", 0, 4, "Regular", 45, 35, 1, 0, "water description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Earth", 0, 3, "Regular", 30, 25, 1, 0, "earth description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Air", 0, 2, "Regular", 25, 20, 1, 0, "air description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Fire324", 0, 1, "Regular", 20, 15, 1, 0, "fire description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Waterdsff", 0, 3, "Regular", 55, 15, 1, 0, "water description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Dragon", 0, 5, "Regular", 70, 50, 1, 0, "dragon description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Wizard", 0, 4, "Regular", 45, 35, 1, 0, "wizard description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Knight", 0, 3, "Regular", 30, 25, 1, 0, "knight description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Archer", 0, 2, "Regular", 25, 20, 1, 0, "archer description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Mega Knight", 0, 5, "Regular", 70, 50, 1, 0, "mega knight description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Sparky", 0, 6, "Regular", 100, 70, 1, 0, "sparky description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Giant", 0, 4, "Regular", 45, 35, 1, 0, "giant description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Goblin", 0, 3, "Regular", 30, 25, 1, 0, "goblin description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Minion", 0, 2, "Regular", 25, 20, 1, 0, "minion description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Pekka", 0, 5, "Regular", 70, 50, 1, 0, "pekka description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Hog Rider", 0, 4, "Regular", 45, 35, 1, 0, "hog rider description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Valkyrie", 0, 3, "Regular", 30, 25, 1, 0, "valkyrie description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Witch", 0, 2, "Regular", 25, 20, 1, 0, "witch description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Lava Hound", 0, 5, "Regular", 70, 50, 1, 0, "lava hound description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Balloon", 0, 4, "Regular", 45, 35, 1, 0, "balloon description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Baby Dragon", 0, 3, "Regular", 30, 25, 1, 0, "baby dragon description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Giant Skeleton", 0, 2, "Regular", 25, 20, 1, 0, "giant skeleton description", new GameCharacter("c1")));
        player_one_cards.add(new Card("Barbarians", 0, 5, "Regular", 70, 50, 1, 0, "barbarians description", new GameCharacter("c1")));
        // add for player two
        player_two_cards.add(new Card("Fire", 0, 1, "Regular", 20, 15, 1, 0, "fire description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Water", 0, 4, "Regular", 45, 35, 1, 0, "water description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Earth", 0, 3, "Regular", 30, 25, 1, 0, "earth description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Air", 0, 2, "Regular", 25, 20, 1, 0, "air description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Fire324", 0, 1, "Regular", 20, 15, 1, 0, "fire description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Waterdsff", 0, 3, "Regular", 55, 15, 1, 0, "water description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Dragon", 0, 5, "Regular", 70, 50, 1, 0, "dragon description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Wizard", 0, 4, "Regular", 45, 35, 1, 0, "wizard description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Knight", 0, 3, "Regular", 30, 25, 1, 0, "knight description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Archer", 0, 2, "Regular", 25, 20, 1, 0, "archer description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Mega Knight", 0, 5, "Regular", 70, 50, 1, 0, "mega knight description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Sparky", 0, 6, "Regular", 100, 70, 1, 0, "sparky description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Giant", 0, 4, "Regular", 45, 35, 1, 0, "giant description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Goblin", 0, 3, "Regular", 30, 25, 1, 0, "goblin description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Minion", 0, 2, "Regular", 25, 20, 1, 0, "minion description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Pekka", 0, 5, "Regular", 70, 50, 1, 0, "pekka description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Hog Rider", 0, 4, "Regular", 45, 35, 1, 0, "hog rider description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Valkyrie", 0, 3, "Regular", 30, 25, 1, 0, "valkyrie description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Witch", 0, 2, "Regular", 25, 20, 1, 0, "witch description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Lava Hound", 0, 5, "Regular", 70, 50, 1, 0, "lava hound description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Balloon", 0, 4, "Regular", 45, 35, 1, 0, "balloon description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Baby Dragon", 0, 3, "Regular", 30, 25, 1, 0, "baby dragon description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Giant Skeleton", 0, 2, "Regular", 25, 20, 1, 0, "giant skeleton description", new GameCharacter("c1")));
        player_two_cards.add(new Card("Barbarians", 0, 5, "Regular", 70, 50, 1, 0, "barbarians description", new GameCharacter("c1")));

        

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

}