package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.EventoDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.EventoDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;

import javax.ws.rs.*;
import java.sql.SQLException;

/**
 * Created by Hermione on 27/04/2016.
 */

@Path("events")
public class EventResource
{
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)
    public EventCollection getAllEvents(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before)
    {
        EventCollection EventCollection;
        EventoDAO EventoDAO = new EventoDAOImpl();
        try
        {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            EventCollection = EventoDAO.getAllEvents(timestamp, before);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return EventCollection;
    }
}
