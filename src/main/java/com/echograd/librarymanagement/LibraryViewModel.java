package com.echograd.librarymanagement;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class LibraryViewModel {
    String SQL_QUERY_SEARCH_ALL = "select * from books";
    private ObservableList<Book> booksData = FXCollections.observableArrayList();
    private ObservableList<Borrower> borrowerData = FXCollections.observableArrayList();
    private ObservableList<Borrower> borrowingFormData = FXCollections.observableArrayList();
    private ObservableList<Borrowing> borrowingData = FXCollections.observableArrayList();
    private StringProperty booksChoiceBoxSelectionChoice = new SimpleStringProperty("");
    private StringProperty borrowersChoiceBoxSelectionChoice = new SimpleStringProperty("");
    private StringProperty borrowingFormBorrowersChoiceBoxSelectionChoice = new SimpleStringProperty("");
    private StringProperty borrowingChoiceBoxSelectionChoice = new SimpleStringProperty("");
    private StringProperty borrowingFormBorrowersSearchText = new SimpleStringProperty("");
    private StringProperty borrowingSearchText = new SimpleStringProperty("");
    private StringProperty booksSearchText = new SimpleStringProperty("");
    private StringProperty borrowersSearchText = new SimpleStringProperty("");
    private StringProperty booksFormTitleText = new SimpleStringProperty("");
    private StringProperty booksFormAuthorsText = new SimpleStringProperty("");
    private StringProperty booksFormIsbnText = new SimpleStringProperty("");
    private StringProperty booksFormPublisherText = new SimpleStringProperty("");
    private StringProperty booksFormPublicationDateText = new SimpleStringProperty("");
    private StringProperty borrowersFormNameText = new SimpleStringProperty("");
    private StringProperty borrowersFormPhoneText = new SimpleStringProperty("");
    private StringProperty borrowersFormEmailText = new SimpleStringProperty("");
    public void handleSubmitBooksButtonClick(ActionEvent event, BooksForm form) throws SQLException, IOException {
        System.out.println("Submit button pressed!");
        ObservableList<Book> books = LibraryView.getBooksTableView().getSelectionModel().getSelectedItems();
        Book book = books.get(0);
        String SQL_QUERY;
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        if(form.isUpdateForm() == false) {
            SQL_QUERY = "insert into books (title, authors, publisher, isbn, publication_date) values (?, ?, ?, ?, ?)";
            try {
                Connection con = databaseConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                pst.setString(1, getBooksFormTitleText());
                pst.setString(2, getBooksFormAuthorsText());
                pst.setString(3, getBooksFormPublisherText());
                pst.setString(4, getBooksFormISBNText());
                pst.setDate(5, getBooksFormPublicationDateText());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        } else {
            SQL_QUERY = "update books set title=?, authors=?, publisher=?, isbn=?, publication_date=? where id=?";
            try {
                Connection con = databaseConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                pst.setString(1, getBooksFormTitleText());
                pst.setString(2, getBooksFormAuthorsText());
                pst.setString(3, getBooksFormPublisherText());
                pst.setString(4, getBooksFormISBNText());
                pst.setDate(5, getBooksFormPublicationDateText());
                pst.setInt(6, book.getId());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        }
        Platform.runLater(() -> form.close());
    }

    public void handleSubmitBorrowersButtonClick(ActionEvent event, BorrowerForm form) throws SQLException, IOException {
        System.out.println("Submit button pressed!");
        ObservableList<Borrower> borrowers = LibraryView.getBorrowersTableView().getSelectionModel().getSelectedItems();
        Borrower borrower = borrowers.get(0);
        String SQL_QUERY;
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        if(form.updateForm == false) {
            SQL_QUERY = "insert into borrowers (name, email, phone) values (?, ?, ?)";
            try {
                Connection con = databaseConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                pst.setString(1, getBorrowersFormNameText());
                pst.setString(2, getBorrowersFormEmailText());
                pst.setString(3, getBorrowersFormPhoneText());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        } else {
            SQL_QUERY = "update borrowers set name=?, email=?, phone=? where id=?";
            try {
                Connection con = databaseConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                pst.setString(1, getBorrowersFormNameText());
                pst.setString(2, getBorrowersFormEmailText());
                pst.setString(3, getBorrowersFormPhoneText());
                pst.setInt(6, borrower.getId());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        }
        Platform.runLater(() -> form.close());
    }
    public void handleDeleteBooksButtonClick(ActionEvent event) throws SQLException {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this item?");
        a.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ObservableList<Book> books = LibraryView.getBooksTableView().getSelectionModel().getSelectedItems();
                String SQL_QUERY = "delete from books where id=?";
                DatabaseConnection databaseConnection = null;
                try {
                    databaseConnection = DatabaseConnection.getInstance();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Connection con = databaseConnection.getConnection();
                    for (Book book: books){
                        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                        pst.setInt(1, book.getId());
                        pst.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace(System.err);
                    System.err.println("SQLState: " +
                            e.getSQLState());
                    System.err.println("Error Code: " +
                            e.getErrorCode());
                    System.err.println("Message: " + e.getMessage());
                }
            }
        });
    }

    public void handleDeleteBorrowersButtonClick(ActionEvent event) throws SQLException {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this item?");
        a.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ObservableList<Borrower> borrowers = LibraryView.getBorrowersTableView().getSelectionModel().getSelectedItems();
                String SQL_QUERY = "delete from borrowers where id=?";
                DatabaseConnection databaseConnection = null;
                try {
                    databaseConnection = DatabaseConnection.getInstance();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Connection con = databaseConnection.getConnection();
                    for (Borrower borrower: borrowers){
                        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                        pst.setInt(1, borrower.getId());
                        pst.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace(System.err);
                    System.err.println("SQLState: " +
                            e.getSQLState());
                    System.err.println("Error Code: " +
                            e.getErrorCode());
                    System.err.println("Message: " + e.getMessage());
                }
                System.out.println(borrowers);
            }
        });
    }
    public void handleSearchBooksButtonClick(ActionEvent event) throws SQLException, IOException {
        ObservableList<Book> books = FXCollections.emptyObservableList();
        if (getBooksChoiceBoxSelectionChoice() == "All"){
            LibraryView.getBooksTableView().getItems().clear();
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            System.out.println("Status of database connection is " + databaseConnection);
            try (Connection con = databaseConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(SQL_QUERY_SEARCH_ALL);
                 ResultSet rs = pst.executeQuery();) {
                books = FXCollections.observableArrayList();
                Book book;
                while (rs.next()) {
                    book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthors(rs.getString("authors"));
                    book.setPublisher(rs.getString("publisher"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublicationDate(rs.getDate("publication_date"));
                    book.setAvailable(rs.getBoolean("available"));
                    books.add(book);
                }
            } catch (SQLException e){
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        } else {
            String SQL_QUERY_SEARCH_LIKE = "select * from books where " + getBooksChoiceBoxSelectionChoice() + " like " + "'%" + getBooksSearchText() + "%'";
            LibraryView.getBooksTableView().getItems().clear();
            System.out.println(SQL_QUERY_SEARCH_LIKE);
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            try (Connection con = databaseConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(SQL_QUERY_SEARCH_LIKE);
                 ResultSet rs = pst.executeQuery();) {
                books = FXCollections.observableArrayList();
                Book book;
                while (rs.next()) {
                    book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthors(rs.getString("authors"));
                    book.setPublisher(rs.getString("publisher"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublicationDate(rs.getDate("publication_date"));
                    book.setAvailable(rs.getBoolean("available"));
                    books.add(book);
                }
            }   catch (SQLException e){
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        }
        booksData.addAll(books);
    }

    public void handleSearchBorrowersButtonClick(ActionEvent event) throws SQLException, IOException {
        ObservableList<Borrower> borrowers = FXCollections.emptyObservableList();
        if (getBorrowersChoiceBoxSelectionChoice() == "All"){
            String SQL_QUERY = "select * from borrowers";
            LibraryView.getBorrowersTableView().getItems().clear();
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            try (Connection con = databaseConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                 ResultSet rs = pst.executeQuery();) {
                borrowers = FXCollections.observableArrayList();
                Borrower borrower;
                while (rs.next()) {
                    borrower = new Borrower();
                    borrower.setId(rs.getInt("id"));
                    borrower.setName(rs.getString("name"));
                    borrower.setEmail(rs.getString("email"));
                    borrower.setPhone(rs.getString("phone"));
                    borrowers.add(borrower);
                }
            } catch (SQLException e){
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        } else {
            String SQL_QUERY_SEARCH_LIKE = "select * from borrowers where " + getBorrowersChoiceBoxSelectionChoice() + " like " + "'%" + getBorrowersSearchText() + "%'";
            LibraryView.getBorrowersTableView().getItems().clear();
            System.out.println(SQL_QUERY_SEARCH_LIKE);
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            try (Connection con = databaseConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(SQL_QUERY_SEARCH_LIKE);
                 ResultSet rs = pst.executeQuery();) {
                borrowers = FXCollections.observableArrayList();
                Borrower borrower;
                while (rs.next()) {
                    borrower = new Borrower();
                    borrower.setId(rs.getInt("id"));
                    borrower.setName(rs.getString("name"));
                    borrower.setEmail(rs.getString("email"));
                    borrower.setPhone(rs.getString("phone"));
                    borrowers.add(borrower);
                }
            }   catch (SQLException e){
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        }
        borrowerData.addAll(borrowers);
    }

    public void handleSearchBorrowersBorrowingFormButtonClick(ActionEvent event) throws SQLException, IOException {
        ObservableList<Borrower> borrowers = FXCollections.emptyObservableList();
        if (getBorrowingFormBorrowersChoiceBoxSelectionChoice() == "All"){
            String SQL_QUERY = "select * from borrowers";
            BorrowingForm.getBorrowersTableView().getItems().clear();
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            try (Connection con = databaseConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                 ResultSet rs = pst.executeQuery();) {
                borrowers = FXCollections.observableArrayList();
                Borrower borrower;
                while (rs.next()) {
                    borrower = new Borrower();
                    borrower.setId(rs.getInt("id"));
                    borrower.setName(rs.getString("name"));
                    borrower.setEmail(rs.getString("email"));
                    borrower.setPhone(rs.getString("phone"));
                    borrowers.add(borrower);
                }
            } catch (SQLException e){
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        } else {
            String SQL_QUERY_SEARCH_LIKE = "select * from borrowers where " + getBorrowingFormBorrowersChoiceBoxSelectionChoice() + " like " + "'%" + getBorrowingFormBorrowersSearchText() + "%'";
            BorrowingForm.getBorrowersTableView().getItems().clear();
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            try (Connection con = databaseConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(SQL_QUERY_SEARCH_LIKE);
                 ResultSet rs = pst.executeQuery();) {
                borrowers = FXCollections.observableArrayList();
                Borrower borrower;
                while (rs.next()) {
                    borrower = new Borrower();
                    borrower.setId(rs.getInt("id"));
                    borrower.setName(rs.getString("name"));
                    borrower.setEmail(rs.getString("email"));
                    borrower.setPhone(rs.getString("phone"));
                    borrowers.add(borrower);
                }
            }   catch (SQLException e){
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        }
        borrowingFormData.addAll(borrowers);
        }
    public void handleSearchBorrowingsButtonClick(ActionEvent event) throws SQLException, IOException {
        ObservableList<Borrowing> borrowings = FXCollections.emptyObservableList();
        if (getBorrowingChoiceBoxSelectionChoice() == "All"){
            String SQL_QUERY = "select * from borrowings";
            LibraryView.getBorrowingsTableView().getItems().clear();
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            try (Connection con = databaseConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                 ResultSet rs = pst.executeQuery();) {
                borrowings = FXCollections.observableArrayList();
                Borrowing borrowing;
                while (rs.next()) {
                    borrowing = new Borrowing();
                    borrowing.setId(rs.getInt("id"));
                    borrowing.setBookID(rs.getInt("book_id"));
                    borrowing.setBorrowerID(rs.getInt("borrower_id"));
                    borrowing.setBorrowedDate(rs.getDate("borrowed_date"));
                    borrowing.setReturnDate(rs.getDate("return_date"));
                    borrowings.add(borrowing);
                }
            } catch (SQLException e){
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        } else {
            String SQL_QUERY_SEARCH_LIKE = "select * from borrowings where " + getBorrowingChoiceBoxSelectionChoice() + " like " + "'%" + getBorrowingSearchText() + "%'";
            LibraryView.getBorrowingsTableView().getItems().clear();
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            try (Connection con = databaseConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(SQL_QUERY_SEARCH_LIKE);
                 ResultSet rs = pst.executeQuery();) {
                borrowings = FXCollections.observableArrayList();
                Borrowing borrowing;
                while (rs.next()) {
                    borrowing = new Borrowing();
                    borrowing.setId(rs.getInt("id"));
                    borrowing.setBookID(rs.getInt("book_id"));
                    borrowing.setBorrowerID(rs.getInt("borrower_id"));
                    borrowing.setBorrowedDate(rs.getDate("borrowed_date"));
                    borrowing.setReturnDate(rs.getDate("return_date"));
                    borrowings.add(borrowing);
                }
            }   catch (SQLException e){
                e.printStackTrace(System.err);
                System.err.println("SQLState: " +
                        e.getSQLState());
                System.err.println("Error Code: " +
                        e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
            }
        }
        borrowingData.addAll(borrowings);
    }

    public void handleBorrowingReturnButtonClick(ActionEvent event) throws SQLException, IOException {
        Borrowing borrowing = (Borrowing)LibraryView.getBorrowingsTableView().getSelectionModel().getSelectedItem();
        String SQL_QUERY_BOOKS = "update books set available=true where id = " + borrowing.getBookID();
        String SQL_QUERY_BORROWINGS = "update borrowings set return_date=? where id = " + borrowing.getId();
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        try (Connection con = databaseConnection.getConnection();
            PreparedStatement pst1 = con.prepareStatement(SQL_QUERY_BOOKS);
            PreparedStatement pst2 = con.prepareStatement(SQL_QUERY_BORROWINGS);){
            pst1.executeUpdate();
            pst2.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            pst2.executeUpdate();
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Book successfully returned!");
            a.showAndWait();
        } catch (SQLException e){
            e.printStackTrace(System.err);
            System.err.println("SQLState: " +
                    e.getSQLState());
            System.err.println("Error Code: " +
                    e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
        }
    }

    public void handleBorrowingFormSubmitButtonClick(ActionEvent event) throws SQLException, IOException {
        Borrower borrower = (Borrower)BorrowingForm.getBorrowersTableView().getSelectionModel().getSelectedItem();
        int borrowerId = borrower.getId();
        ObservableList<Book> book = BorrowingForm.getBooksTableView().getItems();
        int bookId = book.get(0).getId();
        String SQL_QUERY_INSERT = "insert into borrowings (book_id, borrower_id, borrowed_date) values (?, ?, ?)";
        String SQL_QUERY_UPDATE = "update books set available = false where id = " + bookId;
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        try {
             Connection con = databaseConnection.getConnection();
             PreparedStatement pst1 = con.prepareStatement(SQL_QUERY_INSERT);
             PreparedStatement pst2 = con.prepareStatement(SQL_QUERY_UPDATE);
             pst1.setInt(1, bookId);
             pst1.setInt(2, borrowerId);
             pst1.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
             pst1.executeUpdate();
             pst2.executeUpdate();
             Alert a = new Alert(Alert.AlertType.INFORMATION, "Borrowing submitted successfully!");
             a.showAndWait();
        } catch (SQLException e){
            e.printStackTrace(System.err);
            System.err.println("SQLState: " +
                    e.getSQLState());
            System.err.println("Error Code: " +
                    e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
        }
    }
    public ObservableList<Book> getBookData(){
        return booksData;
    }

    public ObservableList<Borrower> getBorrowerData() {
        return borrowerData;
    }

    public ObservableList<Borrower> getBorrowingFormData() {
        return borrowingFormData;
    }

    public ObservableList<Borrowing> getBorrowingData() {
        return borrowingData;
    }

    public StringProperty booksSelectionChoiceProperty() {

        return booksChoiceBoxSelectionChoice;
    }

    public String getBooksChoiceBoxSelectionChoice(){
        return booksChoiceBoxSelectionChoice.get();
    }

    public void setBooksChoiceBoxSelectionChoice(String booksChoiceBoxSelectionChoice) {
        this.booksChoiceBoxSelectionChoice.set(booksChoiceBoxSelectionChoice);
    }

    public String getBorrowersChoiceBoxSelectionChoice() {
        return borrowersChoiceBoxSelectionChoice.get();
    }

    public StringProperty borrowersChoiceBoxSelectionChoiceProperty() {
        return borrowersChoiceBoxSelectionChoice;
    }

    public void setBorrowersChoiceBoxSelectionChoice(String borrowersChoiceBoxSelectionChoice) {
        this.borrowersChoiceBoxSelectionChoice.set(borrowersChoiceBoxSelectionChoice);
    }

    public String getBorrowingFormBorrowersChoiceBoxSelectionChoice() {
        return borrowingFormBorrowersChoiceBoxSelectionChoice.get();
    }

    public StringProperty borrowingFormBorrowersChoiceBoxSelectionChoiceProperty() {
        return borrowingFormBorrowersChoiceBoxSelectionChoice;
    }

    public void setBorrowingFormBorrowersChoiceBoxSelectionChoice(String borrowingFormBorrowersChoiceBoxSelectionChoice) {
        this.borrowingFormBorrowersChoiceBoxSelectionChoice.set(borrowingFormBorrowersChoiceBoxSelectionChoice);
    }

    public String getBorrowingChoiceBoxSelectionChoice() {
        return borrowingChoiceBoxSelectionChoice.get();
    }

    public StringProperty borrowingChoiceBoxSelectionChoiceProperty() {
        return borrowingChoiceBoxSelectionChoice;
    }

    public void setBorrowingChoiceBoxSelectionChoice(String borrowingChoiceBoxSelectionChoice) {
        this.borrowingChoiceBoxSelectionChoice.set(borrowingChoiceBoxSelectionChoice);
    }

    public StringProperty booksSearchTextProperty() {

        return booksSearchText;
    }
    public String getBooksSearchText()  {
        if (booksSearchText.get() == null)
            return "";
        return booksSearchText.get();
    }

    public String getBorrowersSearchText() {
        return borrowersSearchText.get();
    }

    public StringProperty borrowersSearchTextProperty() {
        return borrowersSearchText;
    }

    public String getBorrowingFormBorrowersSearchText() {
        return borrowingFormBorrowersSearchText.get();
    }

    public StringProperty borrowingFormBorrowersSearchTextProperty() {
        return borrowingFormBorrowersSearchText;
    }

    public String getBorrowingSearchText() {
        return borrowingSearchText.get();
    }

    public StringProperty borrowingSearchTextProperty() {
        return borrowingSearchText;
    }

    public String getBooksFormTitleText() {
        return booksFormTitleText.get();
    }

    public void setBooksFormTitleText(String booksFormTitleText) {
        this.booksFormTitleText.set(booksFormTitleText);
    }

    public String getBooksFormAuthorsText() {
        return booksFormAuthorsText.getValue();
    }

    public void setBooksFormAuthorsText(String booksFormAuthorsText) {
        this.booksFormAuthorsText.set(booksFormAuthorsText);
    }

    public String getBooksFormPublisherText() {
        return booksFormPublisherText.getValue();
    }

    public void setBooksFormPublisherText(String booksFormPublisherText) {
        this.booksFormPublisherText.set(booksFormPublisherText);
    }

    public String getBooksFormISBNText() {
        return booksFormIsbnText.getValue();
    }

    public void setBooksFormIsbnText(String booksFormIsbnText) {
        this.booksFormIsbnText.set(booksFormIsbnText);
    }

    public Date getBooksFormPublicationDateText() {
        Date date = Date.valueOf(booksFormPublicationDateText.getValue());
        return date;
    }

    public void setBooksFormPublicationDateText(String booksFormPublicationDateText) {
        this.booksFormPublicationDateText.set(booksFormPublicationDateText);
    }

    public StringProperty booksFormTitleTextProperty() {
        return booksFormTitleText;
    }
    public StringProperty booksFormAuthorsText() {
        return booksFormAuthorsText;
    }
    public StringProperty booksFormIsbnText() {
        return booksFormIsbnText;
    }
    public StringProperty booksFormPublisherText() {
        return booksFormPublisherText;
    }
    public StringProperty booksFormPublicationDateText() {
        return booksFormPublicationDateText;
    }

    public String getBorrowersFormNameText() {
        return borrowersFormNameText.get();
    }

    public StringProperty borrowersFormNameTextProperty() {
        return borrowersFormNameText;
    }

    public void setBorrowersFormNameText(String borrowersFormNameText) {
        this.borrowersFormNameText.set(borrowersFormNameText);
    }

    public String getBorrowersFormPhoneText() {
        return borrowersFormPhoneText.get();
    }

    public StringProperty borrowersFormPhoneTextProperty() {
        return borrowersFormPhoneText;
    }

    public void setBorrowersFormPhoneText(String borrowersFormPhoneText) {
        this.borrowersFormPhoneText.set(borrowersFormPhoneText);
    }

    public String getBorrowersFormEmailText() {
        return borrowersFormEmailText.get();
    }

    public StringProperty borrowersFormEmailTextProperty() {
        return borrowersFormEmailText;
    }

    public void setBorrowersFormEmailText(String borrowersFormEmailText) {
        this.borrowersFormEmailText.set(borrowersFormEmailText);
    }
}
