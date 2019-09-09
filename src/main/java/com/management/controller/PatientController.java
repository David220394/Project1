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
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;

@Controller
public class PatientController implements Initializable {

	@Autowired
	private PatientService patientService;

	@FXML
	private TextField search;

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

	@FXML
	private JFXTreeTableView<Patient> patientTableView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		inputFieldValidation();

		initializeTable();

		ObservableList<Patient> patients = FXCollections.observableArrayList();

		List<com.management.entity.Patient> patientList = patientService.findAll();
		for (com.management.entity.Patient patient : patientList) {
			patients.add(new Patient(patient.getFirstName(), patient.getLastName(), patient.getDateOfBirth(),
					LocalDate.now(), patient.getPhoneNumber()));
		}

		final TreeItem<Patient> root = new RecursiveTreeItem<Patient>(patients, RecursiveTreeObject::getChildren);

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

	public void onSubmit(ActionEvent event) {
		if (fName.validate() & lName.validate() & dOB.validate() & phone.validate()) {
			com.management.entity.Patient p = new com.management.entity.Patient(fName.getText(), lName.getText(),
					dOB.getValue(), Integer.parseInt(phone.getText()), LocalDate.now());
			p = patientService.savePatient(p);
			Alert a = new Alert(AlertType.NONE);
			if (p == null) {
				Notifications.create().darkStyle().title("Error")
						.text("An Error occur while adding this patient; Please verify if this patient already exist")
						.showError();
				/*
				 * a.setAlertType(AlertType.ERROR); a.
				 * setContentText("An Error occur while adding this patient; Please verify if this patient already exist"
				 * ); a.show();
				 */
			} else {
				Notifications.create().darkStyle().title("INFO")
						.text(fName.getText() + " " + lName.getText() + " was succesffuly added").showConfirm();
				/*
				 * a.setAlertType(AlertType.CONFIRMATION); a.setContentText(fName.getText() +
				 * " " + lName.getText() +" was succesffuly added"); a.show();
				 */
			}
		} else {
			Notifications.create().darkStyle().title("Error").text("Fill all required").showError();
		}

	}

	class Patient extends RecursiveTreeObject<Patient> {
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

	}

}
