package org.example.citywars;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.GameCharacter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class M_CharacterChoice extends Menu {
    File[] imageFiles;
    Image[] charsImages;
    @FXML
    ImageView imv1;
    @FXML
    ImageView imv2;
    @FXML
    ImageView imv3;
    @FXML
    ImageView imv4;

    public M_CharacterChoice() {
        super("M_CharacterChoice", new String[] { "BG-Videos\\BG1.jpg" });

        imageFiles = new File[4];
        imageFiles[0] = new File("src/main/resources/Characters/Igoribuki.png");
        imageFiles[1] = new File("src/main/resources/Characters/Master_Masher.png");
        imageFiles[2] = new File("src/main/resources/Characters/Nahane.png");
        imageFiles[3] = new File("src/main/resources/Characters/Sensei_Pandaken.png");
        charsImages = new Image[imageFiles.length];
        for (int i = 0; i < imageFiles.length; i++) {
            charsImages[i] = new Image(imageFiles[i].toURI().toString());
        }

    }

    @Override
    public Menu myMethods() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backGroundIm.setImage(BGims.get(themeIndex));

        imv1.setImage(charsImages[1]);
        imv2.setImage(charsImages[2]);
        imv3.setImage(charsImages[0]);
        imv4.setImage(charsImages[3]);
    }

    @FXML
    protected void choiceCh1(MouseEvent event) throws IOException {

        if (secondPersonNeeded) {
            secondUser.setGameCharacter(new GameCharacter("panda"));
            if (is_bet)
                HelloApplication.menu = new M_Bet();
            else
                HelloApplication.menu = new M_Game();
        } else {
            loggedInUser.setGameCharacter(new GameCharacter("panda"));
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
            secondUser.setGameCharacter(new GameCharacter("dragon"));
            if (is_bet)
                HelloApplication.menu = new M_Bet();
            else
                HelloApplication.menu = new M_Game();
        } else {
            loggedInUser.setGameCharacter(new GameCharacter("dragon"));
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
            secondUser.setGameCharacter(new GameCharacter("robot"));
            if (is_bet)
                HelloApplication.menu = new M_Bet();
            else
                HelloApplication.menu = new M_Game();
        } else {
            loggedInUser.setGameCharacter(new GameCharacter("robot"));
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
            secondUser.setGameCharacter(new GameCharacter("wolf"));
            if (is_bet)
                HelloApplication.menu = new M_Bet();
            else
                HelloApplication.menu = new M_Game();
        } else {
            loggedInUser.setGameCharacter(new GameCharacter("wolf"));
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
}
