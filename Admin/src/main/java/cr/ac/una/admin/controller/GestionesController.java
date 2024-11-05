package cr.ac.una.admin.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.admin.model.ActivitiesDto;
import cr.ac.una.admin.model.ApprovalsDto;
import cr.ac.una.admin.model.FollowsDto;
import cr.ac.una.admin.model.GestionsDto;
import cr.ac.una.admin.model.SubactivitiesDto;
import cr.ac.una.admin.model.UsersDto;
import cr.ac.una.admin.service.ActivitiesService;
import cr.ac.una.admin.service.GestionService;
import cr.ac.una.admin.service.SubactivitiesService;
import cr.ac.una.admin.service.UsersService;
import cr.ac.una.admin.util.AppContext;
import cr.ac.una.admin.util.FlowController;
import cr.ac.una.admin.util.Formato;
import cr.ac.una.admin.util.Mensaje;
import cr.ac.una.admin.util.Respuesta;
import javafx.fxml.Initializable;

public class GestionesController extends Controller implements Initializable {

    @FXML
    private MFXButton bntSearch;

    @FXML
    private MFXButton btnAdjuntarAprobacion;

    @FXML
    private MFXButton btnAdjuntarGestion;

    @FXML
    private MFXButton btnAdjuntarSeguimiento;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnExit;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXCheckbox chkActividad;

    @FXML
    private MFXCheckbox chkAprobacionGestion;

    @FXML
    private MFXCheckbox chkAprobadaAprobacion;

    @FXML
    private MFXCheckbox chkAprobadaGestion;

    @FXML
    private MFXCheckbox chkAprobadaSeguimiento;

    @FXML
    private MFXCheckbox chkAprobador1;

    @FXML
    private MFXCheckbox chkAprobador2;

    @FXML
    private MFXCheckbox chkAprobador4;

    @FXML
    private MFXCheckbox chkAprobador6;

    @FXML
    private MFXCheckbox chkArchivoSeguimiento;

    @FXML
    private MFXCheckbox chkCursoGestion;

    @FXML
    private MFXCheckbox chkCursoSeguimiento;

    @FXML
    private MFXCheckbox chkEsperaGestion;

    @FXML
    private MFXCheckbox chkRechazadaAprobacion;

    @FXML
    private MFXCheckbox chkRechazadaGestion;

    @FXML
    private MFXCheckbox chkRechazadaSeguimiento;

    @FXML
    private MFXCheckbox chkSubactividad;

    @FXML
    private MFXCheckbox chkTextoSeguimiento;

    @FXML
    private MFXComboBox<String> cmbActividades;

    @FXML
    private MFXComboBox<String> cmbAprobador1Gestion;

    @FXML
    private MFXComboBox<String> cmbAprobador2Gestion;

    @FXML
    private MFXComboBox<String> cmbAprobador3Gestion;

    @FXML
    private MFXComboBox<String> cmbAprobador4Gestion;

    @FXML
    private MFXComboBox<String> cmbAprobador5Gestion;

    @FXML
    private MFXComboBox<String> cmbAprobador6Gestion;

    @FXML
    private MFXComboBox<String> cmbAsiganadoGestion;

    @FXML
    private MFXComboBox<String> cmbSubactividades;

    @FXML
    private MFXDatePicker dpCreacionGestion;

    @FXML
    private MFXDatePicker dpFechaAprobacion;

    @FXML
    private MFXDatePicker dpFechaSeguimeinto;

    @FXML
    private MFXDatePicker dpSolucionGestion;

    @FXML
    private TabPane tbGestiones;

    @FXML
    private Tab tptAprobaciones;

    @FXML
    private Tab tptSeguimeinto;

    @FXML
    private Tab tptGestiones;

    @FXML
    private TextArea txaGestion;

    @FXML
    private TextArea txaSolucionAprobacion;

    @FXML
    private TextArea txaTextoSeguimiento;

    @FXML
    private MFXTextField txfAprobadorAprobacion;

    @FXML
    private MFXTextField txfAsuntoGestion;

    @FXML
    private MFXTextField txfComentarioAprobacion;

    @FXML
    private MFXTextField txfDescripcionAprobacion;

    @FXML
    private MFXTextField txfDescripcionSeguimiento;

    @FXML
    private MFXTextField txfIDAprobacion;

    @FXML
    private MFXTextField txfIDGestion;

    @FXML
    private MFXTextField txfIDSeguimiento;

    @FXML
    private MFXTextField txfSolicitanteGestion;

