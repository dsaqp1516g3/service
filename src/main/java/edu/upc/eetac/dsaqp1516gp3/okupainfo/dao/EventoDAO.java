package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface EventoDAO
{
    Event createEvent(String casalid, String title, String description, String localization, Double latitude, Double longitude)throws SQLException;
    Event updateProfile(String id, String title, String description)throws SQLException;
    Event updateLocation(String id, String localization, Double latitude, Double longitude)throws SQLException;
    Event getEventById(String id)throws SQLException;//de la tabla eventos
    Event getEventByCreatorId(String creatorid)throws SQLException;
    Event getEventsByUserId(String userid) throws SQLException;// Pasamos la Id del ususario y en la tabla users_events nos devuelve los eventos
    EventCollection getAllEvents()throws SQLException;
    boolean deleteEvent(String id) throws SQLException;
}
/*id BINARY(16) NOT NULL,
	casalsid BINARY(16) NOT NULL,
 	title VARCHAR(100) NOT NULL,
	descripcion VARCHAR(500) NOT NULL,
	localization VARCHAR(264)NOT NULL,
	latitud DOUBLE DEFAULT 0,
	longitud DOUBLE DEFAULT 0,
	last_modified TIMESTAMP NOT NULL,
	creation_timestamp DATETIME not null default current_timestamp,*/