
package blogics;

import java.sql.ResultSet;
import java.sql.SQLException;
import services.databaseservice.DataBase;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;
import util.Conversion;

public class Fondo {
    public int Id;
    public String Email;
    public String Nome;
    public boolean Attivo;
    
    public Fondo(){}
    
    public Fondo(String Email, String Nome,boolean Attivo)
    {
        this.Email = Email;
        this.Nome = Nome;
        this.Attivo = Attivo;
    }
    
    public Fondo(ResultSet resultSet)
    {
        try {Id=resultSet.getInt("Id");} catch (SQLException sqle) {}
        try {Email=resultSet.getString("Email");} catch (SQLException sqle) {}
        try {Nome=resultSet.getString("Nome");} catch (SQLException sqle) {}        
        try {Attivo=resultSet.getBoolean("Attivo");} catch (SQLException sqle) {}
    }
    
    public void insert(DataBase database)
        throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {

        String sql="";     
               
        sql=" INSERT INTO fondo(Email,Nome,Attivo)"
            +" VALUES ('"+Conversion.getDatabaseString(Email)+"','"
                +Conversion.getDatabaseString(Nome)+"', "+
                "Attivo"+")";
        database.modify(sql);
    }
    
    public void delete(DataBase database) 
            throws NotFoundDBException ,ResultSetDBException 
    {
        String sql = "";
        sql+=   " DELETE FROM fondo "+
                " WHERE Id="+Id;                
        
        database.modify(sql); 
    }
    
    public void update(DataBase database)
        throws NotFoundDBException,ResultSetDBException {
        
        String sql = "";
        sql +=  " UPDATE fondo "+
                " SET Nome = '" + Conversion.getDatabaseString(Nome) + "',"+
                " Email = '" + Conversion.getDatabaseString(Email) +"',"+
                " Attivo = " + Attivo+     
                " WHERE Id="+Id;        
        
        database.modify(sql);  
    }   
}
