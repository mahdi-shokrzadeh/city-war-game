package org.example.citywars;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import controllers.CardController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;
import models.Response;
import models.card.Card;

public class M_AdminMenu extends Menu {

    @FXML
    private Pane pane;

    public M_AdminMenu() {
        super("M_AdminMenu");
    }

    public Menu myMethods() {
        return this;
    }

    @FXML
    protected void GoToAddCardMenu(Event event) throws IOException {
        HelloApplication.menu = new M_AddCardMenu();
        switchMenus(event);
    }

    @FXML
    protected void GoToEditCardMenu(Event event) throws IOException {
        HelloApplication.menu = new M_EditCardMenu();
        switchMenus(event);
    }

    @FXML
    protected void GoToRemoveCardMenu(Event event) throws IOException {
        HelloApplication.menu = new M_RemoveCardMenu();
        switchMenus(event);
    }

    @FXML
    protected void GoToUsersMenu(Event event) throws IOException {
        HelloApplication.menu = new M_UsersMenu();
        switchMenus(event);
    }

    @FXML
    protected void GoToAllCardsMenu(Event event) throws IOException {
        HelloApplication.menu = new M_AllCardsMenu();
        switchMenus(event);
    }

    @FXML
    public void logout(Event event) throws IOException {
        loggedInUser = null;
        HelloApplication.menu = new M_Intro();
        switchMenus(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
