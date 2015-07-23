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

/**
 *
 * @author Massa
 */
public class TelefonoService {    
    
    public TelefonoService() {}    
    
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
    
    public static void insertNewTelefono(DataBase database,String numero,String email) throws NotFoundDBException, DuplicatedRecordDBException, ResultSetDBException
    {
        Telefono telefono = new Telefono(numero,email);        
        telefono.insert(database);        
    }
}
