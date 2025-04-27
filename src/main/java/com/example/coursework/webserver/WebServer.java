package com.example.coursework.webserver;

import com.example.coursework.hibernateControllers.CustomHibernate;
import com.example.coursework.webserver.handlers.*;
import com.sun.net.httpserver.HttpServer;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Web server component for the Book Management application
 * Runs alongside the JavaFX UI on port 9090
 */
public class WebServer {
    private final HttpServer server;
    private final int port;
    private final CustomHibernate customHibernate;

    public WebServer(int port, EntityManagerFactory entityManagerFactory) throws IOException {
        this.port = port;
        this.customHibernate = new CustomHibernate(entityManagerFactory);
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Configure request handlers
        configureEndpoints();
        
        // Set up thread pool for handling requests
        server.setExecutor(Executors.newFixedThreadPool(10));
    }

    private void configureEndpoints() {
        // Books/publications endpoints
        server.createContext("/api/books", new BookHandler(customHibernate));
        server.createContext("/api/books/available", new AvailableBooksHandler(customHibernate));
        
        // Comments endpoints
        server.createContext("/api/comments", new CommentHandler(customHibernate));
        
        // Users endpoints
        server.createContext("/api/users", new UserHandler(customHibernate));
        
        // Default handler for root endpoint
        server.createContext("/", new RootHandler());
    }

    public void start() {
        server.start();
        System.out.println("Web server started on port " + port);
    }

    public void stop() {
        server.stop(0);
        System.out.println("Web server stopped");
    }
}