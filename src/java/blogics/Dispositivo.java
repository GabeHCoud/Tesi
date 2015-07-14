package blogics;

import java.sql.*;

import java.sql.*;
import services.databaseservice.*;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;

public class Dispositivo {
    public int Id;
    public String Nome;
    public double Costo;
    public String Email;
    
    public Dispositivo (){}
    
    public Dispositivo(String Nome,double Costo,String Email)
    {
        this.Nome=Nome;
        this.Costo=Costo;
        this.Email=Email;
    }
    
    public Dispositivo(ResultSet resultSet)
    {
        try {Id=resultSet.getInt("Id");} catch(SQLException sqle) {}
        try {Nome=resultSet.getString("Nome");} catch (SQLException sqle) {}
        try {Costo=resultSet.getDouble("Costo");} catch (SQLException sqle) {}
        try {Email=resultSet.getString("Email");} catch (SQLException sqle) {}
    }
    
    public void insert(DataBase database)
    throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {
    
    String sql="";
    
    sql+=" INSERT INTO dispositivo(Nome,Costo,Email)"
        +" VALUES ('"+Nome+"',"+Costo+",'"+Email+"')";
    database.modify(sql);
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+="DELETE FROM dispositivo WHERE ID="+Id;
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=   " UPDATE dispositivo "+
                " SET Nome = '" + Nome + "', " +                
                " Costo = " + Costo + ", " + 
                " WHERE ID = "+Id;
        
        database.modify(sql);  
    } 
}
