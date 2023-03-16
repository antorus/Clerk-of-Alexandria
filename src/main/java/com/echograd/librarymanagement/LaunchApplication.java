package com.echograd.librarymanagement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LaunchApplication extends Application {
    @Override
    public void start(Stage stage) {

        LibraryView libraryView = new LibraryView();
        Scene scene = new Scene(libraryView, 1160, 460);
        stage.setTitle("Library Management - Clerk of Alexandria");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}