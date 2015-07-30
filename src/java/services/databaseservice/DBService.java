
package services.databaseservice;

import java.sql.*;

import global.*;
import services.databaseservice.exception.*;

public class DBService extends Object {
  
  public DBService() {}

  public static synchronized DataBase getDataBase(String version) throws NotFoundDBException {

     try{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = null;
        if(version.equals("old"))
            connection = DriverManager.getConnection(Constants.DB_CONNECTION_STRING_1); 
        else if(version.equals("new"))
            connection = DriverManager.getConnection(Constants.DB_CONNECTION_STRING_2); 

        return new DataBase(connection);
      } catch (Exception e) {
        throw new NotFoundDBException("DBService: Impossibile creare la Connessione al DataBase: " + e);
      }
    }

    


}