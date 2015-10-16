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
import util.Conversion;


public class Fattura {
    public int IdFattura;
    public String Data;
    public double Totale;
    public double CA;
    public double AAA;
    
    public Fattura(){}
    

    public Fattura(String Data,double Totale,double CA,double AAA)
    {
        this.Data = Data;
        this.Totale = Totale;
        this.CA = CA;
        this.AAA = AAA;
    }
    
    public Fattura(ResultSet resultSet)
    {
        try {IdFattura=resultSet.getInt("IdFattura");} catch (SQLException sqle) {}
        try {Data=resultSet.getString("Data");} catch (SQLException sqle) {}
        try {Totale=resultSet.getDouble("Totale");} catch (SQLException sqle) {}
        try {CA=resultSet.getDouble("CA");} catch (SQLException sqle) {}
        try {AAA=resultSet.getDouble("AAA");} catch (SQLException sqle) {}

    }
    
    public void insert(DataBase database) throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException 
    {
        String sql="";        
        
        sql +=  " SELECT * FROM fattura" +
                " WHERE Data='"+Conversion.getDatabaseString(Data)+"'";
        
        ResultSet resultSet=database.select(sql);    
        try {
            if (resultSet.next()) {
                throw new DuplicatedRecordDBException("Fattura: insert: Esiste già una fattura con la data specificata");
            } 
        } catch (SQLException ex) {
            throw new ResultSetDBException("Fattura: insert():  Errore nel ResultSet: "+ex.getMessage(),database);
        }        

        sql =  " INSERT INTO fattura" +
               " (Data,Totale,CA,AAA)"+
               " VALUES ('"+Conversion.getDatabaseString(Data)+"'," + Totale +"," + CA +","+ AAA +")";
        
        database.modify(sql);        
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+= "DELETE FROM fattura WHERE IdFattura="+IdFattura;
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE fattura "+
                " SET Data ='" + Conversion.getDatabaseString(Data) + "'," +     
                " Totale="+Totale+","+
                " CA="+CA+","+
                " AAA="+AAA+
                " WHERE IdFattura="+IdFattura;
        
        database.modify(sql);  
    }
}
