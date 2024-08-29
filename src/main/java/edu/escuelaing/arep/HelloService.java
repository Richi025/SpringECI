package edu.escuelaing.arep;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Random;

@RestController
public class HelloService {

    @GetMapping("/hello")
    public static String hello(){
        return "Hello Word";
    }

    @GetMapping("/timeCurrent")
    public static String time() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        
        return formattedDateTime;
    }

    @GetMapping("/randomGreeting")
    public static String randomGreeting() {
        String[] greetings = {"Hello", "Hi", "Greetings", "Salutations", "Howdy", "Hola", "Bonjour"};
        Random random = new Random();
        int randomIndex = random.nextInt(greetings.length);
        return greetings[randomIndex];
    }

    @GetMapping("/currentDayOfWeek")
    public static String currentDayOfWeek() {
        LocalDateTime now = LocalDateTime.now();
        return now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

}
