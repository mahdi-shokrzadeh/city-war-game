package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class M_PauseMenu extends Menu{
    public M_PauseMenu() {
        super("M_PauseMenu", new String[]{"BG-Videos\\BG-signUp.png","BG-Videos\\lightmode.png"});
    }
    @FXML
    protected void GoToSettingButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_Setting();
        lastMenu=2;
        switchMenus(event);
    }
    @Override
    public Menu myMethods() {
        return null;
    }
}
