package cr.ac.una.mails.service;

import cr.ac.una.mails.model.VariablesDto;
import cr.ac.una.mails.util.Request;
import cr.ac.una.mails.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

import cr.ac.una.mails.model.UsersDto;

import cr.ac.una.mails.util.Request;
import cr.ac.una.mails.util.Respuesta;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para la gestión de variables
 */
public class VariablesService {

    public Respuesta obtenerVariablesPorNotificacion(Long notId) {
        try {
            Request request = new Request("variables/getVariables/" + notId);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<VariablesDto> variables = (List<VariablesDto>) request.readEntity(new GenericType<List<VariablesDto>>() {});
            return new Respuesta(true, "", "", "Variables", variables);

        } catch (Exception ex) {
            Logger.getLogger(VariablesService.class.getName()).log(Level.SEVERE, "Error obteniendo las variables.", ex);
            return new Respuesta(false, "Error obteniendo las variables.", "obtenerVariablesPorNotificacion " + ex.getMessage());
        }
    }

    public Respuesta guardarVariable(VariablesDto variableDto) {
        try {
            Request request = new Request("variables/save");
            request.post(variableDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            VariablesDto variableGuardada = (VariablesDto) request.readEntity(VariablesDto.class);
            return new Respuesta(true, "", "", "Variable", variableGuardada);

        } catch (Exception ex) {
            Logger.getLogger(VariablesService.class.getName()).log(Level.SEVERE, "Error guardando la variable.", ex);
            return new Respuesta(false, "Error guardando la variable.", "guardarVariable " + ex.getMessage());
        }
    }

    public Respuesta eliminarVariable(Long varId) {
        try {
            Request request = new Request("variables/delete/" + varId);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "", "Variable", null);

        } catch (Exception ex) {
            Logger.getLogger(VariablesService.class.getName()).log(Level.SEVERE, "Error eliminando la variable.", ex);
            return new Respuesta(false, "Error eliminando la variable.", "eliminarVariable " + ex.getMessage());
        }
    }
}
