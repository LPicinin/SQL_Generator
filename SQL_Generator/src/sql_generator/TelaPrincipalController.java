/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql_generator;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    //private ProgressBar progress;
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        colunas = new ArrayList<>();
    }    

    @FXML
    private void evtGeraScript(ActionEvent event)
    {
        //System.out.println(GetNomeR());
        GetScript();
    }

    private Boolean getInformacoes()
    {
        boolean flag = true;
        String aux = txcolunas.getText();
        tabela = txtabela.getText();
        tabela = tabela.replaceAll(" ", "");
        aux = aux.replaceAll(" ", "");
        if(tabela.isEmpty())
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Favor Informar o Nome da Tabela!!!");
            a.show();
            flag = false;
        }
        else if(aux.replaceAll(",", "").isEmpty())
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Favor Informar o Nome da Tabela!!!");
            a.show();
            flag = false;
        }
        else//tudo OK(ignorando algumas exceções)
        {
            for (int i = 0, p; !aux.isEmpty(); i++)
            {
                p = aux.indexOf(",");
                if(p == -1 && !aux.isEmpty())
                    p = aux.length();
                colunas.add(aux.substring(0, p));
                if(p == aux.length())
                    aux = "";
                else
                    aux = aux.replace(colunas.get(i)+",", "");
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
            if(f.exists())
            {
                RandomAccessFile rafNome = new RandomAccessFile(f, "r");
                aux = new Random().nextInt(21984);
                rafNome.seek(aux);
                rafNome.readLine();
                nome = rafNome.readLine()+" ";
                rafNome.close();
            }
            if(f2.exists())
            {
                RandomAccessFile rafMnome = new RandomAccessFile(f2, "r");
                aux = new Random().nextInt(3896);
                rafMnome.seek(aux);
                rafMnome.readLine();
                nome += rafMnome.readLine()+" ";
                rafMnome.close();
            }
            if(f3.exists())
            {
                RandomAccessFile rafSnome = new RandomAccessFile(f3, "r");
                aux = new Random().nextInt(4944);
                rafSnome.seek(aux);
                rafSnome.readLine();
                nome += rafSnome.readLine();
                rafSnome.close();
            }
        }
        catch(FileNotFoundException fex)
        {
            
        } catch (IOException ex)
        {
            Logger.getLogger(TelaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nome;
    }
    
    private Boolean GetScript()
    {
        String sql = "", aux = "";
        txscript.setText("");
        pngenerator.setDisable(true);
        
        if(getInformacoes())
        {
            Random r = new Random();
            Integer j = 0;
            try
            {
                for (j = 0; j < Integer.parseInt((txquantidade.getText().isEmpty())? "0" : txquantidade.getText()); j++)
                {
                    sql = "INSERT INTO "+txtabela.getText()+" (";
                    for (int k = 0; k < colunas.size(); k++)
                    {
                        if(colunas.get(k).toLowerCase().contains("(double)"))
                        {
                            sql += colunas.get(k).substring(8, colunas.get(k).length());
                        }
                        else if(colunas.get(k).toLowerCase().contains("(integer)"))
                        {
                            sql += colunas.get(k).substring(9, colunas.get(k).length());
                        }
                        else if(colunas.get(k).toLowerCase().contains("(serial)"))
                        {
                            sql += colunas.get(k).substring(8, colunas.get(k).length());
                        }
                        else if(colunas.get(k).toLowerCase().contains("(string)"))
                        {
                            sql += colunas.get(k).substring(8, colunas.get(k).length());
                        }
                        else if(colunas.get(k).toLowerCase().contains("(date)"))
                        {
                            sql += colunas.get(k).substring(6, colunas.get(k).length());
                        }
                        else
                        {
                            sql += "''";
                        }
                        //aux = aux.replaceAll("()", "");
                        sql += aux;
                        aux = "";
                        if(k+1 < colunas.size())
                            sql+=",";
                    }
                    sql+=") VALUES (";
                    for (int k = 0; k < colunas.size(); k++)
                    {
                        if(colunas.get(k).toLowerCase().contains("(double)"))
                        {
                            sql += Double.toString(r.nextDouble()*1000).substring(0, 5);
                        }
                        else if(colunas.get(k).toLowerCase().contains("(integer)"))
                        {
                            sql += Integer.toString(r.nextInt(Integer.SIZE - 1)%1000);
                        }
                        else if(colunas.get(k).toLowerCase().contains("(serial)"))
                        {
                            sql += Integer.toString(j+1);
                        }
                        else if(colunas.get(k).toLowerCase().contains("(date)"))
                        {
                            sql += "'"+Date.valueOf(LocalDate.now().plusDays(r.nextInt(30)).plusMonths(r.nextInt(12)).plusYears(-r.nextInt(5))).toString()+"'";
                        }
                        else if(colunas.get(k).toLowerCase().contains("(string)"))
                        {
                            sql += "'"+GetNomeR()+"'";
                        }
                        else
                        {
                            sql += "''";
                        }
                        if(k+1 < colunas.size())
                            sql+=", ";
                    }
                    sql+=");\n";
                    txscript.setText(txscript.getText() + sql);
                    sql = "";
                }
                
            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
            
        }
        else
        {
            pngenerator.setDisable(false);
        }
        colunas.clear();
        pngenerator.setDisable(false);
        return !txscript.getText().isEmpty();
    }
}
