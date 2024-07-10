package org.example.citywars;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

import models.card.Card;

import controllers.CardController;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Response;
import models.User;

public class M_EditCardMenu extends Menu {

    @FXML
    private ImageView preview;
    @FXML
    private Button pandaButton;
    @FXML
    private Button wolfButton;
    @FXML
    private Button warriorButton;
    @FXML
    private Button dragonButton;
    @FXML
    private GridPane form;
    @FXML
    private TextField searchBar;

    private String cardName;
    private String cardPower;
    private String cardDuration;
    private String cardDamage;
    private String cardUpgradeLevel;
    private String cardUpgradeCost;
    private String cardPrice;
    private String cardDescription;
    private String cardCharacter;
    private File uploadedFile;
    private Button[] buttons;
    private Card card;

    public M_EditCardMenu() {
        super("M_EditCardMenu",
                new String[] { "BG-Videos/GameBGs/bg1.png", "BG-Videos/GameBGs/bg2.png", "BG-Videos/GameBGs/bg3.png" });
    }

    public Menu myMethods() {
        return this;
    }

    public void handleNameChange(Event event) {
        TextField target = (TextField) event.getTarget();
        cardName = target.getText();
    }

    public void handleDurationChange(Event event) {
        TextField target = (TextField) event.getTarget();
        cardDuration = target.getText();
    }

    public void handlePowerChange(Event event) {
        TextField target = (TextField) event.getTarget();
        cardPower = target.getText();
    }

    public void handleDamageChange(Event event) {
        TextField target = (TextField) event.getTarget();
        cardDamage = target.getText();
    }

    public void handleUpgradeLevelChange(Event event) {
        TextField target = (TextField) event.getTarget();
        cardUpgradeLevel = target.getText();
    }

    public void handleUpgradeCostChange(Event event) {
        TextField target = (TextField) event.getTarget();
        cardUpgradeCost = target.getText();
    }

    public void handlePriceChange(Event event) {
        TextField target = (TextField) event.getTarget();
        cardPrice = target.getText();
    }

    public void handleDescriptionChange(Event event) {
        TextField target = (TextField) event.getTarget();
        cardDescription = target.getText();
    }

    public void selectPanda() {
        cardCharacter = "panda";
        handleOpacity();
    }

    public void selectWolf() {
        cardCharacter = "wolf";
        handleOpacity();
    }

    public void selectWarrior() {
        cardCharacter = "warrior";
        handleOpacity();
    }

    public void selectDragon() {
        cardCharacter = "dragon";
        handleOpacity();
    }

