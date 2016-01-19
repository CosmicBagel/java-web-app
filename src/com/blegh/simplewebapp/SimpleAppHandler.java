package com.blegh.simplewebapp;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Samuel on 1/18/2016.
 */
public class SimpleAppHandler implements HttpHandler {

    static String name = "no name";

    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            GetHandler(exchange);
        }

        if (requestMethod.equalsIgnoreCase("POST")) {
            PostHandler(exchange);
        }
    }

    private void PostHandler(HttpExchange exchange) throws IOException {

        //need to parse post body
        //uses url encoding, but spaces are + signs
        //check content type in request header
        //application/x-www-form-urlencoded is a normal form post (eg username + password)
        //multipart/form-data is a file upload, or large data stream of some kind

        Headers requestHeaders =  exchange.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();


        InputStream inputStream = exchange.getRequestBody();

        System.out.println(requestHeaders.getFirst("Content-type"));
        if (requestHeaders.getFirst("Content-type").equalsIgnoreCase("application/x-www-form-urlencoded")) {
            int butt = inputStream.read();
            StringBuilder sb = new StringBuilder();

            while (butt > -1) {
                sb.append((char) butt);
                butt = inputStream.read();
            }

            String reqBody = sb.toString();
            name = reqBody;
            System.out.println(reqBody);
        }



//        byte b[] = new byte[1024];
//        inputStream.read(b);
//        String reqBody = new String(b);
//        System.out.println(reqBody);


        //inputStream

        GetHandler(exchange);


//        Iterator<String> iter = keySet.iterator();
//        while(iter.hasNext()) {
//            String key = iter.next();
//            List values = requestHeaders.get(key);
//            String s = key + " = " + values.toString();
//            System.out.println(s);
//        }
    }

    private void GetHandler(HttpExchange exchange) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200,0);

        OutputStream responseBody = exchange.getResponseBody();
        String htmlForm  = "<form method=\"post\">" +
                "Name: <input type=\"text\" name=\"name\"><br/>" +
                "<input type=\"submit\" value=\"Submit\">" +
                "</form>";
        String htmlHeader = "<style>* {font-family: Verdana, Geneva, sans-serif; }</style>";
        String nameHtml = "<p>name: " + name + "</p>";
        String openingHtml = "<html><head>" + htmlHeader +
                "</head><body><h1>Hello from simple app</h1>" + nameHtml + htmlForm + "<p>";
        String closingHtml = "</p></body></html>";
        responseBody.write(openingHtml.getBytes());

        Headers requestHeaders = exchange.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();
        Iterator<String> iter = keySet.iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            List values = requestHeaders.get(key);
            String s = key + " = " + values.toString() + "<br/>";
            responseBody.write(s.getBytes());
        }

        responseBody.write(closingHtml.getBytes());
        responseBody.close();
    }
}
