package com.management.utility;

import java.util.ArrayList;
import java.util.List;

import com.management.controller.dto.ConsultationDTO;
import com.management.controller.dto.ConsultationMedicineDTO;
import com.management.controller.dto.PatientDTO;
import com.management.entity.Consultation;
import com.management.entity.ConsultationMedicine;
import com.management.entity.Patient;

public class Converter {
	
	public static ConsultationDTO consultationToDto(Consultation c) {
		List<ConsultationMedicineDTO> mDtos = new ArrayList<ConsultationMedicineDTO>();
		for (ConsultationMedicine consultationMedicine : c.getConsultationMedicines()) {
			mDtos.add(consultationMedicineToDto(consultationMedicine));
		}
		
		ConsultationDTO dto = new ConsultationDTO(
				c.getComplaints(),
				c.getEars(),
				c.getNose(),
				c.getThroat(),
				c.getNeck(),
				c.getIlS(),
				c.getDiagnosis(),
				c.getCharge(),
				c.getPayment(),
				c.getTitle(),
				c.getStartDate(),
				c.getStartTime(),
				c.getEndDate(),
				c.getEndTime(),
				c.getLocation().getName(),
				mDtos
				);
		return dto;
		
	}

	public static ConsultationMedicineDTO consultationMedicineToDto(ConsultationMedicine cM) {
		ConsultationMedicineDTO dto = new ConsultationMedicineDTO(
				cM.getMedicine().getMedicineName(),
				cM.getMedicine().getDosage(),
				cM.getMedicine().getConsumption().name(),
				cM.getIntakeTimes()
				);
		return dto;
	}

	public static PatientDTO patientToDto(Patient p) {
		List<ConsultationDTO> cDtos = new ArrayList();
		for (Consultation consultation : p.getConsultations()) {
			cDtos.add(consultationToDto(consultation));
		}
		PatientDTO dto = new PatientDTO();
		dto.setFirstName(p.getFirstName());
		dto.setLastName(p.getLastName());
		dto.setDateOfBirth(p.getDateOfBirth());
		dto.setPhoneNumber(p.getPhoneNumber());
		dto.setConsultations(cDtos);
		return dto;
	}
}
