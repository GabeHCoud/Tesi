/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blogics;

import java.sql.ResultSet;
import java.sql.SQLException;
import services.databaseservice.DataBase;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;


public class Telefono {
    public String Numero;
    public String Email;
    public int IdContributo;
    public int IdDispositivo;
    
    public Telefono(){}
    
    public Telefono(String Numero,String Email)
    {
        this.Numero = Numero;
        this.Email = Email;
    }
    
    public Telefono(String Numero,String Email,int IdContributo,int IdDispositivo)
    {
        this.Numero = Numero;
        this.Email = Email;
        this.IdContributo = IdContributo;
        this.IdDispositivo = IdDispositivo;
    }
    
    public Telefono(ResultSet resultSet)
    {
        try {Numero=resultSet.getString("Numero");} catch (SQLException sqle) {}
        try {Email=resultSet.getString("Email");} catch (SQLException sqle) {}
        try {IdContributo=resultSet.getInt("IdContributo");} catch (SQLException sqle) {}
        try {IdDispositivo=resultSet.getInt("IdDispositivo");} catch (SQLException sqle) {}
    }
    
    public void insert(DataBase database) throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException 
    {
        String sql="";     
        
        //controllo duplicati
        sql +=  " SELECT * FROM telefono"+
                " WHERE Numero='"+Numero+"'";
        
        ResultSet resultSet=database.select(sql);
    
        try {
            if (resultSet.next()) {
                throw new DuplicatedRecordDBException("Il numero di telefono è già associato ad un utente");
            } 
        } catch (SQLException ex) {
            throw new ResultSetDBException("Telefono: insert():  Errore nel ResultSet: "+ex.getMessage(),database);
        }        

        sql =   " INSERT INTO telefono "+
                " (Numero,Email)"+       
                " VALUES('"+Numero+"','"+Email+"')";
    
        database.modify(sql);        
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+= " DELETE FROM telefono " +
              " WHERE Numero='"+Numero+"'";
              
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";

        if(IdContributo == 0 && IdDispositivo == 0)
        {
            sql +=  " UPDATE telefono "+
                    " SET Email='" + Email + "'," +     
                    " IdContributo=" + null + "," +
                    " IdDispositivo=" + null +
                    " WHERE Numero='"+Numero+"'";     
        }else if(IdContributo == 0)
        {
            sql +=  " UPDATE telefono "+
                    " SET Email='" + Email + "'," +     
                    " IdContributo=" + null + "," +
                    " IdDispositivo=" + IdDispositivo +
                    " WHERE Numero='"+Numero+"'";  
        }else if(IdDispositivo == 0)
        {
            sql +=  " UPDATE telefono "+
                    " SET Email='" + Email + "'," +     
                    " IdContributo=" + IdContributo + "," +
                    " IdDispositivo=" + null + 
                    " WHERE Numero='"+Numero+"'";  
        }else
        {
            sql +=  " UPDATE telefono "+
                    " SET Email='" + Email + "'," +     
                    " IdContributo=" + IdContributo + "," +
                    " IdDispositivo=" + IdDispositivo + 
                    " WHERE Numero='"+Numero+"'";
        }
        
        database.modify(sql);  
    } 
    
    public void updateNumero(DataBase database,String newnumero)
     throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        
        sql +=  " UPDATE telefono "+
                    " SET Numero='" + newnumero + "'" +
                    " WHERE Numero='"+Numero+"'";
        
        database.modify(sql);  

    }
    
    
}
