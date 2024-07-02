package database.DBs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.ClanGame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClanGameDB {
    private final ObjectMapper mapper;
    private List<ClanGame> data;
    public ClanGameDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/clanGames.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<ClanGame>>(){});
            }
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/clanGames.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public int create(ClanGame clanGame){
        int id = -1;
        try {
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID();
            }
            clanGame.setID(id);
            data.add(clanGame);
            save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    public ClanGame getOne(int id){
        ClanGame clanGame = null;
        try{
            clanGame = data.stream().filter(o -> o.getID() == id).toList().getFirst();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return clanGame;
    }
    public List<ClanGame> getAll(){
        return data;
    }
    public void update(ClanGame clanGame, int id) {
        try{
            data.replaceAll(o -> o.getID() == id ? clanGame: o);
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
