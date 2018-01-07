package db.filters;


/**
 * Used for defining filters for bucket.
 */
public abstract class BaseFilter {
    private final String fieldName;

    protected BaseFilter(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }


    /**
     * Method that must be implemented by the filter.
     * Using the @fieldName in evaluate will be implemented a function which compares the value of @fieldname in @param object with the value of @fieldname in objects from @param collectionName and returns all matches.
     * @param collectionName
     * @param object
     * @param <E>
     * @return
     */
    public abstract  <E> boolean evaluate(Class<E> collectionName, E object);
}