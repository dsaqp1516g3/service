package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.*;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_EventsCollection;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Hermione on 27/04/2016.
 */

@Path("events")
public class EventResource {
    /**
     * Obtenemos una lista de todos los eventos
     **/
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)
    public EventCollection getAllEvents(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        EventCollection EventCollection;
        EventoDAO EventoDAO = new EventoDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            EventCollection = EventoDAO.getAllEvents(timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return EventCollection;
    }

    /**
     * Comprobamos a que eventos asiste el usuario
     **/
    @RolesAllowed("[admin, registered]")
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

    /**
     * Añadimos la asistencia del usuario al evento
     **/
    @RolesAllowed("[admin, registered]")
    @Path("/{eventid}/{userid}")
    @POST
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)//Miramos la asistencia de un usuario a varios eventos
    public boolean addAssistanceToEvent(@PathParam("userid") String userid, @PathParam("eventid") String eventid, @Context UriInfo uriInfo) throws URISyntaxException {
        if (userid == null || eventid == null)
            throw new BadRequestException("all parameters are mandatory");
        EventoDAO eventoDAO = new EventoDAOImpl();

        try {
            eventoDAO.addUserAssistance(userid, eventid);
            return true;
        } catch (UserAlreadyAssists e) {
            throw new WebApplicationException("The user " + userid + " has already confirmed assistance to " + eventid, Response.Status.CONFLICT);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Eliminamos la asistencia del usuario al evento
     **/
    @RolesAllowed("[admin, registered]")
    @Path("/{eventid}/{userid}")
    @DELETE
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    public void deleteAssistanceToEvent(@PathParam("userid") String userid, @PathParam("eventid") String eventid) {
        EventoDAO eventoDAO = new EventoDAOImpl();

        try {
            if (!eventoDAO.deleteAssistanceEvent(userid, eventid)) ;
            throw new NotFoundException("Event with id = " + eventid + " doesn't exist or user with id = " + userid + " doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Comprobamos la asistencia del usuario al evento
     **/
    @Path("{eventid}/{userid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    public boolean checkAssitanceToEvent(@PathParam("userid") String userid, @PathParam("eventid") String eventid) {
        EventoDAO eventoDAO = new EventoDAOImpl();
        boolean assistance = false;
        try {
            assistance = eventoDAO.checkAssistanceToEvent(userid, eventid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assistance;
    }

    /**
     * Obtenemos todos los commentarios que ha hecho un usuarios acerca de todos los eventos
     **/
    @Path("/comments/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_EventsCollection getCommentEventByCreatorId(@PathParam("creatorid") String creatorid, @QueryParam("timestamp") long timestamp,
                                                                @DefaultValue("true") @QueryParam("before") boolean before) {

        Comments_EventsCollection Comments_EventsCollection;
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_EventsCollection = Comments_EventosDAO.getCommentByCreatorId(creatorid, timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_EventsCollection;
    }
}
