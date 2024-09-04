package edu.escuelaing.arep.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate that a class serves as a REST controller in a web
 * application.
 * Classes annotated with `@RestController` are treated as components that
 * handle HTTP requests
 * and return responses, often in formats like JSON or plain text.
 * 
 * This annotation is typically used in conjunction with method-level
 * annotations like
 * `@GetMapping` to define the endpoints of the REST API.
 * 
 * Usage example:
 * 
 * <pre>
 * {@code @RestController
 * public class MyController { @GetMapping("/greet")
 *     public String greet() {
 *         return "Hello, World!";
 *     }
 * }
 * }
 * </pre>
 * 
 * In the example above, the class `MyController` is marked as a REST
 * controller,
 * and it defines a method that handles GET requests to the `/greet` endpoint.
 * 
 * @Target(ElementType.TYPE) indicates that this annotation is applicable to
 *                           classes.
 * @Retention(RetentionPolicy.RUNTIME) ensures that the annotation is available
 *                                     at runtime for reflection.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {
}
