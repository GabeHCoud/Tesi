/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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


public class UserService {   

    public static User getUtenteByEmail(DataBase database, String Email)
        throws NotFoundDBException,ResultSetDBException {

        User user;

        String sql=" SELECT * " +
                    "   FROM utente " +
                    " WHERE " +
                    "   Email='"+Email+"'";

        ResultSet resultSet = database.select(sql);
        try {
            if (resultSet.next()) {
              user=new User(resultSet);
            } else 
                return null; 
        } catch (SQLException ex) {
            throw new ResultSetDBException("FatturaService: getFatturaByTelefonoAndData():  Errore nel ResultSet: "+ex.getMessage(),database);
        }

        return user;
    }
    
    public static ArrayList<User> getUtenti(DataBase database)
        throws NotFoundDBException,ResultSetDBException 
    {        
        User user;
        ArrayList<User> users = new ArrayList<User>();
        String sql = "";
        
        sql+=   " SELECT * " +
                " FROM utente "
                + "ORDER BY cognome" ;

        ResultSet resultSet = database.select(sql);
        try 
        {
            while (resultSet.next()) 
            { 
                user= new User(resultSet);
                users.add(user);
            }
            resultSet.close();
        } catch (SQLException ex) 
        {
          throw new ResultSetDBException("UserService: getUtenti():  ResultSetDBException: "+ex.getMessage(), database);
        }
        return users;
    }
    
    public static User InsertNewUtente(DataBase database,String Email,String Nome,String Cognome) 
                throws NotFoundDBException,
                DuplicatedRecordDBException,ResultSetDBException 
    {
        User utente;
        
        utente = new User(Email,Nome,Cognome);
        utente.insert(database);

        return utente;
    }
}