package com.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.management.entity.Consultation;
import com.management.entity.Patient;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

	//@Query(name = "SELETC * FROM CONSULTATION WHERE PATIENT=?",nativeQuery = true)
	public List<Consultation> findByPatient(Patient patient);
	
	public List<Consultation> findByLocation(String locations);
	
	/*
	 * @Query(name = "SELECT DISTINCT(location) FROM CONSULTATION",nativeQuery =
	 * true) public List<String> findLocations();
	 */
	/*
	 * @Query(name =
	 * "SELETC * FROM CONSULTATION WHERE CONSULTATION_TIME=?",nativeQuery = true)
	 * public List<Consultation> findByDate(LocalDate consultationDate);
	 */
}
