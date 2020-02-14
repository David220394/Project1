package com.management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.management.entity.ConsultationMedicine;
import com.management.entity.Medicine;
import com.management.repository.ConsultationMedicineRepository;
import com.management.repository.MedicineRepository;

@Service
public class MedicineService {

	private MedicineRepository medicineRepository;
	private ConsultationMedicineRepository consultationMedicineRepository;

	public MedicineService(MedicineRepository medicineRepository,ConsultationMedicineRepository consultationMedicineRepository) {
		super();
		this.medicineRepository = medicineRepository;
		this.consultationMedicineRepository = consultationMedicineRepository;
	}
	
	public Medicine addMedicine(Medicine medicine) {
		return medicineRepository.save(medicine);
	}
	
	public Medicine findByMedicineName(String name) {
		return medicineRepository.findByMedicineName(name);
	}
//	
//	public List<String> findAllMedicineName(){
//		return medicineRepository.findAllMedicineName();
//	}
	
	public List<String> findAllMedicineName(){
		List<String> meds = new ArrayList<>();
		List<Medicine> medicines = medicineRepository.findAll();
		for(Medicine med : medicines) {
			List<ConsultationMedicine> cMeds = consultationMedicineRepository.findByMedicine(med);
			for(ConsultationMedicine cMed : cMeds) {
				StringBuilder sb = new StringBuilder();
				sb.append("Name: "+med.getMedicineName() + "|");
				sb.append("Consumption: "+med.getConsumption() + "|");
				sb.append("Dosage: "+cMed.getDosage() + "|");
				sb.append("Intake: "+cMed.getIntakeTimes() + "|");
				sb.append("Period: "+cMed.getPeriod());
				meds.add(sb.toString());
			}
		}
		
		return meds;
	}
}
