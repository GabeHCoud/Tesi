/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bflows;

import blogics.ExtractedPdfPage;
import blogics.Fattura;
import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.Page;
import java.beans.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Massa
 */
public class FattureManagement implements Serializable {   
    
    private ArrayList<String> lines;
    private InputStream inputStream;
    private File outputFile;
    private ArrayList<ExtractedPdfPage> textualPdf;
    private ArrayList<Fattura> fatture;
    
    
    public FattureManagement() {}
    
    public void processPDF()
    {
        Document pdf = null;
        BufferedWriter writer = null;
        fatture = new ArrayList<>();
        
        try{
            outputFile = File.createTempFile("result", ".txt"); 
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
            pdf = PDF.open(inputStream,"prova.pdf");     
            
            int pages = pdf.getPageCnt();            
            for(int j=0;j<pages;j++){
                Page pagePDF = pdf.getPage(j);                
                pagePDF.pipe(new OutputTarget(writer));                
                writer.newLine();
                writer.newLine();                
                
                writer.append("$$$$NEWPAGE$$$");
                               
                writer.newLine();
                writer.newLine();    
                
                writer.flush();
            }                        
            pdf.close();            
            writer.close();
            
        }catch(IOException e){
            System.out.println("TestBean.process: "+e.getMessage());
        }
        
        BufferedReader br = null;
        textualPdf = new ArrayList<>();        
        lines = new ArrayList<>();
        try {

                String sCurrentLine;

                br = new BufferedReader(new FileReader(outputFile.getAbsolutePath()));               
                
                while ((sCurrentLine = br.readLine()) != null) {
                    if(sCurrentLine.isEmpty())
                        continue;
                    
                    if(!sCurrentLine.contains("$$$$NEWPAGE$$$"))
                        lines.add(sCurrentLine.trim());
                    else
                    {
                        textualPdf.add(new ExtractedPdfPage(lines));
                        lines.clear();
                    }                    
                }
                
                br.close();

        } catch (IOException e) {
                System.out.println("TestBean.process: "+e.getMessage());
        } 
        
        boolean startPointFound = false;
        Fattura fattura = null;
        for(int i=0;i<textualPdf.size();i++)
        {                               
            for(String line : textualPdf.get(i).getLines())
            {
                //RIEPILOGO UTENZA marks the start point of data
                if(line.contains("RIEPILOGO PER UTENZA"))
                    startPointFound = !startPointFound;

                if(!startPointFound) continue;                    

                //SERVIZI OPZIONALI marks the end point of data
                if(line.matches("SERVIZI OPZIONALI")) return;

                if(line.matches("(\\d{3}-\\d{7})((?:\\s+)(?:\\w+\\s+)+)(\\d+,\\d+)")) //(3 digits)-(7 digits)(any number of whitespaces)(any number of word followed by whitespace)(1+ digits),(1+digits)
                //if(line.matches("((\\d{3})-(\\d{7}))(\\s+)(\\w+\\s+)+(\\d+),(\\d+)")) 
                {                        
                    //se entro qui significa che inizia una nuova fattura
                    ArrayList<String> splitted = splitLine1(line);   
                    
                    if(fattura != null) //salvo la precedente
                        fatture.add(fattura);
                    
                    fattura = new Fattura();
                    fattura.Telefono = splitted.get(0);                    
                    
                    if(splitted.get(1).contains("Contributi"))
                        fattura.CRB = Double.parseDouble(splitted.get(2).replace(",","."));
                    else if(splitted.get(1).contains("Altri"))
                        fattura.AAA = Double.parseDouble(splitted.get(2).replace(",","."));
                    else if(splitted.get(1).contains("Abbonamenti"))
                        fattura.ABB = Double.parseDouble(splitted.get(2).replace(",","."));
                    
                }   

                if(line.matches("((?:\\w+\\s+)+)(\\d+,\\d+)"))//(any number of words followed by whitespaces)(1+ digits),(1+ digits)
                //if(line.matches("(\\w+\\s+)+(\\d+),(\\d+)")) 
                {                  
                    //continua la fattura precedente                    
                    ArrayList<String> splitted = splitLine2(line); 
                    
                    if(fattura != null){
                        if(splitted.get(0).contains("Contributi"))
                            fattura.CRB = Double.parseDouble(splitted.get(1).replace(",","."));
                        else if(splitted.get(0).contains("Altri"))
                            fattura.AAA = Double.parseDouble(splitted.get(1).replace(",","."));
                        else if(splitted.get(0).contains("Abbonamenti"))
                            fattura.ABB = Double.parseDouble(splitted.get(1).replace(",","."));
                        else if(splitted.get(0).contains("Totale"))
                            fattura.Totale = Double.parseDouble(splitted.get(1).replace(",","."));
                    }
                } 
            }                
        }     
        
    }     
    
    
    public void processCSV() throws IOException
    {              
        ArrayList<String> elementi= new ArrayList<>();
        ArrayList<Integer> ok=new ArrayList<>();
        ArrayList<Fattura> fatture= new ArrayList<>();
        
        String s=null,file=null,data=null;
        
        DataInputStream in = new DataInputStream(inputStream);
        while(true)
        {
            s=in.readLine();
            if(s==null)
                break;
            file=file+"\n"+s;
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
                fatture.add(new Fattura(elementi.get(i),data,contributi,aaa,abb,totale));
                j++;
                f++;
            }
        }
    }
    
    public ArrayList<String> splitLine1(String line)
    {
        ArrayList<String> splitted = new ArrayList<>();
        ArrayList<Pattern> patterns = new ArrayList<>(
            Arrays.asList(
                Pattern.compile("(\\d{3}-\\d{7})"), 
                Pattern.compile("((?:\\s+)(?:\\w+\\s+)+)"), 
                Pattern.compile("(\\d+,\\d+)")
            )
        );
        
        Matcher matcher;
        for(Pattern p : patterns)
        {
            matcher = p.matcher(line);
            while (matcher.find()) {
            splitted.add(matcher.group().trim());
            }  
        }  
        
        return splitted;
    }
    
    public ArrayList<String> splitLine2(String line)
    {
        ArrayList<String> splitted = new ArrayList<>();
        ArrayList<Pattern> patterns = new ArrayList<>(
            Arrays.asList(
                Pattern.compile("((?:\\w+\\s+)+)"), 
                Pattern.compile("(\\d+,\\d+)")
            )
        );
        
        Matcher matcher;
        for(Pattern p : patterns)
        {
            matcher = p.matcher(line);
            while (matcher.find()) {
            splitted.add(matcher.group().trim());
            }  
        }  
        
        return splitted;
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
    
    public ArrayList<ExtractedPdfPage> getTextualPdf()
    {
        return textualPdf;
    }
    
    public void setTextualPdf(ArrayList<ExtractedPdfPage> textualPdf)
    {
        this.textualPdf = textualPdf;
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
}
