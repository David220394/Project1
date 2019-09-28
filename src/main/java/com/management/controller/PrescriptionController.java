package com.management.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.management.controller.dto.PatientDTO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

@Controller
public class PrescriptionController implements Initializable  {

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private JFXComboBox<String> txtPrinter;

    @FXML
    private AnchorPane panePreview;

    @FXML
    private Label lblAge;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblName;

    @FXML
    private JFXTreeTableView<ConsultationMedicineDTO> tblMedicine;

    @FXML
    private TreeTableColumn<ConsultationMedicineDTO, String> colMedicine;

    @FXML
    private TreeTableColumn<ConsultationMedicineDTO, String> colNoPerDay;

    @FXML
    private TreeTableColumn<ConsultationMedicineDTO, String> colNoOfDay;

    private List<com.management.controller.dto.ConsultationMedicineDTO> cMdtos;

    private ObservableList<ConsultationMedicineDTO> list = FXCollections.observableArrayList();
    
    private PatientDTO patient;
    
	public List<com.management.controller.dto.ConsultationMedicineDTO> getcMdtos() {
		return cMdtos;
	}

	public void setcMdtos(List<com.management.controller.dto.ConsultationMedicineDTO> cMdtos) {
		this.cMdtos = cMdtos;
		initTableCol();
		loadData();
	}

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
		lblName.setText("Name: "+patient.getFirstName() + " " + patient.getLastName());
		if(patient.getDateOfBirth() != null) {
			lblAge.setText("Age: " + String.valueOf(LocalDate.now().getYear() - patient.getDateOfBirth().getYear()));
		}
		lblDate.setText("Date: " + LocalDate.now().toString());
	}

	public void loadData() {
		list.clear();
		for (com.management.controller.dto.ConsultationMedicineDTO dto : cMdtos) {
			list.add(new ConsultationMedicineDTO(dto.getMedicine(),dto.getIntakeTimes(),dto.getNoOfDays()));
		}
	}
    
	public void initTableCol() {
		colMedicine.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<ConsultationMedicineDTO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<ConsultationMedicineDTO, String> param) {
						return param.getValue().getValue().medicine;
					}
				});
		
		colNoPerDay.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<ConsultationMedicineDTO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<ConsultationMedicineDTO, String> param) {
						return param.getValue().getValue().noPerDay;
					}
				});
		
		colNoOfDay.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<ConsultationMedicineDTO, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<ConsultationMedicineDTO, String> param) {
						return param.getValue().getValue().noOfDay;
					}
				});
	}
	
	class ConsultationMedicineDTO extends RecursiveTreeObject<ConsultationMedicineDTO> {
		StringProperty medicine;
		StringProperty noPerDay;
		StringProperty noOfDay;
		public ConsultationMedicineDTO(String medicine, int noPerDay, int noOfDay) {
			super();
			this.medicine = new SimpleStringProperty(medicine);
			this.noPerDay = new SimpleStringProperty(String.valueOf(noPerDay));
			this.noOfDay = new SimpleStringProperty(String.valueOf(noOfDay));;
		}
		
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		final TreeItem<ConsultationMedicineDTO> root = new RecursiveTreeItem<ConsultationMedicineDTO>(list, RecursiveTreeObject::getChildren);
		tblMedicine.setRoot(root);
		tblMedicine.setShowRoot(false);
		ObservableSet<Printer> printers = Printer.getAllPrinters();
		for(Printer printer : printers) 
		{
			txtPrinter.getItems().add(printer.getName());
		}       
	}
	
	public void print(ActionEvent event)
    {
        // Create the Printer Job
        PrinterJob printerJob = PrinterJob.createPrinterJob();
         
        // Get The Printer Job Settings
        JobSettings jobSettings = printerJob.getJobSettings();
         
        // Get the Page Layout
        PageLayout pageLayout = jobSettings.getPageLayout();
         
        // Get the Page Attributes
        double pgW = pageLayout.getPrintableWidth();
        double pgH = pageLayout.getPrintableHeight();
        double pgLM = pageLayout.getLeftMargin();
        double pgRM = pageLayout.getRightMargin();
        double pgTM = pageLayout.getTopMargin();
        double pgBM = pageLayout.getBottomMargin(); 
         
        // Get The Printer
        Printer printer = printerJob.getPrinter();
        // Create the Page Layout of the Printer
        pageLayout = printer.createPageLayout(Paper.A5,
                PageOrientation.PORTRAIT,Printer.MarginType.HARDWARE_MINIMUM);
        jobSettings.setPageLayout(pageLayout);
        
        if (printerJob != null) 
        {
            // Show the printer job status
        	panePreview.setPrefSize(pageLayout.getPrintableWidth(), pageLayout.getPrintableHeight());
            boolean printed = printerJob.printPage(panePreview);
         
            if (printed) 
            {
                // End the printer job
            	printerJob.endJob();
            	closeStage(event);
            } 
            else
            {
                // Write Error Message
                Notifications.create().darkStyle().title("Error").text("Printing failed.");
            }
        } 
        else
        {
            // Write Error Message
        	Notifications.create().darkStyle().title("Error").text("Could not create a printer job.");
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
