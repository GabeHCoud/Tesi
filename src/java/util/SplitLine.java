/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SplitLine {
    
    public static ArrayList<String> splitConsumo1(String line)
    {
        ArrayList<String> splitted = new ArrayList<String>();
        ArrayList<Pattern> patterns = new ArrayList<Pattern>(
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
    
    public static ArrayList<String> splitConsumo2(String line)
    {
        ArrayList<String> splitted = new ArrayList<String>();
        ArrayList<Pattern> patterns = new ArrayList<Pattern>(
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
    
    public static ArrayList<String> splitNewLinea(String line)
    {
	ArrayList<String> splitted = new ArrayList<String>();
        ArrayList<Pattern> patterns = new ArrayList<Pattern>(
            Arrays.asList(
                Pattern.compile("(?:\\bLinea\\b\\s)"), 
                Pattern.compile("(\\d{10})")
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
    
    public static ArrayList<String> splitNewConsumo(String line)
    {
	ArrayList<String> splitted = new ArrayList<String>();
        ArrayList<Pattern> patterns = new ArrayList<Pattern>(
            Arrays.asList(		    
                Pattern.compile("((?:\\w+\\s|\\w+-\\w+\\s)+)(\\d{2}\\/\\d{2}\\/\\d{4}\\s)"), 
		Pattern.compile("((?:\\D+\\s)+)(\\d{2}\\/\\d{2}-\\d{2}\\/\\d{2}\\s)"),
		Pattern.compile("(\\d+\\,\\d+)")
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
    
    public static ArrayList<String> splitNewRicarica(String line)
    {
	ArrayList<String> splitted = new ArrayList<String>();
        ArrayList<Pattern> patterns = new ArrayList<Pattern>(
            Arrays.asList(
                Pattern.compile("(Ricariche(?:\\s\\w+)+)"), 
                Pattern.compile("(\\s\\d\\s)"),
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
    
    public static ArrayList<String> splitNewTotale(String line)
    {
	ArrayList<String> splitted = new ArrayList<String>();
        ArrayList<Pattern> patterns = new ArrayList<Pattern>(
            Arrays.asList(
                Pattern.compile("(Totale\\s+)"), 
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
}
