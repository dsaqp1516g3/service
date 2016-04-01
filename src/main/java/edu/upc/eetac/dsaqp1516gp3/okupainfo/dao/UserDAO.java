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
    /*User getEventsById(String id) throws SQLException;
    User getEventsByLoginId(String loginid) throws SQLException;*/
    boolean checkPassword(String id, String password) throws SQLException;
}

