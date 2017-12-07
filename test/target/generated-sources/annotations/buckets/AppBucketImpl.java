package buckets;

import db.BucketDatabase;
import db.BucketDatabaseManager;

public final class AppBucketImpl {
  private static BucketDatabaseManager managerInstance;

  public static BucketDatabaseManager getInstance() {
    if(managerInstance == null) {
        managerInstance = BucketDatabase
                      .newInstance()
                      .declare()
                      .createCollection(model.User.class)
                      .createCollection(model.Store.class)
                      .end();
    }
    return managerInstance;
  }
}
