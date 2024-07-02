package database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import models.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;

public class Database<T> {
    private String tableName;
    // private Class<T> type;
    private ObjectMapper mapper;
    private List<T> data;
    public Database(String tableName
    // , Class<T> typ
    ) {
        this.tableName = tableName;
        // this.type = type;
        this.mapper = new ObjectMapper();

        try {
            File file = new File("./src/main/java/database/json/" + tableName + ".json");
            if(file.length() == 0){
                this.data = new ArrayList<>();
            }else{
                System.out.println();
                this.data = mapper.readValue(file, new TypeReference<List<T>>(){});;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<T> getAll() {
        return data;
    }

    public T getOne(int id) {
        return data.stream().filter(object -> {
            try {
                return object.getClass().getField("id").getInt(object) == id;
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList()).get(0);
    }

    public void delete(int id) {
        data.removeIf(object -> {
            try {
                return object.getClass().getField("id").getInt(object) == id;
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return false;
        });
        save();
    }

    public void deletByName(String name){
        data.removeIf(object -> {
            try{
                return object.getClass().getField("name").toString().equals(name);
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        });
        save();
    }

    public int create(T object) {
        int id = -1;
        try {
            if( data.isEmpty() ){
                id = 0;
            }else {
                Class[] params = new Class[1];
                params[0] = Object.class;
                id = (int) data.getLast().getClass().getMethod("get",params).invoke(data.getLast(), "id") + 1;
            }
            Class[] params = new Class[1];
            params[0] = int.class;
            object.getClass().getMethod("setID", params).invoke(object, id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
        data.add(object);
        save();
        return id;
    }

    public void update(T object, int id) {
        data.replaceAll(data -> {
            try {
                if (data.getClass().getField("id").getInt(data) == id) {
                    return object;
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return data;
        });
        save();
    }


    private void save() {
        try {
            File file = new File("./src/main/java/database/json/" + tableName + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<T> whereEquals(String property, String value) {
        return data.stream().filter(x -> {
            try {
                return x.getClass().getField(property).toString().equals(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
    }

    public T firstWhereEquals(String property, String value) {
        List<T> result = data.stream().filter(x -> {
            try {
                return x.getClass().getField(property).toString().equals(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).toList();
        return result.isEmpty() ? null : result.getFirst();
    }

    public T firstWhereEquals(Map<String, String> conditions){
        return data.stream().filter(x ->{
            try{
                boolean result = true;
                for(Map.Entry<String, String> entry: conditions.entrySet()){
                    if(!getClass().getField(entry.getKey()).toString().equals(entry.getValue())){
                        return  false;
                    }
                }
                return  result;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }).toList().getFirst();
    }

    public T firstWhereEqualsOr(Map<String, String> conditions){
        return data.stream().filter(x -> {
            try{
                for(Map.Entry<String, String> entry: conditions.entrySet()){
                    if(getClass().getField(entry.getKey()).toString().equals(entry.getValue())){
                        return true;
                    }
                }
                return false;
            }catch (Exception e){
                e.printStackTrace();
            }
            return  false;
        }).toList().getFirst();
    }


    public void firstDeleteWhereEquals(Map<String, String> conditions){
        data.removeIf(x -> {
            try{
                for(Map.Entry<String, String> entry: conditions.entrySet()){
                    if( !getClass().getField(entry.getKey()).toString().equals(entry.getValue()) ){
                        return false;
                    }
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return  false;
        });
        save();
    }

}
