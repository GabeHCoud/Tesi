/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bflows;

import blogics.Contributo;
import blogics.ContributoService;
import java.io.Serializable;
import java.util.ArrayList;
import services.databaseservice.DBService;
import services.databaseservice.DataBase;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;
import services.errorservice.EService;


public class ContributiManagement implements Serializable {    
    private ArrayList<Contributo> contributi;
    private Contributo selectedContributo;
    private int idContributo;
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
    
    public void addSubscription()
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
    
    public void deleteSubscription()
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
    
    public void viewEditSubscription()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedContributo = ContributoService.getContributoById(database,idContributo);
            
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
    
    public void editSubscription()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedContributo = ContributoService.getContributoById(database, idContributo);
            selectedContributo.Nome = nome;
            selectedContributo.Costo = costo;
            selectedContributo.update(database);
            database.commit();
            
            contributi = ContributoService.getContributi(database);
            
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
    
    
    public Contributo getSelectedContributo()
    {
        return selectedContributo;
    }
    
    public void setSelectedContributo(Contributo selectedContributo)
    {
        this.selectedContributo = selectedContributo;
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
    
    public int getIdContributo()
    {
        return idContributo;
    }
    
    public void setIdContributo(int idContributo)
    {
        this.idContributo = idContributo;
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
