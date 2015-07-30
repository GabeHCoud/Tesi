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


public class UserContributo {
    public int IdUtenteContributo;
    public String Email;
    public int IdContributo;
    
    public UserContributo (){}
    
    public UserContributo(String Email,int IdContributo)
    {
        this.Email=Email;
        this.IdContributo=IdContributo;
    }
    
    public UserContributo(ResultSet resultSet)
    {
        try {IdUtenteContributo=resultSet.getInt("IdUtenteContributo");} catch(SQLException sqle) {}
        try {Email=resultSet.getString("Email");} catch (SQLException sqle) {}
        try {IdContributo=resultSet.getInt("IdContributo");} catch(SQLException sqle) {}
    }
    
    public void insert(DataBase database)
    throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {
    
    String sql="";
    
    sql+=" INSERT INTO utente_contributo (Email,IdContributo)"
        +" VALUES ('"+Email+"',"+IdContributo+")";
    database.modify(sql);
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+="DELETE FROM utente_contributo WHERE IdUtenteContributo="+IdUtenteContributo;
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE utente_contributo "+
                " SET Email = '" + Email + "', " +                
                " IdContributo = " + IdContributo + ", " + 
                " WHERE IdUtenteContributo = "+IdUtenteContributo;
        
        database.modify(sql);  
    } 
}
