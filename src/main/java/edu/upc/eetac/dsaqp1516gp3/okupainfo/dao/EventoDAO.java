package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface EventoDAO
{
    Event createEvent(String casalid, String title, String description, String localization, double latitude, double longitude, double eventdate)throws SQLException;
    Event updateProfile(String id, String title, String description, double eventdate, String localization, double latitude, double longitude)throws SQLException;
    Event getEventById(String id) throws SQLException;//Nos devuelve el evento que tenga la ID que introducimos
    void addUserAssistance(String userid, String eventid) throws SQLException;
    EventCollection getEventsByCreatorId(String casalid, long timestamp, boolean before)throws SQLException;// Nos devuelve los eventos segun el creador que introduzcamos, 1 casal varios eventos puede crear
    EventCollection getEventsByUserId(String userid, long timestamp, boolean before) throws SQLException;// Pasamos la Id del ususario y en la tabla users_events nos devuelve los eventos
    EventCollection getAllEvents(long timestamp, boolean before)throws SQLException;
    boolean deleteEvent(String id) throws SQLException;
    boolean deleteAssistanceEvent(String userid, String eventid) throws SQLException;
}
