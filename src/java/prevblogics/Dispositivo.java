package prevblogics;

import java.sql.*;

import java.sql.*;
import services.databaseservice.*;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;

public class Dispositivo {
    public Long code;
    public String nome_d;
    public double costo_d;
    public String mail;
    
    public Dispositivo(Long code,String nome_d,double costo_d,String mail)
    {
        this.code=code;
        this.nome_d=nome_d;
        this.costo_d=costo_d;
        this.mail=mail;
    }
    
    public Dispositivo(ResultSet resultSet)
    {
        try {code=resultSet.getLong("code");} catch(SQLException sqle) {}
        try {nome_d=resultSet.getString("nome_d");} catch (SQLException sqle) {}
        try {costo_d=resultSet.getDouble("costo_d");} catch (SQLException sqle) {}
        try {mail=resultSet.getString("mail");} catch (SQLException sqle) {}
    }
    
    public void insert(DataBase database)
    throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {
    
    String sql="";
    System.out.println(nome_d);
    sql=" INSERT INTO dispositivo(code,nome_d,costo_d,mail)"
        +" VALUES ('"+code+"','"+nome_d+"','"+costo_d+"','"+mail+"')";
    database.modify(sql);
    System.out.println("ho modificato");
    }
}
