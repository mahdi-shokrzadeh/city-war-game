package models.game;

import java.util.ArrayList;
import java.util.Collections;

import com.almasb.fxgl.dev.Console;

import models.User;
import models.card.Card;
import views.console.game.ConsoleGame;

public class Round {
    private User player_one;
    private User player_two;
    private boolean game_is_finished = false;
    private String winner;
    private ArrayList<Turn> turns = new ArrayList<Turn>();
    private Turn current_turn;

    private ArrayList<Card> player_one_cards = new ArrayList<Card>();
    private ArrayList<Card> player_two_cards = new ArrayList<Card>();

    private Block[][] board = new Block[2][21];

    public Round(User player_one, User player_two, ArrayList<Card> player_one_cards, ArrayList<Card> player_two_cards) {
        turns.add(new Turn(player_one, player_two, player_one_cards, player_two_cards, board));
        this.current_turn = turns.get(0);
        this.player_one = player_one;
        this.player_two = player_two;

        this.player_one_cards = player_one_cards;
        this.player_two_cards = player_two_cards;
        Collections.shuffle(player_one_cards);
        Collections.shuffle(player_two_cards);

        // fill the board with block
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 21; j++) {
                board[i][j] = new Block();
            }
        }

    }

    public String processRound() {

        ConsoleGame.printRoundStart();
        boolean con = true;
        while (con) {
            String result = current_turn.processTurn(board,
                    this);
            if (result.equals("turn_is_finished")) {
                if (this.turns.size() < 6) {
                    this.turns.add(new Turn(player_one, player_two, player_one_cards, player_two_cards, board));
                    this.current_turn = turns.get(turns.size() - 1);
                } else {
                    System.out.println("HERE!!");
                    // timeLine();
                    if (this.timeLine()) {
                        return "game_is_finished";
                    } else {
                        con = false;
                    }
                }
            }
        }

        return "need_more_rounds";
    }

    // getters and setters
    public User getPlayer_one() {
        return player_one;
    }

    public void setPlayer_one(User player_one) {
        this.player_one = player_one;
    }

    public User getPlayer_two() {
        return player_two;
    }

    public void setPlayer_two(User player_two) {
        this.player_two = player_two;
    }

    public boolean isGame_is_finished() {
        return game_is_finished;
    }

    public void setGame_is_finished(boolean game_is_finished) {
        this.game_is_finished = game_is_finished;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }

    public void setTurns(ArrayList<Turn> turns) {
        this.turns = turns;
    }

    public Turn getCurrent_turn() {
        return current_turn;
    }

    public void setCurrent_turn(Turn current_turn) {
        this.current_turn = current_turn;
    }

    public Block[][] getBoard() {
        return board;
    }

    public void setBoard(Block[][] board) {
        this.board = board;
    }

    public boolean timeLine() {
        System.out.println("HEY here!");
        for (int i = 0; i <= 20; i++) {

            Block player_one_block = this.board[0][i];
            Block player_two_block = this.board[1][i];

            if (player_one_block.isBlockEmpty() && player_two_block.isBlockEmpty()) {

            } else if (player_one_block.isBlockEmpty() && !player_two_block.isBlockEmpty()) {
                this.player_one
                        .setHitPoints(this.player_one.getHitPoints() - player_two_block.getBlockDamage());
                // reduce the damage of the player
                this.player_two.setDamage(this.player_two.getDamage() - player_two_block.getBlockDamage());
            } else if (!player_one_block.isBlockEmpty() && player_two_block.isBlockEmpty()) {
                this.player_two
                        .setHitPoints(this.player_two.getHitPoints() - player_one_block.getBlockDamage());

                // reduce the damage of the player
                this.player_one.setDamage(this.player_one.getDamage() - player_one_block.getBlockDamage());
            } else {
                if (player_one_block.getBlockPower() > player_two_block.getBlockPower()) {
                    this.player_two
                            .setHitPoints(this.player_two.getHitPoints() - player_one_block.getBlockDamage());
                    this.player_one
                            .setDamage(this.player_one.getDamage() - player_one_block.getBlockDamage());

                } else if (player_one_block.getBlockPower() < player_two_block.getBlockPower()) {
                    this.player_one
                            .setHitPoints(this.player_one.getHitPoints() - player_two_block.getBlockDamage());
                    this.player_two
                            .setDamage(this.player_two.getDamage() - player_two_block.getBlockDamage());
                }

            }
            ConsoleGame.printBlockIndex(i + 1);
            ConsoleGame.printBlocksStatus(player_one_block, player_two_block);
            ConsoleGame.printDamageStatus(this.player_one, this.player_two);
            ConsoleGame.printHPStatus(this.player_one, this.player_two);

            if (this.checkGameIsFinished()) {
                return true;
            }

        }

        return false;
    }

    public boolean checkGameIsFinished() {
        if (this.player_one.getHitPoints() <= 0) {
            this.game_is_finished = true;
            this.winner = this.player_two.getUsername();
            return true;
        } else if (this.player_two.getHitPoints() <= 0) {
            this.game_is_finished = true;
            this.winner = this.player_one.getUsername();
            return true;
        }
        return false;
    }

}
