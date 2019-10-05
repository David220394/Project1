package com.management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.management.controller.dto.ConsultationDTO;
import com.management.controller.dto.PatientDTO;
import com.management.entity.Consultation;
import com.management.entity.Patient;
import com.management.repository.PatientRepository;
import com.management.utility.Converter;

@Service
public class PatientService {

	private PatientRepository patientRepository;

	public PatientService(PatientRepository patientRepository) {
		super();
		this.patientRepository = patientRepository;
	}
	
	public Patient savePatient(Patient patient) {
		patient = patientRepository.save(patient);
		return patient;
	}
	
	public PatientDTO findByFirstNameAndLastNameDto(String firstName, String lastName) {
		Patient p = patientRepository.findByFirstNameAndLastName(firstName, lastName);
		PatientDTO dto = null;
		if(p != null) {
			dto = Converter.patientToDto(p);
		}
		return dto;
	}
	
	public Patient findByFirstNameAndLastName(String firstName, String lastName) {
		Patient p = patientRepository.findByFirstNameAndLastName(firstName, lastName);
		return p;
	}
	
	public List<Patient> findAll(){
		return patientRepository.findAll();
	}
	
	public Patient findById(Long id) {
		return patientRepository.findById(id).get();
	}
	
	public Patient updatePatient(Patient patient) {
		return patientRepository.save(patient);
	}
	
	public void deletePatient(Patient patient) {
		patientRepository.delete(patient);
	}
	
}