    @FXML
    private MFXTextField txfUsuarioSeguimiento;

    @FXML
    private VBox vboxHistorial;

    UsersDto user;

    GestionsDto gestion;

    FollowsDto follow;

    ApprovalsDto approval;

    GestionsDto calendarGestion;

    private Map<String, UsersDto> usersMap = new HashMap<>();

    private Map<String, ActivitiesDto> activityMap = new HashMap<>();

    private Map<String, SubactivitiesDto> subactivityMap = new HashMap<>();

    private Map<String, UsersDto> assigned = new HashMap<>();

    @Override
    public void initialize() {
        initValuesOfGestion();

        calendarGestion = (GestionsDto) AppContext.getInstance().get("Gestion");
        if (calendarGestion != null) {
            chargeGestion(calendarGestion.getId());
        } else {
            AppContext.getInstance().set("Gestion", new GestionsDto());
            calendarGestion = (GestionsDto) AppContext.getInstance().get("Gestion");
        }

    }

    // unique chkBox Selections

    private void selectionTypeOfGestion(CheckBox chkBox) {
        CheckBox[] checkBoxs = { chkActividad, chkSubactividad };
        for (CheckBox checkBox : checkBoxs) {
            if (checkBox != chkBox) {
                checkBox.setSelected(false);
            }
        }

    }

    private void selectType() {
        // Configura las acciones para cada CheckBox
        chkActividad.setOnAction(e -> {
            selectionTypeOfGestion(chkActividad); // Asegura que solo uno de los checkboxes esté seleccionado
            if (chkActividad.isSelected()) {
                cmbActividades.setDisable(false); // Activa el ComboBox de Actividades
                cmbActividades.setVisible(true); // Hace visible el ComboBox de Actividades
                cmbSubactividades.setDisable(true); // Desactiva el ComboBox de Subactividades
                cmbSubactividades.setVisible(false); // Oculta el ComboBox de Subactividades
            } else {
                cmbActividades.setDisable(true); // Desactiva y oculta el ComboBox de Actividades si se deselecciona
                cmbActividades.setVisible(false);
            }
        });

        chkSubactividad.setOnAction(e -> {
            selectionTypeOfGestion(chkSubactividad); // Asegura que solo uno de los checkboxes esté seleccionado
            if (chkSubactividad.isSelected()) {
                cmbSubactividades.setDisable(false); // Activa el ComboBox de Subactividades
                cmbSubactividades.setVisible(true); // Hace visible el ComboBox de Subactividades
                cmbActividades.setDisable(true); // Desactiva el ComboBox de Actividades
                cmbActividades.setVisible(false); // Oculta el ComboBox de Actividades
            } else {
                cmbSubactividades.setDisable(true); // Desactiva y oculta el ComboBox de Subactividades si se
                                                    // deselecciona
                cmbSubactividades.setVisible(false);
            }
        });
    }

    private void selectionTypeOfAprobacion(CheckBox chkBox) {
        CheckBox[] checkBoxs = { chkCursoGestion, chkEsperaGestion, chkAprobacionGestion, chkAprobadaGestion,
                chkRechazadaGestion };
        for (CheckBox checkBox : checkBoxs) {
            if (checkBox != chkBox) {
                checkBox.setSelected(false);
            }
        }
    }

    private void selectTypeOfAprobacion() {
        chkCursoGestion.setOnAction(e -> selectionTypeOfAprobacion(chkCursoGestion));
        chkEsperaGestion.setOnAction(e -> selectionTypeOfAprobacion(chkEsperaGestion));
        chkAprobacionGestion.setOnAction(e -> selectionTypeOfAprobacion(chkAprobacionGestion));
        chkAprobadaGestion.setOnAction(e -> selectionTypeOfAprobacion(chkAprobadaGestion));
        chkRechazadaGestion.setOnAction(e -> selectionTypeOfAprobacion(chkRechazadaGestion));
    }

    private void selectQuantityOfApprovers(CheckBox chkBox) {
        CheckBox[] checkBoxs = { chkAprobador1, chkAprobador2, chkAprobador4, chkAprobador6 };

        // Desmarcar todos los demás CheckBoxes
        for (CheckBox checkBox : checkBoxs) {
            if (checkBox != chkBox) {
                checkBox.setSelected(false);
            }
        }

        // Determinar el nivel seleccionado según el CheckBox marcado
        int selectedLevel = 0;
        if (chkAprobador1.isSelected()) {
            selectedLevel = 1;
        } else if (chkAprobador2.isSelected()) {
            selectedLevel = 2;
        } else if (chkAprobador4.isSelected()) {
            selectedLevel = 4;
        } else if (chkAprobador6.isSelected()) {
            selectedLevel = 6;
        }

        // Actualizar los ComboBox según el nivel seleccionado
        updateApproverComboBoxes(selectedLevel);
    }

