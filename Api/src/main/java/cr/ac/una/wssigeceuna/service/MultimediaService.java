package cr.ac.una.wssigeceuna.service;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Variables;
import cr.ac.una.wssigeceuna.model.VariablesDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.persistence.NoResultException;

@Stateless
@LocalBean
public class MultimediaService {

    private static final Logger LOG = Logger.getLogger(MultimediaService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    // Método para obtener la variable multimedia en formato byte[]
    public Respuesta obtenerMultimedia(Long variableId) {
        try {
            Variables variable = em.find(Variables.class, variableId);
            if (variable == null || variable.getMultimedia() == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el archivo multimedia.", "obtenerMultimedia NoResultException");
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Multimedia",
                    variable.getMultimedia());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al obtener el archivo multimedia.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al obtener el archivo multimedia.", "obtenerMultimedia " + ex.getMessage());
        }
    }
    
      public byte[] obtenerImagenDesdeDB(Long variableId) {
        try {
            Variables variable = em.find(Variables.class, variableId);

            if (variable == null || variable.getMultimedia() == null) {
                LOG.log(Level.WARNING, "No se encontró la variable multimedia con ID: {0}", variableId);
                return null;
            }

            return variable.getMultimedia();

        } catch (NoResultException e) {
            LOG.log(Level.SEVERE, "Variable multimedia no encontrada.", e);
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener la variable multimedia.", e);
            return null;
        }
    }

    // Método para guardar o actualizar una variable multimedia
    public Respuesta guardarMultimedia(VariablesDto variableDto) {
        try {
            Variables variable;
            if (variableDto.getId() != null && variableDto.getId() > 0) {
                variable = em.find(Variables.class, variableDto.getId());
                if (variable == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la variable multimedia a modificar.", "guardarMultimedia NoResultException");
                }
                variable.update(variableDto);
            } else {
                variable = new Variables(variableDto);
                em.persist(variable);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO,
                    "El archivo multimedia se guardó correctamente.", "", "Multimedia", new VariablesDto(variable));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al guardar el archivo multimedia.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al guardar el archivo multimedia.", "guardarMultimedia " + ex.getMessage());
        }
    }

    // Método para eliminar una variable multimedia
    public Respuesta eliminarMultimedia(Long variableId) {
        try {
            Variables variable = em.find(Variables.class, variableId);
            if (variable == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró la variable multimedia a eliminar.", "eliminarMultimedia NoResultException");
            }
            em.remove(variable);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO,
                    "Archivo multimedia eliminado correctamente.", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al eliminar el archivo multimedia.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al eliminar el archivo multimedia.", "eliminarMultimedia " + ex.getMessage());
        }
    }
    
    public String obtenerTipoContenido(Long variableId) {
    try {
        // Consulta en la base de datos para obtener el tipo MIME asociado al ID de variable
        Query query = em.createQuery("SELECT v.mimeType FROM Variables v WHERE v.id = :variableId");
        query.setParameter("variableId", variableId);

        String mimeType = (String) query.getSingleResult();
        if (mimeType == null || mimeType.isEmpty()) {
            return "application/octet-stream"; // Tipo MIME genérico en caso de que no se encuentre
        }
        return mimeType;
    } catch (NoResultException e) {
        System.err.println("No se encontró un tipo MIME para el ID de variable: " + variableId);
        return "application/octet-stream"; // Tipo genérico si no hay un resultado
    } catch (Exception e) {
        e.printStackTrace();
        return "application/octet-stream"; // Tipo genérico en caso de error
    }
}

    
    
}
