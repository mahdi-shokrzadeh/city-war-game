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
        super("M_RemoveCardMenu",
                new String[] { "BG-Videos/GameBGs/bg1.png", "BG-Videos/GameBGs/bg2.png", "BG-Videos/GameBGs/bg3.png" });
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
        Card card = null;
        Response res = CardController.getCardByName(input);
        if (res.ok) {
            card = (Card) res.body.get("card");
        } else {
            alert.setAlertType(AlertType.ERROR);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }

        res = CardController.removeCard(loggedInUser, card);
        if (!res.ok) {
            alert.setAlertType(AlertType.ERROR);
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }
        alert.setContentText(res.message);
        alert.show();
    }

    public void Back(Event event) throws IOException {
        HelloApplication.menu = new M_AdminMenu();
        switchMenus(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backGroundIm.setImage(BGims.get(themeIndex));

    }

}
