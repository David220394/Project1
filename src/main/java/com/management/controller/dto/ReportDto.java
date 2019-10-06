package com.management.controller.dto;

public class ReportDto {
	
	private int currentMonthCharge;
	private int month1Charge;
	private int month2Charge;
	private int month3Charge;
	private int month4Charge;
	private int month5Charge;
	private int consultationCount;
	
	public ReportDto(int currentMonthCharge, int month1Charge, int month2Charge, int month3Charge, int month4Charge,
			int month5Charge, int consultationCount) {
		super();
		this.currentMonthCharge = currentMonthCharge;
		this.month1Charge = month1Charge;
		this.month2Charge = month2Charge;
		this.month3Charge = month3Charge;
		this.month4Charge = month4Charge;
		this.month5Charge = month5Charge;
		this.consultationCount = consultationCount;
	}
	
	public ReportDto(int currentMonthCharge, int consultationCount) {
		super();
		this.currentMonthCharge = currentMonthCharge;
		this.consultationCount = consultationCount;
	}



	public int getCurrentMonthCharge() {
		return currentMonthCharge;
	}

	public void setCurrentMonthCharge(int currentMonthCharge) {
		this.currentMonthCharge = currentMonthCharge;
	}

	public int getMonth1Charge() {
		return month1Charge;
	}

	public void setMonth1Charge(int month1Charge) {
		this.month1Charge = month1Charge;
	}

	public int getMonth2Charge() {
		return month2Charge;
	}

	public void setMonth2Charge(int month2Charge) {
		this.month2Charge = month2Charge;
	}

	public int getMonth3Charge() {
		return month3Charge;
	}

	public void setMonth3Charge(int month3Charge) {
		this.month3Charge = month3Charge;
	}

	public int getMonth4Charge() {
		return month4Charge;
	}

	public void setMonth4Charge(int month4Charge) {
		this.month4Charge = month4Charge;
	}

	public int getMonth5Charge() {
		return month5Charge;
	}

	public void setMonth5Charge(int month5Charge) {
		this.month5Charge = month5Charge;
	}

	public int getConsultationCount() {
		return consultationCount;
	}

	public void setConsultationCount(int consultationCount) {
		this.consultationCount = consultationCount;
	}
	
	
	
	

}
