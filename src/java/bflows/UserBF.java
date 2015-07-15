package bflows;

import blogics.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
//import java.util.Date;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import services.databaseservice.*;
import services.databaseservice.exception.*;
import services.errorservice.*;
public class UserBF {
    private String telefono;
    private String mail;
    private String nome;
    private String cognome;
    private String errorMessage;
    private int result;
    private String nome_d;
    private double costo_d;
    private Long code;
    private User[] user;
    private Text[] text;
    private Dispositivo[] dispositivo;

    public void Register(String tel) 
    {
        
        DataBase database=null;
        System.out.println("\nBF");
        try{
            database=DBService.getDataBase();
            User utente=UserService.InsertNewUser(database,nome,cognome,mail,tel);
            System.out.println("user inserito2");
            database.commit();
            
       } catch (NotFoundDBException ex) {
      
      EService.logAndRecover(ex);
      setResult(EService.UNRECOVERABLE_ERROR);
      if(database!=null)
          database.rollBack();
      
    } catch (ResultSetDBException ex) {
      
      EService.logAndRecover(ex);
      setResult(EService.UNRECOVERABLE_ERROR);
      if(database!=null)
          database.rollBack();
      
    } catch (DuplicatedRecordDBException ex) {
      
      EService.logAndRecover(ex);
      setResult(EService.RECOVERABLE_ERROR);
      setErrorMessage("L'utente inserito e gia' esistente.");
      if(database!=null)
          database.rollBack();  
      
    } finally {
      try { if(database!=null)
              database.close(); }
      catch (NotFoundDBException e) 
      { EService.logAndRecover((GeneralException) e); }
    }
    
  }
    public void insert_disp(String nome_d,Double costo_d,String mail) 
    {
        
        DataBase database=null;
        System.out.println("\nBF");
        try{
            database=DBService.getDataBase();
            Dispositivo dis=DispositivoService.InsertNewDis(database, nome_d, costo_d, mail);
            System.out.println("Dis inserito");
            database.commit();
            
       } catch (NotFoundDBException ex) {
      
      EService.logAndRecover(ex);
      setResult(EService.UNRECOVERABLE_ERROR);
      if(database!=null)
          database.rollBack();
      
    } catch (ResultSetDBException ex) {
      
      EService.logAndRecover(ex);
      setResult(EService.UNRECOVERABLE_ERROR);
      if(database!=null)
          database.rollBack();
      
    } catch (DuplicatedRecordDBException ex) {
      
      EService.logAndRecover(ex);
      setResult(EService.RECOVERABLE_ERROR);
      setErrorMessage("Il dispositivo inserito e gia' esistente.");
      if(database!=null)
          database.rollBack();  
      
    } finally {
      try { if(database!=null)
              database.close(); }
      catch (NotFoundDBException e) 
      { EService.logAndRecover((GeneralException) e); }
    }
    
  }
    public void SearchMail(String telefono) {
        
        DataBase database = null;
        
        ArrayList<Text> t = new ArrayList<Text>();
        try {

          database=DBService.getDataBase();
         
          t = TextService.Search(database,mail,telefono);
          text = new Text[t.size()];
          t.toArray(text);

          database.commit();

        }catch (NotFoundDBException ex) {

          EService.logAndRecover(ex);

        } catch (ResultSetDBException ex) {

          EService.logAndRecover(ex);

        }  finally {
          try { database.close(); }
          catch (NotFoundDBException e) { EService.logAndRecover(e); }
        }
        
    }
    public void SearchDisp(String mail) {
        
        DataBase database = null;
        
        ArrayList<Dispositivo> d = new ArrayList<Dispositivo>();
        try {

          database=DBService.getDataBase();
         
          d = DispositivoService.Search(database,mail);
          dispositivo = new Dispositivo[d.size()];
          d.toArray(dispositivo);
          
          database.commit();

        }catch (NotFoundDBException ex) {

          EService.logAndRecover(ex);

        } catch (ResultSetDBException ex) {

          EService.logAndRecover(ex);

        }  finally {
          try { database.close(); }
          catch (NotFoundDBException e) { EService.logAndRecover(e); }
        }
        
    }
    
    public void SearchUser(String telefono) {
        
        DataBase database = null;
        
        ArrayList<User> u = new ArrayList<User>();
        try {

          database=DBService.getDataBase();
         
          u = UserService.Search(database,telefono);
          user = new User[u.size()];
          u.toArray(user);
          
          database.commit();

        }catch (NotFoundDBException ex) {

          EService.logAndRecover(ex);

        } catch (ResultSetDBException ex) {

          EService.logAndRecover(ex);

        }  finally {
          try { database.close(); }
          catch (NotFoundDBException e) { EService.logAndRecover(e); }
        }
        
    }
    public void ViewUser()
    {
        DataBase database=null;
        
        ArrayList<User> u = new ArrayList<User>();
        try {

          database=DBService.getDataBase();
          u = UserService.View(database);
          user = new User[u.size()];
          u.toArray(user);

          database.commit();

        }catch (NotFoundDBException ex) {

          EService.logAndRecover(ex);

        } catch (ResultSetDBException ex) {

          EService.logAndRecover(ex);

        }  finally {
          try { database.close(); }
          catch (NotFoundDBException e) { EService.logAndRecover(e); }
        }
    }
    public void delete(String tel)
{
    DataBase database = null;
    try 
    {
        database=DBService.getDataBase();
        UserService.delete(database,tel);
        database.commit();

        } catch (NotFoundDBException ex) {

          EService.logAndRecover(ex);
          if(database!=null)
          database.rollBack();

        }  finally {
          try { database.close(); }
          catch (NotFoundDBException e) { EService.logAndRecover(e); }
        }
}
    public void delete_disp(Long code)
{
    DataBase database = null;
    try 
    {
        database=DBService.getDataBase();
        DispositivoService.delete(database,code);
        
        database.commit();

        } catch (NotFoundDBException ex) {

          EService.logAndRecover(ex);
          if(database!=null)
          database.rollBack();

        }  finally {
          try { database.close(); }
          catch (NotFoundDBException e) { EService.logAndRecover(e); }
        }
}
    public void edit()
{

    System.out.println("BF\n");
    DataBase database = null;

    try {
        database=DBService.getDataBase();
        System.out.println("edit");
        UserService.editUser(database,telefono,nome,cognome,mail);
        System.out.println("tornato in BF\n");
        database.commit();

        } catch (NotFoundDBException ex) {

          EService.logAndRecover(ex);
          if(database!=null)
          database.rollBack();

        }  finally {
          try { database.close(); }
          catch (NotFoundDBException e) { EService.logAndRecover(e); }
        }
}
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setResult(int result) {
        this.result = result;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public int getResult() {
        return result;
    }

    public User[] getUser() {
        return user;
    }
     public User getUser(int i){
        return user[i];
    }
     public void setUser(User[] user) {
        this.user = user;
    }
     public void setDispositivo(Dispositivo[] dispositivo){
         this.dispositivo=dispositivo;
     }

    public String getTelefono() {
        return telefono;
    }

    public String getMail() {
        return mail;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public Text[] getText() {
        return text;
    }
    public Text getText(int i) {
        return text[i];
    }
     public Dispositivo[] getDispositivo() {
        return dispositivo;
    }
    public Dispositivo getDispositivo(int i) {
        return dispositivo[i];
    }
}
