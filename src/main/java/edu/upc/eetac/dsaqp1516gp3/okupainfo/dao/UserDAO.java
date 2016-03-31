package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.User;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface UserDAO
{
    User createUser(String loginid, String password, String email, String fullname, String descripcion) throws SQLException, UserAlreadyExistsException;
    User updateProfile(String id, String email, String fullname, String descripcion) throws SQLException;
    User getUserById(String id) throws SQLException;
    User getUserByLoginid(String loginid) throws SQLException;
    User getAllUsers() throws SQLException;
    boolean deleteUser(String id) throws SQLException;
    User getEventsById(String id) throws SQLException;
    User getEventsByLoginId(String loginid) throws SQLException;
    boolean checkPassword(String id, String password) throws SQLException;
}


/*
    String UUID = "select REPLACE(UUID(),'-','')";
    String CREATE_USER = "insert into users((id, loginid, password, email, fullname, description, asistencia) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?, ?, ?);";
    String UPDATE_USER = "update users set email=?, fullname=?, description=?, asistencia = ? where id=unhex(?)";
    String ASSIGN_ROLE_REGISTERED = "insert into user_roles(userid,role) values (UNHEX(?), 'registered)";
    String GET_USER_BY_ID = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.descripcion, u.asistencia from users u where id=unhex(?)";
    String GET_USER_BY_USERNAME = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.description, u.asistencia from users u where u.loginid=?";
    String DELETE_USER = "delete from users where id=unhex(?)";
    String GET_PASSWORD = "select hex(password) as password from user where id=unhex(?)";
    String GET_ALL_USERS = "select *from users"; //Highly ineffective but it is a way to show all users in case it is needed
    String GET_USER_ASISTENCIA_BY_USERNAME = "select hex(u.id) as id, u.asistencia from users u where u.loginid=?";
    String GET_USER_ASISTENCIA_BY_ID = "select hex(u.id) as id, u.asistencia from users u where id=unhex(?)";
 */