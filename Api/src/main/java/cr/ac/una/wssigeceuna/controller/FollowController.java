package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.FollowsDto;
import cr.ac.una.wssigeceuna.service.FollowsService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/follow")
public class FollowController {

    @EJB
    FollowsService followsService;

    @POST
    @Path("/createFollow")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createFollow(FollowsDto follow) {
        try {
            Respuesta res = followsService.createFollow(follow);
            if (!res.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok((FollowsDto) res.getResultado("Seguimiento")).build();
        } catch (Exception ex) {
            Logger.getLogger(GestionController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al guardar el seguimiento.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar el seguimiento.").build();
        }
    }

}
