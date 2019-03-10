/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql_generator;

import Controladora.Controller;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author Luish
 */
public class TelaPrincipalController implements Initializable
{

    private Controller ctr;
    @FXML
    private TextField txtabela;
    @FXML
    private TextField txcolunas;

    @FXML
    private TextArea txscript;
    @FXML
    private HBox pngenerator;
    @FXML
    private TextField txquantidade;
    @FXML
    private ImageView ivhelp;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        iniciaComponentes();
    }

    @FXML
    private void evtGeraScript(ActionEvent event)
    {
        //ctr.instancia().GetScriptStringBuilder();//Mais Rápido Que String Normal
        ctr.instancia().geraSQL();
    }

    @FXML
    private void evtCopy(MouseEvent event)
    {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        try
        {
            c.setContents(new StringSelection(txscript.getText()), null);
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void evtHelp(MouseEvent event)
    {
        new Alert(Alert.AlertType.INFORMATION, "Tipos de Dados Suportados Para Esta Versão:\n"
                + "                ►double;\n"
                + "                ►integer;\n"
                + "                ►serial;\n"
                + "                ►string(nomes de pessoas aleatórias);\n"
                + "                ►date;\n"
                + "                ►bit(binary).\n\n"
                + "Caso tenha duvida sobre algum campo posicione o cursor do mouse sobre o campo para uma breve descrição!!!", ButtonType.OK).show();
    }

    private void iniciaComponentes()
    {
        try
        {
            ctr.instancia().setALL(txtabela, txcolunas, txscript, pngenerator, txquantidade, ivhelp);
            Tooltip.install(txtabela, new Tooltip("Nome Da Tabela!!"));
            Tooltip.install(txcolunas, new Tooltip("Nome Das Colunas!!!\nEemplo:\n(Tipo)coluna1, (Tipo)coluna2"));
            Tooltip.install(txquantidade, new Tooltip("Número de Linhas Que Serão Geradas!!!"));
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }
}
