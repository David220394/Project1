package com.management.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.calendarfx.model.Entry;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="CONSULTATION")
public class Consultation{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long consultationId;
	
	@Column(name = "COMPLAINTS")
	private String complaints;
	
	@Column(name = "EARS")
	private String ears;
	
	@Column(name = "NOSE")
	private String nose;
	
	@Column(name = "THROAT")
	private String throat;
	
	@Column(name = "NECK")
	private String neck;
	
	@Column(name = "ILS")
	private String ilS;
	
	@Column(name = "OTHERS")
	private String others;

	@Column(name = "DIAGNOSIS")
	private String diagnosis;
	
	@Column(name = "CHARGE")
	private double charge;
	
	private String title;
	
	private LocalDate startDate;
	
	private LocalTime startTime;
	
	private LocalDate endDate;
	
	private LocalTime endTime;
	
	@ManyToOne
	@JoinColumn(name="LOCATION")
	private Location location;
	
	@OneToMany(mappedBy = "consultation",fetch = FetchType.EAGER)
	@JsonIgnore
	private List<ConsultationMedicine> consultationMedicines;
	
	@ManyToOne
	@JoinColumn(name="PATIENT")
	private Patient patient;
	
	public Consultation() {
		super();
	}

	public Consultation(String complaints, String ears, String nose, String throat, String neck, String ilS,
			String diagnosis,String others, int payment) {
		super();
		this.complaints = complaints;
		this.ears = ears;
		this.nose = nose;
		this.throat = throat;
		this.neck = neck;
		this.ilS = ilS;
		this.diagnosis = diagnosis;
		this.others = others;
	}

	public long getConsultationId() {
		return consultationId;
	}

	public void setConsultationId(long consultationId) {
		this.consultationId = consultationId;
	}

	public String getComplaints() {
		return complaints;
	}

	public void setComplaints(String complaints) {
		this.complaints = complaints;
	}

	public String getEars() {
		return ears;
	}

	public void setEars(String ears) {
		this.ears = ears;
	}

	public String getNose() {
		return nose;
	}

	public void setNose(String nose) {
		this.nose = nose;
	}

	public String getThroat() {
		return throat;
	}

	public void setThroat(String throat) {
		this.throat = throat;
	}

	public String getNeck() {
		return neck;
	}

	public void setNeck(String neck) {
		this.neck = neck;
	}

	public String getIlS() {
		return ilS;
	}

	public void setIlS(String ilS) {
		this.ilS = ilS;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public List<ConsultationMedicine> getConsultationMedicines() {
		return consultationMedicines;
	}

	public void setConsultationMedicines(List<ConsultationMedicine> consultationMedicines) {
		this.consultationMedicines = consultationMedicines;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public double getCharge() {
		return charge;
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}
	
	
}
