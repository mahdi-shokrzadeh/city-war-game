package models;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class AA_CaptchaChar {
    ArrayList<String> lines;
    String name;
    AA_CaptchaChar(String name ,int lineLength, String art){
        lines=new ArrayList<>();
        this.name = name;
        int n = art.length()/lineLength;
        for (int i = 0; i < n; i++) {
            lines.add(art.substring(i*lineLength,(i+1)*lineLength));
        }
    }
    AA_CaptchaChar(String fileName){
        File parentDir = new File("./src/main/java/models/asciiArt");
        File file = new File(parentDir, fileName);
        lines=new ArrayList<>();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()){
                lines.add(sc.nextLine());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
