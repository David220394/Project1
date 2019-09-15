package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.management.entity.Consultation;
import com.management.entity.Patient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

@Controller
public class PatientProfileController implements Initializable{

	 	@FXML
	    private AnchorPane pnlProfile;

	    @FXML
	    private Label fName;

	    @FXML
	    private Label age;

	    @FXML
	    private Label phone;

	    @FXML
	    private AnchorPane consultationPane;

	    @FXML
	    private TableView<Consultation> consultationTable;

	    @FXML
	    private TableColumn<Consultation, String> visitCol;

	    @FXML
	    private TableColumn<Consultation, String> complaintCol;

	    @FXML
	    private TableColumn<Consultation, String> diagnosisCol;

	    @FXML
	    private TableColumn<Consultation, String> locationCol;
	    
	    @FXML
	    private TableColumn<Consultation, String> chargeCol;
	    
	    @FXML
	    private Button btnAddConsultation;
	    
	    
	    private Patient patient;
	    
	    private ObservableList<Consultation> list = FXCollections.observableArrayList();
	
	public PatientProfileController() {
			super();
		}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		fName.setText(patient.getFirstName() + " " + patient.getLastName());
		if(patient.getDateOfBirth() != null) {
			age.setText(String.valueOf(LocalDate.now().getYear() - patient.getDateOfBirth().getYear()));
			phone.setText(String.valueOf(patient.getPhoneNumber()));
		}
		initTableCol();
		loadData();
	}
	
	
	public void initTableCol() {
		visitCol.setCellValueFactory(new PropertyValueFactory<>("visitDate"));
		complaintCol.setCellValueFactory(new PropertyValueFactory<>("complaint"));
		diagnosisCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
		locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
		chargeCol.setCellValueFactory(new PropertyValueFactory<>("charge"));
	}
	
	public void loadData() {
		list.clear();
		for (com.management.entity.Consultation consultation : patient.getConsultations()) {
			list.add(new Consultation(consultation.getStartDate().toString(), 
										consultation.getComplaints(), 
										consultation.getDiagnosis(), 
										consultation.getLocation().getName(), 
										String.valueOf(consultation.getCharge())));
		}
		consultationTable.setItems(list);
	}
	
	
	class Consultation extends RecursiveTreeObject<Consultation> {
		StringProperty visitDate;
		StringProperty complaint;
		StringProperty diagnosis;
		StringProperty location;
		StringProperty charge;

		public Consultation(String visitDate, String complaint, String diagnosis, String location, String charge) {
			super();
			this.visitDate = new SimpleStringProperty(visitDate);
			this.complaint = new SimpleStringProperty(complaint);
			this.diagnosis = new SimpleStringProperty(diagnosis);
			this.location = new SimpleStringProperty(location);
			this.charge = new SimpleStringProperty(charge);
		}

	}

}
