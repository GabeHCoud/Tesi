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


public class UserDispositivoService {
    
    public static ArrayList<UserDispositivo> getUserDispositivi(DataBase database)
    throws NotFoundDBException,ResultSetDBException 
    {
        ArrayList<UserDispositivo> d = new ArrayList<UserDispositivo>();   

        String sql= " SELECT * "+
                    " FROM utente_dispositivo";                    

        ResultSet resultSet = database.select(sql);
        try {
          while (resultSet.next()) { 
              UserDispositivo dis = new UserDispositivo(resultSet);
              d.add(dis);
          }
          resultSet.close();
        } catch (SQLException ex) {
          throw new ResultSetDBException("UserDispositivoService: getUserDispositivi():  ResultSetDBException: "+ex.getMessage(), database);
        }

        return d;   
    }
    
    public static ArrayList<UserDispositivo> getUserDispositiviByEmail(DataBase database,String Email)
    throws NotFoundDBException,ResultSetDBException 
    {
        ArrayList<UserDispositivo> d = new ArrayList<UserDispositivo>();   

        String sql= " SELECT * "+
                    " FROM utente_dispositivo"+
                    " WHERE Email='"+Email+"'";                    

        ResultSet resultSet = database.select(sql);
        try {
          while (resultSet.next()) { 
              UserDispositivo dis = new UserDispositivo(resultSet);
              d.add(dis);
          }
          resultSet.close();
        } catch (SQLException ex) {
          throw new ResultSetDBException("UserDispositivoService: getUserDispositiviByEmail():  ResultSetDBException: "+ex.getMessage(), database);
        }

        return d;   
    }
    
    public static UserDispositivo getUserDispositivoById(DataBase database,int IdUtenteDispositivo) throws ResultSetDBException, NotFoundDBException
    {
        UserDispositivo dispositivo;
        
        String sql = "";
        sql += " SELECT * FROM utente_dispositivo" +
               " WHERE IdUtenteDispositivo="+IdUtenteDispositivo;              
        
        ResultSet resultSet=database.select(sql);    
        try {
            if (resultSet.next()) {
              dispositivo=new UserDispositivo(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("UserDispositivoService: getUserDispositivoById():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return dispositivo;
    }
    
    public static UserDispositivo insertNewUserDispositivo(DataBase database,String Email,int IdDispositivo) 
            throws NotFoundDBException,
            DuplicatedRecordDBException,ResultSetDBException 
    {       
        UserDispositivo disp;
        
        disp = new UserDispositivo(Email,IdDispositivo);
        disp.insert(database);
        
        return disp;        
    }  
}
