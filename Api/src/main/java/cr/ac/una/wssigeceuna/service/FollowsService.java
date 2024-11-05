package cr.ac.una.wssigeceuna.service;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Follows;
import cr.ac.una.wssigeceuna.model.FollowsDto;
import cr.ac.una.wssigeceuna.model.Gestions;
import cr.ac.una.wssigeceuna.model.Users;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
@LocalBean
public class FollowsService {

    private static final Logger LOG = Logger.getLogger(FollowsService.class.getName());
    @PersistenceContext(unitName = "SigeceUnaWsPU")
    EntityManager em;

    public Respuesta createFollow(FollowsDto follow) {
        try {
            // Crear una nueva entidad Seguimiento a partir del DTO
            Follows seguimiento = new Follows();

            // Asignar valores de FollowsDto a Seguimiento
            seguimiento.setDate(follow.getDate() != null ? follow.getDate() : LocalDate.now()); // Usar la fecha actual
                                                                                                // si no se especifica
            seguimiento.setDescription(follow.getDescription());
            seguimiento.setState(follow.getState());

            // Asignar la gestión asociada al seguimiento
            if (follow.getGestions() != null && follow.getGestions().getId() != null) {
                Gestions gestion = em.find(Gestions.class, follow.getGestions().getId());
                if (gestion == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la gestión asociada al seguimiento",
                            "createFollow No se encontró la gestión asociada al seguimiento");
                }
                seguimiento.setGestion(gestion);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "La gestión asociada es requerida",
                        "createFollow La gestión asociada es requerida");
            }

            // Asignar el usuario que creó el seguimiento
            if (follow.getUsers() != null && follow.getUsers().getId() != null) {
                Users user = em.find(Users.class, follow.getUsers().getId());
                if (user == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el usuario asociado al seguimiento",
                            "createFollow No se encontró el usuario asociado al seguimiento");
                }
                seguimiento.setUsers(user);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "El usuario asociado es requerido",
                        "createFollow El usuario asociado es requerido");
            }

            // Asignar el archivo, si está presente en el DTO
            seguimiento.setArchive(follow.getArchive());

            // Persistir el seguimiento en la base de datos
            em.persist(seguimiento);
            em.flush();

            // Devolver respuesta exitosa
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Seguimiento creado exitosamente", "", "Seguimiento",
                    new FollowsDto(seguimiento));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al crear el seguimiento.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al crear el seguimiento.",
                    "createFollow " + ex.getMessage());
        }
    }

}
