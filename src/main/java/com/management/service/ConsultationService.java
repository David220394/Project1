package com.management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.management.entity.Consultation;
import com.management.entity.Patient;
import com.management.repository.ConsultationRepository;

@Service
public class ConsultationService {

	private ConsultationRepository consultationRepository;

	public ConsultationService(ConsultationRepository consultationRepository) {
		super();
		this.consultationRepository = consultationRepository;
	}
	
	public List<Consultation> findAllConsultations() {
		return consultationRepository.findAll();
	}
	
	public void saveConsultation(Consultation consultation) {
		consultationRepository.save(consultation);
	}
	
	public Consultation findById(long id) {
		return consultationRepository.findById(id).get();
	}
	
	public List<Consultation> findByPatient(Patient patient){
		return consultationRepository.findByPatient(patient);
	}
	
	public List<Consultation> findByLocation(String location){
		return consultationRepository.findByLocation(location);
	}
	
	public List<String> findAllLocations(){
		return consultationRepository.findAll().stream().map(Consultation::getLocation).collect(Collectors.toList());
	}	
	
	
}
