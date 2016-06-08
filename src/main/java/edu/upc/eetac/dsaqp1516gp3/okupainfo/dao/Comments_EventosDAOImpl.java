package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Events;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_EventsCollection;

import java.sql.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comments_EventosDAOImpl implements Comments_EventosDAO {
    @Override
    public Comments_Events createComment(String creatorid, String eventoid, String content) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
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
    public Comments_Events updateComment(String id, String content) throws SQLException {
        Comments_Events comments_events = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_EventosDAOQuery.UPDATE_COMMENT);
            stmt.setString(1, content);
            stmt.setString(2, id);

            int rows = stmt.executeUpdate();
            if (rows == 1) {
                comments_events = getCommentById(id);
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
        return comments_events;
    }

    @Override
    public Comments_Events getCommentById(String id) throws SQLException {
        Comments_Events comments_events = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_EventosDAOQuery.GET_COMMENT_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                comments_events = new Comments_Events();
                comments_events.setId(rs.getString("id"));
                comments_events.setCreatorid(rs.getString("creatorid"));
                comments_events.setEventoid(rs.getString("eventoid"));
                comments_events.setContent(rs.getString("content"));
                comments_events.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                comments_events.setLastModified(rs.getTimestamp("last_modified").getTime());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return comments_events;
    }

    @Override
    public Comments_EventsCollection getCommentByEventoId(String eventoid, long timestamp, boolean before) throws SQLException {
        Comments_EventsCollection Comments_EventsCollection = new Comments_EventsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            if (before) {
                stmt = connection.prepareStatement(Comments_EventosDAOQuery.GET_COMMENT_BY_EVENTID);
            } else
                stmt = connection.prepareStatement(Comments_EventosDAOQuery.GET_COMMENT_EVENTS_AFTER);
            stmt.setTimestamp(1, new Timestamp(timestamp));
            stmt.setString(2, eventoid);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;

            while (rs.next()) {
                Comments_Events Comments_Events = new Comments_Events();
                Comments_Events.setId(rs.getString("id"));
                Comments_Events.setCreatorid(rs.getString("creatorid"));
                Comments_Events.setEventoid(rs.getString("eventoid"));
                Comments_Events.setContent(rs.getString("content"));
                Comments_EventsCollection.getComments_events().add(Comments_Events);
                Comments_Events.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                Comments_Events.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    Comments_EventsCollection.setNewestTimestamp(Comments_Events.getLastModified());
                    first = false;
                }
                Comments_EventsCollection.setOldestTimestamp(Comments_Events.getLastModified());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return Comments_EventsCollection;
    }

    @Override
    public Comments_EventsCollection getCommentByCreatorId(String creatorid, long timestamp, boolean before) throws SQLException {

        Comments_EventsCollection comments_EventsCollection = new Comments_EventsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            if (before) {
                stmt = connection.prepareStatement(Comments_EventosDAOQuery.GET_COMMENT_BY_CREATORID);
                stmt.setString(1, creatorid);
            } else {
                stmt = connection.prepareStatement(Comments_EventosDAOQuery.GET_COMMENT_EVENTS_AFTER);
                stmt.setTimestamp(1, new Timestamp(timestamp));
            }


            ResultSet rs = stmt.executeQuery();
            boolean first = true;

            while (rs.next()) {
                Comments_Events Comments_Events = new Comments_Events();
                Comments_Events.setId(rs.getString("id"));
                Comments_Events.setCreatorid(rs.getString("creatorid"));
                Comments_Events.setEventoid(rs.getString("eventoid"));
                Comments_Events.setContent(rs.getString("content"));
                Comments_Events.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                Comments_Events.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    comments_EventsCollection.setNewestTimestamp(Comments_Events.getLastModified());
                    first = false;
                }
                comments_EventsCollection.setOldestTimestamp(Comments_Events.getLastModified());
                comments_EventsCollection.getComments_events().add(Comments_Events);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return comments_EventsCollection;
    }

    @Override
    public Comments_EventsCollection getAllComments(long timestamp, boolean before) throws SQLException {

        Comments_EventsCollection Comments_EventsCollection = new Comments_EventsCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            if (before) {
                stmt = connection.prepareStatement(Comments_EventosDAOQuery.GET_ALL_COMMENTS);
                System.out.println("llista COMMENTS_EVENTS servida");
            } else {
                stmt = connection.prepareStatement(Comments_EventosDAOQuery.GET_COMMENT_EVENTS_AFTER);
                stmt.setTimestamp(1, new Timestamp(timestamp));
            }

            ResultSet rs = stmt.executeQuery();
            boolean first = true;

            while (rs.next()) {
                Comments_Events Comments_Events = new Comments_Events();
                Comments_Events.setId(rs.getString("id"));
                Comments_Events.setCreatorid(rs.getString("creatorid"));
                Comments_Events.setEventoid(rs.getString("eventoid"));
                Comments_Events.setContent(rs.getString("content"));
                Comments_Events.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                Comments_Events.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    Comments_EventsCollection.setNewestTimestamp(Comments_Events.getLastModified());
                    first = false;
                }
                Comments_EventsCollection.setOldestTimestamp(Comments_Events.getLastModified());
                Comments_EventsCollection.getComments_events().add(Comments_Events);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return Comments_EventsCollection;
    }


    @Override
    public boolean deleteComment(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(Comments_EventosDAOQuery.DELETE_COMMENT);
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
