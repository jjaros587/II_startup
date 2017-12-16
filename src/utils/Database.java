/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.security.MessageDigest;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import logika.Komentar;
import logika.Startup;
import logika.User;
/**
 *
 * @author Tým 5
 */
public class Database implements Subject {
    
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
    private static final String DB_URL      = "jdbc:mysql://localhost/startupy";   
    private static final String USER        = "root";
    private static final String PASS        = "";
    
    private Connection con;
    private PreparedStatement stm;
    private ResultSet result;
    private String query;
    
    private List<Observer> listObserveru = new ArrayList<Observer>();
    
    public Database() {
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            //stm = con.createStatement();
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
            stm = null;
            stm = con.prepareStatement("SELECT * FROM users WHERE username=?");
            stm.setString(1, username);
            result = stm.executeQuery();
            if(result.next()){
                error("Toto uživatelské jméno již existuje.");
                
            } else{
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(password.getBytes());
                String encryptedPassword = new String(messageDigest.digest());
                               
                query = "INSERT INTO users (username, password, jeSpravce, smazano) VALUES (?, ?, '0', '0')";
                stm = null;
                stm = con.prepareStatement(query);
                stm.setString(1, username);
                stm.setString(2, encryptedPassword);
                stm.executeUpdate();
    
                stm = null; 
                stm = con.prepareStatement("SELECT * FROM users WHERE username=?");
                stm.setString(1, username);
                result = stm.executeQuery();
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
            stm = null;
            stm = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND smazano='0'");
            stm.setString(1, username);
            stm.setString(2, encryptedPassword);
            result = stm.executeQuery();
            
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
            stm = null;
            stm = con.prepareStatement("SELECT * FROM users WHERE smazano='0'");
            result = stm.executeQuery();
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
            stm = null;
            stm = con.prepareStatement("UPDATE users SET smazano='1' WHERE id_user=?");
            stm.setInt(1, id);
            stm.executeUpdate();
            notifyObservers();
            success("Uživatel byl úspěšně smazán");
            
        } 
          catch(Exception ex){
            error(ex.getMessage());
        }
    }
    public void zmenitRoli(int id, boolean jeSpravce){
        try {
            if(jeSpravce == true){
                query = "UPDATE users SET jeSpravce='0' WHERE id_user=?";
                
            } else{
                query = "UPDATE users SET jeSpravce='1' WHERE id_user=?";
            } 
            stm = null;
            stm = con.prepareStatement(query);
            stm.setInt(1, id);
            stm.executeUpdate();
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
            stm = null;
            stm = con.prepareStatement("SELECT id_startup, nazev FROM startups WHERE smazano='0'");
            result = stm.executeQuery();
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
            stm = null;
            stm = con.prepareStatement("INSERT INTO startups (nazev, kontakt, popis, polohaX, polohaY, smazano) VALUES (?, ?, ?, ?, ?, '0')");
            stm.setString(1, nazev);
            stm.setString(2, kontakt);
            stm.setString(3, popis);
            stm.setString(4, polohaX);
            stm.setString(5, polohaY);
            stm.executeUpdate();
            
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }
    public Startup editovatStartup(int id, String nazev, String kontakt, String popis, String polohaX, String polohaY){
        try {
            stm = null;
            stm = con.prepareStatement("UPDATE startups SET nazev=?, popis=?, kontakt=?, polohaX=?, polohaY=? WHERE id_startup=?");
            stm.setString(1, nazev);
            stm.setString(2, popis);
            stm.setString(3, kontakt);
            stm.setString(4, polohaX);
            stm.setString(5, polohaY);
            stm.setInt(6, id);
            stm.executeUpdate();
            
            return vyhledatStartup(nazev);
            
        } catch (Exception ex) {
            error(ex.getMessage());
        }
        return null;
    } 
    public Startup vyhledatStartup(String nazev){
        try {
            stm = null;
            stm = con.prepareStatement("SELECT * FROM startups WHERE nazev=? AND smazano='0'");
            stm.setString(1, nazev);
            result = stm.executeQuery();
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
            stm = null;
            stm = con.prepareStatement("UPDATE startups SET smazano='1' WHERE id_startup=?");
            stm.setInt(1, id);
            stm.executeUpdate();
            
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
            stm = null;
            stm = con.prepareStatement("SELECT comments.date AS date, comments.id_comment AS id_comment, comments.text AS text, users.username AS username FROM comments "
                    + "JOIN users ON comments.id_user=users.id_user "
                    + "WHERE comments.smazano='0' AND comments.id_startup=?");
            stm.setInt(1, idStartup);
            result = stm.executeQuery();
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
            stm = null;
            stm = con.prepareStatement("INSERT INTO comments (id_user, id_startup, date, text, smazano) VALUES (?, ?, ?, ?, '0')");
            stm.setInt(1, idUser);
            stm.setInt(2, idStartup);
            stm.setDate(3, date);
            stm.setString(4, text);
            stm.executeUpdate();
            
            notifyObservers();
            
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }
    public boolean smazatKomentar(int id){
        try {
            stm = null;
            stm = con.prepareStatement("UPDATE comments SET smazano='1' WHERE id_comment=?");
            stm.setInt(1, id);
            stm.executeUpdate();
            notifyObservers();
            success("Komentář byl úspěšně smazán");
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
            stm = null;
            stm = con.prepareStatement("SELECT avg(rating) AS hodnoceni FROM rating WHERE id_startup=?");
            stm.setInt(1, idStartup);
            result = stm.executeQuery();
            
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
            stm = null;
            stm = con.prepareStatement("SELECT id_rating FROM rating WHERE id_startup=? AND id_user=?");
            stm.setInt(1, idStartup);
            stm.setInt(2, idUser);
            result = stm.executeQuery();
  
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
            stm = null;
            stm = con.prepareStatement("DELETE FROM rating WHERE id_rating=?");
            stm.setInt(1, idRating);
            stm.executeUpdate();
            
            notifyObservers();
            
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }
    public void hodnotit(int idUser, int idStartup, String rating){
        try {
            stm = null;
            stm = con.prepareStatement("INSERT INTO rating (id_user, id_startup, rating) VALUES (?, ?, ?)");
            stm.setInt(1, idUser);
            stm.setInt(2, idStartup);
            stm.setString(3, rating);
            stm.executeUpdate();
            
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