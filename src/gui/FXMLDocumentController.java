package gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import utils.Database;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.FlowPane;
import logika.Startup;
import logika.User;

/**
 *
 * @author Tým 5
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label detailMainLabel, usernameLabel, LabelKomentare;
    @FXML
    private Button buttonZpetNaDetail, buttonEditovatStartup, buttonSmazatStartup, buttonPridatStartupForm, ButtonMenu, ButtonSeznamStartupu;
    @FXML
    private AnchorPane MainFrame, Menu, Uzivatele, SeznamStartupu, Prihlasit, Registrovat, PridatStartupForm, Komentare, DetailStartupu, mapaAdd, mapaDetail, hodnoceni, komentareTable, uzivateleTable;
    @FXML
    private ListView viewStartupy;
    @FXML
    private TextField polohaXAdd, polohaYAdd, nazevAdd, emailAdd, polohaXDetail, polohaYDetail, nazevDetail, emailDetail, vyhledatField, usernameReg, username;
    @FXML
    private PasswordField password1, password2, password;
    @FXML
    private TextArea popisAdd, popisDetail, komentar;
    @FXML
    private ImageView imageViewMapAdd, imageViewMapDetail;
    @FXML
    private TableViewUzivatele tabulkaUzivatele;
    @FXML
    private TableViewKomentare tabulkaKomentare;
    @FXML
    private HodnoceniPane rating;
    @FXML
    private FlowPane UserBox;
    @FXML
    ObservableList<Hyperlink> startupy = FXCollections.observableArrayList();
    
    private Circle tecka;
    
    Database database   = new Database();
    User user           = null;
    Startup startup     = null;    
    
    
    // OBRAZOVKY
    @FXML
    private void MainFrame () {
        MainFrame.setVisible(true);
        SeznamStartupu.setVisible(false);
        Prihlasit.setVisible(false);
        Registrovat.setVisible(false);
        PridatStartupForm.setVisible(false);
        DetailStartupu.setVisible(false);
        Menu.setVisible(false);
        Uzivatele.setVisible(false);
        Komentare.setVisible(false);
        
        buttonZpetNaDetail.setVisible(false);
        ButtonMenu.setVisible(false);
        ButtonSeznamStartupu.setVisible(false);
        UserBox.setVisible(false);
        clean();
    } 
    @FXML
    private void Menu () {
        MainFrame.setVisible(true);
        SeznamStartupu.setVisible(false);
        Prihlasit.setVisible(false);
        Registrovat.setVisible(false);
        PridatStartupForm.setVisible(false);
        DetailStartupu.setVisible(false);
        Menu.setVisible(true);
        Uzivatele.setVisible(false);
        Komentare.setVisible(false);
        
        buttonZpetNaDetail.setVisible(false);
        ButtonMenu.setVisible(false);
        ButtonSeznamStartupu.setVisible(false);
        clean();
    }   
    @FXML
    private void Uzivatele () {
        
        if(user.getJeSpravce()){
            MainFrame.setVisible(false);
            SeznamStartupu.setVisible(false);
            Prihlasit.setVisible(false);
            Registrovat.setVisible(false);
            PridatStartupForm.setVisible(false);
            DetailStartupu.setVisible(false);
            Menu.setVisible(false);
            Uzivatele.setVisible(true);
            Komentare.setVisible(false);
            
            tabulkaUzivatele = new TableViewUzivatele(database);
            uzivateleTable.getChildren().clear();
            uzivateleTable.getChildren().add(tabulkaUzivatele);
            
            buttonZpetNaDetail.setVisible(false);
            ButtonMenu.setVisible(true);
            ButtonSeznamStartupu.setVisible(false);
            
            clean();
        } else{
            error("Do této sekce mají přístup jen správci!");
        }
    }   
    @FXML
    private void SeznamStartupu () {
        MainFrame.setVisible(false);
        SeznamStartupu.setVisible(true);
        Prihlasit.setVisible(false);
        Registrovat.setVisible(false);
        PridatStartupForm.setVisible(false);
        DetailStartupu.setVisible(false);
        Menu.setVisible(false);
        Uzivatele.setVisible(false);
        Komentare.setVisible(false);
        
        buttonZpetNaDetail.setVisible(false);
        if(user.getJeSpravce()){
            ButtonMenu.setVisible(true);
            ButtonSeznamStartupu.setVisible(false);   
            buttonPridatStartupForm.setVisible(true);
        } else{
            ButtonMenu.setVisible(false);
            ButtonSeznamStartupu.setVisible(false);
            buttonPridatStartupForm.setVisible(false);
        }
    
        startupy.clear();
        viewStartupy.setItems(startupy);        
        for(Startup startup : database.startupy()){
            Hyperlink nazev = new Hyperlink();
            nazev.setText(startup.getNazev());
            nazev.setOnAction((ActionEvent event) -> {
                DetailStartupu(startup.getNazev());
            });
            startupy.add(nazev);
        }
        clean();
    }
    @FXML
    private void DetailStartupu (String nazev) {
  
            MainFrame.setVisible(false);
            SeznamStartupu.setVisible(false);
            Prihlasit.setVisible(false);
            Registrovat.setVisible(false);
            PridatStartupForm.setVisible(false);
            Menu.setVisible(false);
            Uzivatele.setVisible(false);
            Komentare.setVisible(false);
            DetailStartupu.setVisible(true);
            
            startup = database.vyhledatStartup(nazev);
            
            detailMainLabel.setText(startup.getNazev());
            polohaXDetail.setText("" + startup.getPolohaX());
            polohaYDetail.setText("" + startup.getPolohaY());
            nazevDetail.setText(startup.getNazev());
            emailDetail.setText(startup.getKontakt());
            popisDetail.setText(startup.getPopis());           

                tecka = new Circle(5, Paint.valueOf("red"));
                mapaDetail.getChildren().clear();
                mapaDetail.getChildren().addAll(imageViewMapDetail, tecka);
                mapaDetail.setTopAnchor(tecka, startup.getPolohaY());
                mapaDetail.setLeftAnchor(tecka, startup.getPolohaX());
        
            
            rating = new HodnoceniPane(database, startup, user);
            hodnoceni.getChildren().clear();
            hodnoceni.getChildren().add(rating);
            
            
            buttonZpetNaDetail.setVisible(false);
            ButtonMenu.setVisible(false);
            ButtonSeznamStartupu.setVisible(true);
            
            if(user.getJeSpravce()){
                buttonEditovatStartup.setVisible(true);
                buttonSmazatStartup.setVisible(true);
            } else{
                buttonEditovatStartup.setVisible(false);
                buttonSmazatStartup.setVisible(false);
            }
    } 
    @FXML
    private void Komentare () {
        MainFrame.setVisible(false);
        SeznamStartupu.setVisible(false);
        Prihlasit.setVisible(false);
        Registrovat.setVisible(false);
        PridatStartupForm.setVisible(false);
        DetailStartupu.setVisible(false);
        Menu.setVisible(false);
        Uzivatele.setVisible(false);
        Komentare.setVisible(true);
        
        tabulkaKomentare = new TableViewKomentare(database, startup, user);
        komentareTable.getChildren().clear();
        komentareTable.getChildren().add(tabulkaKomentare);
        
        ButtonMenu.setVisible(false);
        ButtonSeznamStartupu.setVisible(false);
        
        buttonZpetNaDetail.setVisible(true);
        LabelKomentare.setText("Komentáře k startupu " + startup.getNazev());
        buttonZpetNaDetail.setOnAction((ActionEvent event) -> {
            DetailStartupu(startup.getNazev());
        });
    }   
    @FXML
    private void Registrovat () {
        MainFrame.setVisible(false);
        SeznamStartupu.setVisible(false);
        Prihlasit.setVisible(false);
        Registrovat.setVisible(true);
        PridatStartupForm.setVisible(false);
        DetailStartupu.setVisible(false);
        Menu.setVisible(false);
        Uzivatele.setVisible(false);
        Komentare.setVisible(false);
        
        buttonZpetNaDetail.setVisible(false);
        ButtonMenu.setVisible(false);
        ButtonSeznamStartupu.setVisible(false);
    }
    @FXML
    private void Prihlasit () {
        MainFrame.setVisible(false);
        SeznamStartupu.setVisible(false);
        Prihlasit.setVisible(true);
        Registrovat.setVisible(false);
        PridatStartupForm.setVisible(false);
        DetailStartupu.setVisible(false);
        Menu.setVisible(false);
        Uzivatele.setVisible(false);
        Komentare.setVisible(false);
        
        buttonZpetNaDetail.setVisible(false);
        ButtonMenu.setVisible(false);
        ButtonSeznamStartupu.setVisible(false);
    }
    @FXML
    private void PridatStartupForm () {
        
        if(user.getJeSpravce()){
            MainFrame.setVisible(false);
            SeznamStartupu.setVisible(false);
            Prihlasit.setVisible(false);
            Registrovat.setVisible(false);
            PridatStartupForm.setVisible(true);
            DetailStartupu.setVisible(false);
            Menu.setVisible(false);
            Uzivatele.setVisible(false);
            Komentare.setVisible(false);
            
            buttonZpetNaDetail.setVisible(false);
            ButtonMenu.setVisible(false);
            ButtonSeznamStartupu.setVisible(false);

        } else{
            error("Do této sekce mají přístup jen správci!");
        }
    }    
    //--------------------------------------------------------------------
    //  MAPA
    @FXML
    private void getPositionFromMapAdd () {  
        imageViewMapAdd.setOnMouseClicked(event -> {
            polohaXAdd.setText("" + event.getX());
            polohaYAdd.setText("" + event.getY());
            
            tecka = new Circle(5, Paint.valueOf("red"));
            mapaAdd.getChildren().clear();
            mapaAdd.getChildren().addAll(imageViewMapAdd, tecka);
            mapaAdd.setTopAnchor(tecka, event.getY());
            mapaAdd.setLeftAnchor(tecka, event.getX());
         });
    }
    @FXML
    private void getPositionFromMapDetail () {  
        imageViewMapDetail.setOnMouseClicked(event -> {
            polohaXDetail.setText("" + event.getX());
            polohaYDetail.setText("" + event.getY());
            
            tecka = new Circle(5, Paint.valueOf("red"));
            mapaDetail.getChildren().clear();
            mapaDetail.getChildren().addAll(imageViewMapDetail, tecka);
            mapaDetail.setTopAnchor(tecka, event.getY());
            mapaDetail.setLeftAnchor(tecka, event.getX());
         });
    }
    //-------------------------------------------------------------------------------------------   
    // DATABASE ACTIONS
    @FXML
    private void actionOdhlasit () {      
        user = null;
        MainFrame();
    } 
    @FXML
    private void actionRegistrovat () {
        
        if(usernameReg.getText().trim().isEmpty() || password1.getText().trim().isEmpty() || password2.getText().trim().isEmpty()){
            error("Musíš vyplnit všechna pole");              
        } else if(!password1.getText().trim().equals(password2.getText().trim())){                                 
            error("Hesla se neshodují");
        } else{
            if(database.registrovat(usernameReg.getText().trim(), password1.getText().trim())){
                user = database.prihlasit(usernameReg.getText().trim(), password1.getText().trim()); 
                SeznamStartupu();
                success("Byl jsi úspěšně zaregistrován!");
            } else{
                error("Registrace se nezdařila");
            }
        }
    }  
    @FXML
    private void actionPrihlasit () {
        
        if(username.getText().trim().isEmpty() || password.getText().trim().isEmpty()){
            error("Musíš vyplnit všechna pole");  
  
        } else{
            user = database.prihlasit(username.getText().trim(), password.getText().trim());
            if(user != null){
                clean();
                
                UserBox.setVisible(true);
                usernameLabel.setText(user.getUsername() + "   ");
                if(user.getJeSpravce()){
                    Menu();
                } else{
                    SeznamStartupu();
                }
            } else{
                error("Nesprávné uživatelské jméno nebo heslo");
            }
        }                   
    }       
    @FXML
    private void actionPridatStartup () {
        
        if(nazevAdd.getText().trim().isEmpty() || popisAdd.getText().trim().isEmpty() || emailAdd.getText().trim().isEmpty() || polohaXAdd.getText().trim().isEmpty() || polohaYAdd.getText().trim().isEmpty() || emailAdd.getText().trim().isEmpty()){
            error("Musíš vyplnit všechna pole");            
        } else {           
            String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(emailAdd.getText().trim());
            
            if (!matcher.matches()) {
                database.error("Neplatný tvar emailu");
                
            } else{
                database.pridatStartup(nazevAdd.getText(), emailDetail.getText().trim(), popisAdd.getText(), polohaXAdd.getText(), polohaYAdd.getText());
                clean();
            }           
        }
    } 
    @FXML
    private void actionEditovatStartup () {
        
        if(nazevDetail.getText().trim().isEmpty() || popisDetail.getText().trim().isEmpty() || emailDetail.getText().trim().isEmpty() || polohaXDetail.getText().trim().isEmpty() || polohaYDetail.getText().trim().isEmpty() || emailDetail.getText().trim().isEmpty()){
            error("Musíš vyplnit všechna pole");            
        } else {           
            String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(emailDetail.getText().trim());
            
            if (!matcher.matches()) {
                error("Neplatný tvar emailu");
                
            } else{
                startup = database.editovatStartup(startup.getId(), nazevDetail.getText().trim(), emailDetail.getText().trim(), popisDetail.getText().trim(), polohaXDetail.getText().trim(), polohaYDetail.getText().trim()); 
                DetailStartupu(startup.getNazev());
            }           
        }
    }
    @FXML
    private void actionVyhledatStartup () {  
        if(vyhledatField.getText().trim().isEmpty()){
            error("Vyplň prosím název startupu");            
        } else { 
            Startup startup = database.vyhledatStartup(vyhledatField.getText().trim());
            if(startup == null){
                error("Tento startup jsme nenalezli v databázi");
            } else{
                startupy.clear();
                Hyperlink nazev = new Hyperlink();
                nazev.setText(startup.getNazev());
                nazev.setOnAction((ActionEvent event) -> {
                    DetailStartupu(startup.getNazev());
                });
                startupy.add(nazev);      
                vyhledatField.clear();
            }
        }     
    }
    @FXML
    private void actionSmazatStartup(){
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Opravdu smazat?");

        Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                if(database.smazatStartup(startup.getId(), startup.getNazev())){
                    startup = null;
                    SeznamStartupu();
                    success("Startup byl úspěšně smazán");
                } else{
                    error("Smazání startupu se nezdařilo");
                }
            }         
    }
    @FXML
    private void actionPridatKomentar () {  
        if(komentar.getText().trim().isEmpty()){
            error("Nejdříve napiš komentář");            
        } else { 
            database.pridatKomentar(komentar.getText().trim(), user.getId(), startup.getId());
            clean();
        }     
    }
    //-----------------------------------------------------------------------------------  
    private void clean(){
        polohaXAdd.clear();
        polohaYAdd.clear();
        nazevAdd.clear();
        emailAdd.clear();
        popisAdd.clear();
               
        usernameReg.clear();
        username.clear();
        password1.clear();
        password2.clear();
        password.clear();
        
        vyhledatField.clear();
        komentar.clear();
        
        mapaAdd.getChildren().clear();
        mapaAdd.getChildren().add(imageViewMapAdd);
    }
    
    public void error(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba!");
        alert.setContentText(error);
        alert.showAndWait();
     }
    public void success(String success){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Úspěch!");
        alert.setContentText(success);
        alert.showAndWait();
     }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
}