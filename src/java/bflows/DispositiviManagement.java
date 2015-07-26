/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bflows;

import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author Massa
 */
public class DispositiviManagement implements Serializable {
    
    
    
     
    
    private String errorMessage;
    private int result;
    
    public DispositiviManagement(){}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
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
