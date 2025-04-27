module com.example.coursework {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires lombok;
    requires mysql.connector.j;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires transitive jakarta.persistence;
    requires java.naming;
    requires com.google.gson;
    requires transitive jdk.httpserver;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.coursework to javafx.fxml;
    exports com.example.coursework;
    
    exports com.example.coursework.consoleFunctionality;
    opens com.example.coursework.consoleFunctionality to javafx.fxml;
    
    exports com.example.coursework.fxControllers;
    opens com.example.coursework.fxControllers to javafx.fxml, org.hibernate.orm.core;
    
    exports com.example.coursework.model;
    opens com.example.coursework.model to javafx.fxml, org.hibernate.orm.core, jakarta.persistence;
    
    exports com.example.coursework.model.enums;
    exports com.example.coursework.webserver;
    exports com.example.coursework.webserver.handlers;
    exports com.example.coursework.hibernateControllers;
    opens com.example.coursework.hibernateControllers to org.hibernate.orm.core;
}