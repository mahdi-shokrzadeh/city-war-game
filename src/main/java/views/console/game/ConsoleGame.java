package views.console.game;

public class ConsoleGame {

    public static void printGameMenu() {

    }

    public static void printCharacterMenu(String player_name) {
        System.out.println("\n" + "Choose your character " + player_name + " :" + "\n" +
                "1. Warrior" + "\n" +
                "2. Mage" + "\n" +
                "3. Archer" + "\n" +
                // "4. Cancel" + "\n" +
                "Enter the number of the character you want to choose:" + "\n");
    }

    public static void printGreetings() {
        System.out.println("\n" + "Welcome to the game!" + "\n");
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
}
