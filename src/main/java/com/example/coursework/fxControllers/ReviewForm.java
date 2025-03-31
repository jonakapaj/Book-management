package com.example.coursework.fxControllers;

import com.example.coursework.hibernateControllers.CustomHibernate;
import com.example.coursework.model.Comment;
import com.example.coursework.model.Publication;
import com.example.coursework.model.Client;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.time.LocalDateTime;

public class ReviewForm {

    @FXML
    public Label publicationTitleLabel;
    @FXML
    public Label reviewTypeLabel;
    @FXML
    public TextArea reviewTextArea;
    @FXML
    public TextField commentTitleField;
    @FXML
    public Label parentCommentTitle;
    @FXML
    public HBox parentCommentBox;

    @Setter
    private Stage dialogStage;
    private Publication publication;
    private CustomHibernate customHibernate;
    private EntityManagerFactory entityManagerFactory;
    private Comment parentComment;
    private Client currentUser;

    @FXML
    public void initialize() {
        customHibernate = new CustomHibernate(entityManagerFactory);
        // Hide the parent comment box by default (only show when replying)
        if (parentCommentBox != null) {
            parentCommentBox.setVisible(false);
            parentCommentBox.setManaged(false);
        }
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
        publicationTitleLabel.setText("Review for: " + publication.getTitle());
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.customHibernate = new CustomHibernate(entityManagerFactory);
    }
    
    /**
     * Configure the form for replying to a comment
     */
    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
        this.publication = parentComment.getPublication();
        
        // Update UI to indicate this is a reply
        reviewTypeLabel.setText("Reply to Comment");
        publicationTitleLabel.setText("Reply for: " + publication.getTitle());
        
        // Show the parent comment information
        parentCommentTitle.setText(parentComment.getTitle());
        parentCommentBox.setVisible(true);
        parentCommentBox.setManaged(true);
    }
    
    public void setCurrentUser(Client currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    public void submitReview() {
        if (reviewTextArea.getText() != null && !reviewTextArea.getText().isEmpty() && 
            commentTitleField.getText() != null && !commentTitleField.getText().isEmpty()) {
            
            // Create a new Comment object
            Comment comment = new Comment();
            comment.setMessage(reviewTextArea.getText());
            comment.setPublication(publication);
            comment.setTitle(commentTitleField.getText());
            comment.setDateCreated(LocalDateTime.now());
            
            // Set the owner if available
            if (currentUser != null) {
                comment.setOwner(currentUser);
            }
            
            // Set parent comment if this is a reply
            if (parentComment != null) {
                comment.setParentComment(parentComment);
            }
            
            try {
                customHibernate.create(comment);
                String successMessage = parentComment != null ? 
                                       "Reply submitted successfully!" : 
                                       "Review submitted successfully!";
                JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.INFORMATION, 
                                         "Success", successMessage);
            } catch (Exception e) {
                JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.ERROR, 
                                         "Error", "Could not submit: " + e.getMessage());
                e.printStackTrace();
            }
            dialogStage.close(); // Close the dialog
        } else {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.WARNING, 
                                     "Warning", "Please enter a title and review.");
        }
    }

    @FXML
    public void cancel() {
        dialogStage.close(); // Close the dialog
    }
}