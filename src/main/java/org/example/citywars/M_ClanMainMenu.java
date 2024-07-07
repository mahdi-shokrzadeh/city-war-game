package org.example.citywars;

public class M_ClanMainMenu extends Menu{
    public M_ClanMainMenu(){
        super("M_ClanMainMenu");
    }
    private void printMenu(){
        System.out.println("CLAN MENU");
        System.out.println("Options: ");
        System.out.println("    my clan");
        System.out.println("    join clan");
        System.out.println("    create clan");
    }
    public Menu myMethods(){
        printMenu();
        do {
            String input = consoleScanner.nextLine().trim();
            if (input.toLowerCase().matches("^my clan$"))
                return new M_MyClanMenu();
            else if (input.toLowerCase().matches("^join clan$"))
                return new M_JoinClanMenu();
            else if (input.toLowerCase().matches("^create clan$"))
                return new M_CreatClanMenu();
            else if(input.matches("show current menu")){
                System.out.println("You are currently at " + getName());
            }
            else if(input.matches("^Back$")){
                return new M_GameMainMenu();
            }
            else
                System.out.println("invalid command!");

        }while (true);
    }
}
