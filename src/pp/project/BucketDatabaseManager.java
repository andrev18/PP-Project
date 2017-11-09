package pp.project;

import pp.project.filters.BaseFilter;
import pp.project.filters.IntegerFilter;
import pp.project.filters.StringFilter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

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

        public PredicateFilterBuilder<E> filter(Predicate<E> predicate) {
            return new PredicateFilterBuilder<>(predicate,collectionName);
        }


        public boolean insert(E object) {
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

    public class PredicateFilterBuilder<E> {
        private final Predicate<E> predicate;
        private final Class collectionName;

        public PredicateFilterBuilder(Predicate<E> predicate, Class collectionName) {
            this.predicate = predicate;
            this.collectionName = collectionName;
        }

        public LinkedList<E> find() {
            List<E> list = (List<E>) bucketDatabase.getDatabaseSource()
                    .getListHolder().get(collectionName);

            LinkedList<E> result = new LinkedList();

            if (list == null) {
                throw new RuntimeException("Collection " + collectionName.getName() + " does not exist!");
            }


            for (E object : list) {
                if (predicate.test(object)) {
                    result.add(object);
                }
            }

            return result;

        }
    }

    public class FilterBuilder<E> {
        private final ArrayList<BaseFilter> filters = new ArrayList<>();
        private final Class collectionName;

        public FilterBuilder(BaseFilter filter, Class<E> collectionName) {
            this.collectionName = collectionName;
            filters.add(filter);

        }

        public FilterBuilder<E> equalsTo(String fieldName, int value) {
            filters.add(new IntegerFilter(fieldName, value));
            return this;
        }

        public FilterBuilder<E> equalsTo(String fieldName, String value) {
            filters.add(new StringFilter(fieldName, value));
            return this;
        }

        public <E> LinkedList<E> find() {
            List<E> list = (List<E>) bucketDatabase.getDatabaseSource()
                    .getListHolder().get(collectionName);

            LinkedList<E> result = new LinkedList();


            if (list == null) {
                throw new RuntimeException("Collection " + collectionName.getName() + " does not exist!");
            }


            objectsLoop:
            for (E object : list) {
                for (BaseFilter filter : filters) {
                    if (!filter.evaluate(collectionName, object)) {
                        continue objectsLoop;
                    }
                }
                result.add(object);
            }


            return result;
        }
    }

}
