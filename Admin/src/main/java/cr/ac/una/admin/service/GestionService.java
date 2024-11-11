package cr.ac.una.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cr.ac.una.admin.model.ApprovalsDto;
import cr.ac.una.admin.model.FollowsDto;
import cr.ac.una.admin.model.GestionsDto;
import cr.ac.una.admin.util.Request;
import cr.ac.una.admin.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

public class GestionService {

    public Respuesta createGestion(GestionsDto gestion) {

        try {
            Request request = new Request("gestion/createGestion");
            request.post(gestion);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            GestionsDto gestionGuardada = (GestionsDto) request.readEntity(GestionsDto.class);
            return new Respuesta(true, "", "", "Gestion", gestionGuardada);
        } catch (Exception e) {
            return new Respuesta(false, e.getMessage(), "");
        }
    }

    public Respuesta getGestiones() {

        try {
            Request request = new Request("gestion/getGestions");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<GestionsDto> gestiones = (List<GestionsDto>) request.readEntity(new GenericType<List<GestionsDto>>() {
            });
            return new Respuesta(true, "", "", "Gestiones", gestiones);
        } catch (Exception ex) {
            return new Respuesta(false, "Error obteniendo las gestiones.", "getGestiones " + ex.getMessage());
        }
    }

    public Respuesta getGestion(Long id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("gestion/getGestion", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            GestionsDto gestion = (GestionsDto) request.readEntity(GestionsDto.class);
            return new Respuesta(true, "", "", "Gestion", gestion);
        } catch (Exception ex) {
            return new Respuesta(false, "Error obteniendo la gestion.", "getGestion " + ex.getMessage());
        }
    }

    public Respuesta deleteGestion(Long id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("gestion/deleteGestion", "/{id}", params);
            request.delete();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            return new Respuesta(true, "", "Gestion eliminada correctamente", "Gestion", null);
        } catch (Exception ex) {
            return new Respuesta(false, "Error eliminando la gestion.", "deleteGestion " + ex.getMessage());
        }
    }

    public Respuesta createFollow(FollowsDto follow) {
        try {
            Request request = new Request("follow/createFollow");
            request.post(follow);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            FollowsDto followGuardado = (FollowsDto) request.readEntity(FollowsDto.class);
            return new Respuesta(true, "", "", "Follow", followGuardado);
        } catch (Exception e) {
            return new Respuesta(false, e.getMessage(), "");
        }
    }

    public Respuesta createApproval(ApprovalsDto approval) {
        try {
            Request request = new Request("approvals/createApprovals");
            request.post(approval);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            ApprovalsDto approvalGuardado = (ApprovalsDto) request.readEntity(ApprovalsDto.class);
            return new Respuesta(true, "", "", "Approval", approvalGuardado);
        } catch (Exception e) {
            return new Respuesta(false, e.getMessage(), "");
        }
    }

    public Respuesta getApprovals() {
        try {
            Request request = new Request("approvals/getApprovals");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<ApprovalsDto> approvals = (List<ApprovalsDto>) request
                    .readEntity(new GenericType<List<ApprovalsDto>>() {
                    });
            return new Respuesta(true, "", "", "Approvals", approvals);
        } catch (Exception ex) {
            return new Respuesta(false, "Error obteniendo las aprobaciones.", "getApprovals " + ex.getMessage());
        }
    }

    public Respuesta getApproval(Long id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("approvals/getApproval", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            ApprovalsDto approval = (ApprovalsDto) request.readEntity(ApprovalsDto.class);
            return new Respuesta(true, "", "", "Approval", approval);
        } catch (Exception ex) {
            return new Respuesta(false, "Error obteniendo la aprobación.", "getApproval " + ex.getMessage());
        }
    }

}
