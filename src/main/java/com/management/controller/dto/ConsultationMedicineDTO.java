package com.management.controller.dto;

public class ConsultationMedicineDTO {

private String medicine;
	private int noOfDays;
	private String consumption;
	private int intakeTimes;
	
	public ConsultationMedicineDTO() {
		super();
	}
	public ConsultationMedicineDTO(String medicine,int noOfDays, String consumption, int intakeTimes) {
		super();
		this.medicine = medicine;
		this.noOfDays = noOfDays;
		this.consumption = consumption;
		this.intakeTimes = intakeTimes;
	}
	public String getMedicine() {
		return medicine;
	}
	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}
	
	public int getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
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
	
	
}
