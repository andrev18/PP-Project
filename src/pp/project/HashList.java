package pp.project;

import java.util.HashMap;
import java.util.List;

public class HashList {
    private final HashMap<Class, List<Object>> listHolder = new HashMap<>();

    public HashMap<Class, List<Object>> getListHolder() {
        return listHolder;
    }
}
