package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Casals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comments_CasalsDAOImpl implements Comments_CasalsDAO
{
    @Override
    public Comments_Casals createComment(String creatorid, String casalid, String content) throws SQLException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_CasalsDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);

            stmt.close();
            stmt = connection.prepareStatement(Comments_CasalsDAOQuery.CREATE_COMMENT);
            stmt.setString(1, id);
            stmt.setString(2, creatorid);
            stmt.setString(3, casalid);
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
    public Comments_Casals updateComment(String id,String creatorid, String content) throws SQLException
    {
        Comments_Casals comments_casals = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_CasalsDAOQuery.UPDATE_COMMENT);
            stmt.setString(1,content);

            int rows = stmt.executeUpdate();
            if (rows == 1)
            {
                comments_casals = getCommentById(id);
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
        return comments_casals;
    }

    @Override
    public Comments_Casals getCommentById(String id) throws SQLException
    {
        Comments_Casals comments_casals = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_BY_ID);
            stmt.setString(1,id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                comments_casals = new Comments_Casals();
                comments_casals.setId(rs.getString("id"));
                comments_casals.setCreatorid(rs.getString("creatorid"));
                comments_casals.setCasalid(rs.getString("casalid"));
                comments_casals.setContent(rs.getString("content"));
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
        return comments_casals;
    }

    @Override
    public Comments_Casals getCommentByCasalId(String casalid) throws SQLException
    {
        Comments_Casals comments_casals = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_BY_CASALID);
            stmt.setString(1,casalid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                comments_casals = new Comments_Casals();
                comments_casals.setId(rs.getString("id"));
                comments_casals.setCreatorid(rs.getString("creatorid"));
                comments_casals.setCasalid(rs.getString("casalid"));
                comments_casals.setContent(rs.getString("content"));
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
        return comments_casals;
    }

    @Override
    public Comments_Casals getCommentByCreatorId(String creatorid) throws SQLException
    {
        Comments_Casals comments_casals = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_BY_CREATORID);
            stmt.setString(1,creatorid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                comments_casals = new Comments_Casals();
                comments_casals.setId(rs.getString("id"));
                comments_casals.setCreatorid(rs.getString("creatorid"));
                comments_casals.setCasalid(rs.getString("casalid"));
                comments_casals.setContent(rs.getString("content"));
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
        return comments_casals;
    }

    @Override
    public Comments_Casals getAllComments() throws SQLException
    {
        return null;
    }

    @Override
    public boolean deleteComment(String id) throws SQLException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_CasalsDAOQuery.DELETE_COMMENT);
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
