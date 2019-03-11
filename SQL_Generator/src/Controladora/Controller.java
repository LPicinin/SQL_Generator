/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladora;

import Banco.Banco;
import Banco.Conexao;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author luis
 */
public class Controller
{

    private static Controller controller;

    private String tabela;
    private ArrayList<String> colunas;
    private TextField txtabela;
    private TextField txcolunas;
    private TextArea txscript;
    private HBox pngenerator;
    private TextField txquantidade;
    private ImageView ivhelp;

    private Controller()
    {
        colunas = new ArrayList<>();
    }

    public static Controller instancia()
    {
        if (controller == null)
        {
            controller = new Controller();
        }
        return controller;
    }

    public void setALL(TextField txtabela,
            TextField txcolunas, TextArea txscript, HBox pngenerator,
            TextField txquantidade, ImageView ivhelp)
    {
        this.txtabela = txtabela;
        this.txcolunas = txcolunas;
        this.txscript = txscript;
        this.pngenerator = pngenerator;
        this.txquantidade = txquantidade;
        this.ivhelp = ivhelp;
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
                try
                {
                    RandomAccessFile rafNome = new RandomAccessFile(f, "r");
                    aux = new Random().nextInt((int) rafNome.length());
                    rafNome.seek(aux);
                    rafNome.readLine();
                    nome = rafNome.readLine() + " ";
                    rafNome.close();
                } catch (FileNotFoundException ex)
                {
                    System.out.println(ex.getMessage());
                }
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
        } catch (Exception ex)
        {
            nome = "default";
        }

        return nome;
    }

    private String GetNomeSQLite()
    {
        String nome = "";
        Integer aux;
        Conexao con = Banco.getConexao();
        ResultSet rs;
        Random r = new Random();
        int countP = con.getMaxPK("pnome", "codigo");
        int countM = con.getMaxPK("mnome", "codigo");
        int countS = con.getMaxPK("snome", "codigo");
        Integer ap, am, as;

        ap = r.nextInt(countP);
        am = r.nextInt(countM);
        as = r.nextInt(countS);
        try
        {
            //nome
            rs = con.consultar("SELECT nome FROM pnome WHERE pnome.codigo = " + ap + " \n"
                    + "    UNION \n"
                    + "SELECT nome FROM mnome WHERE mnome.codigo = " + am + "\n"
                    + "    UNION\n"
                    + "SELECT nome FROM snome WHERE snome.codigo = " + as + "");
            while (rs.next())
            {
                nome += rs.getString("nome");
            }
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage() + Banco.getConexao().getMensagemErro());
        }

        return nome;
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

    public String GetScriptStringBuilder()
    {
        StringBuilder sql = new StringBuilder("");
        txscript.setText("");
        pngenerator.setDisable(true);

        if (getInformacoes())
        {
            Random r = new Random();
            Integer j = 0;
            int q = Integer.parseInt((txquantidade.getText().isEmpty()) ? "0" : txquantidade.getText());
            try
            {
                for (j = 0; j < q; j++)
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
                        } else if (colunas.get(k).toLowerCase().contains("(bit)"))
                        {
                            sql.append(colunas.get(k).substring(5, colunas.get(k).length()));
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
                            sql.append("'").append(Date.valueOf(LocalDate.now().plusDays(r.nextInt(30)).plusMonths(r.nextInt(12)).plusYears(-r.nextInt(5))).toString()).append("'");
                        } else if (colunas.get(k).toLowerCase().contains("(bit)"))
                        {
                            sql.append(r.nextInt(10) % 2);
                        } else if (colunas.get(k).toLowerCase().contains("(string)"))
                        {
                            sql.append("'").append(GetNomeSQLite()).append("'");
                            //sql.append("'").append(GetNomeR()).append("'");
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
                }

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
        return sql.toString();
    }

    ////////////////////tentativa schitch
    /**
     * Pequeno ganho de performace se comparado ao metodo sem switch
     *
     * @return String->(SQL)
     */
    public String GetScriptStringBuilderSwitch()
    {
        StringBuilder sql = new StringBuilder("");
        txscript.setText("");
        pngenerator.setDisable(true);

        if (getInformacoes())
        {
            Random r = new Random();
            Integer j = 0, k;
            int q = Integer.parseInt((txquantidade.getText().isEmpty()) ? "0" : txquantidade.getText());
            String tp, otimizacao;
            String db, db2;
            int i1, i2;
            for (j = 0; j < q; j++)
            {
                sql.append("INSERT INTO ").append(txtabela.getText()).append(" (");
                for (k = 0; k < colunas.size(); k++)
                {
                    otimizacao = colunas.get(k).toLowerCase();
                    i1 = otimizacao.indexOf("(");
                    i2 = otimizacao.indexOf(")");
                    tp = otimizacao.substring(i1 + 1, i2);
                    switch (tp)
                    {
                        case "double":
                            sql.append(colunas.get(k).substring(8, colunas.get(k).length()));
                            break;
                        case "integer":
                            sql.append(colunas.get(k).substring(9, colunas.get(k).length()));
                            break;
                        case "serial":
                            sql.append(colunas.get(k).substring(8, colunas.get(k).length()));
                            break;
                        case "string":
                            sql.append(colunas.get(k).substring(8, colunas.get(k).length()));
                            break;
                        case "date":
                            sql.append(colunas.get(k).substring(6, colunas.get(k).length()));
                            break;
                        case "bit":
                            sql.append(colunas.get(k).substring(5, colunas.get(k).length()));
                            break;
                        default:
                            sql.append("''");
                            break;
                    }
                    if (k + 1 < colunas.size())
                    {
                        sql.append(",");
                    }
                }
                sql.append(") VALUES (");
                for (k = 0; k < colunas.size(); k++)
                {

                    otimizacao = colunas.get(k).toLowerCase();
                    i1 = otimizacao.indexOf("(");
                    i2 = otimizacao.indexOf(")");
                    tp = otimizacao.substring(i1 + 1, i2);

                    switch (tp)
                    {
                        case "double":
                            sql.append(Double.toString(r.nextDouble() * 1000).substring(0, 5));
                            break;
                        case "integer":
                            sql.append(Integer.toString(r.nextInt(Integer.SIZE - 1) % 1000));
                            break;
                        case "serial":
                            sql.append(Integer.toString(j + 1));
                            break;
                        case "string":
                            sql.append("'").append(GetNomeR()).append("'");
                            break;
                        case "date":
                            sql.append("'").append(Date.valueOf(LocalDate.now().plusDays(r.nextInt(30)).plusMonths(r.nextInt(12)).plusYears(-r.nextInt(5))).toString()).append("'");
                            break;
                        case "bit":
                            sql.append(r.nextInt(10) % 2);
                            break;
                        default:
                            sql.append("''");
                            break;
                    }

                    if (k + 1 < colunas.size())
                    {
                        sql.append(", ");
                    }
                }
                sql.append(");\n");
            }

        } else
        {
            pngenerator.setDisable(false);
        }
        colunas.clear();
        pngenerator.setDisable(false);
        return sql.toString();
    }

    public void geraSQL()
    {
        txscript.setText(GetScriptStringBuilderSwitch());
    }
}
