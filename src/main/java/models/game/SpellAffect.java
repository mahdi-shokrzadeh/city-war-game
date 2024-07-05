package models.game;

import java.util.ArrayList;

import models.User;
import models.card.Card;
import models.card.Spell;
import views.console.game.ConsoleGame;

public class SpellAffect {

    private int des_index;
    private Block[][] board;
    private Spell spell_card;
    private int turn_index;
    private User current_player;
    private Round round;
    private ArrayList<Card> hand_cards;

    public SpellAffect(Card spell_card, int turn_index, int des_index,
            Block[][] board, User current_player, Round round, ArrayList<Card> hand_cards) {
        this.des_index = des_index;
        this.board = board;
        this.spell_card = (Spell) spell_card;
        this.turn_index = turn_index;
        this.current_player = current_player;
        this.round = round;
        this.hand_cards = hand_cards;
    }

    public boolean spellHandler() {

        switch (this.spell_card.getSpellType().toString()) {

            case "Shield":
                if (!handlePutSpell()) {
                    return false;
                }
                handleShield();

                break;

            case "Heal":
                if (!handlePutSpell()) {
                    return false;
                }
                handleHeal();
                break;

            case "PowerBoost":
                if (!handlePowerBoost()) {
                    return false;
                }
                break;

            case "SpaceShift":
                handleSpaceShift();
                // returns fasle means no need to handleEffect
                return false;

            case "Repair":
                handleRepair();
                return false;

            case "RoundReduce":
                handleRoundReduce();
                return false;

            case "Attenuate":
                handleAttenuate();
                break;

            case "Copy":
                // handleCopy();
                break;

            default:

                break;

        }

        return true;
    }

