package cr.ac.una.admin.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import cr.ac.una.admin.model.AreasDto;
import cr.ac.una.admin.model.GestionsDto;
import cr.ac.una.admin.model.SubactivitiesDto;
import cr.ac.una.admin.model.UsersDto;
import cr.ac.una.admin.service.GestionService;
import cr.ac.una.admin.util.AppContext;
import cr.ac.una.admin.util.FlowController;
import cr.ac.una.admin.util.Mensaje;
import cr.ac.una.admin.util.Respuesta;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class PrincipalController extends Controller implements Initializable {

        @FXML
        private Button btnAreas, btnGestions, btnSearch, btnReports, btnCalendar, btnSalir;
        @FXML
        private Label gestCompletadasLabel, gestEnCursoLabel, gestPendientesLabel, userNameLabel, labelTime, clock;
        @FXML
        private ImageView imvTiempo;
        @FXML
        private TableView<GestionsDto> tbvGestionAprobar, tbvGestionArea, tbvGestionAreaAsignada, tbvGestionCurso,
                        tbvGestionPendientes, tbvGestionSemana, tbvGestionesUser;
        @FXML
        private PieChart ChartGestiones;
        private UsersDto user;
        private String username;

        @Override
        public void initialize(URL url, ResourceBundle rb) {
                // Configura el reloj y las columnas de las tablas al inicializar
                updateClock();
                populateTableColumnsGestions();
                populateTableColumnsOnCurse();
                populateTableColumnsApproved();
                populateTableColumnsArea();
                populateTableColumnsAreaAsignada();
                populateTableColumnsWeek();
                populateTableColumnsPendientes();
        }

        @Override
        public void initialize() {
                // Limpia las tablas y carga datos del usuario actual
                clearTables();
                user = (UsersDto) AppContext.getInstance().get("User");
                username = user.getName();
                loadGestionesData();
                updateGreeting();
                if (user.getRoles().stream().anyMatch(r -> r.getName().equals("Solicitante"))) {
                        btnAreas.setVisible(false);
                        btnReports.setVisible(false);
                        btnCalendar.setVisible(false);
                        btnSearch.setVisible(false);
                } else if (user.getRoles().stream().anyMatch(r -> r.getName().equals("Gestor"))) {
                        btnAreas.setVisible(false);
                        btnGestions.setVisible(false);
                } else {
                        btnAreas.setVisible(false);
                        btnGestions.setVisible(false);
                        btnReports.setVisible(false);
                        btnCalendar.setVisible(false);
                        btnSearch.setVisible(false);
                }
        }

        /**
         * Clears all data from the table views.
         */
        private void clearTables() {
                tbvGestionAprobar.getItems().clear();
                tbvGestionArea.getItems().clear();
                tbvGestionAreaAsignada.getItems().clear();
                tbvGestionCurso.getItems().clear();
                tbvGestionPendientes.getItems().clear();
                tbvGestionSemana.getItems().clear();
                tbvGestionesUser.getItems().clear();
        }

        /**
         * Loads the data for the management tables, calculates statistics, and sets up
         * the pie chart.
         */
        private void loadGestionesData() {
                user = (UsersDto) AppContext.getInstance().get("User");
                GestionService service = new GestionService();
                Respuesta respuesta = service.getGestiones();

                if (!respuesta.getEstado()) {
                        new Mensaje().showModal(AlertType.ERROR, "Error al cargar gestiones", this.getStage(),
                                        respuesta.getMensaje());
                        return;
                }

                List<GestionsDto> allGestiones = (List<GestionsDto>) respuesta.getResultado("Gestiones");

                List<GestionsDto> userGestiones = allGestiones.stream()
                                .filter(g -> (g.getRequester() != null && g.getRequester().getId().equals(user.getId()))
                                                ||
                                                (g.getAssigned() != null
                                                                && g.getAssigned().getId().equals(user.getId()))
                                                ||
                                                (g.getApprovers() != null && g.getApprovers().stream()
                                                                .anyMatch(approver -> approver.getId()
                                                                                .equals(user.getId()))))
                                .collect(Collectors.toList());

                long pendingCount = userGestiones.stream()
                                .filter(g -> "E".equals(g.getState()) || "S".equals(g.getState()))
                                .count();
                long inProgressCount = userGestiones.stream().filter(g -> "C".equals(g.getState())).count();
                long completedCount = userGestiones.stream().filter(g -> "A".equals(g.getState())).count();

                gestPendientesLabel.setText(String.valueOf(pendingCount));
                gestEnCursoLabel.setText(String.valueOf(inProgressCount));
                gestCompletadasLabel.setText(String.valueOf(completedCount));

                List<GestionsDto> gestionesAprobar = new ArrayList<>();

                for (GestionsDto gestion : allGestiones) {
                        if (gestion.getApprovers() != null) {
                                for (UsersDto approver : gestion.getApprovers()) {
                                        if (approver.getId().equals(user.getId()) && !"A".equals(gestion.getState())
                                                        && !"R".equals(gestion.getState())) {
                                                gestionesAprobar.add(gestion);
                                                break;
                                        }
                                }
                        } else {
                                new Mensaje().showModal(AlertType.WARNING, "Gestión sin aprobadores", this.getStage(),
                                                "La gestión " + gestion.getSubject()
                                                                + " no tiene aprobadores asignados");
                        }
                }

                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                                new PieChart.Data("Pendientes", pendingCount),
                                new PieChart.Data("En Curso", inProgressCount),
                                new PieChart.Data("Completadas", completedCount));
                ChartGestiones.setData(pieChartData);
                ChartGestiones.setTitle("Distribución de Gestiones");

                cargarTabla(tbvGestionesUser, userGestiones);
                cargarTabla(tbvGestionCurso,
                                userGestiones.stream().filter(g -> "C".equals(g.getState()))
                                                .collect(Collectors.toList()));
                cargarTabla(tbvGestionPendientes, userGestiones.stream()
                                .filter(g -> "E".equals(g.getState()) || "S".equals(g.getState()))
                                .collect(Collectors.toList()));
                cargarTabla(tbvGestionAprobar, gestionesAprobar);
                cargarTabla(tbvGestionSemana, userGestiones.stream()
                                .filter(g -> g.getCreationDate().isAfter(LocalDate.now().minusDays(7)))
                                .collect(Collectors.toList()));

                cargarTabla(tbvGestionArea, userGestiones.stream()
                                .filter(g -> g.getActivity().getArea() == user.getAreas()
                                                || g.getSubactivities().getActivity().getArea() == user.getAreas())
                                .collect(Collectors.toList()));

                cargarTabla(tbvGestionAreaAsignada, userGestiones.stream()
                                .filter(g -> g.getActivity().getArea() == user.getAreas()
                                                || g.getSubactivities().getActivity().getArea() == user.getAreas())
                                .collect(Collectors.toList()));
        }

        /**
         * Loads the specified list of management data into a table.
         */
        private void cargarTabla(TableView<GestionsDto> table, List<GestionsDto> gestiones) {
                ObservableList<GestionsDto> gestionesObservableList = FXCollections.observableArrayList(gestiones);
                table.setItems(gestionesObservableList);
                table.refresh();
        }

        /**
         * Sets up the columns for the main management table.
         */
        private void populateTableColumnsGestions() {
                TableColumn<GestionsDto, String> colAsunto = new TableColumn<>("Asunto");
                colAsunto.setCellValueFactory(cd -> cd.getValue().subject);

                TableColumn<GestionsDto, String> colDescripcion = new TableColumn<>("Descripción");
                colDescripcion.setCellValueFactory(cd -> cd.getValue().description);

                TableColumn<GestionsDto, String> colFechaCreacion = new TableColumn<>("Fecha de Creación");
                colFechaCreacion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getCreationDate().toString()));

                TableColumn<GestionsDto, String> colFechaSolucion = new TableColumn<>("Fecha de Solución");
                colFechaSolucion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getSolutionDate().toString()));

                tbvGestionesUser.getColumns()
                                .addAll(Arrays.asList(colAsunto, colDescripcion, colFechaCreacion, colFechaSolucion));
        }

        /**
         * Sets up the columns for the "In Progress" management table.
         */
        private void populateTableColumnsOnCurse() {
                TableColumn<GestionsDto, String> colAsunto = new TableColumn<>("Asunto");
                colAsunto.setCellValueFactory(cd -> cd.getValue().subject);

                TableColumn<GestionsDto, String> colDescripcion = new TableColumn<>("Descripción");
                colDescripcion.setCellValueFactory(cd -> cd.getValue().description);

                TableColumn<GestionsDto, String> colFechaCreacion = new TableColumn<>("Fecha de Creación");
                colFechaCreacion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getCreationDate().toString()));

                TableColumn<GestionsDto, String> colFechaSolucion = new TableColumn<>("Fecha de Solución");
                colFechaSolucion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getSolutionDate().toString()));

                tbvGestionCurso.getColumns()
                                .addAll(Arrays.asList(colAsunto, colDescripcion, colFechaCreacion, colFechaSolucion));
        }

        /**
         * Sets up the columns for the "Approved" management table.
         */
        private void populateTableColumnsApproved() {
                TableColumn<GestionsDto, String> colAsunto = new TableColumn<>("Asunto");
                colAsunto.setCellValueFactory(cd -> cd.getValue().subject);

                TableColumn<GestionsDto, String> colDescripcion = new TableColumn<>("Descripción");
                colDescripcion.setCellValueFactory(cd -> cd.getValue().description);

                TableColumn<GestionsDto, String> colFechaCreacion = new TableColumn<>("Fecha de Creación");
                colFechaCreacion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getCreationDate().toString()));

                TableColumn<GestionsDto, String> colFechaSolucion = new TableColumn<>("Fecha de Solución");
                colFechaSolucion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getSolutionDate().toString()));

                tbvGestionAprobar.getColumns()
                                .addAll(Arrays.asList(colAsunto, colDescripcion, colFechaCreacion, colFechaSolucion));
        }

        /**
         * Sets up the columns for the management table associated with areas.
         */
        private void populateTableColumnsArea() {
                TableColumn<GestionsDto, String> colAsunto = new TableColumn<>("Asunto");
                colAsunto.setCellValueFactory(cd -> cd.getValue().subject);

                TableColumn<GestionsDto, String> colDescripcion = new TableColumn<>("Descripción");
                colDescripcion.setCellValueFactory(cd -> cd.getValue().description);

                TableColumn<GestionsDto, String> colFechaCreacion = new TableColumn<>("Fecha de Creación");
                colFechaCreacion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getCreationDate().toString()));

                TableColumn<GestionsDto, String> colFechaSolucion = new TableColumn<>("Fecha de Solución");
                colFechaSolucion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getSolutionDate().toString()));

                tbvGestionArea.getColumns()
                                .addAll(Arrays.asList(colAsunto, colDescripcion, colFechaCreacion, colFechaSolucion));
        }

        /**
         * Sets up the columns for the table of managements assigned to an area.
         */
        private void populateTableColumnsAreaAsignada() {
                TableColumn<GestionsDto, String> colAsunto = new TableColumn<>("Asunto");
                colAsunto.setCellValueFactory(cd -> cd.getValue().subject);

                TableColumn<GestionsDto, String> colDescripcion = new TableColumn<>("Descripción");
                colDescripcion.setCellValueFactory(cd -> cd.getValue().description);

                TableColumn<GestionsDto, String> colFechaCreacion = new TableColumn<>("Fecha de Creación");
                colFechaCreacion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getCreationDate().toString()));

                TableColumn<GestionsDto, String> colFechaSolucion = new TableColumn<>("Fecha de Solución");
                colFechaSolucion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getSolutionDate().toString()));

                tbvGestionAreaAsignada.getColumns()
                                .addAll(Arrays.asList(colAsunto, colDescripcion, colFechaCreacion, colFechaSolucion));
        }

        /**
         * Sets up the columns for the weekly management table.
         */
        private void populateTableColumnsWeek() {
                TableColumn<GestionsDto, String> colAsunto = new TableColumn<>("Asunto");
                colAsunto.setCellValueFactory(cd -> cd.getValue().subject);

                TableColumn<GestionsDto, String> colDescripcion = new TableColumn<>("Descripción");
                colDescripcion.setCellValueFactory(cd -> cd.getValue().description);

                TableColumn<GestionsDto, String> colFechaCreacion = new TableColumn<>("Fecha de Creación");
                colFechaCreacion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getCreationDate().toString()));

                TableColumn<GestionsDto, String> colFechaSolucion = new TableColumn<>("Fecha de Solución");
                colFechaSolucion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getSolutionDate().toString()));

                tbvGestionSemana.getColumns()
                                .addAll(Arrays.asList(colAsunto, colDescripcion, colFechaCreacion, colFechaSolucion));
        }

        /**
         * Sets up the columns for the pending management table.
         */
        private void populateTableColumnsPendientes() {
                TableColumn<GestionsDto, String> colAsunto = new TableColumn<>("Asunto");
                colAsunto.setCellValueFactory(cd -> cd.getValue().subject);

                TableColumn<GestionsDto, String> colDescripcion = new TableColumn<>("Descripción");
                colDescripcion.setCellValueFactory(cd -> cd.getValue().description);

                TableColumn<GestionsDto, String> colFechaCreacion = new TableColumn<>("Fecha de Creación");
                colFechaCreacion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getCreationDate().toString()));

                TableColumn<GestionsDto, String> colFechaSolucion = new TableColumn<>("Fecha de Solución");
                colFechaSolucion
                                .setCellValueFactory(cd -> new SimpleStringProperty(
                                                cd.getValue().getSolutionDate().toString()));

                tbvGestionPendientes.getColumns()
                                .addAll(Arrays.asList(colAsunto, colDescripcion, colFechaCreacion, colFechaSolucion));
        }

        /**
         * Updates the greeting based on the current time of day.
         */
        private void updateGreeting() {
                LocalTime currentTime = LocalTime.now();
                int hour = currentTime.getHour();

                if (hour >= 6 && hour < 12) {
                        labelTime.setText("Buenos Días, " + username);
                        imvTiempo.setImage(
                                        new Image(getClass().getResource("/cr/ac/una/admin/resources/dia-nublado.png")
                                                        .toExternalForm()));
                } else if (hour >= 12 && hour < 18) {
                        labelTime.setText("Buenas Tardes, " + username);
                        imvTiempo
                                        .setImage(new Image(getClass().getResource("/cr/ac/una/admin/resources/dom.png")
                                                        .toExternalForm()));
                } else {
                        labelTime.setText("Buenas Noches, " + username);
                        imvTiempo.setImage(
                                        new Image(getClass().getResource("/cr/ac/una/admin/resources/luna.png")
                                                        .toExternalForm()));
                }
        }

        /**
         * Continuously updates the clock label with the current time.
         */
        private void updateClock() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                        LocalTime currentTime = LocalTime.now();
                        this.clock.setText(currentTime.format(formatter));
                }), new KeyFrame(Duration.seconds(1)));
                clock.setCycleCount(Timeline.INDEFINITE);
                clock.play();
        }

        @FXML
        void onActionBtnAreas(ActionEvent event) {
                FlowController.getInstance().goViewInWindow("AreasView");
                this.getStage().close();
        }

        @FXML
        void onActionBtnGestions(ActionEvent event) {
                FlowController.getInstance().goViewInWindow("GestionView");
                this.getStage().close();
        }

        @FXML
        void onActionBtnSearch(ActionEvent event) {
                FlowController.getInstance().goViewInWindow("Searchs");
                this.getStage().close();
        }

        @FXML
        void onActionBtnCalendar(ActionEvent event) {
                FlowController.getInstance().goViewInWindow("CalendarView");
                this.getStage().close();
        }

        @FXML
        void onActionBtnReports(ActionEvent event) {
                FlowController.getInstance().goViewInWindow("ReportsView");
        }

        @FXML
        void onActionBtnSalir(ActionEvent event) {
                FlowController.getInstance().goViewInWindow("LoginView");
                this.getStage().close();
        }
}
