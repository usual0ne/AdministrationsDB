package sample;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller implements Initializable
{

    @FXML
    private TableView<TableContent> table;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<TableContent, String> shortnameColumn;

    @FXML
    private TableColumn<TableContent, String> codeColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<TableContent, String> fullnameColumn;

    @FXML
    private TextField codeTextField;

    @FXML
    private TextField fullnameTextField;

    @FXML
    private TextField searchField;

    @FXML
    private Button editButton;

    @FXML
    private Button newButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextField shortnameTextField;

    private boolean EDIT = false, ADD = false;

    ObservableList<TableContent> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //Initialization
        initTable();

        try
        {
            ResultSet rs = DatabaseHandler.fillTable();

            while (rs.next())
            {
                oblist.add(new TableContent(rs.getString("Код_ЖА"),
                        rs.getString("Полное_наименование_ЖА"), rs.getString("Аббревиатура_ЖА")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        initTable();

        table.setItems(oblist);


        //Buttons
        editButton.setOnAction(e->
        {
            ADD = false; //if the edit button is pressed, then pass a false value to ADD
            EDIT = true;
            editAdministration();
        });

        saveButton.setOnAction(e->
        {
            saveAdministration();
        });

        newButton.setOnAction(e->
        {
            EDIT = false;
            ADD = true;
            insertNewAdministration();
        });

        deleteButton.setOnAction(e->
        {
            deleteAdministration();
        });

        searchFilter();
    }

    private void saveAdministration()
    {
        String code = codeTextField.getText();
        String fullname = fullnameTextField.getText();
        String shortname = shortnameTextField.getText();

        if(EDIT) // if edit button is pressed
        {
            DatabaseHandler.editQuery(code, fullname, shortname);
        }
        else if(ADD) // if add button is pressed
        {
            DatabaseHandler.addQuery(code, fullname, shortname);
        }


        codeTextField.setText("");
        fullnameTextField.setText("");
        shortnameTextField.setText("");

        refreshTable();

        ADD = true;
    }

    private void editAdministration()
    {
        TableContent selected = table.getSelectionModel().getSelectedItem();
        codeTextField.setText(selected.getpCode().get());
        fullnameTextField.setText(selected.getpFullname().get());
        shortnameTextField.setText(selected.getpShortname().get());
    }

    private void insertNewAdministration()
    {
        codeTextField.setText("");
        fullnameTextField.setText("");
        shortnameTextField.setText("");
    }

    private void deleteAdministration()
    {
        TableContent selected = table.getSelectionModel().getSelectedItem();
        String code = selected.getpCode().get();
        DatabaseHandler.deleteQuery(code);
        refreshTable();
    }

    private void initTable() //initialize table
    {
        codeColumn.setCellValueFactory(cell->cell.getValue().getpCode());
        fullnameColumn.setCellValueFactory(cell->cell.getValue().getpFullname());
        shortnameColumn.setCellValueFactory(cell->cell.getValue().getpShortname());
    }

    private void refreshTable()
    {
        initTable();
        searchFilter();
    }

    private void searchFilter()
    {
        //Wrap the ObservableList in a FilteredList
        FilteredList<TableContent> filteredData = new FilteredList<>(DatabaseHandler.updateValuesQuery(), b-> true);

        //Set the filter Predicate whenever the filter changes
        searchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate(employee -> {

                // If filter text is empty, display all persons
                if (newValue == null || newValue.isEmpty())
                {
                    return true;
                }

                // Compare fullname, shortname and code of every administration with filter text
                String lowerCaseFilter = newValue.toLowerCase();

                if (employee.getpFullname().get().toLowerCase().indexOf(lowerCaseFilter) != -1)
                {
                    return  true; //matches fullname
                }
                else if(employee.getpShortname().get().toLowerCase().indexOf(lowerCaseFilter) != -1)
                {
                    return true;
                }
                else if(employee.getpCode().get().toLowerCase().indexOf(lowerCaseFilter) != -1)
                {
                    return true;
                }
                else
                {
                    return false; //does not match
                }
            });
        }));

        //Wrap the FilteredList in a SortedList
        SortedList<TableContent> sortedData = new SortedList<>(filteredData);

        //Bind the SortedList comparator to the TableView comparator.
        //Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        //Add sorted (and filtered) data to the table.
        table.setItems(sortedData);
    }

}

