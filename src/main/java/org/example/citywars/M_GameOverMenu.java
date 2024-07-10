package org.example.citywars;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controllers.CardController;
import controllers.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import models.GameCharacter;
import models.Response;
import models.User;
import models.card.Card;
import models.game.SimpleGame;
import views.console.game.ConsoleGame;

public class M_GameOverMenu extends Menu {

    @FXML
    private Pane pane;
    @FXML
    private Pane winnerPane;
    @FXML
    private Pane loserPane;

    private SimpleGame game;
    private String reward;
    private User winner;

    // private String looser_reward;
    // private String winner_reward;

    public M_GameOverMenu() {
        super("M_GameOverMenu");
    }

    // public M_GameOverMenu(String wr, String lr) {
    // super("M_GameOverMenu");
    // // winner_reward = wr;
    // // looser_reward = lr;
    // }

    public M_GameOverMenu(User winner, String winner_reward, String looser_reward) {
        super("M_GameOverMenu");
        this.winner = winner;
        // this.looser_reward = looser_reward;
        // this.winner_reward = winner_reward;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String winnerRewardRaw = winner_reward;// "Flank: +1 level / User: +135 experience +2 level +25
        //                                        // hitpoints +50 coins"; // game.getwinnerreward
        String loserRewardRaw = looser_reward;// "Flank: +1 level / User: +135 experience +2 level +25 hitpoints
                                              // +50 coins"; // game.getloserreward
        String winnerReward = "";
        String loserReward = "";
        
        Matcher _matcher = null;

        _matcher = Pattern.compile("\\+[0-9]+ experience").matcher(winnerRewardRaw);
        if (_matcher.find()) {
            winnerReward += _matcher.group() + "\n";
        }
        _matcher = Pattern.compile("\\+[0-9]+ level").matcher(winnerRewardRaw);
        if (_matcher.find()) {
            winnerReward += _matcher.group() + "\n";
        }
        _matcher = Pattern.compile("\\+[0-9]+ hitpoints").matcher(winnerRewardRaw);
        if (_matcher.find()) {
            winnerReward += _matcher.group() + "\n";
        }
        _matcher = Pattern.compile("\\+[0-9]+ coins").matcher(winnerRewardRaw);
        if (_matcher.find()) {
            winnerReward += _matcher.group() + "\n";
        }
        winnerReward += "\n";
        _matcher = Pattern.compile("[a-zA-Z]+: \\+[0-9]+ level ").matcher(winnerRewardRaw);
        while (_matcher.find()) {
            winnerReward += _matcher.group() + "\n";
        }

        _matcher = Pattern.compile("\\+[0-9]+ experience").matcher(loserRewardRaw);
        if (_matcher.find()) {
            loserReward += _matcher.group() + "\n";
        }
        _matcher = Pattern.compile("\\+[0-9]+ level").matcher(loserRewardRaw);
        if (_matcher.find()) {
            loserReward += _matcher.group() + "\n";
        }
        _matcher = Pattern.compile("\\+[0-9]+ coins").matcher(loserReward);
        if (_matcher.find()) {
            loserReward += _matcher.group() + "\n";
        }
        loserReward += "\n";
        _matcher = Pattern.compile("[a-zA-Z]+: \\+[0-9]+ level ").matcher(loserRewardRaw);
        while (_matcher.find()) {
            loserReward += _matcher.group() + "\n";
        }

        Text winnerRewardTitle = new Text("Winner reward: ");
        winnerRewardTitle.setFont(Font.font(40));
        winnerRewardTitle.setFill(Paint.valueOf("white"));
        winnerRewardTitle.setTranslateX(13);
        winnerRewardTitle.setTranslateY(50);
        winnerPane.getChildren().add(winnerRewardTitle);
        Text winnerRewardText = new Text(winnerReward);
        winnerRewardText.setFont(Font.font(33));
        winnerRewardText.setTranslateX(20);
        winnerRewardText.setTranslateY(150);
        winnerRewardText.setFill(Paint.valueOf("white"));
        winnerPane.getChildren().add(winnerRewardText);
        Text loserRewardTitle = new Text("Winner reward: ");
        loserRewardTitle.setFont(Font.font(40));
        loserRewardTitle.setFill(Paint.valueOf("white"));
        loserRewardTitle.setTranslateX(13);
        loserRewardTitle.setTranslateY(50);
        loserPane.getChildren().addAll(loserRewardTitle);
        Text loserRewardText = new Text(loserReward);
        loserRewardText.setFont(Font.font(33));
        loserRewardText.setTranslateX(20);
        loserRewardText.setTranslateY(150);
        loserRewardText.setFill(Paint.valueOf("white"));
        loserPane.getChildren().add(loserRewardText);


        System.out.println("w: " + winnerRewardRaw);
        System.out.println("l: " + loserReward);
    }
}