    public boolean handlePutSpell() {
        boolean cond = true;
        for (int i = 0; i < this.spell_card.getDuration(); i++) {
            if (!checkBlockIsValidForCard(this.des_index + i)) {
                cond = false;
                break;
            }
        }

        if (cond) {
            for (int i = 0; i < this.spell_card.getDuration(); i++) {
                this.board[this.turn_index][this.des_index + i].setBlockCard(this.spell_card);
                this.board[this.turn_index][this.des_index + i].setBlockEmpty(false);
                // handleAffection(des_index, this.des_index + i);
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean checkBlockIsValidForCard(int block_number) {
        if (block_number < 0 || block_number > 20) {
            ConsoleGame.printInvalidBlockNumber();
            return false;
        }
        Block bl = this.board[this.turn_index][block_number];

        if (bl.isBlockUnavailable()) {
            ConsoleGame.printBlockIsUnavailable();
            return false;
        } else if (!bl.isBlockEmpty()) {
            ConsoleGame.printBlockIsNotEmpty();
            return false;
        }
        return true;
    }

    public void handleShield() {
        this.board[this.turn_index][this.des_index].setBlockPower(999);
        this.board[this.turn_index][this.des_index].setBlockDamage(0);
        this.board[this.turn_index][this.des_index].setBlockEmpty(false);
    }

    public void handleHeal() {
        int added_hit_points = 100;
        this.board[this.turn_index][this.des_index].setBlockPower(999);
        this.board[this.turn_index][this.des_index].setBlockDamage(0);
        this.board[this.turn_index][this.des_index].setBlockEmpty(false);
        this.current_player.setHitPoints(this.current_player.getHitPoints() + added_hit_points);
    }

    public boolean handlePowerBoost() {
        // randomly choose a card from user board
        Card found_card = null;
        boolean found = false;
        for (int i = 0; i <= 20; i++) {
            if (!this.board[this.turn_index][i].isBlockEmpty()) {
                if (this.board[this.turn_index][i].getBlockCard().getCardType().toString().equals("Regular")) {
                    found_card = this.board[this.turn_index][i].getBlockCard();
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            ConsoleGame.printNoRegularCard();
            return false;
        } else {
            found_card.setPower(found_card.getPower() + 10);
            ConsoleGame.printPowerBoostSuccess(found_card);
        }

        return true;
    }

    public void handleSpaceShift() {

        int block_number_player_one = findEmptyBlock(0);
        int block_number_player_two = findEmptyBlock(1);
        if (block_number_player_one == -1 || block_number_player_two == -1) {
            ConsoleGame.printNoEmptyBlock();
            return;
        }

        int unavailable_block_player_one = findUnavailableBlock(0);
        int unavailable_block_player_two = findUnavailableBlock(1);

        // swap the blocks
        this.board[0][block_number_player_one].setBlockUnavailable(true);
        this.board[0][unavailable_block_player_one].setBlockUnavailable(false);
        this.board[0][unavailable_block_player_one].setBlockEmpty(true);

        this.board[1][block_number_player_two].setBlockUnavailable(true);
        this.board[1][unavailable_block_player_two].setBlockUnavailable(false);
        this.board[1][unavailable_block_player_two].setBlockEmpty(true);

        ConsoleGame.printSuccessSpaceShift();

    }

    public int findEmptyBlock(int turn_index) {

        for (int i = 0; i <= 20; i++) {
            if (this.board[turn_index][i].isBlockEmpty() && !this.board[turn_index][i].isBlockUnavailable()) {
                return i;
            }
        }

        return -1;
    }

    public int findUnavailableBlock(int turn_index) {

        for (int i = 0; i <= 20; i++) {
            if (!this.board[turn_index][i].isBlockEmpty() && this.board[turn_index][i].isBlockUnavailable()) {
                return i;
            }
        }

        return -1;
    }

    public void handleRepair() {
        // find the unavailable block
        int unavailable_block_index = findUnavailableBlock(this.turn_index);
        if (unavailable_block_index == -1) {
            ConsoleGame.printNoUnavailableBlock();
            return;
        }

        this.board[this.turn_index][unavailable_block_index].setBlockUnavailable(false);
        ConsoleGame.printSuccessRepair();
    }

    public void handleRoundReduce() {
        this.round.setNumberOfRoundTurns(this.round.getNumberOfRoundTurns() - 1);
        ConsoleGame.printSuccessfulTurnReduce();
    }

    public void handleAttenuate() {
        // randomly choose two reqular cards from opponent
        Card found_card_one = null;
        Card found_card_two = null;

        boolean found = false;
        for (int i = 0; i <= 20; i++) {
            if (!this.board[(this.turn_index + 1) % 2][i].isBlockEmpty()) {
                if (this.board[(this.turn_index + 1) % 2][i].getBlockCard().getCardType().toString()
                        .equals("Regular")) {
                    if (!found) {
                        found_card_one = this.board[(this.turn_index + 1)][i].getBlockCard();
                        found = true;
                    } else {
                        found_card_two = this.board[(this.turn_index + 1)][i].getBlockCard();
                        break;
                    }
                }
            }
        }

        if (found_card_one == null || found_card_two == null) {
            ConsoleGame.printNoRegularCardForAttenuate();
            return;
        }

        for (int i = 0; i <= 20; i++) {
            Block op_block = this.board[(this.turn_index + 1) % 2][i];
            if (op_block.getBlockCard().equals(found_card_one)) {
                op_block.setBlockPower(op_block.getBlockPower() - 1);
            } else if (op_block.getBlockCard().equals(found_card_two)) {
                op_block.setBlockDamage(op_block.getBlockDamage() - 10);
            }
            if (this.turn_index == 0) {
                round.getPlayer_two().setDamage(round.getPlayer_two().getDamage() - 10 * found_card_two.getDuration());
            } else {
                round.getPlayer_one().setDamage(round.getPlayer_one().getDamage() - 10 * found_card_two.getDuration());
            }
        }
        ConsoleGame.printAttenuateSuccess(found_card_one, found_card_two);
    }

    public void handleCopy() {
        try {
            Card copy_card = this.hand_cards.get(des_index);
            this.hand_cards.add(0, copy_card);
            this.current_player.setIsBonusActive(true);
            ConsoleGame.printCopySuccess(copy_card);
        } catch (Exception e) {
            ConsoleGame.printNoValidCardToCopy();
        }
    }
}
