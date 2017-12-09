package db.notifier;



/*
Delegate for notifying subscribers when a collection in database is Changed
 */
public interface OnCollectionStateChanged<T extends Class> {
    void onCollectionStateChanged();
}
