package org.example.citywars;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
}
