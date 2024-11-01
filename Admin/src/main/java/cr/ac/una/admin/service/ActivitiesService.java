package cr.ac.una.admin.service;

import java.util.HashMap;
import java.util.Map;

import cr.ac.una.admin.model.ActivitiesDto;
import cr.ac.una.admin.util.Request;
import cr.ac.una.admin.util.Respuesta;

public class ActivitiesService {

    public Respuesta createActivity(ActivitiesDto activity) {
        try {
            Request request = new Request("ActivitiesController/saveActivity");
            request.post(activity);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            ActivitiesDto activitySaved = (ActivitiesDto) request.readEntity(ActivitiesDto.class);

            return new Respuesta(true, "", "", "Activity", activitySaved);
        } catch (Exception e) {
            return new Respuesta(false, e.getMessage(), "");
        }
    }

    public Respuesta deleteActivities(Long id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("ActivitiesController/deleteActivity/", "/{id}", params);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "");
        } catch (Exception e) {
            return new Respuesta(false, e.getMessage(), "");
        }
    }

    public Respuesta getActivity(Long id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("ActivitiesController/getActivity", "/{id}", params);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            ActivitiesDto activity = (ActivitiesDto) request.readEntity(ActivitiesDto.class);
            return new Respuesta(true, "", "", "Activity", activity);
        } catch (Exception e) {
            return new Respuesta(false, e.getMessage(), "");
        }
    }

}
