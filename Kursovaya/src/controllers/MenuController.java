package controllers;

import animations.Shake;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;

public class MenuController {

    @FXML
    private Button productsBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button soldoutBtn;
    @FXML
    private Button usersBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Button statBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button lockImg;

    public void initialize() {


        productsBtn.setOnAction(event -> {
            newScene("../view/products.fxml","SalesManager");
        });
        LogController controller = new LogController();
        addBtn.setOnAction(event -> {
            newScene("../view/add.fxml","SalesManager");

        });
        soldoutBtn.setOnAction(event -> {
            newScene("../view/soldout.fxml","SalesManager");
        });
        usersBtn.setOnAction(event -> {
            if (LogController.check)
                newScene("../view/users.fxml", "SalesManager");
            else {
                lockImg.setTooltip(new Tooltip("Только администраторы имеют \nдоступ к данной функции."));
                Class<?> clazz = MenuController.class;
                InputStream input = clazz.getResourceAsStream("/resources/lock.png");
                Image image = new Image(input);
                ImageView umg = new ImageView(image);
                umg.setFitWidth(55);
                umg.setFitHeight(55);
                lockImg.setGraphic(umg);
                Shake lockAnim = new Shake(lockImg);
                lockAnim.Anim();
            }
        });
        statBtn.setOnAction(event -> {
            newScene("../view/stat.fxml","SalesManager");
        });
        logoutBtn.setOnAction(event -> {
            Stage stage = (Stage) productsBtn.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/logIn.fxml"));
            Parent root1 = null;
            try {
                root1 = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("SalesManager");
            stage.setWidth(640);
            stage.setHeight(510);
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().add(new Image(MenuController.class.getResourceAsStream("/resources/journal.png")));
            stage.show();
        });
        exitBtn.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void newScene(String window, String title){

        Stage stage = (Stage) productsBtn.getScene().getWindow();
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
        stage.setWidth(900);
        stage.setHeight(630);
        stage.setScene(new Scene(root1));
        stage.setResizable(false);
        stage.getIcons().add(new Image(MenuController.class.getResourceAsStream("/resources/journal.png")));
        stage.show();

    }
}

