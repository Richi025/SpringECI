package edu.escuelaing.arep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.print.DocFlavor.STRING;

public class SpringECI {
    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Class c = Class.forName(args[0]);
        Map<String, Method> services = new HashMap<>();

        // Cargar Componentes
        if (c.isAnnotationPresent(RestController.class)) {
            Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    String key = m.getAnnotation(GetMapping.class).value();
                    services.put(key, m);

                }
            }
        }

        // Codigo quemado para /hello
        URL serviceUrl = new URL("http://localhost:8080/App/hello");
        String path = serviceUrl.getPath();
        System.out.println("Path: " + path);
        String serviceName = path.substring(4);
        System.out.println("Service name: " + serviceName);

        Method methodService = services.get(serviceName);
        System.out.println("Respuesta servicio: " + methodService.invoke(null));

        // Codigo quemado metodo /timeCurrent
        URL serviceUrl2 = new URL("http://localhost:8080/App/timeCurrent");
        String path2 = serviceUrl2.getPath();
        System.out.println("Path: " + path2);
        String serviceName2 = path2.substring(4);
        System.out.println("Service name: " + serviceName2);

        Method methodService2 = services.get(serviceName2);
        System.out.println("Respuesta servicio: " + methodService2.invoke(null));

        // Codigo quemado metodo /ramdom
        URL serviceUrl3 = new URL("http://localhost:8080/App/randomGreeting");
        String path3 = serviceUrl3.getPath();
        System.out.println("Path: " + path3);
        String serviceName3 = path3.substring(4);
        System.out.println("Service name: " + serviceName3);

        Method methodService3 = services.get(serviceName3);
        System.out.println("Respuesta servicio: " + methodService3.invoke(null));

        // Codigo quemado metodo /currentDayOfWeek
        URL serviceUrl4 = new URL("http://localhost:8080/App/currentDayOfWeek");
        String path4 = serviceUrl4.getPath();
        System.out.println("Path: " + path3);
        String serviceName4 = path4.substring(4);
        System.out.println("Service name: " + serviceName3);

        Method methodService4 = services.get(serviceName4);
        System.out.println("Respuesta servicio: " + methodService4.invoke(null));

    }
}
