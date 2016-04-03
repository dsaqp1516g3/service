package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;

import java.sql.SQLException;

public interface EventoDAO
{
    Event createEvent(String creatorid, String title, String tipo, String descripcion, float valoracion, String localiaztion, String longitud, String latitud)throws SQLException;
    Event UpdateProfile(String id, String title, String tipo, String description)throws SQLException;
    Event UpdateValoracion(String id, float valoracion)throws SQLException;
    Event UpdateLocation(String localization, String longitud, String latitud)throws SQLException;
    Event GetEventById(String id)throws SQLException;//de la tabla eventos
    Event GetEventByCreatorId(String id, String creatorid)throws SQLException;
    Event getEventsByUserId(String userid) throws SQLException;// Pasamos la Id del ususario y en la tabla users_events nos devuelve los eventos
    Event GetAllEvents()throws SQLException;
    boolean deleteCasal(String id) throws SQLException;
}