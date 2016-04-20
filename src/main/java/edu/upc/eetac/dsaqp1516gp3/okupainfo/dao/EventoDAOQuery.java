package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface EventoDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_EVENT = "insert into events((id, creatorid, title, description, localization, latitude, longitude) values (UNHEX(?), UNHEX(?), ?,  ?, ?,NULL,NULL)";
    String UPDATE_EVENT = "update events set title=?, description=? where id=unhex(?)";
    String UPDATE_LOCATION = "update events set localization=?, latitude=?, longitude=? where id=unhex(?)";
    String GET_EVENT_BY_ID = "select hex(e.id) as id, e.casalid, c.title, c.description, c.localization from events e where id=unhex(?)";
    String GET_EVENT_BY_CREATOR_ID = "select hex(e.id) as id, e.casalid, e.title, e.description, e.localization from events e where creatorid=?";
    String GET_EVENTS_BY_USER_ID = "select hex(uv.id) as id from users u, users_events uv where uv.eventoid=unhex(?) and u.id=uv.userid";
    String GET_ALL_EVENTS = "select *from events";
    String GET_EVENTS_AFTER = "select hex(id) as id, hex(casalid) as casalid, title, description, creation_timestamp, last_modified from events where creation_timestamp > ? order by creation_timestamp desc limit 5";
    String DELETE_EVENT = "delete from events where id=unhex(?)";
}