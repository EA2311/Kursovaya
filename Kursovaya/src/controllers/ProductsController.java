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
import models.Products;
import sample.DatabaseHandler;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductsController {

    @FXML
    private AnchorPane prodPage;

    @FXML
    private Pane prodPane;

    @FXML
    private Button sellBtn;

    @FXML
    private Button menuBtn;

    @FXML
    private Button helpBtn;

    private Products sellProduct = new Products();


    public void initialize(){
        getProducts();
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

        sellBtn.setOnAction(event -> {
            if(sellProduct.getCode() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText(null);
                alert.setContentText("Ошибка! Сначала выберите товар для продажи!");
                alert.showAndWait();
            }
            else sellProducts();
        });

        helpBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Помощь");
            alert.setHeaderText(null);
            alert.setContentText("Данная страница предназначена для просмотра списка товаров, которые на данный момет есть в наличии. При продаже товара необходимо выбрать его в таблице, после чего нажать кнопку \"Продать\".");
            alert.showAndWait();

        });
    }

    private void getProducts() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet result = dbHandler.getProduct("products", 0);
        ObservableList<Products> prod = FXCollections.observableArrayList();

        try {
            while (result.next()) {
                Products products = new Products();

                products.setCode(result.getString(1));
                products.setName(result.getString(2));
                products.setPrice(result.getInt(3));
                products.setSize(result.getString(4));
                products.setDeskr(result.getString(5));
                products.setAmount(result.getInt(6));

                prod.add(products);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TableView<Products> tableView = new TableView<Products>(prod);
        tableView.setPrefWidth(700);
        tableView.setPrefHeight(600);
        tableView.setLayoutX(0);
        tableView.setLayoutY(0);
        tableView.setStyle("-fx-selection-bar: #9ACD50; -fx-selection-bar-non-focused: #9ACD70;");

        TableColumn<Products, String> codeCol = new TableColumn<Products, String>("Артикул");
        codeCol.setCellValueFactory(new PropertyValueFactory<Products, String>("code"));
        tableView.getColumns().add(codeCol);

        TableColumn<Products, String> nameCol = new TableColumn<Products, String>("Наименование");
        nameCol.setCellValueFactory(new PropertyValueFactory<Products, String>("name"));
        tableView.getColumns().add(nameCol);

        TableColumn<Products, String> priceCol = new TableColumn<Products, String>("Цена");
        priceCol.setCellValueFactory(new PropertyValueFactory<Products, String>("price"));
        tableView.getColumns().add(priceCol);

        TableColumn<Products, String> sizeCol = new TableColumn<Products, String>("Размер");
        sizeCol.setCellValueFactory(new PropertyValueFactory<Products, String>("size"));
        tableView.getColumns().add(sizeCol);

        TableColumn<Products, String> amountCol = new TableColumn<Products, String>("Кол-во");
        amountCol.setCellValueFactory(new PropertyValueFactory<Products, String>("amount"));
        tableView.getColumns().add(amountCol);

        TableColumn<Products, String> deskrCol = new TableColumn<Products, String>("Описание");
        deskrCol.setCellValueFactory(new PropertyValueFactory<Products, String>("deskr"));
        tableView.getColumns().add(deskrCol);

        tableView.resizeColumn(nameCol, 120);
        tableView.resizeColumn(deskrCol, 320);

        TableView.TableViewSelectionModel<Products> selectionModel = tableView.getSelectionModel();

        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null){
                        sellProduct = newValue;
                    }
                });

        prodPane.getChildren().add(tableView);
    }

    private void sellProducts(){
        DatabaseHandler handler = new DatabaseHandler();
        try {
            handler.deleteProduct(sellProduct);
            handler.insertSoldProduct(sellProduct);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Продажа");
        alert.setHeaderText(null);
        alert.setContentText("Товар успешно продан!");

        alert.showAndWait();
        getProducts();

    }
}
