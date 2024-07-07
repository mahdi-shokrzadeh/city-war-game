package org.example.citywars;

public class M_GameMainMenu extends Menu {
    public M_GameMainMenu() {
        super("M_GameMainMenu");
    }
    private void printMenu(){
        System.out.println("Main menu");
        System.out.println("Options: ");
        System.out.println("    start game");
        System.out.println("    show cards");
        System.out.println("    game history");
        System.out.println("    shop");
        System.out.println("    profile");
        System.out.println("    log out");
    }
    public Menu myMethods() {
        printMenu();
        do {
            String input = consoleScanner.nextLine();
            if (input.toLowerCase().matches("^ *start +game *$"))
                return new M_GamePlayMenu();
            else if(input.toLowerCase().matches("^clans$"))
                return new M_ClanMainMenu();
            else if (input.toLowerCase().matches("^ *game +history *$"))
                return new M_GameHistoryMenu();
            else if (input.toLowerCase().matches("^ *shop *$"))
                return new M_ShopMenu();
            else if (input.toLowerCase().matches("^ *profile *$"))
                return new M_ProfileMenu();
            else if (input.toLowerCase().matches("^ *log out *$"))
                return new M_Intro();
            else
                System.out.println("invalid command!");
        }while (true);
    }
}
