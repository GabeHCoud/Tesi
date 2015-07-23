/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bflows;

import blogics.Consumo;
import blogics.ConsumoService;
import blogics.Fattura;
import blogics.FatturaService;
import blogics.Telefono;
import blogics.TelefonoService;
import blogics.User;
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

public class UserManagement implements Serializable {
    private String telefono;
    private int idFattura;
    private String nome;
    private String cognome;
    private String email;
    private ArrayList<User> utenti;
    
    
    private String errorMessage;
    private int result;
    
    
    public UserManagement() {}
    
    
    public void viewUsers()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            utenti = UserService.getUtenti(database);
            
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
    
    public void associate() 
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            //associo il numero all'email
            TelefonoService.insertNewTelefono(database,telefono,email);
            
            //aggiorno in ogni fattura i consumi corrispondenti al numero 
            //selezionato inserendo l'email associata
            ArrayList<Fattura> fatture = FatturaService.getFatture(database);            
            for(Fattura f : fatture){
                ArrayList<Consumo> consumi = ConsumoService.getConsumiByFatturaId(database, f.IdFattura);            
                for(Consumo c : consumi)
                {
                    if(c.Telefono.equals(telefono))                        
                    {
                        c.Email = email;   
                        c.update(database);
                    }
                }
            }          
            
            database.commit();
            
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
            setErrorMessage("Il numero di telefono è già associato all'utente selezionato");
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

    public void register()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            UserService.InsertNewUtente(database, email, nome, cognome);            
            TelefonoService.insertNewTelefono(database,telefono,email);
            
            //aggiorno in ogni fattura i consumi corrispondenti al numero 
            //selezionato inserendo l'email associata
            ArrayList<Fattura> fatture = FatturaService.getFatture(database);            
            for(Fattura f : fatture){
                ArrayList<Consumo> consumi = ConsumoService.getConsumiByFatturaId(database, f.IdFattura);            
                for(Consumo c : consumi)
                {
                    if(c.Telefono.equals(telefono))                        
                    {
                        c.Email = email;   
                        c.update(database);
                    }
                }
            }  
            
            database.commit();
            
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
            setErrorMessage("L'utente inserito o il numero di telefono sono già esistenti");
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
    
    
    
    public String getTelefono()
    {
        return telefono;
    }
    
    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }
    
    public int getIdFattura()
    {
        return idFattura;
    }
    
    public void setIdFattura(int idFattura)
    {
        this.idFattura = idFattura;
    }
    
    public String getNome()
    {
        return nome;
    }
    
    public void setNome(String nome)
    {
        this.nome = nome;
    }
    
    public String getCognome()
    {
        return cognome;
    }
    
    public void setCognome(String cognome)
    {
        this.cognome = cognome;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
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