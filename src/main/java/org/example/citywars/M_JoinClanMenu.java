package org.example.citywars;

import java.util.regex.Pattern;

public class M_JoinClanMenu extends Menu{
    public M_JoinClanMenu(){
        super("M_ClanMainMenu");
        patterns.add(Pattern.compile("^ *-Join +(?<clanName>[\\S ]+) *$"));
    }
    public Menu myMethods(String input){
        if (input.toLowerCase().matches("^ *-all +clans *$")){
            // show clans
            return this;
        } else if (input.matches(patterns.get(0).toString())){
            matcher = patterns.get(0).matcher(input);
            if (matcher.find()) {
                // join clans matcher.group("clanName")
            }
            return new M_MyClanMenu();
        }

        System.out.println("invalid command!");
        return this;
    }
}
