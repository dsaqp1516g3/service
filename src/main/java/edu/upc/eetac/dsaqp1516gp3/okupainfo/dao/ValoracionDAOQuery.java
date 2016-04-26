package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ValoracionDAOQuery {

        String UUID = "select REPLACE(UUID(),'-','')";
        String CREATE_VALORACION = "insert into users((id, loginid, password, email, fullname, description) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?, ?);";
        String UPDATE_VALORACION = "update users set email=?, fullname=?, description=? where id=unhex(?)";
        String GET_VALORACION_BY_ID = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.descripcion from users u where id=unhex(?)";
        String GET_VALORACION_BY_LOGINID = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.description from users u where u.loginid=?";
        String GET_VALORACION_BY_CASALID = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.description from users u where u.loginid=?";
        String DELETE_VALORACION = "delete from users where id=unhex(?)";
        String GET_ALL_VALORACIONES = "select *from users";
        String GET_VALORACIONES_BY_LOGINID = "select hex(uv.id) as id from events v, users_events uv where uv.userid=unhex(?) and e.id=uv.eventoid";
        String GET_VALORACIONES_BY_CASALID = "select hex(uv.id) as id";
}
