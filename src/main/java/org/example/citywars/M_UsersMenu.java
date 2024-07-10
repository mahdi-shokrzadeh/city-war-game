package org.example.citywars;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import controllers.UserController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import models.Response;
import models.User;

public class M_UsersMenu extends Menu {

    @FXML
    private TableView<User> usersTable;
    @FXML
    private Pane pane;

    public M_UsersMenu() {
        super("M_UsersMenu");
    }

    public Menu myMethods() {
        return this;
    }

    public void Back(Event event) throws IOException {
        HelloApplication.menu = new M_AdminMenu();
        switchMenus(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                User _user = new User("admin", "Admin1!", "admin", "admin@gmail.com", "admin", "recQuestion",
                        "recAnswer");
                Response res = UserController.getAllUsers(_user);
                Alert alert = new Alert(AlertType.NONE);
                List<User> allUsers = null;
                if (!res.ok) {
                    alert.setAlertType(AlertType.ERROR);
                    alert.setContentText(res.message);
                    if (res.exception != null) {
                        System.out.println(res.exception.getMessage());
                    }
                }
                for (TableColumn<User, ?> tc : usersTable.getColumns()) {
                    if (tc.getText().equals("Username")) {
                        tc.setCellValueFactory(new PropertyValueFactory<>("username"));
                    } else if (tc.getText().equals("Nickname")) {
                        tc.setCellValueFactory(new PropertyValueFactory<>("nickname"));
                    } else if (tc.getText().equals("Role")) {
                        tc.setCellValueFactory(new PropertyValueFactory<>("role"));
                    } else if (tc.getText().equals("Email")) {
                        tc.setCellValueFactory(new PropertyValueFactory<>("level"));
                    } else if (tc.getText().equals("Exp")) {
                        tc.setCellValueFactory(new PropertyValueFactory<>("experience"));
                    } else if (tc.getText().equals("HP")) {
                        tc.setCellValueFactory(new PropertyValueFactory<>("hitPoints"));
                    } else if (tc.getText().equals("Coins")) {
                        tc.setCellValueFactory(new PropertyValueFactory<>("coins"));
                    } else if (tc.getText().equals("Clan ID")) {
                        tc.setCellValueFactory(new PropertyValueFactory<>("clanID"));
                    }
                }
                allUsers = (List<User>) res.body.get("allUsers");
                try {
                    ObservableList<User> observableUsersList = FXCollections.observableList(allUsers);
                    usersTable.setItems(observableUsersList);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

}
