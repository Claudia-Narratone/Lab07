package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Nerc> choiceBox;

    @FXML
    private TextField txtYears;

    @FXML
    private TextField txtHours;

    @FXML
    private Button btnAnalysis;

    @FXML
    private TextArea txtResult;

	private Model model;

    @FXML
    void doAnalysis(ActionEvent event) {
    	txtResult.clear();
    	int maxY=Integer.parseInt(txtYears.getText());
    	int maxH=Integer.parseInt(txtHours.getText());
    	Nerc selectedNerc=choiceBox.getValue();
    	
    	if(selectedNerc==null) {
    		txtResult.setText("Choose a NERC");
    		return;
    	}
    	if(maxY<=0) {
    		txtResult.setText("Insert max quantity of years");
    		return;
    	}
    	if(maxH<=0) {
    		txtResult.setText("Insert max quantity of hours");
    		return;
    	}
    	
    	List<PowerOutages> soluzione=model.doWorstCase(maxY, maxH, selectedNerc);
    	txtResult.appendText("Tot people affected: " + model.sommaAffectedPeople((ArrayList<PowerOutages>) soluzione) + "\n");
    	for(PowerOutages ee: soluzione) {
    		txtResult.appendText(String.format("%d %s %s %d %d", ee.getYear(), ee.getOutageStart(),
					ee.getOutageEnd(), ee.getOutageDuration(), ee.getAffectedPeople()));
    		txtResult.appendText("\n");
    	}
    }

    @FXML
    void initialize() {
        assert choiceBox != null : "fx:id=\"choiceBox\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalysis != null : "fx:id=\"btnAnalysis\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Nerc> nercList = model.getNercList();
    	choiceBox.getItems().addAll(nercList);
    }
}

