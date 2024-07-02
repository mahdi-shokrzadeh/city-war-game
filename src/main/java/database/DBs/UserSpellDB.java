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
            System.out.print(e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/userSpells.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public int create(UserSpell userSpell){
        int id = -1;
        try {
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID();
            }
            userSpell.setID(id);
            data.add(userSpell);
            save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    public UserSpell getOne(int id){
        UserSpell userSpell = null;
        try{
            userSpell = data.stream().filter(o -> o.getID() == id).toList().getFirst();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return userSpell;
    }
    public List<UserSpell> whereEquals(int id){
        try{
           data.removeIf(o -> o.getUserID() != id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return data;
    }
    public List<UserSpell> getAll(){
        return data;
    }
    public void update(UserSpell userSpell, int id) {
        try{
            data.replaceAll(o -> o.getID() == id ? userSpell: o);
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
