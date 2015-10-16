package blogics;

import java.sql.*;

import services.databaseservice.*;
import services.databaseservice.exception.*;
import util.Conversion;

public class Consumo {    
    public String Telefono;    
    public double CRB;
    public double AAA;
    public double ABB;
    public double Totale;
    public String Email;
    public int IdFattura;
    
    public Consumo(){}
    
    public Consumo(String Telefono, double CRB, double AAA, double ABB, double Totale,String Email,int IdFattura)
    {
        this.Telefono=Telefono;
        this.CRB=CRB;
        this.AAA=AAA;
        this.ABB=ABB;
        this.Totale=Totale;
        this.Email = Email;
        this.IdFattura = IdFattura;
    }
    
    public Consumo(ResultSet resultSet)
    {
        try {Telefono=resultSet.getString("Telefono");} catch (SQLException sqle) {}
        try {CRB=resultSet.getDouble("CRB");} catch(SQLException sqle) {}
        try {AAA=resultSet.getDouble("AAA");} catch(SQLException sqle) {}
        try {ABB=resultSet.getDouble("ABB");} catch(SQLException sqle) {}
        try {Totale=resultSet.getDouble("Totale");} catch(SQLException sqle) {}
        try {Email=resultSet.getString("Email");} catch (SQLException sqle) {}
        try {IdFattura=resultSet.getInt("IdFattura");} catch (SQLException sqle) {}
    }
    
    public void insert(DataBase database) throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException 
    {
        String sql="";        

        if(Email != null)
            sql+=   " INSERT INTO consumo "+
                    " (Telefono,CRB,AAA,ABB,Totale,Email,IdFattura)"+       
                    " VALUES('"+Conversion.getDatabaseString(Telefono)+"',"+CRB+","+AAA+","+ABB+","+Totale+",'"+Conversion.getDatabaseString(Email)+"'"+","+IdFattura+")";
        else
            sql+=   " INSERT INTO consumo "+
                    " (Telefono,CRB,AAA,ABB,Totale,IdFattura)"+       
                    " VALUES('"+Conversion.getDatabaseString(Telefono)+"',"+CRB+","+AAA+","+ABB+","+Totale+","+IdFattura+")";
        
        database.modify(sql);        
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+= "DELETE FROM consumo WHERE Telefono='"+Telefono+"'"+
              "AND IdFattura = "+IdFattura;
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE consumo "+
                " SET CRB=" + CRB + 
                " ,AAA=" + AAA + 
                " ,ABB=" + ABB + 
                " ,Totale=" + Totale + 
                " ,Email='" + Conversion.getDatabaseString(Email) + "'" +
                " WHERE Telefono='"+Conversion.getDatabaseString(Telefono)+"'"+
                " AND IdFattura = "+IdFattura;
        
        database.modify(sql);  
    }
} 
    

