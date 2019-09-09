package com.management.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="MEDICINE")
public class Medicine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long medicineId;
	
	@Column(name = "NAME")
	private String medicineName;
	
	@Column(name = "DOSAGE")
	private int dosage;
	
	@Column(name = "CONSUMPTION")
	private MedicineEnum consumption;
	
	@OneToMany(mappedBy = "medicine")
	private List<ConsultationMedicine> consultationMedicines;

	public Medicine() {
		super();
	}

	public Medicine(String medicineName, int dosage, MedicineEnum consumption) {
		super();
		this.medicineName = medicineName;
		this.dosage = dosage;
		this.consumption = consumption;
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

	public int getDosage() {
		return dosage;
	}

	public void setDosage(int dosage) {
		this.dosage = dosage;
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
