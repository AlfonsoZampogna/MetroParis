package it.polito.tdp.metroparis;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MetroController {

	private Model model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Fermata> boxArrivo;

    @FXML
    private ComboBox<Fermata> boxPrtenza;

    @FXML
    private Button btnCerca;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCercaPercorso(ActionEvent event) {
        txtResult.clear();
    	Fermata partenza = boxPrtenza.getValue();
        Fermata arrivo = boxArrivo.getValue();
    	if(partenza== null && arrivo==null && !partenza.equals(arrivo)) {
    		txtResult.appendText("Seleziona due fermate diverse di partenza ed arrivo!");
    		return;
    	}
    	List<Fermata> fermate = new ArrayList<Fermata>();
    	fermate = this.model.calcolaPercorso(partenza,arrivo);
    	txtResult.setText(fermate.toString());
    }

    @FXML
    void initialize() {
        assert boxArrivo != null : "fx:id=\"boxArrivo\" was not injected: check your FXML file 'Metro.fxml'.";
        assert boxPrtenza != null : "fx:id=\"boxPrtenza\" was not injected: check your FXML file 'Metro.fxml'.";
        assert btnCerca != null : "fx:id=\"btnCerca\" was not injected: check your FXML file 'Metro.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Metro.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxArrivo.getItems().addAll(this.model.getFermate());
    	this.boxPrtenza.getItems().addAll(this.model.getFermate());
    }

}
