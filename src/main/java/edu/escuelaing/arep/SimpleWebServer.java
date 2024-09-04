package edu.escuelaing.arep;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import edu.escuelaing.arep.annotations.GetMapping;
import edu.escuelaing.arep.annotations.RequestParam;
import edu.escuelaing.arep.annotations.RestController;

/**
 * The `SimpleWebServer` class implements a basic multithreaded HTTP server
 * capable of serving static files and handling dynamic GET requests using
 * custom controller classes annotated with `@RestController`.
 */
public class SimpleWebServer {
    private static final int PORT = 8081;
    public static final String WEB_ROOT = "src/main/java/edu/escuelaing/arep/resourse/webroot";
    public static final Map<String, Method> getMappings = new HashMap<>();
    public static final Map<String, Object> controllers = new HashMap<>();
    private static boolean isRun = true;

    /**
     * Main method that starts the web server, initializes the controllers, and
     * listens for incoming connections.
     */
    public static void main(String[] args) throws IOException, ReflectiveOperationException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(PORT);
        initializeControllers();  // Load all controllers
        while (isRun) {
            Socket clientSocket = serverSocket.accept();  // Accept new client connection
            threadPool.submit(new ClientHandler(clientSocket));  // Handle the request in a new thread
        }
        serverSocket.close();
        threadPool.shutdown();
    }

    /**
     * Registers controllers (annotated with @RestController) and their methods
     * (annotated with @GetMapping) to handle specific GET requests.
     */
    public static void initializeControllers() throws ReflectiveOperationException {
        registerController(HelloService.class);  // Register HelloService
        registerController(SqrtService.class);   // Register SqrtService
    }

    /**
     * Registers a controller class and maps its methods annotated with @GetMapping.
     *
     * @param controllerClass the class of the controller to register.
     */
    private static void registerController(Class<?> controllerClass) throws ReflectiveOperationException {
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
        controllers.put(controllerClass.getName(), controllerInstance);

        // Map methods annotated with @GetMapping
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                getMappings.put(getMapping.value(), method);
            }
        }
    }

    /**
     * Stops the server by setting the running flag to false.
     */
    public static void stop() {
        isRun = false;
    }
}

/**
 * The `ClientHandler` class implements Runnable and handles individual client connections.
 * It processes HTTP requests and responds accordingly, either by serving static files or 
 * invoking controller methods for dynamic content.
 */
class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            if (requestLine == null) return;

            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String fileRequested = tokens[1];

            printRequestHeader(requestLine, in);

            if (fileRequested.startsWith("/app")) {
                handleAppRequest(method, fileRequested, out);  // Handle dynamic requests
            } else if (method.equals("GET")) {
                handleGetRequest(fileRequested, out, dataOut);  // Handle static file requests
            } else {
                out.println("HTTP/1.1 405 Method Not Allowed");
                out.println();
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints the request headers to the console.
     *
     * @param requestLine the initial request line of the HTTP request.
     * @param in the BufferedReader to read additional headers.
     */
    private void printRequestHeader(String requestLine, BufferedReader in) throws IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (!in.ready()) {
                break;
            }
        }
    }

    /**
     * Handles GET requests for static files located in the web root directory.
     *
     * @param fileRequested the requested file path.
     * @param out the PrintWriter to write HTTP headers.
     * @param dataOut the BufferedOutputStream to send the file data.
     */
    public void handleGetRequest(String fileRequested, PrintWriter out, BufferedOutputStream dataOut)
            throws IOException {
        File file = new File(SimpleWebServer.WEB_ROOT, fileRequested);
        int fileLength = (int) file.length();
        String content = getContentType(fileRequested);

        if (file.exists()) {
            byte[] fileData = readFileData(file, fileLength);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: " + content);
            out.println("Content-length: " + fileLength);
            out.println();
            out.flush();
            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();
        } else {
            out.println("HTTP/1.1 404 Not Found");
            out.println();
            out.flush();
        }
    }

    /**
     * Handles dynamic GET requests mapped to controller methods.
     *
     * @param method the HTTP method (GET).
     * @param path the requested URI path.
     * @param out the PrintWriter to send the HTTP response.
     */
    protected void handleAppRequest(String method, String path, PrintWriter out) {
        if ("GET".equalsIgnoreCase(method)) {
            String[] pathParts = path.split("\\?");
            String basePath = pathParts[0];
            Map<String, String> queryParams = new HashMap<>();

            if (pathParts.length > 1) {
                String queryString = pathParts[1];
                String[] pairs = queryString.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length > 1) {
                        queryParams.put(keyValue[0], keyValue[1]);
                    } else {
                        queryParams.put(keyValue[0], "");
                    }
                }
            }

            Method handlerMethod = SimpleWebServer.getMappings.get(basePath);
            if (handlerMethod != null) {
                try {
                    Object controller = SimpleWebServer.controllers.get(handlerMethod.getDeclaringClass().getName());
                    Object response = invokeControllerMethod(handlerMethod, controller, queryParams);
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-type: text/plain");
                    out.println();
                    out.println(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("HTTP/1.1 500 Internal Server Error");
                }
            } else {
                out.println("HTTP/1.1 404 Not Found");
            }
        } else {
            out.println("HTTP/1.1 405 Method Not Allowed");
        }
        out.flush();
    }

    /**
     * Invokes the controller method that handles the current request.
     *
     * @param handlerMethod the method to invoke.
     * @param controller the controller instance.
     * @param queryParams the query parameters of the request.
     * @return the response object returned by the controller method.
     */
    private Object invokeControllerMethod(Method handlerMethod, Object controller, Map<String, String> queryParams)
            throws ReflectiveOperationException {
        Parameter[] parameters = handlerMethod.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                String paramName = requestParam.value();
                String paramValue = queryParams.getOrDefault(paramName, requestParam.defaultValue());
                args[i] = convertToRequiredType(parameters[i].getType(), paramValue);
            }
        }

        return handlerMethod.invoke(controller, args);
    }

    /**
     * Converts a query parameter value to the required type for method invocation.
     *
     * @param paramType the type of the method parameter.
     * @param paramValue the query parameter value as a string.
     * @return the converted value of the required type.
     */
    private Object convertToRequiredType(Class<?> paramType, String paramValue) {
        if (paramType == int.class) {
            return Integer.parseInt(paramValue);
        } else if (paramType == double.class) {
            return Double.parseDouble(paramValue);
        } else if (paramType == boolean.class) {
            return Boolean.parseBoolean(paramValue);
        } else {
            return paramValue;
        }
    }

    /**
     * Determines the content type of the requested file based on its extension.
     *
     * @param fileRequested the requested file path.
     * @return the MIME type of the file.
     */
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".html"))
            return "text/html";
        else if (fileRequested.endsWith(".css"))
            return "text/css";
        else if (fileRequested.endsWith(".js"))
            return "application/javascript";
        return "text/plain";
    }

    /**
     * Reads the file data from disk into a byte array.
     *
     * @param file the file to read.
     * @param fileLength the length of the file in bytes.
     * @return a byte array containing the file data.
     */
    private byte[] readFileData(File file, int fileLength) throws IOException {
        byte[] fileData = new byte[fileLength];
        try (FileInputStream fileIn = new FileInputStream(file)) {
            fileIn.read(fileData);
        }
        return fileData;
    }
}
