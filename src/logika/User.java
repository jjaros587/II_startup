/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logika;

/**
 *
 * @author Acer
 */
public class User {
    
    private int id;
    private String username;
    private String password;
    private boolean jeSpravce;
    
    public User(int id, String username, String password, boolean jeSpravce){
        this.id         = id;
        this.username   = username;
        this.password   = password;
        this.jeSpravce  = jeSpravce;
    }

    public int getId() {
        return id;
    }
    
    public boolean getJeSpravce() {
        return jeSpravce;
    }
    
    public String getUsername() {
        return username;
    }
}
