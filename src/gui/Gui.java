/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.Main;

/**
 *
 * @author Jakub Jaroš
 */
public class Gui {
    
    private Stage primaryStage;
    private BorderPane mainBorder;
    private MenuLista menuLista;
    
    public Gui() {   
        this.primaryStage   = new Stage();
        this.mainBorder     = new BorderPane();
        init();       
    }
    
    public void init(){        
        menuLista = new MenuLista(Main.main);
        mainBorder.setTop(menuLista);
        
        Scene scene = new Scene(mainBorder, 1500, 1000);

        primaryStage.setTitle("Kurzy");
        primaryStage.setScene(scene);
        primaryStage.show();
    }      
}
