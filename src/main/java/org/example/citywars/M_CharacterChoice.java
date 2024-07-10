package org.example.citywars;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.GameCharacter;
import models.Response;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controllers.GameCharacterController;

public class M_CharacterChoice extends Menu {
    @FXML
    ImageView imv1;
    @FXML
    ImageView imv2;
    @FXML
    ImageView imv3;
    @FXML
    ImageView imv4;

    public M_CharacterChoice() {
        super("M_CharacterChoice", new String[] { "BG-Videos\\BG1.jpg", "BG-Videos\\lightmode.png" });
    }

    @Override
    public Menu myMethods() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backGroundIm.setImage(BGims.get(themeIndex));

        imv1.setImage(charsImages[5]);
        imv2.setImage(charsImages[7]);
        imv3.setImage(charsImages[4]);
        imv4.setImage(charsImages[6]);
    }

    @FXML
    protected void choiceCh1(MouseEvent event) throws IOException {

        if (secondPersonNeeded) {
            secondUser.setGameCharacter(getCharacter("wolf"));
            if (is_bet)
                HelloApplication.menu = new M_Bet();
            else {
                // stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // HelloApplication.menu = new M_Game((Stage) ((Node)
                // event.getSource()).getScene().getWindow());
                HelloApplication.menu = new M_Game();
            }
        } else {
            loggedInUser.setGameCharacter(getCharacter("wolf"));
            HelloApplication.menu = new M_GamePlayMenu();
        }
        switchMenus(event);
    }

    @FXML
    protected void mouseInterCh1(MouseEvent event) throws IOException {
        imv1.setFitHeight(430.0);
        imv1.setFitWidth(430.0);
        imv2.setOpacity(0.5);
        imv3.setOpacity(0.5);
        imv4.setOpacity(0.5);
    }

    @FXML
    protected void mouseExitCh1(MouseEvent event) throws IOException {
        imv1.setFitHeight(400.0);
        imv1.setFitWidth(400.0);
        imv2.setOpacity(1);
        imv3.setOpacity(1);
        imv4.setOpacity(1);
    }

    @FXML
    protected void choiceCh2(MouseEvent event) throws IOException {
        if (secondPersonNeeded) {
            secondUser.setGameCharacter(getCharacter("panda"));
            if (is_bet)
                HelloApplication.menu = new M_Bet();
            else {
                // stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // HelloApplication.menu = new M_Game((Stage) ((Node)
                // event.getSource()).getScene().getWindow());
                HelloApplication.menu = new M_Game();
            }
        } else {
            loggedInUser.setGameCharacter(getCharacter("panda"));
            HelloApplication.menu = new M_GamePlayMenu();
        }
        switchMenus(event);
    }

    @FXML
    protected void mouseInterCh2(MouseEvent event) throws IOException {
        imv2.setFitHeight(430.0);
        imv2.setFitWidth(430.0);
        imv1.setOpacity(0.5);
        imv3.setOpacity(0.5);
        imv4.setOpacity(0.5);
    }

    @FXML
    protected void mouseExitCh2(MouseEvent event) throws IOException {
        imv2.setFitHeight(400.0);
        imv2.setFitWidth(400.0);
        imv1.setOpacity(1);
        imv3.setOpacity(1);
        imv4.setOpacity(1);
    }

    @FXML
    protected void choiceCh3(MouseEvent event) throws IOException {

        if (secondPersonNeeded) {
            secondUser.setGameCharacter(getCharacter("robot"));
            if (is_bet)
                HelloApplication.menu = new M_Bet();
            else {
                // stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // HelloApplication.menu = new M_Game((Stage) ((Node)
                // event.getSource()).getScene().getWindow());
                HelloApplication.menu = new M_Game();
            }
        } else {
            loggedInUser.setGameCharacter(getCharacter("robot"));
            HelloApplication.menu = new M_GamePlayMenu();
        }
        switchMenus(event);
    }

    @FXML
    protected void mouseInterCh3(MouseEvent event) throws IOException {
        imv3.setFitHeight(430.0);
        imv3.setFitWidth(430.0);
        imv2.setOpacity(0.5);
        imv1.setOpacity(0.5);
        imv4.setOpacity(0.5);
    }

    @FXML
    protected void mouseExitCh3(MouseEvent event) throws IOException {
        imv3.setFitHeight(400.0);
        imv3.setFitWidth(400.0);
        imv2.setOpacity(1);
        imv1.setOpacity(1);
        imv4.setOpacity(1);
    }

    @FXML
    protected void choiceCh4(MouseEvent event) throws IOException {

        if (secondPersonNeeded) {
            secondUser.setGameCharacter(getCharacter("dragon"));
            if (is_bet)
                HelloApplication.menu = new M_Bet();
            else {
                // stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // HelloApplication.menu = new M_Game((Stage) ((Node)
                // event.getSource()).getScene().getWindow());
                HelloApplication.menu = new M_Game();
            }
        } else {
            loggedInUser.setGameCharacter(getCharacter("dragon"));
            HelloApplication.menu = new M_GamePlayMenu();
        }
        switchMenus(event);
    }

    @FXML
    protected void mouseInterCh4(MouseEvent event) throws IOException {
        imv4.setFitHeight(430.0);
        imv4.setFitWidth(430.0);
        imv2.setOpacity(0.5);
        imv3.setOpacity(0.5);
        imv1.setOpacity(0.5);
    }

    @FXML
    protected void mouseExitCh4(MouseEvent event) throws IOException {
        imv4.setFitHeight(400.0);
        imv4.setFitWidth(400.0);
        imv2.setOpacity(1);
        imv3.setOpacity(1);
        imv1.setOpacity(1);
    }

    private GameCharacter getCharacter(String name) {
        Response res = GameCharacterController.getGameCharacter(name);
        if (res.ok) {
            return (GameCharacter) res.body.get("character");
        }
        return null;

    }

}
