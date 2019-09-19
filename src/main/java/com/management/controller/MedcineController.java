package com.management.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import com.management.controller.dto.ConsultationMedicineDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

public class MedcineController  implements Initializable {

    @FXML
    private JFXTextField txtMedcine;

    @FXML
    private JFXTextField txtNoPerDay;

    @FXML
    private ChoiceBox<?> txtConsumption;

    @FXML
    private JFXTextField txtDays;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		String[] values = {"Before Meal","During Meal","After Meal"};
		txtConsumption = new ChoiceBox(FXCollections.observableArrayList(values));
		
	}
	
	public ConsultationMedicineDTO getValues() {
		return new ConsultationMedicineDTO(txtMedcine.getText(),
											Integer.parseInt(txtDays.getText()), 
											txtConsumption.getValue().toString(), 
											Integer.parseInt(txtNoPerDay.getText()));
	}
}
