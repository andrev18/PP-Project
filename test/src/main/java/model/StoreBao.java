package model;

import annotations.BAO;
import db.BaseBao;
import db.BucketDatabaseManager;


@BAO
public class StoreBao extends BaseBao<Store>{
    public StoreBao(BucketDatabaseManager bucketDatabaseManager, Class<Store> collectionName) {
        super(bucketDatabaseManager, collectionName);
    }
}
