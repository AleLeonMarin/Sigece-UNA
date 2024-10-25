package cr.ac.una.security.service;

import cr.ac.una.security.model.SystemsDto;
import cr.ac.una.security.util.Request;
import cr.ac.una.security.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemsService {

    private static final Logger LOG = Logger.getLogger(SystemsService.class.getName());

    public Respuesta guardarSystem(SystemsDto systemsDto) {
        try {
            Request request = new Request("SystemsController/saveSystem");
            request.post(systemsDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            SystemsDto systemGuardado = (SystemsDto) request.readEntity(SystemsDto.class);
            return new Respuesta(true, "", "", "Sistema", systemGuardado);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error guardando el sistema.", ex);
            return new Respuesta(false, "Error guardando el sistema.", "guardarSystem " + ex.getMessage());
        }
    }

    public Respuesta obtenerSystems() {
        try {
            Request request = new Request("SystemsController/getSystems");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<SystemsDto> systems = (List<SystemsDto>) request.readEntity(new GenericType<List<SystemsDto>>() {});
            return new Respuesta(true, "", "", "Sistemas", systems);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo los sistemas.", ex);
            return new Respuesta(false, "Error obteniendo los sistemas.", "obtenerSystems " + ex.getMessage());
        }
    }

    public Respuesta obtenerSystem(Long id) {
        try {
            Request request = new Request("SystemsController/getSystem/" + id);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            SystemsDto system = (SystemsDto) request.readEntity(SystemsDto.class);
            return new Respuesta(true, "", "", "Sistema", system);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo el sistema.", ex);
            return new Respuesta(false, "Error obteniendo el sistema.", "obtenerSystem " + ex.getMessage());
        }
    }

    public Respuesta eliminarSystem(Long id) {
        try {
            Request request = new Request("SystemsController/deleteSystem/" + id);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "Sistema eliminado correctamente.", "");

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error eliminando el sistema.", ex);
            return new Respuesta(false, "Error eliminando el sistema.", "eliminarSystem " + ex.getMessage());
        }
    }
}
