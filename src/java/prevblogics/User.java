package prevblogics;

import java.sql.*;

import java.sql.*;
import services.databaseservice.*;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;

/**
 * Classe che gestisce le entità in gioco nelle classi User
 * @author Posenato Angela
 */
public class User {

    public String nome;
    public String cognome;
    public String telefono;
    public String mail;
    

  public User( String nome, String cognome, String telefono, String mail)
  {
      
      this.nome=nome;
      this.cognome=cognome;
      this.mail=mail;
      this.telefono=telefono;
  }

/**
   * Settaggio dell'User su risposta del Database
   * @param resultSet contiene il risultato della chiamata al Database
*/
  public User(ResultSet resultSet) {
    
    
    try {nome=resultSet.getString("nome");} catch (SQLException sqle) {}
    try {cognome=resultSet.getString("cognome");} catch (SQLException sqle) {}
    try {telefono=resultSet.getString("telefono");} catch (SQLException sqle) {}
    try {mail=resultSet.getString("mail");} catch (SQLException sqle) {}
 }
  
  
  /**
   * Funzione che inserisce un User nel Database
   * @param database attributo che indica il Database
   * @throws NotFoundDBException
   * @throws DuplicatedRecordDBException
   * @throws ResultSetDBException 
   */
  public void insert(DataBase database)
  throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {
    
    String sql="";
    
    /* check di unicità */
    /*sql+= " SELECT telefono "+
          "   FROM utente AS U "+
          " WHERE " +
          "   U.mail='"+ mail+"' ";
    
    ResultSet resultSet=database.select(sql);
    
    boolean exist=false;
    
    try {
      exist=resultSet.next();
      resultSet.close();
    } catch (SQLException e) {
      throw new ResultSetDBException("User: insert(): Errore sul ResultSet.");
    }
    
    if (exist) {
      throw new DuplicatedRecordDBException("User: insert(): Tentativo di inserimento di un contatto già esistente.");
    }
    */
   System.out.println(nome+cognome);
    sql=" INSERT INTO utente(nome,cognome,telefono,mail)"
        +" values ('"+nome+"','"+cognome+"','"+telefono+"','"+mail+"')";
    
    System.out.println("forse modifico");
    
    database.modify(sql);
    
    System.out.println("ho modificato");
    
  }
  
} 
 