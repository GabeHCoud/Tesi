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
    private ArrayList<Dispositivo> dispositivi;
    private Dispositivo selectedDispositivo;
    private int idDispositivo;
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
    
    public void viewEditDevice()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedDispositivo = DispositivoService.getDispositivoById(database,idDispositivo);
            
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
    
    public void editDevice()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedDispositivo = DispositivoService.getDispositivoById(database, idDispositivo);
            selectedDispositivo.Nome = nome;
            selectedDispositivo.Costo = costo;
            selectedDispositivo.update(database);
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
    
    
    public Dispositivo getSelectedDispositivo()
    {
        return selectedDispositivo;
    }
    
    public void setSelectedDispositivo(Dispositivo selectedDispositivo)
    {
        this.selectedDispositivo = selectedDispositivo;
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
    
    public int getIdDispositivo()
    {
        return idDispositivo;
    }
    
    public void setIdDispositivo(int idDispositivo)
    {
        this.idDispositivo = idDispositivo;
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
