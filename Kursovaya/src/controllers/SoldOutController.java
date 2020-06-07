package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.SoldProducts;
import sample.DatabaseHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

public class SoldOutController {

    @FXML
    private AnchorPane prodPage;

    @FXML
    private Pane prodPane;

    @FXML
    private Button menuBtn;

    @FXML
    private Button pdfBtn;

    @FXML
    private TextField monthField;

    @FXML
    private Button showBtn;

    @FXML
    private Button helpBtn;

    private int monthNum = 0;
    private  TableView<SoldProducts> tableView;

    public void initialize() throws ParseException {
        getSoldProducts(0);

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

        showBtn.setOnAction(event -> {

            String month = monthField.getText().trim();
            if(month.equals(""))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Введите номер месяца!");
                alert.showAndWait();
            }
            else {
                try {
                    monthNum = Integer.parseInt(month);
                    if(monthNum>12 || monthNum<1){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText(null);
                        alert.setContentText("Введите корректный номер месяца!");
                        alert.showAndWait();
                    }
                    else {

                        try {
                            getSoldProducts(monthNum);
                        } catch (ParseException e) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText(null);
                            alert.setContentText("Введите только номер месяца!");
                            alert.showAndWait();
                            // e.printStackTrace();
                        }
                    }
                }
                catch (NumberFormatException e){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Ошибка!");
                    alert.setHeaderText(null);
                    alert.setContentText("Введите номер месяца используя только цифры!");
                    alert.showAndWait();
                }
            }

        });

        pdfBtn.setOnAction(event -> {
            pdf();

        });
        helpBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Помощь");
            alert.setHeaderText(null);
            alert.setContentText("Данная страница предназначена для просмотра списка проданных товаров. Для просмотра продаж за конкретный месяц необходимо указать номер нужного месяца в соответствующем поле, после чего нажать кнопку \"Показать\". Для экспорта отчета по продажам за конкретный месяц текущего года в файл формата PDF необходимо указать номер нужного месяца в соответствующем поле, после чего нажать кнопку \"Экспорт в PDF\"");
            alert.showAndWait();

        });
    }

    private void getSoldProducts(int i) throws ParseException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        // int n = 100;
        //Products[] products;
        // products = new Products[n];
        //int i = 0;
        ResultSet result = dbHandler.getProduct("soldout" ,i);
        ObservableList<SoldProducts> prod = FXCollections.observableArrayList();

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy | hh:mm");

        try {
            while (result.next()) {
                SoldProducts products = new SoldProducts();

                products.setCode(result.getString(1));
                products.setName(result.getString(2));
                products.setPrice(result.getInt(3));
                products.setSize(result.getString(4));
                products.setAmount(result.getInt(5));
                products.setDeskr(result.getString(6));
                //products.setAmount(result.getInt(6));
                products.setSeller(result.getString(7));
                products.setDate(result.getString(8));
                /*
                String dateStr = products.getDate();
                Date date = formatForDateNow.parse(dateStr);
                dateStr = formatForDateNow.format(date);
                products.setDate(dateStr);
                 */
                prod.add(products);
                //System.out.println(products);
                // ++i;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView = new TableView<SoldProducts>(prod);
        tableView.setPrefWidth(700);
        tableView.setPrefHeight(600);
        tableView.setLayoutX(0);
        tableView.setLayoutY(0);
        tableView.setStyle("-fx-selection-bar: #9ACD50; -fx-selection-bar-non-focused: #9ACD70;");
        //tableView.setStyle("-fx-background-color:  #9ACD32");

        TableColumn<SoldProducts, String> codeCol = new TableColumn<SoldProducts, String>("Артикул");
        codeCol.setCellValueFactory(new PropertyValueFactory<SoldProducts, String>("code"));
        //codeCol.setStyle("-fx-text-fill: red");
        //codeCol.setStyle("-fx-background-color:  #9ACD32;-fx-opacity: 0.7");
        tableView.getColumns().add(codeCol);

        TableColumn<SoldProducts, String> nameCol = new TableColumn<SoldProducts, String>("Наименование");
        nameCol.setCellValueFactory(new PropertyValueFactory<SoldProducts, String>("name"));
        tableView.getColumns().add(nameCol);

        TableColumn<SoldProducts, String> priceCol = new TableColumn<SoldProducts, String>("Цена");
        priceCol.setCellValueFactory(new PropertyValueFactory<SoldProducts, String>("price"));
        tableView.getColumns().add(priceCol);

        TableColumn<SoldProducts, String> sizeCol = new TableColumn<SoldProducts, String>("Размер");
        sizeCol.setCellValueFactory(new PropertyValueFactory<SoldProducts, String>("size"));
        tableView.getColumns().add(sizeCol);
/*
        TableColumn<SoldProducts, String> amountCol = new TableColumn<SoldProducts, String>("Кол-во");
        amountCol.setCellValueFactory(new PropertyValueFactory<SoldProducts, String>("amount"));
        tableView.getColumns().add(amountCol);
*/
        TableColumn<SoldProducts, String> deskrCol = new TableColumn<SoldProducts, String>("Описание");
        deskrCol.setCellValueFactory(new PropertyValueFactory<SoldProducts, String>("deskr"));
        tableView.getColumns().add(deskrCol);

        TableColumn<SoldProducts, String> sellerCol = new TableColumn<SoldProducts, String>("Продал");
        sellerCol.setCellValueFactory(new PropertyValueFactory<SoldProducts, String>("seller"));
        tableView.getColumns().add(sellerCol);

        TableColumn<SoldProducts, String> dateCol = new TableColumn<SoldProducts, String>("Дата");
        dateCol.setCellValueFactory(new PropertyValueFactory<SoldProducts, String>("date"));
        tableView.getColumns().add(dateCol);

       // tableView.resizeColumn(nameCol, 120);
        //tableView.resizeColumn(deskrCol, 320);

        TableView.TableViewSelectionModel<SoldProducts> selectionModel = tableView.getSelectionModel();

        prodPane.getChildren().add(tableView);
    }

    private void pdf(){
            try {
                    //monthNum = Integer.parseInt(month);
                    if(monthNum>12 || monthNum<1){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText(null);
                        alert.setContentText("Введите корректный номер месяца!");
                        alert.showAndWait();
                    }
                    else {

                        BaseFont times = BaseFont.createFont("c:/windows/fonts/times.ttf", "cp1251", BaseFont.EMBEDDED);
                        DatabaseHandler con = new DatabaseHandler();
                        ResultSet result = con.pdfSelect(monthNum);
                        Document my_pdf_report = new Document();
                        PdfWriter.getInstance(my_pdf_report, new FileOutputStream("C:\\Users\\hi\\Desktop\\ОтчетЗа0"+monthNum+"месяц.pdf"));

                        my_pdf_report.open();

                        my_pdf_report.add(new Paragraph("Отчет по продажам за 0"+monthNum+ " месяц:", new Font(times, 14)));


                        PdfPTable my_report_table = new PdfPTable(6);
                        my_report_table.setWidths(new int[]{100, 100, 70, 70, 100, 100});
                        my_report_table.setSpacingBefore(25);
                        PdfPCell table_cell;
                        addTableHeader(my_report_table, times);
                        while (result.next()) {

                            String code = result.getString("code");
                            table_cell = new PdfPCell(new Phrase(code, new Font(times, 14)));
                            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            my_report_table.addCell(table_cell);

                            String name = result.getString("name");
                            table_cell = new PdfPCell(new Phrase(name, new Font(times, 14)));
                            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            my_report_table.addCell(table_cell);

                            String price = result.getString("price");
                            table_cell = new PdfPCell(new Phrase(price, new Font(times, 14)));
                            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            my_report_table.addCell(table_cell);

                            String size = result.getString("size");
                            table_cell = new PdfPCell(new Phrase(size, new Font(times, 14)));
                            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            my_report_table.addCell(table_cell);

                            String descr = result.getString("deskr");
                            table_cell = new PdfPCell(new Phrase(descr, new Font(times, 14)));
                            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            my_report_table.addCell(table_cell);

                            String date = result.getString("date");
                            table_cell = new PdfPCell(new Phrase(date, new Font(times, 14)));
                            table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            my_report_table.addCell(table_cell);

                        }
                        my_pdf_report.add(my_report_table);
                        my_pdf_report.close();
                        result.close();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Отчет создан!");
                        alert.setHeaderText(null);
                        alert.setContentText("Отчет успешно создан!");
                        alert.showAndWait();
                        //stmt.close();
                        //con.getDbConnection().close();
                    }
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Похоже, данный файл уже открыт. Сначала закройте его.");
                alert.showAndWait();
                e.printStackTrace();
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void addTableHeader(PdfPTable table, BaseFont times) {
        Stream.of("Артикул", "Название", "Цена", "Размер","Описание", "Дата")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setPhrase(new Phrase(columnTitle, new Font(times,14)));
                    table.addCell(header);
                });
    }
}
