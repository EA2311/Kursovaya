package controllers;

import animations.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import models.Users;

public class RegController {
    @FXML
    private Pane regPage;

    @FXML
    private Button regBtn;

    @FXML
    private Label regLoginLbl;

    @FXML
    private PasswordField regPasField;

    @FXML
    private Label regPasLbl;

    @FXML
    private TextField regLogField;

    @FXML
    private Label nameLbl;

    @FXML
    private Label SurnLbl;

    @FXML
    private TextField regNamField;

    @FXML
    private TextField regSurnField;

    @FXML
    private Label telLbl;

    @FXML
    private TextField telRegField;

    @FXML
    private Label adrLbl;

    @FXML
    private TextField regAdrField;

    @FXML
    private RadioButton admRadioBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Pane admPane;

    @FXML
    private TextField regLogField1;

    @FXML
    private PasswordField regPasField1;

    @FXML
    private Label regLoginLbl1;

    @FXML
    private Button regBtn1;

    @FXML
    private Button regBtn11;
    @FXML
    private void initialize(){

        cancelBtn.setOnAction(event -> {
            cancelBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/logIn.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(MenuController.class.getResourceAsStream("/resources/journal.png")));
            stage.setTitle("SalesManager");
            stage.setResizable(false);
            stage.showAndWait();
        });

        regBtn.setOnAction(event -> {
            if(regSurnField.getText().trim().equals("")||regPasField.getText().trim().equals("")||
                    regAdrField.getText().trim().equals("")|| regNamField.getText().trim().equals("")
                    || telRegField.getText().trim().equals("")|| regLogField.getText().trim().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText(null);
                alert.setContentText("Сначала заполните все необходимые поля!");
                alert.showAndWait();
            }
            else {
                String loginText = regLogField.getText().trim();
                checkUser(loginText);
            }
        });
    }

    private void regNewUser() {
        DatabaseHandler dbHandler = new DatabaseHandler();

        String username = regLogField.getText();
        String password = regPasField.getText();
        String telephone = telRegField.getText();
        String adres = regAdrField.getText();
        String name = regNamField.getText();
        String surname = regSurnField.getText();
        String func;
        if(admRadioBtn.isSelected()){
            admPane.setVisible(true);
            regBtn1.setOnAction(event -> {
                DatabaseHandler dbHandler1 = new DatabaseHandler();
                Users user = new Users();
                user.setUsername(regLogField1.getText().trim());
                user.setPassword(regPasField1.getText().trim());
                ResultSet result = dbHandler1.getUser(user, true);

                int counter = 0;

                try {
                    while (result.next()) {
                        Users user1 = new Users();

                        counter++;
                        user1.setUserId(result.getInt(1));
                        user1.setUsername(result.getString(2));
                        user1.setPassword(result.getString(3));
                        user1.setAdres(result.getString(4));
                        user1.setTelephone(result.getString(5));
                        user1.setName(result.getString(6));
                        user1.setSurname(result.getString(7));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (counter >= 1) {
                    //func = "Админ";

                    Users user2 = new Users();
                    user2.setUsername(username);
                    user2.setPassword(password);
                    user2.setTelephone(telephone);
                    user2.setAdres(adres);
                    user2.setName(name);
                    user2.setSurname(surname);
                    user2.setFunc("Админ");

                    dbHandler1.registerUser(user2);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Поздравляем!");
                    alert.setHeaderText(null);
                    alert.setContentText("Регистрация прошла успешно!");
                    alert.showAndWait();

                    regBtn.getScene().getWindow().hide();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/logIn.fxml"));

                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.setTitle("SalesManager");
                    stage.setResizable(false);
                    stage.getIcons().add(new Image(MenuController.class.getResourceAsStream("/resources/journal.png")));
                    stage.showAndWait();
                }
                else {
                    Shake loginAnim = new Shake(regLogField1);
                    Shake passwordAnim = new Shake(regPasField1);
                    loginAnim.Anim();
                    passwordAnim.Anim();
                }
            });
            regBtn11.setOnAction(event -> {
                admPane.setVisible(false);
                admRadioBtn.setSelected(false);
            });

        }
        else {
            /*
            //func ="Пользователь";
            Users user = new Users();
            user.setUsername(username);
            user.setPassword(password);
            user.setTelephone(telephone);
            user.setAdres(adres);
            user.setName(name);
            user.setSurname(surname);
            user.setFunc("Пользователь");

            dbHandler.registerUser(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Поздравляем!");
            alert.setHeaderText(null);
            alert.setContentText("Регистрация прошла успешно!");
            alert.showAndWait();

            regBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/logIn.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("SalesManager");
            stage.setResizable(false);
            stage.getIcons().add(new Image(MenuController.class.getResourceAsStream("/resources/journal.png")));
            stage.showAndWait();

             */
            admPane.setVisible(true);
            regBtn1.setOnAction(event -> {
                DatabaseHandler dbHandler1 = new DatabaseHandler();
                Users user = new Users();
                user.setUsername(regLogField1.getText().trim());
                user.setPassword(regPasField1.getText().trim());
                ResultSet result = dbHandler1.getUser(user, true);

                int counter = 0;

                try {
                    while (result.next()) {
                        Users user1 = new Users();

                        counter++;
                        user1.setUserId(result.getInt(1));
                        user1.setUsername(result.getString(2));
                        user1.setPassword(result.getString(3));
                        user1.setAdres(result.getString(4));
                        user1.setTelephone(result.getString(5));
                        user1.setName(result.getString(6));
                        user1.setSurname(result.getString(7));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (counter >= 1) {
                    //func = "Админ";

                    Users user2 = new Users();
                    user2.setUsername(username);
                    user2.setPassword(password);
                    user2.setTelephone(telephone);
                    user2.setAdres(adres);
                    user2.setName(name);
                    user2.setSurname(surname);
                    user2.setFunc("Пользователь");

                    dbHandler1.registerUser(user2);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Поздравляем!");
                    alert.setHeaderText(null);
                    alert.setContentText("Регистрация прошла успешно!");
                    alert.showAndWait();

                    regBtn.getScene().getWindow().hide();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/logIn.fxml"));

                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.setTitle("SalesManager");
                    stage.setResizable(false);
                    stage.getIcons().add(new Image(MenuController.class.getResourceAsStream("/resources/journal.png")));
                    stage.showAndWait();
                }
                else {
                    Shake loginAnim = new Shake(regLogField1);
                    Shake passwordAnim = new Shake(regPasField1);
                    loginAnim.Anim();
                    passwordAnim.Anim();
                }
            });
            regBtn11.setOnAction(event -> {
                admPane.setVisible(false);
                admRadioBtn.setSelected(false);
            });

        }
    }

    private void checkUser(String loginText){
        DatabaseHandler dbHandler = new DatabaseHandler();
        Users user = new Users();
        user.setUsername(loginText);
        ResultSet result = dbHandler.checkUsername(user, admRadioBtn.isSelected());

        int counter = 0;

        try {
            while (result.next()) {
                Users user1 = new Users();

                counter++;
                user1.setUserId(result.getInt(1));
                user1.setUsername(result.getString(2));
                user1.setPassword(result.getString(3));
                user1.setAdres(result.getString(4));
                user1.setTelephone(result.getString(5));
                user1.setName(result.getString(6));
                user1.setSurname(result.getString(7));

                System.out.println(user1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (counter >= 1) {
            Shake loginAnim = new Shake(regLogField);
            loginAnim.Anim();
        }
        else {
            regNewUser();
        }
    }
}
