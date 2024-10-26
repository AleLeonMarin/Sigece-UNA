package cr.ac.una.wssigeceuna.service;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Variables;
import cr.ac.una.wssigeceuna.model.VariablesDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import java.util.stream.Collectors;

@Stateless
@LocalBean
public class VariablesService {

    private static final Logger LOG = Logger.getLogger(VariablesService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta getVarible(Long id) {
        try {
            Query qryVariable = em.createNamedQuery("Variables.findByVarId", Variables.class);
            qryVariable.setParameter("id", id);

            Variables variable = (Variables) qryVariable.getSingleResult();
            em.refresh(variable);
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Variable", new VariablesDto(variable));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existe una variable con el ID ingresado.", "obtenerVariablePorId NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar la variable.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar la variable.",
                    "getVarible " + ex.getMessage());
        }
    }

    public Respuesta saveVariable(VariablesDto variablesDto) {
        try {
            Variables variable;
            if (variablesDto.getId() != null && variablesDto.getId() > 0) {

                variable = em.find(Variables.class, variablesDto.getId());
                if (variable == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró la variable a modificar.", "saveVariable NoResultException");
                }
                variable.update(variablesDto);
                variable = em.merge(variable);
            } else {

                variable = new Variables(variablesDto);
                em.persist(variable);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Variable", new VariablesDto(variable));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar la variable.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar la variable.",
                    "saveVariable " + ex.getMessage());
        }
    }

    public Respuesta getByNotification(Long id) {
        try {
            Query qryVariables = em.createQuery("SELECT v FROM Variables v WHERE v.notifications.id = :id", Variables.class);
            qryVariables.setParameter("id", id);

            List<Variables> variables = qryVariables.getResultList();
            List<VariablesDto> variablesDtoList = variables.stream()
                    .map(VariablesDto::new)
                    .collect(Collectors.toList());
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Variables", variablesDtoList);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar las variables.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al consultar las variables.",
                    "getByNotification " + ex.getMessage());
        }
    }


    public Respuesta deleteVariable(Long id) {
        try {
            Variables variable = em.find(Variables.class, id);
            if (variable == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró la variable a eliminar.", "deleteVariable NoResultException");
            }
            em.remove(variable);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Variable eliminada correctamente.", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar la variable.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar la variable.",
                    "deleteVariable " + ex.getMessage());
        }
    }

}
