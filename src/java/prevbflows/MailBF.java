package prevbflows;

import prevblogics.*;
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
//import services.sessionservice.Session;
 public class MailBF
 {
     private String telefono;
     private String mail;
     private String errorMessage;
    private int result;
    private User[] user;
    private Field1[] field;
    private Text[] text;
     
     public void confirm(String mail,String messaggio,String data) throws IOException, MessagingException 
     {
         DataBase database = null;
         Text t=null;
         try {

          database=DBService.getDataBase("old");
          t = TextService.InsertMail(database,mail,messaggio,data);
          System.out.println("sono in bf");
          UserService.mailConfirm(mail,messaggio);
          database.commit();
    } 
    catch (NotFoundDBException ex) 
    {
        EService.logAndRecover(ex);
        setResult(EService.UNRECOVERABLE_ERROR);
        if(database!=null)
        database.rollBack();
    } 
    catch (DuplicatedRecordDBException ex) 
    {
        //EService.logAndRecover(ex);
        //setResult(EService.RECOVERABLE_ERROR);
        if(database!=null)
        database.rollBack();
    }
    catch (ResultSetDBException ex) 
    {
        EService.logAndRecover(ex);
        setResult(EService.UNRECOVERABLE_ERROR);
        if(database!=null)
        database.rollBack();
    }  
    finally 
    {
        try { database.close(); }
        catch (NotFoundDBException e) { EService.logAndRecover(e); }
    }   
    }
     public void Search() {
        
        DataBase database = null;
        
        ArrayList<User> u = new ArrayList<User>();
        try {

          database=DBService.getDataBase("old");
         
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

          database=DBService.getDataBase("old");
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
     public void SearchF1(String telefono) {
        
        DataBase database = null;
        
        ArrayList<Field1> f = new ArrayList<Field1>();
        try {

          database=DBService.getDataBase("old");
         
          f = Field1Service.Search(database,telefono);
          if(f!=null)
          {field = new Field1[f.size()];
          f.toArray(field);
          }
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

          database=DBService.getDataBase("old");
         
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
     public void View()
    {
        DataBase database=null;
        
        ArrayList<Field1> f = new ArrayList<Field1>();
        try {

          database=DBService.getDataBase("old");
          f = Field1Service.View(database);
          field = new Field1[f.size()];
          f.toArray(field);

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

    public String getTelefono() {
        return telefono;
    }

    public String getMail() {
        return mail;
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

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setUser(User[] user) {
        this.user = user;
    }
    public void setText(Text[] text) {
        this.text = text;
    }
    
    public Field1[] getField1() {
        return field;
    }
    public Field1 getField1(int i) {
        return field[i];
    }
    public User getUser(int i){
        return user[i];
    }
    public Text[] getText() {
        return text;
    }
    public Text getText(int i) {
        return text[i];
    }
     
 }