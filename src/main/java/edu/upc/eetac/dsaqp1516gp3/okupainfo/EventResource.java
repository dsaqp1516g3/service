package edu.upc.eetac.dsaqp1516gp3.okupainfo;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.EventoDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.EventoDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
@Path("eventos")
public class EventResource
{
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createEvent(@FormParam("casalid") String casalid, @FormParam("title") String title, @FormParam("description") String description, @FormParam("localization") String localization, @FormParam("latitude") Double latitude, @FormParam("longitude") Double longitude, @Context UriInfo uriInfo) throws URISyntaxException
    {
        if (casalid == null || title == null || description == null || localization == null || latitude == null || longitude == null)
            throw new BadRequestException("all parameters are mandatory");
        EventoDAO EventoDAO = new EventoDAOImpl();
        Event Event;
        AuthToken authenticationToken;
        try
        {
/*Aqui atacamos a la API externa para obtener las longitudes y latitudes*/
            Event = EventoDAO.createEvent(casalid, title, description, localization, latitude, longitude);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(Event.getId());
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Event.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN).entity(authenticationToken).build();
    }

    @Path("/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    public Event getEventById(@PathParam("id") String id)
    {
        Event Event;
        try
        {
            Event = (new EventoDAOImpl().getEventById(id));
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (Event == null)
            throw new NotFoundException("Event with id = " + id + "doesn't exist");
        return Event;
    }

    @Path("/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)//No coge bien los parametros pasados?????
    public Event getEventByCreatorId(@PathParam("casalid") String casalid)
    {
        Event Event;
        try
        {
            Event = (new EventoDAOImpl().getEventByCreatorId(casalid));
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (Event == null)
            throw new NotFoundException("Event with creatorid = " + casalid + "doesn't exist");
        return Event;
    }
    @Path("/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)


    public Event getEventsByUserId(@PathParam("userid") String userid)
    {
// Hay que hacer un return de una colección de Eventos
    }

    @Path("/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS) //No coge bien los parametros pasados?????
    public Event updateProfile(@PathParam("id") String id, @PathParam("title") String title, @PathParam("description") String description, Event event) {
        if (event == null)
            throw new BadRequestException("entity is null");
        if (!id.equals(event.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(event.getCasalid()))
            throw new ForbiddenException("operation not allowed");
        EventoDAO EventoDAO = new EventoDAOImpl();
        try
        {
            event = EventoDAO.updateProfile(id, title, description);
            if (event == null)
                throw new NotFoundException("El evento con la id " + id + " no existe");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return event;
    }

    @Path("/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)//No coge bien los parametros pasados?????
    public Event updateLocation(@PathParam("id") String id, @PathParam("localization") String localization, @PathParam("latitude") double latitude, @PathParam("longitude") double longitude, Event event) {
        if (event == null)
            throw new BadRequestException("entity is null");
        if (!id.equals(event.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(event.getCasalid()))
            throw new ForbiddenException("operation not allowed");
        EventoDAO eventoDAO = new EventoDAOImpl();
        try
        {
            event = eventoDAO.updateLocation(id, localization, latitude, longitude);
            if (event == null)
                throw new NotFoundException("El evento con la id " + id + " no existe");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return event;
    }

    @Path("({id}")
    @RolesAllowed("[admin, casal]")
    @DELETE
    public void deleteEvent(@PathParam("id") String id)
    {
        EventoDAO EventoDAO = new EventoDAOImpl();
        try
        {
            if (!EventoDAO.deleteEvent(id))
                throw new NotFoundException("Casal with id = " + id + " doesn't exist");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }
    
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