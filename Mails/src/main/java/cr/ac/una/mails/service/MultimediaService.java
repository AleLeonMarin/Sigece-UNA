package cr.ac.una.mails.service;

import cr.ac.una.mails.util.AppContext;
import cr.ac.una.mails.util.Request;
import cr.ac.una.mails.util.Respuesta;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MultimediaService {

    private static final Logger LOG = Logger.getLogger(MultimediaService.class.getName());

    public Respuesta obtenerImagen(Long variableId) {
        try {


            String baseUrl = (String) AppContext.getInstance().get("resturl");
            String imagenUrl = baseUrl + "multimedia/imagen/" + variableId;

            Request request = new Request("multimedia/imagen/" + variableId);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }


            return new Respuesta(true, "", "", "ImagenUrl", imagenUrl);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo la imagen.", ex);
            return new Respuesta(false, "Error obteniendo la imagen.", "obtenerImagen " + ex.getMessage());
        }
    }
}
