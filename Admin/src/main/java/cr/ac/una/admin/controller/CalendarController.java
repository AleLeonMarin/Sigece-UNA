package cr.ac.una.admin.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalendarController extends Controller implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private ImageView imgvExit;
    @FXML
    private VBox vbContainer;

    private CalendarView calendarView;
    private CalendarSource calendarSource;
    private UsersDto user;
    private Calendar gestionCalendar;
    private final Map<Entry<String>, GestionsDto> gestionMap = new HashMap<>();

    // Initializes the calendar view and sets up the main calendar instance
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendarView = new CalendarView();
        calendarView.setPrefSize(800, 550);
        gestionCalendar = new Calendar("Gestiones");
        gestionCalendar.setShortName("Gestiones");
        gestionCalendar.setStyle(Calendar.Style.STYLE1);
        createCalendar();
    }

    // Updates calendar data on each view load
    @Override
    public void initialize() {
        user = (UsersDto) AppContext.getInstance().get("User");
        calendarView.getCalendarSources().clear();
        calendarSource = new CalendarSource(
                "Calendario de gestiones de: " + user.getName() + " " + user.getLastNames());
        calendarSource.getCalendars().add(gestionCalendar);
        calendarView.getCalendarSources().add(calendarSource);
        stackPane.getChildren().clear();
        stackPane.getChildren().add(calendarView);
        loadGestions();
        startCalendarUpdateThread();
    }

    // Loads user-specific tasks and schedules them on the calendar
    private void loadGestions() {
        try {
            GestionService service = new GestionService();
            Respuesta respuesta = service.getGestiones();

            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Error en carga de gestiones", this.getStage(),
                        respuesta.getMensaje());
            } else {
                UsersDto currentUser = (UsersDto) AppContext.getInstance().get("User");
                List<GestionsDto> gestiones = (List<GestionsDto>) respuesta.getResultado("Gestiones");

                Platform.runLater(() -> {
                    gestionCalendar.clear();

                    gestiones.forEach(g -> {
                        boolean isRequester = g.getRequester() != null
                                && g.getRequester().getId().equals(currentUser.getId());
                        boolean isAssigned = g.getAssigned() != null
                                && g.getAssigned().getId().equals(currentUser.getId());
                        boolean isApprover = g.getApprovers() != null && g.getApprovers().stream()
                                .anyMatch(approver -> approver.getId().equals(currentUser.getId()));

                        if (isRequester || isAssigned || isApprover) {
                            Entry<String> gestionEntry = new Entry<>(g.getSubject());
                            gestionEntry.setInterval(g.getCreationDate().atStartOfDay(),
                                    g.getSolutionDate().atStartOfDay());
                            gestionMap.put(gestionEntry, g);
                            gestionCalendar.addEntry(gestionEntry);
                        }
                    });
                });

                calendarView.setEntryDetailsPopOverContentCallback(param -> {
                    Entry<?> entry = param.getEntry();
                    if (entry != null && gestionMap.containsKey(entry)) {
                        param.getNode().setOnMouseClicked(event -> {
                            if (event.getClickCount() == 2) {
                                GestionsDto gestion = gestionMap.get(entry);
                                AppContext.getInstance().set("Gestion", gestion);
                                FlowController.getInstance().goViewInWindow("GestionView");
                                Platform.runLater(() -> {
                                    if (this.getStage().isShowing()) {
                                        this.getStage().close();
                                    }
                                });
                            }
                        });
                    }
                    return null;
                });
            }
        } catch (Exception ex) {
            new Mensaje().showModal(AlertType.ERROR, "Error en la aplicación", this.getStage(),
                    "Error al cargar las gestiones desde el servicio");
        }
    }

    // Sets up the initial calendar view
    private void createCalendar() {
        if (!stackPane.getChildren().contains(calendarView)) {
            stackPane.getChildren().add(calendarView);
        }
    }

    // Updates the calendar's current date and time every 10 seconds
    private void startCalendarUpdateThread() {
        Thread updateTimeThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Platform.runLater(() -> {
                    calendarView.setToday(LocalDate.now());
                    calendarView.setTime(LocalTime.now());
                });
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Calendar: Update Time Thread");

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }

    // Handles the exit button click, returning to the main view
    @FXML
    void onMouseClickedImgvExit(MouseEvent event) {
        FlowController.getInstance().goViewInWindow("PrincipalView");
        ((Stage) imgvExit.getScene().getWindow()).close();
    }
}
