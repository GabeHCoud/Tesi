/*
 * Session.java
 *
 */

package services.sessionservice;

import java.util.*;
import javax.servlet.http.*;

//import util.*;
import services.errorservice.*;
import services.databaseservice.*;
import services.databaseservice.exception.*;

//import blogics.*;
/*
public class Session {
  
  public Session() {}
  
  public static Cookie[] createCookie(DataBase database, String mail) 
  throws NotFoundDBException,ResultSetDBException {
    
    Cookie[] cookies=new Cookie[2];
    Cookie cookie;
    
    User user=UserService.getUtente(database,mail);
    
    cookie=new Cookie("usercode",""+user.usercode);
    cookies[0]=cookie;
    
    cookie=new Cookie("userName",user.nome+"#"+user.cognome);
    cookies[1]=cookie;
    
    for(int i=0; i<cookies.length; i++) {
      cookies[i].setPath("/");
    }
    
    return cookies;
  }
  public static Cookie[] createAdminCookie(DataBase database,String mail)
          throws NotFoundDBException,ResultSetDBException{
    Cookie cookie;
    Cookie[] cookies = new Cookie[2];
    
    User user = UserService.getAdmin(database, mail);
    
    //Cookie(name,value) dove name e value sono String
    //faccio il cast di id a String
    cookie=new Cookie("admin",""+user.usercode);
    cookies[0] = cookie;
    cookie=new Cookie("userName",user.nome+"#"+user.cognome);
    cookies[1]=cookie;
    
    for (int i=0;i<cookies.length;i++)
        cookies[i].setPath("/");
    return cookies;
  }
  
  public static String getValue(Cookie cookies[], String name, int position) {
    
    int i;
    boolean found=false;
    String value=null;
    
    if (cookies != null) { 
    for (i=0;i<cookies.length && !found;i++)
      if (cookies[i].getName().equals(name)) {
        Vector oV = util.Conversion.tokenizeString(cookies[i].getValue(),"#");
        value=(String)oV.elementAt(position);
        found=true;
      }        
    }
    return value;
    
  }
  public static String getValue(Cookie cookies[],String name) 
  {
      String cvalue = null;
      boolean found = false;
      if ( cookies != null) {
          for (int i = 0;i < cookies.length && !found; i++) 
              if (cookies[i].getName().equals(name)) 
              {
                  cvalue = cookies[i].getValue();
                  found = true;
                         
              }
      }
      
      return cvalue;
  }
  
  public static String getIdUtente(Cookie[] cookies) {
    return getValue(cookies, "usercode", 0);
  }
  
   public static String getIdAdmin(Cookie[] cookies) {
    return getValue(cookies, "admin", 0);
  }

  public static String getUserCode(Cookie[] cookies) {
    return getValue(cookies, "usercode", 0);
  }
  
  public static String getUserFirstname(Cookie[] cookies) {
    return getValue(cookies, "userName", 0);
  }
  
  public static String getUserSurname(Cookie[] cookies) {
    return getValue(cookies, "userName", 1);
  } 
    
  public static Cookie[] deleteCookie(Cookie[] cookies) {
    
    for (int i=0; i<cookies.length; i++) {
      cookies[i].setMaxAge(0);
      cookies[i].setPath("/");
    }
    
    return cookies;
  }  
  
  public static void showCookies(Cookie[] cookies){
    
    util.Debug.println("Cookie presenti:" + cookies.length);
    int i;
    for (i=0;i< cookies.length;i++)
      util.Debug.println("Nome:" + cookies[i].getName() + " Valore:" +cookies[i].getValue());
  }
  
}*/