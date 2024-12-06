package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.model.UserDTO;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.text.Text;

import java.util.List;


import javafx.scene.paint.Color;



public class AdminView {
    private TableView<UserDTO> userTableView;
    private ObservableList<UserDTO> usersObservableList;

    private TextField usernameTextField;
    private PasswordField passwordField;

    private Label usernameLabel;
    private Label passwordLabel;

    private Button saveButton;
    private Button deleteButton;
    private Button generateReportButton;
    private Button viewBooksButton;
    private Button upgradeButton;

    private Text actiontarget;


    public AdminView(Stage primaryStage, List<UserDTO> userDTOS) {
        primaryStage.setTitle("Admin Panel");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 1000, 800);
        primaryStage.setScene(scene);

        usersObservableList = FXCollections.observableArrayList(userDTOS);
        initTableView(gridPane);

        initSaveOptions(gridPane);

        primaryStage.show();
    }

    private void initTableView(GridPane gridPane) {
        userTableView = new TableView<>();
        userTableView.setPlaceholder(new Label("No rows to display"));

        // Username
        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Rol
        TableColumn<UserDTO, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // adaugam in tableview
        userTableView.getColumns().addAll(usernameColumn, roleColumn);
        userTableView.setItems(usersObservableList);

        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // se adauga TableView la grid
        gridPane.add(userTableView, 0, 0, 5, 1);


    }

    private void initSaveOptions(GridPane gridPane) {
        // username
        usernameLabel = new Label("Username");
        gridPane.add(usernameLabel, 1, 1);

        usernameTextField = new TextField();
        gridPane.add(usernameTextField, 2, 1);

        // rol
        passwordLabel = new Label("Password");
        gridPane.add(passwordLabel, 3, 1);

        passwordField = new PasswordField();
        gridPane.add(passwordField, 4, 1);

        saveButton = new Button("Save");
        gridPane.add(saveButton, 5, 1);

        deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 6, 1);

        viewBooksButton = new Button("View Books");
        gridPane.add(viewBooksButton, 7, 1);

        generateReportButton = new Button("Generate Report");
        gridPane.add(generateReportButton, 8, 1);

        upgradeButton = new Button("Upgrade");
        gridPane.add(upgradeButton, 9, 1);

    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener) {
        saveButton.setOnAction(saveButtonListener);
    }
    public void addViewBooksButtonListener(EventHandler<ActionEvent> viewBooksButtonListener){
        viewBooksButton.setOnAction(viewBooksButtonListener);
    }
    public void setActionTargetText(String text){ this.actiontarget.setText(text);}


    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addGenerateReportListener(EventHandler<ActionEvent> addButtonListener) {
        generateReportButton.setOnAction(addButtonListener);
    }

    public void addUpgradeButtonListener(EventHandler<ActionEvent> upgradeButtonListener) {
        upgradeButton.setOnAction(upgradeButtonListener);
    }


    public void addSelectionTableListener(ChangeListener selectionTableListener) {
        userTableView.getSelectionModel().selectedItemProperty().addListener(selectionTableListener);
    }

    public void displayAlertMessage(String titleInformation, String headerInformation, String contextInformation) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleInformation);
        alert.setHeaderText(headerInformation);
        alert.setContentText(contextInformation);

        alert.showAndWait();
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public ObservableList<UserDTO> getUsersObservableList() {
        return usersObservableList;
    }

    public void addUserToObservableList(UserDTO userDTO) {
        this.usersObservableList.add(userDTO);
    }

    public void removeUserFromObservableList(UserDTO userDTO) {
        this.usersObservableList.remove(userDTO);
    }

    public TableView getUserTableView() {
        return userTableView;
    }

    public TextField getUsernameTextField() {
        return usernameTextField;
    }

}
