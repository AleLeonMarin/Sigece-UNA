package cr.ac.una.admin.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;

import cr.ac.una.admin.model.GestionsDto;
import cr.ac.una.admin.model.UsersDto;
import cr.ac.una.admin.service.GestionService;
import cr.ac.una.admin.util.AppContext;
import cr.ac.una.admin.util.FlowController;
import cr.ac.una.admin.util.Mensaje;
import cr.ac.una.admin.util.Respuesta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;

public class CalendarController extends Controller implements Initializable {

    @FXML
    private StackPane stackPane;

    private CalendarView calendarView;
    private CalendarSource calendarSource;
    UsersDto user;
    GestionsDto gestion;
    Calendar gestionCalendar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = (UsersDto) AppContext.getInstance().get("User");
        gestion = new GestionsDto();
        calendarView = new CalendarView();
        calendarSource = new CalendarSource("Calendario de gestiones de:" + user.getName() + " " + user.getLastNames());
        createCalendar();
    }

    private void loadGestions() {
        try {
            GestionService service = new GestionService();
            Respuesta respuesta = service.getGestiones();
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Error en carga de gestiones", this.getStage(),
                        respuesta.getMensaje());
            } else {
                List<GestionsDto> gestiones = (List<GestionsDto>) respuesta.getResultado("Gestiones");
                Platform.runLater(() -> {
                    gestiones.forEach(g -> {
                        Entry<String> gestionEntry = new Entry<>(g.getSubject());
                        gestionEntry.setInterval(g.getCreationDate().atStartOfDay(),
                                g.getSolutionDate().atStartOfDay());
                        gestionCalendar.addEntry(gestionEntry);
                    });
                });
            }
        } catch (Exception ex) {
            new Mensaje().showModal(AlertType.ERROR, "Error en la aplicación", this.getStage(),
                    "Error al cargar las gestiones desde el servicio");
        }
    }

    private void createCalendar() {
        gestionCalendar = new Calendar("Gestiones");
        gestionCalendar.setShortName("Gestiones");
        gestionCalendar.setStyle(Calendar.Style.STYLE1);
        calendarSource.getCalendars().add(gestionCalendar);
        calendarView.getCalendarSources().add(calendarSource);
        calendarView.setRequestedTime(LocalTime.now());
        stackPane.getChildren().add(calendarView);

        loadGestions();

        // Hilo para actualizar la fecha y hora del calendario cada 10 segundos
        Thread updateTimeThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Platform.runLater(() -> {
                    calendarView.setToday(LocalDate.now());
                    calendarView.setTime(LocalTime.now());
                });
                try {
                    Thread.sleep(10000); // Actualizar cada 10 segundos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Calendar: Update Time Thread");

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }

    @Override
    public void initialize() {

    }
}
