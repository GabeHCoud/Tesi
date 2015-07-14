package blogics;

import java.sql.*;

import services.databaseservice.*;
import services.databaseservice.exception.*;
/**
 *
 * @author Angela
 */
public class Fattura {
    public String Telefono;
    public String Data;
    public double CRB;
    public double AAA;
    public double ABB;
    public double Totale;
    
    public Fattura(){}
    
    public Fattura(String Telefono, String Data, double CRB, double AAA, double ABB, double Totale)
    {
        this.Telefono=Telefono;
        this.Data=Data;
        this.CRB=CRB;
        this.AAA=AAA;
        this.ABB=ABB;
        this.Totale=Totale;
    }
    
    public Fattura(ResultSet resultSet)
    {
        try {Telefono=resultSet.getString("Telefono");} catch (SQLException sqle) {}
        try {Data=resultSet.getString("Data");} catch (SQLException sqle) {}
        try {CRB=resultSet.getDouble("CRB");} catch(SQLException sqle) {}
        try {AAA=resultSet.getDouble("AAA");} catch(SQLException sqle) {}
        try {ABB=resultSet.getDouble("ABB");} catch(SQLException sqle) {}
        try {Totale=resultSet.getDouble("Totale");} catch(SQLException sqle) {}
    }
    public void insert(DataBase database) throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException 
    {
        String sql="";        

        sql+=" INSERT INTO tab_1 "+
        "(Telefono,Data,CRB,AAA,ABB,Totale)"+       
        " VALUES('"+Telefono+"','"+Data+"',"+CRB+","+AAA+","+ABB+","+Totale+")";
    
        database.modify(sql);        
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+= "DELETE FROM fattura WHERE Telefono='"+Telefono+"'"+
              "AND Data = '"+Data+"'";
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE fattura "+
                " SET CRB = " + CRB + "," + 
                " AAA = " + AAA + "," + 
                " ABB = " + ABB + "," + 
                " Totale = " + Totale +
                " WHERE Telefono='"+Telefono+"'"+
                " AND Data = '"+Data+"'";
        
        database.modify(sql);  
    } 
} 
    

