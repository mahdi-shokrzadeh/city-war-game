package org.example.citywars;

import com.almasb.fxgl.audio.AudioPlayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class M_Setting extends Menu{
    @FXML
    private ChoiceBox<Integer> chooseMusic;

    @FXML
    private ToggleGroup modes;

    @FXML
    private ToggleButton toggle1;

    @FXML
    private ToggleButton toggle2;

    @FXML
    private Slider volume;

    @FXML
    void toggleButtons(ActionEvent event) {
        if (event.getSource()==toggle1){
            themeIndex=0;
        }
        if (event.getSource()==toggle2){
            themeIndex=1;
        }
        System.out.println(themeIndex);
        backGroundIm.setImage(BGims.get(themeIndex));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (themeIndex==0)
           toggle1.setSelected(true);
        else
            toggle2.setSelected(true);
        backGroundIm.setImage(BGims.get(themeIndex));

        chooseMusic.getItems().addAll(1, 2,3);
        chooseMusic.setValue(soundIndex);

        volume.setBlockIncrement(outPutMusic.getVolume());

        volume.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                    outPutMusic.setVolume((int) volume.getValue() / 100.0);
            }
        });
    }
    @FXML
    protected void OkButton(ActionEvent event) throws IOException {
        outPutMusic.stop();
        soundIndex=chooseMusic.getValue()-1;
        outPutMusic=new MediaPlayer(BGMusicMedias.get(soundIndex));
        outPutMusic.play();
        outPutMusic.setCycleCount(-1);

    }
    @FXML
    protected void BackButton(ActionEvent event) throws IOException {
        if (lastMenu==1) {
            HelloApplication.menu = new M_GameMainMenu();
        }
        else if (lastMenu==2)
            HelloApplication.menu=new M_PauseMenu();
        switchMenus(event);
    }
    public M_Setting(){
        super("M_Setting", new String[]{"BG-Videos\\shopMenu.png","BG-Videos\\lightmode.png"});
    }

    @Override
    public Menu myMethods() {
        return null;
    }
}
