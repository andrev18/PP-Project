package pp.project;

import java.util.HashSet;

public class DatabaseConfiguration {
    private final HashSet<Class> databaseClasses = new HashSet<>();



    public DatabaseConfiguration loadClassType(Class classObject){
        boolean isClassDeclared = databaseClasses.contains(classObject);
        if(isClassDeclared){
            throw new RuntimeException("Collection "+classObject.getName()+" declared multiple times!");
        }
        databaseClasses.add(classObject);
        return this;
    }

    public HashSet<Class> getDatabaseClasses() {
        return databaseClasses;
    }
}