    private void selectQuantityOfApprovers() {
        // Configura las acciones para cada CheckBox de aprobador
        chkAprobador1.setOnAction(e -> selectQuantityOfApprovers(chkAprobador1));
        chkAprobador2.setOnAction(e -> selectQuantityOfApprovers(chkAprobador2));
        chkAprobador4.setOnAction(e -> selectQuantityOfApprovers(chkAprobador4));
        chkAprobador6.setOnAction(e -> selectQuantityOfApprovers(chkAprobador6));

        // Llama a la función de actualización inicialmente para establecer el estado
        // correcto
        updateApproverComboBoxes(0); // 0 para deshabilitar todos inicialmente
    }

    private void updateApproverComboBoxes(int selectedLevel) {
        // Dependiendo del nivel seleccionado, habilita los ComboBox correspondientes
        cmbAprobador1Gestion.setDisable(selectedLevel < 1);
        cmbAprobador2Gestion.setDisable(selectedLevel < 2);
        cmbAprobador3Gestion.setDisable(selectedLevel < 3);
        cmbAprobador4Gestion.setDisable(selectedLevel < 4);
        cmbAprobador5Gestion.setDisable(selectedLevel < 5);
        cmbAprobador6Gestion.setDisable(selectedLevel < 6);

        // Controla la visibilidad de los ComboBox también
        cmbAprobador1Gestion.setVisible(selectedLevel >= 1);
        cmbAprobador2Gestion.setVisible(selectedLevel >= 2);
        cmbAprobador3Gestion.setVisible(selectedLevel >= 3);
        cmbAprobador4Gestion.setVisible(selectedLevel >= 4);
        cmbAprobador5Gestion.setVisible(selectedLevel >= 5);
        cmbAprobador6Gestion.setVisible(selectedLevel >= 6);

        // Si ningún CheckBox está seleccionado, deshabilita y oculta todos
        if (selectedLevel == 0) {
            cmbAprobador1Gestion.setDisable(true);
            cmbAprobador2Gestion.setDisable(true);
            cmbAprobador4Gestion.setDisable(true);
            cmbAprobador6Gestion.setDisable(true);

            cmbAprobador1Gestion.setVisible(false);
            cmbAprobador2Gestion.setVisible(false);
            cmbAprobador4Gestion.setVisible(false);
            cmbAprobador6Gestion.setVisible(false);
        }
    }

    private void initEntities() {
        // Inicialización de las entidades
        gestion = new GestionsDto();
        follow = new FollowsDto();
        approval = new ApprovalsDto();

    }

    private void initValuesOfGestion() {

        // Configuración inicial de los componentes

        selectType();
        selectTypeOfAprobacion();
        selectQuantityOfApprovers();

        // Configuración de los comboboxes
        cmbActividades.setVisible(false);
        cmbSubactividades.setVisible(false);

        cmbAprobador1Gestion.setVisible(false);
        cmbAprobador2Gestion.setVisible(false);
        cmbAprobador3Gestion.setVisible(false);
        cmbAprobador4Gestion.setVisible(false);
        cmbAprobador5Gestion.setVisible(false);
        cmbAprobador6Gestion.setVisible(false);

        // Configuración de los campos de texto

        user = (UsersDto) AppContext.getInstance().get("User");
        txfSolicitanteGestion.setText(user.getName() + " " + user.getLastNames());
        txfSolicitanteGestion.setEditable(false);

        txfAprobadorAprobacion.setText(user.getName() + " " + user.getLastNames());
        txfAprobadorAprobacion.setEditable(false);

        txfUsuarioSeguimiento.setText(user.getName() + " " + user.getLastNames());
        txfUsuarioSeguimiento.setEditable(false);

        chargeUsers();
        chargeActivity();
        chargeSubactivity();

        newGestion();

    }

