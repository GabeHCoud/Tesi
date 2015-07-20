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
    
    public static ArrayList<Dispositivo> getDispositiviByEmail(DataBase database, String Email)
    throws NotFoundDBException,ResultSetDBException 
    {
        ArrayList<Dispositivo> d = new ArrayList<Dispositivo>();   

        String sql=" SELECT * " +
                    "   FROM dispositivo " +
                    " WHERE " +
                    "   Email='"+Email+"'";

        ResultSet resultSet = database.select(sql);
        try {
          while (resultSet.next()) { 
              Dispositivo dis = new Dispositivo(resultSet);
              d.add(dis);

          }
          resultSet.close();
        } catch (SQLException ex) {
          throw new ResultSetDBException("DispositivoService: getDispositiviByEmail():  ResultSetDBException: "+ex.getMessage(), database);
        }

        return d;   
    }
    
    public static Dispositivo insertNewDispositivo(DataBase database,String Nome,double Costo,String Email) 
            throws NotFoundDBException,
            DuplicatedRecordDBException,ResultSetDBException 
    {       
        Dispositivo disp;
        
        disp = new Dispositivo(Nome,Costo,Email);
        disp.insert(database);
        
        return disp;        
    }  
}
