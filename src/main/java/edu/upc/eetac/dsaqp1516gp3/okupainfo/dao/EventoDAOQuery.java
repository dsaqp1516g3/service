package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface EventoDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_EVENT = "insert into events (id, casalid, title, description, localization, latitude, longitude, last_modified) values (UNHEX(?), UNHEX(?), ?, ?, ?, ?, ?, ?)";
    String UPDATE_EVENT = "update events set title=?, description=?, eventdate=?, localization=?, latitude=?, longitude=? where id=unhex(?)";
    String GET_EVENT_BY_ID = "select hex(e.id) as id, e.casalid, e.title, e.description, e.localization, e.latitude, e.longitude, e.eventdate from events e where id=unhex(?)";
    String GET_EVENT_BY_CREATOR_ID = "select hex(e.id) as id, e.casalid, e.title, e.description, e.localization, e.latitude, e.longitude, e.eventdate from events e where creatorid=unhex(?)";
    String GET_EVENTS_BY_USER_ID = "select hex(e.id) as eventid, e.casalid, e.title, e.description, e.localization, e.eventdate from users u, events e, users_events uv where uv.userid=unhex(?) and u.id=uv.userid and uv.eventoid=e.id order by e.eventdate";
    String ADD_ASSISTANCE_TO_EVENT = "insert into users_events(userid, eventoid) values (UNHEX(?), UNHEX(?))";
    String DELETE_ASSITANCE_TO_EVENT ="delete from users_events where userid=unhex(?) and eventoid=unhex(?)";
    String GET_ALL_EVENTS = "select hex(id) as id, hex(casalid) as casalid, title, description, localization, latitude, longitude, eventdate from events";
    String GET_EVENTS_AFTER = "select hex(id) as id, hex(casalid) as casalid, title, description, eventdate, creation_timestamp, last_modified from events where creation_timestamp > ? order by creation_timestamp desc limit 5";
    String DELETE_EVENT = "delete from events where id=unhex(?)";
}
