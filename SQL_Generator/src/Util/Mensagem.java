/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Aluno
 */
public class Mensagem
{

    private Image img;
    private String mensagem;
    private ButtonType[] bts;
    private BorderPane bp;

    public Mensagem(Image img, String mensagem, ButtonType[] bts)
    {
        this.img = img;
        this.mensagem = mensagem;
        this.bts = bts;
    }

    public static Mensagem get(Image img, String mensagem, ButtonType... bts)
    {

        Mensagem m = new Mensagem(img, mensagem, bts);
        m.montaTela();
        return m;
    }

    private void montaTela()
    {
        ImageView iv;
        Image def;
        try
        {
            def = new Image(new File("jk.jpg").toURL().toString());

            if (img == null)
            {
                iv = new ImageView(def);
            } else
            {
                iv = new ImageView(img);
            }
            bp.setLeft(iv);
            HBox hcenter = new HBox(new Label(mensagem));
            hcenter.setAlignment(Pos.CENTER);
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void show()
    {
        Scene s = new Scene(bp);
        Stage st = new Stage();
        st.setScene(s);
        st.show();
    }
}
