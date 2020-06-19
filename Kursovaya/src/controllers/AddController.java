package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Products;
import sample.DatabaseHandler;
import java.io.IOException;

public class AddController {

    @FXML
    private Button addBtn;

    @FXML
    private TextField codeField;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField sizeField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField descrField;
    @FXML
    private Button helpBtn;

    public void initialize(){
        cancelBtn.setOnAction(event -> {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
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

        addBtn.setOnAction(event -> {
            if(codeField.getText().trim().equals("")||nameField.getText().trim().equals("")||
                    priceField.getText().trim().equals("")|| amountField.getText().trim().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText(null);
                alert.setContentText("Сначала заполните все необходимые поля!");
                alert.showAndWait();
            }
            else {
                addProduct();
            }
        });
        helpBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Помощь");
            alert.setHeaderText(null);
            alert.setContentText("Данная страница предназначена для добавления новопоступившего товара в базу данных. Для этого необходимо заполнить соответствующие поля, после чего нажать кнопку \"Добавить\".");
            alert.showAndWait();
        });
    }

    private void addProduct(){
        try {
            Products product = new Products();
            product.setCode(codeField.getText());
            product.setName(nameField.getText());
            product.setPrice(Integer.parseInt(priceField.getText()));
            product.setSize(sizeField.getText());
            product.setDeskr(descrField.getText());
            product.setAmount(Integer.parseInt(amountField.getText()));

            DatabaseHandler dbHandler = new DatabaseHandler();
            dbHandler.insertNewProduct(product);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Новый товар");
            alert.setHeaderText(null);
            alert.setContentText("Товар успешно добавлен в базу!");

            alert.showAndWait();

            codeField.clear();
            nameField.clear();
            priceField.clear();
            sizeField.clear();
            descrField.clear();
            amountField.clear();
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Неверный формат данных для поля 'Кол-во' или 'Цена'! Пожалуйста, вводите только цифры для этих полей!");
            alert.showAndWait();
        }
    }
}
