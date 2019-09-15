package com.management.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.management.controller.dto.AppointmentDTO;
import com.management.entity.Consultation;
import com.management.entity.Location;
import com.management.entity.Patient;
import com.management.repository.ConsultationRepository;
import com.management.repository.LocationRepository;
import com.management.repository.PatientRepository;

@Service
public class ConsultationService {

	private ConsultationRepository consultationRepository;
	
	private LocationRepository locationRepository;
	
	private PatientRepository patientRepository;

	public ConsultationService(ConsultationRepository consultationRepository, LocationRepository locationRepository, PatientRepository patientRepository) {
		super();
		this.consultationRepository = consultationRepository;
		this.locationRepository = locationRepository;
		this.patientRepository = patientRepository;
	}
	
	public void saveConsultation(Consultation consultation) {
		consultationRepository.save(consultation);
	}
	
	public void saveConsultationFromDto(AppointmentDTO dto) {
		Consultation consultation = new Consultation();
		if(dto.getPatient() != null) {
			consultation.setPatient(dto.getPatient());
		}else {
			Patient p = new Patient();
			p.setFirstName(dto.getName());
			p = patientRepository.save(p);
			consultation.setPatient(p);
		}
		consultation.setTitle(dto.getName());
		consultation.setStartDate(dto.getStartDate());
		consultation.setStartTime(LocalTime.parse(dto.getFrom()));
		consultation.setEndDate(dto.getEndDate());
		consultation.setEndTime(LocalTime.parse(dto.getTo()));
		saveConsultation(consultation);
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
	
	public List<Consultation> findByLocation(Location location){
		return consultationRepository.findByLocation(location);
	}
	
	public List<Location> findAllLocations(){
		return locationRepository.findAll();
	}
	
	public Location findLocationByName(String name) {
		return locationRepository.findByName(name);
	}
	
	public Location saveLocation(Location location) {
		Location l = null;
		if(findLocationByName(location.getName()) == null) {
			l = locationRepository.save(location);
		}
		return l;
		
	}
}
