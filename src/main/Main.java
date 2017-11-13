/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


import gui.Gui;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;


/**
 *
 * @author Jakub Jaroš
 */
public class Main extends Application {  

    public static Main main;
    

    @Override
    public void start(Stage primaryStage) {
        
        Gui grafika = new Gui();
      
    }  
   
    public static void main(String[] args) {
        
        launch(args); 
    }
}
