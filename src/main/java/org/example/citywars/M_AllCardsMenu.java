package org.example.citywars;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import models.card.Card;

import controllers.CardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.scene.control.Button;
import models.Response;
import models.User;

public class M_AllCardsMenu extends Menu {

    @FXML
    private Pane pane;

    private TableView<Card> tableView;

    public M_AllCardsMenu() {
        super("M_AllCardsMenu");
    }

    public Menu myMethods() {
        return this;
    }

    public void Back(Event event) throws IOException {
        HelloApplication.menu = new M_AdminMenu();
        switchMenus(event);
    }

    private void addEditButton() {
        TableColumn<Card, Void> colBtn = new TableColumn<>("Edit");

        Callback<TableColumn<Card, Void>, TableCell<Card, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Card, Void> call(final TableColumn<Card, Void> param) {
                final TableCell<Card, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Edit");

                    {
                        btn.setPrefWidth(80);
                        btn.setBackground(Background.fill(Paint.valueOf("#8900AC")));
                        btn.setTextFill(Paint.valueOf("white"));
                        btn.setOnMouseClicked((Event event) -> {
                            editingCard = getTableView().getItems().get(getIndex());
                            HelloApplication.menu = new M_EditCardMenu();
                            try {
                                switchMenus(event);
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        tableView.getColumns().add(colBtn);

    }

    private void addDeleteButton() {
        TableColumn<Card, Void> colBtn = new TableColumn<>("Button Column");

        Callback<TableColumn<Card, Void>, TableCell<Card, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Card, Void> call(final TableColumn<Card, Void> param) {
                final TableCell<Card, Void> cell = new TableCell<Card, Void>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setPrefWidth(80);
                        btn.setBackground(Background.fill(Paint.valueOf("red")));
                        btn.setTextFill(Paint.valueOf("white"));
                        btn.setOnAction((ActionEvent event) -> {
                            Card _card = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(AlertType.CONFIRMATION);
                            alert.setContentText("are you sure you want to delete this card?");
                            alert.showAndWait().ifPresent(res -> {
                                if (res.getButtonData().equals(ButtonData.OK_DONE)) {
                                    System.out.println("please delete card");
                                }
                            });
                            ;
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        tableView.getColumns().add(colBtn);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableView = new TableView<>();
        Response res = CardController.getAllCards();
        Alert alert = new Alert(AlertType.NONE);
        List<Card> allCards = null;
        if (res.ok) {
            allCards = (List<Card>) res.body.get("allCards");
        } else {
            alert.setAlertType(AlertType.ERROR);
            alert.setContentText(res.message);
            alert.show();
        }

        TableColumn<Card, String> nameCol = new TableColumn<>("Name");
        TableColumn<Card, Integer> durationCol = new TableColumn<>("Duration");
        TableColumn<Card, String> cardTypeCol = new TableColumn<>("Card Type");
        TableColumn<Card, String> characterCol = new TableColumn<>("Character");
        TableColumn<Card, Integer> powerColumn = new TableColumn<>("Power");
        TableColumn<Card, Integer> damageCol = new TableColumn<>("Damage");

        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(durationCol);
        tableView.getColumns().add(cardTypeCol);
        tableView.getColumns().add(characterCol);
        tableView.getColumns().add(powerColumn);
        tableView.getColumns().add(damageCol);
        addEditButton();
        addDeleteButton();

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        cardTypeCol.setCellValueFactory(new PropertyValueFactory<>("cardType"));
        characterCol.setCellValueFactory(new PropertyValueFactory<>("character"));
        powerColumn.setCellValueFactory(new PropertyValueFactory<>("power"));
        damageCol.setCellValueFactory(new PropertyValueFactory<>("damage"));

        ObservableList<Card> observableCardsList = FXCollections.observableList(allCards);
        tableView.setItems(observableCardsList);
        tableView.setPrefWidth(790);
        tableView.setPrefHeight(600);
        tableView.setTranslateY(80);
        tableView.setTranslateX(55);

        pane.getChildren().add(tableView);

    }

}
