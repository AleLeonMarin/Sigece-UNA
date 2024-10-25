package cr.ac.una.security.service;

import cr.ac.una.security.model.RolesDto;
import cr.ac.una.security.util.Request;
import cr.ac.una.security.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RolesService {

    private static final Logger LOG = Logger.getLogger(RolesService.class.getName());

    public Respuesta guardarRol(RolesDto rolesDto) {
        try {
            Request request = new Request("RolesController/saveRol");
            request.post(rolesDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            RolesDto rolGuardado = (RolesDto) request.readEntity(RolesDto.class);
            return new Respuesta(true, "", "", "Rol", rolGuardado);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error guardando el rol.", ex);
            return new Respuesta(false, "Error guardando el rol.", "guardarRol " + ex.getMessage());
        }
    }

    public Respuesta obtenerRoles() {
        try {
            Request request = new Request("RolesController/getRoles");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<RolesDto> roles = (List<RolesDto>) request.readEntity(new GenericType<List<RolesDto>>() {});
            return new Respuesta(true, "", "", "Roles", roles);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo los roles.", ex);
            return new Respuesta(false, "Error obteniendo los roles.", "obtenerRoles " + ex.getMessage());
        }
    }

    public Respuesta eliminarRol(Long id) {
        try {
            Request request = new Request("RolesController/deleteRol/" + id);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "Rol eliminado correctamente.", "");

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error eliminando el rol.", ex);
            return new Respuesta(false, "Error eliminando el rol.", "eliminarRol " + ex.getMessage());
        }
    }

}
