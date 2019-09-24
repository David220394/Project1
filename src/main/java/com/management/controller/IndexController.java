package com.management.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.management.config.StageManager;
import com.management.entity.FxmlView;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

@Controller
public class IndexController implements Initializable {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnPatient;

    @FXML
    private Button btnAddPatient;

    @FXML
    private Button btnCalendar;

    @FXML
    private Button btnExit;

    @FXML
    private Pane pnlPatients;

    @FXML
    private Pane pnlAddPatient;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlCalendar;
    
    @FXML
    private PatientController patientController;
    
    public void handleClicks(ActionEvent event) {
    	if (event.getSource() == btnOverview) {
    		stageManager.switchPane(FxmlView.CALENDAR, pnlCalendar);
        }
    	if (event.getSource() == btnPatient) {
    		stageManager.switchPane(FxmlView.LIST, pnlPatients);
        }
    	if (event.getSource() == btnAddPatient) {
    		stageManager.switchPane(FxmlView.REGISTRATION, pnlAddPatient);
        }
    	if (event.getSource() == btnExit) {
    		Platform.exit();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	stageManager.switchPane(FxmlView.DASHBOARD, pnlOverview);
    }
}
