package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.model.BookDTO;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import java.awt.*;
import java.util.List;

public class CustomerView {
    private TableView<BookDTO> bookTableView;
    private ObservableList<BookDTO> booksObservableList;

    public CustomerView(Stage primaryStage, List<BookDTO> bookDTOS) {
        primaryStage.setTitle("Library - Customer View");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);


        booksObservableList = FXCollections.observableArrayList(bookDTOS);
        initTableView(gridPane);

        primaryStage.show();
    }

    private void initTableView(GridPane gridPane) {
        bookTableView = new TableView<>();
        bookTableView.setPlaceholder(new Label("No books to display"));


        TableColumn<BookDTO, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));


        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));


        TableColumn<BookDTO, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        TableColumn<BookDTO, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));


        bookTableView.getColumns().addAll(titleColumn, authorColumn, priceColumn, stockColumn);
        bookTableView.setItems(booksObservableList);


        gridPane.add(bookTableView, 0, 0, 5, 1);
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    public void addSelectionTableListener(ChangeListener selectionTableListener) {
        bookTableView.getSelectionModel().selectedItemProperty().addListener(selectionTableListener);
    }

    public void displayAlertMessage(String titleInformation, String headerInformation, String contextInformation) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleInformation);
        alert.setHeaderText(headerInformation);
        alert.setContentText(contextInformation);

        alert.showAndWait();
    }

    public ObservableList<BookDTO> getBooksObservableList() {
        return booksObservableList;
    }

    public void addBookToObservableList(BookDTO bookDTO) {
        this.booksObservableList.add(bookDTO);
    }

    public void removeBookFromObservableList(BookDTO bookDTO) {
        this.booksObservableList.remove(bookDTO);
    }

    public TableView<BookDTO> getBookTableView() {
        return bookTableView;
    }
}
