package prevblogics;

import java.sql.*;

import java.sql.*;
import services.databaseservice.*;
import services.databaseservice.exception.DuplicatedRecordDBException;
import services.databaseservice.exception.NotFoundDBException;
import services.databaseservice.exception.ResultSetDBException;


public class Text {
    public String mail;
    public String testo;
    public String data;
    
    public Text(String mail,String testo,String data)
    {
        this.mail=mail;
        this.testo=testo;
        this.data=data;
    }
    public Text(ResultSet resultSet) 
    {
        try {mail=resultSet.getString("mail");} catch (SQLException sqle) {}
        try {testo=resultSet.getString("testo");} catch (SQLException sqle) {}
        try {data=resultSet.getString("data");} catch (SQLException sqle) {}
    }
    public void insert(DataBase database)
  throws NotFoundDBException,DuplicatedRecordDBException,ResultSetDBException {
    
    String sql="";
    
    /* check di unicità */
    /*sql+= " SELECT * "+
          "   FROM text "+
          " WHERE " +
          "   data='"+ data+"'AND mail='"+mail+"'";
    
    ResultSet resultSet=database.select(sql);
    
    boolean exist=false;
    
    try {
      exist=resultSet.next();
      resultSet.close();
    } catch (SQLException e) {
      throw new ResultSetDBException("Text: insert(): Errore sul ResultSet.");
    }
    
    if (exist) {
      throw new DuplicatedRecordDBException("Text: insert(): Tentativo di inserimento di un messaggio già esistente.");
    }
    */
   System.out.println(mail+"#"+testo);
    sql=" INSERT INTO text(mail,testo,data)"
        +" values ('"+mail+"','"+testo+"','"+data+"')";
    
    System.out.println("forse modifico");
    
    database.modify(sql);
    
    System.out.println("ho modificato");
    
  }
  
} 

