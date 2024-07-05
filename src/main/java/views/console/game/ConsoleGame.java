package views.console.game;

import models.User;
import models.card.Card;
import models.game.Block;

public class ConsoleGame {

    public static void printGameMenu() {

    }

    public static void printCharacterMenu(String player_name) {
        System.out.println("\n" + "Choose your character " + player_name + " :" + "\n" +
                "1. Warrior" + "\n" +
                "2. Mage" + "\n" +
                "3. Archer" + "\n" +
                "Enter the number of the character you want to choose:" + "\n");
    }

    public static void printGreetings() {
        System.out.println("--------------------");
        System.out.println("\n" + "Welcome to the game!" + "\n");
        System.out.println("You can use following commands:\n" +
                "\t start game : to start the game\n" +
                "\t select character : to select your character\n" +
                "\t set bet amount : to set the bet amount\n");
        System.out.println("--------------------");

    }

    public static void printBetNotSet() {
        System.out.println("Please set the bet amount first!");
    }

    public static void printInvalidCardNumber() {
        System.out.println("Please enter a valid card number!");
    }

    public static void printInvalidPlayerNumber() {
        System.out.println("Please enter a valid player number!");
    }

    public static void printInvalidBlockNumber() {
        System.out.println("Please enter a valid block number or card number!");
    }

    public static void printBlockIsUnavailable() {
        System.out.println("This block is unavailable!");
    }

    public static void printBlockIsNotEmpty() {
        System.out.println("This block is not empty!");
    }

    public static void printInvaidInput() {
        System.out.println("Invalid input!");
    }

    public static void printSuccessfulCardPlacement() {
        System.out.println("Card placed successfully!");
    }

    public static void printTurnIsFinished(int turn_index) {
        System.out.println("Turn " + turn_index + " is finished!");
    }

    public static void printTurnIsStarted(int turn_index) {
        System.out.println("Turn " + turn_index + " is started!");
    }

    public static void printGameIsFinished() {
        System.out.println("Game is finished!");
    }

    public static void printWinner(String winner) {
        System.out.println("\n-------------");
        System.out.println("The game is finished and the winner is: " + winner);
        System.out.println("-------------\n");
    }

    public static void printBlocksStatus(Block player_one_block, Block player_two_block) {
        for (int i = 2; i <= 6; i++) {
            ConsoleBoard.printBoardElement(player_one_block, i);
            System.out.print("   ");
        }
        System.out.println("\n" + "--------");
        for (int i = 2; i <= 6; i++) {
            ConsoleBoard.printBoardElement(player_two_block, i);
            System.out.print("   ");
        }
        System.out.println("\n");
    }

    public static void printDamageStatus(User player_one, User player_two) {
        System.out.println("Player one: " + player_one.getUsername() + " Total Damage: " + player_one.getDamage());
        System.out.println("Player two: " + player_two.getUsername() + " Total Damage: " + player_two.getDamage());
    }

    public static void printHPStatus(User player_one, User player_two) {
        System.out.println("Player one: " + player_one.getUsername() + " Hitpoints: " + player_one.getHitPoints());
        System.out.println("Player two: " + player_two.getUsername() + " Hitpoints: " + player_two.getHitPoints());
        System.out.println("\n");
    }

    public static void printBonous() {
        System.out.println("You have a bonous for completely destroying the enemy's card!");
    }

    public static void printCoinBonous() {
        System.out.println("You have a 40 more coin now!");
    }

    public static void printRoundStart() {
        System.out.println("\n" + "---------" + "\n");
        System.out.println("New round is now started!");
        System.out.println("\n" + "---------" + "\n");
    }

    public static void printBlockIndex(int index) {
        System.out.println("Block number: " + index);
    }

    public static void printNoValidCardToPlace() {
        System.out.println("AI: No valid card to place");
    }

    public static void printAIChoice(String in) {
        System.out.println("\nAI says: " + in + "\n");
    }

    public static void printBossDecision(int index, int power) {
        System.out.println("\nBoss bot increased the power of the card number " + index + " by " + power + "!!\n");
    }

    public static void printBuffCard(int index, int power) {
        System.out.println("\nBuff card increased the power of the card number " + index + " by " + power + "!!\n");
    }

    public static void printNoRegularCard() {
        System.out.println("No regular card found to power boost!");
    }

    public static void printPowerBoostSuccess(Card card) {
        System.out.println("Power boost is successful (+10) for the card: " + card.getName());
    }

    public static void printNoEmptyBlock() {
        System.out.println("No empty block found for space shift!");
    }

    public static void printSuccessSpaceShift() {
        System.out.println("Space shift was successful!");
    }

    public static void printNoUnavailableBlock() {
        System.out.println("No unavailable block found!");
    }

    public static void printSuccessRepair() {
        System.out.println("Repair was successful!");
    }

    public static void printSuccessfulTurnReduce() {
        System.out.println("The number of turns for current round reduced by 1!");
    }

    public static void printNoRegularCardForAttenuate() {
        System.out.println("No regular card found to attenuate!");
    }

    public static void printAttenuateSuccess(Card card, Card card_2) {
        System.out.println("Attenuate is successful (-1 power) for the card: " + card.getName());
        System.out.println("Attenuate is successful (-10 damage) for the card: " + card_2.getName());
    }

    public static void printNoValidCardToCopy() {
        System.out.println("No valid card found to copy!");
    }

    public static void printCopySuccess(Card card) {
        System.out.println("Copy is successful for the card: " + card.getName());
    }

    public static void printSuccessfulHide() {
        System.out.println("Hiding and  shuffling opponent cards was successful!");
    }

    public static void printSuccessfulSteal(Card card) {
        System.out.println("Steal was successful for the card: " + card.getName());
    }

    public static void printSuccessfulcharacterChoice(String character) {
        System.out.println("Character choice is successful! ");
    }
}