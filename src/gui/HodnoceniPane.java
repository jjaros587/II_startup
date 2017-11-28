package gui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import logika.Startup;
import logika.User;
import utils.Database;
import utils.Observer;

public class HodnoceniPane extends BorderPane implements Observer{

    private Database database;
    private Startup startup;
    private User user;

    public HodnoceniPane(Database database, Startup startup, User user) {
        this.database   = database;
        this.startup    = startup;
        this.user       = user;
        database.registerObserver(this);
        init();
        update();     
    }

    private void init() {
        Label nadpis = new Label("Hodnocení\n\n");
        nadpis.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        this.setTop(nadpis);
    }
    
    @Override
    public void update() {
        Label label = new Label();
        label.setPrefWidth(100);
        double rating = database.hodnoceni(startup.getId());
        if(rating == 0){
            label.setText("Nehodnoceno   ");
            label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        } else{
            label.setText(rating + " / 5   ");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        }
        this.setLeft(label);
        
        if(database.hodnoceniUzivatelem(user.getId(), startup.getId()) == 0){
            FlowPane flowPane = new FlowPane();
            TextField field = new TextField();
            
            Button button = new Button("Hodnotit");
            button.setOnAction((ActionEvent event) -> {
                String text = field.getText().trim();
                if(text.isEmpty()){
                    database.error("Vyplň prosím hodnocení");
                } else if(text.equals("1") || text.equals("2") || text.equals("3") || text.equals("4") || text.equals("5")){
                    database.hodnotit(user.getId(), startup.getId(), text);
                } else{
                    database.error("Přípustné jsou pouze hodnoty 1, 2, 3, 4 a 5. Číslo 5 je nejvyšší hodnocení.");
                }
            });
            
            flowPane.getChildren().addAll(field, button);
            this.setRight(flowPane);
            
        } else{
            Button button = new Button("Smazat hodnocení");
            button.setOnAction((ActionEvent event) -> {   
                database.smazatHodnoceni(database.hodnoceniUzivatelem(user.getId(), startup.getId()));
            });
            
            this.setRight(button);
        }                  
    }
}