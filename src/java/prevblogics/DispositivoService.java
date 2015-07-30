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

public class DispositivoService {
    public DispositivoService(){}
    
    public static ArrayList<Dispositivo> Search(DataBase database, String mail)
    throws NotFoundDBException,ResultSetDBException {

    ArrayList<Dispositivo> d = new ArrayList<Dispositivo>();
    Boolean c = false;
    int i = 0;

    String sql=" SELECT * " +
                "   FROM dispositivo " +
                " WHERE " +
                "   mail='"+mail+"'";
    
    ResultSet resultSet = database.select(sql);
    try {
      while (resultSet.next()) { 
          Dispositivo dis = new Dispositivo(resultSet);
          d.add(dis);
         
      }
      resultSet.close();
    } catch (SQLException ex) {
      throw new ResultSetDBException("DispositivoService: Search():  ResultSetDBException: "+ex.getMessage(), database);
    }
 
return d;   
}
    public static Dispositivo InsertNewDis(DataBase database,String nome_d,double costo_d,String mail) 
            throws NotFoundDBException,
            DuplicatedRecordDBException,ResultSetDBException 
{
       
        Dispositivo disp;
        System.out.println("sono nel DispositivoService "+ nome_d);
        long code=0;
        disp = new Dispositivo(code,nome_d,costo_d,mail);
        System.out.println(disp.nome_d+" "+disp.mail);
        disp.insert(database);
        
        return disp;
        
    }
public static void delete(DataBase database,Long code) throws NotFoundDBException 
 {
     String sql="DELETE FROM dispositivo WHERE code='"+code+"'";
     database.modify(sql); 
 }

}
