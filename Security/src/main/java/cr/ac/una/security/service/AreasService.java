package cr.ac.una.security.service;

import java.util.List;

import cr.ac.una.security.model.AreasDto;
import cr.ac.una.security.util.Request;
import cr.ac.una.security.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

public class AreasService {

    public Respuesta getAreas() {

        try {
            Request request = new Request("areas/getAreas");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<AreasDto> areas = (List<AreasDto>) request.readEntity(new GenericType<List<AreasDto>>() {
            });
            return new Respuesta(true, "", "", "Areas", areas);
        } catch (Exception ex) {
            return new Respuesta(false, "Error obteniendo las areas.", "getAreas " + ex.getMessage());
        }
    }

}
