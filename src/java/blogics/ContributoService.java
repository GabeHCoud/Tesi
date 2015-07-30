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

public class ContributoService {
    
    public static ArrayList<Contributo> getContributi(DataBase database)
    throws NotFoundDBException,ResultSetDBException 
    {
        ArrayList<Contributo> contributi = new ArrayList<>();   

        String sql=" SELECT * FROM contributo";                    

        ResultSet resultSet = database.select(sql);
        try {
          while (resultSet.next()) { 
              Contributo contributo = new Contributo(resultSet);
              contributi.add(contributo);

          }
          resultSet.close();
        } catch (SQLException ex) {
          throw new ResultSetDBException("ContributoService: getContributi():  ResultSetDBException: "+ex.getMessage(), database);
        }

        return contributi;   
    }
    
    public static Contributo getContributoById(DataBase database,int IdContributo) throws ResultSetDBException, NotFoundDBException
    {
        Contributo contributo;
        
        String sql = "";
        sql += " SELECT * FROM contributo" +
               " WHERE IdContributo="+IdContributo;              
        
        ResultSet resultSet=database.select(sql);    
        try {
            if (resultSet.next()) {
              contributo=new Contributo(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("DispositivoService: getContributoById():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return contributo;
    }
    
    public static Contributo insertNewContributo(DataBase database,String Nome,double Costo) 
            throws NotFoundDBException,
            DuplicatedRecordDBException,ResultSetDBException 
    {       
        Contributo contributo;
        
        contributo = new Contributo(Nome,Costo);
        contributo.insert(database);
        
        return contributo;        
    }  
}
