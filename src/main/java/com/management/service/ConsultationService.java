package com.management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.entity.Consultation;
import com.management.entity.Location;
import com.management.entity.Patient;
import com.management.repository.ConsultationRepository;
import com.management.repository.LocationRepository;

@Service
public class ConsultationService {

	private ConsultationRepository consultationRepository;
	
	private LocationRepository locationRepository;

	public ConsultationService(ConsultationRepository consultationRepository, LocationRepository locationRepository) {
		super();
		this.consultationRepository = consultationRepository;
		this.locationRepository = locationRepository;
	}
	
	public List<Consultation> findAllConsultations() {
		return consultationRepository.findAll();
	}
	
	public Consultation findById(long id) {
		return consultationRepository.findById(id).get();
	}
	
	public List<Consultation> findByPatient(Patient patient){
		return consultationRepository.findByPatient(patient);
	}
	
	public List<Location> findAllLocations(){
		return locationRepository.findAll();
	}
	
	
	
}
