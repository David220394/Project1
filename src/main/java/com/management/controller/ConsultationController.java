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
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.management.config.SpringFXMLLoader;
import com.management.config.StageManager;
import com.management.controller.dto.ConsultationDTO;
import com.management.controller.dto.ConsultationMedicineDTO;
import com.management.controller.dto.PatientDTO;
import com.management.entity.Consultation;
import com.management.entity.ConsultationMedicine;
import com.management.entity.FxmlView;
import com.management.entity.Location;
import com.management.entity.Medicine;
import com.management.entity.MedicineEnum;
import com.management.service.ConsultationService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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
    
    Consultation consultation;
	
    ConsultationDTO dto;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dto = null;
		startDate = LocalDate.now();
		startTime = LocalTime.now();
		lblDateTime.setText(LocalDate.now().toString() + " " + LocalTime.now().toString());
		
		medcines = new ArrayList<>();
		List<String> locationNames = new ArrayList<>();
		List<Location> locations = consultationService.findAllLocations();
		
		for (Location l : locations) {
			location.getItems().add(l.getName());
		}
		initValidation();
	}
	
	public void initValidation() {
		RequiredFieldValidator requiredValidator = new RequiredFieldValidator();
		requiredValidator.setMessage("Input required");

		txtComplaints.getValidators().add(requiredValidator);
		location.getValidators().add(requiredValidator);

		txtComplaints.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					txtComplaints.validate();
				}
			}
		});
		
		location.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					location.validate();
				}
			}
		});
	}
	
	public void setConsultationFromApp(String location, PatientDTO patient) {
		this.patient = patient;
		this.location.setValue(location);
	}
	
	public void addMedcine() {
		SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		Pane pane = stageManager.loadPane(FxmlView.MEDICINE.getFxmlFile());
		MedcineController controller = loader.getLoader()
				.<MedcineController>getController();
		medcines.add(controller);
		vMedcine.getChildren().add(pane);
	}
	
	public void addMedcine(ConsultationMedicine medicine) {
		SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		Pane pane = stageManager.loadPane(FxmlView.MEDICINE.getFxmlFile());
		MedcineController controller = loader.getLoader()
				.<MedcineController>getController();
		controller.setConsultationMedicine(medicine);
		medcines.add(controller);
		vMedcine.getChildren().add(pane);
	}
	
	public void printPrescription() {
		List<ConsultationMedicineDTO> consultationMedicines = new ArrayList<>();
		for (MedcineController med : medcines) {
			consultationMedicines.add(med.getValues());
		}
		SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		Parent parent = stageManager.getParentView(FxmlView.PRESCRIPTION);
		PrescriptionController dialogController = loader.getLoader()
				.<PrescriptionController>getController();
		dialogController.setPatient(patient);
		dialogController.setcMdtos(consultationMedicines);
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	public void onSubmit(ActionEvent event) {
		if(consultation != null) {
			List<ConsultationMedicine> consultationMedicines = new ArrayList<>();
			for (MedcineController med : medcines) {
				ConsultationMedicineDTO medDto = med.getValues();
				ConsultationMedicine cM = new ConsultationMedicine();
				cM.setIntakeTimes(medDto.getIntakeTimes());
				cM.setNoOfDays(medDto.getNoOfDays());
				cM.setMedicine(new Medicine(medDto.getMedicine(), MedicineEnum.valueOf(medDto.getConsumption())));
				consultationMedicines.add(cM);
			}
			consultation.setComplaints(txtComplaints.getText());
			consultation.setEars(txtEars.getText());
			consultation.setThroat(txtThroats.getText());
			consultation.setNeck(txtNeck.getText());
			consultation.setNose(txtNose.getText());
			consultation.setIlS(txtILS.getText());
			consultation.setDiagnosis(txtDiagnosis.getText());
			consultation.setCharge(Integer.parseInt(txtCharges.getText()));
			consultation.setStartDate(startDate);
			consultation.setStartTime(startTime);
			consultation.setEndDate(LocalDate.now());
			consultation.setEndTime(LocalTime.now());
			consultation.setConsultationMedicines(consultationMedicines);
			consultationService.saveConsultation(consultation);
		}else {
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
			dto.setPatient(patient);
			dto.setConsultationMedicines(consultationMedicines);
			
			consultationService.saveConsultation(dto);
		}
		
		
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

	
	
	public Consultation getConsultation() {
		return consultation;
	}

	public void setConsultation(Consultation consultation) {
		this.consultation = consultation;
		fName.setText(consultation.getPatient().getFirstName() + " " + consultation.getPatient().getLastName());
		if(consultation.getPatient().getDateOfBirth() != null) {
			age.setText(String.valueOf(LocalDate.now().getYear() - consultation.getPatient().getDateOfBirth().getYear()));
		}
		if(consultation.getComplaints() != null) { txtComplaints.setText(consultation.getComplaints());}
		if(consultation.getEars() != null) { txtEars.setText(consultation.getEars());}
		if(consultation.getNose() != null) { txtNose.setText(consultation.getNose());}
		if(consultation.getThroat() != null) { txtThroats.setText(consultation.getThroat());}
		if(consultation.getIlS() != null) { txtILS.setText(consultation.getIlS());}
		if(consultation.getNeck() != null) { txtNeck.setText(consultation.getNeck());}
		if(consultation.getDiagnosis() != null) { txtDiagnosis.setText(consultation.getDiagnosis());}
		if(consultation.getCharge() != 0) { txtCharges.setText(String.valueOf(consultation.getCharge()));}
		if(consultation.getLocation() != null) { location.setValue(consultation.getLocation().getName());}
		if(consultation.getConsultationMedicines() != null) {
			for (ConsultationMedicine medicine : consultation.getConsultationMedicines()) {
				addMedcine(medicine);
			}
		}
	}
	
	

}
