package main;

import annotations.BucketConfig;
import db.Bucket;
import db.BucketConfiguration;
import model.Store;
import model.User;


@BucketConfig(entities = {User.class, Store.class})
public class AppBucket extends Bucket {
    @Override
    public BucketConfiguration setConfiguration() {
        return null;
    }
}
