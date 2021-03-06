package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Casals;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_CasalsCollection;

import java.sql.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comments_CasalsDAOImpl implements Comments_CasalsDAO {
    @Override
    public Comments_Casals createComment(String creatorid, String casalid, String content) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
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
        return getCommentById(id);
    }

    @Override
    public Comments_Casals updateComment(String id, String content) throws SQLException {
        Comments_Casals comments_casals = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_CasalsDAOQuery.UPDATE_COMMENT);
            stmt.setString(1, content);

            int rows = stmt.executeUpdate();
            if (rows == 1) {
                comments_casals = getCommentById(id);
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
        return comments_casals;
    }

    @Override
    public Comments_Casals getCommentById(String id) throws SQLException {
        Comments_Casals comments_casals = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                comments_casals = new Comments_Casals();
                comments_casals.setId(rs.getString("id"));
                comments_casals.setCreatorid(rs.getString("creatorid"));
                comments_casals.setCasalid(rs.getString("casalid"));
                comments_casals.setContent(rs.getString("content"));
                comments_casals.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                comments_casals.setLastModified(rs.getTimestamp("last_modified"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return comments_casals;
    }

    @Override
    public Comments_CasalsCollection getCommentsByCasalId(String casalid, long timestamp, boolean before) throws SQLException {
        Comments_CasalsCollection comments_CasalsCollection = new Comments_CasalsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            if (before) {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENTS_BY_CASALID);
                stmt.setString(1, casalid);
            } else {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_CASALS_AFTER);
                stmt.setTimestamp(2, new Timestamp(timestamp));
            }

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Comments_Casals comments_Casals = new Comments_Casals();
                comments_Casals.setId(rs.getString("id"));
                comments_Casals.setCreatorid(rs.getString("creatorid"));
                comments_Casals.setCasalid(rs.getString("casalid"));
                comments_Casals.setContent(rs.getString("content"));
                comments_Casals.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                comments_Casals.setLastModified(rs.getTimestamp("last_modified"));

                if (first) {
                    comments_CasalsCollection.setNewestTimestamp(comments_Casals.getLastModified().getTime());
                    first = false;
                }
                comments_CasalsCollection.setOldestTimestamp(comments_Casals.getLastModified().getTime());
                comments_CasalsCollection.getComments_casals().add(comments_Casals);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return comments_CasalsCollection;
    }

    @Override
    public Comments_CasalsCollection getAllComments(long timestamp, boolean before) throws SQLException {

        Comments_CasalsCollection comments_CasalsCollection = new Comments_CasalsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            if (before) {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_ALL_COMMENTS);
            } else {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_CASALS_AFTER);
                stmt.setTimestamp(1, new Timestamp(timestamp));
            }

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Comments_Casals comments_Casals = new Comments_Casals();
                comments_Casals.setId(rs.getString("id"));
                comments_Casals.setCreatorid(rs.getString("creatorid"));
                comments_Casals.setCasalid(rs.getString("casalid"));
                comments_Casals.setContent(rs.getString("content"));
                comments_Casals.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                comments_Casals.setLastModified(rs.getTimestamp("last_modified"));

                if (first) {
                    comments_CasalsCollection.setNewestTimestamp(comments_Casals.getLastModified().getTime());
                    first = false;
                }
                comments_CasalsCollection.setOldestTimestamp(comments_Casals.getLastModified().getTime());
                comments_CasalsCollection.getComments_casals().add(comments_Casals);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return comments_CasalsCollection;
    }

    @Override
    public Comments_CasalsCollection getCommentByCreatorId(String creatorid, long timestamp, boolean before) throws SQLException {
        Comments_CasalsCollection comments_CasalsCollection = new Comments_CasalsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            if (before) {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_BY_CREATORID);
                stmt.setString(1, creatorid);

            } else {
                stmt = connection.prepareStatement(Comments_CasalsDAOQuery.GET_COMMENT_CASALS_AFTER);
                stmt.setTimestamp(3, new Timestamp(timestamp));
            }

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Comments_Casals comments_Casals = new Comments_Casals();
                comments_Casals.setId(rs.getString("id"));
                comments_Casals.setCreatorid(rs.getString("creatorid"));
                comments_Casals.setCasalid(rs.getString("casalid"));
                comments_Casals.setContent(rs.getString("content"));
                comments_Casals.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                comments_Casals.setLastModified(rs.getTimestamp("last_modified"));

                if (first) {
                    comments_CasalsCollection.setNewestTimestamp(comments_Casals.getLastModified().getTime());
                    first = false;
                }
                comments_CasalsCollection.setOldestTimestamp(comments_Casals.getLastModified().getTime());
                comments_CasalsCollection.getComments_casals().add(comments_Casals);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return comments_CasalsCollection;
    }

    @Override
    public boolean deleteComment(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_CasalsDAOQuery.DELETE_COMMENT);
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
