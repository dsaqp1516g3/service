package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CasalDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_CASAL = "insert into casals((id, loginid, password, email, fullname, description,valoracion, localization, latitud, longitud) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?, ?,NULL,?,NULL,NULL);";
    String UPDATE_CASAL = "update casals set email=?, fullname=?, description=? where id=unhex(?)";
    //String ASSIGN_ROLE_REGISTERED = "insert into user_roles(casalid,role) values (UNHEX(?), 'creador')"; /*Como implementarlo si la tabla es users_roles?*/
    String UPDATE_LOCATION = "update casals set location=?, latitud=?, longitud=? where id=unhex(?)";
    String UPDATE_VALORACION = "update casals set valoracion=? where id=unhex(?)";
    String GET_CASAL_BY_ID = "select hex(c.id) as id, c.loginid, c.email, c.fullname, c.descripcion from casals c where id=unhex(?)";
    String GET_CASAL_BY_LOGIN_ID = "select hex(u.id) as id, c.loginid, c.email, c.fullname, c.description from casals c where c.loginid=?";
    String DELETE_CASAL = "delete from casals where id=unhex(?)";
    String GET_PASSWORD = "select hex(password) as password from casals where id=unhex(?)";
    String GET_ALL_CASALS = "select *from casals"; //Highly ineffective but it is a way to show all casals in case it is needed
}
