package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.management.Notification;

import org.controlsfx.control.Notifications;
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
    private JFXTextArea txtExamination;
    
    @FXML
    private JFXTextArea txtOthers;

//    @FXML
//    private JFXTextArea txtNose;
//
//    @FXML
//    private JFXTextArea txtEars;
//
//    @FXML
//    private JFXTextArea txtILS;
//
//    @FXML
//    private JFXTextArea txtThroats;
//
//    @FXML
//    private JFXTextArea txtNeck;

    @FXML
    private JFXTextArea txtDiagnosis;

    @FXML
    private VBox vMedcine;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRemove;
    
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
		if(medcines.size() == 0) {
			btnRemove.setVisible(false);
		}
		initValidation();
	}
	
	public void initValidation() {
		RequiredFieldValidator requiredValidator = new RequiredFieldValidator();
		requiredValidator.setMessage("Input required");
		NumberValidator numberValidator = new NumberValidator();
		numberValidator.setMessage("Only Number Allow");

		txtComplaints.getValidators().add(requiredValidator);
		location.getValidators().add(requiredValidator);
		txtCharges.getValidators().add(requiredValidator);
		txtCharges.getValidators().add(numberValidator);

		txtComplaints.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					txtComplaints.validate();
				}
			}
		});
		
		txtCharges.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					txtCharges.validate();
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
		btnRemove.setVisible(true);
		SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		Pane pane = stageManager.loadPane(FxmlView.MEDICINE.getFxmlFile());
		MedcineController controller = loader.getLoader()
				.<MedcineController>getController();
		medcines.add(controller);
		vMedcine.getChildren().add(pane);
	}
	
	public void removeMedcine() {
		medcines.remove(medcines.size()-1);
		vMedcine.getChildren().remove(vMedcine.getChildren().size()-1);
		if(medcines.size() == 0) {
			btnRemove.setVisible(false);
		}
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
	SUBMIT: if(location.validate() && txtComplaints.validate() && txtCharges.validate()) {
			if(consultation != null) {
				List<ConsultationMedicine> consultationMedicines = new ArrayList<>();
				for (MedcineController med : medcines) {
					ConsultationMedicineDTO medDto = med.getValues();
					if(medDto == null) {
						break SUBMIT;
					}
					ConsultationMedicine cM = new ConsultationMedicine();
					cM.setIntakeTimes(medDto.getIntakeTimes());
					cM.setDosage(medDto.getDosage());
					cM.setPeriod(medDto.getPeriod());
					if(medDto.getConsumption() != null && medDto.getConsumption().length() > 3) {
						cM.setMedicine(new Medicine(medDto.getMedicine(), medDto.getConsumption()));
					}else {
						cM.setMedicine(new Medicine(medDto.getMedicine()));
					}
					
					consultationMedicines.add(cM);
				}
				
				String[] examinations = txtExamination.getText().split("|");
				for(int i=0; i<examinations.length;i++) {
					if(examinations[i].startsWith("Nose")) {
						consultation.setNose(examinations[i].replace("Nose:", ""));
					}else if(examinations[i].startsWith("Ear")) {
						consultation.setEars(examinations[i].replace("Ears:", ""));
					}else if(examinations[i].startsWith("ILS")) {
						consultation.setIlS(examinations[i].replace("ILS:", ""));
					}else if(examinations[i].startsWith("Throats")) {
						consultation.setThroat(examinations[i].replace("Throats:", ""));
					}else if(examinations[i].startsWith("Neck")) {
						consultation.setNeck(examinations[i].replace("Neck:", ""));
					}
				}
				consultation.setComplaints(txtComplaints.getText());
				consultation.setOthers(txtOthers.getText());
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
					if(med.getValues() == null) {
						break SUBMIT;
					}
					consultationMedicines.add(med.getValues());
				}
				dto = new ConsultationDTO();
				String[] examinations = txtExamination.getText().split("|");
				for(int i=0; i<examinations.length;i++) {
					if(examinations[i].startsWith("Nose")) {
						dto.setNose(examinations[i].replace("Nose:", ""));
					}else if(examinations[i].startsWith("Ear")) {
						dto.setEars(examinations[i].replace("Ears:", ""));
					}else if(examinations[i].startsWith("ILS")) {
						dto.setIlS(examinations[i].replace("ILS:", ""));
					}else if(examinations[i].startsWith("Throats")) {
						dto.setThroat(examinations[i].replace("Throats:", ""));
					}else if(examinations[i].startsWith("Neck")) {
						dto.setNeck(examinations[i].replace("Neck:", ""));
					}
				}
				dto.setComplaints(txtComplaints.getText());
				dto.setOthers(txtOthers.getText());
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
		}else {
			Notifications.create().title("Error")
			.text("Fill all required")
			.showError();
		}
		
		
		
		
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
		if(patient != null) {
			fName.setText(patient.getFirstName() + " " + patient.getLastName());
			if(patient.getDateOfBirth() != null) {
				age.setText(String.valueOf(LocalDate.now().getYear() - patient.getDateOfBirth().getYear()));
			}
		}else {
			fName.setText("Unknown");
			age.setText("Unknown");
		}
		
	}

	
	
	public Consultation getConsultation() {
		return consultation;
	}

	public void setConsultation(Consultation consultation) {
		this.consultation = consultation;
		if(consultation != null) {
			if(consultation.getPatient() != null) {
				fName.setText(consultation.getPatient().getFirstName() + " " + consultation.getPatient().getLastName());
				if(consultation.getPatient().getDateOfBirth() != null) {
					age.setText(String.valueOf(LocalDate.now().getYear() - consultation.getPatient().getDateOfBirth().getYear()));
				}
			}else {
				fName.setText(patient.getFirstName() + " " + patient.getLastName());
				if(patient.getDateOfBirth() != null) {
					age.setText(String.valueOf(LocalDate.now().getYear() - patient.getDateOfBirth().getYear()));
				}
			}
			StringBuilder sb = new StringBuilder();
			if(consultation.getEars() != null) { sb.append("|Ears:"+consultation.getEars()+"\n");}
			if(consultation.getNose() != null) { sb.append("|Nose:"+consultation.getNose()+"\n");}
			if(consultation.getThroat() != null) { sb.append("|Throats:"+consultation.getThroat()+"\n");}
			if(consultation.getIlS() != null) { sb.append("|ILS:"+consultation.getIlS()+"\n");}
			if(consultation.getNeck() != null) { sb.append("|Neck:"+consultation.getNeck()+"\n");}
			txtExamination.setText(sb.toString());
			if(consultation.getComplaints() != null) { txtComplaints.setText(consultation.getComplaints());}
//			if(consultation.getEars() != null) { txtEars.setText(consultation.getEars());}
//			if(consultation.getNose() != null) { txtNose.setText(consultation.getNose());}
//			if(consultation.getThroat() != null) { txtThroats.setText(consultation.getThroat());}
//			if(consultation.getIlS() != null) { txtILS.setText(consultation.getIlS());}
//			if(consultation.getNeck() != null) { txtNeck.setText(consultation.getNeck());}
			if(consultation.getOthers() != null) { txtDiagnosis.setText(consultation.getOthers());}
			if(consultation.getDiagnosis() != null) { txtDiagnosis.setText(consultation.getDiagnosis());}
			if(consultation.getCharge() != 0) { txtCharges.setText(String.valueOf((int)consultation.getCharge()));}
			if(consultation.getLocation() != null) { location.setValue(consultation.getLocation().getName());}
			if(consultation.getConsultationMedicines() != null) {
				for (ConsultationMedicine medicine : consultation.getConsultationMedicines()) {
					addMedcine(medicine);
				}
			}
		}
		
	}

	public ConsultationDTO getDto() {
		return dto;
	}

	public void setDto(ConsultationDTO dto) {
		this.dto = dto;
	}
	
	
	
	

}
