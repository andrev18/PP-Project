package model;


import annotations.BAO;
import db.BaseBao;
import db.BucketDatabaseManager;

@BAO
public class UserBao extends BaseBao<User> {
    public UserBao(BucketDatabaseManager bucketDatabaseManager, Class<User> collectionName) {
        super(bucketDatabaseManager, collectionName);
    }
}
