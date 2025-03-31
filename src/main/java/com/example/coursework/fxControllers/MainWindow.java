package com.example.coursework.fxControllers;

import com.example.coursework.HelloApplication;
import com.example.coursework.hibernateControllers.CustomHibernate;
import com.example.coursework.model.*;
import com.example.coursework.model.enums.Format;
import com.example.coursework.model.enums.Genre;
import com.example.coursework.model.enums.Language;
import com.example.coursework.model.enums.MangaGenre;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import jakarta.persistence.EntityManagerFactory;

public class MainWindow implements Initializable {
    @FXML
    public ListView<Publication> myPublicationListField;
    @FXML
    public TextField publicationTitleField;
    @FXML
    public TextArea publicationDescriptionField;
    @FXML
    public RadioButton radioBook;
    @FXML
    public RadioButton radioManga;
    @FXML
    public ChoiceBox<Format> formatField;
    @FXML
    public ChoiceBox<Genre> genreField;
    @FXML
    public ChoiceBox<Language> languageField;
    @FXML
    public TextField publisherField;
    @FXML
    public TextField isbnField;
    @FXML
    public TextArea authorField;
    @FXML
    public TextArea aboutBook;
    @FXML
    public ListView<String> commentListView;
    @FXML
    public ListView<Publication> availableBookList;
    @FXML
    public ComboBox<Language> mangaLanguageField;
    @FXML
    public ComboBox<MangaGenre> mangaGenreField;
    @FXML
    public CheckBox colorCheckbox;
    @FXML
    public TextField illustratorField;
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, Long> idColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> surnameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> phoneNumberColumn;
    @FXML
    private TableColumn<User, String> employmentDurationColumn;
    @FXML
    public TextField volumeField;
    CustomHibernate customHibernate;
    User currentUser;
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        formatField.getItems().addAll(Format.values());
        genreField.getItems().addAll(Genre.values());
        languageField.getItems().addAll(Language.values());
        mangaLanguageField.getItems().addAll(Language.values());
        mangaGenreField.getItems().addAll(MangaGenre.values());
        availableBookList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadReviews());
        myPublicationListField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadPublicationInfo());
        disableFields();
        toggleMangaFields();
        radioBook.setOnAction(event -> toggleMangaFields());
        radioManga.setOnAction(event -> toggleMangaFields());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

    }

    //This is executed after initialize, so do not put eny code that requires db in initialize
    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        customHibernate = new CustomHibernate(entityManagerFactory);
        loadPublications();
    }

    public void createNewPublication() {
        try {
            if (radioBook.isSelected()) {
                Book book = new Book(
                        publicationTitleField.getText(),
                        publicationDescriptionField.getText(),
                        authorField.getText(),
                        formatField.getValue(),
                        genreField.getValue(),
                        languageField.getValue(),
                        isbnField.getText(),
                        publisherField.getText()
                );
                customHibernate.create(book);
                JavaFxUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Book created successfully");
            } else if (radioManga.isSelected()) {
                Manga manga = new Manga(
                        publicationTitleField.getText(),
                        publicationDescriptionField.getText(),
                        LocalDateTime.now(),
                        authorField.getText(),
                        mangaGenreField.getValue(),
                        mangaLanguageField.getValue(),
                        colorCheckbox.isSelected(),
                        illustratorField.getText(),
                        Integer.parseInt(volumeField.getText())
                );
                customHibernate.create(manga);
                JavaFxUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Manga created successfully");
            } else {
                JavaFxUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "Please select Book or Manga");
                return;
            }
            clearPublicationFields();
            loadPublications();
        } catch (NumberFormatException e) {
            JavaFxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Invalid input for volume. Must be a number.");
        } catch (Exception e) {
            JavaFxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Could not create publication.");
            e.printStackTrace();
        }
    }


    //This method is called when an element from list is selected
    private void loadPublications() {
        myPublicationListField.getItems().clear();
        List<Publication> publications = customHibernate.getAllRecords(Publication.class);
        myPublicationListField.getItems().addAll(publications);
        availableBookList.getItems().clear();
        availableBookList.getItems().addAll(customHibernate.getAvailablePublications());
    }

    public void updatePublication() {
        Publication publication = myPublicationListField.getSelectionModel().getSelectedItem();
        if (publication != null) {
            try {
                publication.setTitle(publicationTitleField.getText());
                publication.setDescription(publicationDescriptionField.getText());
                publication.setAuthor(authorField.getText());
                if (publication instanceof Book) {
                    Book book = (Book) publication;
                    book.setFormat(formatField.getValue());
                    book.setGenre(genreField.getValue());
                    book.setLanguage(languageField.getValue());
                    book.setPublisher(publisherField.getText());
                    book.setIsbn(isbnField.getText());
                } else if (publication instanceof Manga) {
                    Manga manga = (Manga) publication;
                    manga.setMangaGenre(mangaGenreField.getValue());
                    manga.setOriginalLanguage(mangaLanguageField.getValue());
                    manga.setColor(colorCheckbox.isSelected());
                    manga.setIllustrator(illustratorField.getText());
                    manga.setVolume(Integer.parseInt(volumeField.getText()));
                }
                customHibernate.update(publication);
                loadPublications();
                JavaFxUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Publication updated successfully");
            } catch (NumberFormatException e) {
                JavaFxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Invalid input for volume. Must be a number.");
            } catch (Exception e) {
                JavaFxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Could not update publication.");
                e.printStackTrace();
            }
        } else {
            JavaFxUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "No publication selected to update.");
        }
    }

    public void deletePublication() {
        Publication publication = myPublicationListField.getSelectionModel().getSelectedItem();
        if (publication != null) {
            try {
                customHibernate.delete(Publication.class, publication.getId());
                loadPublications();
                clearPublicationFields();
                JavaFxUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Publication deleted successfully");
            } catch (Exception e) {
                JavaFxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Could not delete publication.");
                e.printStackTrace();
            }
        } else {
            JavaFxUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "No publication selected to delete.");
        }
    }

    public void disableFields() {
        boolean isBookSelected = radioBook.isSelected();
        boolean isMangaSelected = radioManga.isSelected();
        formatField.setDisable(!isBookSelected);
        genreField.setDisable(!isBookSelected);
        languageField.setDisable(!isBookSelected);
        publisherField.setDisable(!isBookSelected);
        isbnField.setDisable(!isBookSelected);

        mangaGenreField.setDisable(isMangaSelected);
        mangaLanguageField.setDisable(isMangaSelected);
        colorCheckbox.setDisable(isMangaSelected);
        illustratorField.setDisable(isMangaSelected);
        volumeField.setDisable(isMangaSelected);
    }

    public void clearPublicationFields() {
        publicationTitleField.clear();
        publicationDescriptionField.clear();
        authorField.clear();
        formatField.setValue(null);
        genreField.setValue(null);
        languageField.setValue(null);
        publisherField.clear();
        isbnField.clear();
        mangaGenreField.setValue(null);
        mangaLanguageField.setValue(null);
        colorCheckbox.setSelected(false);
        illustratorField.clear();
        volumeField.clear();
        radioBook.setSelected(false);
        radioManga.setSelected(false);
    }

    public void toggleMangaFields() {
        boolean isMangaSelected = radioManga.isSelected();
        mangaGenreField.setVisible(isMangaSelected);
        mangaLanguageField.setVisible(isMangaSelected);
        colorCheckbox.setVisible(isMangaSelected);
        illustratorField.setVisible(isMangaSelected);
        volumeField.setVisible(isMangaSelected);
    }


    //</editor-fold>

    //<editor-fold desc="Main tab functionality">
    public void loadReviewForm() {
        Publication selectedPublication = availableBookList.getSelectionModel().getSelectedItem();
        if (selectedPublication != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("review-form.fxml"));
                Parent parent = fxmlLoader.load();
                ReviewForm reviewFormController = fxmlLoader.getController();
                reviewFormController.setPublication(selectedPublication);
                Stage stage = new Stage();
                reviewFormController.setDialogStage(stage);
                reviewFormController.setEntityManagerFactory(entityManagerFactory); // Pass EntityManagerFactory

                Scene scene = new Scene(parent);
                stage.setTitle("Submit Review");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait(); // Show and wait for the user to interact
                loadPublications(); // Refresh the publication list after review

            } catch (IOException e) {
                JavaFxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Could not load review form.");
                e.printStackTrace();
            }
        } else {
            JavaFxUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "Please select a publication to review.");
        }
        loadComments(selectedPublication);
    }

    public void loadComments(Publication publication) {
        // load comments to list view


    }

    private TreeItem<Comment> buildCommentTree(Comment comment) {
        TreeItem<Comment> root = new TreeItem<>(comment);
        for (Comment reply : comment.getReplies()) {
            root.getChildren().add(buildCommentTree(reply));
        }
        return root;
    }

    public void requestPublication() {
        //We will do this later
    }

    public void loadReviews() {
        Publication publication = availableBookList.getSelectionModel().getSelectedItem();
        if (publication != null) {
            List<String> comments = customHibernate
                    .getCommentsByPublication(publication)
                    .stream()
                    .map(Comment::getTitle)
                    .toList();
            commentListView.getItems().clear();
            commentListView.getItems().addAll(comments);
        }
    }

    public void loadPublicationInfo() {
        Publication publication = myPublicationListField.getSelectionModel().getSelectedItem();

        if (publication != null) {
            publicationTitleField.setText(publication.getTitle());
            publicationDescriptionField.setText(publication.getDescription());
            authorField.setText(publication.getAuthor());
            if (publication instanceof Book book) {
                formatField.setValue(book.getFormat());
                genreField.setValue(book.getGenre());
                languageField.setValue(book.getLanguage());
                publisherField.setText(book.getPublisher());
                isbnField.setText(book.getIsbn());
                radioBook.setSelected(true);
                radioManga.setSelected(false);
            } else if (publication instanceof Manga manga) {
                mangaGenreField.setValue(manga.getMangaGenre());
                mangaLanguageField.setValue(manga.getOriginalLanguage());
                colorCheckbox.setSelected(manga.isColor());
                illustratorField.setText(manga.getIllustrator());
                volumeField.setText(String.valueOf(manga.getVolume()));
                radioBook.setSelected(false);
                radioManga.setSelected(true);
            }
            disableFields();

        }

    }

    //</editor-fold>

    //<editor-fold desc="User management tab">
    //TODO do the same functionality for users - you need a list to view users, you need to delete users and you need to update them
    //I have prepared tableview for the future
    public void loadUserForm() throws IOException {
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized.");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registration.fxml"));
        Parent parent = fxmlLoader.load();
        Registration registration = fxmlLoader.getController();
        registration.setData(entityManagerFactory, false);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("Book exchange platform!");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        loadUsers();
    }

    public void loadUsers() {
        List<User> users = customHibernate.getAllRecords(User.class);
        userTableView.getItems().clear();
        userTableView.getItems().addAll(users);
    }

    //Implementing user management functionality
    public void updateUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registration.fxml"));
                Parent parent = fxmlLoader.load();
                Registration registration = fxmlLoader.getController();
                
                // Set data and pass the selected user for editing
                registration.setDataForEdit(entityManagerFactory, selectedUser);
                
                Stage stage = new Stage();
                Scene scene = new Scene(parent);
                stage.setTitle("Edit User");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                loadUsers(); // Refresh users list after editing
            } catch (Exception e) {
                JavaFxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Could not update user.");
                e.printStackTrace();
            }
        } else {
            JavaFxUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "No user selected to update.");
        }
    }
    
    public void deleteUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Confirm Delete");
                confirmDialog.setHeaderText("Delete User");
                confirmDialog.setContentText("Are you sure you want to delete user: " + 
                                         selectedUser.getName() + " " + selectedUser.getSurname() + "?");
                
                if (confirmDialog.showAndWait().get() == ButtonType.OK) {
                    customHibernate.delete(User.class, selectedUser.getId());
                    loadUsers();
                    JavaFxUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully");
                }
            } catch (Exception e) {
                JavaFxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Could not delete user.");
                e.printStackTrace();
            }
        } else {
            JavaFxUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "No user selected to delete.");
        }
    }
}