package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;

import java.sql.SQLException;

public interface EventoDAO
{
    Event createEvent(String creatorid, String title, String tipo, String descripcion, float valoracion, String localiaztion, String longitud, String latitud)throws SQLException;
    Event UpdateProfile(String id, String title, String tipo, String description)throws SQLException;
    Event UpdateValoracion(String id, float valoracion)throws SQLException;
    Event UpdateLocation(String localization, String longitud, String latitud)throws SQLException;
    Event GetEventById(String id)throws SQLException;
    Event GetEventByCreatorId(String id, String creatorid)throws SQLException;
    Event GetEventByParticipante(String id)throws SQLException; //pasamos la id del usuario y nos devuelve los eventos en los que es participe
    Event GetParticipanteByEvent(String id)throws SQLException;//Pasamos la id del evento y nos devuelve los usuarios participes
    Event GetAllEvents()throws SQLException;
    boolean deleteCasal(String id) throws SQLException;
}