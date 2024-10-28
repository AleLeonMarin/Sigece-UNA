package cr.ac.una.wssigeceuna.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Users;
import cr.ac.una.wssigeceuna.model.UsersDto;
import cr.ac.una.wssigeceuna.service.UsersService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;

@Path("/UsersController")
@Tag(name = "UsersController", description = "Operaciones sobre los usuarios")
public class UsersController {

    @EJB
    UsersService usersService;

    @Path("/logIn/{user}/{password}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un usuario por usuario y contraseña")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno durante la obtención del usuario", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response logIn(@PathParam("user") String user, @PathParam("password") String password) {
        try {
            Respuesta res = usersService.validateUser(user, password);
            if (!res.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_NOENCONTRADO.getValue()).entity(res.getMensaje()).build();
            }

            UsersDto userDto = (UsersDto) res.getResultado("Usuario");
            return Response.ok(userDto).build();
        } catch (Exception ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al consultar el usuario.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al consultar el usuario.").build();
        }
    }

    @POST
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Crea un usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario creado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(responseCode = "400", description = "Error en los datos de entrada", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno durante la creación del usuario", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response createUser(UsersDto user) {
        try {
            Respuesta res = usersService.saveUser(user);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            UsersDto userDto = (UsersDto) res.getResultado("Usuario");
            return Response.ok(userDto).build();
        } catch (Exception ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al crear el usuario.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al crear el usuario.").build();
        }
    }

    @DELETE
    @Path("/deleteUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario eliminado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "400", description = "Error en los datos de entrada", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno durante la eliminación del usuario", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            Respuesta res = usersService.deleteUser(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al eliminar el usuario.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al eliminar el usuario.").build();
        }
    }

    @GET
    @Path("/getUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un usuario por id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno durante la obtención del usuario", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getUser(@PathParam("id") Long id) {
        try {
            Respuesta res = usersService.getUser(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            UsersDto userDto = (UsersDto) res.getResultado("Usuario");
            return Response.ok(userDto).build();
        } catch (Exception ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al consultar el usuario.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al consultar el usuario.").build();
        }
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los usuarios")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuarios encontrados", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(responseCode = "404", description = "Usuarios no encontrados", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno durante la obtención de los usuarios", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getUsers() {
        try {
            Respuesta res = usersService.getAllUsers();
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<UsersDto>>((List<UsersDto>) res.getResultado("Usuarios")) {
            }).build();
        } catch (Exception ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al consultar los usuarios.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al consultar los usuarios.").build();
        }
    }

    @GET
    @Path("/activateUser/{user}")
    @Produces(MediaType.TEXT_HTML)
    public Response activarUsuario(@PathParam("user") String user) {
        try {
            Respuesta respuesta = usersService.activateUser(user);
            if (!respuesta.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_NOENCONTRADO.getValue())
                        .entity("<html lang=\"es\">"
                                + "<head>"
                                + "<meta charset=\"UTF-8\">"
                                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                                + "<title>Usuario No Encontrado</title>"
                                + "<style>"
                                + "body, html {"
                                + "height: 100%;"
                                + "margin: 0;"
                                + "display: flex;"
                                + "align-items: center;"
                                + "justify-content: center;"
                                + "text-align: center;"
                                + "font-family: Arial, sans-serif;"
                                + "background-color: #d84646;"
                                + /* Fondo rojo */ "color: white;"
                                + /* Texto blanco */ "}"
                                + ".container {"
                                + "text-align: center;"
                                + "background-color: rgba(255, 255, 255, 0.05);"
                                + "padding: 40px;"
                                + "border-radius: 10px;"
                                + "box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);"
                                + "backdrop-filter: blur(5px);"
                                + "}"
                                + "h1 {"
                                + "font-size: 2.5rem;"
                                + "padding: 40px;"
                                + "backdrop-filter: blur(5px);"
                                + "color: white;"
                                + "}"
                                + "body::before {"
                                + "content: \"\";"
                                + "position: absolute;"
                                + "top: 0;"
                                + "left: 0;"
                                + "width: 100%;"
                                + "height: 100%;"
                                + "background-image: repeating-linear-gradient(45deg, transparent, transparent 19px, rgba(255, 255, 255, 0.3) 20px);"
                                + "pointer-events: none;"
                                + "}"
                                + "</style>"
                                + "</head>"
                                + "<body>"
                                + "<div class=\"container\">"
                                + "<h1>Usuario no encontrado</h1>"
                                + "</div>"
                                + "</body>"
                                + "</html>")
                        .build();
            }

            return Response.ok("<html lang=\"es\">"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                    + "<title>Activación de Usuario</title>"
                    + "<style>"
                    + "body, html {"
                    + "height: 100%;"
                    + "margin: 0;"
                    + "display: flex;"
                    + "align-items: center;"
                    + "justify-content: center;"
                    + "text-align: center;"
                    + "font-family: Arial, sans-serif;"
                    + "background-color: #d84646;"
                    + /* Fondo rojo */ "color: white;"
                    + /* Texto blanco */ "}"
                    + ".container {"
                    + "text-align: center;"
                    + "background-color: rgba(255, 255, 255, 0.05);"
                    + "padding: 40px;"
                    + "border-radius: 10px;"
                    + "box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);"
                    + "backdrop-filter: blur(5px);"
                    + "}"
                    + "h1 {"
                    + "font-size: 2.5rem;"
                    + "padding: 40px;"
                    + "backdrop-filter: blur(5px);"
                    + "color: white;"
                    + "}"
                    + "body::before {"
                    + "content: \"\";"
                    + "position: absolute;"
                    + "top: 0;"
                    + "left: 0;"
                    + "width: 100%;"
                    + "height: 100%;"
                    + "background-image: repeating-linear-gradient(45deg, transparent, transparent 19px, rgba(255, 255, 255, 0.3) 20px);"
                    + "pointer-events: none;"
                    + "}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class=\"container\">"
                    + "<h1>Usuario activado: " + user + "</h1>"
                    + "</div>"
                    + "</body>"
                    + "</html>").build();

        } catch (Exception e) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, "");
            String htmlError = "<html><body><h1>Error activando el usuario</h1><p>" + e.getMessage()
                    + "</p></body></html>";
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity(htmlError)
                    .build();
        }
    }

    @PUT
    @Path("/actualizarEstado")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarEstado(UsersDto usuarioDto) {
        try {
            Respuesta respuesta = usersService.actualizarEstadoUsuario(usuarioDto);
            if (!respuesta.getEstado()) {
                return Response.status(Response.Status.NOT_FOUND).entity(respuesta.getMensaje()).build();
            }
            return Response.ok(respuesta).build();
        } catch (Exception e) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE,
                    "Error actualizando el estado del usuario.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error actualizando el estado: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/getByMail/{mail}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByMail(@PathParam("mail") String mail) {
        try {
            Respuesta respuesta = usersService.getUserByMail(mail);
            if (!respuesta.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_NOENCONTRADO.getValue()).entity(respuesta.getMensaje())
                        .build();
            }
            return Response.ok((UsersDto) respuesta.getResultado("Usuario")).build();
        } catch (Exception e) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE,
                    "Error obteniendo el usuario.", e);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo el usuario " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/getByPass/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByPass(@PathParam("password") String password) {
        try {
            Respuesta respuesta = usersService.getByPass(password);
            if (!respuesta.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_NOENCONTRADO.getValue()).entity(respuesta.getMensaje())
                        .build();
            }
            return Response.ok((UsersDto) respuesta.getResultado("Usuarios")).build();
        } catch (Exception e) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE,
                    "Error obteniendo el usuario.", e);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo el usuario " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("/updatePasswordByEmail")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePasswordByEmail(UsersDto usuarioDto) {
        try {
            String email = usuarioDto.getEmail();
            String newPassword = usuarioDto.getPassword();

            Respuesta respuesta = usersService.updatePasswordByEmail(email, newPassword);
            if (!respuesta.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_NOENCONTRADO.getValue()).entity(respuesta.getMensaje())
                        .build();
            }
            return Response.ok((UsersDto) respuesta.getResultado("Usuarios")).build();
        } catch (Exception e) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE,
                    "Error actualizando la contraseña.", e);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error actualizando la contraseña " + e.getMessage())
                    .build();
        }
    }


    /*
     * @GET
     * 
     * @Path("/getUsers")
     * 
     * @Produces(MediaType.APPLICATION_JSON)
     * 
     * @Operation(description = "Obtiene usuarios por nombre, cédula y apellidos")
     * 
     * @ApiResponses({
     * 
     * @ApiResponse(responseCode = "200", description = "Usuarios encontrados",
     * content = @Content(mediaType = MediaType.APPLICATION_JSON, schema
     * = @Schema(implementation = UsersDto.class))),
     * 
     * @ApiResponse(responseCode = "404", description = "Usuarios no encontrados",
     * content = @Content(mediaType = MediaType.TEXT_PLAIN)),
     * 
     * @ApiResponse(responseCode = "500", description =
     * "Error interno durante la obtención de usuarios", content
     * = @Content(mediaType = MediaType.TEXT_PLAIN))
     * })
     * public Response getUsers(
     * 
     * @QueryParam("name") String name,
     * 
     * @QueryParam("idCard") String idCard,
     * 
     * @QueryParam("lastNames") String lastNames) {
     * try {
     * Respuesta res = usersService.filterUsers(name, idCard, lastNames);
     * if (!res.getEstado()) {
     * return
     * Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje())
     * .build();
     * }
     * 
     * return Response.ok(new GenericEntity<List<UsersDto>>((List<UsersDto>)
     * res.getResultado("Usuarios")) {
     * }).build();
     * } catch (Exception ex) {
     * Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE,
     * "Ocurrió un error al consultar los usuarios.", ex);
     * return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
     * .entity("Ocurrió un error al consultar los usuarios.").build();
     * }
     * }
     */
}