    // format method
    private void format() {
        // ID's
        txfIDGestion.setTextFormatter(Formato.getInstance().integerFormat());
        txfIDAprobacion.setTextFormatter(Formato.getInstance().integerFormat());
        txfIDSeguimiento.setTextFormatter(Formato.getInstance().integerFormat());

        // Descriptions
        txfDescripcionAprobacion.setTextFormatter(Formato.getInstance().letrasFormat(200));
        txfDescripcionSeguimiento.setTextFormatter(Formato.getInstance().letrasFormat(200));
        txaGestion.setTextFormatter(Formato.getInstance().letrasFormat(1000));

        // Comments
        txfComentarioAprobacion.setTextFormatter(Formato.getInstance().letrasFormat(100));

        // Subject
        txfAsuntoGestion.setTextFormatter(Formato.getInstance().letrasFormat(100));

        // Solution
        txaSolucionAprobacion.setTextFormatter(Formato.getInstance().letrasFormat(1000));

    }

    // bindings

    private void bindGestion(Boolean isNew) {
        if (!isNew) {
            txfIDGestion.textProperty().bind(gestion.id);
        }
        txfAsuntoGestion.textProperty().bindBidirectional(gestion.subject);
        txaGestion.textProperty().bindBidirectional(gestion.description);
        dpCreacionGestion.valueProperty().bindBidirectional(gestion.creationDate);
        dpSolucionGestion.valueProperty().bindBidirectional(gestion.solutionDate);
    }

    // unbinds

    private void unbindGestion() {
        txfIDGestion.textProperty().unbind();
        txfAsuntoGestion.textProperty().unbindBidirectional(gestion.subject);
        txaGestion.textProperty().unbindBidirectional(gestion.description);
        dpCreacionGestion.valueProperty().unbindBidirectional(gestion.creationDate);
        dpSolucionGestion.valueProperty().unbindBidirectional(gestion.solutionDate);
    }

    private void clean() {
        chkActividad.setSelected(false);
        chkSubactividad.setSelected(false);
        cmbActividades.setDisable(true);
        cmbActividades.setVisible(false);
        cmbActividades.getSelectionModel().clearSelection();
        cmbSubactividades.setDisable(true);
        cmbSubactividades.setVisible(false);
        cmbSubactividades.getSelectionModel().clearSelection();
        cmbAprobador1Gestion.getSelectionModel().clearSelection();
        cmbAprobador1Gestion.setDisable(true);
        cmbAprobador1Gestion.setVisible(false);
        cmbAprobador2Gestion.getSelectionModel().clearSelection();
        cmbAprobador2Gestion.setDisable(true);
        cmbAprobador2Gestion.setVisible(false);
        cmbAprobador3Gestion.getSelectionModel().clearSelection();
        cmbAprobador3Gestion.setDisable(true);
        cmbAprobador3Gestion.setVisible(false);
        cmbAprobador4Gestion.getSelectionModel().clearSelection();
        cmbAprobador4Gestion.setDisable(true);
        cmbAprobador4Gestion.setVisible(false);
        cmbAprobador5Gestion.getSelectionModel().clearSelection();
        cmbAprobador5Gestion.setDisable(true);
        cmbAprobador5Gestion.setVisible(false);
        cmbAprobador6Gestion.getSelectionModel().clearSelection();
        cmbAprobador6Gestion.setDisable(true);
        cmbAprobador6Gestion.setVisible(false);
        cmbAsiganadoGestion.getSelectionModel().clearSelection();
        cmbAsiganadoGestion.setDisable(false);
        cmbAsiganadoGestion.setVisible(true);
        chkAprobador1.setSelected(false);
        chkAprobador2.setSelected(false);
        chkAprobador4.setSelected(false);
        chkAprobador6.setSelected(false);
        chkCursoGestion.setSelected(false);
        chkEsperaGestion.setSelected(false);
        chkAprobacionGestion.setSelected(false);
        chkAprobadaGestion.setSelected(false);
        chkRechazadaGestion.setSelected(false);

    }

    // new entity

    private void newGestion() {
        unbindGestion();
        clean();
        gestion = new GestionsDto();
        bindGestion(true);
        txfIDGestion.clear();
        txfIDGestion.requestFocus();
        chkEsperaGestion.setSelected(true);

    }

    // selection checkBoxes methods

    private void setState() {
        if (chkCursoGestion.isSelected()) {
            gestion.state.set("C");
        } else if (chkEsperaGestion.isSelected()) {
            gestion.state.set("E");
        } else if (chkAprobacionGestion.isSelected()) {
            gestion.state.set("A");
        } else if (chkAprobadaGestion.isSelected()) {
            gestion.state.set("P");
        } else if (chkRechazadaGestion.isSelected()) {
            gestion.state.set("R");
        }
    }

    // set approvers method

