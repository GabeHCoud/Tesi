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

public class Contributo {
    public int IdContributo;
    public String Nome;
    public double Costo;
    
    public Contributo (){}
    
    public Contributo(String Nome,double Costo)
    {
        this.Nome=Nome;
        this.Costo=Costo;
    }
    
    public Contributo(ResultSet resultSet)
    {
        try {IdContributo=resultSet.getInt("IdContributo");} catch(SQLException sqle) {}
        try {Nome=resultSet.getString("Nome");} catch (SQLException sqle) {}
        try {Costo=resultSet.getDouble("Costo");} catch (SQLException sqle) {}
    }
    
    public void insert(DataBase database)
    throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {
    
    String sql="";
    
    sql+=" INSERT INTO contributo (Nome,Costo)"
        +" VALUES ('"+Conversion.getDatabaseString(Nome)+"',"+Costo+")";
    database.modify(sql);
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+="DELETE FROM contributo WHERE IdContributo="+IdContributo;
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE contributo "+
                " SET Nome = '" + Conversion.getDatabaseString(Nome) + "', " +                
                " Costo = " + Costo +
                " WHERE IdContributo = "+IdContributo;
        
        database.modify(sql);  
    } 
}
