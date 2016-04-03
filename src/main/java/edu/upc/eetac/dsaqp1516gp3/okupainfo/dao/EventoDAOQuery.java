package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

public interface EventoDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_EVENT = "insert into events((id, creatorid, title, tipo, description,valoracion, localization, latitud, longitud) values (UNHEX(?), UNHEX(?), ?, ?, ?,NULL,?,NULL,NULL)";
    String UPDATE_EVENT = "update events set title=?, tipo=?, description=? where id=unhex(?)";
    String UPDATE_VALORACION = "update events set valoracion=? where id=unhex(?)";
    String UPDATE_LOCATION = "update events set location=?, latitud=?, longitud=? where id=unhex(?)";
    String GET_EVENT_BY_ID = "select hex(e.id) as id, e.creatorid, c.title, c.tipo, c.descripcion, c.localization from casals c where id=unhex(?)";
    String GET_EVENT_BY_CREATOR_ID = "select hex(e.id) as id, e.creatorid, c.title, c.tipo, c.descripcion, c.localization from casals c where creatorid=unhex(?)";
    String GET_EVENTS_BY_USER_ID = "select hex(uv.id) as id, uv.userid from users_events uv where userid=unhex(?)";
    String GET_ALL_EVENTS = "select *from events";
    String DELETE_EVENT = "delete from events where id=unhex(?)";
}

/*
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_CASAL = "insert into casals((id, loginid, password, email, fullname, description,valoracion, localization, latitud, longitud) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?, ?,NULL,NULL,NULL,NULL);";
    String UPDATE_CASAL = "update casals set email=?, fullname=?, description=? where id=unhex(?)";
    //String ASSIGN_ROLE_REGISTERED = "insert into user_roles(casalid,role) values (UNHEX(?), 'creador')"; /*Como implementarlo si la tabla es users_roles?
    String UPDATE_LOCATION = "update casals set location=?, latitud=?, longitud=? where id=unhex(?)";
    String UPDATE_VALORACION = "update casals set valoracion=? where id=unhex(?)";
    String GET_CASAL_BY_ID = "select hex(c.id) as id, c.loginid, c.email, c.fullname, c.descripcion from casals c where id=unhex(?)";
    String GET_CASAL_BY_LOGIN_ID = "select hex(u.id) as id, c.loginid, c.email, c.fullname, c.description from casals c where c.loginid=?";
    String DELETE_CASAL = "delete from casals where id=unhex(?)";
    String GET_PASSWORD = "select hex(password) as password from casals where id=unhex(?)";
    String GET_ALL_CASALS = "select *from casals"; //Highly ineffective but it is a way to show all casals in case it is needed
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