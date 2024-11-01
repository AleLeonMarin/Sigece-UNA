package cr.ac.una.wssigeceuna.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Activities;
import cr.ac.una.wssigeceuna.model.RolesDto;
import cr.ac.una.wssigeceuna.model.Subactivities;
import cr.ac.una.wssigeceuna.model.SubactivitiesDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
@LocalBean
public class SubactivitiesService {

    private static final Logger LOG = Logger.getLogger(SubactivitiesService.class.getName());
    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta saveSubactivity(SubactivitiesDto subactivity) {
        try {
            Subactivities subactivities;

            if (subactivity.getActivity() == null || subactivity.getActivity().getId() == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE,
                        "Debe asociar una actividad válida a la subactividad.", "saveSubactivity NullPointerException");
            }

            if (subactivity.getId() != null && subactivity.getId() > 0) {
                subactivities = em.find(Subactivities.class, subactivity.getId());
                if (subactivities == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la subactividad a modificar.", "saveSubactivity NoResultException");
                }

                subactivities.update(subactivity);
                subactivities = em.merge(subactivities);
            } else {
                subactivities = new Subactivities(subactivity);
                em.persist(subactivities);
            }

            Activities activityEntity = em.find(Activities.class, subactivity.getActivity().getId());
            if (activityEntity == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró la actividad asociada a la subactividad.", "saveSubactivity NoResultException");
            }

            if (subactivity.getActivity() != null) {
                subactivities.setActivity(activityEntity);
                em.merge(activityEntity);
            }
            em.flush();

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Subactividad",
                    new SubactivitiesDto(subactivities));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el rol.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el rol.",
                    "saveRol " + ex.getMessage());
        }
    }

    public Respuesta deleteSubactivity(Long id) {
        try {
            Subactivities subactivity;
            if (id != null && id > 0) {
                subactivity = em.find(Subactivities.class, id);
                if (subactivity == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la subactividad a eliminar.", "deleteSubactivity NoResultException");
                }

                Activities activity = em.find(Activities.class, subactivity.getActivity().getId());
                activity.getSubactivities().remove(subactivity);
                em.merge(activity);
                em.remove(subactivity);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_CLIENTE,
                        "Debe indicar el id de la subactividad a eliminar.",
                        "deleteSubactivity NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Subactividad", new SubactivitiesDto(subactivity));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar la subactividad.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar la subactividad.",
                    "deleteSubactivity " + ex.getMessage());
        }
    }

}
