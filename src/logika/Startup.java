/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logika;

/**
 *
 * @author Jakub Jaroš
 */
public class Startup {
    
    private String popis;
    private String kontakt;
    private int polohaX;
    private int polohaY;
    
    public Startup(String popis, String kontakt, int polohaX, int polohaY){
        this.popis   = popis;
        this.kontakt = kontakt;
        this.polohaX = polohaX;
        this.polohaY = polohaY;
    }  

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public int getPolohaX() {
        return polohaX;
    }

    public void setPolohaX(int polohaX) {
        this.polohaX = polohaX;
    }

    public int getPolohaY() {
        return polohaY;
    }

    public void setPolohaY(int polohaY) {
        this.polohaY = polohaY;
    }
    
}
