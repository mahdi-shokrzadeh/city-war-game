package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.Response;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static controllers.UserController.userDB;

public class M_LevelMap extends Menu{
    public M_LevelMap() {
        super("M_LevelMap", new String[]{"BG-Videos\\levelmap\\l1.png",
                "BG-Videos\\levelmap\\l2.png",
                "BG-Videos\\levelmap\\l3.png",
                "BG-Videos\\levelmap\\l4.png",
                "BG-Videos\\levelmap\\l5.png"});
    }
    @Override
    public Menu myMethods() {
        return null;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        backGroundIm.setImage(BGims.get(loggedInUser.getProgress()-1));

    }

    @FXML
    protected void restart(ActionEvent event) throws IOException {
        loggedInUser.resetProgress();
        backGroundIm.setImage(BGims.get(loggedInUser.getProgress()-1));

        User user=null;
        try {
            user = userDB.getOne(loggedInUser.getID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user == null) {
            System.out.println("null");
        }

        user.resetProgress();
        try {
            userDB.update(user, user.getProgress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
