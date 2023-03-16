package com.echograd.librarymanagement;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class BooksForm extends Stage {
    private boolean updateForm;
    public BooksForm(LibraryViewModel viewModel, boolean isUpdate){

        this.updateForm = isUpdate;
        System.out.println("Update form is: " + updateForm);
        VBox vbox = new VBox();

        Label labelTitle = new Label("Title: ");
        Label labelAuthors = new Label("Authors: ");
        Label labelIsbn = new Label("ISBN: ");
        Label labelPublicationDate = new Label("Publication date (YYYY-MM-DD): ");
        Label labelPublisher = new Label("Publisher: ");
        Label labelNewBook = new Label("Add new book");

        TextField titleText = new TextField();
        TextField authorsText = new TextField();
        TextField isbnText = new TextField();
        TextField publicationDateText = new TextField();
        TextField publisherText = new TextField();

        Pattern pattern = Pattern.compile("\\d{1,4}-1|\\d{1,4}-0|" +
                "\\d{1,4}-0[1-9]-|\\d{1,4}-1[0-2]-|\\d{1,4}-1[0-2]|\\d{1,4}-0[1-9]|" +
                "\\d{1,4}-|\\d{1,4}|" +
                "\\d{1,4}-0[1-9]-[0-2]|\\d{1,4}-0[1-9]-[0-2][1-9]|\\d{1,4}-0[1-9]-3[0-1]|\\d{1,4}-0[1-9]-3|" +
                "\\d{1,4}-1[0-2]-[0-2]|\\d{1,4}-1[0-2]-[0-2][1-9]|\\d{1,4}-1[0-2]-3[0-1]|\\d{1,4}-1[0-2]-3");

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();
            if (pattern.matcher(text).matches()) {
                if (change.isAdded() && text.length() == 4 || change.isAdded() && text.length() == 7) {
                    change.setText(change.getText() + "-");
                    change.setCaretPosition(change.getCaretPosition() + 1);
                    change.setAnchor(change.getAnchor() + 1);
                }
                return change;
            } else {
                return null;
            }
        };

        TextFormatter isbnFormatter = new TextFormatter<>(change -> {
            if(change.getControlNewText().length() <= 13)
                return change;
            else
                return null;
        });

        TextFormatter dateFormatter = new TextFormatter<>(filter);

        publicationDateText.setTextFormatter(dateFormatter);
        isbnText.setTextFormatter(isbnFormatter);

        titleText.textProperty().bindBidirectional(viewModel.booksFormTitleTextProperty());
        authorsText.textProperty().bindBidirectional(viewModel.booksFormAuthorsText());
        isbnText.textProperty().bindBidirectional(viewModel.booksFormIsbnText());
        publicationDateText.textProperty().bindBidirectional(viewModel.booksFormPublicationDateText());
        publisherText.textProperty().bindBidirectional(viewModel.booksFormPublisherText());

        Button booksSubmitButton = new Button("Submit");
        Button booksCloseButton = new Button("Close");

        GridPane gp = new GridPane();

        gp.setPadding( new Insets(10) );
        gp.setHgap( 4 );
        gp.setVgap( 8 );
        ColumnConstraints col0 = new ColumnConstraints();
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(70);

        gp.getColumnConstraints().addAll(col0, col1);

        gp.add(labelNewBook, 1, 1);
        gp.add(labelTitle, 0, 2);           gp.add(titleText, 1, 2);
        gp.add(labelAuthors, 0, 3);         gp.add(authorsText, 1, 3);
        gp.add(labelPublisher, 0, 4);       gp.add(publisherText, 1, 4);
        gp.add(labelIsbn, 0, 5);            gp.add(isbnText, 1, 5);
        gp.add(labelPublicationDate, 0, 6); gp.add(publicationDateText, 1, 6);
        gp.add(booksSubmitButton, 0, 8);    gp.add(booksCloseButton, 1, 8);

        booksSubmitButton.setOnAction(event -> {
            try {
                viewModel.handleSubmitBooksButtonClick(event, this);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        booksCloseButton.setOnAction( event -> {
                this.close();
        });

        //Update fields based on if it's an update form or not
        if(updateForm == true){
            ObservableList<Book> books = LibraryView.getBooksTableView().getSelectionModel().getSelectedItems();
            Book book = books.get(0);
            viewModel.setBooksFormTitleText(book.getTitle());
            viewModel.setBooksFormAuthorsText(book.getAuthors());
            viewModel.setBooksFormPublisherText(book.getPublisher());
            viewModel.setBooksFormIsbnText(book.getIsbn());
            viewModel.setBooksFormPublicationDateText(book.getPublicationDate().toString());

        } else {
            viewModel.setBooksFormTitleText("");
            viewModel.setBooksFormAuthorsText("");
            viewModel.setBooksFormPublisherText("");
            viewModel.setBooksFormIsbnText("");
            viewModel.setBooksFormPublicationDateText("");
        }

        //Check for empty fields
        booksSubmitButton.disableProperty().bind(authorsText.textProperty().isEmpty());
        booksSubmitButton.disableProperty().bind(titleText.textProperty().isEmpty());
        booksSubmitButton.disableProperty().bind(publisherText.textProperty().isEmpty());
        booksSubmitButton.disableProperty().bind(publicationDateText.textProperty().isEmpty());
        booksSubmitButton.disableProperty().bind(isbnText.textProperty().isEmpty());

        //Add elements and show form
        vbox.getChildren().add(gp);
        VBox.setVgrow(gp, Priority.ALWAYS);
        Scene scene = new Scene(vbox);

        this.setScene(scene);
        this.setMinWidth(509);
        this.setMinHeight(291);
        this.setAlwaysOnTop(true);
        this.initModality(Modality.APPLICATION_MODAL);
        this.show();
    }

    public boolean isUpdateForm() {
        return updateForm;
    }
}
