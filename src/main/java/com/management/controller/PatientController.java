package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javax.management.Notification;

import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.management.config.SpringFXMLLoader;
import com.management.config.StageManager;
import com.management.controller.dto.PatientDTO;
import com.management.entity.FxmlView;
import com.management.entity.Patient;
import com.management.service.PatientService;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXConsole;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TreeTableRow;

@Controller
public class PatientController implements Initializable {

    @Lazy
    @Autowired
    private StageManager stageManager;
	
	@Autowired
	private PatientService patientService;

	@FXML
	private TextField search;

	@FXML
	private JFXTreeTableView<Patient> patientTableView;
	
	private ObservableList<Patient> patients;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initializeTable();

		patients = FXCollections.observableArrayList();

		List<com.management.entity.Patient> patientList = patientService.findAll();
		for (com.management.entity.Patient patient : patientList) {
			Patient p = new Patient();
			p.setFirstName(new SimpleStringProperty(patient.getFirstName()));
			p.setLastName(new SimpleStringProperty(patient.getLastName()));
			if(patient.getDateOfBirth() != null) {
				p.setAge(new SimpleStringProperty(String.valueOf(LocalDate.now().getYear() - patient.getDateOfBirth().getYear())));
				p.setLastVisitDate(new SimpleStringProperty(patient.getLastVisitDate().toString()));
			}
			p.setPhoneNumber(new SimpleStringProperty(String.valueOf(patient.getPhoneNumber())));
			patients.add(p);
		}

		final TreeItem<Patient> root = new RecursiveTreeItem<Patient>(patients, RecursiveTreeObject::getChildren);

		patientTableView.setRowFactory( tv -> {
		    TreeTableRow<Patient> row = new TreeTableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	Patient rowData = row.getItem();
		        	openProfilePage(rowData);
		        }
		    });
		    return row ;
		});
		
		patientTableView.setRoot(root);
		patientTableView.setShowRoot(false);

		search.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				patientTableView.setPredicate(new Predicate<TreeItem<Patient>>() {
					@Override
					public boolean test(TreeItem<Patient> patient) {
						boolean flag = patient.getValue().firstName.getValue().toLowerCase()
								.contains(newValue.toLowerCase())
								|| patient.getValue().lastName.getValue().toLowerCase().contains(newValue.toLowerCase())
								|| patient.getValue().phoneNumber.getValue().toLowerCase()
										.contains(newValue.toLowerCase());
						return flag;
					}
				});
			}
		});

	}
	
	public void openProfilePage(Patient patient) {
		SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		Parent parent = stageManager.getParentView(FxmlView.PROFILE);
		PatientProfileController dialogController = loader.getLoader()
				.<PatientProfileController>getController();
		PatientDTO p = patientService.findByFirstNameAndLastNameDto(patient.firstName.get(), patient.lastName.get());
		dialogController.setPatient(p);
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void addPatient() {
		SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		Parent parent = stageManager.getParentView(FxmlView.REGISTRATION);
		AddPatientController dialogController = loader.getLoader()
				.<AddPatientController>getController();
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();
		
		com.management.entity.Patient patient = dialogController.getP();
		if(patient != null) {
			Patient p = new Patient();
			p.setFirstName(new SimpleStringProperty(patient.getFirstName()));
			p.setLastName(new SimpleStringProperty(patient.getLastName()));
			if(patient.getDateOfBirth() != null) {
				p.setAge(new SimpleStringProperty(String.valueOf(LocalDate.now().getYear() - patient.getDateOfBirth().getYear())));
				p.setLastVisitDate(new SimpleStringProperty(patient.getLastVisitDate().toString()));
			}
			p.setPhoneNumber(new SimpleStringProperty(String.valueOf(patient.getPhoneNumber())));
			patients.add(p);
		}
	}
	
	private void initializeTable() {
		JFXTreeTableColumn<Patient, String> firstName = new JFXTreeTableColumn<>("First Name");
		JFXTreeTableColumn<Patient, String> lastName = new JFXTreeTableColumn<>("Last Name");
		JFXTreeTableColumn<Patient, String> age = new JFXTreeTableColumn<>("Age");
		JFXTreeTableColumn<Patient, String> lastVisitDate = new JFXTreeTableColumn<>("Last Visited Date");
		JFXTreeTableColumn<Patient, String> phoneNumber = new JFXTreeTableColumn<>("Phone");

		firstName.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Patient, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Patient, String> param) {
						return param.getValue().getValue().firstName;
					}
				});

		lastName.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Patient, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Patient, String> param) {
						return param.getValue().getValue().lastName;
					}
				});

		age.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Patient, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Patient, String> param) {
						return param.getValue().getValue().age;
					}
				});

		lastVisitDate.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Patient, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Patient, String> param) {
						return param.getValue().getValue().lastVisitDate;
					}
				});

		phoneNumber.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Patient, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Patient, String> param) {
						return param.getValue().getValue().phoneNumber;
					}
				});

		patientTableView.getColumns().setAll(firstName, lastName, age, lastVisitDate, phoneNumber);
	}

	

	private class Patient extends RecursiveTreeObject<Patient> {
		StringProperty firstName;
		StringProperty lastName;
		StringProperty age;
		StringProperty lastVisitDate;
		StringProperty phoneNumber;

		public Patient(String firstName, String lastName, LocalDate dOB, LocalDate lastVisitDate, int phoneNumber) {
			super();
			this.firstName = new SimpleStringProperty(firstName);
			this.lastName = new SimpleStringProperty(lastName);
			this.age = new SimpleStringProperty(String.valueOf(LocalDate.now().getYear() - dOB.getYear()));
			this.lastVisitDate = new SimpleStringProperty(lastVisitDate.toString());
			this.phoneNumber = new SimpleStringProperty(String.valueOf(phoneNumber));
		}

		public Patient() {
			super();
		}

		public StringProperty getFirstName() {
			return firstName;
		}

		public void setFirstName(StringProperty firstName) {
			this.firstName = firstName;
		}

		public StringProperty getLastName() {
			return lastName;
		}

		public void setLastName(StringProperty lastName) {
			this.lastName = lastName;
		}

		public StringProperty getAge() {
			return age;
		}

		public void setAge(StringProperty age) {
			this.age = age;
		}

		public StringProperty getLastVisitDate() {
			return lastVisitDate;
		}

		public void setLastVisitDate(StringProperty lastVisitDate) {
			this.lastVisitDate = lastVisitDate;
		}

		public StringProperty getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(StringProperty phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		
	}

}
