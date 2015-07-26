/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bflows;

import blogics.Mail;
import blogics.MailService;
import blogics.TelefonoService;
import blogics.User;
import blogics.UserService;
import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import services.databaseservice.DBService;
import services.databaseservice.DataBase;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;
import services.errorservice.EService;

/**
 *
 * @author Massa
 */
public class MailManagement implements Serializable {
    public String userId;
    public ArrayList<User> utenti;
    public ArrayList<Mail> emails;
    
    private String errorMessage;
    private int result;
    
    public MailManagement(){}
    
    public void view()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            utenti = UserService.getUtenti(database);
            
            if(userId != null)
                emails = MailService.getEmailsByUserId(database, userId);
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
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
    
    
    
    
    public String getUserId()
    {
        return userId;
    }
    
    public void setUserId(String email)
    {
        this.userId = email;
    }
    
    public User getUtente(int i)
    {
        return utenti.get(i);
    }
    
    public void setUtente(int i,User utente)
    {
        utenti.set(i, utente);
    }
    
    public ArrayList<User> getUtenti()
    {
        return utenti;
    }
    
    public void setUtenti(ArrayList<User> utenti)
    {
        this.utenti = utenti;
    }
    
    public Mail getEmail(int i)
    {
        return emails.get(i);
    }
    
    public void setEmail(int i,Mail email)
    {
        emails.set(i, email);
    }
    
    public ArrayList<Mail> getEmails()
    {
        return emails;
    }
    
    public void setEmails(ArrayList<Mail> emails)
    {
        this.emails = emails;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String getErrorMessage() 
    {
        return this.errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) 
    {
        this.errorMessage = errorMessage;
    }
    
    public void setResult(int result) 
    {
        this.result = result;
    }
    
    public int getResult() 
    {
        return result;
    }
}
