/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package prevblogics;

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
public static void mailConfirm(String mail, String messaggio)
    throws NotFoundDBException,ResultSetDBException, IOException, MessagingException {
  
       
        
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.unife.it");
        //props.put("mail.smtp.socketFactory.port", "25");
        //props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.port", "25");
       
        
        Session session =Session.getDefaultInstance(props,new Authenticator() {
             protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication("posenato.angela@gmail.com","92FE1008"); 
             }
        });
        
       
        Message message = new MimeMessage(session);
        InternetAddress from = new InternetAddress("erika.foli@unife.it");
        InternetAddress to[] = InternetAddress.parse(mail);
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

public static ArrayList<User> Search(DataBase database, String telefono)
    throws NotFoundDBException,ResultSetDBException {

    ArrayList<User> u = new ArrayList<User>();
    Boolean c = false;
    int i = 0;

    String sql=" SELECT * " +
                "   FROM utente " +
                " WHERE " +
                "   telefono='"+telefono+"'";
    
    ResultSet resultSet = database.select(sql);
    try {
      while (resultSet.next()) { 
          User us = new User(resultSet);
          u.add(us);
         
      }
      resultSet.close();
    } catch (SQLException ex) {
      throw new ResultSetDBException("UserService: Search():  ResultSetDBException: "+ex.getMessage(), database);
    }
 
return u;   
}
public static ArrayList<User> View(DataBase database)
    throws NotFoundDBException,ResultSetDBException 
{
    Field1 f;
    ArrayList<User> user = new ArrayList<User>();
    Boolean c = false;
    int i = 0;

    String sql=" SELECT * " +
                "   FROM utente "
            + "ORDER BY cognome" ;
    
    ResultSet resultSet = database.select(sql);
    try 
    {
        while (resultSet.next()) 
        { 
            
            user.add(new User(resultSet));
            
        }
        resultSet.close();
    } catch (SQLException ex) 
    {
      throw new ResultSetDBException("UserService: View():  ResultSetDBException: "+ex.getMessage(), database);
    }
    return user;
}
public static User InsertNewUser(DataBase database,String nome,String cognome,String mail,String tel) 
            throws NotFoundDBException,
            DuplicatedRecordDBException,ResultSetDBException 
{
       
        User utente;
        System.out.println("sono nell'userservice"+nome+cognome+"#"+mail+tel);
        utente = new User(nome,cognome,tel,mail);
        
        utente.insert(database);
        System.out.println("user inserito");
        return utente;
        
    }
public static void delete(DataBase database,String tel) throws NotFoundDBException 
 {
     String sql="DELETE FROM utente WHERE telefono='"+tel+"'";
     database.modify(sql); 
 }
public static void editUser(DataBase database,String telefono,String nome,String cognome,String mail) throws NotFoundDBException
{
    System.out.println(telefono+nome+" "+cognome+" "+mail);
    String sql="UPDATE utente SET nome='"+nome+"',cognome='"+cognome+"' ,mail='"+mail+"'  WHERE telefono='"+telefono+"'";
    database.modify(sql);
}
}