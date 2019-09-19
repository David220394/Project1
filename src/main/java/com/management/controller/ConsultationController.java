package com.management.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.management.config.SpringFXMLLoader;
import com.management.config.StageManager;
import com.management.entity.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

@Controller
public class ConsultationController  implements Initializable  {

    @Lazy
    @Autowired
    private StageManager stageManager;
	
	@FXML
    private AnchorPane consultationPane;

    @FXML
    private Label lblDateTime;

    @FXML
    private JFXTextField txtComplaints;

    @FXML
    private JFXTextArea txtNose;

    @FXML
    private JFXTextArea txtEars;

    @FXML
    private JFXTextArea txtThroats;

    @FXML
    private JFXTextArea txtNeck;

    @FXML
    private JFXTextArea txtILS;

    @FXML
    private JFXTextArea txtDiagnosis;

    @FXML
    private VBox vMedcine;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSubmit;
    
    List<MedcineController> medcines;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		medcines = new ArrayList<>();
	}
	
	public void addMedcine(ActionEvent event) {
		SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
		Pane pane = stageManager.loadPane(FxmlView.MEDCINE.getFxmlFile());
		MedcineController controller = loader.getLoader()
				.<MedcineController>getController();
		medcines.add(controller);
		vMedcine.getChildren().add(pane);
	}

}
