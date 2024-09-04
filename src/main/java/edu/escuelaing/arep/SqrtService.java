package edu.escuelaing.arep;

import edu.escuelaing.arep.annotations.GetMapping;
import edu.escuelaing.arep.annotations.RequestParam;
import edu.escuelaing.arep.annotations.RestController;

/**
 * The `SqrtService` class provides a RESTful service that calculates the square root of a given number.
 * This service is exposed through the `/app/sqrt` endpoint and handles HTTP GET requests.
 */
@RestController
public class SqrtService {

    /**
     * Handles GET requests to the `/app/sqrt` endpoint.
     * The service expects a query parameter `number` and returns the square root of the number.
     * If the parameter is not provided, it defaults to 25. If the number is negative or not a valid number,
     * appropriate error messages are returned.
     *
     * @param number the number for which the square root will be calculated. It is expected to be a string
     *               that can be converted to a double. If not provided, the default value is "25".
     * @return a string message containing either the square root of the number or an error message
     *         if the input is invalid or negative.
     *
     * Usage examples:
     * <pre>
     * {@code
     * GET /app/sqrt?number=16
     * Response: "La raíz cuadrada de 16.0 es 4.0"
     *
     * GET /app/sqrt?number=-5
     * Response: "Error: El número no puede ser negativo."
     *
     * GET /app/sqrt?number=abc
     * Response: "Error: Por favor ingrese un número válido."
     * }
     * </pre>
     */
    @GetMapping("/app/sqrt")
    public String calculateSquareRoot(@RequestParam(value = "number", defaultValue = "25") String number) {
        try {
            double num = Double.parseDouble(number);
            if (num < 0) {
                return "Error: El número no puede ser negativo.";
            }
            return "La raíz cuadrada de " + num + " es " + Math.sqrt(num);
        } catch (NumberFormatException e) {
            return "Error: Por favor ingrese un número válido.";
        }
    }
}
