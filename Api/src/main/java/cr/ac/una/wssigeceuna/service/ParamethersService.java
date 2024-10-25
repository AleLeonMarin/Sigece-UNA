package cr.ac.una.wssigeceuna.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Paramethers;
import cr.ac.una.wssigeceuna.model.ParamethersDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@LocalBean
@Stateless
public class ParamethersService {

    private static final Logger LOG = Logger.getLogger(ParamethersService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta saveParamethers(ParamethersDto paramethersDto) {
        try {

            Paramethers paramethers = em.find(Paramethers.class, paramethersDto.getId());
            if (paramethers == null) {
                paramethers = new Paramethers(paramethersDto);
                paramethers.setId(1);
                em.persist(paramethers);
            } else {
                paramethers.update(paramethersDto);
                paramethers = em.merge(paramethers);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Parametro",
                    new ParamethersDto(paramethers));

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar los parámetros.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar los parámetros.",
                    "guardarParametros " + ex.getMessage());
        }
    }

    public Respuesta getParamethers() {
        try {
            Paramethers paramethers = em.find(Paramethers.class, 1L);
            if (paramethers == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontraron parámetros.",
                        "getParametros NoResultException");
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Parametro",
                    new ParamethersDto(paramethers));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar los parámetros.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar los parámetros.",
                    "getParametros " + ex.getMessage());
        }
    }

}
