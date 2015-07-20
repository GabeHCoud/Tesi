package blogics;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import services.databaseservice.*;
import services.databaseservice.exception.*;



public class ConsumoService 
{   
    
    public static ArrayList<Consumo> getConsumiByFatturaId(DataBase database,int IdFattura) throws ResultSetDBException, NotFoundDBException{
        ArrayList<Consumo> consumi = new ArrayList<Consumo>();
        
        String sql = "";
        sql += " SELECT * FROM consumo" +
               " WHERE IdFattura="+IdFattura ;

        ResultSet resultSet = database.select(sql);
        try 
        {
            while (resultSet.next()) 
            { 
                consumi.add(new Consumo(resultSet));
            }
            resultSet.close();
        } catch (SQLException ex) 
        {
          throw new ResultSetDBException("FieldService: getConsumiByFatturaId():  ResultSetDBException: "+ex.getMessage(), database);
        }
        return consumi;
    }  
    
        
    public static void InsertNewConsumo(DataBase database,String Telefono,
            double CRB,double AAA,double ABB,double Totale,String Email,int IdFattura) 
            throws NotFoundDBException, DuplicatedRecordDBException, ResultSetDBException
    {
        Consumo consumo = new Consumo(Telefono,CRB,AAA,ABB,Totale,Email,IdFattura);        
        consumo.insert(database);        
    }
    
}
