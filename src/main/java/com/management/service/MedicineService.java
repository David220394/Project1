package com.management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.entity.Medicine;
import com.management.repository.MedicineRepository;

@Service
public class MedicineService {

	private MedicineRepository medicineRepository;

	public MedicineService(MedicineRepository medicineRepository) {
		super();
		this.medicineRepository = medicineRepository;
	}
	
	public Medicine addMedicine(Medicine medicine) {
		return medicineRepository.save(medicine);
	}
	
	public Medicine findByMedicineName(String name) {
		return medicineRepository.findByMedicineName(name);
	}
	
	public List<String> findAllMedicineName(){
		return medicineRepository.findAllMedicineName();
	}
}
