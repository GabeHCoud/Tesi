/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blogics;

import java.util.ArrayList;

/**
 *
 * @author Massa
 */
public class ExtractedPdfPage {
    private ArrayList<String> lines;
    
    public ExtractedPdfPage(ArrayList<String> lines){
        this.lines = new ArrayList<>(lines);              
    }
    
    public ArrayList<String> getLines()
    {
        return lines;
    }        
}
