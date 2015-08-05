/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blogics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import services.databaseservice.DataBase;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;

/**
 *
 * @author Massa
 */
public class TelefonoService {    
    
    public static Telefono getTelefono(DataBase database,String Numero) 
            throws NotFoundDBException, ResultSetDBException
    {
        Telefono telefono;
        String sql = "";
        sql += " SELECT * FROM telefono" +
               " WHERE Numero='"+Numero+"'";              
        
        ResultSet resultSet=database.select(sql);
    
        try {
            if (resultSet.next()) {
              telefono=new Telefono(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("TelefonoService: getTelefono():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return telefono;
    }
    
    public static ArrayList<Telefono> getTelefoni(DataBase database) throws NotFoundDBException, ResultSetDBException
    {
        ArrayList<Telefono> telefoni = new ArrayList<Telefono>();
        String sql = "";
        
        sql += " SELECT * FROM telefono";
                         
        
        ResultSet resultSet=database.select(sql);
    
        try {
            while (resultSet.next()) {
              Telefono telefono=new Telefono(resultSet);
              telefoni.add(telefono);
            } 
        } catch (SQLException ex) {
            throw new ResultSetDBException("TelefonoService: getTelefoni():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return telefoni;        
    }
    
    public static ArrayList<Telefono> getTelefoniByEmail(DataBase database,String Email) throws NotFoundDBException, ResultSetDBException
    {
        ArrayList<Telefono> telefoni = new ArrayList<Telefono>();
        String sql = "";
        
        sql += " SELECT * FROM telefono"+
               " WHERE Email='"+Email+"'";
                         
        
        ResultSet resultSet=database.select(sql);
    
        try {
            while (resultSet.next()) {
              Telefono telefono=new Telefono(resultSet);
              telefoni.add(telefono);
            } 
        } catch (SQLException ex) {
            throw new ResultSetDBException("TelefonoService: getTelefoniByEmail():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return telefoni;
        
    }
    
    public static void insertNewTelefono(DataBase database,String numero,String email,int IdContributo,int IdDispositivo) throws NotFoundDBException, DuplicatedRecordDBException, ResultSetDBException
    {      
        Telefono telefono = new Telefono(numero,email,IdContributo,IdDispositivo);        
        telefono.insert(database);        
    }
    
    public static void insertNewTelefono(DataBase database,String numero,String email) throws NotFoundDBException, DuplicatedRecordDBException, ResultSetDBException
    {      
        Telefono telefono = new Telefono(numero,email);        
        telefono.insert(database);        
    }
}
