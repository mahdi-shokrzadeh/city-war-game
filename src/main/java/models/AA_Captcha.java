package models;

import java.util.ArrayList;
import java.util.Random;

public class AA_Captcha {
    ArrayList<AA_CaptchaChar> equation;
    private int answer;

    public AA_Captcha() {
        equation=new ArrayList<>();
        Random generator = new Random();
        int rand1 = generator.nextInt(10);
        int rand2 = generator.nextInt(10);
        int rand3 = generator.nextInt(10);

        equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\(.txt"));
        equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\" + rand1 + ".txt"));

        int op1 = generator.nextInt(3);
        switch (op1) {
            case 0:
                answer = rand1 + rand2;
                equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\+2.txt"));
                break;
            case 1:
                answer = rand1 - rand2;
                equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\-2.txt"));
                break;
            case 2:
                answer = rand1 * rand2;
                equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\x2.txt"));
                break;
        }

        equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\" + rand2 + ".txt"));
        equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\).txt"));

        int op2 = generator.nextInt(3);
        switch (op2) {
            case 0:
                answer += rand3;
                equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\+1.txt"));
                break;
            case 1:
                answer -= rand3;
                equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\-1.txt"));
                break;
            case 2:
                answer *= rand3;
                equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\x1.txt"));
                break;
        }

        equation.add(new AA_CaptchaChar("src\\main\\java\\models\\asciiArt\\" + rand3 + ".txt"));
    }

    public String showEquation(){
        String output = "";
        for (int i = 0; i < 6; i++) {
            for (AA_CaptchaChar ch : equation){
                output += ch.lines.get(i);
                output += "   ";
            }
            output += "\n";
        }

        return output;
    }

    public int getAnswer() {
        return answer;
    }
}
