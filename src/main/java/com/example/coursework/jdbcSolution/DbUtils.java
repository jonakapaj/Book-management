package com.example.coursework.jdbcSolution;

import com.example.coursework.model.Client;

import java.sql.*;
//
//public class DbUtils {
//    public static Connection connectToDb() throws ClassNotFoundException {
//        Connection conn = null;
//        Class.forName("com.mysql.jdbc.Driver");
//        String DB_URL = "jdbc:mysql://sql7.freesqldatabase.com/sql7770264";
//        String USER = "sql7770264";
//        String PASS = "iTRIhth2vk";
//        try {
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return conn;
//    }
//
//    public void printClients() throws ClassNotFoundException, SQLException {
//        Connection connection = connectToDb();
//        Statement stmt = connection.createStatement();
//        String query = "Select * from user";
//        ResultSet rs = stmt.executeQuery(query);
//        while (rs.next()) {
//            String login = rs.getString("login");
//            String password = rs.getString(3);
//            var id = rs.getInt("id");
//
//            //Client client = new Client(login, password, ...);
//        }
//        //I should disconnect
//    }
//
//    public void insertClient(Client client) throws ClassNotFoundException, SQLException {
//        Connection connection = connectToDb();
//        String insertString = "insert into user(`login`, `password`, `name`, `surname`) VALUES (?,?,?,?)";
//        PreparedStatement insertAuthor = connection.prepareStatement(insertString);
//        insertAuthor.setString(1, client.getLogin());
//        insertAuthor.setString(2, client.getPassword());
////        insertAuthor.setInt(3, year);
////        insertAuthor.setString(4, id);
//        insertAuthor.execute();
//
//    }
//
//}
