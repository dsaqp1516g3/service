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
    public Casal createCasal(String adminid, String email, String name, String description, String localization, double latitude,double longitude, boolean validado) throws SQLException, CasalAlreadyExistsException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String casalid = null;
        try {
            Casal casal = getCasalByEmail(email);
            if (casal != null)
                throw new CasalAlreadyExistsException();

            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                casalid = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);

            stmt.close();
            stmt = connection.prepareStatement(CasalDAOQuery.CREATE_CASAL);

            stmt.setString(1, casalid);
            stmt.setString(2, adminid);
            stmt.setString(4, email);
            stmt.setString(5, name);
            stmt.setString(6, description);
            stmt.setString(7, localization);
            stmt.setString(8, String.valueOf(latitude));
            stmt.setString(9, String.valueOf(longitude));
            stmt.setString(10, String.valueOf(false));
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
        return getCasalByCasalid(casalid);
    }



    @Override
    public Casal updateProfile(String casalid, String email, String name, String description) throws SQLException
    {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UPDATE_CASAL);
            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setString(3, description);

            int rows = stmt.executeUpdate();
            if (rows == 1) {
                casal = getCasalByCasalid(casalid);
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
    public Casal updateLocation(String casalid, String localization, String latitud, String longitud) throws SQLException
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
                casal = getCasalByCasalid(casalid);
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
    public Casal getCasalByCasalid(String casalid) throws SQLException
    {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            // Obtiene la conexión del DataSource
            connection = Database.getConnection();

            // Prepara la consulta
            stmt = connection.prepareStatement(CasalDAOQuery.GET_CASAL_BY_CASALID);
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
    public Casal getCasalByEmail(String email) throws SQLException {
        return null;
    }

    @Override
    public Casal getValidatedCasals() throws SQLException {
        return null;
    }

    @Override
    public Casal getNoValidatedCasals() throws SQLException {
        return null;
    }


    @Override
    public Casal getAllCasals() throws SQLException
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


}