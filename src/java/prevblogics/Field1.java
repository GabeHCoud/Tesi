package prevblogics;

import java.sql.*;

import services.databaseservice.*;
import services.databaseservice.exception.*;
/**
 *
 * @author Angela
 */
public class Field1 {
    public String telefono;
    public double contributi;
    public double aaa;
    public double totale;
    public double abb;
    public String data;
    
    public Field1(String telefono,double contributi,double aaa,double abb,double totale,String data)
    {
        this.telefono=telefono;
        this.contributi=contributi;
        this.aaa=aaa;
        this.abb=abb;
        this.totale=totale;
        this.data=data;
    }
    
    public Field1(ResultSet resultSet)
    {
        try {telefono=resultSet.getString("telefono");} catch (SQLException sqle) {}
        try {contributi=resultSet.getDouble("crb");} catch(SQLException sqle) {}
        try {aaa=resultSet.getDouble("aaa");} catch(SQLException sqle) {}
        try {abb=resultSet.getDouble("abb");} catch(SQLException sqle) {}
        try {totale=resultSet.getDouble("totale");} catch(SQLException sqle) {}
        try {data=resultSet.getString("data");} catch (SQLException sqle) {}
    }
    public void insert(DataBase database) throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException 
    {
        String sql="";
        ResultSet resultSet;
        
        //check unicità
        sql=" INSERT INTO tab_1 "+
        "   ( telefono,"+
        "     crb,"+
        "     aaa,abb,totale,data "+
        "   ) "+
        " VALUES ('"+telefono+"','"+contributi+"','"+aaa+"','"+abb+"','"+totale+"','"+data+"')";
    System.out.println("inserito\n");
    database.modify(sql);
        
    }
} 
    

