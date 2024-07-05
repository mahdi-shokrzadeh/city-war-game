package org.example.citywars;

import controllers.UserController;
import controllers.game.GameController;
import models.Response;
import models.User;
import models.game.Game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class M_GameHistoryMenu extends Menu{
    public ArrayList<Game> games;
    private Exception exception = null;
    public M_GameHistoryMenu(){
        super("M_GameHistoryMenu");
    }
    private void printMenu(){
        System.out.println("GAME HISTORY MENU");
        System.out.println("Options: ");
        System.out.println("    Back");
        System.out.println("    only show wins");
        System.out.println("    only show losses");
        System.out.println("    sort by date-ascending");
        System.out.println("    sort by date-descending");
        System.out.println("    sort by opponent name-ascending");
        System.out.println("    sort by opponent name-descending");
        System.out.println("    sort by opponent level-ascending");
        System.out.println("    sort by opponent level-descending");
    }
    private void printHeadings(){
        System.out.println();
        System.out.print("\t\t\t\t\tOpponent\t\tMode\t\t\tCreated at\t\t\t\t\tWinner\t\t\t\tNumber of rounds");
        System.out.println();
    }
    public void printGame(Game game,int i){
        User opponent = (User) UserController.getByID(game.getPlayer_two_id()).body.get("user");
        System.out.print("Game #" + (i/10 == 0 ? ("0"+i) : i) + "\t\t\t" + opponent.getUsername()  + "\t\t\t" + game.getMode() + "\t\t\t" + game.getCreated_at() + "\t\t\t" + game.getWinner() + "\t\t\t" + game.getNumber_of_rounds());
        System.out.println();
    }
    public Menu myMethods() {
        printMenu();
        String input = null;


        Response res = GameController.getAllUserGames(loggedInUser.getID());
        if(res.ok){
            List<Game> temp = (List<Game>) res.body.get("games");
            games = new ArrayList<>();
            for (int i = 0; i < temp.size(); i++) {
                games.add(temp.get(i));
            }
        }else{
            System.out.println(res.message);
            if(res.exception != null){
                System.out.println(res.exception.getMessage());
            }
        }
        if( games == null ){
            System.out.println("no games were found");
            return this;
        }else{
            printHeadings();
            for(int i=0;i<games.size();i++){
                printGame(games.get(i),i);
            }
        }
        do {
            input = consoleScanner.nextLine().trim();
            if(input.matches("^only show wins$")){
                printHeadings();
                for(int i=0;i<games.size();i++){
                    String userPos = games.get(i).getPlayer_one_id() == loggedInUser.getID()? "player_one":"player_two";
                    if( games.get(i).getWinner().equals(userPos) ){
                        printGame(games.get(i),i);
                    }
                }
            }else if(input.matches("^only show losses$")){
                printHeadings();
                for(int i=0;i<games.size();i++){
                    String oppPos = games.get(i).getPlayer_one_id() == loggedInUser.getID()? "player_two":"player_one";
                    if( games.get(i).getWinner().equals(oppPos) ){
                        printGame(games.get(i),i);
                    }
                }
            }else if(input.matches("^sort by date-ascending$")){
                games.sort(Comparator.comparing(Game::getCreated_at));
                printHeadings();
                for(int i=0;i< games.size();i++){
                    printGame(games.get(i),i);
                }
            }else if(input.matches("^sort by date-descending$")){
                games.sort((s1,s2)->{
                    String s11 = s1.getCreated_at().replaceAll("\\D","");
                    String s22 = s2.getCreated_at().replaceAll("\\D","");
                    return s22.compareTo(s11);
                });
                printHeadings();
                for(int i=0;i< games.size();i++){
                    printGame(games.get(i),i);
                }
            }else if(input.matches("^sort by opponent name-ascending$")){
                games.sort((g1, g2) -> {
                    User opponent1 = null;
                    User opponent2 = null;
                    if (g1.getPlayer_one_id() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayer_two_id()).body.get("user");
                    } else if (g1.getPlayer_two_id() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayer_one_id()).body.get("user");
                    }
                    if (g2.getPlayer_one_id() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayer_two_id()).body.get("user");
                    } else if (g2.getPlayer_two_id() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayer_one_id()).body.get("user");
                    }
                    return opponent1.getUsername().compareTo(opponent2.getUsername());
                });
                printHeadings();
                for(int i=0;i<games.size();i++){
                    printGame(games.get(i),i);
                }
            }else if(input.matches("^sort by opponent name-descending$")){
                games.sort((g1, g2) -> {
                    User opponent1 = null;
                    User opponent2 = null;
                    if (g1.getPlayer_one_id() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayer_two_id()).body.get("user");
                    } else if (g1.getPlayer_two_id() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayer_one_id()).body.get("user");
                    }
                    if (g2.getPlayer_one_id() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayer_two_id()).body.get("user");
                    } else if (g2.getPlayer_two_id() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayer_one_id()).body.get("user");
                    }
                    return -1*(opponent1.getUsername().compareTo(opponent2.getUsername()));
                });
                printHeadings();
                for(int i=0;i<games.size();i++){
                    printGame(games.get(i),i);
                }
            }else if(input.matches("^sort by opponent level-ascending$")){
                games.sort((g1, g2) -> {
                    User opponent1 = null;
                    User opponent2 = null;
                    if (g1.getPlayer_one_id() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayer_two_id()).body.get("user");
                    } else if (g1.getPlayer_two_id() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayer_one_id()).body.get("user");
                    }
                    if (g2.getPlayer_one_id() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayer_two_id()).body.get("user");
                    } else if (g2.getPlayer_two_id() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayer_one_id()).body.get("user");
                    }
                    return opponent1.getLevel()-opponent2.getLevel();
                });
                printHeadings();
                for(int i=0;i<games.size();i++){
                    printGame(games.get(i),i);
                }
            }else if(input.matches("^sort by opponent level-descending$")){
                games.sort((g1, g2) -> {
                    User opponent1 = null;
                    User opponent2 = null;
                    if (g1.getPlayer_one_id() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayer_two_id()).body.get("user");
                    } else if (g1.getPlayer_two_id() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayer_one_id()).body.get("user");
                    }
                    if (g2.getPlayer_one_id() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayer_two_id()).body.get("user");
                    } else if (g2.getPlayer_two_id() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayer_one_id()).body.get("user");
                    }
                    return opponent2.getLevel()-opponent1.getLevel();
                });
            }else if(input.matches("^show current menu$")){
                System.out.println("You are currently in " + getName());
            }else if(input.matches("^Back$")){
                return new M_GameMainMenu();
            }else{
                System.out.println("invalid command!");
            }
        }while (true);
    }
}
