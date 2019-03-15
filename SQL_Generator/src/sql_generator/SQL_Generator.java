/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql_generator;

import Banco.Banco;
import Util.Mensagem;
import java.sql.ResultSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author Luish
 */
public class SQL_Generator extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TelaPrincipal.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        //Mensagem.get(null, "", ButtonType.OK).show();
        if (Banco.conectar()) {
            stage.show();
            /*try
            {
                ResultSet consultar = Banco.getConexao().consultar("SELECT nome from snome where codigo = 150");
                consultar.next();
                System.out.println(consultar.getString("nome"));
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
            }*/
        } else {
            System.out.println(Banco.getConexao().getMensagemErro());
            System.exit(0);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
