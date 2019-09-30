package com.management.controller;

import java.io.File;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.AllDayView;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.DateControl.CreateEntryParameter;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import com.calendarfx.view.VirtualGrid;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.management.config.SpringFXMLLoader;
import com.management.config.StageManager;
import com.management.controller.dto.AppointmentDTO;
import com.management.controller.dto.PatientDTO;
import com.management.controller.dto.ReportDto;
import com.management.entity.Consultation;
import com.management.entity.FxmlView;
import com.management.entity.Location;
import com.management.service.ConsultationService;
import com.management.service.PatientService;
import com.management.utility.Converter;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableRow;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@Controller
public class HomeController implements Initializable {
	
    @Lazy
    @Autowired
    private StageManager stageManager;
	
	@Autowired
	private PatientService patientService;

	 @FXML
    private JFXTabPane mainTabs;

    @FXML
    private Tab patientTab;

    @FXML
    private TextField search;

    @FXML
    private Tab calendarTab;

    @FXML
    private Tab administrationTab;

    @FXML
    private Label txtTotalPatient;

    @FXML
    private Label txtMonthGain;

    @FXML
    private Label txtMonthlyConsultations;

    @FXML
    private AreaChart<String, Number> gainChart;

    @FXML
    private JFXButton btnImportPatient;

    @FXML
    private JFXButton btnExportConsultation;

	@FXML
	private JFXTreeTableView<Patient> patientTableView;
	
	@Autowired
	private ConsultationService consultationService;

	@FXML
	private Pane pnlCalendar;

	private CalendarView calendarView;

	private AddConsultationController addConsultationController;
	
	private ObservableList<Patient> patients;

