package database.DBs;

import models.card.Card;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardDB {
    private final ObjectMapper mapper;
    private List<Card> data;
    public CardDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/cards.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<Card>>(){});
            }
        }catch (Exception e){
            System.out.print("Exception in CardDB: " +e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/cards.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println("Exception in CardDB: " +e.getMessage());
        }
    }
    public int create(Card user){
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
    public Card getOne(int id){
        Card card = null;
        card = data.stream().filter(o -> o.getID() == id).findFirst().orElse(null);
        return card;
    }
    public Card getByName(String name){
        Card card = null;
        card = data.stream().filter(o -> o.getName().equals(name)).findFirst().orElse(null);
        return  card;
    }
    public List<Card> getAll(){
        return data;
    }
    public void update(Card card, int id) {
        data.replaceAll(o -> o.getID() == id ? card: o);
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
