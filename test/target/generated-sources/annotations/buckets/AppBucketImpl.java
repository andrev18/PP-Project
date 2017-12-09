package buckets;

import db.BucketDatabase;
import db.BucketDatabaseManager;
import model.StoreBao;
import model.UserBao;

public final class AppBucketImpl {
  private static AppBucketImpl instance;

  private final BucketDatabaseManager bucketManager;

  public StoreBao storeBao;

  public UserBao userBao;

  private AppBucketImpl() {
      bucketManager = BucketDatabase
                    .newInstance()
                    .declare()
                    .createCollection(model.User.class)
                    .createCollection(model.Store.class)
                    .end();
    storeBao = new model.StoreBao(bucketManager,model.Store.class);
    userBao = new model.UserBao(bucketManager,model.User.class);
  }

  public static AppBucketImpl getInstance() {
    if(instance == null) {
        instance = new AppBucketImpl();
    }
    return instance;
  }
}
