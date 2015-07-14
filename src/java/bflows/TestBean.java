/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bflows;

import blogics.ExtractedPdfPage;
import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.Page;
import com.snowtide.pdf.VisualOutputTarget;
import com.snowtide.pdf.layout.BlockParent;
import java.beans.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Massa
 */
public class TestBean implements Serializable {
    
    private ArrayList<String> lines;
    private InputStream inputStream;
    private File outputFile;
    private ArrayList<ExtractedPdfPage> textualPdf;
    
    public TestBean(){}
    
//    public void process2()
//    {
//        Document pdf = null;
//        BufferedWriter writer = null;
//        
//        try{
//            outputFile = File.createTempFile("result", ".txt"); 
//            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
//            pdf = PDF.open(inputStream,"prova.pdf");     
//            
//            int pages = pdf.getPageCnt();            
//            for(int j=0;j<pages;j++){
//                Page pagePDF = pdf.getPage(j);
//                pagePDF.pipe(new VisualOutputTarget(writer));
//                
//                writer.newLine();
//                writer.newLine();                
//                for(int k=0;k<200;k++)
//                {
//                    writer.append("$");
//                }                
//                writer.newLine();
//                writer.newLine();    
//                
//                writer.flush();
//            }                        
//            pdf.close();            
//            writer.close();
//            
//        }catch(IOException e){
//            System.out.println("TestBean.process: "+e.getMessage());
//        }finally{
//            
//        }
//    }
    
    public void process()
    {
        Document pdf = null;
        BufferedWriter writer = null;
        
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
        }finally{
            
        }
    }
    
    public void readFile()
    {
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

        } catch (IOException e) {
                System.out.println("TestBean.readFile: "+e.getMessage());
        } finally {
                try {
                        if (br != null)br.close();
                } catch (IOException ex) {
                        ex.printStackTrace();
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
}
