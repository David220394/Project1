package com.management.controller;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.management.config.SpringFXMLLoader;
import com.management.config.StageManager;
import com.management.controller.dto.AppointmentDTO;
import com.management.entity.Consultation;
import com.management.entity.FxmlView;
import com.management.service.ConsultationService;
import com.sun.webkit.ContextMenu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.AllDayView;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.DateControl.ContextMenuParameter;
import com.calendarfx.view.DateControl.CreateEntryParameter;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import com.calendarfx.view.VirtualGrid;

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

		Calendar clinics = new Calendar("Clinics");
		Calendar home = new Calendar("Domicile");

		clinics.setStyle(Style.STYLE1);
		home.setStyle(Style.STYLE2);

		CalendarSource myCalendarSource = new CalendarSource("My Calendars");
		myCalendarSource.getCalendars().addAll(clinics, home);

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
			public Consultation call(CreateEntryParameter param) {
				Consultation entry = null;
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
					entry = new Consultation();
					entry.setTitle(appointmentDTO.getName());
					entry.changeStartDate(time.toLocalDate());
					entry.changeStartTime(LocalTime.parse(appointmentDTO.getFrom()));
					entry.changeEndDate(entry.getStartDate());
					entry.changeEndTime(LocalTime.parse(appointmentDTO.getTo()));
					if (control instanceof AllDayView) {
						entry.setFullDay(true);
					}
				}
				return entry;
			}
		});

	}

	public void populateEntry() {
	
	}
}
