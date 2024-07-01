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
            System.out.print(e.getMessage());
        }
    }
    private void save(){
        try {
            File file = new File("./src/main/java/database/json/spells.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public int create(Spell spell){
        int id = -1;
        try {
            if( data.isEmpty() ){
                id = 0;
            }else {
                id = data.getLast().getID();
            }
            spell.setID(id);
            data.add(spell);
            save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    public Spell getOne(int id){
        Spell spell = null;
        try{
            spell = data.stream().filter(o -> o.getID() == id).toList().getFirst();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return spell;
    }
    public Spell getByName(String name){
        Spell spell = null;
        try{
            spell = data.stream().filter(o -> o.getName().equals(name) ).toList().getFirst();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return spell;
    }
    public List<Spell> getAll(){
        return data;
    }
    public void update(Spell spell, int id) {
        try{
            data.replaceAll(o -> o.getID() == id ? spell: o);
            save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
