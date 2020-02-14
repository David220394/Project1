package com.management.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.management.controller.dto.AppointmentDTO;
import com.management.controller.dto.ConsultationDTO;
import com.management.controller.dto.ConsultationMedicineDTO;
import com.management.controller.dto.ReportDto;
import com.management.entity.Consultation;
import com.management.entity.ConsultationMedicine;
import com.management.entity.Location;
import com.management.entity.Medicine;
import com.management.entity.MedicineEnum;
import com.management.entity.Patient;
import com.management.repository.ConsultationMedicineRepository;
import com.management.repository.ConsultationRepository;
import com.management.repository.LocationRepository;
import com.management.repository.PatientRepository;

@Service
public class ConsultationService {

	private ConsultationRepository consultationRepository;
	
	private LocationRepository locationRepository;
	
	private MedicineService medicineService;
	
	private PatientRepository patientRepository;
	
	private ConsultationMedicineRepository consultationMedicineRepository;

	public ConsultationService(ConsultationRepository consultationRepository, LocationRepository locationRepository, PatientRepository patientRepository, MedicineService medicineService, ConsultationMedicineRepository consultationMedicineRepository) {
		super();
		this.consultationRepository = consultationRepository;
		this.locationRepository = locationRepository;
		this.patientRepository = patientRepository;
		this.consultationMedicineRepository = consultationMedicineRepository;
		this.medicineService = medicineService;
	}
	
	public void saveConsultation(ConsultationDTO dto) {
		Patient p = null;
		if(dto.getPatient() != null) {
			p = patientRepository.findByFirstNameAndLastName(dto.getPatient().getFirstName(), dto.getPatient().getLastName());
		}
	
		Consultation consultation = new Consultation();
		consultation.setComplaints(dto.getComplaints());
		consultation.setEars(dto.getEars());
		consultation.setThroat(dto.getThroat());
		consultation.setNeck(dto.getNeck());
		consultation.setNose(dto.getNose());
		consultation.setIlS(dto.getIlS());
		consultation.setDiagnosis(dto.getDiagnosis());
		consultation.setCharge(dto.getCharge());
		consultation.setStartDate(dto.getStartDate());
		consultation.setStartTime(dto.getStartTime());
		consultation.setEndDate(dto.getEndDate());
		consultation.setEndTime(dto.getEndTime());
		consultation.setTitle(p.getFirstName()+" | "+ p.getLastName());
		consultation.setLocation(findLocationByName(dto.getLocation()));
		consultation.setPatient(p);
		consultation.setOthers(dto.getOthers());
		
		consultationRepository.save(consultation);
		
		for (ConsultationMedicineDTO cMDto : dto.getConsultationMedicines()) {
			Medicine medicine = medicineService.findByMedicineName(cMDto.getMedicine());
			if(medicine == null) {
				medicine = new Medicine();
				medicine.setMedicineName(cMDto.getMedicine());
				medicine.setConsumption(cMDto.getConsumption());
				medicine = medicineService.addMedicine(medicine);
			}
			ConsultationMedicine cM = new ConsultationMedicine();
			cM.setConsultation(consultation);
			cM.setMedicine(medicine);
			cM.setDosage(cMDto.getDosage());
			cM.setIntakeTimes(cMDto.getIntakeTimes());
			cM.setPeriod(cMDto.getPeriod());
			consultationMedicineRepository.save(cM);
		}
	}
	
	public void saveConsultation(Consultation consultation) {
				
		consultationRepository.save(consultation);
		for (ConsultationMedicine cM : consultation.getConsultationMedicines()) {
			Medicine medicine = medicineService.findByMedicineName(cM.getMedicine().getMedicineName());
			if(medicine == null) {
				medicineService.addMedicine(cM.getMedicine());
			}
			cM.setConsultation(consultation);
			consultationMedicineRepository.save(cM);
		}
	}
	
	public Consultation saveConsultationFromDto(AppointmentDTO dto) {
		Consultation consultation = new Consultation();
		if(dto.getPatient() != null) {
			consultation.setPatient(dto.getPatient());
		}else {
			String[] names = dto.getName().split(" ");
			Patient p = new Patient();
			if(names.length > 1) {
				p.setFirstName(names[0]);
				p.setLastName(names[1]);
			}else {
				p.setFirstName(names[0]);
			}
			p = patientRepository.save(p);
			consultation.setPatient(p);
		}
		Location l = locationRepository.findByName(dto.getLocation());
		consultation.setTitle(dto.getName());
		consultation.setStartDate(dto.getStartDate());
		consultation.setStartTime(dto.getFrom());
		consultation.setEndDate(dto.getEndDate());
		consultation.setEndTime(dto.getTo());
		consultation.setLocation(l);
		consultation.setTitle(dto.getName());
		return consultationRepository.save(consultation);
	}
	
	public List<Consultation> findAllConsultations() {
		return consultationRepository.findAll();
	}
	
	public List<Consultation> findByPatient(String firstName, String lastName){
		Patient patient = patientRepository.findByFirstNameAndLastName(firstName, lastName);
		return consultationRepository.findByPatient(patient);
	}
	
