package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import java.io.IOException;
import java.sql.*;


public class StatController {

    @FXML
    private AnchorPane prodPage;

    @FXML
    private Pane prodPane;

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private Button helpBtn;

    @FXML
    private Button menuBtn;

    @FXML
    private Label amountLbl;


    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        showStat();
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
            stage.setTitle("Меню");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().add(new Image(MenuController.class.getResourceAsStream("/resources/journal.png")));
            stage.show();
        });
        helpBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Помощь");
            alert.setHeaderText(null);
            alert.setContentText("Данная страница предназначена для просмотра статистики продаж по месяцам текущего года, а также для просмотра кол-ва единиц товаров в наличии.");
            alert.showAndWait();

        });
    }

    private void showStat() throws SQLException, ClassNotFoundException {
        DatabaseHandler handler = new DatabaseHandler();
        ResultSet result;
        String month = "";
        int sum = 0;
        result = handler.forStat();
        XYChart.Series set1 = new XYChart.Series<>();
        while (result.next()) {
            sum = result.getInt(1);
            int i = result.getInt(2);
            switch (i) {
                case 1:
                    month = "Январь";
                    break;
                case 2:
                    month = "Февраль";
                    break;
                case 3:
                    month = "Март";
                    break;
                case 4:
                    month = "Апрель";
                    break;
                case 5:
                    month = "Май";
                    break;
                case 6:
                    month = "Июнь";
                    break;
                case 7:
                    month = "Июль";
                    break;
                case 8:
                    month = "Август";
                    break;
                case 9:
                    month = "Сентябрь";
                    break;
                case 10:
                    month = "Октябрь";
                    break;
                case 11:
                    month = "Ноябрь";
                    break;
                case 12:
                    month = "Декабрь";
                    break;
                default:
                    System.out.println(month);
            }

            set1.getData().add(new XYChart.Data<>(month, sum));

        }
        barChart.getData().addAll(set1);
        result = handler.getStat();
        while (result.next())
        amountLbl.setText(String.valueOf(result.getInt(1)));
    }
}
