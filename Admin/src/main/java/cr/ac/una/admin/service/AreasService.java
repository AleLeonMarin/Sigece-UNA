package cr.ac.una.admin.service;

import cr.ac.una.admin.model.AreasDto;
import cr.ac.una.admin.util.Request;
import cr.ac.una.admin.util.Respuesta;

public class AreasService {

    public Respuesta createArea(AreasDto area) {

        try {

            Request request = new Request("areas/save");
            request.post(area);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            AreasDto areaGuardada = (AreasDto) request.readEntity(AreasDto.class);

            return new Respuesta(true, "", "", "Area", areaGuardada);
        } catch (Exception e) {
            return new Respuesta(false, e.getMessage(), "");
        }
    }

}
