/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logika;

/**
 *
 * @author Tým 5
 */
public class Startup {
    
    private String popis;
    private String nazev;
    private String kontakt;
    private double polohaX;
    private double polohaY;
    private int id;
    
    public Startup(int id, String nazev, String popis, String kontakt, double polohaX, double polohaY){
        this.id      = id;
        this.nazev   = nazev;
        this.popis   = popis;
        this.kontakt = kontakt;
        this.polohaX = polohaX;
        this.polohaY = polohaY;
    }  
    
    public Startup(int id, String nazev){
        this.id      = id;
        this.nazev   = nazev;
    }   

    public String getPopis() {
        return popis;
    }
    
    public int getId() {
        return id;
    }
    
    public String getNazev() {
        return nazev;
    }

    public String getKontakt() {
        return kontakt;
    }

    public double getPolohaX() {
        return polohaX;
    }

    public double getPolohaY() {
        return polohaY;
    }  
}
