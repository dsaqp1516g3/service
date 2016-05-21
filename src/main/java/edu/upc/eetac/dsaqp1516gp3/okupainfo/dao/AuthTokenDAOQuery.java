package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

public interface AuthTokenDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_TOKEN = "insert into auth_tokens (userid, token) values (UNHEX(?), UNHEX(?))";
    String GET_USER_BY_TOKEN = "select hex(u.id) as id from users u, auth_tokens t where t.token=unhex(?) and u.id=t.userid";
    String GET_ROLES_OF_USER = "select hex(userid), role from user_roles where userid=unhex(?)";
    String DELETE_TOKEN = "delete from auth_tokens where userid = unhex(?)";
}
