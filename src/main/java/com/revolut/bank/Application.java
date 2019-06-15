package com.revolut.bank;

import com.revolut.bank.configuration.RevolutBank;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;

public class Application {

    public static final URI BASE_URI = URI.create("http://localhost:8080/");

    public static void main(String[] args) throws IOException {
        HttpServer server = start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.shutdown()));

        System.out.println("\nRevolut Bank is listening on " + BASE_URI);
        System.out.println("Press Ctrl+C to terminate server...\n");
    }

    public static HttpServer start() {
        HttpServer server = getServer();

        try {
            server.start();
        } catch (IOException exception) {
            System.out.println("A problem occured when starting Revolut Bank server...");
            exception.printStackTrace();
        }

        return server;
    }

    private static HttpServer getServer() {
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, new RevolutBank());
    }

}
