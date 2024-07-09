package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.citywars.M_Game.bet_amount;

public class M_Bet extends Menu {
    @FXML
    Spinner<Integer> betAmount;

    public M_Bet() {
        super("M_Bet", new String[] { "BG-Videos\\bit.png" });
    }

    @FXML
    protected void betOkButton(ActionEvent event) throws IOException {
        bet_amount = betAmount.getValue();
        System.out.println(bet_amount);////////////////////////
        HelloApplication.menu = new M_Game((Stage) ((Node) event.getSource()).getScene().getWindow());
        switchMenus(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backGroundIm.setImage(BGims.get(themeIndex));
        int min = Integer.min(loggedInUser.getCoins(), secondUser.getCoins());
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, min);
        valueFactory.setValue(0);
        betAmount.setValueFactory(valueFactory);
    }

    @Override
    public Menu myMethods() {
        return null;
    }
}
