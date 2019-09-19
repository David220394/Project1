package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.management.config.SpringFXMLLoader;
import com.management.config.StageManager;
import com.management.entity.FxmlView;
import com.management.entity.Location;
import com.management.entity.Patient;
import com.management.service.ConsultationService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@Controller
public class ConsultationController  implements Initializable  {

    @Lazy
    @Autowired
    private StageManager stageManager;
    
    @Autowired
    private ConsultationService consultationService;
	
    @FXML
    private AnchorPane consultationPane;

    @FXML
    private Label lblDateTime;

    @FXML
    private Label fName;

    @FXML
    private Label age;

    @FXML
    private JFXComboBox<String> location;

    @FXML
    private JFXTextArea txtComplaints;

    @FXML
    private JFXTextArea txtNose;

    @FXML
    private JFXTextArea txtEars;

    @FXML
    private JFXTextArea txtILS;

    @FXML
    private JFXTextArea txtThroats;

    @FXML
    private JFXTextArea txtNeck;

    @FXML
    private JFXTextArea txtDiagnosis;

    @FXML
    private VBox vMedcine;

    @FXML
    private Button btnAdd;

    @FXML
    private JFXTextField txtCharges;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSubmit;
    
    List<MedcineController> medcines;
    
    Patient patient;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		medcines = new ArrayList<>();
		List<String> locationNames = new ArrayList<>();
		List<Location> locations = consultationService.findAllLocations();
		
		for (Location l : locations) {
			location.getItems().add(l.getName());
		}
	}
	
	public void addMedcine(ActionEvent event) {
		SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		Pane pane = stageManager.loadPane(FxmlView.MEDICINE.getFxmlFile());
		MedcineController controller = loader.getLoader()
				.<MedcineController>getController();
		medcines.add(controller);
		vMedcine.getChildren().add(pane);
	}
	
	public void onSubmit(ActionEvent event) {
		
	}
	
	public void onCancel(ActionEvent event) {
		closeStage(event);
	}
	
	private void closeStage(ActionEvent event) {
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

	public List<MedcineController> getMedcines() {
		return medcines;
	}

	public void setMedcines(List<MedcineController> medcines) {
		this.medcines = medcines;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(com.management.entity.Patient patient) {
		this.patient = patient;
		fName.setText(patient.getFirstName() + " " + patient.getLastName());
		if(patient.getDateOfBirth() != null) {
			age.setText(String.valueOf(LocalDate.now().getYear() - patient.getDateOfBirth().getYear()));
		}
	}
	
	

}
