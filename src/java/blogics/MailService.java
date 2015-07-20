
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

public class MailService {
    
    public static ArrayList<Mail> getEmailsByData(DataBase database, String Data)
        throws NotFoundDBException,ResultSetDBException 
    {
        ArrayList<Mail> t = new ArrayList<Mail>();
        String sql = "";    
        sql+=   " SELECT * " +
                " FROM mail " +
                " WHERE Data='"+Data+"'";      

        ResultSet resultSet = database.select(sql);
        try {
            while (resultSet.next()) { 
                Mail text = new Mail(resultSet);
                t.add(text);         
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw new ResultSetDBException("MailService: getEmailsByData():  ResultSetDBException: "+ex.getMessage(), database);
        }

        return t;   
    }
    
    public static Mail InsertMail(DataBase database,String Data,String Testo,String Email) 
            throws NotFoundDBException,
            DuplicatedRecordDBException,ResultSetDBException 
    {       
        Mail t;        
        
        t = new Mail(Data,Testo,Email);        
        t.insert(database);
        
        return t;        
    }    
}
