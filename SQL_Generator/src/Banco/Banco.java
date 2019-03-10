/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Banco;

/**
 *
 * @author luis
 */
public class Banco
{

    static private Conexao con = null;

    private Banco()
    {
    }

    public static Conexao getConexao()
    {
        return con;
    }

    static public boolean conectar()
    {
        if (con == null)
        {
            con = new Conexao();
            return con.conectar("jdbc:sqlite:src\\db\\", "sqlitejava.db");
        }
        return true;
    }

}
