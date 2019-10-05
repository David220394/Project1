package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import org.controlsfx.control.Notifications;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.management.config.StageManager;
import com.management.controller.dto.AppointmentDTO;
import com.management.entity.Location;
import com.management.entity.Patient;
import com.management.service.ConsultationService;
import com.management.service.PatientService;
import com.management.utility.JFXAutocompleteTextTField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

@Controller
public class AddConsultationController  implements Initializable {
	
    @Lazy
    @Autowired
    private static StageManager stageManager;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private ConsultationService consultationService;
    
    @FXML
    private JFXAutocompleteTextTField patient;

    @FXML
    private JFXComboBox<String> location;

    @FXML
    private JFXTextField from;

    @FXML
    private JFXTextField to;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSubmit;
    
    private AppointmentDTO dto;
    
    private Map<String, Patient> patientMaps;
	
	public AddConsultationController() {
		super();
		dto = new AppointmentDTO();
		patientMaps = new HashMap<>();
	}

	public void initValidation() {
		RequiredFieldValidator requiredValidator = new RequiredFieldValidator();
		requiredValidator.setMessage("Input required");

		NumberValidator numberValidator = new NumberValidator();
		numberValidator.setMessage("Only Number Allow");

		patient.getValidators().add(requiredValidator);
		location.getValidators().add(requiredValidator);

		patient.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					patient.validate();
				}
			}
		});
	}
	
	public void onSubmit(ActionEvent event) {
		if(patient.validate() & location.validate()) {
			if(patient.getText().split(" ").length < 2) {
				Notifications.create().title("Error").text("Patient must have a first name and last name").showError();
			}else {
				try {
					LocalTime startTime = LocalTime.parse(from.getText());
					LocalTime endTime = LocalTime.parse(to.getText());
					dto.setName(patient.getText());
					dto.setLocation(location.getValue());
					dto.setFrom(startTime);
					dto.setTo(endTime);
					Patient p = patientMaps.get(patient.getText());
					dto.setPatient(p);
					closeStage(event);
				} catch (DateTimeParseException e) {
					Notifications.create().title("Error").text("Invalid Time format").showError();
				}
			}
			
		}else {
			Notifications.create().title("Error").text("Fill all required").showError();
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
	
	public void setTimes(String startTime, String stopTime, LocalDate startDate, LocalDate endDate) {
		from.setText(startTime);
		to.setText(stopTime);
		dto.setStartDate(startDate);
		dto.setEndDate(endDate);
	}

	public AppointmentDTO getDto() {
		return dto;
	}

	public void setDto(AppointmentDTO dto) {
		this.dto = dto;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dto = new AppointmentDTO();
		List<Patient> patients = patientService.findAll();
		SortedSet<String> entries = new TreeSet<>();
		for (Patient patient : patients) {
			entries.add(patient.getFirstName() + " | " + patient.getLastName() + " | " + patient.getPhoneNumber());
			patientMaps.put(patient.getFirstName() + " | " + patient.getLastName() + " | " + patient.getPhoneNumber(), patient);
		}
		patient.getEntries().addAll(entries);
		
		List<Location> locations = consultationService.findAllLocations();
		
		for (Location l : locations) {
			location.getItems().add(l.getName());
		}
		initValidation();
	}
	
	

}
