package cr.ac.una.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cr.ac.una.admin.model.AreasDto;
import cr.ac.una.admin.util.Request;
import cr.ac.una.admin.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

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

    public Respuesta getArea(Long id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("areas/get", "/{id}", params);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            AreasDto area = (AreasDto) request.readEntity(AreasDto.class);
            return new Respuesta(true, "", "", "Area", area);
        } catch (Exception ex) {
            return new Respuesta(false, "Error obteniendo el area.", "getArea " + ex.getMessage());
        }
    }

    public Respuesta deleteArea(Long id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("areas/delete/", "/{id}", params);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            return new Respuesta(false, "Error eliminando el area.", "deleteArea " + ex.getMessage());
        }
    }

}
