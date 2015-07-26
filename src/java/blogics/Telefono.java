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
    
    public Telefono(){}
    
    public Telefono(String Numero,String Email)
    {
        this.Numero = Numero;
        this.Email = Email;
    }
    
    public Telefono(ResultSet resultSet)
    {
        try {Numero=resultSet.getString("Numero");} catch (SQLException sqle) {}
        try {Email=resultSet.getString("Email");} catch (SQLException sqle) {}
        
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
        sql +=  " UPDATE telefono "+
                " SET Email = '" + Email + "'" +                 
                " WHERE Numero='"+Numero+"'";                
        
        database.modify(sql);  
    } 
    
    
}
