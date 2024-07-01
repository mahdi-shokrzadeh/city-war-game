package views.console.game;

import models.card.Card;
import models.game.Block;

public class ConsoleBoard {

    public ConsoleBoard() {

    }

    public static void printBoard(Block[][] board) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 21; j++) {
                for (int k = 0; k <= 6; k++) {

                    printBoardElement(board[i][j], k);
                }
            }

            System.out.println("\n");
        }
    }

    public static void printBoardElement(Block block, int line) {
        int maxLineLength = 8; 
    
        if (line == 0 || line == 6) {
            System.out.format("%-" + maxLineLength + "s", "----------");
            printSpace();
        } else if (line == 2) {
            System.out.print("|");
            if (block.isBlockUnavailable()) {
                System.out.format("%-" + maxLineLength + "s", "UNAVAILABLE");
                printSpace();
            } else if (block.isBlockEmpty()) {
                System.out.format("%-" + maxLineLength + "s", "EMPTY");
            }
            System.out.print("|");
        } else if (line == 3) {
            System.out.print("|");
            if (!block.isBlockEmpty()) {
                if (block.isCardHidden()) {
                    System.out.format("%-" + maxLineLength + "s", "HIDDEN");
                } else {
                    Card card = block.getBlockCard();
                    System.out.format("%-" + maxLineLength + "s", card.getName());
                }
            }
            System.out.print("|");
        } else if (line == 4) {
            System.out.print("|");
            if (!block.isBlockEmpty()) {
                Card card = block.getBlockCard();
                System.out.format("%-" + maxLineLength + "s", "damage: " + card.getDamage());
            }
            System.out.print("|");
        } else if (line == 5) {
            System.out.print("|");
            if (!block.isBlockEmpty()) {
                Card card = block.getBlockCard();
                double powerPerDuration = card.getPower() / card.getDuration();
                System.out.format("%-" + maxLineLength + "s", "power: " + powerPerDuration);
            }
            System.out.print("|");
        } else {
            System.out.format("%-" + maxLineLength + "s", "");
            printSpace();
        }
    }
    
    

    public static void printSpace() {
        System.out.print("     ");
    }
}
