package database;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.dlsc.formsfx.model.structure.StringField;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            File file = new File("./json/" + tableName + ".json");
            this.data = mapper.readValue(file, new TypeReference<List<T>>() {
            });
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

    public void create(T object) {
        try {
            int id = data.get(data.size() - 1).getClass().getField("id").getInt(data.get(data.size() - 1)) + 1;
            object.getClass().getField("id").setInt(object, id);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        data.add(object);
        save();
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
            File file = new File("./json/" + tableName + ".json");
            mapper.writeValue(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<T> whereEquals(String property, String value) {
        List<T> result = data.stream().filter(x -> {
            try {
                return x.getClass().getField(property).toString().equals(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());

        return result;
    }

    public T firstWhereEquals(String property, String value) {
        T result = data.stream().filter(x -> {
            try {
                return x.getClass().getField(property).toString().equals(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList()).getFirst();

        return result;
    }

    public List<T> whereNotEquals(String property, String value) {
        List<T> result = data.stream().filter(x -> {
            try {
                return !x.getClass().getField(property).toString().equals(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());

        return result;
    }

    public T firstWhereNotEquals(String property, String value) {
        T result = data.stream().filter(x -> {
            try {
                return x.getClass().getField(property).toString().equals(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList()).getFirst();

        return result;
    }

}
