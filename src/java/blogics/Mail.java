package blogics;

import java.sql.*;

import java.sql.*;
import services.databaseservice.*;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;
import util.Conversion;


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
    
    sql+=   " SELECT * FROM mail"+
            " WHERE IdFattura="+IdFattura+
            " AND Email='"+Conversion.getDatabaseString(Email)+"'";
    
    ResultSet resultSet=database.select(sql);
    
    try {
        if (resultSet.next()) {
            throw new DuplicatedRecordDBException("E' già stata inviata una email all'utente per la fattura selezionata.");
        } 
    } catch (SQLException ex) {
        throw new ResultSetDBException("Mail: insert():  Errore nel ResultSet: "+ex.getMessage(),database);
    }   
    
    
    sql =" INSERT INTO mail(Data,Testo,IdFattura,Email)"
        +" VALUES ('"+Conversion.getDatabaseString(Data)+"','"+Conversion.getDatabaseString(Testo)+"',"+IdFattura+",'"+Conversion.getDatabaseString(Email)+"')";
    database.modify(sql);
}
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+=   " DELETE FROM mail "+
                " WHERE Data='"+Conversion.getDatabaseString(Data)+"'"+
                " AND Email='"+Conversion.getDatabaseString(Email)+"'"+
                " AND IdFattura="+IdFattura;        
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE mail "+
                " SET Testo = '" + Conversion.getDatabaseString(Testo) + "'" +      
                " WHERE Data='"+Conversion.getDatabaseString(Data)+"'"+
                " AND Email='"+Conversion.getDatabaseString(Email)+"'"+
                " AND IdFattura="+IdFattura;   
        
        database.modify(sql);  
    }   
} 

