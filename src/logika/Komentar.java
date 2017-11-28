/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logika;

import java.sql.Date;

/**
 *
 * @author Acer
 */
public class Komentar {
       
    private int id;
    private String username;
    private Date date;
    private String text;
    
    public Komentar(int id, String username, Date date, String text){
        this.id         = id;
        this.username   = username;
        this.date       = date;
        this.text       = text;
    } 

    public int getId() {
        return id;
    }
}