	public Consultation findByStartDateAndStartTimeAndComplaintsAndDiagnosis(LocalDate startDate, LocalTime startTime, String conplaints, String diagnosis) {
		return consultationRepository.findByStartDateAndStartTimeAndComplaintsAndDiagnosis(startDate, startTime, conplaints, diagnosis);
	}
	
	public ReportDto findConsultationFor6Months(){
		int currentMonthCharge=0;
		int month1Charge=0;
		int month2Charge=0;
		int month3Charge=0;
		int month4Charge=0;
		int month5Charge=0;
		int consultationCount=0;
		for(Consultation c : consultationRepository.findAll()) {
			Period period = Period.between(c.getStartDate(), LocalDate.now());
			int month = period.getMonths();
			switch(month) {
			case 0:
				currentMonthCharge+=c.getCharge();
				consultationCount++;
				break;
			case 1:
				month1Charge+=c.getCharge();
				break;
			case 2:
				month2Charge+=c.getCharge();
				break;
			case 3:
				month3Charge+=c.getCharge();
				break;
			case 4:
				month4Charge+=c.getCharge();
				break;
			case 5:
				month5Charge+=c.getCharge();
				break;
			}
		}
		return new ReportDto(currentMonthCharge, month1Charge, month2Charge, month3Charge, month4Charge, month5Charge, consultationCount);
	}
	
	public ReportDto getRportForMonthAndYear(int month, int year) {
		int consultation = 0;
		int charges = 0;
		for(Consultation c : consultationRepository.findAll()) {
			Period period = Period.between(c.getStartDate(), LocalDate.now());
			int m = period.getMonths();
			if(month == period.getMonths() && year == period.getYears()) {
				charges += c.getCharge();
				consultation++;
			}
		}
		return new ReportDto(charges, consultation);
	}
	
	
	
	public void exportConsultation(File file) {
		List<Consultation> consultations = consultationRepository.findAll();
		 XSSFWorkbook workbook = new XSSFWorkbook();
	     XSSFSheet sheet = workbook.createSheet("Applicant");

        int rowNum = 0;
        int c = 0;
        Row r = sheet.createRow(rowNum++);
        r.createCell(c++).setCellValue((String) "Date");
        r.createCell(c++).setCellValue((String) "Time");
        r.createCell(c++).setCellValue((String) "Patient");
        r.createCell(c++).setCellValue((String) "Location");
        r.createCell(c++).setCellValue((String) "Complaints");
        r.createCell(c++).setCellValue((String) "Nose");
        r.createCell(c++).setCellValue((String) "Ears");
        r.createCell(c++).setCellValue((String) "Throats");
        r.createCell(c++).setCellValue((String) "Neack");
        r.createCell(c++).setCellValue((String) "ILS");
        r.createCell(c++).setCellValue((String) "Others");
        r.createCell(c++).setCellValue((String) "Diagnosis");
        r.createCell(c++).setCellValue((String) "Charge");
		for (Consultation consultation : consultations) {
			Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            row.createCell(colNum++).setCellValue((String) consultation.getStartDate().toString());
            row.createCell(colNum++).setCellValue((String) consultation.getStartTime().toString());
            if (consultation.getPatient() != null) {
				row.createCell(colNum++).setCellValue((String) consultation.getPatient().getFirstName() + " "
						+ consultation.getPatient().getLastName());
			}else {
				row.createCell(colNum++).setCellValue("Unknown");
			}
			row.createCell(colNum++).setCellValue((String) consultation.getLocation().getName());
            row.createCell(colNum++).setCellValue((String) consultation.getComplaints());
            row.createCell(colNum++).setCellValue((String) consultation.getNose());
            row.createCell(colNum++).setCellValue((String) consultation.getEars());
            row.createCell(colNum++).setCellValue((String) consultation.getThroat());
            row.createCell(colNum++).setCellValue((String) consultation.getNeck());
            row.createCell(colNum++).setCellValue((String) consultation.getIlS());
            row.createCell(colNum++).setCellValue((String) consultation.getOthers());
            row.createCell(colNum++).setCellValue((String) consultation.getDiagnosis());
            row.createCell(colNum++).setCellValue((String) String.valueOf(consultation.getCharge()));
		}
		try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public Consultation findById(long id) {
		Optional<Consultation> c = consultationRepository.findById(id);
		return c.get();
	}
	
	public List<Consultation> findByPatient(Patient patient){
		return consultationRepository.findByPatient(patient);
	}
	
	public List<Consultation> findByLocation(Location location){
		return consultationRepository.findByLocation(location);
	}
	
	public List<Location> findAllLocations(){
		return locationRepository.findAll();
	}
	
	public Location findLocationByName(String name) {
		return locationRepository.findByName(name);
	}
	
	public Location saveLocation(Location location) {
		Location l = null;
		if(findLocationByName(location.getName()) == null) {
			l = locationRepository.save(location);
		}
		return l;
		
	}
}
