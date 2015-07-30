/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bflows;

import blogics.ExtractedPdfPage;
import blogics.Consumo;
import blogics.ConsumoService;
import blogics.Fattura;
import blogics.FatturaService;
import blogics.Telefono;
import blogics.TelefonoService;
import blogics.User;
import blogics.UserService;
import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.Page;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import services.databaseservice.DBService;
import services.databaseservice.DataBase;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;
import services.errorservice.EService;
import util.SplitLine;

public class FattureManagement implements Serializable {   
    
    private ArrayList<String> lines;
    private InputStream inputStream;
    private java.io.File outputFile;
    private Fattura fattura;
    private ArrayList<Consumo> consumi;    
    ArrayList<User> utenti;
    private String errorMessage;
    private int result;
    private String data;
    
    public FattureManagement() {}   
   
    
    public void processPDF()
    {
        Document pdf = null;
        BufferedWriter writer = null;
        consumi = new ArrayList<>();        
        
        try{
            outputFile = File.createTempFile("result", ".txt"); 
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
            pdf = PDF.open(inputStream,"prova.pdf");     
           
            int pages = pdf.getPageCnt();            
            for(int j=0;j<pages;j++){
                Page pagePDF = pdf.getPage(j);                
                pagePDF.pipe(new OutputTarget(writer)); 
                writer.flush();
            }                        
            pdf.close();            
            writer.close();
            
        }catch(IOException e){
            System.out.println("TestBean.process: "+e.getMessage());
        }
        
        BufferedReader br = null;
        lines = new ArrayList<>();
        try {
                String sCurrentLine;

                br = new BufferedReader(new FileReader(outputFile.getAbsolutePath()));               
                
                while ((sCurrentLine = br.readLine()) != null) {
                    if(sCurrentLine.isEmpty())
                        continue;
                    
                    lines.add(sCurrentLine.trim());                                
                }
                
                br.close();

        } catch (IOException e) {
                System.out.println("TestBean.process: "+e.getMessage());
        }         
        
        boolean startPointFound = false;
        boolean dataFound = false;
        Consumo consumo = null;             
        
        for(String line : lines)
        {
            //recupero data fattura
            if(!dataFound)
            {
                if(line.contains("Emessa"))
                {
                    String cleanLine = line.replaceAll("\\s+"," ");
                    String[] splitted = cleanLine.split(" ");
                    for(String s :splitted){
                        if(s.contains("/"))
                        {
                            s = s.replace("/","-");
                            data = s;
                        }                            
                    }
                }  
            }          
            
            //RIEPILOGO UTENZA segna l'inizio della tabella
            if(line.contains("RIEPILOGO PER UTENZA"))
                startPointFound = !startPointFound;

            if(!startPointFound) continue;                    

            //SERVIZI OPZIONALI segna la fine della tabella
            if(line.matches("SERVIZI OPZIONALI")) 
            {
                consumi.add(consumo);
                return;
            }

            if(line.matches("(\\d{3}-\\d{7})((?:\\s+)(?:\\w+\\s+)+)(\\d+,\\d+)")) //(3 digits)-(7 digits)(any number of whitespaces)(any number of word followed by whitespace)(1+ digits),(1+digits)
            //if(line.matches("((\\d{3})-(\\d{7}))(\\s+)(\\w+\\s+)+(\\d+),(\\d+)")) 
            {                        
                //se entro qui significa che inizia una nuova fattura
                ArrayList<String> splitted = SplitLine.splitLine1(line);   

                if(consumo != null) //salvo la precedente
                {    
                    consumi.add(consumo);
                }
                consumo = new Consumo();
                consumo.Telefono = splitted.get(0); 
                
                if(splitted.get(1).contains("Contributi"))
                    consumo.CRB = Double.parseDouble(splitted.get(2).replace(",","."));
                else if(splitted.get(1).contains("Altri"))
                    consumo.AAA = Double.parseDouble(splitted.get(2).replace(",","."));
                else if(splitted.get(1).contains("Abbonamenti"))
                    consumo.ABB = Double.parseDouble(splitted.get(2).replace(",","."));
            }   

            if(line.matches("((?:\\w+\\s+)+)(\\d+,\\d+)"))//(any number of words followed by whitespaces)(1+ digits),(1+ digits)
            //if(line.matches("(\\w+\\s+)+(\\d+),(\\d+)")) 
            {                  
                //continua la fattura precedente                    
                ArrayList<String> splitted = SplitLine.splitLine2(line); 

                if(consumo != null){
                    if(splitted.get(0).contains("Contributi"))
                        consumo.CRB = Double.parseDouble(splitted.get(1).replace(",","."));
                    else if(splitted.get(0).contains("Altri"))
                        consumo.AAA = Double.parseDouble(splitted.get(1).replace(",","."));
                    else if(splitted.get(0).contains("Abbonamenti"))
                        consumo.ABB = Double.parseDouble(splitted.get(1).replace(",","."));
                    else if(splitted.get(0).contains("Totale"))
                        consumo.Totale = Double.parseDouble(splitted.get(1).replace(",","."));
                }   
            }   
        }    
    }   
    
    public void processCSV() 
    {              
        DataBase database = null;                    
        ArrayList<String> elementi= new ArrayList<>();
        String s=null,file=null,data=null;
        consumi = new ArrayList<>();        
        DataInputStream in = new DataInputStream(inputStream);
        
        try{
            while(true)
            {
                s=in.readLine();
                if(s==null)
                    break;
                file=file+"\n"+s;
            }          
        }catch(IOException ex)
        {
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);
            return;
        }
                
