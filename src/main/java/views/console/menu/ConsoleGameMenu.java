package views.console.menu;

public class ConsoleGameMenu {

    private String menu_options = "Please select the game mode:\n" +
            "1. Mono\n" +
            "2. Duel\n" +
            "3. Clan\n" +
            "4. Gamble\n" +
            "5. Exit\n" +
            "Enter your choice: ";

    ConsoleGameMenu() {
        System.out.println(menu_options);
    }


    public static void printGameMenu() {
        new ConsoleGameMenu();
    }

    
}
