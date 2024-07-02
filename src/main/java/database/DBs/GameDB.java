package database.DBs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.game.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameDB {
    private final ObjectMapper mapper;
    private List<Game> data;
    public GameDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/games.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<Game>>(){});
            }
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/games.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public int create(Game game){
        int id = -1;
        try {
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID();
            }
            game.setID(id);
            data.add(game);
            save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    public Game getOne(int id){
        Game game = null;
        try{
            game = data.stream().filter(o -> o.getID() == id).toList().getFirst();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return game;
    }
    public List<Game> getByUserID(int id){
        List<Game> games = null;
        try{
            games = data.stream().filter(o -> o.getPlayer_one_id() == id || o.getPlayer_two_id() == id).toList();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return games;
    }
    public List<Game> getAll(){
        return data;
    }
    public void update(Game game, int id) {
        try{
            data.replaceAll(o -> o.getID() == id ? game: o);
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
