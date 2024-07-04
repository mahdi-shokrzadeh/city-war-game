package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import models.P_PlayMode;
import models.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.citywars.HelloApplication.icon;

public abstract class Menu implements Initializable {
    boolean BGisImage = true;
     File file;
     Media media;
     MediaPlayer mediaPlayer;
     @FXML
     MediaView backGround;
    Image BGim;
    @FXML
    ImageView backGroundIm;
    Parent root;
    Stage stage;
    Scene scene;
    private String name;
    ArrayList<Pattern> patterns;
    Matcher matcher;
    static User loggedInUser ;
    static boolean secondPersonNeeded;
    P_PlayMode playMode;
    protected final Scanner consoleScanner = new Scanner(System.in);

    public abstract Menu myMethods();
    public Menu(){};
    public Menu(String name,boolean isImage,String BG){
        this.name = name;
        this.BGisImage = isImage;
        file= new File("src\\main\\resources\\"+BG);
    }
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
        stage.getIcons().add(icon);
        stage.setScene(scene);
//        stage.setFullScreen(true);
        stage.show();
    }
    String passwordProblem(String s){
        if (s.isBlank())
            return "Blank Field!";
        else if(!s.matches(".*[a-z]+.*") || !s.matches(".*[A-Z]+.*"))
            return "Password must have at least an upper case and a lower case letter!";
        else if(!s.matches(".+[0-9]+.*"))
            return "Password must have at least a number!";
        else if(!s.matches(".*[!@#$%^&*]+.*"))
            return "Password must have at least a non-alphanumeric character!";
        else if(s.contains(" "))
            return "Password mustn't have any space!";
        else if (s.length() < 8 || s.length() > 24)
            return "Password must have between 8 and 24 characters!";

        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (!BGisImage) {
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            backGround.setMediaPlayer(mediaPlayer);

            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(-1);
        }
        else {
            BGim = new Image(file.toURI().toString());
            backGroundIm.setImage(BGim);

        }
    }

    //Control Methods
    @FXML
    protected void GoToSignUpButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_SignUpMenu();
        switchMenus(event);
    }

    @FXML
    protected void GoToLoginButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_LoginMenu();
        switchMenus(event);
    }
    @FXML
    protected void GoToIntroButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_Intro();
        switchMenus(event);
    }
}
