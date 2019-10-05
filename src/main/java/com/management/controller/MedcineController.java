package com.management.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.management.controller.dto.ConsultationMedicineDTO;
import com.management.entity.ConsultationMedicine;
import com.management.entity.Patient;
import com.management.service.MedicineService;
import com.management.utility.JFXAutocompleteTextTField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

@Controller
public class MedcineController  implements Initializable {

    @FXML
    private JFXAutocompleteTextTField txtMedcine;

    @FXML
    private JFXTextField txtDosage;

    @FXML
    private JFXTextField txtNoPerDay;

    @FXML
    private JFXComboBox<String> txtConsumption;

    @FXML
    private JFXTextField txtDays;

    @FXML
    private JFXComboBox<String> txtPeriod;
    
    @Autowired
    private MedicineService service;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		String[] values = {"","BEFORE_MEAL","AFTER_MEAL"};
		txtConsumption.getItems().addAll(values);
		String[] values1 = {"DAYS","MONTHS"};
		txtPeriod.getItems().addAll(values1);
		List<String> medicines = service.findAllMedicineName();
		SortedSet<String> entries = new TreeSet<>();
		for (String medicine : medicines) {
			entries.add(medicine);
		}
		txtMedcine.getEntries().addAll(entries);
		initValidation();
	}
	
	public void setConsultationMedicine(ConsultationMedicine medicine) {
		String period = "MONTHS";
		String freq = "";
		if(medicine.getPeriod() != null && medicine.getPeriod().contains("DAYS")) {
			freq = medicine.getPeriod().replace(" DAYS", "");
			period = "DAYS";
		}else {
			freq = medicine.getPeriod().replace(" MONTHS", "");
		}
		txtMedcine.setText(medicine.getMedicine().getMedicineName());
		txtDosage.setText(medicine.getDosage());
		txtNoPerDay.setText(String.valueOf(medicine.getIntakeTimes()));
		txtConsumption.setValue(medicine.getMedicine().getConsumption().name());
		txtDays.setText(freq);
		txtPeriod.setValue(period);
	}
	
	public ConsultationMedicineDTO getValues() {
		if(txtMedcine.validate() &
			txtDosage.validate() &
			txtNoPerDay.validate() &
			txtDays.validate() &
			txtPeriod.validate()) {
			String period = txtDays.getText()+" "+txtPeriod.getValue();
			return new ConsultationMedicineDTO(txtMedcine.getText(),
												txtDosage.getText(),
												period, 
												txtConsumption.getValue(), 
												Integer.parseInt(txtNoPerDay.getText()));
		}else {
			return null;
		}
		
	}
	
	public void initValidation() {
		RequiredFieldValidator requiredValidator = new RequiredFieldValidator();
		requiredValidator.setMessage("Input required");

		NumberValidator numberValidator = new NumberValidator();
		numberValidator.setMessage("Only Number Allow");

		txtMedcine.getValidators().add(requiredValidator);
		txtNoPerDay.getValidators().add(requiredValidator);
		txtNoPerDay.getValidators().add(numberValidator);
		txtDosage.getValidators().add(requiredValidator);
		txtDosage.getValidators().add(numberValidator);
		txtDays.getValidators().add(requiredValidator);
		txtDays.getValidators().add(numberValidator);
		txtPeriod.getValidators().add(requiredValidator);
		
		txtMedcine.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					txtMedcine.validate();
				}
			}
		});
		
		txtDosage.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					txtDosage.validate();
				}
			}
		});
	
		txtNoPerDay.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					txtNoPerDay.validate();
				}
			}
		});
		
		txtDays.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					txtDays.validate();
				}
			}
		});
		
		txtPeriod.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					txtPeriod.validate();
				}
			}
		});
		
	}
}