	private ReportDto reportDto;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initTable();
		initCalendar();
		mainTabs.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
			            if("patientTab".equals(t1.idProperty().getValue())) {
			            	populatePatientList();
			            }else if("administrationTab".equals(t1.idProperty().getValue())) {
			            	populateReport();
			            	initChart();
			            }
			        }
			    }
			);
	}
	
	public void initTable() {
		initializeTable();

		patients = FXCollections.observableArrayList();

		populatePatientList();

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
	
	public void populatePatientList() {
		patients.clear();
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
	
	public void addConsultation() {
		calendarView.setEntryDetailsCallback(new Callback<DateControl.EntryDetailsParameter, Boolean>(){

			@Override
			public Boolean call(EntryDetailsParameter param) {
				InputEvent evt = param.getInputEvent();
		        if (evt instanceof MouseEvent) {
		                MouseEvent mouseEvent = (MouseEvent) evt;
		                if (mouseEvent.getClickCount() == 2) {
		                	Entry<?> entry = param.getEntry();
		                	Consultation c = consultationService.findById(Long.parseLong(entry.getId()));
		                	com.management.entity.Patient patient = c.getPatient();
		                	SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		            		Parent parent = stageManager.getParentView(FxmlView.CONSULTATION);
		            		ConsultationController dialogController = loader.getLoader()
		            				.<ConsultationController>getController();
		            		dialogController.setPatient(Converter.patientToDto(patient));
		            		dialogController.setConsultation(c);
		            		Scene scene = new Scene(parent);
		            		Stage stage = new Stage();
		            		stage.initModality(Modality.APPLICATION_MODAL);
		            		stage.setScene(scene);
		            		stage.showAndWait();
		        }
		    } 
				return true;
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

		firstName.getStyleClass().add("col-field");
		lastName.getStyleClass().add("col-field");
		age.getStyleClass().add("col-field");
		lastVisitDate.getStyleClass().add("col-field");
		phoneNumber.getStyleClass().add("col-field");
		firstName.prefWidthProperty().bind(patientTableView.widthProperty().divide(3)); // w * 1/4
		lastName.prefWidthProperty().bind(patientTableView.widthProperty().divide(3)); // w * 1/2
		age.prefWidthProperty().bind(patientTableView.widthProperty().divide(10)); // w * 1/4
		lastVisitDate.prefWidthProperty().bind(patientTableView.widthProperty().divide(5)); // w * 1/4
		phoneNumber.prefWidthProperty().bind(patientTableView.widthProperty().divide(5)); // w * 1/4
		patientTableView.getColumns().setAll(firstName, lastName, age, lastVisitDate, phoneNumber);
	}

	public void initCalendar() {
		calendarView = new CalendarView();

		CalendarSource myCalendarSource = initCalendars();
		calendarView.getCalendarSources().addAll(myCalendarSource);
		calendarView.setRequestedTime(LocalTime.now());
		calendarView.setShowPrintButton(false);
		populateEntry(myCalendarSource.getCalendars());
		
		Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
			@Override
			public void run() {
				while (true) {
					Platform.runLater(() -> {
						calendarView.setToday(LocalDate.now());
						calendarView.setTime(LocalTime.now());
					});

					try {
						// update every 10 seconds
						sleep(100000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			};
		};

		addEntry();
		addCanlendar();
		addConsultation();
		updateTimeThread.setPriority(Thread.MIN_PRIORITY);
		updateTimeThread.setDaemon(true);
		updateTimeThread.start();
		pnlCalendar.getChildren().setAll(calendarView);

	}

	public void addEntry() {
		calendarView.setEntryFactory(new Callback<DateControl.CreateEntryParameter, Entry<?>>() {

			@Override
			public Entry<?> call(CreateEntryParameter param) {
				Entry entry = null;
				DateControl control = param.getDateControl();
				VirtualGrid grid = control.getVirtualGrid();
				ZonedDateTime time = param.getZonedDateTime();
				DayOfWeek firstDayOfWeek = calendarView.getFirstDayOfWeek();

				ZonedDateTime lowerTime = grid.adjustTime(time, false, firstDayOfWeek);
				ZonedDateTime upperTime = grid.adjustTime(time, true, firstDayOfWeek);

				if (Duration.between(time, lowerTime).abs().minus(Duration.between(time, upperTime).abs())
						.isNegative()) {
					time = lowerTime;
				} else {
					time = upperTime;
				}
				SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
				Parent parent = stageManager.getParentView(FxmlView.NEW_APPOINTMENT);
				AddConsultationController dialogController = loader.getLoader()
						.<AddConsultationController>getController();
				dialogController.setTimes(time.toLocalTime().toString(), time.toLocalTime().plusHours(1).toString(), time.toLocalDate(),time.toLocalDate());
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setScene(scene);
				stage.showAndWait();
				AppointmentDTO appointmentDTO = dialogController.getDto();
				if (appointmentDTO != null && appointmentDTO.getName() != null && appointmentDTO.getName().length() > 0) {
					entry = new Entry<>();
					entry.setTitle(appointmentDTO.getName());
					entry.changeStartDate(time.toLocalDate());
					entry.changeStartTime(LocalTime.parse(appointmentDTO.getFrom()));
					entry.changeEndDate(entry.getStartDate());
					entry.changeEndTime(LocalTime.parse(appointmentDTO.getTo()));
					consultationService.saveConsultationFromDto(appointmentDTO);
					if (control instanceof AllDayView) {
						entry.setFullDay(true);
					}
				}
				
				return entry;
			}
		});

	}

	public void populateEntry(List<Calendar> calendars) {
		for (Calendar calendar : calendars) {
			Location location = consultationService.findLocationByName(calendar.getName());
			List<Consultation> consultations = consultationService.findByLocation(location);
			List<Entry<?>> entries = new ArrayList<>();
			for (Consultation consultation : consultations) {
				
				Entry<?> entry = new Entry();
				Interval interval = new Interval(consultation.getStartDate(),
						consultation.getStartTime(),
						consultation.getEndDate(),
						consultation.getEndTime());
				entry.setInterval(interval);
				entry.setId(String.valueOf(consultation.getConsultationId()));
				entry.setTitle(consultation.getTitle());
				/*
				 * entry.changeStartDate(consultation.getStartDate());
				 * entry.changeStartTime(consultation.getStartTime());
				 * entry.changeEndDate(consultation.getEndDate());
				 * entry.changeEndTime(consultation.getEndTime());
				 */
				calendar.addEntry(entry);
			}
		}
	}

	public void populateReport() {
		reportDto = consultationService.findConsultationFor6Months();
		txtTotalPatient.setText(String.valueOf(patients.size()));
		txtMonthGain.setText(String.valueOf(reportDto.getCurrentMonthCharge()));
		txtMonthlyConsultations.setText(String.valueOf(reportDto.getConsultationCount()));
	}
	
	public void initChart() {
		int maxVal = 0;
		if(reportDto.getMonth1Charge() > maxVal) { maxVal = reportDto.getMonth1Charge();}
		if(reportDto.getMonth1Charge() > maxVal) { maxVal = reportDto.getMonth2Charge();}
		if(reportDto.getMonth1Charge() > maxVal) { maxVal = reportDto.getMonth3Charge();}
		if(reportDto.getMonth1Charge() > maxVal) { maxVal = reportDto.getMonth4Charge();}
		if(reportDto.getMonth1Charge() > maxVal) { maxVal = reportDto.getMonth5Charge();}
		
		int minVal = (maxVal / 5) + (maxVal % 5);
		CategoryAxis xAxis = new CategoryAxis();  
        
		//Defining the y Axis 
		NumberAxis yAxis = new NumberAxis(); 
		yAxis.setLabel("Total Charges (Rs.)");
		gainChart = new AreaChart<>(xAxis, yAxis);
		
		XYChart.Series series1 = new XYChart.Series();  
		series1.getData().add(new XYChart.Data(LocalDate.now().minusMonths(-5).getMonth(), reportDto.getMonth1Charge())); 
		series1.getData().add(new XYChart.Data(LocalDate.now().minusMonths(-4).getMonth(), reportDto.getMonth2Charge())); 
		series1.getData().add(new XYChart.Data(LocalDate.now().minusMonths(-3).getMonth(),  reportDto.getMonth3Charge())); 
		series1.getData().add(new XYChart.Data(LocalDate.now().minusMonths(-2).getMonth(), reportDto.getMonth4Charge())); 
		series1.getData().add(new XYChart.Data(LocalDate.now().minusMonths(-1).getMonth(), reportDto.getMonth5Charge())); 
		
		gainChart.getData().add(series1);
		
	}
	
	
	public void downloadConsultations() {
		FileChooser chooser = new FileChooser();
		  
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx");
        chooser.getExtensionFilters().add(extFilter);
        File saveFile = chooser.showSaveDialog(mainTabs.getScene().getWindow());
        if(saveFile != null){
            consultationService.exportConsultation(saveFile);
        }
	}
	
	public CalendarSource initCalendars() {
		CalendarSource myCalendarSource = new CalendarSource();
		int count = 1;
		List<Location> locations = consultationService.findAllLocations();
		if(locations == null || locations.size() < 2) {
			locations = new ArrayList<>();
			locations.add(consultationService.saveLocation(new Location("Home")));
			locations.add(consultationService.saveLocation(new Location("Clinic")));
		}
		for (Location location : locations) {
			Calendar c = new Calendar(location.getName());
			c.setStyle(Style.getStyle(count));
			count++;
			myCalendarSource.getCalendars().add(c);
		}
		
		
		return myCalendarSource;
	}

	public void addCanlendar() {
			calendarView.setCalendarSourceFactory(param -> {
	        CalendarSource source = calendarView.getCalendarSources().get(0);
	        TextInputDialog dialog = new TextInputDialog();
	        dialog.setTitle("Calendar");
	        dialog.setHeaderText("Enter New Calendar Name");
	        Optional<String> result = dialog.showAndWait();
	        
	        result.ifPresent(name -> {
	        	if(consultationService.findLocationByName(name) == null) {
	        		consultationService.saveLocation(new Location(name));
	        		Calendar calendar = new Calendar(name);
	        		calendar.setStyle(Style.STYLE7);
			        source.getCalendars().add(calendar);
	        	}
	        });
	        
	        return source;
	 });
		
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
