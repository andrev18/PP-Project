package db;


/**
 * This object is used for accessing database objects of type TYPE
 * @param <TYPE> - Defines the TYPE of object that this Bao can access.
 */
public abstract class BaseBao<TYPE> {
    public final BucketDatabaseManager bucketDatabaseManager;
    private final Class<TYPE> collectionName;

    public BaseBao(BucketDatabaseManager bucketDatabaseManager, Class<TYPE> collectionName) {
        this.bucketDatabaseManager = bucketDatabaseManager;
        this.collectionName = collectionName;
    }


    /**
     * Insert objects in Bucket
     * @param objects - array of objects
     */
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

    /**
     * Delete objects from Bucket
     * @param objects - array of objects
     */
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

    /**
     * Insert a single object in Bucket
     * @param object - single object
     * @return - returns true if the object was inserted, false otherwise
     */
    public boolean insert(TYPE object) {
        return bucketDatabaseManager
                .where(collectionName)
                .insert(object);

    }

    /**
     * Delete a single object from Bucket
     * @param object - single object
     * @return - returns true if the object was deleted, false otherwise
     */
    public boolean delete(TYPE object) {
        return bucketDatabaseManager
                .where(collectionName)
                .delete(object);

    }

    /**
     * Defines configuration of the query.
     * @return
     */
    public BucketDatabaseManager.QueryBuilder<TYPE> query() {
        return bucketDatabaseManager.where(collectionName);
    }
}
