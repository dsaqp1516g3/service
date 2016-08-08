package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;

import javax.imageio.ImageIO;
import javax.ws.rs.InternalServerErrorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoDAOImpl implements EventoDAO {
    @Override
    public Event createEvent(String casalid, String title, String description, String localization, double latitude, double longitude, long eventdate, InputStream image) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String uuid = writeAndConvertImage(image);
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
            stmt.setDouble(6, latitude);
            stmt.setDouble(7, longitude);
            stmt.setTimestamp(8, new Timestamp(eventdate * 1000));
            stmt.setString(9, uuid);
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

    private String writeAndConvertImage(InputStream file){
        BufferedImage image;
        try{
            image = ImageIO.read(file);
        }catch(IOException E){
            throw new InternalServerErrorException("error");
        }
        if(image==null){
            return null;
        }
        else {
            UUID uuid = UUID.randomUUID();
            String filename = uuid.toString() + ".png";
            try {
                PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("okupainfo");

                ImageIO.write(image, "png", new File(prb.getString("uploadFolder") + filename));

            } catch (IOException e) {
                throw new InternalServerErrorException("error");
            }
            String respuesta = uuid.toString();
            return respuesta;
        }
    }

    @Override
    public Event updateProfile(String id, String title, String description, Timestamp eventdate, String localization, double latitude, double longitude) throws SQLException {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.UPDATE_EVENT);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setTimestamp(3, eventdate);
            stmt.setString(4, localization);
            stmt.setDouble(5, latitude);
            stmt.setDouble(6, longitude);
            stmt.setString(7, id);

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
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENT_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                event = new Event();
                event.setId(rs.getString("id"));
                event.setCasalid(rs.getString("casalid"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setLocalization(rs.getString("localization"));
                event.setLatitude(rs.getDouble("latitude"));
                event.setLongitude(rs.getDouble("longitude"));
                event.setEventdate(rs.getTimestamp("eventdate"));
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                event.setLastModified(rs.getTimestamp("last_modified"));
                event.setImage(rs.getString("image"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return event;
    }

    @Override
    public void addUserAssistance(String userid, String eventid) throws SQLException, UserAlreadyAssists {
        PreparedStatement stmt = null;
        Connection connection = null;

        try {

            /*EventCollection eventCollection;
            eventCollection = getEventsByUserId(userid, 0, true);
            if (eventCollection != null) {
                throw new UserAlreadyAssists();
            }*/

            connection = Database.getConnection();

            connection.setAutoCommit(false);

            stmt = connection.prepareStatement(EventoDAOQuery.ADD_ASSISTANCE_TO_EVENT);
            stmt.setString(1, userid);
            stmt.setString(2, eventid);
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
    }

    @Override
    public EventCollection getEventsByCasalId(String casalid, long timestamp, boolean before) throws SQLException {

        EventCollection eventCollection = new EventCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            if (before) {
                stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENTS_BY_CASAL_ID);
                stmt.setString(1, casalid);
            } else {
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
                event.setEventdate(rs.getTimestamp("eventdate"));
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                event.setLastModified(rs.getTimestamp("last_modified"));
                event.setImage(rs.getString("image"));
                if (first) {
                    eventCollection.setNewestTimestamp(event.getLastModified().getTime());
                    first = false;
                }
                eventCollection.setOldestTimestamp(event.getLastModified().getTime());
                eventCollection.getEvents().add(event);
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
                stmt.setString(1, userid);
            } else {
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
                event.setEventdate(rs.getTimestamp("eventdate"));
                event.setLocalization(rs.getString("localization"));
                event.setLatitude(rs.getDouble("latitude"));
                event.setLongitude(rs.getDouble("longitude"));
                event.setImage(rs.getString("image"));
                eventCollection.getEvents().add(event);
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                event.setLastModified(rs.getTimestamp("last_modified"));
                if (first) {
                    eventCollection.setNewestTimestamp(event.getLastModified().getTime());
                    first = false;
                }
                eventCollection.setOldestTimestamp(event.getLastModified().getTime());
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
            } else {
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
                event.setEventdate(rs.getTimestamp("eventdate"));
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
                event.setLastModified(rs.getTimestamp("last_modified"));
                event.setImage(rs.getString("image"));
                if (first) {
                    eventCollection.setNewestTimestamp(event.getLastModified().getTime());
                    first = false;
                }
                eventCollection.setOldestTimestamp(event.getLastModified().getTime());
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
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public boolean checkAssistanceToEvent(String userid, String eventid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.CHECK_ASSISTANCE_TO_EVENT);
            stmt.setString(1, userid);
            stmt.setString(2, eventid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            } else
                return false;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public boolean checkEventsOfCasal(String casalid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENTS_BY_CASAL_ID);
            stmt.setString(1, casalid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            } else
                return false;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }
}
