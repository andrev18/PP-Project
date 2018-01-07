package annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Define the configuration of the bucket.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface BucketConfig {


    /**
     * entities -  class types contained in Bucket
     */
    Class[] entities();
}
