package db;

import java.util.HashMap;
import java.util.List;

/**
 * Main data structure for holding the database.
 * It is using a hashmap with key of type Class and a List of objects.
 */
public class HashList {
    private final HashMap<Class, List<Object>> listHolder = new HashMap<>();

    public HashMap<Class, List<Object>> getListHolder() {
        return listHolder;
    }
}
