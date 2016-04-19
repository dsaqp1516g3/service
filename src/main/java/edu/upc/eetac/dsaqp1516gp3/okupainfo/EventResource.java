package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.EventAlreadyExistsException;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.EventoDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.EventoDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Event;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.EventCollection;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;


@Path("eventos")
public class EventResource
{

    //Event createEvent(String casalid, String title, String description, String localization, Double latitude, Double longitude)throws SQLException;
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createEvent(@FormParam("casalid") String casalid, @FormParam("title") String title, @FormParam("description") String description,
                                @FormParam("localization") String localization, @FormParam("latitude") Double latitude, @FormParam("longitude") Double longitude,  @Context UriInfo uriInfo) throws URISyntaxException
    {
        if(casalid == null ||  title == null || description == null || localization == null || latitude == null || longitude == null)
            throw new BadRequestException("all parameters are mandatory");
       EventoDAO EventoDAO = new EventoDAOImpl();
        Event Event;
        AuthToken authenticationToken;
        try
        {
            /*Aqui atacamos a la API externa para obtener las longitudes y latitudes*/
            Event = EventoDAO.createEvent(casalid, title, description, localization, latitude, longitude);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(Event.getId());
        } catch(SQLException e)
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
            throw  new NotFoundException("Event with id = " + id + "doesn't exist");
        return Event;
    }


    @Path("/{creatorid}")
     @GET
     @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
     public Event getEventByCreatorId(@PathParam("creatorid") String creatorid)
{
    Event Event;
    try
    {
        Event = (new EventoDAOImpl().getEventByCreatorId(creatorid));
    }
    catch (SQLException e)
    {
        throw new InternalServerErrorException(e.getMessage());
    }
    if (Event == null)
        throw  new NotFoundException("Event with creatorid = " + creatorid + "doesn't exist");
    return Event;
}



    @Path("/{userid}")
     @GET
     @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
     public Event getEventsByUserId(@PathParam("userid") String userid)
{
    Event Event;
    try
    {
        Event = (new EventoDAOImpl().getEventsByUserId(userid));
    }
    catch (SQLException e)
    {
        throw new InternalServerErrorException(e.getMessage());
    }
    if (Event == null)
        throw  new NotFoundException("Event with userid = " + userid + "doesn't exist");
    return Event;
}

    //Event updateProfile(String id, String title, String description)throws SQLException;

    @Path("/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    public Event updateProfile(@PathParam("id") String id, @PathParam("title") String title,  @PathParam("description") String description, Event Event) {
        if(Event == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(Event.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        EventoDAO EventoDAO = new EventoDAOImpl();
        try {
            Event = EventoDAO.updateProfile(id, Event.getTitle(), Event.getDescription());
            if(Event == null)
                throw new NotFoundException("El evento con la id "+id+" no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Event;
    }

    //Event updateLocation(String id, String localization, Double latitude, Double longitude)throws SQLException;
    @Path("/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    public Event updateLocation(@PathParam("id") String id, @PathParam("localization") String localization, @PathParam("latitude") Double latitude, @PathParam("longitude") Double longitude, Event Event) {
        if(Event == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(Event.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        EventoDAO EventoDAO = new EventoDAOImpl();
        try {
            Event = EventoDAO.updateLocation(id, Event.getLocalization(), Event.getLatitude(), Event.getLongitude());
            if(Event == null)
                throw new NotFoundException("El evento con la id "+id+" no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Event;
    }



    @Path("({id}")
    @RolesAllowed("[admin, casal]")
    @DELETE
    public void deleteEvent(@PathParam("id") String id)
    {

        EventoDAO EventoDAO = new EventoDAOImpl();
        try
        {
            if(!EventoDAO.deleteEvent(id))
                throw new NotFoundException("Casal with id = " + id + " doesn't exist");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public EventCollection getAllEvents() {
        EventCollection EventCollection = null;
        EventoDAO EventoDAO = new EventoDAOImpl();
        try {
            EventCollection = EventoDAO.getAllEvents();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return EventCollection;
    }
}
