package org.example.citywars;

import controllers.UserController;
import controllers.game.GameController;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Response;
import models.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class M_GameHistoryMenu extends Menu {
    public ArrayList<M_Game> games;
    int gamePerPage;
    int currentPage;
    int allPagesCount;
    SortKind sortKind;
    boolean nextOrPre;
    @FXML
    TableView<M_Game> table = createTable();
    @FXML
    Pagination pages;

    private Exception exception = null;

    public M_GameHistoryMenu() {
        super("M_GameHistoryMenu", new String[]{"BG-Videos\\BG1.jpg","BG-Videos\\lightmode.png"});
        gamePerPage = 7;

        Response res = GameController.getAllUserGames(loggedInUser.getID());
        if (res.ok) {
            List<M_Game> temp = (List<M_Game>) res.body.get("games");
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

        allPagesCount = games.size() % gamePerPage == 0 ? games.size() / gamePerPage
                : (games.size() / gamePerPage) + 1;
    }

    private TableView<M_Game> createTable(){
        TableView<M_Game> temp = new TableView<>();

        TableColumn<M_Game,Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<M_Game,Integer>("id"));

//        TableColumn<M_Game,String> opponentName = new TableColumn<>("Opponent Name");
//        id.setCellValueFactory(new PropertyValueFactory<M_Game,String>("player_two."));
        TableColumn<M_Game,String> createdAt = new TableColumn<>("Created at");
//        id.setCellValueFactory(new PropertyValueFactory<M_Game,String>("created_at"));

        temp.getColumns().addAll(id);

        temp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        temp.getItems().addAll(games);
        return temp;

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

    public void printGame(M_Game game, int i) {
        User opponent = (User) UserController.getByID(game.getPlayer_two_id()).body.get("user");
        System.out.println("Game #" + (i / 10 == 0 ? ("0" + i) : i) + "\t" + opponent.getUsername() + "\t\t\t"
                + opponent.getLevel() + "\t\t\t" + game.getWinner() + "\t\t" + game.getCreated_at());
        System.out.println("Winner reward:  " + game.getWinnerReward());
        System.out.println("Loser  reward:  " + game.getLoserReward());
        System.out.println();
    }

    public Menu myMethods() {
        printMenu();
        String input = null;

        Response res = GameController.getAllUserGames(loggedInUser.getID());
        if (res.ok) {
            List<M_Game> temp = (List<M_Game>) res.body.get("games");
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
                    String userPos = games.get(i).getPlayer_one_id() == loggedInUser.getID() ? "player_one"
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
                    String oppPos = games.get(i).getPlayer_one_id() == loggedInUser.getID() ? "player_two"
                            : "player_one";
                    if (games.get(i).getWinner().equals(oppPos)) {
                        printGame(games.get(i), i);
                    }
                }
            } else if (input.matches("^sort by date-ascending$")
                    || (sortKind.equals(SortKind.date_ascending) && nextOrPre)) {
                nextOrPre = false;
                sortKind = SortKind.date_ascending;
                games.sort(Comparator.comparing(M_Game::getCreated_at));
                printHeadings();
                for (int i = (currentPage - 1) * gamePerPage; i < currentPage * gamePerPage && i < games.size(); i++) {
                    printGame(games.get(i), i);
                }
            } else if (input.matches("^sort by date-descending$")
                    || (sortKind.equals(SortKind.date_descending) && nextOrPre)) {
                nextOrPre = false;
                sortKind = SortKind.date_descending;
                games.sort((s1, s2) -> {
                    String s11 = s1.getCreated_at().replaceAll("\\D", "");
                    String s22 = s2.getCreated_at().replaceAll("\\D", "");
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backGroundIm.setImage(BGims.get(themeIndex));
        pages.setPageCount(allPagesCount);
        pages.setCurrentPageIndex(currentPage);

    }
}
