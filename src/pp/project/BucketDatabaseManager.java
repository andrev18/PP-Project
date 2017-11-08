package pp.project;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BucketDatabaseManager {
    private final BucketDatabase bucketDatabase;

    public BucketDatabaseManager(BucketDatabase bucketDatabase) {
        this.bucketDatabase = bucketDatabase;
    }


    public <E> QueryBuilder<E> where(Class<E> collectionName) {
        return new QueryBuilder<E>(collectionName);
    }


    public class QueryBuilder<E> {
        private final Class<E> collectionName;

        public QueryBuilder(Class<E> collectionName) {
            this.collectionName = collectionName;

        }

        public FilterBuilder<E> equalsTo(String fieldName, int value) {
            return new FilterBuilder<E>(new IntegerFilter(fieldName, value), collectionName);
        }

        public FilterBuilder<E> equalsTo(String fieldName, String value) {
            return new FilterBuilder<E>(new StringFilter(fieldName, value), collectionName);
        }


        public boolean  insert(E object) {
            if (object.getClass() != collectionName) {
                throw new RuntimeException("Invalid object type " + object.getClass().getName() + " for collection " + collectionName.getName());
            }
            List list = bucketDatabase.getDatabaseSource()
                    .getListHolder().get(collectionName);


            if (list == null) {
                throw new RuntimeException("Collection " + collectionName.getName() + " does not exist!");
            }
            return list.add(object);

        }
    }

    public class FilterBuilder<E> {
        private final ArrayList<BaseFilter> filters = new ArrayList<>();
        private final Class collectionName;

        public FilterBuilder(BaseFilter filter, Class<E> collectionName) {
            this.collectionName = collectionName;
            filters.add(filter);

        }

        public FilterBuilder equalsTo(String fieldName, int value) {
            filters.add(new IntegerFilter(fieldName, value));
            return this;
        }

        public FilterBuilder equalsTo(String fieldName, String value) {
            filters.add(new StringFilter(fieldName, value));
            return this;
        }

        public LinkedList<E> find() {
            List<E> list = (List<E>) bucketDatabase.getDatabaseSource()
                    .getListHolder().get(collectionName);

            LinkedList<E> result = new LinkedList();


            if (list == null) {
                throw new RuntimeException("Collection " + collectionName.getName() + " does not exist!");
            }

            for (BaseFilter filter :
                    filters) {

                if (filter instanceof StringFilter) {
                    Field field = null;
                    try {
                        field = collectionName.getDeclaredField(filter.getFieldName());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    if (field == null) {
                        throw new RuntimeException("Field " + filter.getFieldName() + " not found!");
                    }
                    field.setAccessible(true);
                    for (E obj :
                            list) {
                        String value = null;
                        try {
                            value = (String) field.get(obj);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        if (((StringFilter) filter).getValue().equals(value)) {
                            result.add(obj);
                        }
                    }
                }

            }


            return result;
        }
    }

    private static abstract class BaseFilter {
        private final String fieldName;

        protected BaseFilter(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }

    private static class IntegerFilter extends BaseFilter {
        private final int value;

        protected IntegerFilter(String fieldValue, int value) {
            super(fieldValue);
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static class StringFilter extends BaseFilter {
        private final String value;

        protected StringFilter(String fieldValue, String value) {
            super(fieldValue);
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