    private List<UsersDto> setApprovers() {
        List<UsersDto> selectedApprovers = new ArrayList<>();

        // Verificar y agregar los usuarios seleccionados de cada ComboBox basado en el
        // nivel de aprobación
        if (chkAprobador1.isSelected()) {
            String selectedUser1 = cmbAprobador1Gestion.getSelectionModel().getSelectedItem();
            if (selectedUser1 != null && usersMap.containsKey(selectedUser1)) {
                selectedApprovers.add(usersMap.get(selectedUser1));
            }
        }

        if (chkAprobador2.isSelected()) {
            // Incluir el usuario 1 y 2 si el nivel de aprobación es 2
            String selectedUser1 = cmbAprobador1Gestion.getSelectionModel().getSelectedItem();
            String selectedUser2 = cmbAprobador2Gestion.getSelectionModel().getSelectedItem();

            if (selectedUser1 != null && usersMap.containsKey(selectedUser1)) {
                selectedApprovers.add(usersMap.get(selectedUser1));
            }
            if (selectedUser2 != null && usersMap.containsKey(selectedUser2)) {
                selectedApprovers.add(usersMap.get(selectedUser2));
            }
        }

        if (chkAprobador4.isSelected()) {
            // Incluir los usuarios del 1 al 4 si el nivel de aprobación es 4
            String selectedUser1 = cmbAprobador1Gestion.getSelectionModel().getSelectedItem();
            String selectedUser2 = cmbAprobador2Gestion.getSelectionModel().getSelectedItem();
            String selectedUser3 = cmbAprobador3Gestion.getSelectionModel().getSelectedItem();
            String selectedUser4 = cmbAprobador4Gestion.getSelectionModel().getSelectedItem();

            if (selectedUser1 != null && usersMap.containsKey(selectedUser1)) {
                selectedApprovers.add(usersMap.get(selectedUser1));
            }
            if (selectedUser2 != null && usersMap.containsKey(selectedUser2)) {
                selectedApprovers.add(usersMap.get(selectedUser2));
            }
            if (selectedUser3 != null && usersMap.containsKey(selectedUser3)) {
                selectedApprovers.add(usersMap.get(selectedUser3));
            }
            if (selectedUser4 != null && usersMap.containsKey(selectedUser4)) {
                selectedApprovers.add(usersMap.get(selectedUser4));
            }
        }

        if (chkAprobador6.isSelected()) {
            // Incluir los usuarios del 1 al 6 si el nivel de aprobación es 6
            String selectedUser1 = cmbAprobador1Gestion.getSelectionModel().getSelectedItem();
            String selectedUser2 = cmbAprobador2Gestion.getSelectionModel().getSelectedItem();
            String selectedUser3 = cmbAprobador3Gestion.getSelectionModel().getSelectedItem();
            String selectedUser4 = cmbAprobador4Gestion.getSelectionModel().getSelectedItem();
            String selectedUser5 = cmbAprobador5Gestion.getSelectionModel().getSelectedItem();
            String selectedUser6 = cmbAprobador6Gestion.getSelectionModel().getSelectedItem();

            if (selectedUser1 != null && usersMap.containsKey(selectedUser1)) {
                selectedApprovers.add(usersMap.get(selectedUser1));
            }
            if (selectedUser2 != null && usersMap.containsKey(selectedUser2)) {
                selectedApprovers.add(usersMap.get(selectedUser2));
            }
            if (selectedUser3 != null && usersMap.containsKey(selectedUser3)) {
                selectedApprovers.add(usersMap.get(selectedUser3));
            }
            if (selectedUser4 != null && usersMap.containsKey(selectedUser4)) {
                selectedApprovers.add(usersMap.get(selectedUser4));
            }
            if (selectedUser5 != null && usersMap.containsKey(selectedUser5)) {
                selectedApprovers.add(usersMap.get(selectedUser5));
            }
            if (selectedUser6 != null && usersMap.containsKey(selectedUser6)) {
                selectedApprovers.add(usersMap.get(selectedUser6));
            }
        }

        return selectedApprovers;
    }

    private void setApproversGestion() {
        List<UsersDto> selectedApprovers = setApprovers();
        gestion.setApprovers(selectedApprovers);
    }

    // selection Activity or subactivity

