package db.filters;

import java.lang.reflect.Field;

public class LessThanIntegerFilter extends BaseFilter {
    private final int value;

    public LessThanIntegerFilter(String fieldValue, int value) {
        super(fieldValue);
        this.value = value;
    }

    public int getValue() {
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
        Integer value = null;
        try {
            value =  field.getInt(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (getValue() > value) {
            return true;
        }
        return false;
    }
}