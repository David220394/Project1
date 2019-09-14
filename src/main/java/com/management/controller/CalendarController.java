package com.management.controller;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.AllDayView;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.DateControl.CreateEntryParameter;
import com.calendarfx.view.VirtualGrid;
import com.management.config.SpringFXMLLoader;
import com.management.config.StageManager;
import com.management.controller.dto.AppointmentDTO;
import com.management.entity.Consultation;
import com.management.entity.FxmlView;
import com.management.service.ConsultationService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@Controller
public class CalendarController implements Initializable {

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private ConsultationService consultationService;

	@FXML
	private Pane pnlCalendar;

	private CalendarView calendarView;

	private AddConsultationController addConsultationController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		calendarView = new CalendarView();
		CalendarSource myCalendarSource = new CalendarSource("My Calendars");
		List<Calendar> calendars = new ArrayList<>();
		List<String> locations = consultationService.findAllLocations();
		int count = 1;
		if (locations.size() != 0) {
			for (String location : locations) {
				Calendar cal = new Calendar(location);
				cal.setStyle(Style.valueOf("STYLE" + count));
				calendars.add(cal);
				count++;
			}
		}else {
			Calendar cal = new Calendar("Home");
			cal.setStyle(Style.valueOf("STYLE" + count));
			calendars.add(cal);
			Calendar cal1 = new Calendar("Clinics");
			cal1.setStyle(Style.valueOf("STYLE" + count));
			calendars.add(cal1);
		}
		myCalendarSource.getCalendars().addAll(populateEntry(calendars));
		calendarView.getCalendarSources().addAll(myCalendarSource);

		calendarView.setRequestedTime(LocalTime.now());

		Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
			@Override
			public void run() {
				while (true) {
					Platform.runLater(() -> {
						calendarView.setToday(LocalDate.now());
						calendarView.setTime(LocalTime.now());
					});

					try {
						// update every 10 seconds
						sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			};
		};

		addEntry();
		updateTimeThread.setPriority(Thread.MIN_PRIORITY);
		updateTimeThread.setDaemon(true);
		updateTimeThread.start();
		pnlCalendar.getChildren().setAll(calendarView);

	}

	public void addEntry() {
		calendarView.setEntryFactory(new Callback<DateControl.CreateEntryParameter, Entry<?>>() {

			@Override
			public Entry<?> call(CreateEntryParameter param) {
				Entry entry = null;
				DateControl control = param.getDateControl();
				VirtualGrid grid = control.getVirtualGrid();
				ZonedDateTime time = param.getZonedDateTime();
				DayOfWeek firstDayOfWeek = calendarView.getFirstDayOfWeek();

				ZonedDateTime lowerTime = grid.adjustTime(time, false, firstDayOfWeek);
				ZonedDateTime upperTime = grid.adjustTime(time, true, firstDayOfWeek);

				if (Duration.between(time, lowerTime).abs().minus(Duration.between(time, upperTime).abs())
						.isNegative()) {
					time = lowerTime;
				} else {
					time = upperTime;
				}
				SpringFXMLLoader loader = stageManager.getSpringFXMLLoader();
				Parent parent = stageManager.getParentView(FxmlView.NEW_APPOINTMENT);
				AddConsultationController dialogController = loader.getLoader()
						.<AddConsultationController>getController();
				dialogController.setTimes(time.toLocalTime().toString(), time.toLocalTime().plusHours(1).toString());
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setScene(scene);
				stage.showAndWait();
				AppointmentDTO appointmentDTO = dialogController.getDto();
				if (appointmentDTO != null) {
					
					entry = new Entry<>();
					Consultation consultation = new Consultation();
					entry.setTitle(appointmentDTO.getName());
					consultation.setTitle(appointmentDTO.getName());
					entry.changeStartDate(time.toLocalDate());
					consultation.setStartDate(time.toLocalDate());
					entry.changeStartTime(LocalTime.parse(appointmentDTO.getFrom()));
					consultation.setStartTime(LocalTime.parse(appointmentDTO.getFrom()));
					entry.changeEndDate(entry.getStartDate());
					consultation.setEndDate(entry.getStartDate());
					entry.changeEndTime(LocalTime.parse(appointmentDTO.getTo()));
					consultation.setEndTime(LocalTime.parse(appointmentDTO.getTo()));
					entry.setLocation(appointmentDTO.getLocation());
					consultation.setLocation(appointmentDTO.getLocation());
					consultationService.saveConsultation(consultation);
					if (control instanceof AllDayView) {
						entry.setFullDay(true);
					}
				}
				return entry;
			}
		});

	}
}
