package models.game;

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

    public SpellAffect(Card spell_card, int turn_index, int des_index, Block[][] board, User current_player) {
        this.des_index = des_index;
        this.board = board;
        this.spell_card = (Spell) spell_card;
        this.turn_index = turn_index;
        this.current_player = current_player;
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
}
