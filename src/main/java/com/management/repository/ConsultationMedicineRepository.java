package com.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.entity.ConsultationMedicine;
import com.management.entity.Medicine;

public interface ConsultationMedicineRepository extends JpaRepository<ConsultationMedicine, Long>  {

	public List<ConsultationMedicine> findByMedicine(Medicine medicine);
	
}
