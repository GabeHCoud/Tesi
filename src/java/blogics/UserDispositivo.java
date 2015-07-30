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


public class UserDispositivo {
    
    public int IdUtenteDispositivo;
    public String Email;
    public int IdDispositivo;
    
    public UserDispositivo (){}
    
    public UserDispositivo(String Email,int IdDispositivo)
    {
        this.Email=Email;
        this.IdDispositivo=IdDispositivo;
    }
    
    public UserDispositivo(ResultSet resultSet)
    {
        try {IdUtenteDispositivo=resultSet.getInt("IdUtenteDispositivo");} catch(SQLException sqle) {}
        try {Email=resultSet.getString("Email");} catch (SQLException sqle) {}
        try {IdDispositivo=resultSet.getInt("IdDispositivo");} catch(SQLException sqle) {}
    }
    
    public void insert(DataBase database)
    throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {
    
    String sql="";
    
    sql+=" INSERT INTO utente_dispositivo (Email,IdDispositivo)"
        +" VALUES ('"+Email+"',"+IdDispositivo+")";
    database.modify(sql);
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+="DELETE FROM utente_dispositivo WHERE IdUtenteDispositivo="+IdUtenteDispositivo;
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE utente_dispositivo "+
                " SET Email = '" + Email + "', " +                
                " IdDispositivo = " + IdDispositivo + ", " + 
                " WHERE IdUtenteDispositivo = "+IdUtenteDispositivo;
        
        database.modify(sql);  
    } 
}
