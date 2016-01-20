package com.blegh.simplewebapp;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javafx.geometry.Pos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Samuel on 1/18/2016.
 */
public class SimpleAppHandler implements HttpHandler {

    static String name = "no name";
    static String age = "no age";

    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            HttpGetHandler(exchange);
        }

        if (requestMethod.equalsIgnoreCase("POST")) {
            HttpPostHandler(exchange);
        }
    }

    private void HttpPostHandler(HttpExchange exchange) throws IOException {
        //need to parse post body
        //uses url encoding, but spaces are + signs
        //check content type in request header
        //application/x-www-form-urlencoded is a normal form post (eg username + password)
        //multipart/form-data is a file upload, or large data stream of some kind

        Headers requestHeaders = exchange.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();

        //check if we're receiving form data
        if (requestHeaders.getFirst("Content-type").equalsIgnoreCase("application/x-www-form-urlencoded")) {
            InputStream inputStream = exchange.getRequestBody();
            int b = inputStream.read();
            //using string builder over concatenation for memory, and it looks a little nicer syntactically
            StringBuilder sb = new StringBuilder();

            //a read of negative one means that the stream has ended
            while (b > -1) {
                sb.append((char) b);
                b = inputStream.read();
            }

            String reqBody = sb.toString();

            PostBodyParser parser = new PostBodyParser();
            HashMap<String,String> postParams = parser.ParseDatShit(reqBody);
            name = postParams.get("name");
            age = postParams.get("age");
        }

        //return the main page when we're done
        HttpGetHandler(exchange);
    }

    private void HttpGetHandler(HttpExchange exchange) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, 0);

        OutputStream responseBody = exchange.getResponseBody();
        String htmlForm = "<form method=\"post\">" +
                "Name: <input type=\"text\" name=\"name\"><br/>" +
                "Age: <input type=\"text\" name=\"age\"><br/>" +
                "<input type=\"submit\" value=\"Submit\">" +
                "</form>";
        String htmlHeader = "<style>* {font-family: Verdana, Geneva, sans-serif; }</style>";
        String nameHtml = "<p>name: " + name + "<br/>age: " + age + "</p>";
        String openingHtml = "<html><head>" + htmlHeader +
                "</head><body><h1>Hello from simple app</h1>" + nameHtml + htmlForm + "<p>";
        String closingHtml = "</p></body></html>";
        responseBody.write(openingHtml.getBytes());

        Headers requestHeaders = exchange.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            List values = requestHeaders.get(key);
            String s = key + " = " + values.toString() + "<br/>";
            responseBody.write(s.getBytes());
        }

        responseBody.write(closingHtml.getBytes());
        responseBody.close();
    }
}
