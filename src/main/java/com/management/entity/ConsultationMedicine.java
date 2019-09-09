package com.management.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="CONSULTATION_MEDICINE_LINK")
public class ConsultationMedicine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="CONSULTATION")
	private Consultation consultation;
	
	@ManyToOne
	@JoinColumn(name="MEDICINE")
	private Medicine medicine;
	
	@Column(name = "INTAKE_PER_DAY")
	private int intakeTimes;

	public ConsultationMedicine() {
		super();
	}

	public ConsultationMedicine(Consultation consultation, Medicine medicine, int intakeTimes) {
		super();
		this.consultation = consultation;
		this.medicine = medicine;
		this.intakeTimes = intakeTimes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Consultation getConsultation() {
		return consultation;
	}

	public void setConsultation(Consultation consultation) {
		this.consultation = consultation;
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public int getIntakeTimes() {
		return intakeTimes;
	}

	public void setIntakeTimes(int intakeTimes) {
		this.intakeTimes = intakeTimes;
	}

	
}
