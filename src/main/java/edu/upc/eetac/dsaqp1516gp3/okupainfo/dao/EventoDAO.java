package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface EventoDAO
{
    Event createEvent(String creatorid, String title, String tipo, String descripcion, float valoracion, String localiaztion, String longitud, String latitud)throws SQLException;
    Event updateProfile(String id, String title, String tipo, String description)throws SQLException;
    Event updateValoracion(String id, float valoracion)throws SQLException;
    Event updateLocation(String id, String localization, String longitud, String latitud)throws SQLException;
    Event getEventById(String id)throws SQLException;//de la tabla eventos
    Event getEventByCreatorId(String creatorid)throws SQLException;
    Event getEventsByUserId(String userid) throws SQLException;// Pasamos la Id del ususario y en la tabla users_events nos devuelve los eventos
    Event getAllEvents()throws SQLException;
    boolean deleteCasal(String id) throws SQLException;
}