package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.CasalCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CasalDAOImpl implements CasalDAO
{
    @Override
    public Casal createCasal(String adminid, String email, String name, String description, String localization, double latitude,double longitude, boolean validated) throws SQLException, CasalAlreadyExistsException {
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
            stmt.setString(3, email);
            stmt.setString(4, name);
            stmt.setString(5, description);
            stmt.setString(6, localization);
            stmt.setDouble(7, latitude);
            stmt.setDouble(8, longitude);
            stmt.setString(9, String.valueOf(false));
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
    public Casal updateProfile(String casalid, String email, String name, String description, String localization, double latitude, double longitude, boolean validated) throws SQLException
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
            stmt.setString(4, localization);
            stmt.setDouble(5, latitude);
            stmt.setDouble(6, longitude);
            stmt.setBoolean(7, validated);

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
                casal.setLocalization(rs.getString("localization"));
                casal.setLatitude(rs.getDouble("latitude"));
                casal.setLongitude(rs.getDouble("longitude"));
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
    public CasalCollection getValidatedCasals() throws SQLException
    {
        CasalCollection casalCollection = new CasalCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(CasalDAOQuery.GET_VALIDATED_CASALS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                Casal casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));
                casalCollection.getCasals().add(casal);
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
        return casalCollection;
    }

    @Override
    public CasalCollection getNoValidatedCasals() throws SQLException
    {
        CasalCollection casalCollection = new CasalCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(CasalDAOQuery.GET_NO_VALIDATED_CASALS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                Casal casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));
                casalCollection.getCasals().add(casal);
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
        return casalCollection;
    }

    @Override
    public CasalCollection getAllCasals() throws SQLException
    {
        CasalCollection casalCollection = new CasalCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(CasalDAOQuery.GET_ALL_CASALS);
            ResultSet rs = stmt.executeQuery();
            System.out.println("llista CASALS servida");
            while (rs.next())
            {
                Casal casal = new Casal();
                casal.setCasalid(rs.getString("casalid"));
                casal.setAdminid(rs.getString("adminid"));
                casal.setEmail(rs.getString("email"));
                casal.setName(rs.getString("name"));
                casal.setDescription(rs.getString("description"));
                casal.setLatitude(rs.getDouble("latitude"));
                casal.setLongitude(rs.getDouble("longitude"));
                casalCollection.getCasals().add(casal);
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

    @Override
    public String getAdminId(String casalid) throws SQLException {
        String adminid = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CasalDAOQuery.GET_ADMINID);
            stmt.setString(1, casalid);

            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                adminid = rs.getString(1);
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
        return adminid;
    }
}