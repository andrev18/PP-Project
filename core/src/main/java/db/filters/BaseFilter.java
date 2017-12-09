package db.filters;

public abstract class BaseFilter {
    private final String fieldName;

    protected BaseFilter(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }


    public abstract  <E> boolean evaluate(Class<E> collectionName, E object);
}