package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXTextField;
import com.management.config.StageManager;
import com.management.controller.dto.AppointmentDTO;
import com.management.entity.Patient;
import com.management.service.PatientService;
import com.management.utility.JFXAutocompleteTextTField;

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
    
    @FXML
    private JFXAutocompleteTextTField patient;

    @FXML
    private JFXTextField location;

    @FXML
    private JFXTextField from;

    @FXML
    private JFXTextField to;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSubmit;
    
    private AppointmentDTO dto;
    
    
	
	public AddConsultationController() {
		super();
		dto = new AppointmentDTO();
	}

	public void onSubmit(ActionEvent event) {
		dto.setName(patient.getText());
		dto.setLocation(location.getText());
		dto.setFrom(from.getText());
		dto.setTo(to.getText());
		
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
		List<Patient> patients = patientService.findAll();
		SortedSet<String> entries = new TreeSet<>();
		for (Patient patient : patients) {
			entries.add(patient.getFirstName() + " | " + patient.getLastName() + " | " + patient.getPhoneNumber());
		}
		patient.getEntries().addAll(entries);
	}
	
	

}
