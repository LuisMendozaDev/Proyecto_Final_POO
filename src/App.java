import Conexion.Connect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/Vista/inicioSesion.fxml")); // nombre formulario
        Parent root = fxmlloader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Inicio de Sesion");
        primaryStage.setScene(scene);
        primaryStage.show();
        Connect connect = new Connect();
        connect.connect();
    }
}
