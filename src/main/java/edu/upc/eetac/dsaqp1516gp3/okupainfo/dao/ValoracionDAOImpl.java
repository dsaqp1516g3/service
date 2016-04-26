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




}
