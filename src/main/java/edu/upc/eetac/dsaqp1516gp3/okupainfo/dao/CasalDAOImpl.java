package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;
<<<<<<< HEAD
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
=======

>>>>>>> origin/master
import java.sql.SQLException;

public class CasalDAOImpl implements CasalDAO{
    @Override
<<<<<<< HEAD
    public Casal createCasal(String loginid, String password, String email, String fullname, String descripcion) throws SQLException, CasalAlreadyExistsException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try
        {
            Casal casal = getCasalByLoginid(loginid);
            if (user != null)
                throw new CasalAlreadyExistsException();

            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);

            stmt.close();
            stmt = connection.prepareStatement(CasalDAOQuery.CREATE_USER);
            stmt.setString(1, id);
            stmt.setString(2, loginid);
            stmt.setString(3, password);
            stmt.setString(4, email);
            stmt.setString(5, fullname);
            stmt.setString(6, descripcion);
            stmt.executeUpdate();

            stmt.close();
            stmt = connection.prepareStatement(CasalDAOQuery.ASSIGN_ROLE_REGISTERED);
            stmt.setString(1, id);
            stmt.executeUpdate();

            connection.commit();
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            if (stmt != null) stmt.close();
            if (connection != null)
            {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getCasalById(id);
    }

    @Override
    public Casal updateProfile(String id, String email, String fullname, String descripcion) throws SQLException
    {
        Casal user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UPDATE_USER);
            stmt.setString(1, email);
            stmt.setString(2, fullname);
            stmt.setString(3, descripcion);

            int rows = stmt.executeUpdate();
            if (rows == 1)
            {
                user = getCasalById(id);
            }

            stmt = connection.prepareStatement(CasalDAOQuery.ASSIGN_ASSISTANCE);// en eventos buscammos la id de usuario e insertamos dentro de user_events
            stmt.setString(1, id);
            stmt.executeUpdate();
            connection.commit();

        }
        catch(SQLException e)
        {
            throw e;
        }
        finally
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
    public Casal getCasalById(String id) throws SQLException
    {
        Casal user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            // Obtiene la conexión del DataSource
            connection = Database.getConnection();

            // Prepara la consulta
            stmt = connection.prepareStatement(CasalDAOQuery.GET_USER_BY_ID);
            // Da valor a los parámetros de la consulta
            stmt.setString(1, id);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            // Procesa los resultados
            if (rs.next())
            {
                user = new Casal();
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
    public Casal getCasalByLoginid(String loginid) throws SQLException {
        Casal user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();


            stmt = connection.prepareStatement(CasalDAOQuery.GET_USER_BY_USERNAME);
            stmt.setString(1, loginid);

            ResultSet rs = stmt.executeQuery();
            /*while (rs.next()) // Tiene que leer todos los usuarios y devolverlos, pero quizas no sera necesario
            {
                user = new Casal();
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
    public Casal getAllCasals() throws SQLException {
        Casal user = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.GET_ALL_USERS);

            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                user = new Casal();
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
    public boolean deleteCasal(String id) throws SQLException {
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
=======
    public Casal createCasal(String loginid, String password, String email, String fullname, String description, float valoracion, String localization, String latitud, String longitud) throws SQLException, CasalAlreadyExistsException {
        return null;
    }

    @Override
    public Casal UpdateProfile(String id, String email, String fullname, String description, String localization, String latitud, String longitud) throws SQLException {
        return null;
    }

    @Override
    public Casal UpdateValoracion(String id, float valoracion) throws SQLException {
        return null;
    }

    @Override
    public Casal UpdtateLocation(String id, String localization, String latitud, String longitud) throws SQLException {
        return null;
    }

    @Override
    public Casal GetCasalById(String id) throws SQLException {
        return null;
    }

    @Override
    public Casal GetCasalByLoginId(String loginid) throws SQLException {
        return null;
    }

    @Override
    public Casal GetAllCasals() throws SQLException {
        return null;
    }

    @Override
    public boolean deleteCasal(String id) throws SQLException {
        return false;
    }

    @Override
    public boolean checkPassword(String id, String password) throws SQLException {
        return false;
>>>>>>> origin/master
    }
}
