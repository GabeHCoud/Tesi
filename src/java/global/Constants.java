 
package global;

public class Constants {
   //scp Desktop/Tesi.war fattel@aries.ing.man:/home/fattel
     
  /* Constants for language codes */
  public static final String CD_LANGUAGE_ITALIAN = "ITA";
  public static final String CD_LANGUAGE_ENGLISH = "ENG";
              
  /* Contants for Mail Service.
   * Choose between (YES,NO,TEST).
  */ 
  public static final String ENABLE_MAIL_SERVICE = "NO";
  public static final String TEST_MAIL = "ncl.massa@gmail.com";//TODO: modifica
  public static final String[] MAIL_GATEWAYS = {"151.99.250.122"};
 
  /* Constants for Debug */
  public static final boolean DEBUG=true;  
  
  /** Constants for db connection */
  public static final String DB_USER_NAME         = "fattel";
  public static final String DB_PASSWORD          = "FattureTelecom2015";  
  public static final String DB_CONNECTION_STRING_1 = "jdbc:mysql://localhost:3306/fattel?user="+DB_USER_NAME+"&password="+DB_PASSWORD;
  public static final String DB_CONNECTION_STRING_2 = "jdbc:mysql://localhost:3306/fattel2?user="+DB_USER_NAME+"&password="+DB_PASSWORD;

//  public static final String DB_USER_NAME         = "fattel";
//  public static final String DB_PASSWORD          = "FattureTelecom2015";  
//  public static final String DB_CONNECTION_STRING = "jdbc:mysql://localhost:3306/fattel?user="+DB_USER_NAME+"&password="+DB_PASSWORD;
//  
  /* Constants for log files*/
//public final static String LOG_DIR = "Users/Nicola/Documents/NetBeans Projects/log";//TODO: modifica
  public final static String LOG_DIR = "/var/lib/tomcat7/webapps/fattel2/";
  public final static String FRONTEND_ERROR_LOG_FILE = LOG_DIR + "frontenderror.log";
  public final static String FATAL_LOG_FILE = LOG_DIR + "fatalerror.log";
  public final static String GENERAL_LOG_FILE = LOG_DIR + "generalerror.log";  
  public final static String GENERAL_EXCEPTION_LOG_FILE = LOG_DIR + "generalexception.log";
  public final static String WARNING_LOG_FILE = LOG_DIR + "warning.log";  
  public final static String DATABASE_SERVICE_LOG_FILE = LOG_DIR + "databaseservice.log";  
  public final static String MAIL_SERVICE_LOG_FILE = LOG_DIR + "mailservice.log";
  public static final String APPLICATION_MANAGER_MAIL = "posenato.angela@gmail.com";

}