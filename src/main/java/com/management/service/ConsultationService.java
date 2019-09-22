package com.management.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.management.controller.dto.AppointmentDTO;
import com.management.controller.dto.ConsultationDTO;
import com.management.controller.dto.ConsultationMedicineDTO;
import com.management.entity.Consultation;
import com.management.entity.ConsultationMedicine;
import com.management.entity.Location;
import com.management.entity.Medicine;
import com.management.entity.MedicineEnum;
import com.management.entity.Patient;
import com.management.repository.ConsultationMedicineRepository;
import com.management.repository.ConsultationRepository;
import com.management.repository.LocationRepository;
import com.management.repository.PatientRepository;

@Service
public class ConsultationService {

	private ConsultationRepository consultationRepository;
	
	private LocationRepository locationRepository;
	
	private MedicineService medicineService;
	
	private PatientRepository patientRepository;
	
	private ConsultationMedicineRepository consultationMedicineRepository;

	public ConsultationService(ConsultationRepository consultationRepository, LocationRepository locationRepository, PatientRepository patientRepository, MedicineService medicineService, ConsultationMedicineRepository consultationMedicineRepository) {
		super();
		this.consultationRepository = consultationRepository;
		this.locationRepository = locationRepository;
		this.patientRepository = patientRepository;
		this.consultationMedicineRepository = consultationMedicineRepository;
		this.medicineService = medicineService;
	}
	
	public void saveConsultation(ConsultationDTO dto) {
		Patient p = null;
		if(dto.getPatient() != null) {
			p = patientRepository.findByFirstNameAndLastName(dto.getPatient().getFirstName(), dto.getPatient().getLastName());
		}
	
		Consultation consultation = new Consultation();
		consultation.setComplaints(dto.getComplaints());
		consultation.setEars(dto.getEars());
		consultation.setThroat(dto.getThroat());
		consultation.setNeck(dto.getNeck());
		consultation.setNose(dto.getNose());
		consultation.setIlS(dto.getIlS());
		consultation.setDiagnosis(dto.getDiagnosis());
		consultation.setCharge(dto.getCharge());
		consultation.setStartDate(dto.getStartDate());
		consultation.setStartTime(dto.getStartTime());
		consultation.setEndDate(dto.getEndDate());
		consultation.setEndTime(dto.getEndTime());
		consultation.setLocation(findLocationByName(dto.getLocation()));
		consultation.setPatient(p);
		
		consultationRepository.save(consultation);
		
		for (ConsultationMedicineDTO cMDto : dto.getConsultationMedicines()) {
			Medicine medicine = medicineService.findByMedicineName(cMDto.getMedicine());
			if(medicine == null) {
				medicine = new Medicine();
				medicine.setMedicineName(cMDto.getMedicine());
				medicine.setConsumption(MedicineEnum.valueOf(cMDto.getConsumption()));
				medicine = medicineService.addMedicine(medicine);
			}
			ConsultationMedicine cM = new ConsultationMedicine();
			cM.setConsultation(consultation);
			cM.setMedicine(medicine);
			cM.setIntakeTimes(cMDto.getIntakeTimes());
			cM.setNoOfDays(cMDto.getNoOfDays());
			consultationMedicineRepository.save(cM);
		}
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
		consultationRepository.save(consultation);
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
