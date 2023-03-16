package com.echograd.librarymanagement;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class BorrowingForm extends VBox {

     private TabPane tabPane;
     private Tab tab;
     private LibraryViewModel viewModel;
     private static TableView borrowersTableView;
     private static TableView booksTableView;
     public BorrowingForm(LibraryViewModel viewModel, TabPane tabPane){

          this.viewModel = viewModel;
          this.tabPane = tabPane;
          borrowersTableView = new TableView<>();

          Label titleLabel = new Label("You are about to borrow the following book: ");
          Label toLabel = new Label("To ");

          TextField borrowersSearchTextField = new TextField();
          ChoiceBox borrowersSearchSelectionBox = new ChoiceBox();

          Button submitButton = new Button("Submit");
          Button closeButton = new Button("Close");
          Button searchButton = new Button("Search");

          booksTableView = new TableView<>();
          booksTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
          borrowersTableView = new TableView<>();
          borrowersTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

          TableColumn<Borrower, Integer> borrowersIdColumn = new TableColumn<>("ID");
          TableColumn<Borrower, String> borrowersNameColumn = new TableColumn<>("Name");
          TableColumn<Borrower, String> borrowersEmailColumn = new TableColumn<>("Email");
          TableColumn<Borrower, String> borrowersPhoneColumn = new TableColumn<>("Phone");

          TableColumn<Book, Integer> booksIdColumn = new TableColumn<>("ID");
          TableColumn<Book, String> booksTitleColumn = new TableColumn<>("Title");
          TableColumn<Book, String> booksAuthorsColumn = new TableColumn<>("Authors");
          TableColumn<Book, String> booksPublisherColumn = new TableColumn<>("Publisher");
          TableColumn<Book, String> booksIsbnColumn = new TableColumn<>("ISBN");
          TableColumn<Book, Date> booksDateColumn = new TableColumn<>("Publication date");
          TableColumn<Book, Boolean> booksAvailableColumn = new TableColumn<>("Available");

          booksIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
          booksTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
          booksAuthorsColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
          booksPublisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
          booksIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
          booksDateColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
          booksAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

          borrowersIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
          borrowersNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
          borrowersEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
          borrowersPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

          booksTableView.getColumns().addAll(booksIdColumn,booksTitleColumn,booksAuthorsColumn,booksPublisherColumn,booksIsbnColumn,booksDateColumn,booksAvailableColumn);
          borrowersTableView.getColumns().addAll(borrowersIdColumn, borrowersNameColumn, borrowersEmailColumn, borrowersPhoneColumn);

          ObservableList<Book> books = LibraryView.getBooksTableView().getSelectionModel().getSelectedItems();

          booksTableView.setPrefHeight(58);
          borrowersTableView.setPrefHeight(58);

          borrowersSearchSelectionBox.getItems().addAll("ID", "Name", "Email", "Phone", "All");
          viewModel.setBorrowingFormBorrowersChoiceBoxSelectionChoice("All");
          borrowersSearchSelectionBox.valueProperty().bindBidirectional(viewModel.borrowingFormBorrowersChoiceBoxSelectionChoiceProperty());

          BooleanBinding disableBorrowersSearchTextField = Bindings.createBooleanBinding(() -> {
               String selectedValue = null;
               if(borrowersSearchSelectionBox.getValue() != null) {
                    selectedValue = borrowersSearchSelectionBox.getValue().toString();
                    System.out.println(selectedValue);
               } return selectedValue == "All";
          }, borrowersSearchSelectionBox.valueProperty());

          borrowersSearchTextField.disableProperty().bind(disableBorrowersSearchTextField);
          borrowersSearchTextField.textProperty().bindBidirectional(viewModel.borrowingFormBorrowersSearchTextProperty());

          submitButton.disableProperty().bind(
                  borrowersTableView.getSelectionModel().selectedItemProperty().isNull()
          );

          submitButton.setOnAction(event -> {
               try {
                    viewModel.handleBorrowingFormSubmitButtonClick(event);
               } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
               }
               tabPane.getTabs().remove(tab);
               tabPane.getSelectionModel().select(0);
          });

          closeButton.setOnAction(event -> {
               tabPane.getTabs().remove(tab);
               tabPane.getSelectionModel().select(0);
          });

          searchButton.setOnAction(event -> {
               try {
                    viewModel.handleSearchBorrowersBorrowingFormButtonClick(event);
               } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
               }
          });

          booksTableView.setItems(books);
          borrowersTableView.setItems(viewModel.getBorrowingFormData());

          this.setSpacing(10);
          this.setPadding(new Insets(5,10,5,5));

          HBox borrowersSearchHbox = new HBox(borrowersSearchSelectionBox, borrowersSearchTextField, searchButton);
          HBox bottomButtonBarHbox = new HBox(submitButton,closeButton);
          bottomButtonBarHbox.setSpacing(10);
          this.getChildren().addAll(titleLabel, booksTableView, toLabel, borrowersSearchHbox, borrowersTableView, bottomButtonBarHbox);
     }

     public void setTab(Tab tab) {
          this.tab = tab;
     }
     public static TableView getBorrowersTableView() {
          return borrowersTableView;
     }

     public static TableView getBooksTableView() {
          return booksTableView;
     }
}
