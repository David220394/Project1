package com.management.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.management.entity.Consultation;
import com.management.entity.Location;
import com.management.entity.Patient;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

	//@Query(name = "SELETC * FROM CONSULTATION WHERE PATIENT=?",nativeQuery = true)
	public List<Consultation> findByPatient(Patient patient);
	 
	public List<Consultation> findByLocation(Location location);
	
	public Consultation findByStartDateAndStartTimeAndComplaintsAndDiagnosis(LocalDate startDate, LocalTime startTime, String conplaints, String diagnosis);
}
