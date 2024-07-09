package org.example.citywars;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import models.card.Card;
import controllers.CardController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import models.Response;
import models.User;

public class M_RemoveCardMenu extends Menu {

    @FXML
    private TextField searchBar;

    public M_RemoveCardMenu() {
        super("M_RemoveCardMenu");
    }

    public Menu myMethods() {
        return this;
    }

    public void handleDelete(Event event) {
        String input = searchBar.getText();
        Alert alert = new Alert(AlertType.NONE);
        if (input.isBlank() || !input.matches("^[a-zA-Z]+$")) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Invalid card name");
            alert.show();
            return;
        }
        Response res = CardController.getCardByName(input);
        if (res.ok) {
            User _user = new User("admin", "Admin1!", "admin", "admin@gmail.com", "admin", "recQuestion", "recAnswer");
            CardController.removeCard(_user, ((Card) res.body.get("card")));
        } else {
            alert.setAlertType(AlertType.ERROR);
            alert.setContentText(res.message);
            alert.show();
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }
    }

    public void Back(Event event) throws IOException {
        HelloApplication.menu = new M_AdminMenu();
        switchMenus(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
