package com.management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.entity.Patient;
import com.management.repository.PatientRepository;

@Service
public class PatientService {

	private PatientRepository patientRepository;

	public PatientService(PatientRepository patientRepository) {
		super();
		this.patientRepository = patientRepository;
	}
	
	public Patient savePatient(Patient patient) {
		Patient p = null;
		if(patientRepository.findByFirstNameAndLastName(patient.getFirstName(), patient.getLastName()) == null) {
			p = patientRepository.save(patient);
		}
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
	
}
