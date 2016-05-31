package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Casals;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_CasalsCollection;

import java.sql.*;

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
    public Comments_CasalsCollection getCommentByCasalId(String casalid, long timestamp, boolean before) throws SQLException
    {
        Comments_CasalsCollection Comments_CasalsCollection = new Comments_CasalsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();
            if(before)
            {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_BY_CASALID);
            }
            else
            {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_CASALS_AFTER);
            }


            stmt.setString(1, casalid);
            stmt.setTimestamp(2, new Timestamp(timestamp));

            ResultSet rs = stmt.executeQuery();
            boolean first = true;

            while(rs.next())
            {
                Comments_Casals Comments_Casals = new Comments_Casals();
                Comments_Casals.setId(rs.getString("id"));
                Comments_Casals.setCreatorid(rs.getString("creatorid"));
                Comments_Casals.setCasalid(rs.getString("casalid"));
                Comments_Casals.setContent(rs.getString("content"));
                Comments_CasalsCollection.getComments_casals().add(Comments_Casals);
                Comments_Casals.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                Comments_Casals.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first)
                {
                    Comments_CasalsCollection.setNewestTimestamp(Comments_Casals.getLastModified());
                    first = false;
                }
                Comments_CasalsCollection.setOldestTimestamp(Comments_Casals.getLastModified());
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
        return Comments_CasalsCollection;
    }

    @Override
    public Comments_CasalsCollection getCommentByCreatorId(String creatorid, long timestamp, boolean before) throws SQLException
    {
        Comments_CasalsCollection Comments_CasalsCollection = new Comments_CasalsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();
            if(before)
            {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_BY_CREATORID);
            }
            else
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_CASALS_AFTER);
            stmt.setTimestamp(1, new Timestamp(timestamp));
            stmt.setString(2, creatorid);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;

            while(rs.next())
            {
                Comments_Casals Comments_Casals = new Comments_Casals();
                Comments_Casals.setId(rs.getString("id"));
                Comments_Casals.setCreatorid(rs.getString("creatorid"));
                Comments_Casals.setCasalid(rs.getString("casalid"));
                Comments_Casals.setContent(rs.getString("content"));
                Comments_CasalsCollection.getComments_casals().add(Comments_Casals);
                Comments_Casals.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                Comments_Casals.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first)
                {
                    Comments_CasalsCollection.setNewestTimestamp(Comments_Casals.getLastModified());
                    first = false;
                }
                Comments_CasalsCollection.setOldestTimestamp(Comments_Casals.getLastModified());
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
        return Comments_CasalsCollection;
    }

    @Override
    public Comments_CasalsCollection getAllComments(long timestamp, boolean before) throws SQLException
    {

        Comments_CasalsCollection comments_CasalsCollection = new Comments_CasalsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();
            if(before)
            {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_ALL_COMMENTS);
                System.out.println("llista COMMENTS_CASALS servida");
            }
            else
            {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_CASALS_AFTER);
                stmt.setTimestamp(1, new Timestamp(timestamp));
            }

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            if (rs.next())
            {
                Comments_Casals comments_Casals = new Comments_Casals();
                comments_Casals.setId(rs.getString("id"));
                comments_Casals.setCreatorid(rs.getString("creatorid"));
                comments_Casals.setCasalid(rs.getString("casalid"));
                comments_Casals.setContent(rs.getString("content"));
                comments_Casals.setCreationTimestamp(rs.getLong("creation_timestamp"));

                if (first)
                {
                    comments_CasalsCollection.setNewestTimestamp(comments_Casals.getLastModified());
                    first = false;
                }
                comments_CasalsCollection.setOldestTimestamp(comments_Casals.getLastModified());
                comments_CasalsCollection.getComments_casals().add(comments_Casals);
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
        return comments_CasalsCollection;
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
