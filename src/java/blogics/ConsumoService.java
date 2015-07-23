package blogics;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import services.databaseservice.*;
import services.databaseservice.exception.*;



public class ConsumoService 
{   
    
    public static ArrayList<Consumo> getConsumiByFatturaId(DataBase database,int IdFattura) 
            throws ResultSetDBException, NotFoundDBException{
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
          throw new ResultSetDBException("ConsumoService: getConsumiByFatturaId():  ResultSetDBException: "+ex.getMessage(), database);
        }
        return consumi;
    }  
    
    public static Consumo getConsumoByFatturaIdAndTelefono(DataBase database,int IdFattura,String Telefono) 
            throws NotFoundDBException, ResultSetDBException
    {
        String sql = "";
        Consumo consumo;
        
        sql +=  " SELECT * FROM consumo"+
                " WHERE IdFattura="+IdFattura+""+
                " AND Telefono='"+Telefono+"'";
        
        ResultSet resultSet = database.select(sql);
        try 
        {
            if(resultSet.next()) 
            { 
                consumo=new Consumo(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) 
        {
          throw new ResultSetDBException("ConsumoService: getConsumoByFatturaIdAndTelefono():  ResultSetDBException: "+ex.getMessage(), database);
        }
        return consumo;
    }
        
    public static void InsertNewConsumo(DataBase database,String Telefono,
            double CRB,double AAA,double ABB,double Totale,String Email,int IdFattura) 
            throws NotFoundDBException, DuplicatedRecordDBException, ResultSetDBException
    {
        Consumo consumo = new Consumo(Telefono,CRB,AAA,ABB,Totale,Email,IdFattura);        
        consumo.insert(database);        
    }
    
}
