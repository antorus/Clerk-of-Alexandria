module com.echograd.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;


    opens com.echograd.librarymanagement to javafx.fxml;
    exports com.echograd.librarymanagement;
}