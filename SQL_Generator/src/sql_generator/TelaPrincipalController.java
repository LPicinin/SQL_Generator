/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql_generator;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    @FXML
    private TextField txtabela;
    @FXML
    private TextField txcolunas;
    private ArrayList<String> colunas;
    private String tabela;
    @FXML
    private TextArea txscript;
    @FXML
    private HBox pngenerator;
    @FXML
    private TextField txquantidade;
    @FXML
    private ImageView ivhelp;

    //private ProgressBar progress;
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        colunas = new ArrayList<>();
        Tooltip.install(txtabela, new Tooltip("Nome Da Tabela!!"));
        Tooltip.install(txcolunas, new Tooltip("Nome Das Colunas!!!\nEemplo:\n(Tipo)coluna1, (Tipo)coluna2"));
        Tooltip.install(txquantidade, new Tooltip("Número de Linhas Que Serão Geradas!!!"));
    }

    @FXML
    private void evtGeraScript(ActionEvent event)
    {
        long inicio;
        long fim;
        long tresult;
        System.out.flush();
        inicio = System.currentTimeMillis();
        GetScriptStringBuilder();//Mais Rápido Que String Normal
        fim = System.currentTimeMillis();
        tresult = fim - inicio;
        System.out.println("StringBuilder = " + tresult);
    }

    private Boolean getInformacoes()
    {
        boolean flag = true;
        String aux = txcolunas.getText();
        tabela = txtabela.getText();
        tabela = tabela.replaceAll(" ", "");
        aux = aux.replaceAll(" ", "");
        if (tabela.isEmpty())
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Favor Informar o Nome da Tabela!!!");
            a.show();
            flag = false;
        } else if (aux.replaceAll(",", "").isEmpty())
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Favor Informar o Nome da Tabela!!!");
            a.show();
            flag = false;
        } else//tudo OK(ignorando algumas exceções)
        {
            for (int i = 0, p; !aux.isEmpty(); i++)
            {
                p = aux.indexOf(",");
                if (p == -1 && !aux.isEmpty())
                {
                    p = aux.length();
                }
                colunas.add(aux.substring(0, p));
                if (p == aux.length())
                {
                    aux = "";
                } else
                {
                    aux = aux.replace(colunas.get(i) + ",", "");
                }
            }
        }
        return flag && colunas.size() > 0;
    }

    private String GetNomeR()
    {
        String nome = "";
        Integer aux;
        File f = new File("src/Arquivos/PrimeiroNome.txt");
        File f2 = new File("src/Arquivos/MeioNome.txt");
        File f3 = new File("src/Arquivos/SobreNome.txt");
        try
        {
            if (f.exists())
            {
                RandomAccessFile rafNome = new RandomAccessFile(f, "r");
                aux = new Random().nextInt((int) rafNome.length());
                rafNome.seek(aux);
                rafNome.readLine();
                nome = rafNome.readLine() + " ";
                rafNome.close();
            }
            if (f2.exists())
            {
                RandomAccessFile rafMnome = new RandomAccessFile(f2, "r");
                aux = new Random().nextInt((int) rafMnome.length());
                rafMnome.seek(aux);
                rafMnome.readLine();
                nome += rafMnome.readLine() + " ";
                rafMnome.close();
            }
            if (f3.exists())
            {
                RandomAccessFile rafSnome = new RandomAccessFile(f3, "r");
                aux = new Random().nextInt((int) rafSnome.length());
                rafSnome.seek(aux);
                rafSnome.readLine();
                nome += rafSnome.readLine();
                rafSnome.close();
            }
        } catch (FileNotFoundException fex)
        {

        } catch (IOException ex)
        {
            Logger.getLogger(TelaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nome;
    }
    private Boolean GetScriptStringBuilder()
    {
        StringBuilder sql = new StringBuilder("");
        txscript.setText("");
        pngenerator.setDisable(true);

        if (getInformacoes())
        {
            Random r = new Random();
            Integer j = 0;
            try
            {
                for (j = 0; j < Integer.parseInt((txquantidade.getText().isEmpty()) ? "0" : txquantidade.getText()); j++)
                {
                    sql.append("INSERT INTO " + txtabela.getText() + " (");
                    for (int k = 0; k < colunas.size(); k++)
                    {
                        if (colunas.get(k).toLowerCase().contains("(double)"))
                        {
                            sql.append(colunas.get(k).substring(8, colunas.get(k).length()));
                        } else if (colunas.get(k).toLowerCase().contains("(integer)"))
                        {
                            sql.append(colunas.get(k).substring(9, colunas.get(k).length()));
                        } else if (colunas.get(k).toLowerCase().contains("(serial)"))
                        {
                            sql.append(colunas.get(k).substring(8, colunas.get(k).length()));
                        } else if (colunas.get(k).toLowerCase().contains("(string)"))
                        {
                            sql.append(colunas.get(k).substring(8, colunas.get(k).length()));
                        } else if (colunas.get(k).toLowerCase().contains("(date)"))
                        {
                            sql.append(colunas.get(k).substring(6, colunas.get(k).length()));
                        } else
                        {
                            sql.append("''");
                        }
                        if (k + 1 < colunas.size())
                        {
                            sql.append(",");
                        }
                    }
                    sql.append(") VALUES (");
                    for (int k = 0; k < colunas.size(); k++)
                    {
                        if (colunas.get(k).toLowerCase().contains("(double)"))
                        {
                            sql.append(Double.toString(r.nextDouble() * 1000).substring(0, 5));
                        } else if (colunas.get(k).toLowerCase().contains("(integer)"))
                        {
                            sql.append(Integer.toString(r.nextInt(Integer.SIZE - 1) % 1000));
                        } else if (colunas.get(k).toLowerCase().contains("(serial)"))
                        {
                            sql.append(Integer.toString(j + 1));
                        } else if (colunas.get(k).toLowerCase().contains("(date)"))
                        {
                            sql.append("'" + Date.valueOf(LocalDate.now().plusDays(r.nextInt(30)).plusMonths(r.nextInt(12)).plusYears(-r.nextInt(5))).toString() + "'");
                        } else if (colunas.get(k).toLowerCase().contains("(string)"))
                        {
                            sql.append("'" + GetNomeR() + "'");
                        } else
                        {
                            sql.append("''");
                        }
                        if (k + 1 < colunas.size())
                        {
                            sql.append(", ");
                        }
                    }
                    sql.append(");\n");
                    //txscript.setText(txscript.getText() + sql);
                    //sql = new StringBuilder("");
                }
                txscript.setText(sql.toString());

            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }

        } else
        {
            pngenerator.setDisable(false);
        }
        colunas.clear();
        pngenerator.setDisable(false);
        return !txscript.getText().isEmpty();
    }

    @FXML
    private void evtCopy(MouseEvent event)
    {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        try
        {
            c.setContents(new StringSelection(txscript.getText()), null);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void evtHelp(MouseEvent event)
    {
        new Alert(Alert.AlertType.INFORMATION, "Tipos de Dados Suportados Para Esta Versão:\n" +
"                ►double;\n" +
"                ►integer;\n" +
"                ►serial;\n" +
"                ►string(nomes de pessoas aleatórias);\n" +
"                ►date.\n\n"+
                "Caso tenha duvida sobre algum campo posicione o cursor do mouse sobre o campo para uma breve descrição!!!", ButtonType.OK).show();
    }
}
