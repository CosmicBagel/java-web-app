package com.blegh.simplewebapp;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by Samuel on 1/17/2016.
 */
public class AppMain {
    public static void main(String args[]) throws IOException {
        System.out.println("hello from simple app");
        InetSocketAddress addr = new InetSocketAddress(8080);
        HttpServer server = HttpServer.create(addr, 0);

        server.createContext("/", new SimpleAppHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("Server is listening on port 8080");
    }
}