        String []d= file.split("\n"); //separo le righe
        String [] d1;
        int n=d.length,m=0,el=0;
        System.out.println("split a capo"+n);

        for(int i=0;i<n;i++)
        {
           d1=d[i].split(";"); //separo i campi
           m=d1.length;
           for(int j=0;j<m;j++)
           {
                elementi.add(d1[j]);//salvo nell'arraylist                     
           }
        }
        
        try
        {
            database=DBService.getDataBase("new");

            for(int i=0;i<elementi.size();i++)//scorro l'array stringhe
            {
                String a1="300-0000000";
                String a2="400-0000000";

                int f=0;
                if((elementi.get(i).compareTo(a1)>0) && (elementi.get(i).compareTo(a2)<0) && (elementi.get(i).contains("-"))) //seleziono i numeri di telefono
                {
                    double aaa=0,totale=0,contributi=0,abb=0;
                    String conv;

                    int j=i+1;
                    while((elementi.get(j).contains("-"))==false)
                    {
                        if(elementi.get(j).contains("Contributi"))
                        {
                            conv=elementi.get(j+1).replaceAll(",",".");
                            contributi=Double.parseDouble(conv);
                        }
                        if(elementi.get(j).contains("Altri"))
                        {
                            conv=elementi.get(j+1).replaceAll(",",".");
                            aaa=Double.parseDouble(conv);
                        }
                        if(elementi.get(j).contains("Totale"))
                        {
                            conv=elementi.get(j+1).replaceAll(",",".");
                            totale=Double.parseDouble(conv);                                    
                        }
                        if(elementi.get(j).contains("Abbonamenti"))
                        {
                            conv=elementi.get(j+1).replaceAll(",",".");
                            abb=Double.parseDouble(conv);
                        }
                        j++;
                    }

                    Telefono t = TelefonoService.getTelefono(database, elementi.get(i));
                    if(t != null)                    
                        consumi.add(new Consumo(elementi.get(i),contributi,aaa,abb,totale,t.Email,-1));
                    else
                        consumi.add(new Consumo(elementi.get(i),contributi,aaa,abb,totale,null,-1));
                    j++;
                    f++;
                }
            }
            
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
    
    public void insert()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
                       
            //creo nuova fattura
            if(data == null)
                FatturaService.insertNewFattura(database, "");            
            else            
                FatturaService.insertNewFattura(database, data);    
            
            database.commit();            
            
            if(data != null)//recupero fattura generata da PDF            
                fattura = FatturaService.getFatturaByDate(database,data);
            else//recupero fattura generata da CSV
                fattura = FatturaService.getLatestFattura(database);
            
            if(consumi != null)
            {
                for(Consumo c : consumi)
                {                    
                    Telefono t = TelefonoService.getTelefono(database, c.Telefono);
                    if(t != null)
                        c.Email = t.Email;
                    
                    ConsumoService.InsertNewConsumo(database,c.Telefono,c.CRB,c.AAA,c.ABB,c.Totale,c.Email,fattura.IdFattura);                    
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
        } 
        catch (DuplicatedRecordDBException ex) 
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
    
    public void setDate()
    {
        DataBase database = null; 
        consumi = new ArrayList<>();
        
        String[] splitted = data.split("-");//inverto data
        data = splitted[2] + "-" + splitted[1] + "-" + splitted[0];
        
        try 
        {
            database=DBService.getDataBase("new");  
            
            //controllo che non esista già una fattura per la data selezionata
            fattura = FatturaService.getFatturaByDate(database, data);
            
            if(fattura != null)
            {
                fattura = FatturaService.getLatestFattura(database);
                fattura.delete(database);
                
                database.commit();
                
                setResult(EService.RECOVERABLE_ERROR);
                setErrorMessage("Esiste già una fattura per la data inserita");                
            }else{            
                fattura = FatturaService.getLatestFattura(database);
                consumi = ConsumoService.getConsumiByFatturaId(database, fattura.IdFattura);

                if(fattura != null)
                {
                    fattura.Data = data;
                    fattura.update(database);
                }

                database.commit();
            }
            
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
    
    public void cleanDB()
    {
        DataBase database = null;        
        ArrayList<Fattura> fatture;
        try 
        {
            database=DBService.getDataBase("new");
            
            fatture = FatturaService.getFatture(database);
            for(Fattura f : fatture)
            {
                if(f.Data.isEmpty())
                    f.delete(database);
            }
            
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
    
    public String getLine(int i)
    {
        return lines.get(i);
    }
    
    public void setLine(int i,String line)
    {
        lines.set(i, line);
    }
    
    public ArrayList<String> getLines()
    {
        return lines;
    }
    
    public void setLines(ArrayList<String> lines)
    {
        this.lines = lines;
    }
    
    public InputStream getInputStream()
    {
        return inputStream;
    }
    
    public void setInputStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }
    
    public File getOutputFile()
    {
        return outputFile;
    }
    
    public void setOutputFile(File outputFile)
    {
        this.outputFile = outputFile;
    }
    
    public Fattura getFattura()
    {
        return fattura;
    }
    
    public void setFattura(Fattura fattura)
    {
        this.fattura = fattura;
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
    
    public String getData ()
    {
        return data;
    }
    
    public void setData(String data)
    {
        this.data = data;
    }
}
