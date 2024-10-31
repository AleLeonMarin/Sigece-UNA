package cr.ac.una.wssigeceuna.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Areas;
import cr.ac.una.wssigeceuna.model.Roles;
import cr.ac.una.wssigeceuna.model.RolesDto;
import cr.ac.una.wssigeceuna.model.Users;
import cr.ac.una.wssigeceuna.model.UsersDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import java.util.stream.Collectors;

@Stateless
@LocalBean
public class UsersService {

    private static final Logger LOG = Logger.getLogger(UsersService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    @EJB
    MailsService mails;

    public Respuesta validateUser(String username, String password) {

        try {
            Query query = em.createNamedQuery("Users.findByUserPass", Users.class);
            query.setParameter("user", username);
            query.setParameter("password", password);

            List<Users> users = query.getResultList();

            if (users.isEmpty()) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontraron usuarios con las credenciales ingresadas", "validateUser No Result List");
            }

            Users user = users.get(0);
            UsersDto userDto = new UsersDto(user);
            for (Roles role : user.getRoles()) {
                userDto.getRoles().add(new RolesDto(role));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Usuario", userDto);

        } catch (NoResultException ex) {
            LOG.log(Level.INFO, "No se encontró ningún usuario con las credenciales proporcionadas.");
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existe un usuario con las credenciales ingresadas.", "validateUser NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error inesperado al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error inesperado al consultar el usuario.", "validateUser " + ex.getMessage());
        }
    }

    public Respuesta saveUser(UsersDto usersDto) {
        try {
            Users user;

            if (usersDto.getId() != null && usersDto.getId() > 0) {
                user = em.find(Users.class, usersDto.getId());
                if (user == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el usuario a modificar.", "saveUser NoResultException");
                }

                user.update(usersDto);

                // Eliminar roles
                for (RolesDto rolDto : usersDto.getEliminatedRoles()) {
                    Roles rol = em.find(Roles.class, rolDto.getId());
                    if (rol != null) {
                        rol.getUsers().remove(user);
                        user.getRoles().remove(rol);
                        em.merge(rol);
                        em.merge(user);
                    }
                    LOG.log(Level.INFO, "Rol eliminado: {0}", rolDto.getId());
                }

                // Agregar roles
                if (!usersDto.getRoles().isEmpty()) {
                    for (RolesDto rolDto : usersDto.getRoles()) {
                        Roles rol = em.find(Roles.class, rolDto.getId());
                        if (rol != null) {
                            rol.getUsers().add(user);
                            user.getRoles().add(rol);
                            LOG.log(Level.INFO, "Rol agregado: {0}", rol.getId());
                        }
                    }
                }

                // Asignar área
                if (usersDto.getAreas() != null) {
                    Areas area = em.find(Areas.class, usersDto.getAreas().getId());
                    if (area != null) {
                        user.setArea(area);
                    } else {
                        LOG.log(Level.WARNING, "Área no encontrada con ID: {0}", usersDto.getAreas().getId());
                    }
                }

                user = em.merge(user); // Asegurarse de guardar todos los cambios
            } else {
                user = new Users(usersDto);
                em.persist(user);

                // Asignar rol de administrador al primer usuario registrado
                if (em.createQuery("SELECT COUNT(u) FROM Users u", Long.class).getSingleResult() == 1) {
                    Roles administradorRole = em.find(Roles.class, 1L); // assume role ID 1 is administrator
                    if (administradorRole != null) {
                        administradorRole.getUsers().add(user);
                        user.getRoles().add(administradorRole);
                        em.merge(administradorRole);
                        LOG.log(Level.INFO, "Rol administrador asignado al primer usuario registrado.");
                    }
                }

                UsersDto usuario = new UsersDto();
                usuario.setUser(usersDto.getUser());
                usuario.setEmail(usersDto.getEmail());
                mails.activationMail(usuario);
            }

            // Aseguramos que los cambios se confirmen en la base de datos
            em.flush();

            LOG.log(Level.INFO, "Usuario guardado correctamente: {0}", user);

            return new Respuesta(true, CodigoRespuesta.CORRECTO,
                    "El usuario se guardó correctamente", "", "Usuario",
                    new UsersDto(user));
        } catch (NoResultException ex) {
            LOG.log(Level.SEVERE, "Usuario no encontrado.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontró el usuario a modificar.",
                    "saveUser " + ex.getMessage());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al guardar el usuario.",
                    "saveUser " + ex.getMessage());
        }
    }

    public Respuesta deleteUser(Long id) {
        try {
            Users users;
            if (id != null && id > 0) {
                users = em.find(Users.class, id);
                if (users == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontro el usuario a eliminar.", "deleteUser NoResultException");
                }

                em.remove(users);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el usuario a eliminar.",
                        "deleteUser NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al eliminar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al eliminar el usuario.",
                    "deleteUser " + ex.getMessage());
        }
    }

    public Respuesta getUser(Long id) {
        try {
            Query query = em.createNamedQuery("Users.findById", Users.class);
            query.setParameter("id", id);
            Users user = (Users) query.getSingleResult();
            UsersDto userDto = new UsersDto(user);

            for (Roles rol : user.getRoles()) {
                userDto.getRoles().add(new RolesDto(rol));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Usuario", userDto);

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existe un usuario con el código ingresado.", "getUsuario NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al consultar el usuario.",
                    "getUsuario NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al consultar el usuairo.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al consultar el usuario.",
                    "getUsuario " + ex.getMessage());
        }
    }

    public Respuesta getAllUsers() {
        try {
            Query query = em.createNamedQuery("Users.findAll", Users.class);
            List<Users> users = query.getResultList();
            List<UsersDto> usersDto = new ArrayList<>();

            for (Users user : users) {
                UsersDto userDto = new UsersDto(user);
                usersDto.add(userDto);
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Usuarios", usersDto);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al consultar los usuarios.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al consultar los usuarios.",
                    "getAllUsers " + ex.getMessage());
        }
    }

