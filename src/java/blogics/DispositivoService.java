package blogics;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import services.databaseservice.DataBase;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;

public class DispositivoService {
    
    public static ArrayList<Dispositivo> getDispositivi(DataBase database)
    throws NotFoundDBException,ResultSetDBException 
    {
        ArrayList<Dispositivo> d = new ArrayList<Dispositivo>();   

        String sql=" SELECT * FROM dispositivo";                    

        ResultSet resultSet = database.select(sql);
        try {
          while (resultSet.next()) { 
              Dispositivo dis = new Dispositivo(resultSet);
              d.add(dis);

          }
          resultSet.close();
        } catch (SQLException ex) {
          throw new ResultSetDBException("DispositivoService: getDispositivi():  ResultSetDBException: "+ex.getMessage(), database);
        }

        return d;   
    }
    
    public static Dispositivo getDispositivoById(DataBase database,int IdDispositivo) throws ResultSetDBException, NotFoundDBException
    {
        Dispositivo dispositivo;
        
        String sql = "";
        sql += " SELECT * FROM dispositivo" +
               " WHERE IdDispositivo="+IdDispositivo;              
        
        ResultSet resultSet=database.select(sql);    
        try {
            if (resultSet.next()) {
              dispositivo=new Dispositivo(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("DispositivoService: getDispositivoById():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return dispositivo;
    }
    
    public static Dispositivo insertNewDispositivo(DataBase database,String Nome,double Costo) 
            throws NotFoundDBException,
            DuplicatedRecordDBException,ResultSetDBException 
    {       
        Dispositivo disp;
        
        disp = new Dispositivo(Nome,Costo);
        disp.insert(database);
        
        return disp;        
    }  
}
