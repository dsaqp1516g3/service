package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.CasalCollection;

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
    /*private List<Link> links;
    private String casalid;
    private String adminid;
    private String email;
    private String name;
    private String description;
    private Boolean validadet;
    private String localization;
    private double latitude;
    private double longitude;
    */

    @Override
    public Casal createCasal(String adminid, String email, String name, String description, String localization, double latitude,double longitude, boolean validadet) throws SQLException, CasalAlreadyExistsException {
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

            stmt.setString(1, adminid);
            stmt.setString(2, casalid);
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
    public Casal updateLocation(String casalid, String localization, Double latitude, Double longitude) throws SQLException
    {

        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.UPDATE_LOCATION);
            stmt.setString(1, localization);
            stmt.setDouble(2, latitude);
            stmt.setDouble(3, longitude);

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
            stmt.setString(1, casalid);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            // Procesa los resultados
            if (rs.next())
            {
                casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));
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
    public Casal getCasalByEmail(String email) throws SQLException
    {
        Casal casal = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            // Obtiene la conexión del DataSource
            connection = Database.getConnection();

            // Prepara la consulta
            stmt = connection.prepareStatement(CasalDAOQuery.GET_CASAL_BY_EMAIL);
            // Da valor a los parámetros de la consulta
            stmt.setString(1, email);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            // Procesa los resultados
            if (rs.next())
            {
                casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));
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
    public Casal getValidatedCasals() throws SQLException
    {
        return null;
    }

    @Override
    public Casal getNoValidatedCasals() throws SQLException
    {
        return null;
    }

    @Override
    public CasalCollection getAllCasals() throws SQLException {
        CasalCollection casalCollection = new CasalCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(CasalDAOQuery.GET_ALL_CASALS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Casal casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));
                casalCollection.getCasals().add(casal);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return casalCollection;
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