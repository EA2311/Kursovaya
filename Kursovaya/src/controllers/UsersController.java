package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Users;
import sample.DatabaseHandler;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsersController {

    @FXML
    private AnchorPane prodPage;

    @FXML
    private Pane prodPane;

    @FXML
    private Button menuBtn;

    @FXML
    private Button helpBtn;

    @FXML
    private Button deleteBtn;

    private Users delUser = new Users();

    @FXML
    private void initialize() throws SQLException {
        getSalary();
        showInfo();
        menuBtn.setOnAction(event -> {
            Stage stage = (Stage) menuBtn.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/menu.fxml"));
            Parent root1 = null;
            try {
                root1 = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("SalesManager");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().add(new Image(MenuController.class.getResourceAsStream("/resources/journal.png")));
            stage.show();
        });
        helpBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Помощь");
            alert.setHeaderText(null);
            alert.setContentText("Данная страница доступна толькьо для администраторов. Она предназначена для просмотра личных данных о сотрудниках. Для удаления учетной записи сотрудника из базы необходимо выбрать ее в таблице, после чего нажать кнопку \"Удалить\".");
            alert.showAndWait();
        });
        deleteBtn.setOnAction(event -> {
            if(delUser.getUsername() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText(null);
                alert.setContentText("Сначала выберите пользователя!");
                alert.showAndWait();
            }
            else {
                try {
                    delete();
                    showInfo();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void showInfo(){
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet result = dbHandler.getUserInfo();
        ObservableList<Users> users = FXCollections.observableArrayList();

        try {
            while (result.next()) {
                Users user = new Users();

                user.setUserId(result.getInt(1));
                user.setUsername(result.getString(2));
                user.setPassword(result.getString(3));
                user.setTelephone(result.getString(4));
                user.setAdres(result.getString(5));
                user.setName(result.getString(6));
                user.setSurname(result.getString(7));
                user.setSalary(result.getString(9));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TableView<Users> tableView = new TableView<Users>(users);
        tableView.setPrefWidth(700);
        tableView.setPrefHeight(600);
        tableView.setLayoutX(0);
        tableView.setLayoutY(0);
        tableView.setStyle("-fx-selection-bar: #9ACD50; -fx-selection-bar-non-focused: #9ACD70;");

        TableColumn<Users, String> usernameCol = new TableColumn<Users, String>("Логин");
        usernameCol.setCellValueFactory(new PropertyValueFactory<Users, String>("username"));
        tableView.getColumns().add(usernameCol);


        TableColumn<Users, String> passwordCol = new TableColumn<Users, String>("Пароль");
        passwordCol.setCellValueFactory(new PropertyValueFactory<Users, String>("password"));
        tableView.getColumns().add(passwordCol);

        TableColumn<Users, String> telephoneCol = new TableColumn<Users, String>("Номер телефона");
        telephoneCol.setCellValueFactory(new PropertyValueFactory<Users, String>("telephone"));
        tableView.getColumns().add(telephoneCol);

        TableColumn<Users, String> adresCol = new TableColumn<Users, String>("Адресс");
        adresCol.setCellValueFactory(new PropertyValueFactory<Users, String>("adres"));
        tableView.getColumns().add(adresCol);

        TableColumn<Users, String> nameCol = new TableColumn<Users, String>("Имя");
        nameCol.setCellValueFactory(new PropertyValueFactory<Users, String>("name"));
        tableView.getColumns().add(nameCol);

        TableColumn<Users, String> surnameCol = new TableColumn<Users, String>("Фамилия");
        surnameCol.setCellValueFactory(new PropertyValueFactory<Users, String>("surname"));
        tableView.getColumns().add(surnameCol);

        TableColumn<Users, String> salaryCol = new TableColumn<Users, String>("% от продаж за текущий месяц");
        salaryCol.setCellValueFactory(new PropertyValueFactory<Users, String>("salary"));
        tableView.getColumns().add(salaryCol);

        TableView.TableViewSelectionModel<Users> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                delUser = newValue;
            }
        });
        prodPane.getChildren().add(tableView);
    }

    public void delete() throws SQLException, ClassNotFoundException {
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Подтвердите ваше действие");
        alert1.setHeaderText(null);
        alert1.setContentText("Вы уверены, что хотите удалить учетную запись данного пользователя из базы?");

        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == ButtonType.OK){
            DatabaseHandler handler = new DatabaseHandler();
            handler.deleteUser(delUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Удаление пользователя");
            alert.setHeaderText(null);
            alert.setContentText("Учетная запись пользователя успешно удалена!");
            alert.showAndWait();
        }

    }

    public void getSalary() throws SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resultSet = dbHandler.forSalary();
        while (resultSet.next()){
            Users user = new Users();
            user.setSalary(resultSet.getString(1));
            user.setUsername(resultSet.getString(2));
            dbHandler.insertSalary(user);
        }
    }
}
