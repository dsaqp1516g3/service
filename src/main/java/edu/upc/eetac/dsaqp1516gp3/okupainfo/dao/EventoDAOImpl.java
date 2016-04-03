package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;

import java.sql.SQLException;

public class EventoDAOImpl implements EventoDAO
{
    @Override
    public Event createEvent(String creatorid, String title, String tipo, String descripcion, float valoracion, String localiaztion, String longitud, String latitud) throws SQLException {
        return null;
    }

    @Override
    public Event UpdateProfile(String id, String title, String tipo, String description) throws SQLException {
        return null;
    }

    @Override
    public Event UpdateValoracion(String id, float valoracion) throws SQLException {
        return null;
    }

    @Override
    public Event UpdateLocation(String localization, String longitud, String latitud) throws SQLException {
        return null;
    }

    @Override
    public Event GetEventById(String id) throws SQLException {
        return null;
    }

    @Override
    public Event GetEventByCreatorId(String id, String creatorid) throws SQLException {
        return null;
    }

    @Override
    public Event getEventsByUserId(String userid) throws SQLException /*Mirar getUserByAuthToken porque parece ser parecido*/
    {
        return null;
    }

    @Override
    public Event GetAllEvents() throws SQLException {
        return null;
    }

    @Override
    public boolean deleteCasal(String id) throws SQLException {
        return false;
    }
}
