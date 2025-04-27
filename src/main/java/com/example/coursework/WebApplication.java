package com.example.coursework;

import com.example.coursework.webserver.WebServer;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class WebApplication {
    public static void main(String[] args) {
        try {
            // Initialize the EntityManagerFactory
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("book_exchange");
            
            // Create and start the web server on port 9090
            WebServer webServer = new WebServer(9090, entityManagerFactory);
            webServer.start();
            
            System.out.println("Book Management Web Application started successfully on port 9090");
            System.out.println("Press Ctrl+C to shut down...");
        } catch (Exception e) {
            System.err.println("Error starting the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}