package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

public interface EventoDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_EVENT = "";
    String UPDATE_EVENT = "";
    String UPDATE_VALORACION = "";
    String UPDATE_LOCATION = "";
    String GET_EVENT_BY_ID = "";
    String GET_EVENT_BY_CREATOR_ID = "";
    String GET_EVENTS_BY_USER_ID = "select hex(uv.id) as id, uv.userid from users_events uv where userid=unhex(?)";
    String GET_ALL_EVENTS = "";
    String DELETE_CASAL = "";
}

/*

    String CREATE_USER = "insert into users((id, loginid, password, email, fullname, description) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?, ?);";
    String UPDATE_USER = "update users set email=?, fullname=?, description=? where id=unhex(?)";
    String ASSIGN_ROLE_REGISTERED = "insert into user_roles(userid,role) values (UNHEX(?), 'registered)";
    String ASSIGN_ASSISTANCE = "insert into user_events(userid,eventoid) values (UNHEX(?), UNHEX(?)"; //Falta descubrir como implementarlo
    String GET_USER_BY_ID = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.descripcion from users u where id=unhex(?)";
    String GET_USER_BY_USERNAME = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.description from users u where u.loginid=?";
    String DELETE_USER = "delete from users where id=unhex(?)";
    String GET_PASSWORD = "select hex(password) as password from users where id=unhex(?)";
    String GET_ALL_USERS = "select *from users";
    String GET_USERS_BY_EVENT_ID = "select hex(uv.id) as id, uv.eventoid from users_events uv where uv.eventoid=?";
 */