package blogics;

import java.sql.*;

import java.sql.*;
import services.databaseservice.*;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;


public class Mail {
    public String Data;
    public String Testo;
    public int IdFattura;
    public String Email;
    
    public Mail(){}
     
    public Mail(String Data,String Testo,int IdFattura,String Email)
    {
        this.Data=Data;
        this.Testo=Testo;
        this.IdFattura = IdFattura;
        this.Email=Email;
    }
    
    public Mail(ResultSet resultSet) 
    {
        try {Data=resultSet.getString("Data");} catch (SQLException sqle) {}
        try {Testo=resultSet.getString("Testo");} catch (SQLException sqle) {}
        try {IdFattura=resultSet.getInt("IdFattura");} catch (SQLException sqle) {} 
        try {Email=resultSet.getString("Email");} catch (SQLException sqle) {}
    }
    
    public void insert(DataBase database)
    throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {
    
    String sql="";
    
    sql+=" INSERT INTO mail(Data,Testo,IdFattura,Email)"
        +" VALUES ('"+Data+"','"+Testo+"',"+IdFattura+",'"+Email+"')";
    database.modify(sql);
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+=   " DELETE FROM mail "+
                " WHERE Data='"+Data+"'"+
                " AND Email='"+Email+"'"+
                " AND IdFattura="+IdFattura;        
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE mail "+
                " SET Testo = '" + Testo + "'" +      
                " WHERE Data='"+Data+"'"+
                " AND Email='"+Email+"'"+
                " AND IdFattura="+IdFattura;   
        
        database.modify(sql);  
    }   
} 

