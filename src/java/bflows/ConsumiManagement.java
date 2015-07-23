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
import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import services.databaseservice.DBService;
import services.databaseservice.DataBase;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;
import services.errorservice.EService;

public class ConsumiManagement implements Serializable {    
    private ArrayList<Fattura> fatture;
    private ArrayList<Consumo> consumi;  
    private int idFattura;    
    private Fattura selectedFattura;
    private Consumo selectedConsumo;
    private String telefono;
    private String contributi;
    private String altri;
    private String abbonamenti;
    private String totale;
    
    private String errorMessage;
    private int result;
    
    public ConsumiManagement() {}
    
    public void viewFatture()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            fatture = FatturaService.getFatture(database);
            
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
    
    public void viewConsumi()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            selectedFattura = FatturaService.getFatturaById(database, idFattura);
            consumi = ConsumoService.getConsumiByFatturaId(database, idFattura);
            
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
    
    public void viewEdit()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            selectedFattura = FatturaService.getFatturaById(database, idFattura);            
            selectedConsumo = ConsumoService.getConsumoByFatturaIdAndTelefono(database, idFattura, telefono);
            
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
            
            selectedConsumo = ConsumoService.getConsumoByFatturaIdAndTelefono(database, idFattura, telefono);
            selectedConsumo.CRB = Double.parseDouble(contributi);
            selectedConsumo.AAA = Double.parseDouble(altri);
            selectedConsumo.ABB = Double.parseDouble(abbonamenti);
            selectedConsumo.Totale = Double.parseDouble(totale);
            
            selectedConsumo.update(database);
            
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
        catch (Exception e){
            System.out.println(e);
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
            
            Fattura fattura = FatturaService.getFatturaById(database, idFattura);            
            fattura.delete(database);
            
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
    
    public Consumo getConsumo(int i)
    {
        return consumi.get(i);
    }
    
    public void setConsumo(int i,Consumo consumo)
    {
        consumi.set(i, consumo);
    }
    
    public ArrayList<Consumo> getConsumi()
    {
        return consumi;
    }
    
    public void setConsumi(ArrayList<Consumo> consumi)
    {
        this.consumi = consumi;
    }
    
    public Fattura getFattura(int i)
    {
        return fatture.get(i);
    }
    
    public void setFattura(int i,Fattura fattura)
    {
        fatture.set(i, fattura);
    }
    
    public ArrayList<Fattura> getFatture()
    {
        return fatture;
    }
    
    public void setFatture(ArrayList<Fattura> fatture)
    {
        this.fatture = fatture;
    }
    
    public int getIdFattura() 
    {
        return idFattura;
    }
    
    public void setIdFattura(int idFattura) 
    {
        this.idFattura = idFattura;
    }  
    
    public Fattura getSelectedFattura() 
    {
        return selectedFattura;
    }
    
    public void setSelectedFattura(Fattura selectedFattura) 
    {
        this.selectedFattura = selectedFattura;
    }
    
    public Consumo getSelectedConsumo() 
    {
        return selectedConsumo;
    }
    
    public void setSelectedConsumo(Consumo selectedConsumo) 
    {
        this.selectedConsumo = selectedConsumo;
    }
    
    public String getTelefono() 
    {
        return telefono;
    }
    
    public void setTelefono(String telefono) 
    {
        this.telefono = telefono;
    }
    
    public String getContributi() 
    {
        return contributi;
    }
    
    public void setContributi(String crb) 
    {
        this.contributi = crb;
    }
    
    public String getAltri() 
    {
        return altri;
    }
    
    public void setAltri(String aaa) 
    {
        this.altri = aaa;
    }
    
    public String getAbbonamenti() 
    {
        return abbonamenti;
    }
    
    public void setAbbonamenti(String abb) 
    {
        this.abbonamenti = abb;
    }
    
    public String getTotale() 
    {
        return totale;
    }
    
    public void setTotale(String totale) 
    {
        this.totale = totale;
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
