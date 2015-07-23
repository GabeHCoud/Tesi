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
    
    public static ArrayList<String> splitLine1(String line)
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
    
    public static ArrayList<String> splitLine2(String line)
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
}
