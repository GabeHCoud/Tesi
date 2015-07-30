package blogics;

import java.sql.*;

import java.sql.*;
import services.databaseservice.*;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;

public class Dispositivo {
    public int IdDispositivo;
    public String Nome;
    public double Costo;
    
    public Dispositivo (){}
    
    public Dispositivo(String Nome,double Costo)
    {
        this.Nome=Nome;
        this.Costo=Costo;
    }
    
    public Dispositivo(ResultSet resultSet)
    {
        try {IdDispositivo=resultSet.getInt("IdDispositivo");} catch(SQLException sqle) {}
        try {Nome=resultSet.getString("Nome");} catch (SQLException sqle) {}
        try {Costo=resultSet.getDouble("Costo");} catch (SQLException sqle) {}
    }
    
    public void insert(DataBase database)
    throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {
    
    String sql="";
    
    sql+=" INSERT INTO dispositivo (Nome,Costo)"
        +" VALUES ('"+Nome+"',"+Costo+")";
    database.modify(sql);
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+="DELETE FROM dispositivo WHERE IdDispositivo="+IdDispositivo;
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE dispositivo "+
                " SET Nome = '" + Nome + "', " +                
                " Costo = " + Costo + ", " + 
                " WHERE IdDispositivo = "+IdDispositivo;
        
        database.modify(sql);  
    } 
}
