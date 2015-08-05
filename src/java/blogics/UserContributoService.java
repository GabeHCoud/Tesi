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

public class UserContributoService {
    
    public static ArrayList<UserContributo> getUserContributi(DataBase database)
    throws NotFoundDBException,ResultSetDBException 
    {
        ArrayList<UserContributo> c = new ArrayList<UserContributo>();   

        String sql= " SELECT * "+
                    " FROM utente_contributo";                    

        ResultSet resultSet = database.select(sql);
        try {
          while (resultSet.next()) { 
              UserContributo contributo = new UserContributo(resultSet);
              c.add(contributo);
          }
          resultSet.close();
        } catch (SQLException ex) {
          throw new ResultSetDBException("UserContributoService: getUserContributi():  ResultSetDBException: "+ex.getMessage(), database);
        }

        return c;   
    }
    
    public static ArrayList<UserContributo> getUserContributiByEmail(DataBase database,String Email)
    throws NotFoundDBException,ResultSetDBException 
    {
        ArrayList<UserContributo> c = new ArrayList<>();   

        String sql= " SELECT * "+
                    " FROM utente_contributo"+
                    " WHERE Email='"+Email+"'";                    

        ResultSet resultSet = database.select(sql);
        try {
          while (resultSet.next()) { 
              UserContributo contributo = new UserContributo(resultSet);
              c.add(contributo);
          }
          resultSet.close();
        } catch (SQLException ex) {
          throw new ResultSetDBException("UserContributoService: getUserContributiByEmail():  ResultSetDBException: "+ex.getMessage(), database);
        }

        return c;   
    }
    
    public static UserContributo getUserContributoById(DataBase database,int IdUtenteContributo) throws ResultSetDBException, NotFoundDBException
    {
        UserContributo contributo;
        
        String sql = "";
        sql += " SELECT * FROM utente_contributo" +
               " WHERE IdUtenteContributo="+IdUtenteContributo;              
        
        ResultSet resultSet=database.select(sql);    
        try {
            if (resultSet.next()) {
              contributo=new UserContributo(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("UserContributoService: getUserContributoById():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return contributo;
    }
    
    public static UserContributo insertNewUserContributo(DataBase database,String Email,int IdContributo) 
            throws NotFoundDBException,
            DuplicatedRecordDBException,ResultSetDBException 
    {       
        UserContributo contributo;
        
        contributo = new UserContributo(Email,IdContributo);
        contributo.insert(database);
        
        return contributo;        
    }  
}
