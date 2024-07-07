package database.DBs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.citywars.M_Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameDB {
    private final ObjectMapper mapper;
    private List<M_Game> data;
    public GameDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/games.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<M_Game>>(){});
            }
        }catch (Exception e){
            System.out.println("Exception in GameDB: " +e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/games.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println("Exception in GameDB: " +e.getMessage());
        }
    }
    public int create(M_Game game){
        int id = -1;
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID() + 1;
            }
            game.setID(id);
            data.add(game);
            save();
        return id;
    }
    public M_Game getOne(int id){
        M_Game game = null;
        game = data.stream().filter(o -> o.getID() == id).findFirst().orElse(null);
        return game;
    }
    public List<M_Game> getByUserID(int id){
        List<M_Game> games = null;
        games = data.stream().filter(o -> o.getPlayer_one_id() == id || o.getPlayer_two_id() == id).toList();
        return games;
    }
    public List<M_Game> getAll(){
        return data;
    }
    public void update(M_Game game, int id) {
        data.replaceAll(o -> o.getID() == id ? game: o);
        save();
    }
    public void delete(int id){
        data.removeIf(o -> o.getID() == id);
        save();
    }
}
