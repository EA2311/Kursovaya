package sample;
import controllers.LogController;
import javafx.scene.control.Alert;
import models.Products;
import models.SoldProducts;
import models.Users;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.Date;
import models.Users;

public class DatabaseHandler extends Configs {
    Connection dbConnection = null;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName + "?serverTimezone=Europe/Moscow&useSSL=false";

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void registerUser(Users user) {
        String insertUs = "INSERT INTO users (username, password, telephone, adres, name, surname)VALUES(?,?,?,?,?,?)";
        String insertAdm = "INSERT INTO admins (username, password, telephone, adres, name, surname)VALUES(?,?,?,?,?,?)";

        try {
            if (user.getFunc().equals("Админ")) {
                PreparedStatement prSt1 = getDbConnection().prepareStatement(insertAdm);

                prSt1.setString(1, user.getUsername());
                prSt1.setString(2, user.getPassword());
                prSt1.setString(3, user.getTelephone());
                prSt1.setString(4, user.getAdres());
                prSt1.setString(5, user.getName());
                prSt1.setString(6, user.getSurname());
                prSt1.executeUpdate();
                getDbConnection().close();
            } else {
                PreparedStatement prSt = getDbConnection().prepareStatement(insertUs);
                prSt.setString(1, user.getUsername());
                prSt.setString(2, user.getPassword());
                prSt.setString(3, user.getTelephone());
                prSt.setString(4, user.getAdres());
                prSt.setString(5, user.getName());
                prSt.setString(6, user.getSurname());
                prSt.executeUpdate();
                getDbConnection().close();
            }
        } catch (SQLException |
                ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public ResultSet getUser(Users user, boolean check){
        ResultSet resultSet = null;
        String select;
        if(check)
            select = "SELECT * FROM admins WHERE username =? AND password =?";
        else
            select = "SELECT * FROM users WHERE username =? AND password =?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            prSt.setString(1, user.getUsername());
            prSt.setString(2, user.getPassword());

            resultSet = prSt.executeQuery();
            getDbConnection().close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet checkUsername(Users user, boolean check){
        ResultSet resultSet = null;
        String select;
        if(check)
            select = "SELECT * FROM admins WHERE username =?";
        else
            select = "SELECT * FROM users WHERE username =?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getUsername());
            resultSet = prSt.executeQuery();
            getDbConnection().close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getProduct(String table, int i){
        ResultSet resultSet = null;
        String select;
        if(i == 0)
            select = "SELECT * FROM "+ table;
        else
            select = "SELECT * FROM soldout WHERE month(date) = " + i;
        try {
            Statement statement = getDbConnection().createStatement();
            resultSet = statement.executeQuery(select);
            getDbConnection().close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void deleteProduct(Products product) throws SQLException, ClassNotFoundException {
        String delete = "DELETE FROM products WHERE code =?";
        String update = "UPDATE products SET amount = ? WHERE code =?";

        if(product.getAmount()>1){
            PreparedStatement prSt = getDbConnection().prepareStatement(update);

            prSt.setInt(1, product.getAmount()-1);
            prSt.setString(2, product.getCode());


            prSt.executeUpdate();
        }else {
            try {
                PreparedStatement prSt = getDbConnection().prepareStatement(delete);

                prSt.setString(1, product.getCode());

                prSt.executeUpdate();
                getDbConnection().close();

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void insertSoldProduct(Products product) {
        String insert = "INSERT INTO soldout (code, name, price, size, amount, deskr, seller, date)VALUES(?,?,?,?,?,?,?,?)";
        LogController controller = new LogController();
        try {
            Date date = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd' 'hh:mm:ss");

            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, product.getCode());
            prSt.setString(2, product.getName());
            prSt.setInt(3, product.getPrice());
            prSt.setString(4, product.getSize());
            prSt.setInt(5, product.getAmount());
            prSt.setString(6, product.getDeskr());
            prSt.setString(7, controller.seller);
            prSt.setString(8, formatForDateNow.format(date));

            prSt.executeUpdate();
            getDbConnection().close();

        } catch (SQLException |
                ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertNewProduct(Products product){
        int i = addNewProdCheck(product.getCode());
        System.out.println(i);
        if(i>=1){
        String update = "UPDATE products SET amount = amount + ? WHERE code = ?";
            try {

                PreparedStatement prSt = getDbConnection().prepareStatement(update);
                prSt.setInt(1, product.getAmount());
                prSt.setString(2, product.getCode());

                prSt.executeUpdate();
                getDbConnection().close();

            } catch (SQLException |
                    ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            String insert = "INSERT INTO products (code, name, price, size, description, amount)VALUES(?,?,?,?,?,?)";
            try {

                PreparedStatement prSt = getDbConnection().prepareStatement(insert);
                prSt.setString(1, product.getCode());
                prSt.setString(2, product.getName());
                prSt.setInt(3, product.getPrice());
                prSt.setString(4, product.getSize());
                prSt.setString(5, product.getDeskr());
                prSt.setInt(6, product.getAmount());

                prSt.executeUpdate();
                getDbConnection().close();

            } catch (SQLException |
                    ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet getUserInfo(){
        ResultSet resultSet = null;
        String select = "SELECT * FROM users";

        try {
            Statement statement = getDbConnection().createStatement();
            resultSet = statement.executeQuery(select);
            getDbConnection().close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
        }

    public int addNewProdCheck(String code){
        ResultSet resultSet = null;
        String select = "SELECT * FROM products WHERE code =?";
        int i = 0;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, code);
            resultSet = prSt.executeQuery();

            while (resultSet.next()) {
                i++;
            }
            getDbConnection().close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return i;
    }

    public void deleteUser(Users user) throws SQLException, ClassNotFoundException {
        String delete = "DELETE FROM users WHERE username =?";

            try {
                PreparedStatement prSt = getDbConnection().prepareStatement(delete);

                prSt.setString(1, user.getUsername());

                prSt.executeUpdate();
                getDbConnection().close();

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

    }

    public ResultSet pdfSelect(int month){
        //Statement stmt = dbConnection.createStatement();
       // ResultSet query_set = stmt.executeQuery("SELECT * FROM soldout");

        ResultSet resultSet = null;
        String select = "SELECT * FROM soldout WHERE month(date) = ? and year(date)=year(now())";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            prSt.setInt(1, month);

            resultSet = prSt.executeQuery();
            getDbConnection().close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet forStat(/*int month*/){
        ResultSet resultSet = null;
       // String select = "SELECT price FROM soldout WHERE month(date) = ? and year(date)=year(now())";
        String select = "SELECT SUM(price), DATE_FORMAT(`date`, '%m') as period FROM soldout WHERE year(date)=year(now()) GROUP BY period";
        try {
            //PreparedStatement prSt = getDbConnection().prepareStatement(select);
            Statement st = getDbConnection().createStatement();
            //prSt.setInt(1, month);

            resultSet = st.executeQuery(select);
            getDbConnection().close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
