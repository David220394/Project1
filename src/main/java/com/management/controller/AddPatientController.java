package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
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
import com.management.entity.Patient;
import com.management.service.PatientService;

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
	private JFXDatePicker dOB;

	@FXML
	private Button btnSubmit;

	private Patient p;

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
	}

	public void onSubmit(ActionEvent event) {
		if (fName.validate() & lName.validate() & dOB.validate() & phone.validate()) {
			Patient patient = new com.management.entity.Patient(fName.getText(), lName.getText(),
					dOB.getValue(), Integer.parseInt(phone.getText()), LocalDate.now());
			p = patientService.savePatient(patient);
			if (p == null) {
				Notifications.create().darkStyle().title("Error")
						.text("An Error occur while adding this patient; Please verify if this patient already exist")
						.showError();
			} else {
				Notifications.create().darkStyle().title("INFO")
						.text(fName.getText() + " " + lName.getText() + " was succesffuly added").showConfirm();
				closeStage(event);
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