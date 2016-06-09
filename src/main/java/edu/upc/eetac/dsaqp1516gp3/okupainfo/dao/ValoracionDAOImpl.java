package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Valoracion;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.ValoracionCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValoracionDAOImpl implements ValoracionDAO {

    @Override
    public Valoracion createValoracion(String loginid, String casalid, boolean valoracion) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
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
        return getValoracionById(id);
    }

    @Override
    public Valoracion updateValoracion(String id, String loginid, String casalid, boolean valoracion) throws SQLException {
        Valoracion Valoracion = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ValoracionDAOQuery.UPDATE_VALORACION);
            stmt.setString(1, loginid);
            stmt.setString(2, casalid);
            stmt.setBoolean(3, valoracion);

            int rows = stmt.executeUpdate();
            if (rows == 1) {
                Valoracion = getValoracionById(id);
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
        return Valoracion;
    }

    @Override
    public Valoracion getValoracionById(String id) throws SQLException {
        Valoracion Valoracion = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ValoracionDAOQuery.GET_VALORACION_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Valoracion = new Valoracion();
                Valoracion.setId(rs.getString("id"));
                Valoracion.setLoginid(rs.getString("loginid"));
                Valoracion.setCasalid(rs.getString("casalid"));
                Valoracion.setValoracion(rs.getString("valoracion"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            // Libera la conexión
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return Valoracion;
    }

    @Override
    public ValoracionCollection getValoracionesByCasalId(String casalid) throws SQLException {
        ValoracionCollection valoracionCollection = new ValoracionCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(ValoracionDAOQuery.GET_VALORACION_BY_CASALID);
            stmt.setString(1, casalid);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Valoracion valoracion = new Valoracion();
                valoracion.setId(rs.getString("id"));
                valoracion.setLoginid(rs.getString("loginid"));
                valoracion.setCasalid(rs.getString("casalid"));
                valoracion.setValoracion(rs.getString("valoracion"));
                valoracionCollection.getValoraciones().add(valoracion);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return valoracionCollection;
    }

    @Override
    public ValoracionCollection getValoracionesByUserId(String userid) throws SQLException {
        ValoracionCollection valoracionCollection = new ValoracionCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(ValoracionDAOQuery.GET_VALORACION_BY_USERID);
            stmt.setString(1, userid);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Valoracion valoracion = new Valoracion();
                valoracion.setId(rs.getString("id"));
                valoracion.setLoginid(rs.getString("loginid"));
                valoracion.setCasalid(rs.getString("casalid"));
                valoracion.setValoracion(rs.getString("valoracion"));
                valoracionCollection.getValoraciones().add(valoracion);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return valoracionCollection;
    }

    @Override
    public boolean deleteValoracion(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ValoracionDAOQuery.DELETE_VALORACION);
            stmt.setString(1, id);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }
}
