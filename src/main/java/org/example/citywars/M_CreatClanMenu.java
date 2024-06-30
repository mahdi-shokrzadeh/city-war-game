package org.example.citywars;

import java.util.regex.Pattern;

public class M_CreatClanMenu extends Menu{
    public M_CreatClanMenu(){
        super("M_ClanMainMenu");
        patterns.add(Pattern.compile("^ *-Create +a +clan +(?<clanName>[\\S ]+) *$"));

    }
    public Menu myMethods(String input){
        matcher = patterns.get(0).matcher(input);
        if (matcher.find()) {
            // join clans matcher.group("clanName")
            return new M_MyClanMenu();
        }

        System.out.println("invalid command!");
        return this;
    }
}
