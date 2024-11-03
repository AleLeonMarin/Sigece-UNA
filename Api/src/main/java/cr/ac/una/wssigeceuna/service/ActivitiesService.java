package cr.ac.una.wssigeceuna.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Activities;
import cr.ac.una.wssigeceuna.model.ActivitiesDto;
import cr.ac.una.wssigeceuna.model.Areas;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
@LocalBean
public class ActivitiesService {

    private static final Logger LOG = Logger.getLogger(ActivitiesService.class.getName());
    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta saveActivity(ActivitiesDto activity) {

        try {
            Activities activities;

            if (activity.getArea() == null || activity.getArea().getId() == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE,
                        "Debe asociar un area válida la actividad.", "saveActivity NullPointerException");
            }

            if (activity.getId() != null && activity.getId() > 0) {
                activities = em.find(Activities.class, activity.getId());
                if (activities == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la actividad a modificar.", "saveActivity NoResultException");
                }

                activities.update(activity);
                activities = em.merge(activities);
            } else {
                activities = new Activities(activity);
                em.persist(activities);
            }

            Areas areaEntity = em.find(Areas.class, activity.getArea().getId());
            if (areaEntity == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el area asociado a la actividad.", "saveActivity NoResultException");
            }

            if (activity.getArea() != null) {
                activities.setArea(areaEntity);
                em.merge(areaEntity);
            }

            em.flush();

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Actividad", new ActivitiesDto(activities));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar la actividad.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar la actividad..",
                    "saveActivity " + ex.getMessage());
        }

    }

    public Respuesta deleteActivity(Long id) {
        try {
            Activities activity;
            if (id != null && id > 0) {
                activity = em.find(Activities.class, id);
                if (activity == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la actividad a eliminar.", "deleteActivity NoResultException");
                }

                Areas area = em.find(Areas.class, activity.getArea().getId());
                area.getActivity().remove(activity);
                em.merge(area);
                em.remove(activity);
                em.flush();
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE,
                        "Debe indicar el id de la actividad a eliminar.",
                        "deleteActivity NoResultException");
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Actividad", new ActivitiesDto(activity));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar la actividad.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar la actividad.",
                    "deleteActivity " + ex.getMessage());
        }
    }

    public Respuesta getActivity(Long id) {
        try {
            Query query = em.createNamedQuery("Activities.findByID", Activities.class);
            query.setParameter("id", id);
            Activities activity = (Activities) query.getSingleResult();
            ActivitiesDto activityDto = new ActivitiesDto(activity);
            if (activity == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró la actividad.", "getActivity NoResultException");
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Actividad", activityDto);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al obtener la actividad.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al obtener la actividad.",
                    "getActivity " + ex.getMessage());
        }
    }

    public Respuesta getActivities() {
        try {
            Query query = em.createNamedQuery("Activities.findAll", Activities.class);
            List<Activities> activities = query.getResultList();
            List<ActivitiesDto> activitiesDto = new ArrayList<>();

            activities.forEach((activity) -> {
                activitiesDto.add(new ActivitiesDto(activity));
            });
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Actividades", activitiesDto);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al obtener las actividades.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al obtener las actividades.",
                    "getActivities " + ex.getMessage());
        }
    }

}
