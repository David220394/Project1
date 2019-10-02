package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.management.config.StageManager;
import com.management.controller.dto.PatientDTO;
import com.management.entity.Patient;
import com.management.service.PatientService;
import com.management.utility.Converter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

@Controller
public class AddPatientController implements Initializable {
	
	@Autowired
	private PatientService patientService;

	@FXML
	private JFXTextField fName;

	@FXML
	private JFXTextField lName;

	@FXML
	private JFXTextField phone;

	@FXML
	private JFXTextField dOB;

	@FXML
	private Button btnSubmit;

	private Patient p;
	
	PatientDTO patientDto;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inputFieldValidation();
		
	}
	
	private void inputFieldValidation() {
		RequiredFieldValidator requiredValidator = new RequiredFieldValidator();
		requiredValidator.setMessage("Input required");

		NumberValidator numberValidator = new NumberValidator();
		numberValidator.setMessage("Only Number Allow");

		lName.getValidators().add(requiredValidator);
		fName.getValidators().add(requiredValidator);
		dOB.getValidators().add(requiredValidator);
		phone.getValidators().add(requiredValidator);
		phone.getValidators().add(numberValidator);

		fName.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					fName.validate();
				}
			}
		});

		lName.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					lName.validate();
				}
			}
		});

		dOB.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					dOB.validate();
				}
			}
		});

		phone.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					phone.validate();
				}
			}
		});
	}
	
	public Patient getP() {
		return p;
	}

	public void setP(Patient p) {
		this.p = p;
		if(p.getLastName() != null) {
			lName.setText(p.getLastName());
		}
		if(p.getDateOfBirth() != null) {
			dOB.setText(p.getDateOfBirth().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
		}
		if(p.getPhoneNumber() != 0) {
			phone.setText(String.valueOf(p.getPhoneNumber()));
		}
		fName.setText(p.getFirstName());

	}
	
	

	public PatientDTO getPatientDto() {
		return patientDto;
	}

	public void setPatientDto(PatientDTO patientDto) {
		this.patientDto = patientDto;
		if(patientDto.getLastName() != null) {
			lName.setText(patientDto.getLastName());
		}
		if(patientDto.getDateOfBirth() != null) {
			dOB.setText(patientDto.getDateOfBirth().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
		}
		if(patientDto.getPhoneNumber() != 0) {
			phone.setText(String.valueOf(patientDto.getPhoneNumber()));
		}
		fName.setText(patientDto.getFirstName());
	}

	public void onSubmit(ActionEvent event) {
		if (fName.validate() & lName.validate() & dOB.validate() & phone.validate()) {
			try {
				Patient patient = new com.management.entity.Patient(fName.getText(), lName.getText(),
						LocalDate.parse(dOB.getText(),DateTimeFormatter.ofPattern("ddMMyyyy")), Integer.parseInt(phone.getText()), LocalDate.now());
				if(patientDto != null) {
					Patient p1 = patientService.findByFirstNameAndLastName(patientDto.getFirstName(), patientDto.getLastName());
					if(p1 != null) {
						p1.setFirstName(fName.getText());
						p1.setLastName(lName.getText());
						p1.setDateOfBirth(LocalDate.parse(dOB.getText(),DateTimeFormatter.ofPattern("ddMMyyyy")));
						p1.setPhoneNumber(Integer.parseInt(phone.getText()));
						p = patientService.savePatient(p1);
					}
				}else {
					p = patientService.savePatient(patient);
				}
				
				if (p == null) {
					Notifications.create().darkStyle().title("Error")
							.text("An Error occur while adding this patient; Please verify if this patient already exist")
							.showError();
				} else {
					Notifications.create().darkStyle().title("INFO")
							.text(fName.getText() + " " + lName.getText() + " was succesffuly added").showConfirm();
					patientDto = Converter.patientToDto(p);
					closeStage(event);
				}
			} catch (DateTimeParseException  e) {
				Notifications.create().darkStyle().title("Error").text("Incorrect Date Format").showError();
			}
		} else {
			Notifications.create().darkStyle().title("Error").text("Fill all required").showError();
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
}
