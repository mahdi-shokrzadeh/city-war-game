package models;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {
    public FXMLLoader background ;
    String name;//for console version
    ArrayList<Pattern> patterns;
    Matcher matcher;
    static User loggedInUser = null;

    public void myNameIs(){
        System.out.println(name);
    }//for console version
    public abstract Menu myMethods(String input);
    Menu(){};
    Menu(String name){
        this.name = name;
        try {
            background = FXMLLoader.load(getClass().getResource(name+".fxml"));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
//    public Parent getBackground(){
//        return background;
//    }
}
