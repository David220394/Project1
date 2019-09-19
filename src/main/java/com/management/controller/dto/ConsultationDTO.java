package com.management.controller.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.management.entity.ConsultationMedicine;
import com.management.entity.Location;

public class ConsultationDTO {
	
	private String complaints;
	
	private String ears;
	
	private String nose;
	
	private String throat;
	
	private String neck;
	
	private String ilS;

	private String diagnosis;
	
	private double charge;
	
	private int payment;	
	
	private String title;
	
	private LocalDate startDate;
	
	private LocalTime startTime;
	
	private LocalDate endDate;
	
	private LocalTime endTime;

	private String location;
	
	private List<ConsultationMedicineDTO> consultationMedicines;

	
	
	public ConsultationDTO() {
		super();
	}

	public ConsultationDTO(String complaints, String ears, String nose, String throat, String neck, String ilS,
			String diagnosis, double charge, int payment, String title, LocalDate startDate, LocalTime startTime,
			LocalDate endDate, LocalTime endTime, String location,
			List<ConsultationMedicineDTO> consultationMedicines) {
		super();
		this.complaints = complaints;
		this.ears = ears;
		this.nose = nose;
		this.throat = throat;
		this.neck = neck;
		this.ilS = ilS;
		this.diagnosis = diagnosis;
		this.charge = charge;
		this.payment = payment;
		this.title = title;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.endTime = endTime;
		this.location = location;
		this.consultationMedicines = consultationMedicines;
	}

	public String getComplaints() {
		return complaints;
	}

	public void setComplaints(String complaints) {
		this.complaints = complaints;
	}

	public String getEars() {
		return ears;
	}

	public void setEars(String ears) {
		this.ears = ears;
	}

	public String getNose() {
		return nose;
	}

	public void setNose(String nose) {
		this.nose = nose;
	}

	public String getThroat() {
		return throat;
	}

	public void setThroat(String throat) {
		this.throat = throat;
	}

	public String getNeck() {
		return neck;
	}

	public void setNeck(String neck) {
		this.neck = neck;
	}

	public String getIlS() {
		return ilS;
	}

	public void setIlS(String ilS) {
		this.ilS = ilS;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public double getCharge() {
		return charge;
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

	public int getPayment() {
		return payment;
	}

	public void setPayment(int payment) {
		this.payment = payment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<ConsultationMedicineDTO> getConsultationMedicines() {
		return consultationMedicines;
	}

	public void setConsultationMedicines(List<ConsultationMedicineDTO> consultationMedicines) {
		this.consultationMedicines = consultationMedicines;
	}
	
	
}
