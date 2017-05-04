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
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import services.databaseservice.DBService;
import services.databaseservice.DataBase;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;
import services.errorservice.EService;
import services.errorservice.FatalError;
import util.SplitLine;

public class FattureManagement implements Serializable {   
    
    private ArrayList<String> lines;
    private InputStream inputStream;
    private java.io.File outputFile;
    private Fattura fattura;
    private ArrayList<Consumo> consumi;    
    private ArrayList<User> utenti;
    private String errorMessage;
    private int result;
    private String data;
    private double totale;
    private double contributi;
    private double altri;
    
    public FattureManagement() {}   
   
    
    public void processPDF()
    {
//        Document pdf = null;
        BufferedWriter writer = null;
        consumi = new ArrayList<Consumo>();        
        lines = new ArrayList<String>();
        
        try{
	    // Salvo file temporaneo per debugging
            outputFile = new File("C:\\Users\\nklma\\Documents\\NetBeansProjects\\temp", "temp.txt"); 
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));            
            //FileOutputStream fileOutputStream = new FileOutputStream("extracted.txt");
            
            
            // iText Library      
            PdfReader pdfReader = new PdfReader(inputStream);
            for (int page = 1; page <= pdfReader.getNumberOfPages(); page++) 
            {               
                SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                String currentText = PdfTextExtractor.getTextFromPage(pdfReader, page,strategy);
                String[] l = currentText.split("\n");
                for(int i=0;i<l.length;i++)
                    lines.add(l[i]);                
            }
            pdfReader.close();   
	    
	    for(String line : lines)
	    {
		writer.write(line);
		writer.newLine();
	    }
	    
	    writer.close();    
        
            boolean startPointFound = false;
            boolean dateFound = false;
            boolean totaleFound = false;
            boolean contributiFound = false;
            boolean altriFound = false;
            
            
            Consumo consumo = null;             

