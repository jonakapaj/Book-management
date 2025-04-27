package com.example.coursework.webserver.handlers;

import com.example.coursework.hibernateControllers.CustomHibernate;
import com.example.coursework.model.Comment;
import com.example.coursework.model.Publication;
import com.example.coursework.webserver.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentHandler implements HttpHandler {
    private final CustomHibernate customHibernate;
    private final Gson gson;

    public CommentHandler(CustomHibernate customHibernate) {
        this.customHibernate = customHibernate;
        this.gson = GsonUtils.createGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        int statusCode = 200;

        try {
            switch (method) {
                case "GET":
                    // Get comments by publication ID or get all comments
                    String query = exchange.getRequestURI().getQuery();
                    if (query != null && query.startsWith("publicationId=")) {
                        String idStr = query.substring(14);
                        int publicationId = Integer.parseInt(idStr);
                        Publication publication = customHibernate.getById(Publication.class, publicationId);
                        
                        if (publication != null) {
                            List<Comment> comments = customHibernate.getCommentsByPublication(publication);
                            response = gson.toJson(comments);
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"Publication not found\"}";
                        }
                    } else if (query != null && query.startsWith("id=")) {
                        String idStr = query.substring(3);
                        int id = Integer.parseInt(idStr);
                        Comment comment = customHibernate.getById(Comment.class, id);
                        
                        if (comment != null) {
                            response = gson.toJson(comment);
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"Comment not found\"}";
                        }
                    } else {
                        // Get all comments
                        List<Comment> allComments = customHibernate.getAllRecords(Comment.class);
                        response = gson.toJson(allComments);
                    }
                    break;

                case "POST":
                    // Create new comment
                    String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    
                    JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);
                    
                    // Get the publication this comment belongs to
                    int publicationId = jsonObject.get("publicationId").getAsInt();
                    Publication publication = customHibernate.getById(Publication.class, publicationId);
                    
                    if (publication != null) {
                        Comment newComment = new Comment();
                        newComment.setTitle(jsonObject.get("title").getAsString());
                        newComment.setMessage(jsonObject.get("message").getAsString());
                        newComment.setDateCreated(LocalDateTime.now());
                        newComment.setPublication(publication);
                        
                        // Set parent comment if it's a reply
                        if (jsonObject.has("parentCommentId")) {
                            int parentId = jsonObject.get("parentCommentId").getAsInt();
                            Comment parentComment = customHibernate.getById(Comment.class, parentId);
                            if (parentComment != null) {
                                newComment.setParentComment(parentComment);
                            }
                        }
                        
                        customHibernate.create(newComment);
                        statusCode = 201;
                        response = gson.toJson(newComment);
                    } else {
                        statusCode = 404;
                        response = "{\"error\": \"Publication not found\"}";
                    }
                    break;

                case "PUT":
                    // Update existing comment
                    query = exchange.getRequestURI().getQuery();
                    if (query != null && query.startsWith("id=")) {
                        String idStr = query.substring(3);
                        int id = Integer.parseInt(idStr);
                        Comment existingComment = customHibernate.getById(Comment.class, id);
                        
                        if (existingComment != null) {
                            requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                                    .lines()
                                    .collect(Collectors.joining("\n"));
                            
                            jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
                            
                            if (jsonObject.has("title")) existingComment.setTitle(jsonObject.get("title").getAsString());
                            if (jsonObject.has("message")) existingComment.setMessage(jsonObject.get("message").getAsString());
                            
                            // Update modify date
                            existingComment.setDateModified(LocalDateTime.now());
                            
                            customHibernate.update(existingComment);
                            response = gson.toJson(existingComment);
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"Comment not found\"}";
                        }
                    } else {
                        statusCode = 400;
                        response = "{\"error\": \"Missing id parameter\"}";
                    }
                    break;

                case "DELETE":
                    // Delete comment
                    query = exchange.getRequestURI().getQuery();
                    if (query != null && query.startsWith("id=")) {
                        String idStr = query.substring(3);
                        int id = Integer.parseInt(idStr);
                        Comment commentToDelete = customHibernate.getById(Comment.class, id);
                        
                        if (commentToDelete != null) {
                            customHibernate.delete(Comment.class, id);
                            response = "{\"success\": true, \"message\": \"Comment deleted successfully\"}";
                        } else {
                            statusCode = 404;
                            response = "{\"error\": \"Comment not found\"}";
                        }
                    } else {
                        statusCode = 400;
                        response = "{\"error\": \"Missing id parameter\"}";
                    }
                    break;

                default:
                    statusCode = 405;
                    response = "{\"error\": \"Method not allowed\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusCode = 500;
            response = "{\"error\": \"" + e.getMessage() + "\"}";
        }

        // Set response headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        
        // Write response
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}