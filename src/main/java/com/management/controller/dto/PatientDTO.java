package com.management.controller.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.management.entity.Consultation;

public class PatientDTO {

private String firstName;
	
	private String lastName;
	
	private LocalDate dateOfBirth;
	
	private int phoneNumber;
	
	private LocalDate lastVisitDate;
	
	private List<ConsultationDTO> consultations;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDate getLastVisitDate() {
		return lastVisitDate;
	}

	public void setLastVisitDate(LocalDate lastVisitDate) {
		this.lastVisitDate = lastVisitDate;
	}

	public List<ConsultationDTO> getConsultations() {
		return consultations;
	}

	public void setConsultations(List<ConsultationDTO> consultations) {
		this.consultations = consultations;
	}
	
}
