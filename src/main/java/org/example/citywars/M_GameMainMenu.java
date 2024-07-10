package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.citywars.M_ProfileMenu.profileIndex;

public class M_GameMainMenu extends Menu {
    @FXML
    private Label HP;

    @FXML
    private Label XP;
    @FXML
    private Label coin;

    @FXML
    private Label level;

    @FXML
    private Label name;

    @FXML
    private ImageView profileIm;
    public M_GameMainMenu() {
        super("M_GameMainMenu", new String[]{"BG-Videos\\BG_GameMain.png","BG-Videos\\lightmode.png"});
    }
    private void printMenu(){
        System.out.println("Main menu");
        System.out.println("Options: ");
        System.out.println("    start game");
        System.out.println("    clans");
        System.out.println("    game history");
        System.out.println("    shop");
        System.out.println("    profile");
        System.out.println("    log out");
    }
    @FXML
    protected void GoToSettingButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_Setting();
        lastMenu=1;
        switchMenus(event);
//        System.out.println(lastMenu);
    }
    public Menu myMethods() {
        printMenu();
        do {
            String input = consoleScanner.nextLine();
            if (input.toLowerCase().matches("^ *start +game *$"))
                return new M_GamePlayMenu();
            else if (input.toLowerCase().matches("^clans$"))
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
        } while (true);
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        loggedInUser =null;
        HelloApplication.menu = new M_Intro();
        switchMenus(event);
    }
}
