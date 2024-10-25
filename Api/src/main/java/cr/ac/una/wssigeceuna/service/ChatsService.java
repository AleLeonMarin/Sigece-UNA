package cr.ac.una.wssigeceuna.service;


import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Chats;
import cr.ac.una.wssigeceuna.model.ChatsDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;

@Stateless
@LocalBean
public class ChatsService {

    private static final Logger LOG = Logger.getLogger(ChatsService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta getChat(Long id) {
        try {
            Query qryChat = em.createNamedQuery("Chats.findById", Chats.class);
            qryChat.setParameter("chtId", id);

            Chats chat = (Chats) qryChat.getSingleResult();
            em.refresh(chat);
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Chat", new ChatsDto(chat));

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No existe un chat con el código ingresado.", "getChat NoResultException");
        } catch (NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al consultar el chat.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al consultar el chat.",
                    "getChat NonUniqueResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al consultar el chat.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al consultar el chat.",
                    "getChat " + ex.getMessage());
        }
    }

    public Respuesta getChats() {
        try {
            Query qryChats = em.createNamedQuery("Chats.findAll", Chats.class);

            List<Chats> chats = qryChats.getResultList();

            for (Chats chat : chats) {
                em.refresh(chat);
            }

            List<ChatsDto> chatsDto = new ArrayList<>();
            for (Chats chat : chats) {
                chatsDto.add(new ChatsDto(chat));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Chats", chatsDto);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al consultar los chats.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al consultar los chats.",
                    "getChats " + ex.getMessage());
        }
    }

    public Respuesta saveChat(ChatsDto chatDto) {
        try {
            Chats chat;
            if (chatDto.getId() != null && chatDto.getId() > 0) {

                chat = em.find(Chats.class, chatDto.getId());
                if (chat == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el chat a modificar.", "saveChat NoResultException");
                }
                chat.update(chatDto);
                chat = em.merge(chat);
            } else {

                chat = new Chats(chatDto);
                em.persist(chat);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Chat", new ChatsDto(chat));
        } catch (Exception ex) {
            Logger.getLogger(ChatsService.class.getName()).log(Level.SEVERE, "Ocurrió un error al guardar el chat.",
                    ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el chat.",
                    "saveChat " + ex.getMessage());
        }
    }

    public Respuesta deleteChat(Long id) {
        try {
            Chats chat = em.find(Chats.class, id);
            if (chat == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el chat a eliminar.",
                        "deleteChat NoResultException");
            }
            em.remove(chat);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Chat eliminado correctamente.", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar el chat.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el chat.",
                    "deleteChat " + ex.getMessage());
        }
    }

    public Respuesta getChatsByUsuario(Long id) {
        try {
            
            

            Query qryChat = em.createQuery(
                    "SELECT c FROM Chats c WHERE c.emisor.id = :id OR c.receptor.id = :id",
                    Chats.class);
            qryChat.setParameter("id", id);

            List<Chats> chats = qryChat.getResultList();

            List<ChatsDto> chatsDto = new ArrayList<>();
            for (Chats chat : chats) {
                chatsDto.add(new ChatsDto(chat));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Chats", chatsDto);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrio un error al consultar los chats del usuario.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrio un error al consultar los chats del usuario.", "getChatsByUsuario " + ex.getMessage());
        }
    }

    public Respuesta getChatsEntreUsuarios(Long emisor, Long receptor) {
        try {

            Query qryChats = em.createQuery(
                    "SELECT c FROM Chats c WHERE (c.emisor.id = :emisor AND c.receptor.id = :receptor) "
                            + "OR (c.emisor.id = :receptor AND c.receptor.id = :emisor)",
                    Chats.class);
            qryChats.setParameter("emisor", emisor);
            qryChats.setParameter("receptor", receptor);

        List<Chats> chats = qryChats.getResultList();

        List<ChatsDto> chatsDto = new ArrayList<>();
        for (Chats chat : chats) {
            chatsDto.add(new ChatsDto(chat));
        }

        return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Chats", chatsDto);

    } catch (Exception ex) {
        LOG.log(Level.SEVERE, "Ocurrio un error al consultar los chats entre usuarios.", ex);
        return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                "Ocurrio un error al consultar los chats entre usuarios.", "getChatsEntreUsuarios " + ex.getMessage());
    }
}




}
