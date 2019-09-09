package com.management.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.jfoenix.controls.JFXTextField;
import com.management.config.StageManager;
import com.management.controller.dto.AppointmentDTO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

@Controller
public class AddConsultationController  implements Initializable {
	
    @Lazy
    @Autowired
    private static StageManager stageManager;
    
    @FXML
    private JFXTextField patient;

    @FXML
    private JFXTextField location;

    @FXML
    private JFXTextField from;

    @FXML
    private JFXTextField to;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSubmit;
    
    private AppointmentDTO dto;
	
	public void onSubmit(ActionEvent event) {
		dto = new AppointmentDTO();
		dto.setName(patient.getText());
		dto.setLocation(location.getText());
		dto.setFrom(from.getText());
		dto.setTo(to.getText());
		
		closeStage(event);
	}
	
	public void onCancel(ActionEvent event) {
		closeStage(event);
	}
	
	private void closeStage(ActionEvent event) {
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
	
	public void setTimes(String startTime, String stopTime) {
		from.setText(startTime);
		to.setText(stopTime);
	}

	public AppointmentDTO getDto() {
		return dto;
	}

	public void setDto(AppointmentDTO dto) {
		this.dto = dto;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	

}
