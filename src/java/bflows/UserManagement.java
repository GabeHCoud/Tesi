/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bflows;

import blogics.Consumo;
import blogics.ConsumoService;
import blogics.Contributo;
import blogics.ContributoService;
import blogics.Dispositivo;
import blogics.DispositivoService;
import blogics.Fattura;
import blogics.FatturaService;
import blogics.Fondo;
import blogics.FondoService;
import blogics.Telefono;
import blogics.TelefonoService;
import blogics.User;
import blogics.UserService;
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
    private int idTelefono;
    private String newnumero;
    private int idFattura;
    private String nome;
    private String cognome;
    private String email;
    private String newemail;
    private ArrayList<User> utenti;
    private ArrayList<Telefono> telefoni;
    private ArrayList<Contributo> contributi;
    private ArrayList<Dispositivo> dispositivi;
    private ArrayList<Fondo> fondi;
    private User selectedUser;
    private Telefono selectedTelefono;
    private Fondo selectedFondo;
    private int idFondo;
    private int idContributo;
    private int idDispositivo;
    
    
    private String errorMessage;
    private int result;
    
    
    public UserManagement() {}    
    
    public void viewUsers()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            utenti = UserService.getUtenti(database);
            telefoni = TelefonoService.getTelefoni(database);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void associate() 
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
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

    public void register()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
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
    
    public void viewEditUser()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            telefoni = TelefonoService.getTelefoniByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondiByEmail(database,email);
            
            database.commit();
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void addUser()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");            
            
            UserService.InsertNewUtente(database, email, nome, cognome);            
            
            utenti = UserService.getUtenti(database);
            telefoni = TelefonoService.getTelefoni(database);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);
            
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
    
    public void deleteUser()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");            
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            selectedUser.delete(database);            
            
            utenti = UserService.getUtenti(database);
            telefoni = TelefonoService.getTelefoni(database);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);
            
            database.commit();
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
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
    
    public void editUser()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
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
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);  
            fondi = FondoService.getFondi(database);     
            
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
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
    
    public void viewEditPhone()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            selectedTelefono = TelefonoService.getTelefonoById(database, idTelefono);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);   
            fondi = FondoService.getFondi(database);          
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void addPhone()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");            
            
            TelefonoService.insertNewTelefono(database, numero, email);
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            telefoni = TelefonoService.getTelefoniByEmail(database, email);  
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);
            
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
    
    public void deletePhone()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");            
            
            Telefono t = TelefonoService.getTelefonoById(database, idTelefono);
            t.delete(database);     
            
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            telefoni = TelefonoService.getTelefoniByEmail(database, email);  
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);  
            fondi = FondoService.getFondi(database);  
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
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
    
    public void editPhone()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedTelefono = TelefonoService.getTelefonoById(database, idTelefono);             
            
            if(!newnumero.equals(numero))//se l'email cambia aggiorno                     
                selectedTelefono.updateNumero(database,newnumero);
            else
                selectedTelefono.update(database);         
                
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            telefoni = TelefonoService.getTelefoniByEmail(database, email);  
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database); 
            fondi = FondoService.getFondi(database);                 
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void addSubscription()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedTelefono = TelefonoService.getTelefonoById(database, idTelefono);            
            selectedTelefono.IdContributo = idContributo;
            selectedTelefono.update(database);            
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);             
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void deleteSubscription()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedTelefono = TelefonoService.getTelefonoById(database, idTelefono);            
            selectedTelefono.IdContributo = 0;
            selectedTelefono.update(database);            
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);             
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void addDevice()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedTelefono = TelefonoService.getTelefonoById(database, idTelefono);            
            selectedTelefono.IdDispositivo = idDispositivo;
            selectedTelefono.update(database);            
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);             
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void deleteDevice()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedTelefono = TelefonoService.getTelefonoById(database, idTelefono);            
            selectedTelefono.IdDispositivo = 0;
            selectedTelefono.update(database);            
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database); 
            fondi = FondoService.getFondi(database);            
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void viewEditFondo()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedFondo = FondoService.getFondoById(database, idFondo);
            selectedUser = UserService.getUtenteByEmail(database, email);
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void addFondo()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            Fondo fondo = FondoService.InsertNewFondo(database, email, nome, false);
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            telefoni = TelefonoService.getTelefoniByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);             
            
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
        }
        catch (ResultSetDBException ex) 
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
    
    public void editFondo()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            Fondo fondo = FondoService.getFondoById(database, idFondo);
            if(!fondo.Nome.equals(nome))
            {
                fondo.Nome = nome;
                fondo.update(database);
            }            
            
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            telefoni = TelefonoService.getTelefoniByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);             
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void activateFondo()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            Fondo selectedFondo = FondoService.getFondoById(database, idFondo);
            ArrayList<Fondo> userFondi = FondoService.getFondiByEmail(database, email);
            
            for(Fondo f : userFondi)
            {
                if(f.IdFondo == idFondo)
                {
                    f.Attivo = true;
                }else{
                    f.Attivo = false;
                }
                f.update(database);
            }
            
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            telefoni = TelefonoService.getTelefoniByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);                           
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    public void deleteFondo()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            Fondo fondo = FondoService.getFondoById(database, idFondo);
            fondo.delete(database);
            database.commit();
            
            selectedUser = UserService.getUtenteByEmail(database, email);
            telefoni = TelefonoService.getTelefoniByEmail(database, email);
            contributi = ContributoService.getContributi(database);
            dispositivi = DispositivoService.getDispositivi(database);
            fondi = FondoService.getFondi(database);             
            
        }catch (NotFoundDBException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (ResultSetDBException ex) 
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
    
    
    public String getNumero()
    {
        return numero;
    }
    
    public void setNumero(String numero)
    {
        this.numero = numero;
    }
    
    public int getIdTelefono()
    {
        return idTelefono;
    }
    
    public void setIdTelefono(int idTelefono)
    {
        this.idTelefono = idTelefono;
    }
    
    public String getNewnumero()
    {
        return newnumero;
    }
    
    public void setNewnumero(String newnumero)
    {
        this.newnumero = newnumero;
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
    
    public Dispositivo getDispositivo(int i)
    {
        return dispositivi.get(i);
    }
    
    public void setDispositivo(int i,Dispositivo dispositivo)
    {
        dispositivi.set(i, dispositivo);
    }
    
    public ArrayList<Dispositivo> getDispositivi()
    {
        return dispositivi;
    }
    
    public void setDispositivi(ArrayList<Dispositivo> dispositivi)
    {
        this.dispositivi = dispositivi;
    }
    
    public Fondo getFondo(int i)
    {
        return fondi.get(i);
    }
    
    public void setFondo(int i,Fondo fondo)
    {
        fondi.set(i, fondo);
    }
    
    public ArrayList<Fondo> getFondi()
    {
        return fondi;
    }
    
    public void setFondi(ArrayList<Fondo> fondi)
    {
        this.fondi = fondi;
    }
        
    public User getSelectedUser()
    {
        return selectedUser;
    }
    
    public void setSelectedUser(User selectedUser)
    {
        this.selectedUser = selectedUser;
    }
    
    public Telefono getSelectedTelefono()
    {
        return selectedTelefono;
    }
    
    public void setSelectedTelefono(Telefono selectedTelefono)
    {
        this.selectedTelefono = selectedTelefono;
    }
    
    public Fondo getSelectedFondo()
    {
        return selectedFondo;
    }
    
    public void setSelectedTelefono(Fondo selectedFondo)
    {
        this.selectedFondo = selectedFondo;
    }
    
    public int getIdFondo()
    {
        return this.idFondo;
    }
    
    public void setIdFondo(int idFondo)
    {
        this.idFondo = idFondo;
    }
    
    public int getIdContributo()
    {
        return idContributo;
    }
    
    public void setIdContributo(int idContributo)
    {
        this.idContributo = idContributo;
    }
    
    public int getIdDispositivo()
    {
        return idDispositivo;
    }
    
    public void setIdDispositivo(int idDispositivo)
    {
        this.idDispositivo = idDispositivo;
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