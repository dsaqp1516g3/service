package edu.upc.eetac.dsaqp1516gp3.okupainfo.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

  /*    Event createEvent(String casalid, String title, String description, String localization, String latitude, String longitude)throws SQLException;
        Event updateProfile(String id, String title, String description)throws SQLException;
        Event updateLocation(String id, String localization, String latitude, String longitude)throws SQLException;
        Event getEventById(String id)throws SQLException;//de la tabla eventos
        Event getEventByCreatorId(String creatorid)throws SQLException;
        Event getEventsByUserId(String userid) throws SQLException;// Pasamos la Id del ususario y en la tabla users_events nos devuelve los eventos
        Event getAllEvents()throws SQLException;
        boolean deleteEvent(String id) throws SQLException;*/


@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoDAOImpl implements EventoDAO
{
    @Override
    public Event createEvent(String casalid, String title, String description,  String localization, Double latitude, Double longitude) throws SQLException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try
        {
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
            if (connection != null)
            {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getEventById(id);
    }

    @Override
    public Event updateProfile(String id, String title, String description) throws SQLException
    {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.UPDATE_EVENT);
            stmt.setString(1, title);
            stmt.setString(2, description);

            int rows = stmt.executeUpdate();
            if (rows == 1)
            {
                event = getEventById(id);
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
        return event;
    }


    @Override
    public Event updateLocation(String id, String localization, Double latitude, Double longitude) throws SQLException
    {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.UPDATE_LOCATION);
            stmt.setString(1, localization);
            stmt.setDouble(2, latitude);
            stmt.setDouble(3, longitude);

            int rows = stmt.executeUpdate();
            if (rows == 1)
            {
                event = getEventById(id);
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
        return event;
    }

    @Override
    public Event getEventById(String id) throws SQLException {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            // Obtiene la conexión del DataSource
            connection = Database.getConnection();

            // Prepara la consulta
            stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENT_BY_ID);
            // Da valor a los parámetros de la consulta
            stmt.setString(1, id);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            // Procesa los resultados
            if (rs.next())
            {
                event = new Event();
                event.setId(rs.getString("id"));
                event.setCasalid(rs.getString("casalid"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
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
        return event;
    }

    @Override
    public Event getEventByCreatorId(String creatorid) throws SQLException {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENT_BY_CREATOR_ID);
            stmt.setString(1, creatorid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                event = new Event();
                event.setId(rs.getString("id"));
                event.setCasalid(rs.getString("casalid"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
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
        return event;
    }

    @Override
    public Event getEventsByUserId(String userid) throws SQLException /*Mirar getUserByAuthToken porque parece ser parecido*/
    {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;

        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.GET_EVENTS_BY_USER_ID);
            stmt.setString(1, userid);

            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                event = new Event();
                event.setId(rs.getString("id"));
                event.setCasalid(rs.getString("casalid"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
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
        return event;
    }


    @Override
    public EventCollection getAllEvents() throws SQLException {
        EventCollection eventCollection = new EventCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(EventoDAOQuery.GET_ALL_EVENTS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Event events = new Event();
                events.setId(rs.getString("id"));
                events.setCasalid(rs.getString("casalid"));
                events.setTitle(rs.getString("title"));
                events.setDescription(rs.getString("description"));
                events.setLocalization(rs.getString("localization"));
                events.setLatitude(rs.getDouble("latitude"));
                events.setLongitude(rs.getDouble("longitude"));
                eventCollection.getEvents().add(events);
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
    public boolean deleteEvent(String id) throws SQLException
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        try
        {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventoDAOQuery.DELETE_EVENT);
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
