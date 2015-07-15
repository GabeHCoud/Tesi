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
//import services.sessionservice.Session;

public class FieldBF {
    
private String telefono; 
private double contributi;
private double aaa;
private double abb;
private double totale;
private String data;
private String att;
private String dis;
private String piano;
private String nome;
private String cognome;
private String mail;
private String errorMessage;
private int result;
private Field1[] field;
private User[] user;


public void insert(String telefono,double contributi,double aaa,double abb,double totale,String data) 
{

    DataBase database = null;
    try 
    {
        database=DBService.getDataBase();
        Field1Service.insert(database,telefono,contributi,aaa,abb,totale,data);
        
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
        EService.logAndRecover(ex);
        setResult(EService.RECOVERABLE_ERROR);
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
public void delete()
{
    DataBase database = null;
    try 
    {
        database=DBService.getDataBase();
        Field1Service.delete(database);
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
public void DataFattura(String data)
{

    System.out.println("BF\n");
    DataBase database = null;

    try {
        database=DBService.getDataBase();
        System.out.println();
        Field1Service.modifyField(database,data);
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
    


}public void edit()
{

    System.out.println("BF\n");
    DataBase database = null;

    try {
        database=DBService.getDataBase();
        System.out.println();
        Field1Service.editField(database,telefono,contributi,aaa,abb,totale);
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
public void SearchUser(String telefono) {
        
        DataBase database = null;
        
        ArrayList<User> u = new ArrayList<User>();
        try {

          database=DBService.getDataBase();
         
          u = UserService.Search(database,telefono);
          user = new User[u.size()];
          if(u.size()!=0)
                u.toArray(user);
          System.out.println("trovato"+u);
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

          database=DBService.getDataBase();
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

public void SearchF1(String telefono) {
        
        DataBase database = null;
        
        ArrayList<Field1> f = new ArrayList<Field1>();
        try {

          database=DBService.getDataBase();
         
          f = Field1Service.Search(database,telefono);
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


    /**
     * Funzione che resituisce l'Anno
     * @return l'Anno
     */
    public int getYear() {
        return new Field1Service().getYear();
    }
    
    /**
     * Funzione che restituisce la Data
     * @param data parametro per i dati della Data
     * @return la Data
     */
    public String getDate(int data) {
        return new Field1Service().getDate(data);
    }
    public String getTelefono() {
        return telefono;
    }
    public String getData() {
        return data;
    }

    public double getContributi() {
        return contributi;
    }

    public double getAaa() {
        return aaa;
    }
    public double getAbb() {
        return abb;
    }

    public double getTotale() {
        return totale;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public void setResult(int result) {
        this.result = result;
    }
    public int getResult() {
        return result;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public void setData(String data) {
        this.data = data;
    }

    public void setContributi(double contributi) {
        this.contributi = contributi;
    }

    public void setAaa(double aaa) {
        this.aaa = aaa;
    }
    public void setAbb(double abb) {
        this.abb = abb;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public Field1[] getField1() {
        return field;
    }
    public Field1 getField1(int i) {
        return field[i];
    }
    
    public User getUser(int i)
    {
        return user[i];
    }
    public User[] getUser(){
        return user;
    }

    public String getAtt() {
        return att;
    }

    public String getDis() {
        return dis;
    }

    public String getPiano() {
        return piano;
    }

    public Field1[] getField() {
        return field;
    }

    public void setAtt(String att) {
        this.att = att;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public void setPiano(String piano) {
        this.piano = piano;
    }

    public void setField(Field1[] field) {
        this.field = field;
    }

    public void setUser(User[] user) {
        this.user = user;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getMail() {
        return mail;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

}
