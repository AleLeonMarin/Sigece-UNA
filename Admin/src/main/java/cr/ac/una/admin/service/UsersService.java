package cr.ac.una.admin.service;

import cr.ac.una.admin.model.UsersDto;
import cr.ac.una.admin.util.Request;
import cr.ac.una.admin.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersService {

    private static final Logger LOG = Logger.getLogger(UsersService.class.getName());

    public Respuesta logIn(String user, String password) {
        try {

            Map<String, Object> params = new HashMap<>();
            params.put("user", user);
            params.put("password", password);
            Request request = new Request("UsersController/logIn", "/{user}/{password}", params);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            UsersDto userDto = (UsersDto) request.readEntity(UsersDto.class);
            return new Respuesta(true, "", "", "Usuario", userDto);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error iniciando sesión.", ex);
            return new Respuesta(false, "Error iniciando sesión.", "logIn " + ex.getMessage());
        }
    }

    public Respuesta guardarUsuario(UsersDto userDto) {
        try {
            Request request = new Request("UsersController/user");
            request.post(userDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            UsersDto usuarioGuardado = (UsersDto) request.readEntity(UsersDto.class);
            return new Respuesta(true, "", "", "Usuario", usuarioGuardado);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error guardando el usuario.", ex);
            return new Respuesta(false, "Error guardando el usuario.", "guardarUsuario " + ex.getMessage());
        }
    }

    public Respuesta activarUsuario(String user) {
        try {
            Request request = new Request("UsersController/activateUser/" + user);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "Usuario activado correctamente.", "");

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error activando el usuario.", ex);
            return new Respuesta(false, "Error activando el usuario.", "activarUsuario " + ex.getMessage());
        }
    }

    public Respuesta getUsers() {
        try {
            Request request = new Request("UsersController/users");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<UsersDto> users = (List<UsersDto>) request.readEntity(new GenericType<List<UsersDto>>() {
            });
            return new Respuesta(true, "", "", "Usuarios", users);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo los usuarios.", ex);
            return new Respuesta(false, "Error obteniendo los usuarios.", "getUsers " + ex.getMessage());
        }
    }

    public Respuesta getByMail(String mail) {

        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("mail", mail);

            Request request = new Request("UsersController/getByMail", "/{mail}", parametros);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            UsersDto user = (UsersDto) request.readEntity(UsersDto.class);
            return new Respuesta(true, "", "", "Usuario", user);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo el usuario por correo.", ex);
            return new Respuesta(false, "Error obteniendo el usuario por correo.", "getByMail " + ex.getMessage());

        }
    }

    public Respuesta getByPass(String password) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("password", password);

            Request request = new Request("UsersController/getByPass", "/{password}", parametros);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            UsersDto user = (UsersDto) request.readEntity(UsersDto.class);
            return new Respuesta(true, "", "", "Usuario", user);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo el usuario por contraseña.", ex);
            return new Respuesta(false, "Error obteniendo el usuario por contraseña.", "getByPass " + ex.getMessage());
        }
    }

    public Respuesta updatePassword(String email, String newPass) {
        try {
            // Crear un objeto UsersDto con el correo y la nueva contraseña
            UsersDto usuarioDto = new UsersDto();
            usuarioDto.setEmail(email);
            usuarioDto.setPassword(newPass);

            // Crear la solicitud con el endpoint
            Request request = new Request("UsersController/updatePasswordByEmail");

            // Enviar el objeto UsersDto en el cuerpo de la solicitud PUT
            request.put(usuarioDto);

            // Verificar si hubo error en la solicitud
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            // Retornar respuesta exitosa
            return new Respuesta(true, "", "", "Contraseña actualizada correctamente.", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error actualizando la contraseña.", ex);
            return new Respuesta(false, "Error actualizando la contraseña.", "updatePassword " + ex.getMessage());
        }
    }

}
