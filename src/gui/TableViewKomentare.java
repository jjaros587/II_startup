package gui;

import java.sql.Date;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import logika.Komentar;
import logika.Startup;
import logika.User;
import utils.Database;
import utils.Observer;

public class TableViewKomentare extends TableView<Komentar> implements Observer{

    private final ObservableList<Komentar> list = FXCollections.observableArrayList();
    private Database database;
    private Startup startup;
    private User user;

    public TableViewKomentare(Database database, Startup startup, User user) {
        this.database   = database;
        this.startup    = startup;
        this.user       = user;
        database.registerObserver(this);
        init();
        update();     
    }

    private void init() {
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setTranslateX(50);
        this.setTranslateY(100);
        this.setPrefWidth(650);
        this.setPrefHeight(600);
        
        TableColumn<Komentar, String> username = new TableColumn<>("Username");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Komentar, Date> date = new TableColumn<>("Datum");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
       
        TableColumn<Komentar, String> text = new TableColumn<>("Text");
        text.setCellValueFactory(new PropertyValueFactory<>("text"));

        this.getColumns().addAll(username, date, text);
        this.setItems(list);
        
        if(user.getJeSpravce()){
            addButtonSmazat();
        }
    }
    
    @Override
    public void update() {
        list.clear();
        list.addAll(database.komentare(startup.getId()));                      
    }
    
    private void addButtonSmazat() {
        TableColumn<Komentar, Void> colBtn = new TableColumn("Action smazat");

        Callback<TableColumn<Komentar, Void>, TableCell<Komentar, Void>> cellFactory = (final TableColumn<Komentar, Void> param) -> {
            final TableCell<Komentar, Void> cell = new TableCell<Komentar, Void>() {
                
                private final Button btn = new Button("Smazat");
                {
                    btn.setOnAction((ActionEvent event) -> { 
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation Dialog");
                        alert.setHeaderText("Look, a Confirmation Dialog");
                        alert.setContentText("Opravdu smazat?");

                        Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                Komentar komentar = getTableView().getItems().get(getIndex());
                                database.smazatKomentar(komentar.getId());
                            }         
                    });
                }      
                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        };
        colBtn.setCellFactory(cellFactory);
        this.getColumns().add(colBtn);
    }
}