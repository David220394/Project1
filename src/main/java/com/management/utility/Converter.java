package com.management.utility;

import java.util.ArrayList;
import java.util.List;

import com.management.controller.dto.ConsultationDTO;
import com.management.controller.dto.ConsultationMedicineDTO;
import com.management.controller.dto.PatientDTO;
import com.management.entity.Consultation;
import com.management.entity.ConsultationMedicine;
import com.management.entity.Location;
import com.management.entity.Patient;

public class Converter {
	
	public static ConsultationDTO consultationToDto(Consultation c) {
		List<ConsultationMedicineDTO> mDtos = new ArrayList<ConsultationMedicineDTO>();
		for (ConsultationMedicine consultationMedicine : c.getConsultationMedicines()) {
			mDtos.add(consultationMedicineToDto(consultationMedicine));
		}
		Location l = c.getLocation();
		String locName = null;
		if(l != null) {
			locName = l.getName();
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
				c.getStartDate(),
				c.getStartTime(),
				c.getEndDate(),
				c.getEndTime(),
				locName,
				mDtos
				);
		return dto;
		
	}

	public static ConsultationMedicineDTO consultationMedicineToDto(ConsultationMedicine cM) {
		ConsultationMedicineDTO dto = new ConsultationMedicineDTO(
				cM.getMedicine().getMedicineName(),
				cM.getDosage(),
				cM.getPeriod(),
				cM.getIntakeTimes()
				);
		if(cM.getMedicine().getConsumption() != null) {
			dto.setConsumption(cM.getMedicine().getConsumption().name());
		}
		return dto;
	}

	public static PatientDTO patientToDto(Patient p) {
		List<ConsultationDTO> cDtos = new ArrayList();
		if(p.getConsultations() != null) {
			for (Consultation consultation : p.getConsultations()) {
				cDtos.add(consultationToDto(consultation));
			}
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
