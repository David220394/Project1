package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.management.config.SpringFXMLLoader;
import com.management.config.StageManager;
import com.management.controller.dto.ConsultationDTO;
import com.management.controller.dto.PatientDTO;
import com.management.entity.Consultation;
import com.management.entity.FxmlView;
import com.management.entity.Patient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@Controller
public class PatientProfileController implements Initializable{

	   	@Lazy
	    @Autowired
	    private StageManager stageManager;
	
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
	    private JFXTreeTableView<Consultation> consultationTable;

	    @FXML
	    private TreeTableColumn<Consultation, String> visitCol;

	    @FXML
	    private TreeTableColumn<Consultation, String> complaintCol;

	    @FXML
	    private TreeTableColumn<Consultation, String> diagnosisCol;

	    @FXML
	    private TreeTableColumn<Consultation, String> locationCol;

	    @FXML
	    private TreeTableColumn<Consultation, String> chargeCol;
	    
	    @FXML
	    private Button btnAddConsultation;
	    
	    @FXML
	    private Button btnUpdatePatient;
	    
	    
	    private PatientDTO patient;
	    
	    private ObservableList<Consultation> list = FXCollections.observableArrayList();
	
	    public PatientProfileController() {
			super();
		}

	    public void setPatient(PatientDTO patient) {
			this.patient = patient;
			loadData();
			fName.setText(patient.getFirstName() + " " + patient.getLastName());
			if(patient.getDateOfBirth() != null) {
				age.setText(String.valueOf(LocalDate.now().getYear() - patient.getDateOfBirth().getYear()));
				phone.setText(String.valueOf(patient.getPhoneNumber()));
				loadData();
			}
	    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initTableCol();
		list = FXCollections.observableArrayList();
		final TreeItem<Consultation> root = new RecursiveTreeItem<Consultation>(list, RecursiveTreeObject::getChildren);
		consultationTable.setRoot(root);
		consultationTable.setShowRoot(false);
	}
	
	public void updatePatient() {
		SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		Parent parent = stageManager.getParentView(FxmlView.REGISTRATION);
		AddPatientController dialogController = loader.getLoader()
				.<AddPatientController>getController();
		dialogController.setPatientDto(patient);
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();
		
		PatientDTO dto = dialogController.getPatientDto();
		if(dto != null) {
			setPatient(dto);
		}
	}
	
	public void initTableCol() {
		visitCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Consultation, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Consultation, String> param) {
						return param.getValue().getValue().visitDate;
					}
				});
		complaintCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Consultation, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Consultation, String> param) {
						return param.getValue().getValue().complaint;
					}
				});
		
		diagnosisCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Consultation, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Consultation, String> param) {
						return param.getValue().getValue().diagnosis;
					}
				});
		
		locationCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Consultation, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Consultation, String> param) {
						return param.getValue().getValue().location;
					}
				});

		chargeCol.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Consultation, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Consultation, String> param) {
						return param.getValue().getValue().charge;
					}
				});
	}
	
	public void loadData() {
		list.clear();
		for (ConsultationDTO consultation : patient.getConsultations()) {
			list.add(new Consultation(consultation.getStartDate().toString(), 
										consultation.getComplaints(), 
										consultation.getDiagnosis(), 
										consultation.getLocation(), 
										String.valueOf(consultation.getCharge())));
		}
	}
	
	public void addConsultation() {
		SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		Parent parent = stageManager.getParentView(FxmlView.CONSULTATION);
		ConsultationController dialogController = loader.getLoader()
				.<ConsultationController>getController();
		dialogController.setPatient(patient);
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();
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
