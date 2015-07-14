package blogics;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import services.databaseservice.*;
import services.databaseservice.exception.*;



public class FatturaService 
{
    public FatturaService() {}    
    
    public static Fattura getFatturaByTelefonoAndData(DataBase database,String Data,String Telefono) 
            throws NotFoundDBException, ResultSetDBException
    {
        Fattura fattura;
        String sql = "";
        sql += " SELECT * FROM fattura" +
               " WHERE Data='"+Data+"'"+
               " AND Telefono='"+Telefono+"'" ;
        
        ResultSet resultSet=database.select(sql);
    
        try {
            if (resultSet.next()) {
              fattura=new Fattura(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("FatturaService: getFatturaByTelefonoAndData():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return fattura;
    }
    
    
    public static ArrayList<Fattura> getFattureByData(DataBase database,String Data)
        throws NotFoundDBException,ResultSetDBException 
    {        
        ArrayList<Fattura> fatture = new ArrayList<Fattura>();
        String sql = "";
        sql += " SELECT * FROM fattura" +
               " WHERE Data='"+Data+"'" ;

        ResultSet resultSet = database.select(sql);
        try 
        {
            while (resultSet.next()) 
            { 

                fatture.add(new Fattura(resultSet));

            }
            resultSet.close();
        } catch (SQLException ex) 
        {
          throw new ResultSetDBException("FieldService: getFattureByData():  ResultSetDBException: "+ex.getMessage(), database);
        }
        return fatture;
    }
    
    
    public static ArrayList<Fattura> getFattureByTelefono(DataBase database, String Telefono)
        throws NotFoundDBException,ResultSetDBException 
    {

        ArrayList<Fattura> f = new ArrayList<Fattura>();
        
        String sql = "";
        sql += " SELECT * FROM fattura" +
               " WHERE Telefono='"+Telefono+"'" ;

        ResultSet resultSet = database.select(sql);
        try {
            while (resultSet.next()) { 
                Fattura field = new Fattura(resultSet);
                f.add(field);

            }
            resultSet.close();
        } catch (SQLException ex) {
            throw new ResultSetDBException("FieldService: getFattureByTelefono():  ResultSetDBException: "+ex.getMessage(), database);
        }

    return f;   
    }
        
    public static void InsertNewFattura(DataBase database,String Telefono,
            String Data,double CRB,double AAA,double ABB,double Totale) 
            throws NotFoundDBException, DuplicatedRecordDBException, ResultSetDBException
    {
        Fattura f= new Fattura(Telefono,Data,CRB,AAA,ABB,Totale);        
        f.insert(database);
    }
    
}
