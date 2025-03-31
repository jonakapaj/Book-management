package com.example.coursework.fxControllers;

import com.example.coursework.model.Admin;
import com.example.coursework.model.User;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;

/**
 * Utility class for JavaFX-related functionality
 */
public class JavaFxUtils {
    /**
     * Generates an alert dialog with the specified type, title and content.
     *
     * @param alertType The type of alert to show
     * @param title     The title of the alert
     * @param content   The content text of the alert
     */
    public static void generateAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Creates a custom cell factory for the admin employment duration column
     * that changes color based on employment duration
     * 
     * @param <T> The type of the TableView items
     * @return A cell factory for coloring cells based on employment duration
     */
    public static <T extends User> TableCell<T, String> createEmploymentDurationCellFactory() {
        return new TableCell<T, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                
                User user = getTableRow().getItem();
                if (user instanceof Admin admin) {
                    setText(admin.getEmploymentDuration());
                    
                    // Color based on employment duration
                    if (admin.isEmploymentCurrent(1)) {
                        setTextFill(Color.GREEN); // New admin (less than 1 year)
                    } else if (admin.isEmploymentCurrent(3)) {
                        setTextFill(Color.BLACK); // Regular admin (1-3 years)
                    } else {
                        setTextFill(Color.BLUE); // Experienced admin (more than 3 years)
                    }
                } else {
                    setText(null);
                    setStyle("");
                }
            }
        };
    }
}
