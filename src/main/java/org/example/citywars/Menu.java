package org.example.citywars;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
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
    static int lastMenu;
    int soundIndex=1;
    static MediaPlayer outPutMusic;
    static ArrayList<File> BGMusicFiles;
    static ArrayList<Media> BGMusicMedias;

//    static ArrayList<MediaPlayer> BGMusics;
    static File[] CharImageFiles;
    static Image[] charsImages;
    static int themeIndex=0;
    ArrayList<File> files;
     Media media;
     MediaPlayer mediaPlayer;
     @FXML
     MediaView backGround;
    ArrayList<Image> BGims;
    @FXML
    ImageView backGroundIm;
    Parent root;
    Stage stage;
    Scene scene;
    private String name;
    ArrayList<Pattern> patterns;
    Matcher matcher;
    static User loggedInUser ;
    static User secondUser ;
    static boolean secondPersonNeeded;
    static boolean is_bet=false;
    P_PlayMode playMode;
    protected final Scanner consoleScanner = new Scanner(System.in);

    public abstract Menu myMethods();
    public Menu(){};

    public Menu(String name,String[] BG){
        this.name = name;
        files = new ArrayList<>();
        BGims= new ArrayList<>();
        for (int i = 0; i < BG.length; i++) {
            files.add(new File("src\\main\\resources\\"+BG[i]));
            BGims.add( new Image(files.get(i).toURI().toString()));
        }
    }
    public Menu(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    void switchMenus(Event event) throws IOException {
        System.out.println(HelloApplication.menu.getName());////////////////
//        stage.setFullScreen(true);

        root =  FXMLLoader.load(getClass().getResource(HelloApplication.menu.getName()+".fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle(HelloApplication.menu.getName());
        stage.getIcons().add(icon);
//        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
    void switchMenus() throws IOException {
        System.out.println(HelloApplication.menu.getName());////////////////
//        stage.setFullScreen(true);

        root =  FXMLLoader.load(getClass().getResource(HelloApplication.menu.getName()+".fxml"));
        // stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        // stage.setTitle(HelloApplication.menu.getName());
        // stage.getIcons().add(icon);
        //  stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
    public static String passwordProblem(String s){
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

            backGroundIm.setImage(BGims.get(themeIndex));
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
    @FXML
    protected void GoToPauseButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_PauseMenu();
        switchMenus(event);
    }
    @FXML
    protected void GoToShopButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_ShopMenu();
        switchMenus(event);
    }
    @FXML
    protected void GoToCharChoiceButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_CharacterChoice();
        switchMenus(event);
    }
    @FXML
    protected void GoToGameHistoryButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_GameHistoryMenu();
        switchMenus(event);
    }
    @FXML
    protected void GoToGamePlayModeButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_GamePlayMenu();
        switchMenus(event);
    }
    @FXML
    protected void GoToGameMainMenuButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_GameMainMenu();
        switchMenus(event);
    }

    @FXML
    protected void GoToGameButton(ActionEvent event) throws IOException {
        HelloApplication.menu = new M_Game();
        switchMenus(event);
    }
}