            for(String line : lines)
            {
                //recupero data fattura
                if(!dateFound)
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
			dateFound = true;
                    }  
                }       
                
                //recupero totale fattura con iva
                if(!totaleFound)
                {
                    if(line.contains("IMPORTO"))
                    {
                        String cleanLine = line.replaceAll("\\s+"," ").replaceAll("_","");
                        String importo  = cleanLine.replace("IMPORTO: ","").replace("Euro","").trim();
                        totale = Double.parseDouble(importo.replace(".","").replace(",","."));
                        totaleFound = true;
                    }
                }
                
                //recupero importo contributi e abbonamenti
                if(!contributiFound)
                {
                    if(line.contains("CONTRIBUTI E ABBONAMENTI"))
                    {
                        String cleanLine = line.replaceAll("\\s+"," ");
                        String importo  = cleanLine.replace("CONTRIBUTI E ABBONAMENTI ","");
                        contributi = Double.parseDouble(importo.replace(".","").replace(",","."));
                        contributiFound = true;
                    }
                }
                
                //recupero importo altri addebiti e accrediti
                if(!altriFound)
                {
                    if(line.contains("ALTRI ADDEBITI E ACCREDITI"))
                    {
                        String cleanLine = line.replaceAll("\\s+"," ");
                        String importo  = cleanLine.replace("ALTRI ADDEBITI E ACCREDITI ","");
                        altri = Double.parseDouble(importo.replace(".","").replace(",","."));
                        altriFound = true;
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
		
		if(data.contains("2017"))
		{
		    // ------------------------------
		    // PER FATTURE SUCCESSIVE AL 2017
		    // ------------------------------
		    if(line.matches("(?:Linea\\s)(\\d{10})"))
		    {
			// Nuovo consumo
			ArrayList<String> splitted = SplitLine.splitNewLinea(line);

			if(consumo != null) //salvo la precedente
			{    
			    consumi.add(consumo);
			}
			//creo un nuovo consumo
			consumo = new Consumo();
			StringBuilder str = new StringBuilder(splitted.get(0)); 
			str.insert(3,"-");
			consumo.Telefono = str.toString();
		    }

		    if(line.matches("((?:(?:\\w+\\s)+\\w+-)?(?:\\w+\\s)+)(\\d{2}\\/\\d{2}\\/\\d{4})\\s((?:\\w+\\s)+)(\\d{2}\\/\\d{2}\\-\\d{2}\\/\\d{2})\\s(\\d+,\\d+)"))
		    {
			// Contributi o abbonamenti
			ArrayList<String> splitted = SplitLine.splitNewConsumo(line);

			if(consumo != null)
			{
			    if(splitted.get(2).contains("Contributi"))
				consumo.CRB += Double.parseDouble(splitted.get(4).replace(",","."));
			    else if(splitted.get(2).contains("Abbonamenti"))
				consumo.ABB += Double.parseDouble(splitted.get(4).replace(",","."));
			}
		    }

		    if(line.matches("(Ricariche(?:\\s\\w+)+)(\\s\\d\\s)(\\d+,\\d+)"))
		    {
			// Ricariche
			ArrayList<String> splitted = SplitLine.splitNewRicarica(line);

			if(consumo != null)
			{
			    consumo.AAA += Double.parseDouble(splitted.get(2).replace(",","."));
			}
		    }

		    if(line.matches("(Totale\\s+)(\\d+,\\d+)"))
		    {
			// Totale
			ArrayList<String> splitted = SplitLine.splitNewTotale(line);

			if(consumo != null)
			{
			    consumo.Totale += Double.parseDouble(splitted.get(1).replace(",","."));
			}
		    }   
		}else
		{
		    // ------------------------------
		    // PER FATTURE PRECEDENTI AL 2017
		    // ------------------------------

		    // Linea e consumo
		    if(line.matches("(\\d{3}(\\s+)?-(\\s+)?\\d{7})((?:\\s+)(?:\\w+\\s+)+)(\\d+,\\d+)")) 
		    // (3 digits)(optional whitespaces)-(optional whitespaces)(7 digits)
		    // (any number of whitespaces)(any number of words followed by whitespace)(1+ digits),(1+digits)
		    {                        
			//elimino gli spazi nel numero di telefono
			line = line.replace(" - ","-");

			//se entro qui significa che inizia un consumo
			ArrayList<String> splitted = SplitLine.splitConsumo1(line);   

			//esiste un consumo con lo stesso numero quindi i dati vanno aggiunti
			if(consumo != null && splitted.get(0).replaceAll("\\s+","").equals(consumo.Telefono)) //è il continuo del precedente
			{
			    if(splitted.get(1).contains("Contributi"))
				consumo.CRB = Double.parseDouble(splitted.get(2).replace(",","."));
			    else if(splitted.get(1).contains("Altri"))
				consumo.AAA = Double.parseDouble(splitted.get(2).replace(",","."));
			    else if(splitted.get(1).contains("Abbonamenti"))
				consumo.ABB = Double.parseDouble(splitted.get(2).replace(",","."));
			}else
			{
			    //non esiste un consumo con il numero letto
			    if(consumo != null) //salvo la precedente
			    {    
				consumi.add(consumo);
			    }
			    //creo un nuovo consumo
			    consumo = new Consumo();
			    consumo.Telefono = splitted.get(0); 

			    if(splitted.get(1).contains("Contributi"))
				consumo.CRB = Double.parseDouble(splitted.get(2).replace(",","."));
			    else if(splitted.get(1).contains("Altri"))
				consumo.AAA = Double.parseDouble(splitted.get(2).replace(",","."));
			    else if(splitted.get(1).contains("Abbonamenti"))
				consumo.ABB = Double.parseDouble(splitted.get(2).replace(",","."));
			}
		    }   

		    if(line.matches("((?:\\w+\\s+)+)(\\d+,\\d+)"))//(any number of words followed by whitespaces)(1+ digits),(1+ digits)
		    {                  
			//continua la fattura precedente                    
			ArrayList<String> splitted = SplitLine.splitConsumo2(line); 

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

            //outputFile.delete();
        }catch(IOException ex){
            EService.logAndRecover(ex);
            setResult(EService.UNRECOVERABLE_ERROR);            
            setErrorMessage("FattureManagement.ProcessPDF(): "+ex.getMessage());
        }catch(NumberFormatException ex)
        {
            EService.logAndRecover((FatalError) ex);
            setResult(EService.UNRECOVERABLE_ERROR);            
            setErrorMessage("FattureManagement.ProcessPDF(): "+ex.getMessage());
        }
    }   
    
    public void processCSV() 
    {              
        DataBase database = null;                    
        ArrayList<String> elementi= new ArrayList<String>();
        String s=null,file=null,data=null;
        consumi = new ArrayList<Consumo>();        
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
    
    public void insert()
    {
        DataBase database = null;        
        
        try 
        {
            database=DBService.getDataBase("new");
                       
            //creo nuova fattura
            if(data == null)
                FatturaService.insertNewFattura(database, "",totale,contributi,altri);            
            else            
                FatturaService.insertNewFattura(database,data,totale,contributi,altri);    
            
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
        consumi = new ArrayList<Consumo>();
        
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
    
    public double getTotale()
    {
        return totale;
    }
    
    public void setTotale(double totale)
    {
        this.totale = totale;
    }
    
    public double getContributi ()
    {
        return contributi;
    }
    
    public void setContirbuti(double contributi)
    {
        this.contributi = contributi;
    }
    
    public double getAltri ()
    {
        return altri;
    }
    
    public void setAltri(double altri)
    {
        this.altri = altri;
    }
}
