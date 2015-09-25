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
import blogics.Mail;
import blogics.MailService;
import blogics.Telefono;
import blogics.TelefonoService;
import blogics.User;
import blogics.UserService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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

public class MailManagement implements Serializable {
    private String absolutePath;
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
    private ArrayList<Fondo> fondi;
    private String messaggio;
    private String sender;
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
            
            messaggio = getDefaultMail();
            
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
            fondi = FondoService.getFondi(database);         
            
            messaggio = getDefaultMail();
            
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
            fondi = FondoService.getFondi(database); 
        
            messaggio = getDefaultMail().replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&egrave;","è").replaceAll("&agrave;","à").replace("&euro;","€");
            
            for(int i=0; i<selectedUsers.length;i++)
            {   
                
                String baseMessage = messaggio.trim();
                baseMessage = baseMessage.replace("Ringrazio e saluto cordialmente.", "");
                resultMessage = "";
                
                SimpleDateFormat dateFormat = new SimpleDateFormat( "LLLL", Locale.getDefault() );
                String dataFattura = selectedFattura.Data;
                String[] splitted = dataFattura.split("-");
                Calendar cal = Calendar.getInstance();              
                cal.set(Integer.parseInt(splitted[2]),Integer.parseInt(splitted[1]),Integer.parseInt(splitted[0]));
                cal.add(Calendar.MONTH, -2);
                Date tempdate = cal.getTime();                
                String previous1 = dateFormat.format(tempdate);
                cal.add(Calendar.MONTH, -1);
                tempdate = cal.getTime();
                String previous2 = dateFormat.format(tempdate);                               
                
                baseMessage = baseMessage.replace("<data>",selectedFattura.Data);
                baseMessage = baseMessage.replace("<bimestre>",previous2 + "-" + previous1);
                
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
                double Totale = 0.0;                             
                DecimalFormat dbf = new DecimalFormat("0.00");
                ArrayList<String> selectedPhones = new ArrayList<String>(); //lista ausiliaria per gestire duplicati
                ArrayList<Double> parziali = new ArrayList<Double>(); //lista totali per numero
                
                for(Telefono t : telefoni)
                {                        
                    if(t.Email.equals(utente.Email))
                    {
                        if(selectedPhones.contains(t.Numero)) //numero duplicato, devo sommare i costi
                        {
                            String expr = t.Numero.toString();
                            int startIndex = resultMessage.indexOf(expr);           
                            
                            String before = resultMessage.substring(0, startIndex);
                            String after = resultMessage.substring(startIndex, resultMessage.length() - 1);
                            for(Dispositivo d : dispositivi)
                            {
                                if(d.IdDispositivo == t.IdDispositivo)
                                {
                                    String replacement = "Dispositivo "+d.Nome+": € "+dbf.format(d.Costo)+"<br/>Dispositivo ";
                                    after = after.replaceFirst("Dispositivo",replacement);                                   
                                    Totale += d.Costo;
                                    
                                    //aggiorno la lista dei totali per numero con l'importo del dispositivo
                                    for(String phone : selectedPhones)
                                    {
                                        if(phone.equals(t.Numero))
                                        {
                                            int index = selectedPhones.indexOf(phone);
                                            parziali.set(index, parziali.get(index) + d.Costo);
                                        }
                                    }
                                }
                            }                   
                            resultMessage = before+after;
                            
                        }else{   
                            selectedPhones.add(t.Numero); //aggiungo nuovo numero     
                            
                            if(isFirst)
                            {
                                isFirst = false;
                            }else
                            {    
                                // dal 2° numero in poi inizio un nuovo messaggio 
                                baseMessage = messaggio.trim();
                                baseMessage = baseMessage.replace("Ringrazio e saluto cordialmente.", "");
                                int startIndex = baseMessage.indexOf("Telefono");                        
                                baseMessage = baseMessage.substring(startIndex,baseMessage.length());
                            }

                            baseMessage = baseMessage.replace("<telefono>", t.Numero);

                            boolean foundConsumo = false;

                            for(Consumo c : selectedConsumi)
                            {
                                if(c.Telefono.equals(t.Numero))
                                {    
                                    foundConsumo = true;

                                    if(t.IdDispositivo > 0)
                                    {
                                        for(Dispositivo d : dispositivi)
                                        {
                                            if(d.IdDispositivo == t.IdDispositivo)
                                            {
                                                baseMessage = baseMessage.replace("<dispositivoNome>",d.Nome);
                                                baseMessage = baseMessage.replace("<dispositivoCosto>",dbf.format(d.Costo));
                                                Totale += d.Costo;
                                            }
                                        }
                                    }else //nessun dispostivo
                                    {
                                        baseMessage = baseMessage.replace("Dispositivo <dispositivoNome>: € <dispositivoCosto>", "Dispositivo: nessuno dispositivo");
                                    }  

                                    if(t.IdContributo > 0)
                                    {
                                        for(Contributo co : contributi)
                                        {
                                            if(co.IdContributo == t.IdContributo)
                                            {
                                                baseMessage = baseMessage.replace("<contributiNome>",co.Nome);
                                            }
                                        }
                                    }else{
                                        baseMessage = baseMessage.replace("<contributiNome>","Contributo Ricaricabile Business");
                                    }

                                    baseMessage = baseMessage.replace("<contributi>", dbf.format(c.CRB));
                                    baseMessage = baseMessage.replace("<aaa>", dbf.format(c.AAA));
                                    baseMessage = baseMessage.replace("<abb>", dbf.format(c.ABB));

                                    Totale += c.Totale;
                                }
                            }

                            if(!foundConsumo)
                            {
                                if(t.IdDispositivo > 0)
                                {
                                    for(Dispositivo d : dispositivi)
                                    {
                                        if(d.IdDispositivo == t.IdDispositivo)
                                        {
                                            baseMessage = baseMessage.replace("<dispositivoNome>",d.Nome);
                                            baseMessage = baseMessage.replace("<dispositivoCosto>",dbf.format(d.Costo));
                                            Totale += d.Costo;
                                        }
                                    }
                                }else //nessun dispostivo
                                {
                                    baseMessage = baseMessage.replace("Dispositivo <dispositivoNome>: € <dispositivoCosto>", "Dispositivo: nessuno dispositivo");
                                }

                                baseMessage = baseMessage.replace("<contributiNome>","Contributo Ricaricabile Business");
                                baseMessage = baseMessage.replace("<contributi>","0.00");
                                baseMessage = baseMessage.replace("<aaa>", "0.00");
                                baseMessage = baseMessage.replace("<abb>", "0.00");                          

                            }
                            
                            //salvo il totale per questo numero     
                            Double parziale_i = Totale;
                            for(Double p : parziali)
                            {
                                parziale_i -= p;
                            }
                            parziali.add(parziale_i);
                            
                            resultMessage += baseMessage.replaceAll("&#010;","<br/>");
                        }                        
                    }                        
                }      
                
                //imposto i totali per telefono
                for(Double p : parziali)
                {
                    resultMessage = resultMessage.replaceFirst("<totale_i>", dbf.format(p));
                }
                
                //imposto il fondo selezionando quello attivo
                Fondo fondoAttivo = null;
                for(Fondo f : fondi)
                {
                    if(f.Email.equals(utente.Email) && f.Attivo)
                        fondoAttivo = f;
                }
                if(fondoAttivo != null)
                    resultMessage = resultMessage.replace("<fondo>",fondoAttivo.Nome);
                
                resultMessage = resultMessage.replace("<totale>", dbf.format(Totale));
                resultMessage = resultMessage + "Ringrazio e saluto cordialmente.";
                resultMessage = resultMessage.replaceAll("€","&euro;");
                
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
            }
        }         
        catch (NotFoundDBException ex) 
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
            fondi = FondoService.getFondi(database);
        
            messaggio = getDefaultMail().replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&egrave;","è").replaceAll("&agrave;","à").replace("&euro;","€");            
            
            Properties props = System.getProperties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props,new Authenticator() {
                 protected PasswordAuthentication getPasswordAuthentication() {
                      return new PasswordAuthentication(sender,password); 
                 }
            });
            
            Message message = new MimeMessage(session);            
            InternetAddress from;
            if(sender.contains("florke")){
                from = new InternetAddress("erika.foli@unife.it");
            }else 
                from = new InternetAddress(sender);
            
            InternetAddress to[];
            
            for(int i=0; i<selectedUsers.length;i++)
            {   
                String baseMessage = messaggio.trim();                
                baseMessage = baseMessage.replace("Ringrazio e saluto cordialmente.", "");
                resultMessage = "";
                
                to = InternetAddress.parse(selectedUsers[i]);

                message.setFrom(from);                
                message.setRecipients(Message.RecipientType.TO, to);
                
                SimpleDateFormat dateFormat = new SimpleDateFormat( "LLLL", Locale.getDefault() );
                String dataFattura = selectedFattura.Data;
                String[] splitted = dataFattura.split("-");
                Calendar cal = Calendar.getInstance();              
                cal.set(Integer.parseInt(splitted[2]),Integer.parseInt(splitted[1]),Integer.parseInt(splitted[0]));
                cal.add(Calendar.MONTH, -2);
                Date tempdate = cal.getTime();                
                String previous1 = dateFormat.format(tempdate);
                cal.add(Calendar.MONTH, -1);
                tempdate = cal.getTime();
                String previous2 = dateFormat.format(tempdate);
                
                message.setSubject("Fattura TIM del " + selectedFattura.Data + "(bimestre " + previous2 + "-" + previous1 + ")");
                message.setSentDate(new java.util.Date());
                
                baseMessage = baseMessage.replace("<data>",selectedFattura.Data);
                baseMessage = baseMessage.replace("<bimestre>",previous2 + "-" + previous1);
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
                double Totale = 0.0;                        
                DecimalFormat dbf = new DecimalFormat("0.00");
                ArrayList<String> selectedPhones = new ArrayList<String>();
                ArrayList<Double> parziali = new ArrayList<Double>(); //lista totali per numero

                for(Telefono t : telefoni)
                {                        
                    if(t.Email.equals(utente.Email))
                    {
                        if(selectedPhones.contains(t.Numero)) //numero duplicato, devo sommare i costi
                        {
                            String expr = t.Numero.toString();
                            int startIndex = resultMessage.indexOf(expr);           
                            
                            String before = resultMessage.substring(0, startIndex);
                            String after = resultMessage.substring(startIndex, resultMessage.length() - 1);
                            for(Dispositivo d : dispositivi)
                            {
                                if(d.IdDispositivo == t.IdDispositivo)
                                {
                                    String replacement = "Dispositivo "+d.Nome+": € "+dbf.format(d.Costo)+"<br/>Dispositivo ";
                                    after = after.replaceFirst("Dispositivo",replacement);                                   
                                    Totale += d.Costo;
                                    
                                    //aggiorno la lista dei totali per numero con l'importo del dispositivo
                                    for(String phone : selectedPhones)
                                    {
                                        if(phone.equals(t.Numero))
                                        {
                                            int index = selectedPhones.indexOf(phone);
                                            parziali.set(index, parziali.get(index) + d.Costo);
                                        }
                                    }
                                }
                            }                   
                            resultMessage = before+after;
                            
                        }else{   
                            selectedPhones.add(t.Numero); //aggiungo nuovo numero     
                            
                            if(isFirst)
                            {
                                isFirst = false;
                            }else
                            {    
                                // dal 2° numero in poi inizio un nuovo messaggio 
                                baseMessage = messaggio.trim();
                                baseMessage = baseMessage.replace("Ringrazio e saluto cordialmente.", "");
                                int startIndex = baseMessage.indexOf("Telefono");                        
                                baseMessage = baseMessage.substring(startIndex,baseMessage.length());
                            }

                            baseMessage = baseMessage.replace("<telefono>", t.Numero);

                            boolean foundConsumo = false;

                            for(Consumo c : selectedConsumi)
                            {
                                if(c.Telefono.equals(t.Numero))
                                {    
                                    foundConsumo = true;

                                    if(t.IdDispositivo > 0)
                                    {
                                        for(Dispositivo d : dispositivi)
                                        {
                                            if(d.IdDispositivo == t.IdDispositivo)
                                            {
                                                baseMessage = baseMessage.replace("<dispositivoNome>",d.Nome);
                                                baseMessage = baseMessage.replace("<dispositivoCosto>",dbf.format(d.Costo));
                                                Totale += d.Costo;
                                            }
                                        }
                                    }else //nessun dispostivo
                                    {
                                        baseMessage = baseMessage.replace("Dispositivo <dispositivoNome>: € <dispositivoCosto>", "Dispositivo: nessuno dispositivo");
                                    }  

                                    if(t.IdContributo > 0)
                                    {
                                        for(Contributo co : contributi)
                                        {
                                            if(co.IdContributo == t.IdContributo)
                                            {
                                                baseMessage = baseMessage.replace("<contributiNome>",co.Nome);
                                            }
                                        }
                                    }else{
                                        baseMessage = baseMessage.replace("<contributiNome>","Contributo Ricaricabile Business");
                                    }

                                    baseMessage = baseMessage.replace("<contributi>", dbf.format(c.CRB));
                                    baseMessage = baseMessage.replace("<aaa>", dbf.format(c.AAA));
                                    baseMessage = baseMessage.replace("<abb>", dbf.format(c.ABB));

                                    Totale += c.Totale;
                                }
                            }

                            if(!foundConsumo)
                            {
                                if(t.IdDispositivo > 0)
                                {
                                    for(Dispositivo d : dispositivi)
                                    {
                                        if(d.IdDispositivo == t.IdDispositivo)
                                        {
                                            baseMessage = baseMessage.replace("<dispositivoNome>",d.Nome);
                                            baseMessage = baseMessage.replace("<dispositivoCosto>",dbf.format(d.Costo));
                                            Totale += d.Costo;
                                        }
                                    }
                                }else //nessun dispostivo
                                {
                                    baseMessage = baseMessage.replace("Dispositivo <dispositivoNome>: € <dispositivoCosto>", "Dispositivo: nessuno dispositivo");
                                }

                                baseMessage = baseMessage.replace("<contributiNome>","Contributo Ricaricabile Business");
                                baseMessage = baseMessage.replace("<contributi>","0.00");
                                baseMessage = baseMessage.replace("<aaa>", "0.00");
                                baseMessage = baseMessage.replace("<abb>", "0.00");                          

                            }
                            
                            //salvo il totale per questo numero     
                            Double parziale_i = Totale;
                            for(Double p : parziali)
                            {
                                parziale_i -= p;
                            }
                            parziali.add(parziale_i);
                            
                            resultMessage += baseMessage.replaceAll("&#010;","<br/>");
                        }                        
                    }                        
                }    
                
                 //imposto i totali per telefono
                for(Double p : parziali)
                {
                    resultMessage = resultMessage.replaceFirst("<totale_i>", dbf.format(p));
                }
                
                resultMessage = resultMessage.replace("<totale>", dbf.format(Totale));
                
                //imposto il fondo selezionando quello attivo
                Fondo fondoAttivo = null;
                for(Fondo f : fondi)
                {
                    if(f.Email.equals(utente.Email) && f.Attivo)
                        fondoAttivo = f;
                }
                if(fondoAttivo != null)
                    resultMessage = resultMessage.replace("<fondo>",fondoAttivo.Nome);
                
                //chiudo messaggio
                resultMessage = resultMessage + "Ringrazio  e saluto cordialmente.";
                resultMessage = resultMessage.replaceAll("€","&euro;");     
                
                message.setText(resultMessage);
                message.setContent(resultMessage, "text/html; charset=ISO-8859-1");
                Transport tr = session.getTransport("smtp");
                tr.connect("smtp.gmail.com",sender,password);
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
            }
        }
        catch(AuthenticationFailedException ex)
        {
            Logger.getLogger(MailManagement.class.getName()).log(Level.SEVERE, null, ex);
            setResult(EService.RECOVERABLE_ERROR);
            setErrorMessage("Autenticazione Fallita: la password inserita non è corretta.");
            if(database!=null)
            database.rollBack();
        }
        catch (MessagingException ex) 
        {
            Logger.getLogger(MailManagement.class.getName()).log(Level.SEVERE, null, ex);
            setResult(EService.RECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
            if(database!=null)
            database.rollBack();
        }
        catch (NotFoundDBException ex) 
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
        }catch(Exception ex)
        {
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
    
    public void saveDefaultMail()
    {
        messaggio = messaggio.replaceAll("\r\n", "&#010;");
        try {
            
            File file = new File(absolutePath + "/defaultMail.txt");    
            // if file doesnt exists, then create it
            if (!file.exists()) {
                    file.createNewFile();
            }
            
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is,"ISO-8859-1");    
            Writer w = new BufferedWriter(osw);             
                    
            w.write(messaggio);
            w.close();
            
            
        } catch (IOException ex) {
            setResult(EService.RECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));            
        }
        
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");            
            
            fatture = FatturaService.getFatture(database);
            
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
    
    private String getDefaultMail()
    {
        File file = new File(absolutePath+"/defaultMail.txt");
        String text = "";
        try{
            if (!file.exists()) {
                file.createNewFile();
                
                FileOutputStream is = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(is);    
                Writer w = new BufferedWriter(osw);
                
                SimpleDateFormat dateFormat = new SimpleDateFormat( "LLLL", Locale.getDefault() );
                String dataFattura = selectedFattura.Data;
                String[] splitted = dataFattura.split("-");
                Calendar cal = Calendar.getInstance();              
                cal.set(Integer.parseInt(splitted[2]),Integer.parseInt(splitted[1]),Integer.parseInt(splitted[0]));
                cal.add(Calendar.MONTH, -2);
                Date tempdate = cal.getTime();                
                String previous1 = dateFormat.format(tempdate);
                cal.add(Calendar.MONTH, -1);
                tempdate = cal.getTime();
                String previous2 = dateFormat.format(tempdate);
                
                text = "Gentile &lt;nome&gt; &lt;cognome&gt;,&#010;&#010;la sua quota per la fattura TIM del &lt;data&gt; (relativa al bimestre &lt;bimestre&gt;) &egrave; complessivamente di &euro; &lt;totale&gt;.&#010;La spesa verr&agrave; addebitata sul fondo &lt;fondo&gt; come da lei indicato precedentemente.&#010;Se desidera cambiare il fondo la prego di comunicarmelo, rispondendo a questa mail, entro una settimana.&#010;&#010;Le inviamo di seguito i dettagli:&#010;&#010;Telefono: &lt;telefono&gt;&#010;&#010;Dispositivo &lt;dispositivoNome&gt;: &euro; &lt;dispositivoCosto&gt;&#010;&lt;contributiNome&gt;  (Accesso Internet): &euro; &lt;contributi&gt;&#010;Altri addebiti e accrediti (Ricariche effettuate): &euro; &lt;aaa&gt;&#010;Abbonamenti: &euro; &lt;abb&gt;&#010;Totale: &euro; &lt;totale_i&gt;&#010;&#010;Ringrazio e saluto cordialmente.";
                        
                w.write(text);
                w.close();
            }else{            
                BufferedReader bufferedReader = 
                    new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"));
                String line = "";                
                while((line = bufferedReader.readLine()) != null) {
                    text += line;
                } 
                bufferedReader.close();   
            }
        } catch (FileNotFoundException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));            
        }catch (IOException ex) 
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));
        }catch(Exception ex)
        {
            setResult(EService.UNRECOVERABLE_ERROR);
            setErrorMessage(ex.getMessage().replace("Warning: ", ""));            
        }
        return text;
    }
    
    public String getAbsolutePath()
    {
        return this.absolutePath;
    }
    
    public void setAbsolutePath(String absolutePath)
    {
        this.absolutePath = absolutePath;
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
    
    public String getMessaggio()
    {
        return messaggio;
    }
    
    public void setMessaggio(String messaggio)
    {
        this.messaggio = messaggio;
    }
    
    public String getSender()
    {
        return sender;
    }
    
    public void setSender(String sender)
    {
        this.sender = sender;
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
