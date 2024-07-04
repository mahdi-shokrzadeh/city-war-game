package database.DBs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.card.Spell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpellDB {
    private final ObjectMapper mapper;
    private List<Spell> data;
    public SpellDB(){
        mapper = new ObjectMapper();
        try{
            File file = new File("./src/main/java/database/json/spells.json");
            if(file.length() == 0){
                data = new ArrayList<>();
            }else{
                data = mapper.readValue(file, new TypeReference<List<Spell>>(){});
            }
        }catch (Exception e){
            System.out.println("Exception in SpellDB: " +e.getMessage());
        }
    }
    private void save(){
        File file = new File("./src/main/java/database/json/spells.json");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public int create(Spell spell){
        int id = -1;
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID() + 1;
            }
            spell.setID(id);
            data.add(spell);
            save();
        return id;
    }
    public Spell getOne(int id){
        Spell spell = null;
        spell = data.stream().filter(o -> o.getID() == id).findFirst().orElse(null);
        return spell;
    }
    public Spell getByName(String name){
        Spell spell = null;
        spell = data.stream().filter(o -> o.getName().equals(name) ).findFirst().orElse(null);
        return spell;
    }
    public List<Spell> getAll(){
        return data;
    }
    public void update(Spell spell, int id) {
        data.replaceAll(o -> o.getID() == id ? spell: o);
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
