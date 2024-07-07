package models.game;

import models.card.Card;

public class Block {

    private Card block_card;
    private boolean is_empty = true;
    private boolean block_is_unavailable = false; // mean unable to host card
    private boolean card_is_hidden = false;
    private boolean block_is_destroyed = false; // it was attacked!
    private int bloc_power = 0;
    private int block_damage = 0;

    public Block() {

    }

    public void attachCardToBlock(Card card) {
        this.block_card = card;
        this.is_empty = false;
    }

    // setter and getters:
    public Card getBlockCard() {
        return block_card;
    }

    public void setBlockCard(Card c) {
        this.block_card = c;
        this.bloc_power = c.getPower();
        this.block_damage = c.getDamage() / c.getDuration();
    }

    public boolean isBlockEmpty() {
        return is_empty;
    }

    public void setBlockEmpty(boolean is_empty) {
        this.is_empty = is_empty;
    }

    public boolean isBlockUnavailable() {
        return block_is_unavailable;
    }

    public void setBlockUnavailable(boolean block_is_unavailable) {
        this.block_is_unavailable = block_is_unavailable;
    }

    public boolean isCardHidden() {
        return card_is_hidden;
    }

    public void setCardHidden(boolean card_is_hidden) {
        this.card_is_hidden = card_is_hidden;
    }

    public boolean isBlockDestroyed() {
        return block_is_destroyed;
    }

    public void setBlockDestroyed(boolean block_is_destroyed) {
        this.block_is_destroyed = block_is_destroyed;
    }

    public int getBlockPower() {
        return bloc_power;
    }

    public void setBlockPower(int bloc_power) {
        this.bloc_power = bloc_power;
    }

    public int getBlockDamage() {
        return block_damage;
    }

    public void setBlockDamage(int block_damage) {
        this.block_damage = block_damage;
    }

}
