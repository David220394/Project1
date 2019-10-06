package com.management.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="MEDICINE")
public class Medicine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long medicineId;
	
	@Column(name = "NAME")
	private String medicineName;
	
	@Column(name = "CONSUMPTION")
	private MedicineEnum consumption;
	
	@OneToMany(mappedBy = "medicine")
	@JsonIgnore
	private List<ConsultationMedicine> consultationMedicines;

	public Medicine() {
		super();
	}

	public Medicine(String medicineName, MedicineEnum consumption) {
		super();
		this.medicineName = medicineName;
		this.consumption = consumption;
	}
	
	public Medicine(String medicineName) {
		super();
		this.medicineName = medicineName;
	}

	public long getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(long medicineId) {
		this.medicineId = medicineId;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public MedicineEnum getConsumption() {
		return consumption;
	}

	public void setConsumption(MedicineEnum consumption) {
		this.consumption = consumption;
	}

	public List<ConsultationMedicine> getConsultationMedicines() {
		return consultationMedicines;
	}

	public void setConsultationMedicines(List<ConsultationMedicine> consultationMedicines) {
		this.consultationMedicines = consultationMedicines;
	}

}
