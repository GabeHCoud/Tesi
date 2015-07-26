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
    private String numero;    
    private int idFattura;
    private String nome;
    private String cognome;
    private String email;
    private String newemail;
    private ArrayList<User> utenti;
    private ArrayList<Telefono> telefoni;
    private User selectedUser;
    
    
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
            telefoni = TelefonoService.getTelefoni(database);
            
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
            TelefonoService.insertNewTelefono(database,numero,email);
            
            //aggiorno in ogni fattura i consumi corrispondenti al numero 
            //selezionato inserendo l'email associata
            ArrayList<Fattura> fatture = FatturaService.getFatture(database);            
            for(Fattura f : fatture){
                ArrayList<Consumo> consumi = ConsumoService.getConsumiByFatturaId(database, f.IdFattura);            
                for(Consumo c : consumi)
                {
                    if(c.Telefono.equals(numero))                        
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

    public void register()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            UserService.InsertNewUtente(database, email, nome, cognome);            
            TelefonoService.insertNewTelefono(database,numero,email);
            
            //aggiorno in ogni fattura i consumi corrispondenti al numero 
            //selezionato inserendo l'email associata
            ArrayList<Fattura> fatture = FatturaService.getFatture(database);            
            for(Fattura f : fatture){
                ArrayList<Consumo> consumi = ConsumoService.getConsumiByFatturaId(database, f.IdFattura);            
                for(Consumo c : consumi)
                {
                    if(c.Telefono.equals(numero))                        
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
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
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
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
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
            database=DBService.getDataBase();            
            
            UserService.InsertNewUtente(database, email, nome, cognome);            
            
            utenti = UserService.getUtenti(database);
            telefoni = TelefonoService.getTelefoni(database);
            
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
            database=DBService.getDataBase();            
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            selectedUser.delete(database);            
            
            utenti = UserService.getUtenti(database);
            telefoni = TelefonoService.getTelefoni(database);
            
            database.commit();
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
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
    
    public void viewEditUser()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            
            database.commit();
            
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
    
    public void edit()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            selectedUser = UserService.getUtenteByEmail(database, email);  
            selectedUser.Nome = nome;
            selectedUser.Cognome = cognome;    
            
            if(!newemail.equals(email))//se l'email cambia aggiorno                     
                selectedUser.updateEmail(database,newemail);
            else
                selectedUser.update(database);         
                
            database.commit();
            
            utenti = UserService.getUtenti(database);
            telefoni = TelefonoService.getTelefoni(database);           
            
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
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
    
    public void viewAddPhone()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            telefoni = TelefonoService.getTelefoni(database);            
            
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
    
    public void addPhone()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();            
            
            TelefonoService.insertNewTelefono(database, numero, email);
            
            utenti = UserService.getUtenti(database);
            telefoni = TelefonoService.getTelefoni(database);
            
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
    
    public void deletePhone()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();            
            
            Telefono t = TelefonoService.getTelefono(database, numero);
            t.delete(database);     
            
            database.commit();
            
            utenti = UserService.getUtenti(database);
            telefoni = TelefonoService.getTelefoni(database);
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
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
    
    public String getNumero()
    {
        return numero;
    }
    
    public void setNumero(String numero)
    {
        this.numero = numero;
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
    
    public String getNewemail()
    {
        return newemail;
    }
    
    public void setNewemail(String newemail)
    {
        this.newemail = newemail;
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
    
    public Telefono getTelefono(int i)
    {
        return telefoni.get(i);
    }
    
    public void setTelefono(int i,Telefono telefono)
    {
        telefoni.set(i, telefono);
    }
    
    public ArrayList<Telefono> getTelefoni()
    {
        return telefoni;
    }
    
    public void setTelefoni(ArrayList<Telefono> telefoni)
    {
        this.telefoni = telefoni;
    }    
    
    public User getSelectedUser()
    {
        return selectedUser;
    }
    
    public void setSelectedUser(User selectedUser)
    {
        this.selectedUser = selectedUser;
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