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
import blogics.UserService;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import services.databaseservice.DBService;
import services.databaseservice.DataBase;
import services.databaseservice.exception.DuplicatedRecordDBException;
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
    private ArrayList<Fattura> fatture;
    private User selectedUser;
    private String[] selectedUsers;
    private int idFattura;
    private Fattura selectedFattura;
    private ArrayList<Consumo> selectedConsumi;
    private ArrayList<Telefono> telefoni;
    private ArrayList<Dispositivo> dispositivi;
    private ArrayList<Contributo> contributi;
    private String messaggio;
    private String password;
    
    private String errorMessage;
    private int result;
    
    public MailManagement(){}
    
    public void view()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");            
            
            fatture = FatturaService.getFatture(database);
            
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
            database=DBService.getDataBase("new");
            
            selectedFattura = FatturaService.getFatturaById(database, idFattura);
            selectedConsumi = ConsumoService.getConsumiByFatturaId(database, idFattura);
            utenti = UserService.getUtenti(database);   
            emails = MailService.getEmailsByFatturaId(database, idFattura);
            telefoni = TelefonoService.getTelefoni(database);
            dispositivi = DispositivoService.getDispositivi(database);
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
    
    public void viewUser()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");            
            
            fatture = FatturaService.getFatture(database);
            utenti = UserService.getUtenti(database);        
            selectedUser = UserService.getUtenteByEmail(database, userId);
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
    
    public void sendTest()
    {
        DataBase database = null;        
        String resultMessage = "";
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedFattura = FatturaService.getFatturaById(database, idFattura);
            selectedConsumi = ConsumoService.getConsumiByFatturaId(database, idFattura);
            utenti = UserService.getUtenti(database);   
            telefoni = TelefonoService.getTelefoni(database);
            dispositivi = DispositivoService.getDispositivi(database);
            contributi = ContributoService.getContributi(database); 
        
            for(int i=0; i<selectedUsers.length;i++)
            {   
                String baseMessage = messaggio.trim();  
                resultMessage = "";
                
                //imposto messaggio
                User utente = null;               
                for(User u : utenti)
                {
                    if(u.Email.equals(selectedUsers[i]))
                        utente = u;
                } 
                
                baseMessage = baseMessage.replace("<nome>", utente.Nome);
                baseMessage = baseMessage.replace("<cognome>", utente.Cognome);                    
                boolean isFirst = true;
                
                for(Telefono t : telefoni)
                {    
                    if(t.Email.equals(utente.Email))
                    {
                        if(isFirst)
                        {
                            isFirst = false;
                        }else
                        {    
                            // dal 2° numero in poi inizio un nuovo messaggio da aggiungere
                            baseMessage = messaggio.trim();
                            int startIndex = baseMessage.indexOf("Telefono");                        
                            baseMessage = baseMessage.substring(startIndex,baseMessage.length());                            
                        }
                        
                        baseMessage = baseMessage.replace("<telefono>", t.Numero);
                        DecimalFormat df = new DecimalFormat("####.##");  
                        for(Consumo c : selectedConsumi){                        
                            if(c.Telefono.equals(t.Numero))
                            {    
                                
                                baseMessage = baseMessage.replace("<contributi>", Double.toString(c.CRB));
                                double crbI = c.CRB + c.CRB * 22/100;
                                baseMessage = baseMessage.replace("<contributiI>", df.format(crbI));
                                baseMessage = baseMessage.replace("<aaa>", Double.toString(c.AAA));
                                double aaaI = c.AAA + c.AAA * 22/100;
                                baseMessage = baseMessage.replace("<aaaI>", df.format(aaaI));
                                baseMessage = baseMessage.replace("<abb>", Double.toString(c.ABB));
                                double abbI = c.ABB + c.ABB * 22/100;
                                baseMessage = baseMessage.replace("<abbI>", df.format(abbI));
                                baseMessage = baseMessage.replace("<totale>", Double.toString(c.Totale));
                                double totaleI = c.Totale + c.Totale * 22/100;
                                baseMessage = baseMessage.replace("<totaleI>", df.format(totaleI));
                            }
                        }
                        
                        if(t.IdContributo > 0)
                        {
                            for(Contributo c : contributi)
                            {
                                if(c.IdContributo == t.IdContributo)
                                {
                                    baseMessage = baseMessage.replace("<contributo>",c.Nome);
                                    baseMessage = baseMessage.replaceFirst("<costo>",Double.toString(c.Costo));
                                    double costoI = c.Costo + c.Costo * 22/100;
                                    baseMessage = baseMessage.replaceFirst("<costoI>", df.format(costoI));
                                }
                            }
                        }else //nessun contributo
                        {
                            baseMessage = baseMessage.replace("<contributo>", "nessun contributo o abbonamento");
                            baseMessage = baseMessage.replaceFirst("<costo>","0");
                            baseMessage = baseMessage.replaceFirst("\\(<costoI> con IVA\\)","");
                        }
                        
                        if(t.IdDispositivo > 0)
                        {
                            for(Dispositivo d : dispositivi)
                            {
                                if(d.IdDispositivo == t.IdDispositivo)
                                {
                                    df = new DecimalFormat("####.####"); 
                                    baseMessage = baseMessage.replace("<dispositivo>",d.Nome);
                                    baseMessage = baseMessage.replaceFirst("<costo>",Double.toString(d.Costo));
                                    double costoI = d.Costo + d.Costo * 22/100;
                                    baseMessage = baseMessage.replaceFirst("<costoI>", df.format(costoI));
                                    baseMessage += "<br/><br/>";
                                }
                            }
                        }else //nessun dispostivo
                        {
                            baseMessage = baseMessage.replace("<dispositivo>", "nessun dispositivo");
                            baseMessage = baseMessage.replaceFirst("<costo>","0");
                            baseMessage = baseMessage.replaceFirst("\\(<costoI> con IVA\\)","");
                            baseMessage += "<br/><br/>";
                        }           
                        
                        resultMessage += baseMessage.replaceAll("\n","<br/>");
                    }                        
                }      
                
                //salvo nel db
                Date d = new Date();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String date = df.format(d);
                
                try{
                    MailService.InsertMail(database, date, resultMessage, idFattura, selectedUsers[i]);                
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
                }
                catch (ResultSetDBException ex) 
                {
                    EService.logAndRecover(ex);
                    setResult(EService.UNRECOVERABLE_ERROR);
                    if(database!=null)
                    database.rollBack();
                }
            }
        }         
        catch (NotFoundDBException ex) 
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
    
    public void send()
    {
        DataBase database = null;        
        String resultMessage = "";
        try 
        {
            database=DBService.getDataBase("new");
            
            selectedFattura = FatturaService.getFatturaById(database, idFattura);
            selectedConsumi = ConsumoService.getConsumiByFatturaId(database, idFattura);
            utenti = UserService.getUtenti(database);   
            telefoni = TelefonoService.getTelefoni(database);
            dispositivi = DispositivoService.getDispositivi(database);
            contributi = ContributoService.getContributi(database); 
        
            Properties props = System.getProperties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props,new Authenticator() {
                 protected PasswordAuthentication getPasswordAuthentication() {
                      return new PasswordAuthentication("erika.foli@unife.it",password); 
                 }
            });
            
            Message message = new MimeMessage(session);
            InternetAddress from = new InternetAddress("erika.foli@unife.it");
            InternetAddress to[];
            for(int i=0; i<selectedUsers.length;i++)
            {   
                String baseMessage = messaggio.trim();    
                resultMessage = "";
                
                to = InternetAddress.parse(selectedUsers[i]);

                message.setFrom(from);
                message.setRecipients(Message.RecipientType.TO, to);
                message.setSubject("Fattura Telecom");
                message.setSentDate(new java.util.Date());
                
                //imposto messaggio
                User utente = null;               
                for(User u : utenti)
                {
                    if(u.Email.equals(selectedUsers[i]))
                        utente = u;
                } 
                
                baseMessage = baseMessage.replace("<nome>", utente.Nome);
                baseMessage = baseMessage.replace("<cognome>", utente.Cognome);                    
                boolean isFirst = true;
                
                for(Telefono t : telefoni)
                {    
                    if(t.Email.equals(utente.Email))
                    {
                        if(isFirst)
                        {
                            isFirst = false;
                        }else
                        {    
                            // dal 2° numero in poi inizio un nuovo messaggio da aggiungere
                            baseMessage = messaggio.trim();
                            int startIndex = baseMessage.indexOf("Telefono");                        
                            baseMessage = baseMessage.substring(startIndex,baseMessage.length());                            
                        }
                        
                        baseMessage = baseMessage.replace("<telefono>", t.Numero);
                        DecimalFormat df = new DecimalFormat("####.##");  
                        for(Consumo c : selectedConsumi){                        
                            if(c.Telefono.equals(t.Numero))
                            {    
                                
                                baseMessage = baseMessage.replace("<contributi>", Double.toString(c.CRB));
                                double crbI = c.CRB + c.CRB * 22/100;
                                baseMessage = baseMessage.replace("<contributiI>", df.format(crbI));
                                baseMessage = baseMessage.replace("<aaa>", Double.toString(c.AAA));
                                double aaaI = c.AAA + c.AAA * 22/100;
                                baseMessage = baseMessage.replace("<aaaI>", df.format(aaaI));
                                baseMessage = baseMessage.replace("<abb>", Double.toString(c.ABB));
                                double abbI = c.ABB + c.ABB * 22/100;
                                baseMessage = baseMessage.replace("<abbI>", df.format(abbI));
                                baseMessage = baseMessage.replace("<totale>", Double.toString(c.Totale));
                                double totaleI = c.Totale + c.Totale * 22/100;
                                baseMessage = baseMessage.replace("<totaleI>", df.format(totaleI));
                            }
                        }
                        
                        if(t.IdContributo > 0)
                        {
                            for(Contributo c : contributi)
                            {
                                if(c.IdContributo == t.IdContributo)
                                {
                                    baseMessage = baseMessage.replace("<contributo>",c.Nome);
                                    baseMessage = baseMessage.replaceFirst("<costo>",Double.toString(c.Costo));
                                    double costoI = c.Costo + c.Costo * 22/100;
                                    baseMessage = baseMessage.replaceFirst("<costoI>", df.format(costoI));
                                }
                            }
                        }else //nessun contributo
                        {
                            baseMessage = baseMessage.replace("<contributo>", "nessun contributo o abbonamento");
                            baseMessage = baseMessage.replaceFirst("<costo>","0");
                            baseMessage = baseMessage.replaceFirst("\\(<costoI> con IVA\\)","");
                        }
                        
                        if(t.IdDispositivo > 0)
                        {
                            for(Dispositivo d : dispositivi)
                            {
                                if(d.IdDispositivo == t.IdDispositivo)
                                {
                                    df = new DecimalFormat("####.####"); 
                                    baseMessage = baseMessage.replace("<dispositivo>",d.Nome);
                                    baseMessage = baseMessage.replaceFirst("<costo>",Double.toString(d.Costo));
                                    double costoI = d.Costo + d.Costo * 22/100;
                                    baseMessage = baseMessage.replaceFirst("<costoI>", df.format(costoI));
                                    baseMessage += "<br/><br/>";
                                }
                            }
                        }else //nessun dispostivo
                        {
                            baseMessage = baseMessage.replace("<dispositivo>", "nessun dispositivo");
                            baseMessage = baseMessage.replaceFirst("<costo>","0");
                            baseMessage = baseMessage.replaceFirst("\\(<costoI> con IVA\\)","");
                            baseMessage += "<br/><br/>";
                        }           
                        
                        resultMessage += baseMessage.replaceAll("\n","<br/>");
                    }                        
                }               
                
                message.setText(resultMessage);
                message.setContent(resultMessage, "text/html; charset=ISO-8859-1");
                Transport tr = session.getTransport("smtp");
                tr.connect("smtp.gmail.com", "erika.foli@unife.it",password);
                message.saveChanges();
                tr.sendMessage(message, message.getAllRecipients());
                tr.close(); 
                
                //salvo nel db
                Date d = new Date();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String date = df.format(d);
                
                try{
                    MailService.InsertMail(database, date, resultMessage, idFattura, selectedUsers[i]);                
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
                }
                catch (ResultSetDBException ex) 
                {
                    EService.logAndRecover(ex);
                    setResult(EService.UNRECOVERABLE_ERROR);
                    if(database!=null)
                    database.rollBack();
                }
            }
        } 
        catch (MessagingException ex) 
        {
            Logger.getLogger(MailManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NotFoundDBException ex) 
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
    
    public String getMessaggio()
    {
        return messaggio;
    }
    
    public void setMessaggio(String messaggio)
    {
        this.messaggio = messaggio;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
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
