package cr.ac.una.admin.service;

import java.util.HashMap;
import java.util.Map;

import cr.ac.una.admin.model.SubactivitiesDto;
import cr.ac.una.admin.util.Request;
import cr.ac.una.admin.util.Respuesta;

public class SubactivitiesService {

    public Respuesta createSubactivity(SubactivitiesDto subactivity) {
        try {
            Request request = new Request("SubactivitiesController/saveSubactivity");
            request.post(subactivity);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            SubactivitiesDto subactivitySaved = (SubactivitiesDto) request.readEntity(SubactivitiesDto.class);

            return new Respuesta(true, "", "", "Subactivity", subactivitySaved);
        } catch (Exception e) {
            return new Respuesta(false, e.getMessage(), "");
        }
    }

    public Respuesta deleteSubactivities(Long id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("SubactivitiesController/deleteSubactivity/", "/{id}", params);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "");
        } catch (Exception e) {
            return new Respuesta(false, e.getMessage(), "");
        }
    }
}
