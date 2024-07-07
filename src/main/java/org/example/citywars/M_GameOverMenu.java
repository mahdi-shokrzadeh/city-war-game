package org.example.citywars;

import models.User;
import views.console.game.ConsoleGame;

public class M_GameOverMenu extends Menu {

    private String reward;
    private User winner;
    private String looser_reward;
    private String winner_reward;

    public M_GameOverMenu(User winner, String winner_reward, String looser_reward) {
        super("M_GameOverMenu");
        this.winner = winner;
        this.looser_reward = looser_reward;
        this.winner_reward = winner_reward;

        String res = "Winner: " + winner.getNickname() + "\n" +
                "Reward: " + winner_reward + "\n" +
                "Loser rewards:" + "\n" +
                "Reward: " + looser_reward + "\n";
        ConsoleGame.printGameResult(res);
    }

    public Menu myMethods() {
        String input = consoleScanner.nextLine();
        if (input.toLowerCase().equals("back to main menu"))
            return new M_GameMainMenu();

        System.out.println("invalid command!");
        return this;
    }
}
