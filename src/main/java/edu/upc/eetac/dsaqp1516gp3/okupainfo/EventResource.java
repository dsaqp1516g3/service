package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.Comments_EventosDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.Comments_EventosDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.EventoDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.EventoDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_EventsCollection;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Hermione on 27/04/2016.
 */

@Path("events")
public class EventResource
{
    /**Obtenemos una lista de todos los eventos**/
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

    /**Comprobamos a que eventos asiste el usuario**/
    @Path("assistance/{userid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)//Miramos la asistencia de un usuario a varios eventos
    public EventCollection getEventsByUserId(@PathParam("userid") String userid, @QueryParam("timestamp") long timestamp, @DefaultValue("true")
    @QueryParam("before") boolean before, @Context Request request) {
        EventCollection eventCollection;
        EventoDAO eventoDAO = new EventoDAOImpl();

        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            eventCollection = eventoDAO.getEventsByUserId(userid, timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return eventCollection;
    }

    /**Añadimos la asistencia del usuario al evento**/
    @Path("assistance/{userid}")
    @POST
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)//Miramos la asistencia de un usuario a varios eventos
    public void addAssistanceToEvent(@FormParam("userid") String userid, @FormParam("eventid") String eventid, @Context UriInfo uriInfo) throws URISyntaxException {
        if (userid == null || eventid == null)
            throw new BadRequestException("all parameters are mandatory");
        EventoDAO eventoDAO = new EventoDAOImpl();

        try {
            eventoDAO.addUserAssistance(userid, eventid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**Eliminamos la asistencia del usuario al evento**/
    @Path("{eventid}/{userid}")
    @DELETE
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)//Miramos la asistencia de un usuario a varios eventos
    public void deleteAssistanceToEvent(@PathParam("userid") String userid, @PathParam("eventid") String eventid) {
        EventoDAO eventoDAO = new EventoDAOImpl();

        try {
            if (!eventoDAO.deleteAssistanceEvent(userid, eventid)) ;
            throw new NotFoundException("Event with id = " + eventid + " doesn't exist or user with id = " + userid + " doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**Comprobamos la asistencia del usuario al evento**/
    @Path("{eventid}/{userid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)//Miramos la asistencia de un usuario a varios eventos
    public boolean checkAssitanceToEvent(@PathParam("userid") String userid) {
        return  true;
    }

    /**Obtenemos todos los commentarios que ha hecho un usuarios acerca de todos los eventos**/
    @Path("/comments/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_EventsCollection getCommentEventByCreatorId(@PathParam("creatorid") String creatorid, @QueryParam("timestamp") long timestamp,
                                                                @DefaultValue("true") @QueryParam("before") boolean before)
    {

        Comments_EventsCollection Comments_EventsCollection;
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try
        {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_EventsCollection = Comments_EventosDAO.getCommentByCreatorId(creatorid, timestamp, before);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Comments_EventsCollection;
    }
}
