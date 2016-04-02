package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDAOImpl implements UserDAO {
    @Override
    public User createUser(String loginid, String password, String email, String fullname, String descripcion) throws SQLException, UserAlreadyExistsException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            User user = getUserByLoginid(loginid);
            if (user != null)
                throw new UserAlreadyExistsException();

            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);

            stmt.close();
            stmt = connection.prepareStatement(UserDAOQuery.CREATE_USER);
            stmt.setString(1, id);
            stmt.setString(2, loginid);
            stmt.setString(3, password);
            stmt.setString(4, email);
            stmt.setString(5, fullname);
            stmt.setString(6, descripcion);
            stmt.executeUpdate();

            stmt.close();
            stmt = connection.prepareStatement(UserDAOQuery.ASSIGN_ROLE_REGISTERED);
            stmt.setString(1, id);
            stmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getUserById(id);
    }

    @Override
    public User updateProfile(String id, String email, String fullname, String descripcion) throws SQLException {
        User user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UPDATE_USER);
            stmt.setString(1, email);
            stmt.setString(2, fullname);
            stmt.setString(3, descripcion);

            int rows = stmt.executeUpdate();
            if (rows == 1) {
                user = getUserById(id);
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return user;
    }

    @Override
    public User updateAsistance(String id) throws SQLException
    {
        User user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            stmt = connection.prepareStatement(UserDAOQuery.ASSIGN_ASSISTANCE);// en eventos buscammos la id de usuario e insertamos dentro de user_events
            stmt.setString(1, id);
            stmt.executeUpdate();
            connection.commit();

        } catch (SQLException e)
        {
            throw e;
        } finally
        {
            if (stmt != null) stmt.close();
            if (connection != null)
            {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return user;

    }




    @Override
    public User getUserById(String id) throws SQLException
    {
        User user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            // Obtiene la conexión del DataSource
            connection = Database.getConnection();

            // Prepara la consulta
            stmt = connection.prepareStatement(UserDAOQuery.GET_USER_BY_ID);
            // Da valor a los parámetros de la consulta
            stmt.setString(1, id);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            // Procesa los resultados
            if (rs.next())
            {
                user = new User();
                user.setId(rs.getString("id"));
                user.setLoginid(rs.getString("loginid"));
                user.setEmail(rs.getString("email"));
                user.setFullname(rs.getString("fullname"));
                user.setDescription(rs.getString("description"));
            }
        }
        catch (SQLException e)
        {
            // Relanza la excepción
            throw e;
        }
        finally
        {
            // Libera la conexión
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        // Devuelve el modelo
        return user;
    }

    @Override
    public User getUserByLoginid(String loginid) throws SQLException {
        User user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();


            stmt = connection.prepareStatement(UserDAOQuery.GET_USER_BY_USERNAME);
            stmt.setString(1, loginid);

            ResultSet rs = stmt.executeQuery();
            /*while (rs.next()) // Tiene que leer todos los usuarios y devolverlos, pero quizas no sera necesario
            {
                user = new User();
                user.setId(rs.getString("id"));
                user.setLoginid(rs.getString("loginid"));
                user.setEmail(rs.getString("email"));
                user.setFullname(rs.getString("fullname"));
                user.setDescription(rs.getString("description"));
            }*/
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return user;
    }

    @Override
    public User getAllUsers() throws SQLException {
        User user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.GET_ALL_USERS);

            ResultSet rs = stmt.executeQuery();
        while(rs.next())
            {
                user = new User();
                user.setId(rs.getString("id"));
                user.setLoginid(rs.getString("loginid"));
                user.setEmail(rs.getString("email"));
                user.setFullname(rs.getString("fullname"));
                user.setDescription(rs.getString("description"));
            }
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return user;
    }

    @Override
    public boolean deleteUser(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.DELETE_USER);
            stmt.setString(1, id);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    /*@Override
    public User getEventsById(String id) throws SQLException
    {

    }

    @Override
    public User getEventsByLoginId(String loginid) throws SQLException {
        return null;
    }*/

    @Override
    public boolean checkPassword(String id, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.GET_PASSWORD);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                String storedPassword = rs.getString("password");
                try
                {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(password.getBytes());
                    String passedPassword = new BigInteger(1, md.digest()).toString(16);

                    return passedPassword.equalsIgnoreCase(storedPassword);
                }
                catch (NoSuchAlgorithmException e) {}
            }
            return false;
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }
}