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
            System.out.print("Exception in ClanBattleDB: " +e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/clanBattles.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println("Exception in ClanBattleDB: " +e.getMessage());
        }
    }
    public int create(ClanBattle battle){
        int id = -1;
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID();
            }
            battle.setID(id);
            data.add(battle);
            save();
        return id;
    }
    public ClanBattle getOne(int id){
        ClanBattle battle = null;
        battle = data.stream().filter(o -> o.getID() == id).findFirst().orElse(null);
        return battle;
    }
    public ClanBattle firstWhereEqualsOr(int id){
        ClanBattle battle = null;
        battle = data.stream().filter(o -> (o.getAttackerID() == id || o.getDefenderID() == id) && !o.hasEnded()).findFirst().orElse(null);
        return battle;
    }
    public List<ClanBattle> whereEqualsOr(int id){
        List<ClanBattle> battles = null;
        battles = data.stream().filter(o -> o.getAttackerID() == id || o.getDefenderID() == id).toList();
        return battles;
    }
    public List<ClanBattle> getAll(){
        return data;
    }
    public void update(ClanBattle battle, int id) {
        data.replaceAll(o -> o.getID() == id ? battle: o);
        save();
    }
    public void delete(int id){
        data.removeIf(o -> o.getID() == id);
        save();
    }
}