    public void handleChooseFileClick(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }
        uploadedFile = new File("src/main/resources/Cards/" + file.getName());
        try {
            Files.copy(file.toPath(), uploadedFile.toPath());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        Image image;
        try {
            image = new Image(new FileInputStream(uploadedFile), preview.getFitWidth(), preview.getFitHeight(), false,
                    false);
            preview.setImage(image);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleAddCardClick(Event event) {
        Alert alert = new Alert(AlertType.NONE);
        alert.setAlertType(AlertType.ERROR);
        if (!cardName.matches("^[a-zA-Z]+$")) {
            alert.setContentText("card name should contain alphabet only");
            alert.show();
            return;
        }
        if (!cardDuration.matches("^[12345]$")) {
            alert.setContentText("card duration should be a number and within range 1 to 5");
            alert.show();
            return;
        }
        if (!cardDamage.matches("^[0-9]+$") || Integer.parseInt(cardDamage) < 10 || Integer.parseInt(cardDamage) > 50) {
            alert.setContentText("card damage should be a number and within range 10 to 50");
            alert.show();
            return;
        }
        if (!cardPower.matches("^[0-9]+$") || Integer.parseInt(cardPower) < 10 || Integer.parseInt(cardPower) > 100) {
            alert.setContentText("card power should be a number and within range 10 to 100");
            alert.show();
            return;
        }
        if (!cardUpgradeCost.matches("^[0-9]+$")) {
            alert.setContentText("card upgrade cost should be a number");
            alert.show();
            return;
        }
        if (!cardUpgradeLevel.matches("^[0-9]+$")) {
            alert.setContentText("card upgrade level should be a number");
            alert.show();
            return;
        }
        if (!cardPrice.matches("^[0-9]+$")) {
            alert.setContentText("card price should be a number");
            alert.show();
            return;
        }
        if (cardDescription == null || cardDescription.isBlank()) {
            cardDescription = new String();
        }
        if (cardCharacter == null || cardCharacter.isBlank()) {
            alert.setContentText("card character can not be blank");
            alert.show();
            return;
        }
        if (uploadedFile == null || !uploadedFile.isFile()) {
            alert.setContentText("you should upload an image as the background");
            alert.show();
            return;
        }
        User _user = new User("admin", "Admin1!", "admin", "admin@gmail.com", "admin", "recQuestion", "recAnswer");
        System.out.println(card.getID());
        Response res = CardController.editCard(_user, card.getID(), cardName,
                Integer.parseInt(cardPrice),
                Integer.parseInt(cardDuration),
                Integer.parseInt(cardPower), Integer.parseInt(cardDamage),
                Integer.parseInt(cardUpgradeLevel),
                Integer.parseInt(cardUpgradeCost), cardDescription, cardCharacter,
                "src/main/resources/Cards/" + uploadedFile.getName());
        if (res.ok) {
            alert.setAlertType(AlertType.INFORMATION);
            alert.setContentText("card edited successfully");
            alert.show();
        } else {
            alert.setAlertType(AlertType.ERROR);
            alert.setContentText(res.message);
            alert.show();
            if (res.exception != null) {
                System.out.println(res.exception.getMessage());
            }
        }

    }

    public void handleSearch(Event event) {
        String input = searchBar.getText();
        if (input.matches("^[a-zA-Z]+$")) {
            Response res = CardController.getCardByName(input);
            if (res.ok) {
                card = (Card) res.body.get("card");
                form.setVisible(true);
            } else {
                System.out.println(res.message);
                form.setVisible(false);
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Oops, card not found");
                alert.show();
                return;
            }
        }

        for (Node node : form.getChildren()) {
            if ((node.getClass().getSimpleName().equals("TextField")
                    || node.getClass().getSimpleName().equals("TextArea")) && !node.getId().equals("searchBar")) {
                if (node.getId().equals("name")) {
                    ((TextField) node).setText(card.getName());
                    cardName = card.getName();
                } else if (node.getId().equals("price")) {
                    ((TextField) node).setText(String.valueOf(card.getPrice()));
                    cardPrice = String.valueOf(card.getPrice());
                } else if (node.getId().equals("duration")) {
                    ((TextField) node).setText(String.valueOf(card.getDuration()));
                    cardDuration = String.valueOf(card.getDuration());
                } else if (node.getId().equals("damage")) {
                    ((TextField) node).setText(String.valueOf(card.getDamage()));
                    cardDamage = String.valueOf(card.getDamage());
                } else if (node.getId().equals("power")) {
                    ((TextField) node).setText(String.valueOf(card.getPower()));
                    cardPower = String.valueOf(card.getPower());
                } else if (node.getId().equals("upgradeLevel")) {
                    ((TextField) node).setText(String.valueOf(card.getUpgradeLevel()));
                    cardUpgradeLevel = String.valueOf(card.getUpgradeLevel());
                } else if (node.getId().equals("upgradeCost")) {
                    ((TextField) node).setText(String.valueOf(card.getUpgradeCost()));
                    cardUpgradeCost = String.valueOf(card.getUpgradeCost());
                } else if (node.getId().equals("description")) {
                    ((TextArea) node).setText(card.getDesc());
                    cardDescription = card.getDesc();
                }
            }
        }

        cardCharacter = card.getCharacter().getName();
        handleOpacity();
        if (card.getImageURL() == null) {
            uploadedFile = new File("src/main/resources/Cards/" + card.getCharacter().getName()
                    + "_d" + String.valueOf(card.getDuration()) + "_l1.png");
        } else {
            uploadedFile = new File(card.getImageURL());
        }
        Image image;
        try {
            image = new Image(new FileInputStream(uploadedFile), preview.getFitWidth(), preview.getFitHeight(), false,
                    false);
            preview.setImage(image);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void fillFields() {
        for (Node node : form.getChildren()) {
            if ((node.getClass().getSimpleName().equals("TextField")
                    || node.getClass().getSimpleName().equals("TextArea")) && !node.getId().equals("searchBar")) {
                if (node.getId().equals("name")) {
                    ((TextField) node).setText(card.getName());
                    cardName = card.getName();
                } else if (node.getId().equals("price")) {
                    ((TextField) node).setText(String.valueOf(card.getPrice()));
                    cardPrice = String.valueOf(card.getPrice());
                } else if (node.getId().equals("duration")) {
                    ((TextField) node).setText(String.valueOf(card.getDuration()));
                    cardDuration = String.valueOf(card.getDuration());
                } else if (node.getId().equals("damage")) {
                    ((TextField) node).setText(String.valueOf(card.getDamage()));
                    cardDamage = String.valueOf(card.getDamage());
                } else if (node.getId().equals("power")) {
                    ((TextField) node).setText(String.valueOf(card.getPower()));
                    cardPower = String.valueOf(card.getPower());
                } else if (node.getId().equals("upgradeLevel")) {
                    ((TextField) node).setText(String.valueOf(card.getUpgradeLevel()));
                    cardUpgradeLevel = String.valueOf(card.getUpgradeLevel());
                } else if (node.getId().equals("upgradeCost")) {
                    ((TextField) node).setText(String.valueOf(card.getUpgradeCost()));
                    cardUpgradeCost = String.valueOf(card.getUpgradeCost());
                } else if (node.getId().equals("description")) {
                    ((TextArea) node).setText(card.getDesc());
                    cardDescription = card.getDesc();
                }
            }
        }

        cardCharacter = card.getCharacter().getName();
        handleOpacity();
        if (card.getImageURL() == null) {
            uploadedFile = new File("src/main/resources/Cards/" + card.getCharacter().getName()
                    + "_d" + String.valueOf(card.getDuration()) + "_l1.png");
        } else {
            uploadedFile = new File(card.getImageURL());
        }
        Image image;
        try {
            image = new Image(new FileInputStream(uploadedFile), preview.getFitWidth(), preview.getFitHeight(), false,
                    false);
            preview.setImage(image);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleOpacity() {
        if (cardCharacter.equals("panda")) {
            for (Button b : buttons) {
                b.setOpacity(b.getId().equals("pandaButton") ? 1 : 0.5);
            }
        } else if (cardCharacter.equals("wolf")) {
            for (Button b : buttons) {
                b.setOpacity(b.getId().equals("wolfButton") ? 1 : 0.5);
            }
        } else if (cardCharacter.equals("warrior")) {
            for (Button b : buttons) {
                b.setOpacity(b.getId().equals("warriorButton") ? 1 : 0.5);
            }
        } else if (cardCharacter.equals("dragon")) {
            for (Button b : buttons) {
                b.setOpacity(b.getId().equals("dragonButton") ? 1 : 0.5);
            }
        }
    }

    public void Back(Event event) throws IOException {
        HelloApplication.menu = new M_AdminMenu();
        switchMenus(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        backGroundIm.setImage(BGims.get(themeIndex));

        buttons = new Button[4];
        buttons[0] = pandaButton;
        buttons[1] = wolfButton;
        buttons[2] = warriorButton;
        buttons[3] = dragonButton;
        for (Button b : buttons) {
            b.setOpacity(0.5);
        }

        if (editingCard == null) {
            form.setVisible(false);
        } else {
            card = editingCard;
            fillFields();
        }

    }

}
