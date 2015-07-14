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

/**
 *
 * @author Angela
 */
public class UserService {
    public UserService() {}   

    //UNI
    public static void mailConfirm(String Email, String messaggio)
        throws NotFoundDBException,ResultSetDBException, IOException, MessagingException 
    {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.unife.it");
        //props.put("mail.smtp.socketFactory.port", "25");
        //props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.port", "25");

        Session session = Session.getDefaultInstance(props,new Authenticator() {
             protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication("posenato.angela@gmail.com","92FE1008"); 
             }
        });

        Message message = new MimeMessage(session);
        InternetAddress from = new InternetAddress("erika.foli@unife.it");
        InternetAddress to[] = InternetAddress.parse(Email);
        //reply
        message.setFrom(from);
        message.setRecipients(Message.RecipientType.TO, to);
        message.setSubject("Fattura Telecom");
        message.setSentDate(new java.util.Date());
        message.setText(messaggio);
        message.setContent(messaggio, "text/html; charset=ISO-8859-1");
        Transport tr = session.getTransport("smtp");
        tr.connect("smtp.unife.it", "","");
        message.saveChanges();
        tr.sendMessage(message, message.getAllRecipients());
        tr.close(); 
    }

    //TEST
    /*public static void mailConfirm(String mail, String messaggio)
        throws NotFoundDBException,ResultSetDBException, IOException, MessagingException {



            Properties props = System.getProperties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");


            Session session =Session.getDefaultInstance(props,new Authenticator() {
                 protected PasswordAuthentication getPasswordAuthentication() {
                      return new PasswordAuthentication("posenato.angela@gmail.com","92FE1008"); 
                 }
            });


            Message message = new MimeMessage(session);
            InternetAddress from = new InternetAddress("posenato.angela@gmail.com");
            InternetAddress to[] = InternetAddress.parse(mail);
            message.setFrom(from);
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject("Fattura Telecom");
            message.setSentDate(new java.util.Date());
            message.setText(messaggio);
            message.setContent(messaggio, "text/html; charset=ISO-8859-1");
            Transport tr = session.getTransport("smtp");
            tr.connect("smtp.gmail.com", "posenato.angela@gmai.com","92FE1008");
            message.saveChanges();
            tr.sendMessage(message, message.getAllRecipients());
            tr.close(); 


            }*/

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