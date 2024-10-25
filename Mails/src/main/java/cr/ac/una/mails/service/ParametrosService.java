package cr.ac.una.mails.service;

import cr.ac.una.mails.model.ParamethersDto;
import cr.ac.una.mails.util.Request;
import cr.ac.una.mails.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para la gestión de parámetros
 */
public class ParametrosService {

    public Respuesta getParametros() {
        try {
            Request request = new Request("paramethers/get");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            ParamethersDto parametros = (ParamethersDto) request.readEntity(ParamethersDto.class);
            return new Respuesta(true, "", "", "Parametros", parametros);

        } catch (Exception ex) {
            Logger.getLogger(ParametrosService.class.getName()).log(Level.SEVERE, "Error obteniendo los parámetros.", ex);
            return new Respuesta(false, "Error obteniendo los parámetros.", "getParametros " + ex.getMessage());
        }
    }

    public Respuesta guardarParametros(ParamethersDto parametrosDTO) {
        try {
            Request request = new Request("paramethers/save");
            request.post(parametrosDTO);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            ParamethersDto parametrosGuardados = (ParamethersDto) request.readEntity(ParamethersDto.class);
            return new Respuesta(true, "", "", "Parametros", parametrosGuardados);

        } catch (Exception ex) {
            Logger.getLogger(ParametrosService.class.getName()).log(Level.SEVERE, "Error guardando los parámetros.", ex);
            return new Respuesta(false, "Error guardando los parámetros.", "guardarParametros " + ex.getMessage());
        }
    }
}
