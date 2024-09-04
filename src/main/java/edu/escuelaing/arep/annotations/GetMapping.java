package edu.escuelaing.arep.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to map HTTP GET requests to specific handler methods in REST controllers.
 * This annotation is used at the method level to specify the URI path that the method will handle.
 * The path is defined using the 'value' element, which represents the endpoint of the GET request.
 * 
 * Usage example:
 * 
 * <pre>
 * {@code
 * @GetMapping("/example")
 * public String exampleHandler() {
 *     return "Example response";
 * }
 * }
 * </pre>
 * 
 * The above method will handle GET requests to "/example".
 * 
 * @Target(ElementType.METHOD) indicates that this annotation is applicable to methods.
 * @Retention(RetentionPolicy.RUNTIME) ensures that the annotation is available at runtime for reflection.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {
    
    /**
     * The URI path that this method will handle for GET requests.
     * 
     * @return the path for the GET request.
     */
    public String value();
}




 