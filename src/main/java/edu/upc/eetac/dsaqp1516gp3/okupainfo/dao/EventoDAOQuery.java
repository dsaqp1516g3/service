package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

public interface EventoDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_EVENT = "insert into events((id, creatorid, title, tipo, description,valoracion, localization, latitud, longitud) values (UNHEX(?), UNHEX(?), ?, ?, ?,NULL,?,NULL,NULL)";
    String UPDATE_EVENT = "update events set title=?, tipo=?, description=? where id=unhex(?)";
    String UPDATE_VALORACION = "update events set valoracion=? where id=unhex(?)";
    String UPDATE_LOCATION = "update events set location=?, latitud=?, longitud=? where id=unhex(?)";
    String GET_EVENT_BY_ID = "select hex(e.id) as id, e.creatorid, c.title, c.tipo, c.descripcion, c.localization from events e where id=unhex(?)";
    String GET_EVENT_BY_CREATOR_ID = "select hex(e.id) as id, e.creatorid, e.title, e.tipo, e.descripcion, e.localization from events e where creatorid=?";
    String GET_EVENTS_BY_USER_ID = "select hex(uv.id) as id from users u, users_events uv where uv.eventoid=unhex(?) and u.id=uv.userid";
    String GET_ALL_EVENTS = "select *from events";
    String DELETE_EVENT = "delete from events where id=unhex(?)";
}
/*
public final static String GET_USER_BY_TOKEN = "select hex(u.id) as id from users u, auth_tokens t where t.token=unhex(?) and u.id=t.userid";
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_TOKEN = "insert into auth_tokens (userid, token) values (UNHEX(?), UNHEX(?))";
    public final static String GET_USER_BY_TOKEN = "select hex(u.id) as id from users u, auth_tokens t where t.token=unhex(?) and u.id=t.userid";
    public final static String GET_ROLES_OF_USER = "select hex(userid), role from user_roles where userid=unhex(?)";
    public final static String DELETE_TOKEN = "delete from auth_tokens where userid = unhex(?)";
 */