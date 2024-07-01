package views.console.game;

import models.User;
import models.card.Card;
import models.game.Block;

public class ConsoleBoard {

    public ConsoleBoard() {

    }

    public static void printBoard(Block[][] board, User player_one, User player_two, int player_one_damage,
            int player_two_damage) {
        System.out.println("Player one: " + player_one.getUsername() + " Hitpoints: " +
                player_one.getHitPoints() + " Total Damage: " + player_one_damage);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 21; j++) {
                System.out.print((j + 1) + "- ");

                for (int k = 0; k <= 6; k++) {
                    if (k > 0) {
                        System.out.print("   ");
                    }
                    printBoardElement(board[i][j], k);
                }
                System.out.println();
            }
            System.out.println();
            if (i == 1)
                System.out.println(
                        "Player two: " + player_two.getUsername() + " Hitpoints: " + player_two.getHitPoints() +
                                " Total Damage: " + player_two_damage);
        }
    }

    public static void printBoardElement(Block block, int line) {
        final int fixedLength = 8;

        if (line == 0 || line == 6) {
            // System.out.print("------------");
        } else if (line == 2) {
            System.out.print("|");
            if (block.isBlockUnavailable()) {
                System.out.print(padString("UNAVAILABLE", fixedLength));
            } else if (block.isBlockEmpty()) {
                System.out.print(padString("EMPTY", fixedLength));
            } else {
                System.out.print(padString("", fixedLength));
            }
            System.out.print("|");
        } else if (line == 3) {
            System.out.print("|");
            if (!block.isBlockEmpty()) {
                if (block.isCardHidden()) {
                    System.out.print(padString("HIDDEN", fixedLength));
                } else {
                    Card card = block.getBlockCard();
                    System.out.print(padString(card.getName(), fixedLength));
                }
            } else {
                System.out.print(padString("", fixedLength));
            }
            System.out.print("|");
        } else if (line == 4) {
            System.out.print("|");
            if (!block.isBlockEmpty()) {
                Card card = block.getBlockCard();
                System.out.print(padString("damage: " + card.getDamage(), fixedLength));
            } else {
                System.out.print(padString("", fixedLength));
            }
            System.out.print("|");
        } else if (line == 5) {
            System.out.print("|");
            if (!block.isBlockEmpty()) {
                Card card = block.getBlockCard();
                System.out.print(padString("power: " + card.getPower() / card.getDuration(),
                        fixedLength));
            } else {
                System.out.print(padString("", fixedLength));
            }
            System.out.print("|");
        } else {
            System.out.print(padString("", fixedLength));
        }
    }

    public static String padString(String input, int length) {
        if (input.length() >= length) {
            return input.substring(0, length);
        } else {
            StringBuilder sb = new StringBuilder(input);
            while (sb.length() < length) {
                sb.append(" ");
            }
            return sb.toString();
        }
    }

    

}
