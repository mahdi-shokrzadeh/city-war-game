package database.DBs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.GameCharacter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameCharacterDB {
    private final ObjectMapper mapper;
    private List<GameCharacter> data;
    public GameCharacterDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/gameCharacters.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<GameCharacter>>(){});
            }
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/gameCharacters.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public int create(GameCharacter gameCharacter){
        int id = -1;
        try {
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID();
            }
            gameCharacter.setID(id);
            data.add(gameCharacter);
            save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    public GameCharacter getOne(int id){
        GameCharacter gameCharacter = null;
        try{
            gameCharacter = data.stream().filter(o -> o.getID() == id).toList().getFirst();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return gameCharacter;
    }
    public GameCharacter getByName(String name){
        GameCharacter gameCharacter = null;
        try{
            gameCharacter = data.stream().filter(o -> o.getName().equals(name)).toList().getFirst();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return gameCharacter;
    }
    public List<GameCharacter> getAll(){
        return data;
    }
    public void update(GameCharacter gameCharacter, int id) {
        try{
            data.replaceAll(o -> o.getID() == id ? gameCharacter: o);
            save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void delete(int id) {
        data.removeIf(o -> o.getID() == id);
        save();
    }
}
