package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface UserDAOQuery
{
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_USER = "insert into users (id, loginid, password, email, fullname, description,image) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?, ?, ?)";
    String UPDATE_USER = "update users set email=?, fullname=?, description=? where id=unhex(?)";
    String ASSIGN_ROLE_REGISTERED = "insert into user_roles(userid,role) values (UNHEX(?), 'registered')";
    String GET_USER_BY_ID = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.description, u.image from users u where id=unhex(?)";
    String GET_USER_BY_USERNAME = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.description, u.image from users u where u.loginid=?";
    String DELETE_USER = "delete from users where id=unhex(?)";
    String GET_PASSWORD = "select hex(password) as password from users where id=unhex(?)";
    String GET_ALL_USERS = "select hex(id) as id, loginid, email, fullname, description, image from users";
}