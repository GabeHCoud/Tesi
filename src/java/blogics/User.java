package blogics;

import java.nio.charset.Charset;
import java.sql.*;

import java.sql.*;
import java.util.ArrayList;
import services.databaseservice.*;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;
import util.Conversion;

public class User {
    public String Email;
    public String Nome;
    public String Cognome;

    public User( String Email, String Nome, String Cognome)
    {            
        this.Email=Email;
        this.Nome=Nome;
        this.Cognome=Cognome;
    }

    public User(ResultSet resultSet) 
    {           
        try {Email=resultSet.getString("Email");} catch (SQLException sqle) {}
        try {Nome=resultSet.getString("Nome");} catch (SQLException sqle) {}
        try {Cognome=resultSet.getString("Cognome");} catch (SQLException sqle) {}
    }
    
    public void insert(DataBase database)
        throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {

        String sql="";     
        
        //controllo duplicati
        sql +=  " SELECT * FROM utente"+
                " WHERE Email='"+Conversion.getDatabaseString(Email)+"'";
        
        ResultSet resultSet=database.select(sql);
    
        try {
            if (resultSet.next()) {
                throw new DuplicatedRecordDBException("Esiste già un utente per l'indirizzo mail inserito.");
            } 
        } catch (SQLException ex) {
            throw new ResultSetDBException("User: insert():  Errore nel ResultSet: "+ex.getMessage(),database);
        }        

        sql=" INSERT INTO utente(Email,Nome,Cognome)"
            +" VALUES ('"+Conversion.getDatabaseString(Email)+"','"
                +Conversion.getDatabaseString(Nome)+"','"
                +Conversion.getDatabaseString(Cognome)+"')";
        database.modify(sql);
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+=   " DELETE FROM utente "+
                " WHERE Email='"+Conversion.getDatabaseString(Email)+"'";                
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE utente "+
                " SET Nome = '" + Conversion.getDatabaseString(Nome) + "',"+
                " Cognome = '" + Conversion.getDatabaseString(Cognome) +"'"+     
                " WHERE Email='"+Conversion.getDatabaseString(Email)+"'";        
        
        database.modify(sql);  
    }   
  
    public void updateEmail(DataBase database,String newemail)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE utente "+
                " SET Nome = '" + Conversion.getDatabaseString(Nome) + "',"+
                " Cognome = '" + Conversion.getDatabaseString(Cognome) +"',"+
                " Email = '" + Conversion.getDatabaseString(newemail) +"'"+   
                " WHERE Email='"+Conversion.getDatabaseString(Email)+"'";        
        
        database.modify(sql);  
    }  
    
} 
 