    public Respuesta activateUser(String user) {
        try {
            Query query = em.createQuery("SELECT u FROM Users u WHERE u.user = :user", Users.class);
            query.setParameter("user", user);

            Users users = (Users) query.getSingleResult();
            if (user == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el usuario con el nombre proporcionado.", "activarUsuario NoResultException");
            }

            users.setState("A");
            em.merge(users);
            em.flush();

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Usuario activado exitosamente.", "", "Usuario",
                    new UsersDto(users));
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontro el usuario con el nombre proporcionado.", "activarUsuario NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al activar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al activar el usuario.",
                    "activarUsuario " + ex.getMessage());
        }
    }

    public Respuesta actualizarEstadoUsuario(UsersDto usuarioDto) {
        try {
            Users usuario = em.find(Users.class, usuarioDto.getId());
            if (usuario == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el usuario.",
                        "actualizarEstadoUsuario NoResultException");
            }

            usuario.setStatus(usuarioDto.getStatus());
            em.merge(usuario);
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Usuario", new UsersDto(usuario));
        } catch (Exception ex) {
            Logger.getLogger(UsersService.class.getName()).log(Level.SEVERE,
                    "Error actualizando el estado del usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error actualizando el estado del usuario.",
                    "actualizarEstadoUsuario " + ex.getMessage());
        }
    }

    public Respuesta filterUsers(String name, String idCard, String lastNames) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT u FROM Users u WHERE 1=1");

            if (name != null && !name.trim().isEmpty()) {
                jpql.append(" AND u.name LIKE :name");
            }
            if (idCard != null && !idCard.trim().isEmpty()) {
                jpql.append(" AND u.idCard LIKE :idCard");
            }
            if (lastNames != null && !lastNames.trim().isEmpty()) {
                jpql.append(" AND u.lastNames LIKE :lastNames");
            }

            Query query = em.createQuery(jpql.toString(), Users.class);

            if (name != null && !name.trim().isEmpty()) {
                query.setParameter("name", "%" + name + "%");
            }
            if (idCard != null && !idCard.trim().isEmpty()) {
                query.setParameter("idCard", "%" + idCard + "%");
            }
            if (lastNames != null && !lastNames.trim().isEmpty()) {
                query.setParameter("lastNames", "%" + lastNames + "%");
            }

            List<Users> users = query.getResultList();
            List<UsersDto> usersDto = users.stream().map(UsersDto::new).collect(Collectors.toList());

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "Usuarios", usersDto);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error filtrando los usuarios.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error filtrando los usuarios.",
                    "filterUsers " + ex.getMessage());
        }
    }

    public Respuesta getUserByMail(String mail) {
        try {
            Query query = em.createNamedQuery("Users.findByMail", Users.class);
            query.setParameter("mail", mail);
            Users usuarios = (Users) query.getSingleResult();
            UsersDto usuarioDto = new UsersDto(usuarios);

            // Generar una contraseña aleatoria
            String nuevaContrasena = generarContrasenaAleatoria();
            usuarios.setPassword(nuevaContrasena); // Asigna la nueva contraseña al usuario

            em.merge(usuarios); // Guarda los cambios en la base de datos

            // Enviar un correo con la nueva contraseña
            UsersDto u = new UsersDto();
            u.setUser(usuarioDto.getUser());
            u.setEmail(usuarioDto.getEmail());
            u.setPassword(nuevaContrasena);
            mails.sendPasswordResetMail(u);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Usuario", usuarioDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existe un usuario con el correo ingresado.", "getUserByMail NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el usuario.",
                    "getUserByMail NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el usuario.",
                    "getUserByMail " + ex.getMessage());
        }
    }

    public Respuesta getByPass(String password) {
        try {
            Query query = em.createNamedQuery("Users.findByPass", Users.class);
            query.setParameter("password", password);

            Users user = (Users) query.getSingleResult();
            UsersDto userDto = new UsersDto(user);
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Usuarios", userDto);
        } catch (NoResultException ex) {
            LOG.log(Level.WARNING, "No se encontró un usuario con la contraseña especificada.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontró un usuario con la contraseña especificada.", "getByPass NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Se encontró más de un usuario con la misma contraseña.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Se encontró más de un usuario con la misma contraseña.", "getByPass NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar los usuarios.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar los usuarios.", "getByPass " + ex.getMessage());
        }
    }

    // Método para generar una contraseña aleatoria
    private String generarContrasenaAleatoria() {
        int longitud = 12; // Longitud de la contraseña
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(longitud);

        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }

        return sb.toString();
    }

    public Respuesta updatePasswordByEmail(String email, String newPassword) {
        try {
            // Buscar el usuario por el correo
            Query query = em.createNamedQuery("Users.findByMail", Users.class);
            query.setParameter("mail", email);
            Users user = (Users) query.getSingleResult();

            // Actualizar la contraseña del usuario con la nueva proporcionada
            user.setPassword(newPassword);
            em.merge(user); // Guarda los cambios en la base de datos

            // Convertir el usuario a DTO para la respuesta
            UsersDto userDto = new UsersDto(user);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Contraseña actualizada correctamente.", "",
                    "Usuarios", userDto);
        } catch (NoResultException ex) {
            LOG.log(Level.WARNING, "No se encontró un usuario con el correo especificado.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontró un usuario con el correo especificado.",
                    "updatePasswordByEmail NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al actualizar la contraseña.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error actualizando la contraseña.",
                    "updatePasswordByEmail " + ex.getMessage());
        }
    }

}
