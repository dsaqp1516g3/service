package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface EventoDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_EVENT = "insert into events (id, casalid, title, description, localization, latitude, longitude, eventdate, image) values (UNHEX(?), UNHEX(?), ?, ?, ?, ?, ?, ?, ?)";
    String UPDATE_EVENT = "update events set title=?, description=?, eventdate=?, localization=?, latitude=?, longitude=? where id=unhex(?)";
    String GET_EVENT_BY_ID = "select hex(id) as id, hex(casalid) as casalid, title, description, localization, latitude, longitude, eventdate, image, creation_timestamp, last_modified from events where id=unhex(?)";
    String GET_EVENTS_BY_CASAL_ID = "select hex(id) as id, hex(casalid) as casalid, title, description, localization, latitude, longitude, eventdate, image, creation_timestamp, last_modified from events where casalid=unhex(?)";
    String GET_EVENTS_BY_USER_ID = "select hex(e.id) as id, hex(e.casalid) as casalid, e.title, e.description, e.localization, e.eventdate, e.latitude, e.longitude, e.image,  e.creation_timestamp, e.last_modified from users u, events e, users_events uv where uv.userid=unhex(?) and u.id=uv.userid and uv.eventoid=e.id order by e.eventdate";
    String CHECK_ASSISTANCE_TO_EVENT = "select hex(userid) as userid, hex(eventoid) as eventoid from users_events where userid=unhex(?) and eventoid=unhex(?)";
    String ADD_ASSISTANCE_TO_EVENT = "insert into users_events(userid, eventoid) values (UNHEX(?), UNHEX(?))";
    String DELETE_ASSITANCE_TO_EVENT ="delete from users_events where userid=unhex(?) and eventoid=unhex(?)";
    String GET_ALL_EVENTS = "select hex(id) as id, hex(casalid) as casalid, title, description, localization, latitude, longitude, eventdate, image, creation_timestamp, last_modified from events";
    String GET_EVENTS_AFTER = "select hex(id) as id, hex(casalid) as casalid, title, description, eventdate, image, creation_timestamp, last_modified from events where creation_timestamp > ? order by creation_timestamp desc limit 5";
    String DELETE_EVENT = "delete from events where id=unhex(?)";
}
