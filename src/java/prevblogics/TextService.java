
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

public class TextService {
    public TextService(){}
    
    public static Text InsertMail(DataBase database,String mail,String testo,String data) 
            throws NotFoundDBException,
            DuplicatedRecordDBException,ResultSetDBException 
    {
       
        Text t;
        
        t = new Text(mail,testo,data);
        
        t.insert(database);
        System.out.println("mail inserita");
        return t;
        
    }
    public static ArrayList<Text> Search(DataBase database, String mail,String telefono)
    throws NotFoundDBException,ResultSetDBException {

    ArrayList<Text> t = new ArrayList<Text>();
    Boolean c = false;
    int i = 0;
System.out.println(telefono);
    String sql=" SELECT * " +
                "   FROM text " +
                " WHERE " +
                "   mail='"+mail+"' AND testo LIKE '%" +telefono+"%'";
    
    ResultSet resultSet = database.select(sql);
    try {
      while (resultSet.next()) { 
          Text text = new Text(resultSet);
          t.add(text);
         
      }
      resultSet.close();
    } catch (SQLException ex) {
      throw new ResultSetDBException("TextService: Search():  ResultSetDBException: "+ex.getMessage(), database);
    }
 
return t;   
}
    
}
