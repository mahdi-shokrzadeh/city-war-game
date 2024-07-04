package database.DBs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.UserSpell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserSpellDB {
    private final ObjectMapper mapper;
    private List<UserSpell> data;
    public UserSpellDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/userSpells.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<UserSpell>>(){});
            }
        }catch (Exception e){
            System.out.println("Exception in UserSpellDB: " +e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/userSpells.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println("Exception in UserSpellDB: " +e.getMessage());
        }
    }
    public int create(UserSpell userSpell){
        int id = -1;
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID() + 1;
            }
            userSpell.setID(id);
            data.add(userSpell);
            save();
        return id;
    }
    public UserSpell getOne(int id){
        UserSpell userSpell = null;
        userSpell = data.stream().filter(o -> o.getID() == id).toList().getFirst();
        return userSpell;
    }
    public List<UserSpell> whereEquals(int id){
        List<UserSpell> spells = null;
        spells = data.stream().filter(o -> o.getUserID() != id).toList();
        return spells;
    }
    public List<UserSpell> getAll(){
        return data;
    }
    public void update(UserSpell userSpell, int id) {
        data.replaceAll(o -> o.getID() == id ? userSpell: o);
        save();
    }
    public void delete(int id){
        data.removeIf(o -> o.getID() == id);
        save();
    }
}
