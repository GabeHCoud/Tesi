
package blogics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import services.databaseservice.DataBase;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;

public class FondoService {
    
    public static Fondo getFondoById(DataBase database,int id)
    throws NotFoundDBException,ResultSetDBException {

        Fondo fondo;

        String sql=" SELECT * " +
                    " FROM fondo " +
                    " WHERE " +
                    " IdFondo="+id;

        ResultSet resultSet = database.select(sql);
        try {
            if (resultSet.next()) {
              fondo=new Fondo(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("FondoService: getFondoById():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return fondo;
    }
    
    public static ArrayList<Fondo> getFondi(DataBase database)
    throws NotFoundDBException,ResultSetDBException {

        Fondo fondo;
        ArrayList<Fondo> fondi = new ArrayList<Fondo>();
        String sql= " SELECT * " +
                    " FROM fondo ";

        ResultSet resultSet = database.select(sql);
        try {
            while (resultSet.next()) 
            { 
                fondo= new Fondo(resultSet);
                fondi.add(fondo);
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw new ResultSetDBException("FondoService: getFondi():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return fondi;
    }
    
    public static ArrayList<Fondo> getFondiByEmail(DataBase database,String Email)
    throws NotFoundDBException,ResultSetDBException {

        Fondo fondo;
        ArrayList<Fondo> fondi = new ArrayList<Fondo>();
        String sql= " SELECT * " +
                    " FROM fondo " +
                    " WHERE " +
                    " Email='"+Email+"'";

        ResultSet resultSet = database.select(sql);
        try {
            while (resultSet.next()) 
            { 
                fondo= new Fondo(resultSet);
                fondi.add(fondo);
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw new ResultSetDBException("FondoService: getFondiByEmail():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return fondi;
    }
    
    public static Fondo InsertNewFondo(DataBase database,String Email,String Nome,boolean Attivo) 
                throws NotFoundDBException,
                DuplicatedRecordDBException,ResultSetDBException 
    {
        Fondo fondo;
        
        fondo = new Fondo(Email,Nome,Attivo);
        fondo.insert(database);

        return fondo;
    }
    
}
