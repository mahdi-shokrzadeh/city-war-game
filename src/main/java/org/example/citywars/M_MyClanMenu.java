package org.example.citywars;

import java.util.regex.Pattern;

public class M_MyClanMenu extends Menu{
    public M_MyClanMenu(){
        super("M_ClanMainMenu");
        patterns.add(Pattern.compile("^ *-Play +(?<clanName>[\\S ]+) *$"));
    }
    public Menu myMethods(String input){
        matcher = patterns.get(0).matcher(input);
        if (matcher.find()) {
            // join clans (matcher.group("clanName"))
            return new M_LoginMenu();
        }

        if (input.toLowerCase().matches("^ *-play +clanwar *$"))
            return new M_LoginMenu();

        System.out.println("invalid command!");
        return this;
    }
}
