package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
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
import com.management.controller.dto.ConsultationDTO;
import com.management.controller.dto.ConsultationMedicineDTO;
import com.management.controller.dto.PatientDTO;
import com.management.entity.FxmlView;
import com.management.entity.Location;
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
    
	private LocalDate startDate;
	
	private LocalTime startTime;
	
	private LocalDate endDate;
	
	private LocalTime endTime;
    
    List<MedcineController> medcines;
    
    PatientDTO patient;
	
    ConsultationDTO dto;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dto = null;
		
		if(startDate == null) {
			lblDateTime.setText(LocalDate.now().toString() + " " + LocalTime.now().toString());
		}
		
		medcines = new ArrayList<>();
		List<String> locationNames = new ArrayList<>();
		List<Location> locations = consultationService.findAllLocations();
		
		for (Location l : locations) {
			location.getItems().add(l.getName());
		}
	}
	
	public void setConsultationFromApp(String location, PatientDTO patient) {
		this.patient = patient;
		this.location.setValue(location);
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
		List<ConsultationMedicineDTO> consultationMedicines = new ArrayList<>();
		for (MedcineController med : medcines) {
			consultationMedicines.add(med.getValues());
		}
		dto = new ConsultationDTO();
		dto.setComplaints(txtComplaints.getText());
		dto.setEars(txtEars.getText());
		dto.setThroat(txtThroats.getText());
		dto.setNeck(txtNeck.getText());
		dto.setNose(txtNose.getText());
		dto.setIlS(txtILS.getText());
		dto.setDiagnosis(txtDiagnosis.getText());
		dto.setCharge(Integer.parseInt(txtCharges.getText()));
		dto.setStartDate(startDate);
		dto.setStartTime(startTime);
		dto.setEndDate(LocalDate.now());
		dto.setEndTime(LocalTime.now());
		dto.setLocation(location.getValue());
		dto.setConsultationMedicines(consultationMedicines);
		
		consultationService.saveConsultation(dto);
		closeStage(event);
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

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
		fName.setText(patient.getFirstName() + " " + patient.getLastName());
		if(patient.getDateOfBirth() != null) {
			age.setText(String.valueOf(LocalDate.now().getYear() - patient.getDateOfBirth().getYear()));
		}
	}
	
	

}
