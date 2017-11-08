package pp.project;


import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.function.Consumer;

public class BucketDatabase {
    private final DatabaseConfiguration databaseConfiguration;
    private final HashList databaseSource = new HashList();


    private BucketDatabase(){
        databaseConfiguration = new DatabaseConfiguration();
    }


    public static BucketDatabase newInstance(){
        return new BucketDatabase();
    }


    public DatabaseConfigurationBuilder declare(){
        return new DatabaseConfigurationBuilder();
    }


    public class DatabaseConfigurationBuilder{
        DatabaseConfigurationBuilder createCollection(Class collection){
            databaseConfiguration.loadClassType(collection);
            return this;
        }


        BucketDatabaseManager end(){



            databaseConfiguration.getDatabaseClasses()
                    .forEach(aClass -> {
                        databaseSource.getListHolder().put(aClass,new LinkedList<>());
                    });

            return new BucketDatabaseManager(BucketDatabase.this);
        }

    }

    public HashList getDatabaseSource() {
        return databaseSource;
    }
}
