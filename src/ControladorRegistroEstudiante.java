import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Conexion.Connect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorRegistroEstudiante {
    PreparedStatement ps;
    ResultSet rs;
    @FXML
    private TextArea areaResultados;

    @FXML
    private Button botonAgregar;

    @FXML
    private Button botonLimpiar;

    @FXML
    private Button botonMostrar;

    @FXML
    private Button botonSalir;

    @FXML
    private TextField cajaFisica;

    @FXML
    private TextField cajaGenero;

    @FXML
    private TextField cajaID;

    @FXML
    private TextField cajaInformatica;

    @FXML
    private TextField cajaNombre;

    @FXML
    private TextField cajaQuimica;

    @FXML
    void limpiar(ActionEvent event) {
        cajaGenero.setText("");
        areaResultados.setText("");
        cajaNombre.setText("");
        cajaID.setText("");
        cajaFisica.setText("");
        cajaInformatica.setText("");
        cajaQuimica.setText("");
    }

    @FXML
    void agregar(ActionEvent event) {
        Connection conexion = null;
        Connect onConnect = new Connect();
        try {
            conexion = onConnect.connect(); // establecemos la conexion
            ps = conexion.prepareStatement(
                    "INSERT INTO estudiantes (id,nombre,genero,nota_fisica,nota_informatica,nota_quimica) VALUES (?,?,?,?,?,?)");
            if (cajaID.getLength() <= 3) {
                ps.setInt(1, Integer.parseInt(cajaID.getText()));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "El ID debe contener máximo de 3 digitos");
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.showAndWait();
            }
            ps.setString(2, cajaNombre.getText());
            ps.setString(3, cajaGenero.getText());

            if (Float.parseFloat(cajaFisica.getText()) > 100 | Float.parseFloat(cajaInformatica.getText()) > 100
                    | Float.parseFloat(cajaQuimica.getText()) > 100) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "la calificacion debe estar entre 0 y 100");
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.showAndWait();
            } else {
                ps.setDouble(4, Math.round(Double.parseDouble(cajaFisica.getText()) * 100.0) / 100.0);
                ps.setDouble(5, Math.round(Double.parseDouble(cajaInformatica.getText()) * 100.0) / 100.0);
                ps.setDouble(6, Math.round(Double.parseDouble(cajaQuimica.getText()) * 100.0) / 100.0);
            }

            int resultado = ps.executeUpdate(); // ejecutamos la insercion de la base de datos
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Estudiante agregado correctamente");

            } else {
                JOptionPane.showMessageDialog(null, "Error al insertar el Registro");
            }
            conexion.close();
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }

    }

    @FXML
    void mostrar(ActionEvent event) {
        Connection conexion = null;
        Connect onConnect = new Connect();
        try {
            conexion = onConnect.connect();
            ps = conexion.prepareStatement("SELECT * FROM estudiantes");
            rs = ps.executeQuery();
            float sumaNotas = 0;
            float sumaNotasQuim = 0;
            float sumaNotasInfor = 0;
            int notasExcelentesInformatica = 0;
            int notasRegularesFisica = 0;
            int notasDeficientesQuimica = 0;
            int conteo = 0;
            while (rs.next()) {
                sumaNotas += rs.getFloat("nota_fisica");
                sumaNotasQuim += rs.getFloat("nota_quimica");
                sumaNotasInfor += rs.getFloat("nota_informatica");
                notasExcelentesInformatica += (rs.getFloat("nota_informatica") > 90
                        && rs.getFloat("nota_informatica") <= 100) ? 1 : 0;
                notasRegularesFisica += (rs.getFloat("nota_fisica") > 60 && rs.getFloat("nota_fisica") <= 80) ? 1 : 0;
                notasDeficientesQuimica += (rs.getFloat("nota_quimica") > 0 && rs.getFloat("nota_quimica") <= 30) ? 1
                        : 0;
                conteo++;
            }
            areaResultados
                    .setText("El promedio de Informatica es " + Math.round(sumaNotasInfor / conteo * 100.0) / 100.0
                            + "\nEl promedio de Fisica es " + Math.round(sumaNotas / conteo * 100.0) / 100.0
                            + "\nEl promedio de Quimica es " + Math.round(sumaNotasQuim / conteo * 100.0) / 100.0
                            + "\n\nPorcentaje notas excelentes en informatica: "
                            + notasExcelentesInformatica * 100 / conteo + "%"
                            + "\nPorcentaje notas regulares en fisica: " + notasRegularesFisica * 100 / conteo + "%"
                            + "\nPorcentaje notas deficientes en quimica: " + notasDeficientesQuimica * 100 / conteo
                            + "%");
            conexion.close();
        } catch (Exception ex) {
            System.err.println("Error: " + ex);
        }
    }

    @FXML
    void salir(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/Vista/inicioSesion.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Inicio Sesion");
        stage.show();
        Stage old = (Stage) botonSalir.getScene().getWindow();
        old.close();
    }
}