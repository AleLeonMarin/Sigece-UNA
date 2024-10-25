package cr.ac.una.security.service;

import cr.ac.una.security.model.UsersDto;
import cr.ac.una.security.util.Request;
import cr.ac.una.security.util.Respuesta;
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

    public Respuesta eliminarUsuario(Long id) {
        try {
            Request request = new Request("UsersController/deleteUser/" + id);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "Usuario eliminado correctamente.", "");

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error eliminando el usuario.", ex);
            return new Respuesta(false, "Error eliminando el usuario.", "eliminarUsuario " + ex.getMessage());
        }
    }

    public Respuesta obtenerUsuario(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("UsersController/getUser/", "/{id}", parametros);
            request.getToken();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            UsersDto usuario = (UsersDto) request.readEntity(UsersDto.class);
            return new Respuesta(true, "", "", "Usuario", usuario);

        } catch (Exception ex) {
            Logger.getLogger(UsersService.class.getName()).log(Level.SEVERE, "Error obteniendo el usuario [" + id + "]",
                    ex);
            return new Respuesta(false, "Error obteniendo el empleado.", "obtnerUsuario " + ex.getMessage());
        }
    }

    public Respuesta obtenerUsuarios() {
        try {
            Request request = new Request("UsersController/users");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<UsersDto> usuarios = (List<UsersDto>) request.readEntity(new GenericType<List<UsersDto>>() {
            });
            return new Respuesta(true, "", "", "Usuarios", usuarios);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo los usuarios.", ex);
            return new Respuesta(false, "Error obteniendo los usuarios.", "obtenerUsuarios " + ex.getMessage());
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

}
