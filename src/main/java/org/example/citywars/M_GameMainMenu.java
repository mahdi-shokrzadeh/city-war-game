package org.example.citywars;

public class M_GameMainMenu extends Menu {
    public M_GameMainMenu() {
        super("M_GameMainMenu");
    }

    public Menu myMethods() {
        String input = consoleScanner.nextLine();
        if (input.toLowerCase().matches("^ *start +game *$"))
            return new M_GameModeChoiseMenu();
        else if (input.toLowerCase().matches("^ *show +cards *$"))
            return new M_MyCardsMenu();
        else if (input.toLowerCase().matches("^ *game +history *$"))
            return new M_GameHistoryMenu();
        else if (input.toLowerCase().matches("^ *shop *$"))
            return new M_ShopMenu();
        else if (input.toLowerCase().matches("^ *profile *$"))
            return new M_ProfileMenu();
        else if (input.toLowerCase().matches("^ *log +out *$"))
            return new M_Intro();

        System.out.println("invalid command!");
        return this;
    }
}
