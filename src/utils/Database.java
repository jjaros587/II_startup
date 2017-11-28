/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.security.MessageDigest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import logika.Komentar;
import logika.Startup;
import logika.User;
/**
 *
 * @author Jakub Jaroš
 */
public class Database implements Subject {
    
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
    /*
    private static final String DB_URL      = "jdbc:mysql://sql2.freemysqlhosting.net:3306/sql2205189";   
    private static final String USER        = "sql2205189";
    private static final String PASS        = "wU2%aY7*";
    */
    private static final String DB_URL      = "jdbc:mysql://localhost/startupy";   
    private static final String USER        = "root";
    private static final String PASS        = "";
    
    private Connection con;
    private Statement stm;
    private ResultSet result;
    private String query;
    
    private List<Observer> listObserveru = new ArrayList<Observer>();
    
    public Database() {
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stm = con.createStatement();
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
    
    public void error(String error)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(error);
        alert.showAndWait();
    }
    
    public void success(String success)
     {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(success);
        alert.showAndWait();
     }
    
    //--------------------------------------------------------------------------------------------------
    // USERS
    public boolean registrovat(String username, String password){
        try {
            
            query = "SELECT * FROM users WHERE username='" + username + "'";
            result = stm.executeQuery(query);
            if(result.next()){
                error("Toto uživatelské jméno již existuje.");
                
            } else{
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(password.getBytes());
                String encryptedPassword = new String(messageDigest.digest());
                               
                query = "INSERT INTO users (username, password, jeSpravce, smazano) VALUES ('" + username + "', '" + encryptedPassword + "', '0', '0')";
                stm.executeUpdate(query);
                
                query = "SELECT * FROM users WHERE username='" + username + "'";
                result = stm.executeQuery(query);
                if(result.next()){
                    return true;
                }
   
            }           
        } catch (Exception ex) {
            error(ex.getMessage());
        }
        return false;
    } 
    public User prihlasit(String username, String password){
        try {
            
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            String encryptedPassword = new String(messageDigest.digest());
            
            query = "SELECT * FROM users WHERE username='" + username + "' AND password='" + encryptedPassword + "' AND smazano='0'";
            result = stm.executeQuery(query);
            
            if(result.next()){
                User user = new User(
                    result.getInt("id_user"),
                    result.getString("username"),
                    result.getString("password"),
                    result.getBoolean("jeSpravce") 
                );
                return user;              
            }  
            return null;
        } catch (Exception ex) {
            error(ex.getMessage());
        }
        return null;
    } 
    public ObservableList<User> uzivatele(){
        try {
            query = "SELECT * FROM users WHERE smazano='0'";
            result = stm.executeQuery(query);
            User uzivatel;
            int id;
            ObservableList <User> list = FXCollections.observableArrayList();

            while(result.next()){

                uzivatel = new User(result.getInt("id_user"), result.getString("username"), result.getString("password"), result.getBoolean("jeSpravce"));
                list.add(uzivatel);
            }
            return list;
        } 
        catch(Exception ex){
            System.out.println(ex);
        }
        return null;
    }
    public void deleteUser(int id){
        try {
            query = "UPDATE users SET smazano='1' WHERE id_user='"+ id +"'";
            stm.executeUpdate(query);
            notifyObservers();
            
        } 
          catch(Exception ex){
            error(ex.getMessage());
        }
    }
    public void zmenitRoli(int id, boolean jeSpravce){
        try {
            if(jeSpravce == true){
                query = "UPDATE users SET jeSpravce='0' WHERE id_user='"+ id +"'";
                
            } else{
                query = "UPDATE users SET jeSpravce='1' WHERE id_user='"+ id +"'";
            } 
            stm.executeUpdate(query);
            notifyObservers();
        } 
          catch(Exception ex){
            error(ex.getMessage());
        }
    }
    //-------------------------------------------------------------------------------
    // STARTUPS
    public ObservableList<Startup> startupy(){
        try {
            query = "SELECT id_startup, nazev FROM startups WHERE smazano='0'";
            result = stm.executeQuery(query);
            Startup startup;
            ObservableList <Startup> list = FXCollections.observableArrayList();
            while(result.next()){
                
                startup = new Startup(result.getInt("id_startup"), result.getString("nazev"));
                list.add(startup);
            }
            return list;
        } 
        catch(Exception ex){
            System.out.println(ex);
        }
        return null;
    }    
    public void pridatStartup(String nazev, String kontakt, String popis, String polohaX, String polohaY){
        try {
            query = "INSERT INTO startups (nazev, kontakt, popis, polohaX, polohaY, smazano) VALUES ('" + nazev + "', '" + kontakt + "','" + popis + "', '" + polohaX + "', '" + polohaY + "', '0')";
            stm.executeUpdate(query);
            
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }
    public Startup editovatStartup(int id, String nazev, String kontakt, String popis, String polohaX, String polohaY){
        try {
            query = "UPDATE startups SET nazev='" + nazev + "', popis='" + popis + "', kontakt='" + kontakt + "', polohaX='" + polohaX + "', polohaY='" + polohaY + "' WHERE id_startup='" + id + "'";
            System.out.println(query);
            stm.executeUpdate(query);
            
            return vyhledatStartup(nazev);
            
        } catch (Exception ex) {
            error(ex.getMessage());
        }
        return null;
    } 
    public Startup vyhledatStartup(String nazev){
        try {
            query = "SELECT * FROM startups WHERE nazev='"+ nazev +"' AND smazano='0'";
            result = stm.executeQuery(query);
            Startup startup;
            ObservableList <Startup> list = FXCollections.observableArrayList();
            if(!result.next()){
                return null;
            } else{
               
                    startup = new Startup(result.getInt("id_startup"), result.getString("nazev"), result.getString("popis"), result.getString("kontakt"), result.getDouble("polohaX"), result.getDouble("polohaY"));
                    list.add(startup);
                
                return startup;
            }
        } 
          catch(Exception ex){
            error(ex.getMessage());
        }
        return null;
    }
    public boolean smazatStartup(int id, String nazev){
        try {
            query = "UPDATE startups SET smazano='1' WHERE id_startup='"+ id +"'";
            stm.executeUpdate(query);
            
            Startup startup = vyhledatStartup(nazev);
            if(startup == null){
                return true;
            }
        } 
          catch(Exception ex){
            error(ex.getMessage());
        }
        return false;
    }
    //----------------------------------------------------------------------------------------------------
    // KOMENTARE
    public ObservableList<Komentar> komentare(int idStartup){
        try {
            query = "SELECT comments.date AS date, comments.id_comment AS id_comment, comments.text AS text, users.username AS username FROM comments "
                    + "JOIN users ON comments.id_user=users.id_user "
                    + "WHERE comments.smazano='0' AND comments.id_startup='" + idStartup + "'";

            result = stm.executeQuery(query);
            Komentar komentar;
            ObservableList <Komentar> list = FXCollections.observableArrayList();
            while(result.next()){
                
                komentar = new Komentar(result.getInt("id_comment"), result.getString("username"), result.getDate("date"), result.getString("text"));
                list.add(komentar);
            }
            return list;
        } 
        catch(Exception ex){
            System.out.println(ex);
        }
        return null;
    }
    public void pridatKomentar(String text, int idUser, int idStartup){
        try {
            Date date = new Date(new java.util.Date().getTime());
            query = "INSERT INTO comments (id_user, id_startup, date, text, smazano) VALUES ('" + idUser + "', '" + idStartup + "','" + date + "', '" + text + "', '0')";
            stm.executeUpdate(query);
            
            notifyObservers();
            
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }
    public boolean smazatKomentar(int id){
        try {
            query = "UPDATE comments SET smazano='1' WHERE id_comment='"+ id +"'";
            stm.executeUpdate(query);
            notifyObservers();
            success("Startup byl úspěšně smazán");
        } 
          catch(Exception ex){
            error(ex.getMessage());
        }
        return false;
    }
    //--------------------------------------------------------------------------------------------------
    // HODNOCENI
    public double hodnoceni(int idStartup){
        try {
            query = "SELECT avg(rating) AS hodnoceni FROM rating WHERE id_startup='"+ idStartup +"'";
            result = stm.executeQuery(query);
            
            if(result.next()){
                return result.getDouble("hodnoceni");
            }   
        } 
          catch(Exception ex){
            error(ex.getMessage());
        }
        return 0;
    }
    public int hodnoceniUzivatelem(int idUser, int idStartup){
        try {
            query = "SELECT id_rating FROM rating WHERE id_startup='"+ idStartup +"' AND id_user='"+ idUser +"'";
            result = stm.executeQuery(query);
  
            if(result.next()){
                return result.getInt("id_rating");
            }  
        } 
          catch(Exception ex){
            error(ex.getMessage());
        }
        return 0;
    }
    public void smazatHodnoceni(int idRating){
        try {
            query = "DELETE FROM rating WHERE id_rating='"+ idRating +"'";
            stm.executeUpdate(query);
            
            notifyObservers();
            
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }
    public void hodnotit(int idUser, int idStartup, String rating){
        try {
            query = "INSERT INTO rating (id_user, id_startup, rating) VALUES ('"+ idUser +"', '"+ idStartup +"', '"+ rating +"')";
            stm.executeUpdate(query);
            
            notifyObservers();
            
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }
    //----------------------------------------------------------------------------------------------
    // OBSERVERS
    @Override
    public void registerObserver(Observer observer) {
        listObserveru.add(observer);
    }
    /**
     * Zrušení observeru
     * @param observer Observer
     */
    @Override
    public void removeObserver(Observer observer) {
        listObserveru.remove(observer);
    }
    /**
     * Oznámení observeru
     */   
    @Override
    public void notifyObservers() {
        for (Observer listObserveruItem : listObserveru) {
            listObserveruItem.update();
        }
    }
}      
        
      
