package db;

import java.util.LinkedList;

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
       public DatabaseConfigurationBuilder createCollection(Class collection){
            databaseConfiguration.loadClassType(collection);
            return this;
        }


        public BucketDatabaseManager end(){



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
