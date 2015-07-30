/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bflows;

import blogics.Dispositivo;
import blogics.DispositivoService;
import blogics.TelefonoService;
import blogics.User;
import blogics.UserDispositivo;
import blogics.UserDispositivoService;
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


public class DispositiviManagement implements Serializable {
    private String email;
    private ArrayList<Dispositivo> dispositivi;
    private ArrayList<UserDispositivo> udispositivi;
    private User utente;
    private int idDispositivo;
    private int idUserDispositivo;
    private String nome;
    private double costo;
    
     
    
    private String errorMessage;
    private int result;
    
    public DispositiviManagement(){}
    
    public void view()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            dispositivi = DispositivoService.getDispositivi(database);
            
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
    
    public void addDevice()
    {        
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            DispositivoService.insertNewDispositivo(database, nome, costo);
            
            database.commit();
            dispositivi = DispositivoService.getDispositivi(database);
            
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
    
    public void deleteDevice()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            Dispositivo dispositivo = DispositivoService.getDispositivoById(database,idDispositivo);
            dispositivo.delete(database);
            
            database.commit();
            dispositivi = DispositivoService.getDispositivi(database);
            
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
    
    public void viewUserDevices()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            utente = UserService.getUtenteByEmail(database, email);           
            udispositivi = UserDispositivoService.getUserDispositiviByEmail(database, email);
            dispositivi = DispositivoService.getDispositivi(database);
            
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
    
    public void addUserDevice()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            UserDispositivoService.insertNewUserDispositivo(database, email, idDispositivo);
            
            database.commit();
            utente = UserService.getUtenteByEmail(database, email);
            udispositivi = UserDispositivoService.getUserDispositiviByEmail(database, email);
            dispositivi = DispositivoService.getDispositivi(database);
            
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
    
    public void deleteUserDevice()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            UserDispositivo dispositivo = UserDispositivoService.getUserDispositivoById(database,idUserDispositivo);
            dispositivo.delete(database);
            
            database.commit();
            utente = UserService.getUtenteByEmail(database, email);           
            udispositivi = UserDispositivoService.getUserDispositiviByEmail(database, email);
            dispositivi = DispositivoService.getDispositivi(database);
            
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
    
    public UserDispositivo getUdispositivo(int i)
    {
        return udispositivi.get(i);
    }
    
    public void setUdispositivo(int i,UserDispositivo udispositivo)
    {
        udispositivi.set(i, udispositivo);
    }
    
    public ArrayList<UserDispositivo> getUdispositivi()
    {
        return udispositivi;
    }
    
    public void setUdispositivi(ArrayList<UserDispositivo> udispositivi)
    {
        this.udispositivi = udispositivi;
    }
    
    public User getUtente()
    {
        return utente;
    }
    
    public void setUtente(User utente)
    {
        this.utente = utente;
    }
    
    public int getIdDispositivo()
    {
        return idDispositivo;
    }
    
    public void setIdDispositivo(int idDispositivo)
    {
        this.idDispositivo = idDispositivo;
    }
    
    public int getIdUserDispositivo()
    {
        return idUserDispositivo;
    }
    
    public void setIdUserDispositivo(int idUserDispositivo)
    {
        this.idUserDispositivo = idUserDispositivo;
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
