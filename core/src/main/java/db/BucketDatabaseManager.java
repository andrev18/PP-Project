package db;

import db.filters.*;

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


    /**
     * Generic query builder. Use this object to instantiate specialized FilterBuilders.
     * @param <E>
     */
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


        public FilterBuilder<E> greaterThan(String fieldName, int value) {
            return new FilterBuilder<E>(new GreaterThanIntegerFilter(fieldName, value), collectionName);
        }

        public FilterBuilder<E> lessThan(String fieldName, int value) {
            return new FilterBuilder<E>(new LessThanIntegerFilter(fieldName, value), collectionName);
        }


        public PredicateFilterBuilder<E> filter(Predicate<E> predicate) {
            return new PredicateFilterBuilder<E>(predicate, collectionName);
        }


        /**
         * Inserts an object in bucket
         * @param object
         * @return
         */
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

        /**
         * Deletes an object from bucket.
         * @param object
         * @return
         */
        public boolean delete(E object) {
            if (object.getClass() != collectionName) {
                throw new RuntimeException("Invalid object type " + object.getClass().getName() + " for collection " + collectionName.getName());
            }
            List list = bucketDatabase.getDatabaseSource()
                    .getListHolder().get(collectionName);


            if (list == null) {
                throw new RuntimeException("Collection " + collectionName.getName() + " does not exist!");
            }
            if (list.contains(object)) {
                return list.remove(object);
            }
            return false;
        }
    }

    public class PredicateFilterBuilder<E> {
        private final Predicate<E> predicate;
        private final Class collectionName;

        /**
         * Creates a PredicateFilter.
         * @param predicate - used for evaluation of comparison
         * @param collectionName
         */
        public PredicateFilterBuilder(Predicate<E> predicate, Class collectionName) {
            this.predicate = predicate;
            this.collectionName = collectionName;
        }

        /**
         * Starts the query
         * @param <E>
         * @return - result of query
         */
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


        /**
         * Instantiate a FilterBuilder.
         * @param filter
         * @param collectionName
         */
        public FilterBuilder(BaseFilter filter, Class<E> collectionName) {
            this.collectionName = collectionName;
            filters.add(filter);

        }

        /**
         * Returns objects that have where the value of field declared as fieldName is equal with value;
         * @param fieldName - fieldName
         * @param value - value
         * @return
         */
        public FilterBuilder<E> equalsTo(String fieldName, int value) {
            filters.add(new IntegerFilter(fieldName, value));
            return this;
        }

        /**
         * Returns objects that have where the value of field declared as fieldName is equal with value;
         * @param fieldName - fieldName
         * @param value - value
         * @return
         */
        public FilterBuilder<E> equalsTo(String fieldName, String value) {
            filters.add(new StringFilter(fieldName, value));
            return this;
        }

        /**
         * Returns objects that have where the value of field declared as fieldName is greater than value;
         * @param fieldName - fieldName
         * @param value - value
         * @return
         */
        public FilterBuilder<E> greaterThan(String fieldName, int value) {
            filters.add(new GreaterThanIntegerFilter(fieldName, value));
            return this;
        }

        /**
         * Returns objects that have where the value of field declared as fieldName is less than value;
         * @param fieldName - fieldName
         * @param value - value
         * @return
         */
        public FilterBuilder<E> lessThan(String fieldName, int value) {
            filters.add(new LessThanIntegerFilter(fieldName, value));
            return this;
        }

        /**
         * Starts the query
         * @param <E>
         * @return - result of query
         */
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
