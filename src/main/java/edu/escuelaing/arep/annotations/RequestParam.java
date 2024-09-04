package edu.escuelaing.arep.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to bind HTTP request parameters to method parameters in a REST controller.
 * This annotation is applied at the method parameter level and is typically used in conjunction 
 * with GET or POST request handlers.
 * 
 * The 'value' element defines the name of the HTTP parameter, and the 'defaultValue' element 
 * allows specifying a default value if the parameter is not provided in the request.
 * 
 * Usage example:
 * 
 * <pre>
 * {@code
 * @GetMapping("/example")
 * public String exampleHandler(@RequestParam(value = "name", defaultValue = "World") String name) {
 *     return "Hello, " + name;
 * }
 * }
 * </pre>
 * 
 * In the example above, the 'name' parameter from the HTTP request is bound to the method's 
 * 'name' parameter. If the 'name' parameter is missing from the request, "World" will be used as the default value.
 * 
 * @Target(ElementType.PARAMETER) indicates that this annotation is applicable to method parameters.
 * @Retention(RetentionPolicy.RUNTIME) ensures that the annotation is available at runtime for reflection.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {

    /**
     * The name of the HTTP request parameter to bind to the method parameter.
     * 
     * @return the name of the HTTP request parameter.
     */
    String value();

    /**
     * The default value to use if the HTTP request parameter is not provided.
     * If no default value is provided, an empty string is used by default.
     * 
     * @return the default value if the parameter is not present in the request.
     */
    String defaultValue() default "";
}
