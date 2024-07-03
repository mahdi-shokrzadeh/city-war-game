package database.DBs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Clan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClanDB {
    private final ObjectMapper mapper;
    private List<Clan> data;
    public ClanDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/clans.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<Clan>>(){});
            }
        }catch (Exception e){
            System.out.print("Exception in ClanDB: " +e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/clans.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println("Exception in ClanDB: " +e.getMessage());
        }
    }
    public int create(Clan clan){
        int id = -1;
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID();
            }
            clan.setID(id);
            data.add(clan);
            save();
        return id;
    }
    public Clan getOne(int id){
        Clan clan = null;
        clan = data.stream().filter(o -> o.getID() == id).findFirst().orElse(null);
        return clan;
    }
    public Clan getByName(String  name){
        Clan clan = null;
        clan = data.stream().filter(o -> o.getName().equals(name)).findFirst().orElse(null);
        return clan;
    }
    public Clan getByBattleKey(String key){
        Clan clan = null;
        clan = data.stream().filter(o -> o.getBattleKey().equals(key) ).findFirst().orElse(null);
        return clan;
    }
    public List<Clan> getAll(){
        return data;
    }
    public void update(Clan clan, int id) {
        data.replaceAll(o -> o.getID() == id ? clan: o);
        save();
    }
    public void delete(int id){
        data.removeIf(o -> o.getID() == id);
        save();
    }
    public void deleteByName(String name){
        data.removeIf(o -> o.getName().equals(name) );
        save();
    }
}
