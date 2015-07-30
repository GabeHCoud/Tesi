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
import blogics.Mail;
import blogics.MailService;
import blogics.Telefono;
import blogics.TelefonoService;
import blogics.User;
import blogics.UserContributo;
import blogics.UserContributoService;
import blogics.UserDispositivo;
import blogics.UserDispositivoService;
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
    private String userId;
    private ArrayList<User> utenti;
    private ArrayList<Mail> emails;
    private User selectedUser;
    private String[] selectedUsers;
    private int idFattura;
    private Fattura selectedFattura;
    private ArrayList<Consumo> selectedConsumi;
    private ArrayList<Telefono> telefoni;
    private ArrayList<Dispositivo> dispositivi;
    private ArrayList<UserDispositivo> udispositivi;
    private ArrayList<Contributo> contributi;
    private ArrayList<UserContributo> ucontributi;
    
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
            {
                selectedUser = UserService.getUtenteByEmail(database, userId);
                emails = MailService.getEmailsByUserId(database, userId);
            }
            
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
    
    public void viewFattura()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase();
            
            selectedFattura = FatturaService.getFatturaById(database, idFattura);
            selectedConsumi = ConsumoService.getConsumiByFatturaId(database, idFattura);
            utenti = UserService.getUtenti(database);   
            telefoni = TelefonoService.getTelefoni(database);
            dispositivi = DispositivoService.getDispositivi(database);
            udispositivi = UserDispositivoService.getUserDispositivi(database);
            contributi = ContributoService.getContributi(database);
            ucontributi = UserContributoService.getUserContributi(database);
            
            
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
    
    public User getSelectedUser()
    {
        return selectedUser;
    }
    
    public void setSelectedUser(User selectedUser)
    {
        this.selectedUser = selectedUser;
    }
    
    public String getSelectedUsers(int i)
    {
        return selectedUsers[i];
    }
    
    public void setSelectedUsers(int i,String selectedUser)
    {
        this.selectedUsers[i] = selectedUser;
    }
    
    public String[] getSelectedUsers()
    {
        return selectedUsers;
    }
    
    public void setSelectedUsers(String[] selectedUsers)
    {
        this.selectedUsers = selectedUsers;
    }
    
    public int getIdFattura()
    {
        return idFattura;
    }
    
    public void setIdFattura(int idFattura)
    {
        this.idFattura = idFattura;
    }
    
    public Fattura getSelctedFattura()
    {
        return selectedFattura;
    }
    
    public void setSelectedFattura(Fattura selectedFattura)
    {
        this.selectedFattura = selectedFattura;
    }
    
    public Consumo getSelectedConsumo(int i)
    {
        return selectedConsumi.get(i);
    }
    
    public void setSelectedConsumo(int i,Consumo email)
    {
        selectedConsumi.set(i, email);
    }
    
    public ArrayList<Consumo> getSelectedConsumi()
    {
        return selectedConsumi;
    }
    
    public void setSelectedConsumi(ArrayList<Consumo> selectedConsumi)
    {
        this.selectedConsumi = selectedConsumi;
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
