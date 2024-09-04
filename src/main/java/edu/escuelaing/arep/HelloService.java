package edu.escuelaing.arep;

import java.util.Random;
import edu.escuelaing.arep.annotations.GetMapping;
import edu.escuelaing.arep.annotations.RequestParam;
import edu.escuelaing.arep.annotations.RestController;

/**
 * The `HelloService` class represents a RESTful service that provides random greeting messages.
 * This class is annotated with `@RestController`, indicating that it will handle HTTP requests.
 * The service defines a method that generates a greeting message in different languages, 
 * followed by the provided name or a default value.
 */
@RestController
public class HelloService {

    /**
     * Handles HTTP GET requests to the `/app/hello` endpoint. 
     * It returns a random greeting message followed by the provided name.
     * 
     * @param name the name to include in the greeting. If the parameter is not provided, 
     *             the default value "Estimad@" will be used.
     * @return a random greeting message including the specified or default name.
     * 
     * Usage examples:
     * 
     * <pre>
     * {@code
     * GET /app/hello?name=John
     * Response: "Hello John" or another greeting
     * 
     * GET /app/hello
     * Response: "Hello Estimad@" or another greeting
     * }
     * </pre>
     */
    @GetMapping("/app/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "Estimad@") String name){
        String[] greetings = {"Hello", "Hi", "Greetings", "Salutations", "Howdy", "Hola", "Bonjour"};
        Random random = new Random();
        int randomIndex = random.nextInt(greetings.length);

        return greetings[randomIndex] + " " + name;
    }
}
