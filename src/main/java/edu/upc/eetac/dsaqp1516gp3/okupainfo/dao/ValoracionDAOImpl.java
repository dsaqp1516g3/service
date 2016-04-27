package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Valoracion;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.ValoracionCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValoracionDAOImpl implements ValoracionDAO
{

    @Override
    public Valoracion createValoracion(String loginid, String casalid, Boolean valoracion) throws SQLException, UserAlreadyExistsException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ValoracionDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);

            stmt.close();
            stmt = connection.prepareStatement(ValoracionDAOQuery.CREATE_VALORACION);
            stmt.setString(1, id);
            stmt.setString(2, loginid);
            stmt.setString(3, casalid);
            stmt.setBoolean(4, valoracion);
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
        return getValoracionById(id);
    }

    @Override
    public Valoracion updateValoracion(String id, String loginid, String casalid, Boolean valoracion) throws SQLException
    {
        Valoracion Valoracion = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ValoracionDAOQuery.UPDATE_VALORACION);
            stmt.setString(1, loginid);
            stmt.setString(2, casalid);
            stmt.setBoolean(3, valoracion);

            int rows = stmt.executeUpdate();
            if (rows == 1)
            {
                Valoracion = getValoracionById(id);
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
        return Valoracion;
    }

    @Override
    public Valoracion getValoracionById(String id) throws SQLException
    {
        Valoracion Valoracion = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            // Obtiene la conexión del DataSource
            connection = Database.getConnection();

            // Prepara la consulta
            stmt = connection.prepareStatement(ValoracionDAOQuery.GET_VALORACION_BY_ID);
            // Da valor a los parámetros de la consulta
            stmt.setString(1, id);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            // Procesa los resultados
            if (rs.next())
            {
                Valoracion = new Valoracion();
                Valoracion.setId(rs.getString("id"));
                Valoracion.setLoginid(rs.getString("loginid"));
                Valoracion.setCasalid(rs.getString("casalid"));
                Valoracion.setValoracion(rs.getString("valoracion"));
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
        return Valoracion;
    }

    @Override
    public Valoracion getValoracionByLoginid(String loginid) throws SQLException
    {
        Valoracion Valoracion = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            // Obtiene la conexión del DataSource
            connection = Database.getConnection();

            // Prepara la consulta
            stmt = connection.prepareStatement(ValoracionDAOQuery.GET_VALORACION_BY_LOGINID);
            // Da valor a los parámetros de la consulta
            stmt.setString(1, loginid);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            // Procesa los resultados
            if (rs.next())
            {
                Valoracion = new Valoracion();
                Valoracion.setId(rs.getString("id"));
                Valoracion.setLoginid(rs.getString("loginid"));
                Valoracion.setCasalid(rs.getString("casalid"));
                Valoracion.setValoracion(rs.getString("valoracion"));
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
        return Valoracion;
    }


    @Override
    public Valoracion getValoracionByCasalid(String casalid) throws SQLException
    {
        Valoracion Valoracion = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();


            stmt = connection.prepareStatement(ValoracionDAOQuery.GET_VALORACION_BY_CASALID);
            stmt.setString(1, casalid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                Valoracion = new Valoracion();
                Valoracion.setId(rs.getString("id"));
                Valoracion.setLoginid(rs.getString("loginid"));
                Valoracion.setCasalid(rs.getString("casalid"));
                Valoracion.setValoracion(rs.getString("valoracion"));
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
        return Valoracion;
    }

    @Override
    public ValoracionCollection getAllValoraciones() throws SQLException
    {
        ValoracionCollection ValoracionCollection = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(ValoracionDAOQuery.GET_ALL_VALORACIONES);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                Valoracion Valoracion = new Valoracion();
                Valoracion.setId(rs.getString("id"));
                Valoracion.setLoginid(rs.getString("loginid"));
                Valoracion.setCasalid(rs.getString("casalid"));
                Valoracion.setValoracion(rs.getString("valoracion"));
                ValoracionCollection.getValoracion().add(Valoracion);
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
        return ValoracionCollection;
    }

    @Override
    public ValoracionCollection getValoracionesByCasalId(String casalid) throws SQLException {
        ValoracionCollection ValoracionCollection = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(ValoracionDAOQuery.GET_VALORACION_BY_CASALID);
            stmt.setString(1, casalid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                Valoracion Valoracion = new Valoracion();
                Valoracion.setId(rs.getString("id"));
                Valoracion.setLoginid(rs.getString("loginid"));
                Valoracion.setCasalid(rs.getString("casalid"));
                Valoracion.setValoracion(rs.getString("valoracion"));
                ValoracionCollection.getValoracion().add(Valoracion);
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
        return ValoracionCollection;
    }

    @Override
    public ValoracionCollection getValoracionesByLoginid(String loginid) throws SQLException {
        ValoracionCollection ValoracionCollection = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(ValoracionDAOQuery.GET_VALORACION_BY_LOGINID);
            stmt.setString(1, loginid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                Valoracion Valoracion = new Valoracion();
                Valoracion.setId(rs.getString("id"));
                Valoracion.setLoginid(rs.getString("loginid"));
                Valoracion.setCasalid(rs.getString("casalid"));
                Valoracion.setValoracion(rs.getString("valoracion"));
                ValoracionCollection.getValoracion().add(Valoracion);
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
        return ValoracionCollection;
    }


    @Override
    public boolean deleteValoracion(String id) throws SQLException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ValoracionDAOQuery.DELETE_VALORACION);
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
