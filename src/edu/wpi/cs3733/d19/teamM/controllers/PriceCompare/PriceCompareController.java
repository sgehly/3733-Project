package edu.wpi.cs3733.d19.teamM.controllers.PriceCompare;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PriceCompareController {

    public class DataItem implements Comparable{

        SimpleStringProperty price;
        SimpleStringProperty name;
        SimpleStringProperty procedure;

        public DataItem(String price, String name, String procedure){
            this.price = new SimpleStringProperty(price);
            this.name = new SimpleStringProperty(name);
            this.procedure = new SimpleStringProperty(procedure);
        }

        public String getPrice() {
            return price.get();
        }

        public String getName() {
            return name.get();
        }

        public String getProcedure(){
            return procedure.get();
        }

        @Override
        public int compareTo(Object o) {
            DataItem dI = (DataItem) o;
            Double val1 = Double.parseDouble(price.get());
            Double val2 = Double.parseDouble(dI.getPrice());
            return (int) Math.round(val1 - val2);
        }
    }

    private List<List<String>> labWorkData;
    private List<String> labTests;
    private List<List<String>> bodyScanData;
    private List<String> bodyScans;
    private List<List<String>> bWData;

    private boolean visible;

    @FXML
    TableColumn<DataItem, String> hospitalCol;

    @FXML
    TableColumn<DataItem, String> priceCol;

    @FXML
    TableColumn<DataItem, String> procedureCol;

    @FXML
    TableColumn<DataItem, String> hospitalCol1;

    @FXML
    TableColumn<DataItem, String> priceCol1;

    @FXML
    TableColumn<DataItem, String> procedureCol1;

    @FXML
    TableColumn<DataItem, String> hospitalCol2;

    @FXML
    TableColumn<DataItem, String> priceCol2;

    @FXML
    TableColumn<DataItem, String> procedureCol2;

    @FXML
    private ListView listView;

    @FXML
    private ListView listView2;

    @FXML
    private TableView priceTable;

    @FXML
    private TableView priceTable2;

    @FXML
    private ProgressBar bar;

    @FXML
    private Label maxLBL;

    @FXML
    private Label minLBL;

    @FXML
    private Label ourLBL;

    @FXML
    private ImageView imgOverlay;

    @FXML
    private ProgressBar bar2;

    @FXML
    private Label maxLBL2;

    @FXML
    private Label minLBL2;

    @FXML
    private Label ourLBL2;

    @FXML
    private ImageView imgOverlay2;

    @FXML
    private TableView chargeMasterTV;

    @FXML
    private TextField searchBar;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblClock;

    @FXML
    private Text userText;

    @FXML
    public void initialize(){
        labWorkData = new ArrayList<>();
        labTests = new ArrayList<>();
        bodyScanData = new ArrayList<>();
        bodyScans = new ArrayList<>();
        bWData = new ArrayList<>();

        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        try {
            parseFile("labWork.csv", labWorkData);
            parseFile("bodyScans.csv", bodyScanData);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        for (List<String> l : labWorkData){
            if (!labTests.contains(l.get(0))){
                labTests.add(l.get(0));
            }
        }
        Collections.sort(labTests);

        for (List<String> l : bodyScanData){
            if (!bodyScans.contains(l.get(0))){
                bodyScans.add(l.get(0));
            }
        }
        Collections.sort(bodyScans);

        hospitalCol.setCellValueFactory(new PropertyValueFactory<DataItem, String>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<DataItem, String>("price"));
        procedureCol.setCellValueFactory(new PropertyValueFactory<DataItem, String>("procedure"));

        hospitalCol1.setCellValueFactory(new PropertyValueFactory<DataItem, String>("name"));
        priceCol1.setCellValueFactory(new PropertyValueFactory<DataItem, String>("price"));
        procedureCol1.setCellValueFactory(new PropertyValueFactory<DataItem, String>("procedure"));

        hospitalCol2.setCellValueFactory(new PropertyValueFactory<DataItem, String>("name"));
        priceCol2.setCellValueFactory(new PropertyValueFactory<DataItem, String>("price"));
        procedureCol2.setCellValueFactory(new PropertyValueFactory<DataItem, String>("procedure"));

        try {
            populateChargeMaster();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        updateListView(labTests);
        updateListView2(bodyScans);
        setUpListeners();
        initVisibility();

    }

    private void initVisibility(){
        bar.setVisible(false);
        maxLBL.setVisible(false);
        minLBL.setVisible(false);
        ourLBL.setVisible(false);
        priceTable.setVisible(false);
        imgOverlay.setVisible(true);
        bar2.setVisible(false);
        maxLBL2.setVisible(false);
        minLBL2.setVisible(false);
        ourLBL2.setVisible(false);
        priceTable2.setVisible(false);
        imgOverlay2.setVisible(true);
    }

    private void setUpListeners(){
        listView.getSelectionModel().selectedItemProperty().addListener((changed, oldVal, newVal) ->
                populateBarGraph(newVal)
        );

        listView2.getSelectionModel().selectedItemProperty().addListener((changed, oldVal, newVal) ->
                populateBarGraph2(newVal)
        );

        priceTable.getSelectionModel().selectedItemProperty().addListener((changed, oldVal, newVal) ->
                updateProgressBar(labWorkData, ((DataItem) newVal).getName(), ((DataItem) newVal).getProcedure())
        );

        priceTable2.getSelectionModel().selectedItemProperty().addListener((changed, oldVal, newVal) ->
                updateProgressBar2(bodyScanData, ((DataItem) newVal).getName(), ((DataItem) newVal).getProcedure())
        );

        List<String> procedures = new ArrayList<>();
        for (List<String> pr : bWData) procedures.add(pr.get(1));
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            TextFields.bindAutoCompletion(searchBar,procedures);
            filterTable();
        });
    }

    private void filterTable(){
        String txt = searchBar.getText();
        chargeMasterTV.getItems().clear();
        ObservableList<DataItem> entries = FXCollections.observableArrayList();

        for (List<String> x : bWData){
            if (txt.equals("") || x.get(1).equals(txt)) {
                entries.add(new DataItem(x.get(2).replace("\"", ""), "Brigham and Women's", x.get(1)));
            }
        }
        chargeMasterTV.getItems().addAll(entries);
    }

    private void populateChargeMaster() throws Exception{
        try (BufferedReader br = new BufferedReader(new FileReader("BrighamWomens.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                bWData.add(Arrays.asList(values));
            }
        }

        ObservableList<DataItem> entries = FXCollections.observableArrayList();

        for (List<String> x : bWData){
            entries.add(new DataItem(x.get(2).replace("\"", ""), "Brigham and Women's", x.get(1)));
        }
        chargeMasterTV.getItems().addAll(entries);
    }

    private void populateBarGraph(Object s){

        priceTable.getItems().clear();

        bar.setVisible(true);
        maxLBL.setVisible(true);
        minLBL.setVisible(true);
        ourLBL.setVisible(true);
        imgOverlay.setVisible(false);

        Label lbl = (Label) s;
        List<List<String>> data = getCorrectData(lbl.getText());
        if (data == null) return;

        priceTable.getItems().addAll(getData(data, lbl.getText()));
        priceTable.setVisible(true);
        updateProgressBar(data, "Brigham and Women's Hospital", lbl.getText());
    }

    private void populateBarGraph2(Object s){

        priceTable2.getItems().clear();

        bar2.setVisible(true);
        maxLBL2.setVisible(true);
        minLBL2.setVisible(true);
        ourLBL2.setVisible(true);
        imgOverlay2.setVisible(false);


        Label lbl = (Label) s;
        List<List<String>> data = getCorrectData2(lbl.getText());
        if (data == null) return;

        priceTable2.getItems().addAll(getData(data, lbl.getText()));
        priceTable2.setVisible(true);
        updateProgressBar2(data, "Brigham and Women's Hospital", lbl.getText());
    }

    private ObservableList<DataItem> getData(List<List<String>> dat, String s) {
        ObservableList<DataItem> entries = FXCollections.observableArrayList();

        for (List<String> x : dat){
            if (x.get(0).equals(s)) {
                entries.add(new DataItem(x.get(2), x.get(3), x.get(0)));
            }
        }

        Collections.sort(entries);

        return entries;
    }

    @FXML
    private void tabChanged(){
        initVisibility();
    }

    @FXML
    private void navigateToHome(){
        Main.setScene("Home");
    }

    @FXML
    private void logout(){
        Main.logOut();
    }

    private void updateProgressBar(List<List<String>> data, String hospital, String procedure) {
        Double min = 1000000.0;
        Double max = -1.0;
        Double bW = 0.0;
        Double count = 0.0;

        for (List<String> s : data){
            if (s.get(0).equals(procedure)) {
                Double val = Double.parseDouble(s.get(2));
                if (val < min) {
                    min = val;
                }
                if (val > max) {
                    max = val;
                    if (val == 1807) System.out.println(s.get(3));
                }
                if (s.get(3).equals(hospital)) {
                    bW = val;
                }
                count += 1;
            }
        }

        minLBL.setText("$" + String.valueOf(min));
        maxLBL.setText("$" + String.valueOf(max));
        ourLBL.setText(hospital + " price: $" + String.valueOf(bW));

        double prog = (100.0 / (max - min) * (bW - min));
        bar.setStyle(getColor(prog / 100.0));
        bar.setProgress(prog/100);

        Label hosLBL = new Label();
        hosLBL.setText(hospital);
        hosLBL.setStyle("-fx-font: 18 system;");

    }

    private void updateProgressBar2(List<List<String>> data, String hospital, String procedure) {
        Double min = 1000000.0;
        Double max = -1.0;
        Double bW = 0.0;
        Double count = 0.0;

        for (List<String> s : data){
            if (s.get(0).equals(procedure)) {
                Double val = Double.parseDouble(s.get(2));
                if (val < min) {
                    min = val;
                }
                if (val > max) {
                    max = val;
                    if (val == 1807) System.out.println(s.get(3));
                }
                if (s.get(3).equals(hospital)) {
                    bW = val;
                }
                count += 1;
            }
        }

        minLBL2.setText("$" + String.valueOf(min));
        maxLBL2.setText("$" + String.valueOf(max));
        ourLBL2.setText(hospital + " price: $" + String.valueOf(bW));

        double prog = (100.0 / (max - min) * (bW - min));
        bar2.setStyle(getColor(prog / 100.0));
        bar2.setProgress(prog/100);

        Label hosLBL = new Label();
        hosLBL.setText(hospital);
        hosLBL.setStyle("-fx-font: 18 system;");

    }


    private List<List<String>> getCorrectData(String s){
        for (List<String> x : labWorkData){
            if (x.get(0).equals(s)) return labWorkData;
        }
        return null;
    }

    private List<List<String>> getCorrectData2(String s){
        for (List<String> x : bodyScanData){
            if (x.get(0).equals(s)) return bodyScanData;
        }
        return null;
    }

    private void updateListView(List<String> items){
        listView.getItems().clear();
        for (String s : items){
            Label temp = new Label(s);
            temp.setStyle("-fx-font: 18 system;");
            listView.getItems().add(temp);
        }
    }

    private void updateListView2(List<String> items){
        listView2.getItems().clear();
        for (String s : items){
            Label temp = new Label(s);
            temp.setStyle("-fx-font: 18 system;");
            listView2.getItems().add(temp);
        }
    }


    private void parseFile(String fileURI, List<List<String>> data) throws Exception{
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileURI))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(Arrays.asList(values));
            }
        }
    }

    public String getColor(Double amount){
        if (amount > 0.75) return "-fx-accent: Red; ";
        else if (amount > 0.50) return "-fx-accent: Orange; ";
        else if (amount > 0.25) return "-fx-accent: Yellow; ";
        else return "-fx-accent: Green; ";
    }
}
