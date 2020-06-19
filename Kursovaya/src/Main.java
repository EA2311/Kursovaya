import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/logIn.fxml"));

        primaryStage.setTitle("SalesManager");
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.setResizable(false);

        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/resources/journal.png")));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
