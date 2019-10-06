package com.management.controller.dto;

public class ConsultationMedicineDTO {

private String medicine;
	private String period;
	private String dosage;
	private String consumption;
	private int intakeTimes;
	
	public ConsultationMedicineDTO() {
		super();
	}
	public ConsultationMedicineDTO(String medicine,String dosage,String period, int intakeTimes) {
		super();
		this.medicine = medicine;
		this.dosage = dosage;
		this.period = period;
		this.intakeTimes = intakeTimes;
	}
	public String getMedicine() {
		return medicine;
	}
	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}
	
	public String getConsumption() {
		return consumption;
	}
	public void setConsumption(String consumption) {
		this.consumption = consumption;
	}
	public int getIntakeTimes() {
		return intakeTimes;
	}
	public void setIntakeTimes(int intakeTimes) {
		this.intakeTimes = intakeTimes;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	
	
	
	
}
