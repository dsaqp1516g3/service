package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;

import java.sql.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoDAOImpl implements EventoDAO {
    @Override
    public Event createEvent(String casalid, String title, String description, String localization, double latitude, double longitude, double eventdate) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);

            stmt.close();
            stmt = connection.prepareStatement(EventoDAOQuery.CREATE_EVENT);
            stmt.setString(1, id);
            stmt.setString(2, casalid);
            stmt.setString(3, title);
            stmt.setString(4, description);
            stmt.setString(5, localization);
            stmt.setString(6, String.valueOf(latitude));
            stmt.setString(7, String.valueOf(longitude));
            stmt.setString(5, String.valueOf(eventdate));
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
        return getEventById(id);
    }

    @Override
    public Event updateProfile(String id, String title, String description, double eventdate, String localization, double latitude, double longitude) throws SQLException {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.UPDATE_EVENT);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, String.valueOf(eventdate));
            stmt.setString(4, localization);
            stmt.setDouble(5, latitude);
            stmt.setDouble(6, longitude);

            int rows = stmt.executeUpdate();
            if (rows == 1) {
                event = getEventById(id);
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
        return event;
    }

    @Override
    public Event getEventById(String id) throws SQLException {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            // Obtiene la conexión del DataSource
            connection = Database.getConnection();

            // Prepara la consulta
            stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENT_BY_ID);
            // Da valor a los parámetros de la consulta
            stmt.setString(1, id);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            // Procesa los resultados
            if (rs.next()) {
                event = new Event();
                event.setId(rs.getString("id"));
                event.setCasalid(rs.getString("casalid"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setEventdate(rs.getLong("eventdate"));
            }
        } catch (SQLException e) {
            // Relanza la excepción
            throw e;
        } finally {
            // Libera la conexión
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        // Devuelve el modelo
        return event;
    }

    @Override
    public void addUserAssistance(String userid, String eventid) throws SQLException {
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = Database.getConnection();

            connection.setAutoCommit(false);
            stmt.close();
            stmt = connection.prepareStatement(EventoDAOQuery.ADD_ASSISTANCE_TO_EVENT);
            stmt.setString(1, userid);
            stmt.setString(2, eventid);
            stmt.executeUpdate();
            connection.commit();

        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    @Override
    public EventCollection getEventsByCreatorId(String casalid, long timestamp, boolean before) throws SQLException {

        EventCollection eventCollection = new EventCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            if (before) {
                stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENT_BY_CREATOR_ID);
            } else
                stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENTS_AFTER);
            stmt.setTimestamp(1, new Timestamp(timestamp));
            stmt.setString(2, casalid);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            if (rs.next()) {
                Event event = new Event();
                event.setId(rs.getString("id"));
                event.setCasalid(rs.getString("casalid"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setEventdate(rs.getLong("eventdate"));
                eventCollection.getEvents().add(event);
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                event.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    eventCollection.setNewestTimestamp(event.getLastModified());
                    first = false;
                }
                eventCollection.setOldestTimestamp(event.getLastModified());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return eventCollection;
    }

    @Override
    public EventCollection getEventsByUserId(String userid, long timestamp, boolean before) throws SQLException {
        EventCollection eventCollection = new EventCollection();

        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Database.getConnection();
            if (before) {
                stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENTS_BY_USER_ID);
            } else
                stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENTS_AFTER);
            stmt.setTimestamp(1, new Timestamp(timestamp));
            stmt.setString(2, userid);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getString("id"));
                event.setCasalid(rs.getString("casalid"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setEventdate(rs.getLong("eventdate"));
                eventCollection.getEvents().add(event);
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                event.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    eventCollection.setNewestTimestamp(event.getLastModified());
                    first = false;
                }
                eventCollection.setOldestTimestamp(event.getLastModified());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return eventCollection;
    }

    @Override
    public EventCollection getAllEvents(long timestamp, boolean before) throws SQLException {
        EventCollection eventCollection = new EventCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            if (before) {
                stmt = connection.prepareStatement(EventoDAOQuery.GET_ALL_EVENTS);
                System.out.println("llista EVENTS servida");
            }
            else {
                stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENTS_AFTER);
                stmt.setTimestamp(1, new Timestamp(timestamp));
            }

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getString("id"));
                event.setCasalid(rs.getString("casalid"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setLocalization(rs.getString("localization"));
                event.setLatitude(rs.getDouble("latitude"));
                event.setLongitude(rs.getDouble("longitude"));
                event.setEventdate(rs.getLong("eventdate"));
                if (first) {
                    eventCollection.setNewestTimestamp(event.getLastModified());
                    first = false;
                }
                eventCollection.setOldestTimestamp(event.getLastModified());
                eventCollection.getEvents().add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return eventCollection;
    }

    @Override
    public boolean deleteEvent(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.DELETE_EVENT);
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

    @Override
    public boolean deleteAssistanceEvent(String userid, String eventid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.DELETE_ASSITANCE_TO_EVENT);
            stmt.setString(1, userid);
            stmt.setString(2, eventid);

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