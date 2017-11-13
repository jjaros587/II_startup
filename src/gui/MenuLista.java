/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import main.Main;

/**
 *
 * @author Jakub Jaroš
 */
public class MenuLista extends MenuBar{
    
    private Main main;
    
    /**
    *  Konstruktor třídy
    *  
    *  @param hra 
    *  @param main
    */ 
    public MenuLista(Main main){
        this.main = main;
        init();
        
    }
    /**
    *  Úvodní nastavení menu
    *  
    */
    private void init(){
                  
            Menu hraMenu = new Menu("Hra");           
                MenuItem itemNova = new MenuItem("Nová hra");
                itemNova.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
                
                MenuItem itemKonec = new MenuItem("Konec hry");                
            hraMenu.getItems().addAll(itemNova, itemKonec);
                
                itemNova.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event){
                        
                    }
                });     

                itemKonec.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event){
                        System.exit(0);
                    }
                });
            
                
            Menu napovedaMenu = new Menu("Nápověda");
            
                MenuItem oProgramu = new MenuItem("O programu");
                    oProgramu.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            
                        }
                    }); 
                    
                MenuItem napoveda = new MenuItem("Nápověda");
                    napoveda.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            
                        }
                    }); 
            napovedaMenu.getItems().addAll(oProgramu, napoveda);
            
        this.getMenus().addAll(hraMenu, napovedaMenu);       
    }
}
