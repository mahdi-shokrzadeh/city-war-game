package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {
    Parent root;
    Stage stage;
    Scene scene;
    private String name;
    ArrayList<Pattern> patterns;
    Matcher matcher;
    static User loggedInUser = null;

    public abstract Menu myMethods(String input);
    public Menu(){};
    public Menu(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    void switchMenus(ActionEvent event) throws IOException {
        root =  FXMLLoader.load(getClass().getResource(HelloApplication.menu.getName()+".fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle(HelloApplication.menu.getName());
        stage.setScene(scene);
        stage.show();
    }
}
