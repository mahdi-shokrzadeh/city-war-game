package models.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import models.User;
import models.card.Card;
import models.card.Spell;
import views.console.game.ConsoleGame;

public class SpellAffect {

    private int des_index;
    private Block[][] board;
    private Card spell_card;
    private int turn_index;
    private User current_player;
    private Round round;
    private ArrayList<Card> hand_cards;

    public SpellAffect(Card spell_card, int turn_index, int des_index,
            Block[][] board, User current_player, Round round, ArrayList<Card> hand_cards) {
        this.des_index = des_index;
        this.board = board;
        // this.spell_card = (Spell) spell_card;
        this.spell_card = spell_card;
        this.turn_index = turn_index % 2;
        this.current_player = current_player;
        this.round = round;
        this.hand_cards = hand_cards;
    }

    public boolean spellHandler() throws Exception {

        switch (this.spell_card.getSpellType().toString()) {

            case "Shield":
                if (!handlePutSpell()) {
                    return false;
                }
                handleShield();
                return true;

            case "Heal":
                if (!handlePutSpell()) {
                    return false;
                }
                handleHeal();
                return false;

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
                return true;

            case "Copy":
                handleCopy();
                return false;

            case "Hide":
                handleHide();
                return false;

            case "Steal":
                handleSteal();
                return false;

            case "Mirror":

                if (!handlePutSpell()) {
                    return false;
                }
                handleMirror();
                return false;

            case "AddSpace":
                handleAddSpace();
                return false;

            // special spell cards

            default:
                System.out.println("\n nothing!\n\n");
                return false;

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
        this.removeCardFromHand(this.spell_card);
    }

    public void handleHeal() {
        int added_hit_points = 100;
        this.board[this.turn_index][this.des_index].setBlockPower(999);
        this.board[this.turn_index][this.des_index].setBlockDamage(0);
        this.board[this.turn_index][this.des_index].setBlockEmpty(false);
        this.current_player.setHitPoints(this.current_player.getHitPoints() + added_hit_points);
        this.removeCardFromHand(this.spell_card);
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
            this.removeCardFromHand(this.spell_card);
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
        if (unavailable_block_player_one != -1) {
            this.board[0][block_number_player_one].setBlockUnavailable(true);
            this.board[0][unavailable_block_player_one].setBlockUnavailable(false);
            this.board[0][unavailable_block_player_one].setBlockEmpty(true);
        }

        if (unavailable_block_player_two != -1) {
            this.board[1][block_number_player_two].setBlockUnavailable(true);
            this.board[1][unavailable_block_player_two].setBlockUnavailable(false);
            this.board[1][unavailable_block_player_two].setBlockEmpty(true);
        }

        ConsoleGame.printSuccessSpaceShift();
        this.removeCardFromHand(this.spell_card);

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
            if (this.board[turn_index][i].isBlockUnavailable()) {
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
        this.removeCardFromHand(this.spell_card);

    }

    public void handleRoundReduce() {
        this.round.setNumberOfRoundTurns(this.round.getNumberOfRoundTurns() - 1);
        ConsoleGame.printSuccessfulTurnReduce();
        this.removeCardFromHand(this.spell_card);
    }

    public void handleAttenuate() {
        // randomly choose two reqular cards from opponent
        Card found_card_one = null;
        Card found_card_two = null;


        System.out.println("Attenuate");

        boolean found = false;
        for (int i = 0; i <= 20; i++) {
            if (!this.board[(this.turn_index + 1) % 2][i].isBlockEmpty()
                    && this.board[(this.turn_index + 1) % 2][i].getBlockCard() != null) {
                if (this.board[(this.turn_index + 1) % 2][i].getBlockCard().getCardType().toString()
                        .equals("Regular")) {
                    if (!found) {
                        found_card_one = this.board[(this.turn_index + 1) % 2][i].getBlockCard();
                        found = true;
                    } else {
                        found_card_two = this.board[(this.turn_index + 1) % 2][i].getBlockCard();
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
            if (this.turn_index % 2 == 0) {
                round.getPlayer_two().setDamage(round.getPlayer_two().getDamage() - 3);
            } else {
                round.getPlayer_one().setDamage(round.getPlayer_one().getDamage() - 3);
            }
        }
        ConsoleGame.printAttenuateSuccess(found_card_one, found_card_two);
        this.removeCardFromHand(this.spell_card);
    }

    public void handleCopy() {
        try {
            Card copy_card = this.hand_cards.get(des_index);
            this.hand_cards.add(0, copy_card);
            this.current_player.setIsBonusActive(true);
            ConsoleGame.printCopySuccess(copy_card);
            this.removeCardFromHand(this.spell_card);
        } catch (Exception e) {
            ConsoleGame.printNoValidCardToCopy();
        }
    }

    public void handleHide() {
        if (this.turn_index % 2 == 0) {
            this.round.getPlayer_two().setShouldCardsBeHidden(true);
            Collections.shuffle(this.round.getPlayer_two_cards().subList(0, 5));

        } else {
            this.round.getPlayer_one().setShouldCardsBeHidden(true);
            Collections.shuffle(this.round.getPlayer_one_cards().subList(0, 5));
        }
        ConsoleGame.printSuccessfulHide();
        this.removeCardFromHand(this.spell_card);
    }

    public void handleSteal() {

        int i = (int) (Math.random() * 5);
        Card card = null;
        if (this.turn_index == 0) {
            card = this.round.getPlayer_two_cards().get(i);
            this.hand_cards.add(0, this.round.getPlayer_two_cards().get(i));
            this.round.getPlayer_two_cards().remove(i);
            this.round.getPlayer_two().setCardsAreStolen(true);
        } else {
            card = this.round.getPlayer_one_cards().get(i);
            this.hand_cards.add(0, this.round.getPlayer_one_cards().get(i));
            this.round.getPlayer_one_cards().remove(i);
            this.round.getPlayer_one().setCardsAreStolen(true);
        }
        current_player.setIsBonusActive(true);
        ConsoleGame.printSuccessfulSteal(card);
        this.removeCardFromHand(this.spell_card);
    }

    public void removeCardFromHand(Card card) {
        this.hand_cards.remove(card);
    }

    public void handleMirror() {
        Block op_block = this.board[(this.turn_index + 1) % 2][this.des_index];
        if (this.turn_index % 2 == 0) {
            this.round.getPlayer_two().setDamage(this.round.getPlayer_two().getDamage() - op_block.getBlockDamage());
            this.current_player.setDamage(this.current_player.getDamage() + op_block.getBlockDamage());
        } else {
            this.round.getPlayer_one().setDamage(this.round.getPlayer_one().getDamage() - op_block.getBlockDamage());
            this.current_player.setDamage(this.current_player.getDamage() + op_block.getBlockDamage());
        }

        this.removeCardFromHand(this.spell_card);
        ConsoleGame.printSuccessfulMirror();
    }

    public void handleAddSpace() {
        int block_number = findEmptyBlock((this.turn_index + 1) % 2);
        if (block_number == -1) {
            ConsoleGame.printNoEmptyBlock();
            return;
        }

        this.board[this.turn_index][block_number].setBlockUnavailable(true);
        ConsoleGame.printSuccessfulAddSpecialCard();
        this.removeCardFromHand(this.spell_card);
    }
}
