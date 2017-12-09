package db;

import db.BucketDatabaseManager;

public abstract class BaseBao<TYPE> {
    public final BucketDatabaseManager bucketDatabaseManager;
    private final Class<TYPE> collectionName;

    public BaseBao(BucketDatabaseManager bucketDatabaseManager, Class<TYPE> collectionName) {
        this.bucketDatabaseManager = bucketDatabaseManager;
        this.collectionName = collectionName;
    }


    public void insert(TYPE... objects) {
        if (objects != null) {
            for (TYPE object :
                    objects) {
                bucketDatabaseManager
                        .where(collectionName)
                        .insert(object);
            }
        }
    }

    public void delete(TYPE... objects) {
        if (objects != null) {
            for (TYPE object :
                    objects) {
                bucketDatabaseManager
                        .where(collectionName)
                        .delete(object);
            }
        }
    }

    public boolean insert(TYPE object) {
        return bucketDatabaseManager
                .where(collectionName)
                .insert(object);

    }

    public boolean delete(TYPE object) {
        return bucketDatabaseManager
                .where(collectionName)
                .delete(object);

    }

    public BucketDatabaseManager.QueryBuilder<TYPE> query() {
        return bucketDatabaseManager.where(collectionName);
    }
}
