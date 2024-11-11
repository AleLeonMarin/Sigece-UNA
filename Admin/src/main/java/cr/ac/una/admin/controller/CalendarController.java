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

    // Inicialización inicial que solo se ejecuta una vez
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendarView = new CalendarView();
        calendarView.setPrefSize(800, 550);
        gestionCalendar = new Calendar("Gestiones");
        gestionCalendar.setShortName("Gestiones");
        gestionCalendar.setStyle(Calendar.Style.STYLE1);

        createCalendar();
    }

    // Método de inicialización para cada carga de la vista
    @Override
    public void initialize() {
        // Obtener el usuario actualizado y configurar el CalendarSource
        user = (UsersDto) AppContext.getInstance().get("User");

        // Remover el CalendarSource previo si existe
        calendarView.getCalendarSources().clear();

        // Crear un nuevo CalendarSource con el nombre del usuario actualizado
        calendarSource = new CalendarSource(
                "Calendario de gestiones de: " + user.getName() + " " + user.getLastNames());
        calendarSource.getCalendars().add(gestionCalendar);

        // Añadir el nuevo CalendarSource al CalendarView
        calendarView.getCalendarSources().add(calendarSource);

        // Limpiar y volver a añadir el CalendarView en el stackPane
        stackPane.getChildren().clear();
        stackPane.getChildren().add(calendarView);

        // Cargar gestiones y actualizar el hilo de tiempo del calendario
        loadGestions();
        startCalendarUpdateThread();
    }

    private void loadGestions() {
        try {
            GestionService service = new GestionService();
            Respuesta respuesta = service.getGestiones();

            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.ERROR, "Error en carga de gestiones", this.getStage(),
                        respuesta.getMensaje());
            } else {
                // Obtener el usuario actual desde AppContext
                UsersDto currentUser = (UsersDto) AppContext.getInstance().get("User");
                List<GestionsDto> gestiones = (List<GestionsDto>) respuesta.getResultado("Gestiones");

                Platform.runLater(() -> {
                    gestionCalendar.clear(); // Limpiar las entradas anteriores en el calendario

                    gestiones.forEach(g -> {
                        // Filtrar la gestión solo si el usuario es Requester, Assigned o está en
                        // Approvers
                        boolean isRequester = g.getRequester() != null
                                && g.getRequester().getId().equals(currentUser.getId());
                        boolean isAssigned = g.getAssigned() != null
                                && g.getAssigned().getId().equals(currentUser.getId());
                        boolean isApprover = g.getApprovers() != null && g.getApprovers().stream()
                                .anyMatch(approver -> approver.getId().equals(currentUser.getId()));

                        if (isRequester || isAssigned || isApprover) {
                            // Crear y añadir la entrada al calendario si cumple con los criterios
                            Entry<String> gestionEntry = new Entry<>(g.getSubject());
                            gestionEntry.setInterval(g.getCreationDate().atStartOfDay(),
                                    g.getSolutionDate().atStartOfDay());

                            // Guardar la entrada en el mapa para acceso
                            gestionMap.put(gestionEntry, g);

                            // Añadir la entrada al calendario
                            gestionCalendar.addEntry(gestionEntry);
                        }
                    });
                });

                // Añadir un listener de selección para manejar doble clic
                calendarView.setEntryDetailsPopOverContentCallback(param -> {
                    Entry<?> entry = param.getEntry();
                    if (entry != null && gestionMap.containsKey(entry)) {
                        param.getNode().setOnMouseClicked(event -> {
                            if (event.getClickCount() == 2) { // Detectar doble clic
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
                    return null; // Evitar mostrar el popover predeterminado
                });
            }
        } catch (Exception ex) {
            new Mensaje().showModal(AlertType.ERROR, "Error en la aplicación", this.getStage(),
                    "Error al cargar las gestiones desde el servicio");
        }
    }

    private void createCalendar() {
        // Este método prepara la vista del calendario, que solo necesita llamarse una
        // vez
        if (!stackPane.getChildren().contains(calendarView)) {
            stackPane.getChildren().add(calendarView);
        }
    }

    private void startCalendarUpdateThread() {
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

    @FXML
    void onMouseClickedImgvExit(MouseEvent event) {
        FlowController.getInstance().goViewInWindow("PrincipalView");
        this.getStage().close();
    }
}
