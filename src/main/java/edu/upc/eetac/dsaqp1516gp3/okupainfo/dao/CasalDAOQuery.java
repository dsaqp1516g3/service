package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CasalDAOQuery
{
    //oju, cal fer les sql queries adaptades a casal
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_CASAL = "insert into users((id, loginid, password, email, fullname, description) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?, ?);";
    String UPDATE_CASAL = "update users set email=?, fullname=?, description=? where id=unhex(?)";
    String ASSIGN_ROLE_REGISTERED = "insert into user_roles(userid,role) values (UNHEX(?), 'registered)";
    String ASSIGN_ASSISTANCE = "insert into user_events(userid,eventoid) values (UNHEX(?), UNHEX(?)"; //Falta descubrir como implementarlo
    String GET_CASAL_BY_ID = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.descripcion from users u where id=unhex(?)";
    String GET_CASAL_BY_CASALNAME = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.description from users u where u.loginid=?";
    String DELETE_CASAL = "delete from users where id=unhex(?)";
    String GET_PASSWORD = "select hex(password) as password from user where id=unhex(?)";
    String GET_ALL_CASALS = "select *from users"; //Highly ineffective but it is a way to show all users in case it is needed
}