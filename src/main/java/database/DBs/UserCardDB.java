package database.DBs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;
import models.UserCard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserCardDB {
    private final ObjectMapper mapper;
    private List<UserCard> data;
    public UserCardDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/userCards.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<UserCard>>(){});
            }
        }catch (Exception e){
            System.out.print("Exception in UserCardDB: " +e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/userCards.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println("Exception in UserCardDB: " +e.getMessage());
        }
    }
    public int create(UserCard userCard){
        int id = -1;
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID();
            }
            userCard.setID(id);
            data.add(userCard);
            save();
        return id;
    }
    public UserCard getOne(int id){
        UserCard userCard = null;
        userCard = data.stream().filter(o -> o.getID() == id).findFirst().orElse(null);
        return userCard;
    }
    public UserCard firstWhereEquals(int id){
        UserCard userCard = null;
        userCard = data.stream().filter(o -> o.getUserID() == id).findFirst().orElse(null);
        return userCard;
    }
    public UserCard firstWhereEquals(int userID, int cardID){
        UserCard userCard = null;
        userCard = data.stream().filter(o -> o.getUserID() == userID && o.getCardID() == cardID).findFirst().orElse(null);
        return userCard;
    }
    public List<UserCard> whereEquals(int id){
        List<UserCard> cards = null;
        cards = data.stream().filter(o -> o.getUserID() != id).toList();
        return cards;
    }
    public List<UserCard> getAll(){
        return data;
    }
    public void update(UserCard userCard, int id) {
        data.replaceAll(o -> o.getID() == id ? userCard: o);
        save();
    }
    public void delete(int id){
        data.removeIf(o -> o.getID() == id);
        save();
    }
}
