package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;

import java.sql.SQLException;
import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface EventoDAO
{
    Event createEvent(String casalid, String title, String description, String localization, double latitude, double longitude, long eventdate)throws SQLException;
    Event updateProfile(String id, String title, String description, Timestamp eventdate, String localization, double latitude, double longitude)throws SQLException;
    Event getEventById(String id) throws SQLException;
    void addUserAssistance(String userid, String eventid) throws SQLException;
    EventCollection getEventsByCasalId(String casalid, long timestamp, boolean before)throws SQLException;
    EventCollection getEventsByUserId(String userid, long timestamp, boolean before) throws SQLException;
    EventCollection getAllEvents(long timestamp, boolean before)throws SQLException;
    boolean deleteEvent(String id) throws SQLException;
    boolean deleteAssistanceEvent(String userid, String eventid) throws SQLException;
    boolean checkAssistanceToEvent(String userid, String eventid) throws SQLException;
    boolean checkEventsOfCasal(String casalid) throws SQLException;
}
