package cr.ac.una.wssigeceuna.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Activities;
import cr.ac.una.wssigeceuna.model.ActivitiesDto;
import cr.ac.una.wssigeceuna.model.Areas;
import cr.ac.una.wssigeceuna.model.AreasDto;
import cr.ac.una.wssigeceuna.model.Subactivities;
import cr.ac.una.wssigeceuna.model.SubactivitiesDto;
import cr.ac.una.wssigeceuna.model.Users;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
@LocalBean
public class AreasService {

    private static final Logger LOG = Logger.getLogger(AreasService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta createArea(AreasDto area) {
        try {
            Areas areas;

            // Si el área tiene ID, intenta buscarla en la base de datos
            if (area.getId() != null && area.getId() > 0) {
                areas = em.find(Areas.class, area.getId());
                if (areas == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el área a modificar", "createArea NoResultException");
                }
                areas.update(area);
            } else {
                // Si no tiene ID, crea una nueva área
                areas = new Areas(area);
                em.persist(areas); // Persistimos el área primero para que tenga ID generado
            }

            // Proceso de las actividades
            for (ActivitiesDto activityDto : area.getActivities()) {
                Activities activity;

                // Si la actividad tiene ID, busca la actividad en la base de datos
                if (activityDto.getId() != null && activityDto.getId() > 0) {
                    activity = em.find(Activities.class, activityDto.getId());
                    if (activity == null) {
                        activity = new Activities(activityDto);
                    } else {
                        activity.update(activityDto);
                    }
                } else {
                    // Si no tiene ID, crea una nueva actividad
                    activity = new Activities(activityDto);
                }

                areas.getActivity().add(activity); // Relaciona la actividad con el área

                // Proceso de las subactividades
                for (SubactivitiesDto subactivityDto : activityDto.getSubactivities()) {
                    Subactivities subactivity;

                    // Si la subactividad tiene ID, búscala en la base de datos
                    if (subactivityDto.getId() != null && subactivityDto.getId() > 0) {
                        subactivity = em.find(Subactivities.class, subactivityDto.getId());
                        if (subactivity == null) {
                            subactivity = new Subactivities(subactivityDto);
                        } else {
                            subactivity.update(subactivityDto);
                        }
                    } else {
                        // Si no tiene ID, crea una nueva subactividad
                        subactivity = new Subactivities(subactivityDto);
                    }

                    activity.getSubactivities().add(subactivity); // Relaciona la subactividad con la actividad
                }
            }

            // Finalmente, mergeamos el área para guardar los cambios
            areas = em.merge(areas);
            em.flush();

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "",
                    "", "Area", new AreasDto(areas));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al guardar el área: " + ex.getMessage(), ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al guardar el área.", "createArea " + ex.getMessage());
        }
    }

    public Respuesta deleteArea(Long id) {
        try {
            Areas area = em.find(Areas.class, id);
            if (area == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el área a eliminar", "deleteArea NoResultException");
            }
            if (area != null) {
                // Desvincular los usuarios del área sin eliminarlos
                for (Users user : area.getUsers()) {
                    user.setArea(null); // Romper la relación con el área
                    em.merge(user); // Actualizar el usuario en la base de datos
                }
            }
            em.remove(area);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Área eliminada correctamente", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al eliminar el área: " + ex.getMessage(), ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al eliminar el área.", "deleteArea " + ex.getMessage());
        }
    }

    public Respuesta getAreas() {
        try {

            Query query = em.createNamedQuery("Areas.findAll", Areas.class);
            List<Areas> areas = query.getResultList();
            List<AreasDto> areasDto = new ArrayList<>();
            for (Areas area : areas) {
                AreasDto areaDto = new AreasDto(area);
                areasDto.add(areaDto);
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "",
                    "Areas", areasDto);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al obtener las áreas: " + ex.getMessage(), ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al obtener las áreas.", "getAreas " + ex.getMessage());
        }
    }

    public Respuesta getArea(Long id) {
        try {
            Query query = em.createNamedQuery("Areas.findById", Areas.class);
            query.setParameter("id", id);
            Areas area = (Areas) query.getSingleResult();
            AreasDto areaDto = new AreasDto(area);
            for (Activities activity : area.getActivity()) {
                ActivitiesDto activityDto = new ActivitiesDto(activity);
                for (Subactivities subactivity : activity.getSubactivities()) {
                    activityDto.getSubactivities().add(new SubactivitiesDto(subactivity));
                }
                areaDto.getActivities().add(activityDto);
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "",
                    "Area", areaDto);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al obtener el área: " + ex.getMessage(), ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al obtener el área.", "getArea " + ex.getMessage());
        }
    }

}
