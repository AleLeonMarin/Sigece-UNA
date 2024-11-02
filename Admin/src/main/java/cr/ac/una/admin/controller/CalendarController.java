package cr.ac.una.admin.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;

import cr.ac.una.admin.util.FlowController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;

public class CalendarController extends Controller implements Initializable {

    @FXML
    private StackPane stackPane;

    private CalendarView calendarView;
    private CalendarSource calendarSource;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendarView = new CalendarView();
        calendarSource = new CalendarSource("Calendario de gestiones de: Alejandro");
        createCalendar();
    }

    private void createCalendar() {
        Calendar gestionCalendar = new Calendar("Gestiones");
        gestionCalendar.setShortName("Gestiones");
        gestionCalendar.setStyle(Calendar.Style.STYLE1);
        calendarSource.getCalendars().add(gestionCalendar);
        calendarView.getCalendarSources().add(calendarSource);
        calendarView.setRequestedTime(LocalTime.now());
        stackPane.getChildren().add(calendarView);

        // Crear y añadir una entrada de prueba al calendario
        Entry<String> gestionEntry = new Entry<>("Prueba");
        LocalDateTime now = LocalDateTime.now();
        gestionEntry.setInterval(now, now.plus(5, ChronoUnit.HOURS));
        gestionCalendar.addEntry(gestionEntry);

        // Configurar el evento de clic para todas las entradas en el CalendarView
        calendarView.setEntryDetailsPopOverContentCallback(param -> {
            if (param.getEntry().equals(gestionEntry)) {
                FlowController.getInstance().goViewInWindow("GestionView");
            }
            return null; // Esto evita que se muestre el popover predeterminado
        });

        // Hilo para actualizar la fecha y hora del calendario cada 10 segundos
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        sleep(10000); // Actualizar cada 10 segundos
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

    @Override
    public void initialize() {

    }
}
