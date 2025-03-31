package com.example.coursework.consoleFunctionality;

import com.example.coursework.model.*;
import com.example.coursework.model.enums.Format;
import com.example.coursework.model.enums.Genre;
import com.example.coursework.model.enums.Language;
import com.example.coursework.model.enums.MangaGenre;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {
    public static void generateUserMenu(Scanner scanner, BookExchange bookExchange) {

        //In this case cmd is an int
        var cmd = 0;
        while (cmd != 6) {
            System.out.println("""                                                                
                    Choose an option:
                    1 - create a user
                    2 - update a user
                    3 - delete a user
                    4 - get all users
                    5 - get user
                    6 - return to main menu
                    """);

            cmd = scanner.nextInt(); //nextInt doesn't have a next line symbol, so sequent line is necessary to not have any issues
            scanner.nextLine();

            switch (cmd) {
                case 1:
                    System.out.println("Which user type? C/A");
                    var line = scanner.nextLine();
                    if (line.equals("C")) {
                        System.out.println("Enter user data: login;psw;name;surname;email;address;YYYY-MM-DD;");
                        line = scanner.nextLine();

                        String[] info = line.split(";");

//                        Client client = new Client(info[0], info[1], info[2], info[3], info[4], info[6], LocalDate.parse(info[5]));
//                        System.out.println(client);
                        //Add this user to user list
                       // bookExchange.getAllUsers().add(client);
                    } else if (line.equals("A")) {
                        //Admin creation
                        System.out.println("Enter admin data: login;psw;name;surname;email;phoneNum;YYYY-MM-DD;");
                        line = scanner.nextLine();

                        String[] info = line.split(";");
                        
                        Admin admin = new Admin(info[2], info[3], info[0], info[1], info[4], LocalDate.parse(info[6]), info[5]);
                        System.out.println(admin);
                        //Add this admin to user list
                        bookExchange.getAllUsers().add(admin);
                    } else {
                        System.out.println("No such user type");
                    }
                    break;
                case 2:
                    System.out.println("Enter user login");
                    var userLogin = scanner.nextLine();
                    for(User u: bookExchange.getAllUsers()){
                        if(u.getLogin().equals(userLogin)){
                            //TODO ask user for update info and then update
                            if(u instanceof Client client){
                                System.out.println("Enter updated client data: name;surname;email;address;YYYY-MM-DD;");
                                String updateInfo = scanner.nextLine();
                                String[] info = updateInfo.split(";");
                                
                                client.setName(info[0]);
                                client.setSurname(info[1]);
                                client.setEmail(info[2]);
                                client.setAddress(info[3]);
                                client.setBirthDate(LocalDate.parse(info[4]));
                                System.out.println("Client updated successfully: " + client);
                            }else if (u instanceof Admin admin){
                                System.out.println("Enter updated admin data: name;surname;email;phoneNum;YYYY-MM-DD;");
                                String updateInfo = scanner.nextLine();
                                String[] info = updateInfo.split(";");
                                
                                admin.setName(info[0]);
                                admin.setSurname(info[1]);
                                admin.setEmail(info[2]);
                                admin.setPhoneNum(info[3]);
                                admin.setEmploymentDate(LocalDate.parse(info[4]));
                                System.out.println("Admin updated successfully: " + admin);
                            }
                        }
                    }
                    break;
                case 3:
                    //TODO find a user by login and then just remove
                    System.out.println("Enter user login to delete:");
                    String loginToDelete = scanner.nextLine();
                    User userToRemove = null;
                    
                    for(User u: bookExchange.getAllUsers()){
                        if(u.getLogin().equals(loginToDelete)){
                            userToRemove = u;
                            break;
                        }
                    }
                    
                    if(userToRemove != null){
                        bookExchange.getAllUsers().remove(userToRemove);
                        System.out.println("User removed successfully!");
                    } else {
                        System.out.println("User with login " + loginToDelete + " not found.");
                    }
                    break;
                case 4:
                    for(User u: bookExchange.getAllUsers()){
                        System.out.println(u);
                    }
                    //User print using lambdas
                    //bookExchange.getAllUsers().forEach(u-> System.out.println(u));
                    break;
            }
        }
    }

    public static void generatePublicationMenu(Scanner scanner, BookExchange bookExchange) {
        // Create a menu for publications. Very similar to users
        var cmd = 0;
        while (cmd != 6) {
            System.out.println("""                                                                
                    Choose an option:
                    1 - create a publication
                    2 - update a publication
                    3 - delete a publication
                    4 - get all publications
                    5 - get publication
                    6 - return to main menu
                    """);

            cmd = scanner.nextInt();
            scanner.nextLine();

            switch (cmd) {
                case 1:
                    System.out.println("Which publication type? B (Book) / M (Manga)");
                    var line = scanner.nextLine();
                    if (line.equals("B")) {
                        System.out.println("Enter book data: title;description;author;format;genre;language;isbn;publisher");
                        line = scanner.nextLine();
                        String[] info = line.split(";");
                        
                        // Create a Book object and add it to the list
                        Book book = new Book();
                        book.setTitle(info[0]);
                        book.setDescription(info[1]);
                        book.setAuthor(info[2]);
                        book.setFormat(Format.valueOf(info[3]));
                        book.setGenre(Genre.valueOf(info[4]));
                        book.setLanguage(Language.valueOf(info[5]));
                        book.setIsbn(info[6]);
                        book.setPublisher(info[7]);
                        
                        bookExchange.getAllPublications().add(book);
                        System.out.println("Book added successfully!");
                    } else if (line.equals("M")) {
                        System.out.println("Enter manga data: title;description;author;mangaGenre;language;isColor(true/false);illustrator;volume");
                        line = scanner.nextLine();
                        String[] info = line.split(";");
                        
                        // Create a Manga object and add it to the list
                        Manga manga = new Manga();
                        manga.setTitle(info[0]);
                        manga.setDescription(info[1]);
                        manga.setAuthor(info[2]);
                        manga.setMangaGenre(MangaGenre.valueOf(info[3]));
                        manga.setOriginalLanguage(Language.valueOf(info[4]));
                        manga.setColor(Boolean.parseBoolean(info[5]));
                        manga.setIllustrator(info[6]);
                        manga.setVolume(Integer.parseInt(info[7]));
                        manga.setCreationDate(LocalDateTime.now());
                        
                        bookExchange.getAllPublications().add(manga);
                        System.out.println("Manga added successfully!");
                    } else {
                        System.out.println("No such publication type");
                    }
                    break;
                case 2:
                    System.out.println("Enter publication title to update:");
                    String titleToUpdate = scanner.nextLine();
                    boolean found = false;
                    
                    for(Publication p: bookExchange.getAllPublications()) {
                        if(p.getTitle().equals(titleToUpdate)) {
                            found = true;
                            if(p instanceof Book book) {
                                System.out.println("Enter updated book data: title;description;author;format;genre;language;isbn;publisher");
                                String updateInfo = scanner.nextLine();
                                String[] info = updateInfo.split(";");
                                
                                book.setTitle(info[0]);
                                book.setDescription(info[1]);
                                book.setAuthor(info[2]);
                                book.setFormat(Format.valueOf(info[3]));
                                book.setGenre(Genre.valueOf(info[4]));
                                book.setLanguage(Language.valueOf(info[5]));
                                book.setIsbn(info[6]);
                                book.setPublisher(info[7]);
                                
                                System.out.println("Book updated successfully!");
                            } else if(p instanceof Manga manga) {
                                System.out.println("Enter updated manga data: title;description;author;mangaGenre;language;isColor(true/false);illustrator;volume");
                                String updateInfo = scanner.nextLine();
                                String[] info = updateInfo.split(";");
                                
                                manga.setTitle(info[0]);
                                manga.setDescription(info[1]);
                                manga.setAuthor(info[2]);
                                manga.setMangaGenre(MangaGenre.valueOf(info[3]));
                                manga.setOriginalLanguage(Language.valueOf(info[4]));
                                manga.setColor(Boolean.parseBoolean(info[5]));
                                manga.setIllustrator(info[6]);
                                manga.setVolume(Integer.parseInt(info[7]));
                                
                                System.out.println("Manga updated successfully!");
                            }
                            break;
                        }
                    }
                    
                    if(!found) {
                        System.out.println("Publication with title " + titleToUpdate + " not found.");
                    }
                    break;
                case 3:
                    System.out.println("Enter publication title to delete:");
                    String titleToDelete = scanner.nextLine();
                    Publication publicationToRemove = null;
                    
                    for(Publication p: bookExchange.getAllPublications()) {
                        if(p.getTitle().equals(titleToDelete)) {
                            publicationToRemove = p;
                            break;
                        }
                    }
                    
                    if(publicationToRemove != null) {
                        bookExchange.getAllPublications().remove(publicationToRemove);
                        System.out.println("Publication removed successfully!");
                    } else {
                        System.out.println("Publication with title " + titleToDelete + " not found.");
                    }
                    break;
                case 4:
                    System.out.println("All publications:");
                    for(Publication p: bookExchange.getAllPublications()) {
                        System.out.println(p);
                    }
                    break;
                case 5:
                    System.out.println("Enter publication title to find:");
                    String titleToFind = scanner.nextLine();
                    boolean publicationFound = false;
                    
                    for(Publication p: bookExchange.getAllPublications()) {
                        if(p.getTitle().equals(titleToFind)) {
                            System.out.println(p);
                            publicationFound = true;
                            break;
                        }
                    }
                    
                    if(!publicationFound) {
                        System.out.println("Publication with title " + titleToFind + " not found.");
                    }
                    break;
            }
        }
    }

    public static void writeToFileAsObject(String fileName, BookExchange bookExchange) {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
            objectOutputStream.writeObject(bookExchange);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static BookExchange readFromFile(String fileName) {
        BookExchange bookExchange = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            //We must specify the type of data that is in the file, because the program doesn't know
            bookExchange = (BookExchange) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return bookExchange;
    }

    public static void writeUsersToFile(List<User> userList) {
        try (FileWriter fileWriter = new FileWriter("users.txt")) {

            for (User u : userList) {
                fileWriter.write(u.getId() + ":" + u.getLogin() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getUsersFromFile(){
        //Initialize List
        List<User> users = new ArrayList<>();

        //logic to read line by line and create CLient/Admin objects
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Expected format: type:id:login:password:name:surname:email:specificFields
                String[] userData = line.split(":");
                
                if (userData.length >= 8) {
                    String type = userData[0];
                    
                    if (type.equals("C")) {  // Client
                        // Expected format: C:id:login:password:name:surname:email:address:birthDate
                        Client client = new Client(
                            userData[4],  // name
                            userData[5],  // surname
                            userData[2],  // login
                            userData[3],  // password
                            userData[6],  // email
                            LocalDate.parse(userData[8]),  // birthDate
                            userData[7]   // address
                        );
                        client.setId((int) Long.parseLong(userData[1]));  // set ID
                        users.add(client);
                    } else if (type.equals("A")) {  // Admin
                        // Expected format: A:id:login:password:name:surname:email:phoneNum:employmentDate
                        Admin admin = new Admin(
                            userData[4],  // name
                            userData[5],  // surname
                            userData[2],  // login
                            userData[3],  // password
                            userData[6],  // email
                            LocalDate.parse(userData[8]),  // employmentDate
                            userData[7]   // phoneNum
                        );
                        admin.setId((int) Long.parseLong(userData[1]));  // set ID
                        users.add(admin);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }
        
        return users;
    }
}
