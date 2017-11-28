package gui;

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
import javafx.util.Callback;
import logika.Komentar;
import logika.User;
import utils.Database;
import utils.Observer;

public class TableViewUzivatele extends TableView<User> implements Observer{

    private final ObservableList<User> list = FXCollections.observableArrayList();
    private Database database;

    public TableViewUzivatele(Database database) {
        this.database = database;
        database.registerObserver(this);
        init();
        update();     
    }

    private void init() {
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setTranslateX(50);
        this.setTranslateY(100);
        this.setPrefWidth(600);
        this.setPrefHeight(600);
        
        TableColumn<User, String> username = new TableColumn<>("Username");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> jeSpravce = new TableColumn<>("Správce?");
        jeSpravce.setCellValueFactory(new PropertyValueFactory<>("jeSpravce"));

        this.getColumns().addAll(username, jeSpravce);
        this.setItems(list);
        
        addButtonZmenitRoli();
        addButtonSmazat();
    }
    
    @Override
    public void update() {
        list.clear();
        list.addAll(database.uzivatele());                      
    }

    private void addButtonSmazat() {
        TableColumn<User, Void> colBtn = new TableColumn("Action smazat");

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = (final TableColumn<User, Void> param) -> {
            final TableCell<User, Void> cell = new TableCell<User, Void>() {
                
                private final Button btn = new Button("Smazat");
                {
                    btn.setOnAction((ActionEvent event) -> {  
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation Dialog");
                        alert.setHeaderText("Look, a Confirmation Dialog");
                        alert.setContentText("Opravdu smazat?");

                        Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                User user = getTableView().getItems().get(getIndex());
                                database.deleteUser(user.getId());
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
    private void addButtonZmenitRoli() {
        TableColumn<User, Void> colBtn = new TableColumn("Action role");

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<User, Void>() {

                    private final Button btn = new Button("Změnit roli");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            User user = getTableView().getItems().get(getIndex());
                            database.zmenitRoli(user.getId(), user.getJeSpravce());
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
            }
        };
        colBtn.setCellFactory(cellFactory);
        this.getColumns().add(colBtn);
    }    
}