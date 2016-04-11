package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Events;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comments_EventosDAOImpl implements Comments_EventosDAO
{
    @Override
    public Comments_Events createComment(String creatorid, String eventoid, String content) throws SQLException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_EventosDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);

            stmt.close();
            stmt = connection.prepareStatement(Comments_EventosDAOQuery.CREATE_COMMENT);
            stmt.setString(1, id);
            stmt.setString(2, creatorid);
            stmt.setString(3, eventoid);
            stmt.setString(4, content);
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
        return getCommentById(id);
    }

    @Override
    public Comments_Events updateComment(String id, String content) throws SQLException {

        Comments_Events comments_events = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_EventosDAOQuery.UPDATE_COMMENT);
            stmt.setString(1,content);

            int rows = stmt.executeUpdate();
            if (rows == 1)
            {
                comments_events = getCommentById(id);
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
        return comments_events;
    }

    @Override
    public Comments_Events getCommentById(String id) throws SQLException {
        return null;
    }

    @Override
    public Comments_Events getCommentByEventoId(String eventoid) throws SQLException {
        return null;
    }

    @Override
    public Comments_Events getCommentByCreatorId(String creatorid) throws SQLException {
        return null;
    }

    @Override
    public Comments_Events getAllComments() throws SQLException {
        return null;
    }

    @Override
    public boolean deleteComment(String id) throws SQLException {
        return false;
    }
}
