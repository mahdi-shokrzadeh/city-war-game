package database.DBs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import models.User;

import com.fasterxml.jackson.databind.ObjectMapper;


public class UserDB {
    private final ObjectMapper mapper;
    private List<User> data;
    public UserDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/users.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<User>>(){});
            }
        }catch (Exception e){
            System.out.print("Exception in UserDB: " + e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/users.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println("Exception in UserDB: " + e.getMessage());
        }
    }
    public int create(User user){
        int id = -1;
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID();
            }
            user.setID(id);
            data.add(user);
            save();
        return id;
    }
    public User getOne(int id){
        User user = null;
        user = data.stream().filter(o -> o.getID() == id).findFirst().orElse(null);
        return user;
    }
    public User getByUserName(String name){
        User user = null;
        user = data.stream().filter(o -> o.getUsername().equals(name) ).findFirst().orElse(null);
        return user;
    }
    public List<User> getAll(){
        return data;
    }
    public void update(User user, int id) {
        data.replaceAll(o -> o.getID() == id ? user: o);
        save();
    }
    public void delete(int id){
        data.removeIf(o -> o.getID() == id);
        save();
    }
    public void deleteByUserName(String name){
        data.removeIf(o -> o.getUsername().equals(name) );
        save();
    }
}