    private void setActivity() {
        if (chkActividad.isSelected()) {
            String selectedActivity = cmbActividades.getSelectionModel().getSelectedItem();
            System.out.println(selectedActivity);
            if (selectedActivity != null && activityMap.containsKey(selectedActivity)) {
                gestion.setActivity(activityMap.get(selectedActivity));
                System.out.println(gestion.getActivity().getId());
            }
        } else if (chkSubactividad.isSelected()) {
            String selectedSubactivity = cmbSubactividades.getSelectionModel().getSelectedItem();
            System.out.println(selectedSubactivity);
            if (selectedSubactivity != null && subactivityMap.containsKey(selectedSubactivity)) {
                gestion.setSubactivities(subactivityMap.get(selectedSubactivity));
                System.out.println("Subactividad: " + gestion.getSubactivities().getId());
            }
        }
    }

    // assigned method

    private void setAssigned() {
        String selectedAssigned = cmbAsiganadoGestion.getSelectionModel().getSelectedItem();
        if (selectedAssigned != null && assigned.containsKey(selectedAssigned)) {
            // Obtener la lista de aprobadores de la gestión
            List<UsersDto> aprobadores = gestion.getApprovers();

            // Si la lista es nula, inicializarla y agregar todos los asignados
            if (aprobadores == null) {
                aprobadores = new ArrayList<>(assigned.values());
                gestion.setApprovers(aprobadores);
            } else {
                // Si la lista no es nula, agregar solo los usuarios que no estén ya en la lista
                UsersDto usuarioAsignado = assigned.get(selectedAssigned);
                if (!aprobadores.contains(usuarioAsignado)) {
                    aprobadores.add(usuarioAsignado);
                }
            }

            // Establecer el usuario asignado en la gestión (si aplica)
            gestion.setAssigned(assigned.get(selectedAssigned));
        }
    }

    // charging methods

    private void chargeUsers() {
        try {
            UsersService service = new UsersService();
            Respuesta respuesta = service.getUsers();
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Usuarios", getStage(), respuesta.getMensaje());
            } else {
                // Obtener el usuario actualmente logeado desde AppContext
                UsersDto currentUser = (UsersDto) AppContext.getInstance().get("User");
                List<UsersDto> users = (List<UsersDto>) respuesta.getResultado("Usuarios");

                usersMap.clear();
                for (UsersDto user : users) {
                    usersMap.put(user.getName() + " " + user.getLastNames(), user);
                }

                assigned.clear();
                for (UsersDto user : users) {
                    assigned.put(user.getName() + " " + user.getLastNames(), user);
                }

                // Limpiar items previos en los ComboBox
                cmbAsiganadoGestion.getItems().clear();
                cmbAprobador1Gestion.getItems().clear();
                cmbAprobador2Gestion.getItems().clear();
                cmbAprobador3Gestion.getItems().clear();
                cmbAprobador4Gestion.getItems().clear();
                cmbAprobador5Gestion.getItems().clear();
                cmbAprobador6Gestion.getItems().clear();

                for (UsersDto user : users) {
                    // Excluir el usuario logueado
                    if (!user.getId().equals(currentUser.getId())) {
                        String userName = user.getName() + " " + user.getLastNames();
                        cmbAsiganadoGestion.getItems().add(userName);

                        // Agregar el usuario a todos los ComboBox de aprobadores
                        cmbAprobador1Gestion.getItems().add(userName);
                        cmbAprobador2Gestion.getItems().add(userName);
                        cmbAprobador3Gestion.getItems().add(userName);
                        cmbAprobador4Gestion.getItems().add(userName);
                        cmbAprobador5Gestion.getItems().add(userName);
                        cmbAprobador6Gestion.getItems().add(userName);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error cargando los usuarios", e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar Usuarios", getStage(), "Error cargando los usuarios.");
        }
    }

    private void chargeActivity() {
        try {
            ActivitiesService service = new ActivitiesService();
            Respuesta respuesta = service.getActivities();
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Actividades", getStage(), respuesta.getMensaje());
            } else {
                List<ActivitiesDto> activities = (List<ActivitiesDto>) respuesta.getResultado("Activity");

                activityMap.clear();
                for (ActivitiesDto activity : activities) {
                    activityMap.put(activity.getName(), activity);
                }

                // Limpiar items previos en los ComboBox
                cmbActividades.getItems().clear();

                for (ActivitiesDto activity : activities) {
                    String activityName = activity.getName();
                    cmbActividades.getItems().add(activityName);

                }
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error cargando las actividades",
                    e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar Actividades", getStage(),
                    "Error cargando las actividades.");
        }
    }

