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

public class StringMatcher {
    
    public static ArrayList<String> matches(String line, String regex)
    {
	ArrayList<String> splitted = new ArrayList<String>();
	
	Pattern p = Pattern.compile(regex);
        
	Matcher matcher = p.matcher(line);
        
	while (matcher.find())
	{
	    int i;
	    for(i = 1; i< matcher.groupCount();i++)
		splitted.add(matcher.group(i));
	}
	return splitted;
    }
}
