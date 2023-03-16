package com.echograd.librarymanagement;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class BorrowerForm extends Stage {
    boolean updateForm;
    public BorrowerForm(LibraryViewModel viewModel, boolean isUpdate){

        this.updateForm = isUpdate;
        VBox vbox = new VBox();

        Label labelName = new Label("Name: ");
        Label labelEmail = new Label("Email: ");
        Label labelPhone = new Label("Phone: ");
        Label labelNewBorrower = new Label("Add new borrower");

        TextField nameText = new TextField("");
        TextField emailText = new TextField("");
        TextField phoneText = new TextField("");

        nameText.textProperty().bindBidirectional(viewModel.borrowersFormNameTextProperty());
        emailText.textProperty().bindBidirectional(viewModel.borrowersFormEmailTextProperty());
        phoneText.textProperty().bindBidirectional(viewModel.borrowersFormPhoneTextProperty());

        Button borrowersSubmitButton = new Button("Submit");
        Button borrowersCloseButton = new Button("Close");

        GridPane gp = new GridPane();

        gp.setPadding( new Insets(10) );
        gp.setHgap( 4 );
        gp.setVgap( 8 );
        ColumnConstraints col0 = new ColumnConstraints();
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(70);

        gp.getColumnConstraints().addAll(col0, col1);

        gp.add(labelNewBorrower, 1, 1);
        gp.add(labelName, 0, 2);           gp.add(nameText, 1, 2);
        gp.add(labelEmail, 0, 3);         gp.add(emailText, 1, 3);
        gp.add(labelPhone, 0, 4);       gp.add(phoneText, 1, 4);
        gp.add(borrowersSubmitButton, 0, 5);    gp.add(borrowersCloseButton, 1, 5);

        borrowersSubmitButton.setOnAction(event -> {
            try {
                viewModel.handleSubmitBorrowersButtonClick(event,this);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        borrowersCloseButton.setOnAction( event -> {
            this.close();
        });

        if(updateForm == true){
            ObservableList<Borrower> borrowers = LibraryView.getBorrowersTableView().getSelectionModel().getSelectedItems();
            Borrower borrower = borrowers.get(0);
            viewModel.setBorrowersFormNameText(borrower.getName());
            viewModel.setBorrowersFormEmailText(borrower.getEmail());
            viewModel.setBorrowersFormPhoneText(borrower.getPhone());

        } else {
            viewModel.setBorrowersFormNameText("");
            viewModel.setBorrowersFormEmailText("");
            viewModel.setBorrowersFormPhoneText("");
        }

        borrowersSubmitButton.disableProperty().bind(nameText.textProperty().isEmpty());
        borrowersSubmitButton.disableProperty().bind(emailText.textProperty().isEmpty());
        borrowersSubmitButton.disableProperty().bind(phoneText.textProperty().isEmpty());

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
}
