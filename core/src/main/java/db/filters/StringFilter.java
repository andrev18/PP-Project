package db.filters;


import java.lang.reflect.Field;

public class StringFilter extends BaseFilter {
    private final String value;

    public StringFilter(String fieldValue, String value) {
        super(fieldValue);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <E> boolean evaluate(Class<E> collectionName, E object) {
        Field field = null;
        try {
            field = collectionName.getDeclaredField(getFieldName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (field == null) {
            throw new RuntimeException("Field " + getFieldName() + " not found!");
        }
        field.setAccessible(true);
        String value = null;
        try {
            value = (String) field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (getValue().equals(value)) {
            return true;
        }
        return false;
    }
}