    private void chargeSubactivity() {
        try {
            SubactivitiesService service = new SubactivitiesService();
            Respuesta respuesta = service.getSubactivities();
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Subactividades", getStage(), respuesta.getMensaje());
            } else {
                List<SubactivitiesDto> subactivities = (List<SubactivitiesDto>) respuesta
                        .getResultado("Subactivity");

                subactivityMap.clear();

                for (SubactivitiesDto subactivity : subactivities) {
                    subactivityMap.put(subactivity.getName(), subactivity);
                }

                // Limpiar items previos en los ComboBox
                cmbSubactividades.getItems().clear();

                for (SubactivitiesDto subactivity : subactivities) {
                    String subactivityName = subactivity.getName();
                    cmbSubactividades.getItems().add(subactivityName);

                }
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error cargando las subactividades",
                    e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar Subactividades", getStage(),
                    "Error cargando las subactividades.");
        }
    }

    private void chargeGestion(Long id) {
        try {
            GestionService service = new GestionService();
            Respuesta respuesta = service.getGestion(id);

            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Gestiones", getStage(), respuesta.getMensaje());
                return;
            }

            unbindGestion();
            gestion = (GestionsDto) respuesta.getResultado("Gestion");

            // Selección de actividad o subactividad
            if (gestion.getActivity() != null) {
                chkActividad.setSelected(true);
                if (cmbActividades.getItems().contains(gestion.getActivity().getName())) {
                    cmbActividades.setVisible(true);
                    cmbActividades.setDisable(false);
                    cmbActividades.getSelectionModel().selectItem(gestion.getActivity().getName());
                } else {
                    System.out.println(
                            "La actividad " + gestion.getActivity().getName() + " no está en la lista de opciones.");
                }
            } else if (gestion.getSubactivities() != null) {
                chkSubactividad.setSelected(true);
                if (cmbSubactividades.getItems().contains(gestion.getSubactivities().getName())) {
                    cmbSubactividades.setVisible(true);
                    cmbSubactividades.setDisable(false);
                    cmbSubactividades.getSelectionModel().selectItem(gestion.getSubactivities().getName());
                } else {
                    System.out.println("La subactividad " + gestion.getSubactivities().getName()
                            + " no está en la lista de opciones.");
                }
            }

            // Selección de asignado
            if (gestion.getAssigned() != null) {
                String assignedFullName = gestion.getAssigned().getName() + " " + gestion.getAssigned().getLastNames();
                if (cmbAsiganadoGestion.getItems().contains(assignedFullName)) {
                    cmbAsiganadoGestion.getSelectionModel().selectItem(assignedFullName);
                } else {
                    System.out.println("El asignado " + assignedFullName + " no está en la lista de opciones.");
                }
            }

            // Selección de aprobadores
            int approversCount = gestion.getApprovers().size();
            for (int i = 0; i < approversCount; i++) {
                String approverFullName = gestion.getApprovers().get(i).getName() + " "
                        + gestion.getApprovers().get(i).getLastNames();
                switch (i) {
                    case 0:
                        chkAprobador1.setSelected(true);
                        if (cmbAprobador1Gestion.getItems().contains(approverFullName)) {
                            cmbAprobador1Gestion.setVisible(true);
                            cmbAprobador1Gestion.setDisable(false);
                            cmbAprobador1Gestion.getSelectionModel().selectItem(approverFullName);
                        }
                        break;
                    case 1:
                        chkAprobador2.setSelected(true);
                        if (cmbAprobador2Gestion.getItems().contains(approverFullName)) {
                            cmbAprobador2Gestion.setVisible(true);
                            cmbAprobador2Gestion.setDisable(false);
                            cmbAprobador2Gestion.getSelectionModel().selectItem(approverFullName);
                        }
                        break;
                    case 2:
                        chkAprobador4.setSelected(true);
                        if (cmbAprobador4Gestion.getItems().contains(approverFullName)) {
                            cmbAprobador4Gestion.setVisible(true);
                            cmbAprobador4Gestion.setDisable(false);
                            cmbAprobador4Gestion.getSelectionModel().selectItem(approverFullName);
                        }
                        break;
                    case 3:
                        chkAprobador6.setSelected(true);
                        if (cmbAprobador6Gestion.getItems().contains(approverFullName)) {
                            cmbAprobador6Gestion.setVisible(true);
                            cmbAprobador6Gestion.setDisable(false);
                            cmbAprobador6Gestion.getSelectionModel().selectItem(approverFullName);
                        }
                        break;
                    default:
                        System.out.println("Número de aprobadores excede el límite soportado.");
                }
            }

            bindGestion(false);

        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error cargando la gestion", e);
            new Mensaje().showModal(AlertType.ERROR, "Cargar Gestion", getStage(), "Error cargando la gestion.");
        }
    }

    // save methods

    private void saveGestion() {
        try {
            setAssigned();
            setState();
            setActivity();
            setApproversGestion();
            UsersDto user = (UsersDto) AppContext.getInstance().get("User");
            this.gestion.setRequester(user);
            System.out.println("Solicitante: " + gestion.getRequester().getId());
            if (gestion.getActivity() != null) {
                System.out.println("Actividad: " + gestion.getActivity().getId());
            } else if (gestion.getSubactivities() != null) {
                System.out.println("Subactividad: " + gestion.getSubactivities().getId());
            }
            System.out.println(gestion.getState());
            GestionService service = new GestionService();
            Respuesta respuesta = service.createGestion(gestion);
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Gestión", getStage(), respuesta.getMensaje());
            } else {
                new Mensaje().showModal(AlertType.INFORMATION, "Gestión", getStage(), respuesta.getMensaje());
                unbindGestion();
                this.gestion = (GestionsDto) respuesta.getResultado("Gestion");
                bindGestion(false);
                new Mensaje().showModal(AlertType.INFORMATION, "Gestión", getStage(), "Gestión guardada con éxito");
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error guardando la gestion", e);
            new Mensaje().showModal(AlertType.ERROR, "Guardar Gestion", getStage(), "Error guardando la gestion.");
        }

    }

    // delete methods

    private void deleteGestion(Long id) {
        try {
            GestionService service = new GestionService();
            Respuesta respuesta = service.deleteGestion(id);
            if (!respuesta.getEstado()) {
                new Mensaje().showModal(AlertType.INFORMATION, "Gestiones", getStage(), respuesta.getMensaje());
            } else {
                new Mensaje().showModal(AlertType.CONFIRMATION, "Gestiones", getStage(),
                        "La gestion ha sido correctamente eliminada");
                newGestion();
            }
        } catch (Exception e) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, "Error eliminando la gestión", e);
            new Mensaje().showModal(AlertType.ERROR, "Eliminar Gestion", getStage(), "Error eliminando la gestión.");
        }
    }

    @FXML
    void onActionBtnAdjuntarAprobacion(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        File file = fileChooser.showOpenDialog(this.getStage());

    }

    @FXML
    void onActionBtnAdjuntarGestion(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        File file = fileChooser.showOpenDialog(this.getStage());

    }

    @FXML
    void onActionBtnAdjuntarSeguimeinto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        File file = fileChooser.showOpenDialog(this.getStage());

    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {
        if (tptGestiones.isSelected()) {
            deleteGestion(Long.valueOf(txfIDGestion.getText()));
        }

    }

    @FXML
    void onActionBtnExit(ActionEvent event) {
        AppContext.getInstance().set("Gestion", null);
        FlowController.getInstance().goViewInWindow("PrincipalView");
        this.getStage().close();
    }

    @FXML
    void onActionBtnNew(ActionEvent event) {
        if (tptGestiones.isSelected()) {
            if (new Mensaje().showConfirmation("Nueva Gestion", getStage(), "Desea crear una nueva gestion?")) {
                newGestion();
            }
        }

    }

    @FXML
    void onActionBtnSave(ActionEvent event) {
        if (tptGestiones.isSelected()) {
            saveGestion();
        }

    }

    @FXML
    void onActionBtnSearch(ActionEvent event) {

        SpecificSearchViewController controller = (SpecificSearchViewController) FlowController.getInstance()
                .getController("SpecificSearch");

        FlowController.getInstance().goViewInWindowModal("SpecificSearch", getStage(), false);

        Object result = controller.getResult();

        GestionsDto gestion = (GestionsDto) result;
        if (gestion != null) {
            txfIDGestion.setText(gestion.getId().toString());
            chargeGestion(Long.valueOf(txfIDGestion.getText()));
        }

    }

    @FXML
    void onKeyPressedTxfIDGestion(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !txfIDGestion.getText().isBlank()) {
            chargeGestion(Long.valueOf(txfIDGestion.getText()));
        }

    }

    @FXML
    void onKeyPressedTxfIdSeguimiento(KeyEvent event) {

    }

    @FXML
    void onSelecitonChangedTptSeguimiento(Event event) {

    }

    @FXML
    void onSelectionChangedTptAprobaciones(Event event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        format();
        initEntities();
        newGestion();

    }

}
