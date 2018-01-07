package main;

import annotations.BucketConfig;
import db.Bucket;

import model.Store;
import model.User;


@BucketConfig(entities = {User.class, Store.class})
public class AppBucket extends Bucket {

}
