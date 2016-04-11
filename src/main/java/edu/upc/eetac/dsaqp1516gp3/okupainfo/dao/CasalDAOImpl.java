package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CasalDAOImpl implements CasalDAO
{
    @Override
    public Casal createCasal(String loginid, String password, String email, String fullname, String descripcion, String localization) throws SQLException, CasalAlreadyExistsException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            Casal casal = getCasalByLoginid(loginid);
            if (casal != null)
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
            stmt = connection.prepareStatement(CasalDAOQuery.CREATE_CASAL);
            /*
            insert into casals (id,     loginid, password,    email, fullname, description, valoracion, localization, latitud, longitud)
                        values (UNHEX(?), ?,    UNHEX(MD5(?)), ?,        ?,          ?,         NULL,       ?,          NULL,   NULL);*/
            stmt.setString(1, id);
            stmt.setString(2, loginid);
            stmt.setString(3, password);
            stmt.setString(4, email);
            stmt.setString(5, fullname);
            stmt.setString(6, descripcion);
            stmt.setString(7, localization);
            stmt.executeUpdate();

            /*stmt.close();
            stmt = connection.prepareStatement(CasalDAOQuery.ASSIGN_ROLE_REGISTERED);
            stmt.setString(1, id);
            stmt.executeUpdate();*/

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
        return getCasalById(id);
    }

    @Override
    public Casal UpdateProfile(String id, String email, String fullname, String descripcion) throws SQLException
    {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UPDATE_CASAL);
            stmt.setString(1, email);
            stmt.setString(2, fullname);
            stmt.setString(3, descripcion);

            int rows = stmt.executeUpdate();
            if (rows == 1) {
                casal = getCasalById(id);
            }

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
        return casal;
    }

    @Override
    public Casal updateValoracion(String id, float valoracion) throws SQLException
    {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UPDATE_VALORACION);
            stmt.setString(1, String.valueOf(valoracion));

            int rows = stmt.executeUpdate();
            if (rows == 1)
            {
                casal = getCasalById(id);
            }
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
        return casal;
    }

    @Override
    public Casal UpdateLocation(String id, String localization, String latitud, String longitud) throws SQLException
    {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UPDATE_LOCATION);
            stmt.setString(1, localization);
            stmt.setString(2, latitud);
            stmt.setString(3, longitud);

            int rows = stmt.executeUpdate();
            if (rows == 1)
            {
                casal = getCasalById(id);
            }
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
        return casal;
    }

    @Override
    public Casal getCasalById(String id) throws SQLException
    {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            // Obtiene la conexión del DataSource
            connection = Database.getConnection();

            // Prepara la consulta
            stmt = connection.prepareStatement(CasalDAOQuery.GET_CASAL_BY_ID);
            // Da valor a los parámetros de la consulta
            stmt.setString(1, id);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            // Procesa los resultados
            if (rs.next())
            {
                casal = new Casal();
                casal.setId(rs.getString("id"));
                casal.setLoginid(rs.getString("loginid"));
                casal.setEmail(rs.getString("email"));
                casal.setFullname(rs.getString("fullname"));
                casal.setDescription(rs.getString("descripcion"));
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
        return casal;
    }

    @Override
    public Casal getCasalByLoginid(String loginid) throws SQLException
    {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.GET_CASAL_BY_LOGIN_ID);
            stmt.setString(1, loginid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                casal = new Casal();
                casal.setId(rs.getString("id"));
                casal.setLoginid(rs.getString("loginid"));
                casal.setEmail(rs.getString("email"));
                casal.setFullname(rs.getString("fullname"));
                casal.setDescription(rs.getString("descripcion"));
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
        return casal;
    }

    @Override
    public Casal GetAllCasals() throws SQLException
    {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.GET_ALL_CASALS);
            /* Habria que crear un vector de casals donde añadir todos los casals y devolver este vector, ahora solo se esta devolviendo unor
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                casal = new Casal();
                casal.setId(rs.getString("id"));
                casal.setLoginid(rs.getString("loginid"));
                casal.setEmail(rs.getString("email"));
                casal.setFullname(rs.getString("fullname"));
                casal.setDescription(rs.getString("descripcion"));
            }
            }*/

        }
        catch(SQLException e)
        {
            throw e;
        }
        finally
        {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return null;
    }

    @Override
    public boolean deleteCasal(String id) throws SQLException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.DELETE_CASAL);
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

    @Override
    public boolean checkPassword(String id, String password) throws SQLException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.GET_PASSWORD);
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