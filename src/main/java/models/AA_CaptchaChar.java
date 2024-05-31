package models;

import java.util.ArrayList;

public class AA_CaptchaChar {
    ArrayList<String> lines;
    String name;
    AA_CaptchaChar(String name ,int lineLength, String art){
        this.name = name;
        int n = art.length()/lineLength;
        for (int i = 0; i < n; i++) {
            lines.add(art.substring(i*lineLength,(i+1)*lineLength));
        }
    }
}
