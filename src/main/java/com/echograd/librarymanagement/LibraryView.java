package com.echograd.librarymanagement;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class LibraryView extends VBox {

    private LibraryViewModel viewModel;
    private static TableView booksTableView;
    private static TableView borrowersTableView;
    private static TableView borrowingsTableView;
    private static TabPane tabPane = new TabPane();
    public LibraryView(){

        this.viewModel = new LibraryViewModel();
        borrowersTableView = new TableView<>();
        borrowersTableView.setMaxHeight(10000);
        borrowersTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        borrowersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        booksTableView = new TableView<>();
        booksTableView.setMaxHeight(10000);
        booksTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        booksTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        borrowingsTableView = new TableView<>();
        borrowingsTableView.setMaxHeight(10000);
        borrowingsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        borrowingsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Borrower, Integer> borrowersIdColumn = new TableColumn<>("ID");
        TableColumn<Borrower, String> borrowersNameColumn = new TableColumn<>("Name");
        TableColumn<Borrower, String> borrowersEmailColumn = new TableColumn<>("Email");
        TableColumn<Borrower, String> borrowersPhoneColumn = new TableColumn<>("Phone");

        TableColumn<Borrowing, Integer> borrowingIdColumn = new TableColumn<>("ID");
        TableColumn<Borrowing, Integer> borrowingBookIdColumn = new TableColumn<>("BookID");
        TableColumn<Borrowing, Integer> borrowingBorrowerIdColumn = new TableColumn<>("BorrowerID");
        TableColumn<Borrowing, Date> borrowingBorrowedDateColumn = new TableColumn<>("Borrowed date");
        TableColumn<Borrowing, Date> borrowingReturnDateColumn = new TableColumn<>("Return date");

        TableColumn<Book, Integer> booksIdColumn = new TableColumn<>("ID");
        TableColumn<Book, String> booksTitleColumn = new TableColumn<>("Title");
        TableColumn<Book, String> booksAuthorsColumn = new TableColumn<>("Authors");
        TableColumn<Book, String> booksPublisherColumn = new TableColumn<>("Publisher");
        TableColumn<Book, String> booksIsbnColumn = new TableColumn<>("ISBN");
        TableColumn<Book, Date> booksDateColumn = new TableColumn<>("Publication date");
        TableColumn<Book, Boolean> booksAvailableColumn = new TableColumn<>("Available");

        booksIdColumn.setPrefWidth(48);
        booksTitleColumn.setPrefWidth(325);
        booksAuthorsColumn.setPrefWidth(220);
        booksPublisherColumn.setPrefWidth(196);
        booksDateColumn.setPrefWidth(98);
        booksIsbnColumn.setPrefWidth(76);
        booksAvailableColumn.setPrefWidth(65);

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

        borrowingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        borrowingBookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        borrowingBorrowerIdColumn.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
        borrowingBorrowedDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        borrowingReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        borrowingIdColumn.setPrefWidth(48);
        borrowingBookIdColumn.setPrefWidth(48);
        borrowingBorrowerIdColumn.setPrefWidth(48);
        borrowingBorrowedDateColumn.setPrefWidth(98);
        borrowingReturnDateColumn.setPrefWidth(98);

        booksTableView.getColumns().addAll(booksIdColumn,booksTitleColumn,booksAuthorsColumn,booksPublisherColumn,booksIsbnColumn,booksDateColumn,booksAvailableColumn);
        borrowersTableView.getColumns().addAll(borrowersIdColumn, borrowersNameColumn, borrowersEmailColumn, borrowersPhoneColumn);
        borrowingsTableView.getColumns().addAll(borrowingIdColumn, borrowingBookIdColumn, borrowingBorrowerIdColumn, borrowingBorrowedDateColumn, borrowingReturnDateColumn);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        ChoiceBox booksSearchSelectionBox = new ChoiceBox();
        booksSearchSelectionBox.getItems().addAll("ID", "Title", "Authors", "Publisher", "All");
        viewModel.setBooksChoiceBoxSelectionChoice("All");

        ChoiceBox borrowersSearchSelectionBox = new ChoiceBox();
        borrowersSearchSelectionBox.getItems().addAll("ID", "Name", "Email", "Phone", "All");
        viewModel.setBorrowersChoiceBoxSelectionChoice("All");

        ChoiceBox borrowingsSearchSelectionBox = new ChoiceBox();
        borrowingsSearchSelectionBox.getItems().addAll("ID", "Book_ID", "Borrower_ID", "All");
        viewModel.setBorrowingChoiceBoxSelectionChoice("All");

        TextField booksSearchTextField = new TextField();
        TextField borrowersSearchTextField = new TextField();
        TextField borrowingsSearchTextField = new TextField();

        Button booksAddButton = new Button("Add");
        Button booksUpdateButton = new Button("Update");
        Button booksDeleteButton = new Button("Delete");
        Button booksSearchButton = new Button("Search");
        Button booksBorrowButton = new Button("Borrow");

        booksAddButton.setMinWidth(100);
        booksBorrowButton.setMinWidth(100);
        booksUpdateButton.setMinWidth(100);
        booksDeleteButton.setMinWidth(100);

        Button borrowersAddButton = new Button("Add");
        Button borrowersUpdateButton = new Button("Update");
        Button borrowersDeleteButton = new Button("Delete");
        Button borrowersSearchButton = new Button("Search");

        borrowersAddButton.setMinWidth(100);
        borrowersUpdateButton.setMinWidth(100);
        borrowersDeleteButton.setMinWidth(100);

        Button borrowingsReturnButton = new Button("Mark as returned");
        Button borrowingsSearchButton = new Button("Search");

        borrowingsReturnButton.setMinWidth(100);

        booksAddButton.setOnAction(event -> {
            BooksForm form = new BooksForm(viewModel, false);
        });

        borrowersAddButton.setOnAction(event -> {
            BorrowerForm form = new BorrowerForm(viewModel,false);
        });

        booksUpdateButton.setOnAction(event -> {BooksForm form = new BooksForm(viewModel, true);
        });

        borrowersUpdateButton.setOnAction(event -> {
            BorrowerForm form = new BorrowerForm(viewModel,true);
        });

        booksDeleteButton.setOnAction(event -> {
            try {
                viewModel.handleDeleteBooksButtonClick(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        booksBorrowButton.setOnAction(event -> {
            Book book = (Book)booksTableView.getSelectionModel().getSelectedItem();
            if(book.isAvailable()){
                BorrowingForm form = new BorrowingForm(viewModel, tabPane);
                Tab borrowTab = new Tab("New borrowing form", form);
                form.setTab(borrowTab);
                tabPane.getTabs().add(borrowTab);
                tabPane.getSelectionModel().select(borrowTab);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "This book isn't available!");
                a.showAndWait();
            }
        });

        borrowersDeleteButton.setOnAction(event -> {
            try {
                viewModel.handleDeleteBorrowersButtonClick(event);
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        });

        booksSearchButton.setOnAction(event -> {
            try {
                viewModel.handleSearchBooksButtonClick(event);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        booksSearchTextField.setOnAction(event -> {
            try {
                viewModel.handleSearchBooksButtonClick(event);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        borrowersSearchButton.setOnAction(event -> {
            try {
                viewModel.handleSearchBorrowersButtonClick(event);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        borrowersSearchTextField.setOnAction(event -> {
            try {
                viewModel.handleSearchBorrowersButtonClick(event);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        borrowingsSearchButton.setOnAction(event -> {
            try {
                viewModel.handleSearchBorrowingsButtonClick(event);
            } catch (SQLException | IOException e){
                throw new RuntimeException(e);
            }
        });

        borrowingsSearchTextField.setOnAction(event -> {
            try {
                viewModel.handleSearchBorrowingsButtonClick(event);
            } catch (SQLException | IOException e){
                throw new RuntimeException(e);
            }
        });

        borrowingsReturnButton.setOnAction(event -> {
            try {
                viewModel.handleBorrowingReturnButtonClick(event);
            } catch (SQLException | IOException e){
                throw new RuntimeException(e);
            }
        });

        booksUpdateButton.disableProperty().bind(
                booksTableView.getSelectionModel().selectedItemProperty().isNull()
        );

        booksDeleteButton.disableProperty().bind(
                booksTableView.getSelectionModel().selectedItemProperty().isNull()
        );

        booksBorrowButton.disableProperty().bind(
                booksTableView.getSelectionModel().selectedItemProperty().isNull()
        );

        borrowersUpdateButton.disableProperty().bind(
                borrowersTableView.getSelectionModel().selectedItemProperty().isNull()
        );

        borrowersDeleteButton.disableProperty().bind(
                borrowersTableView.getSelectionModel().selectedItemProperty().isNull()
        );

        borrowingsReturnButton.disableProperty().bind(
                borrowingsTableView.getSelectionModel().selectedItemProperty().isNull()
        );

        BooleanBinding disableBooksSearchTextField = Bindings.createBooleanBinding(() -> {
            String selectedValue = null;
            if(booksSearchSelectionBox.getValue() != null) {
                selectedValue = booksSearchSelectionBox.getValue().toString();
            } return selectedValue == "All";
                }, booksSearchSelectionBox.valueProperty());

        booksSearchTextField.disableProperty().bind(disableBooksSearchTextField);

        BooleanBinding disableBorrowersSearchTextField = Bindings.createBooleanBinding(() -> {
            String selectedValue = null;
            if(borrowersSearchSelectionBox.getValue() != null) {
                selectedValue = borrowersSearchSelectionBox.getValue().toString();
            } return selectedValue == "All";
        }, borrowersSearchSelectionBox.valueProperty());

        borrowersSearchTextField.disableProperty().bind(disableBorrowersSearchTextField);

        BooleanBinding disableBorrowingsSearchTextField = Bindings.createBooleanBinding(() -> {
            String selectedValue = null;
            if(borrowingsSearchSelectionBox.getValue() != null) {
                selectedValue = borrowingsSearchSelectionBox.getValue().toString();
            } return selectedValue == "All";
        }, borrowingsSearchSelectionBox.valueProperty());

        borrowingsSearchTextField.disableProperty().bind(disableBorrowingsSearchTextField);

        VBox booksButtonsVbox = new VBox(booksAddButton, booksUpdateButton, booksDeleteButton, booksBorrowButton);
        booksButtonsVbox.setSpacing(5);
        HBox booksHbox = new HBox(booksTableView, booksButtonsVbox);
        booksHbox.setSpacing(10);
        HBox searchBarHbox = new HBox(booksSearchSelectionBox, booksSearchTextField, booksSearchButton);
        VBox booksOuterVbox = new VBox(searchBarHbox, booksHbox);
        booksOuterVbox.setPadding(new Insets(5,10,5,5));

        VBox.setVgrow(booksTableView, Priority.ALWAYS);
        VBox.setVgrow(booksHbox, Priority.ALWAYS);
        HBox.setHgrow(booksTableView, Priority.ALWAYS);

        Tab booksTab = new Tab("Books", booksOuterVbox);

        tabPane.getTabs().add(booksTab);

        VBox borrowersButtonVbox = new VBox(borrowersAddButton,borrowersUpdateButton,borrowersDeleteButton);
        borrowersButtonVbox.setSpacing(5);
        HBox borrowersHbox = new HBox(borrowersTableView, borrowersButtonVbox);
        borrowersHbox.setSpacing(10);
        HBox borrowersSearchBarHbox = new HBox(borrowersSearchSelectionBox, borrowersSearchTextField, borrowersSearchButton);
        VBox borrowersOuterVbox = new VBox(borrowersSearchBarHbox, borrowersHbox);
        borrowersOuterVbox.setPadding(new Insets(5, 10, 5, 5));

        VBox.setVgrow(borrowersTableView, Priority.ALWAYS);
        VBox.setVgrow(borrowersHbox, Priority.ALWAYS);
        HBox.setHgrow(borrowersTableView, Priority.ALWAYS);

        Tab borrowersTab = new Tab("Borrowers", borrowersOuterVbox);

        tabPane.getTabs().add(borrowersTab);

        HBox borrowingsSearchBarHbox = new HBox(borrowingsSearchSelectionBox, borrowingsSearchTextField, borrowingsSearchButton);
        VBox borrowingsOuterVbox = new VBox(borrowingsSearchBarHbox, borrowingsTableView, borrowingsReturnButton);
        borrowingsReturnButton.setPadding(new Insets(10, 0, 0 , 0));
        borrowingsOuterVbox.setPadding(new Insets(5, 10, 5 ,5));

        VBox.setVgrow(borrowingsTableView, Priority.ALWAYS);

        Tab borrowingsTab = new Tab("Borrowings", borrowingsOuterVbox);

        tabPane.getTabs().add(borrowingsTab);

        VBox.setVgrow(tabPane, Priority.ALWAYS);

        booksTableView.setItems(viewModel.getBookData());
        booksSearchSelectionBox.valueProperty().bindBidirectional(viewModel.booksSelectionChoiceProperty());
        booksSearchTextField.textProperty().bindBidirectional(viewModel.booksSearchTextProperty());

        borrowersTableView.setItems(viewModel.getBorrowerData());
        borrowersSearchSelectionBox.valueProperty().bindBidirectional(viewModel.borrowersChoiceBoxSelectionChoiceProperty());
        borrowersSearchTextField.textProperty().bindBidirectional(viewModel.borrowersSearchTextProperty());

        borrowingsTableView.setItems(viewModel.getBorrowingData());
        borrowingsSearchSelectionBox.valueProperty().bindBidirectional(viewModel.borrowingChoiceBoxSelectionChoiceProperty());
        borrowingsSearchTextField.textProperty().bindBidirectional(viewModel.borrowingSearchTextProperty());

        this.getChildren().add(tabPane);

    }

    public static TableView getBooksTableView() {
        return booksTableView;
    }

    public static TableView getBorrowersTableView() {
        return borrowersTableView;
    }

    public static TableView getBorrowingsTableView() {
        return borrowingsTableView;
    }

    public static TabPane getTabPane() {
        return tabPane;
    }
}
