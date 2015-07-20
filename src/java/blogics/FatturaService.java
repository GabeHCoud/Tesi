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

public class FatturaService {    
    
    public static Fattura getFatturaById(DataBase database,int IdFattura) throws ResultSetDBException, NotFoundDBException
    {
        Fattura fattura;
        
        String sql = "";
        sql += " SELECT * FROM fattura" +
               " WHERE IdFattura="+IdFattura;              
        
        ResultSet resultSet=database.select(sql);    
        try {
            if (resultSet.next()) {
              fattura=new Fattura(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("FatturaService: getFatturaById():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return fattura;
    }
    
    public static Fattura getFatturaByDate(DataBase database,String Data) throws ResultSetDBException, NotFoundDBException
    {
        Fattura fattura;
        
        String sql = "";
        sql += " SELECT * FROM fattura" +
               " WHERE Data='"+Data+"'";              
        
        ResultSet resultSet=database.select(sql);    
        try {
            if (resultSet.next()) {
              fattura=new Fattura(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("FatturaService: getFatturaByDate():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return fattura;
    }
    
    public static Fattura getLatestFattura(DataBase database) throws ResultSetDBException, NotFoundDBException
    {
        Fattura fattura;
        
        String sql = "";
        sql += " SELECT * FROM fattura" +
               " WHERE Data=''";              
        
        ResultSet resultSet=database.select(sql);    
        try {
            if (resultSet.next()) {
              fattura=new Fattura(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("FatturaService: getLatestFattura():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return fattura;
    }
    
    public static void insertNewFattura(DataBase database,String data) throws NotFoundDBException, DuplicatedRecordDBException, ResultSetDBException
    {
        Fattura fattura = new Fattura(data);        
        fattura.insert(database);        
    }
}
