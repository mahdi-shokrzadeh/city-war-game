package database.DBs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.ClanBattle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClanBattleDB {
    private final ObjectMapper mapper;
    private List<ClanBattle> data;
    public ClanBattleDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/clanBattles.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<ClanBattle>>(){});
            }
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/clanBattles.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public int create(ClanBattle battle){
        int id = -1;
        try {
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID();
            }
            battle.setID(id);
            data.add(battle);
            save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    public ClanBattle getOne(int id){
        ClanBattle battle = null;
        try{
            battle = data.stream().filter(o -> o.getID() == id).toList().getFirst();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return battle;
    }
    public ClanBattle firstWhereEqualsOr(int id){
        ClanBattle battle = null;
        try{
            battle = data.stream().filter(o -> (o.getAttackerID() == id || o.getDefenderID() == id) && !o.hasEnded()).toList().getFirst();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return battle;
    }
    public List<ClanBattle> whereEqualsOr(int id){
        List<ClanBattle> battles = null;
        try{
            battles = data.stream().filter(o -> o.getAttackerID() == id || o.getDefenderID() == id).toList();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return battles;
    }
    public List<ClanBattle> getAll(){
        return data;
    }
    public void update(ClanBattle battle, int id) {
        try{
            data.replaceAll(o -> o.getID() == id ? battle: o);
            save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void delete(int id){
        data.removeIf(o -> o.getID() == id);
        save();
    }
}
