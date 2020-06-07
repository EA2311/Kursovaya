package controllers;

import animations.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Users;
import sample.DatabaseHandler;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogController {

    @FXML
    private Pane logPage;

    @FXML
    private Label loginLbl;

    @FXML
    private Label pasLbl;

    @FXML
    private TextField logField;

    @FXML
    private PasswordField pasField;

    @FXML
    private Button enterBtn;

    @FXML
    private Button newBtn;
    @FXML
    private CheckBox adminCheckBox;

    public static String seller;
    public static boolean check = false;

    @FXML
    void initialize() {

        //Кнопка входа в аккаунт
        enterBtn.setOnMouseClicked(event -> {
            String loginText = logField.getText().trim();
            String passwordText = pasField.getText().trim();
            seller = loginText;

            if(!loginText.equals("") && !passwordText.equals("")) {

                logInUser(loginText, passwordText, check);
            }
            else {
                loginLbl.setTextFill(Color.RED);
                pasLbl.setTextFill(Color.RED);
            }
        });
        adminCheckBox.setOnAction(event -> {
            if(check == false)
                check = true;
            else check = false;
        });

        newBtn.setOnAction(event -> {
            newScene("/view/registration.fxml", "SalesManager");
        });

    }


    private void logInUser(String loginText, String passwordText, boolean check) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Users user = new Users();
        user.setUsername(loginText);
        user.setPassword(passwordText);
        ResultSet result = dbHandler.getUser(user, check);

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
            //if(loginText.equals(user.getUsername())&&passwordText.equals(user.getPassword()))
            newScene("../view/menu.fxml", "SalesManager");
        }
        else {
         Shake loginAnim = new Shake(logField);
         Shake passwordAnim = new Shake(pasField);
         loginAnim.Anim();
         passwordAnim.Anim();

        }

    }

    public void newScene(String window, String title){

        Stage stage = (Stage) enterBtn.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(window));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root1));
        stage.setResizable(false);
        stage.getIcons().add(new Image(MenuController.class.getResourceAsStream("/resources/journal.png")));
        stage.show();
    }
}
