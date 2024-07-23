package org.example.citywars;

import controllers.UserController;
import controllers.game.GameController;
import kotlin.contracts.SimpleEffect;
import models.Response;
import models.User;
//import models.game.Game;
import models.game.SimpleGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class M_GameHistoryMenu extends Menu {
    public ArrayList<SimpleGame> games;
    int gamePerPage;
    int currentPage;
    int allPagesCount;
    SortKind sortKind;
    boolean nextOrPre;

    private Exception exception = null;

    public M_GameHistoryMenu() {
        super("M_GameHistoryMenu");
        gamePerPage = 7;
    }

    private void printMenu() {
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
        System.out.println("    next page");
        System.out.println("    previous page");

    }

    private void printHeadings() {
        System.out.println();
        System.out.println("\t\t\t\t\tPage " + currentPage + " of " + allPagesCount);
        System.out.println();
        System.out.println("\t\tOpponent Name\t\tOpponent Level\t\tWinner\t\t\tCreated at");
    }

    public void printGame(SimpleGame game, int i) {
        User opponent = (User) UserController.getByID(game.getPlayerTwoID()).body.get("user");
        String winner = "";
        if (game.getWinner().equals("player_one")) {
            if (game.getPlayerOneID() == loggedInUser.getID()) {
                winner = "You";
            } else if (game.getPlayerTwoID() == loggedInUser.getID()) {
                winner = "Opponent";
            }
        } else if (game.getWinner().equals("player_two")) {
            if (game.getPlayerOneID() == loggedInUser.getID()) {
                winner = "Opponent";
            } else if (game.getPlayerTwoID() == loggedInUser.getID()) {
                winner = "You";
            }
        }
        System.out.println("Game #" + (i / 10 == 0 ? ("0" + i) : i) + "\t" + opponent.getUsername() + "\t\t\t"
                + opponent.getLevel() + "\t\t\t" + winner + "\t\t" + game.getCreatedAt());
        System.out.println("Winner reward:  " + game.getWinnerReward());
        System.out.println("Loser  reward:  " + game.getLoserReward());
        System.out.println();
    }

    public Menu myMethods() {
        printMenu();
        String input = null;

        Response res = GameController.getAllUserGames(loggedInUser.getID());
        if (res.ok) {
            List<SimpleGame> temp = (List<SimpleGame>) res.body.get("games");
            games = new ArrayList<>();
            for (int i = 0; i < temp.size(); i++) {
                games.add(temp.get(i));
            }
        } else {
            System.out.println(res.message);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }

        if (games == null) {
            System.out.println("no games were found");
            return this;
        } else {
            sortKind = SortKind.no_filter;
            nextOrPre = false;
            currentPage = 1;
            allPagesCount = games.size() % gamePerPage == 0 ? games.size() / gamePerPage
                    : (games.size() / gamePerPage) + 1;
            printHeadings();
            for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                printGame(games.get(i), i);
            }
        }
        do {
            if (!nextOrPre) {
                input = consoleScanner.nextLine().trim();
            } else
                input = "";

            if (input.matches("^only show wins$") || (sortKind.equals(SortKind.only_wins) && nextOrPre)) {
                nextOrPre = false;
                sortKind = SortKind.only_wins;
                printHeadings();
                for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                    String userPos = games.get(i).getPlayerTwoID() == loggedInUser.getID() ? "player_one"
                            : "player_two";
                    if (games.get(i).getWinner().equals(userPos)) {
                        printGame(games.get(i), i);
                    }
                }
            } else if (input.matches("^only show losses$") || (sortKind.equals(SortKind.only_losses) && nextOrPre)) {
                nextOrPre = false;
                sortKind = SortKind.only_losses;
                printHeadings();
                for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                    String oppPos = games.get(i).getPlayerOneID() == loggedInUser.getID() ? "player_two"
                            : "player_one";
                    if (games.get(i).getWinner().equals(oppPos)) {
                        printGame(games.get(i), i);
                    }
                }
            } else if (input.matches("^sort by date-ascending$")
                    || (sortKind.equals(SortKind.date_ascending) && nextOrPre)) {
                nextOrPre = false;
                sortKind = SortKind.date_ascending;
                games.sort(Comparator.comparing(SimpleGame::getCreatedAt));
                printHeadings();
                for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                    printGame(games.get(i), i);
                }
            } else if (input.matches("^sort by date-descending$")
                    || (sortKind.equals(SortKind.date_descending) && nextOrPre)) {
                nextOrPre = false;
                sortKind = SortKind.date_descending;
                games.sort((s1, s2) -> {
                    String s11 = s1.getCreatedAt().replaceAll("\\D", "");
                    String s22 = s2.getCreatedAt().replaceAll("\\D", "");
                    return s22.compareTo(s11);
                });
                printHeadings();
                for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                    printGame(games.get(i), i);
                }
            } else if (input.matches("^sort by opponent name-ascending$")
                    || (sortKind.equals(SortKind.opponent_name_ascending) && nextOrPre)) {
                nextOrPre = false;
                sortKind = SortKind.opponent_name_ascending;
                games.sort((g1, g2) -> {
                    User opponent1 = null;
                    User opponent2 = null;
                    if (g1.getPlayerOneID() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayerTwoID()).body.get("user");
                    } else if (g1.getPlayerTwoID() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayerOneID()).body.get("user");
                    }
                    if (g2.getPlayerOneID() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayerTwoID()).body.get("user");
                    } else if (g2.getPlayerTwoID() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayerOneID()).body.get("user");
                    }
                    return opponent1.getUsername().compareTo(opponent2.getUsername());
                });
                printHeadings();
                for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                    printGame(games.get(i), i);
                }
            } else if (input.matches("^sort by opponent name-descending$")
                    || (sortKind.equals(SortKind.opponent_name_descending) && nextOrPre)) {
                nextOrPre = false;
                sortKind = SortKind.opponent_name_descending;
                games.sort((g1, g2) -> {
                    User opponent1 = null;
                    User opponent2 = null;
                    if (g1.getPlayerOneID() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayerTwoID()).body.get("user");
                    } else if (g1.getPlayerOneID() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayerTwoID()).body.get("user");
                    }
                    if (g2.getPlayerOneID() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayerTwoID()).body.get("user");
                    } else if (g2.getPlayerOneID() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayerTwoID()).body.get("user");
                    }
                    return -1 * (opponent1.getUsername().compareTo(opponent2.getUsername()));
                });
                printHeadings();
                for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                    printGame(games.get(i), i);
                }
            } else if (input.matches("^sort by opponent level-ascending$")
                    || (sortKind.equals(SortKind.opponent_level_ascending) && nextOrPre)) {
                nextOrPre = false;
                sortKind = SortKind.opponent_level_ascending;
                games.sort((g1, g2) -> {
                    User opponent1 = null;
                    User opponent2 = null;
                    if (g1.getPlayerOneID() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayerTwoID()).body.get("user");
                    } else if (g1.getPlayerOneID() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayerTwoID()).body.get("user");
                    }
                    if (g2.getPlayerOneID() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayerTwoID()).body.get("user");
                    } else if (g2.getPlayerOneID() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayerTwoID()).body.get("user");
                    }
                    return opponent1.getLevel() - opponent2.getLevel();
                });
                printHeadings();
                for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                    printGame(games.get(i), i);
                }
            } else if (input.matches("^sort by opponent level-descending$")
                    || (sortKind.equals(SortKind.opponent_level_descending) && nextOrPre)) {
                nextOrPre = false;
                sortKind = SortKind.opponent_level_descending;
                games.sort((g1, g2) -> {
                    User opponent1 = null;
                    User opponent2 = null;
                    if (g1.getPlayerTwoID() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayerTwoID()).body.get("user");
                    } else if (g1.getPlayerTwoID() == loggedInUser.getID()) {
                        opponent1 = (User) UserController.getByID(g1.getPlayerTwoID()).body.get("user");
                    }
                    if (g2.getPlayerTwoID() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayerTwoID()).body.get("user");
                    } else if (g2.getPlayerTwoID() == loggedInUser.getID()) {
                        opponent2 = (User) UserController.getByID(g2.getPlayerTwoID()).body.get("user");
                    }
                    return opponent2.getLevel() - opponent1.getLevel();
                });
                printHeadings();
                for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                    printGame(games.get(i), i);
                }
            } else if (input.matches("^show current menu$")) {
                System.out.println("You are currently in " + getName());
            } else if (input.matches("^next page$")) {
                if (currentPage < allPagesCount) {
                    nextOrPre = true;
                    currentPage++;
                } else
                    System.out.println("It's last page!");
            } else if (input.matches("^previous page$")) {
                if (1 < currentPage) {
                    nextOrPre = true;
                    currentPage--;
                } else
                    System.out.println("It's first page!");
            } else if (sortKind.equals(SortKind.no_filter) && nextOrPre) {
                nextOrPre = false;
                printHeadings();
                for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                    printGame(games.get(i), i);
                }
            } else if (input.matches("^Back$")) {
                return new M_GameMainMenu();
            } else {
                System.out.println("invalid command!");
            }
        } while (true);
    }
}
