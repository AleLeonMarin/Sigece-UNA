package cr.ac.una.admin.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

public class CalendarController extends Controller implements Initializable {

    @FXML
    private StackPane stackPane;

    CalendarView calendarView;

    CalendarSource calendarSource;

    @Override
    public void initialize() {

        calendarView = new CalendarView();
        calendarSource = new CalendarSource("Calendario de gestiones de: " + " Alejandro");
        createCalendar();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void createCalendar() {
        Calendar gestion = new Calendar("Gestiones");
        gestion.setShortName("Gestiones");
        gestion.setStyle(Calendar.Style.STYLE1);
        calendarSource.getCalendars().add(gestion);
        calendarView.getCalendarSources().add(calendarSource);
        calendarView.setRequestedTime(LocalTime.now());
        stackPane.getChildren().add(calendarView);

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
            }
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }

}
