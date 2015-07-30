/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bflows;

import blogics.Contributo;
import blogics.ContributoService;
import blogics.Dispositivo;
import blogics.DispositivoService;
import blogics.User;
import blogics.UserContributo;
import blogics.UserContributoService;
import blogics.UserService;
import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import services.databaseservice.DBService;
import services.databaseservice.DataBase;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;
import services.errorservice.EService;


public class ContributiManagement implements Serializable {
    
    private String email;
    private ArrayList<Contributo> contributi;
    private ArrayList<UserContributo> ucontributi;
    private User utente;
    private int idContributo;
    private int idUserContributo;
    private String nome;
    private double costo;
    
     
    
    private String errorMessage;
    private int result;
    
    public ContributiManagement(){}
    
    public void view()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            contributi = ContributoService.getContributi(database);
            
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
    
    public void add()
    {        
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            ContributoService.insertNewContributo(database, nome, costo);
            
            database.commit();
            contributi = ContributoService.getContributi(database);
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            if(database!=null)
            database.rollBack();
        }catch (DuplicatedRecordDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.RECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }catch (ResultSetDBException ex) 
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
    
    public void delete()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            Contributo contributo = ContributoService.getContributoById(database,idContributo);
            contributo.delete(database);
            
            database.commit();
            contributi = ContributoService.getContributi(database);
            
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
    
    public void viewUserSubscriptions()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            utente = UserService.getUtenteByEmail(database, email);           
            ucontributi = UserContributoService.getUserContributiByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            
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
    
    public void addUserSubscription()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            UserContributoService.insertNewUserContributo(database, email, idContributo);
            
            database.commit();
            utente = UserService.getUtenteByEmail(database, email);
            ucontributi = UserContributoService.getUserContributiByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            if(database!=null)
            database.rollBack();
        }catch (DuplicatedRecordDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.RECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }catch (ResultSetDBException ex) 
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
    
    public void deleteUserSubscription()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            UserContributo contributo = UserContributoService.getUserContributoById(database, idUserContributo);
            contributo.delete(database);
            
            database.commit();
            utente = UserService.getUtenteByEmail(database, email);           
            ucontributi = UserContributoService.getUserContributiByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            
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
    
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public Contributo getContributo(int i)
    {
        return contributi.get(i);
    }
    
    public void setContributo(int i,Contributo contributo)
    {
        contributi.set(i, contributo);
    }
    
    public ArrayList<Contributo> getContributi()
    {
        return contributi;
    }
    
    public void setContributi(ArrayList<Contributo> contributi)
    {
        this.contributi = contributi;
    }
    
    public UserContributo getUcontributo(int i)
    {
        return ucontributi.get(i);
    }
    
    public void setUcontributo(int i,UserContributo ucontributo)
    {
        ucontributi.set(i, ucontributo);
    }
    
    public ArrayList<UserContributo> getUcontributi()
    {
        return ucontributi;
    }
    
    public void setUContributi(ArrayList<UserContributo> ucontributi)
    {
        this.ucontributi = ucontributi;
    }
    
    public User getUtente()
    {
        return utente;
    }
    
    public void setUtente(User utente)
    {
        this.utente = utente;
    }
    
    public int getIdContributo()
    {
        return idContributo;
    }
    
    public void setIdContributo(int idContributo)
    {
        this.idContributo = idContributo;
    }
    
    public int getIdUserContributo()
    {
        return idUserContributo;
    }
    
    public void setIdUserContributo(int idUserContributo)
    {
        this.idUserContributo = idUserContributo;
    }
    
    public String getNome()
    {
        return nome;
    }
    
    public void setNome(String nome)
    {
        this.nome = nome;
    }
    
    public double getCosto()
    {
        return costo;
    }
    
    public void setCosto(double costo)
    {
        this.costo = costo;
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
