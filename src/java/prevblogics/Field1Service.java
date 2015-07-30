package prevblogics;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import services.databaseservice.*;
import services.databaseservice.exception.*;



public class Field1Service 
{
public Field1Service() {}
    
public static void insert(DataBase database,String telefono,double contributi,double aaa,double abb,double totale,String data) throws NotFoundDBException, DuplicatedRecordDBException, ResultSetDBException
{
    Field1 field= new Field1(telefono,contributi,aaa,abb,totale,data);        
    field.insert(database);
}

public static void delete(DataBase database) throws NotFoundDBException 
 {
     String sql="DELETE FROM tab_1";
     database.modify(sql); 
 }

public static void modifyField(DataBase database,String data) throws NotFoundDBException
{
    String sql="UPDATE tab_1 SET data='"+data+"'";
    database.modify(sql);
}
public static void editField(DataBase database,String telefono, double contributi,double aaa,double abb,double totale) throws NotFoundDBException
{
    System.out.println(telefono+contributi+" "+aaa+" "+totale);
    String sql="UPDATE tab_1 SET crb='"+contributi+"',aaa='"+aaa+"' ,abb='"+abb+"' ,totale='"+totale+"' WHERE telefono='"+telefono+"'";
    database.modify(sql);
}
public static ArrayList<Field1> View(DataBase database)
    throws NotFoundDBException,ResultSetDBException 
{
    Field1 f;
    ArrayList<Field1> field = new ArrayList<Field1>();
    Boolean c = false;
    int i = 0;

    String sql=" SELECT * " +
                "   FROM tab_1 " ;
    
    ResultSet resultSet = database.select(sql);
    try 
    {
        while (resultSet.next()) 
        { 
            
            field.add(new Field1(resultSet));
            
        }
        resultSet.close();
    } catch (SQLException ex) 
    {
      throw new ResultSetDBException("FieldService: View():  ResultSetDBException: "+ex.getMessage(), database);
    }
    return field;
}
public static ArrayList<Field1> Search(DataBase database, String telefono)
    throws NotFoundDBException,ResultSetDBException {

    ArrayList<Field1> f = new ArrayList<Field1>();
    Boolean c = false;
    int i = 0;

    String sql=" SELECT * " +
                "   FROM tab_1 " +
                " WHERE " +
                "   telefono= '"+telefono+"'";
    
    ResultSet resultSet = database.select(sql);
    try {
      while (resultSet.next()) { 
          Field1 field = new Field1(resultSet);
          f.add(field);
         
      }
      resultSet.close();
    } catch (SQLException ex) {
      throw new ResultSetDBException("FieldService: Search():  ResultSetDBException: "+ex.getMessage(), database);
    }
 
return f;   
}
public int getYear() {
    Calendar date = Calendar.getInstance();
    SimpleDateFormat ft = new SimpleDateFormat("YYYY");
    int y = Integer.parseInt(ft.format(date.getTime()).toString());
    return y;
}
    
public String getDate(int data) {
    Calendar date = Calendar.getInstance();
    SimpleDateFormat ft = new SimpleDateFormat("dd/MM/YYYY");
    date.add(Calendar.DAY_OF_MONTH, data);
    String y = ft.format(date.getTime()).toString();
    return y;
}
    
}
