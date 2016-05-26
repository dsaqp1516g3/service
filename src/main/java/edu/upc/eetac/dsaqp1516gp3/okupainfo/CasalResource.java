package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.*;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static javax.ws.rs.core.HttpHeaders.USER_AGENT;

@Path("casals")
public class CasalResource
{
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createCasal(@FormParam("adminid") String adminid, @FormParam("email") String email, @FormParam("name") String name, @FormParam("description") String description,
                                @FormParam("localization") String localization, @FormParam("latitude") Double latitude, @FormParam("longitude") Double longitude,
                                @FormParam("validated") boolean validated, @Context UriInfo uriInfo) throws URISyntaxException
    {

        if (adminid == null || email == null || name == null || description == null || localization == null || latitude == null || longitude == null)
            throw new BadRequestException("all parameters are mandatory");
        CasalDAO casalDAO = new CasalDAOImpl();
        Casal casal = null;
        AuthToken authenticationToken = null;
        try
        {
            /**Aqui atacamos a la API externa para obtener las longitudes y latitudes**/
            StringBuffer coordinates = sendGet(localization);
            /**Asignamos el valor devuelto por OpenStreetMap a nuestros valores de longitud y latitud**/
            //casal = casalDAO.createCasal(adminid, email, name, description, localization, coordinates.longitude, coordinates.latitude, validated);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(casal.getCasalid());
        }
        catch (CasalAlreadyExistsException e)
        {
            throw new WebApplicationException("Casalid already exists", Response.Status.CONFLICT);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + casal.getCasalid());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN).entity(authenticationToken).build();
    }

    /**Aqui atacamos a la API externa para obtener las longitudes y latitudes**/
    private StringBuffer sendGet(String localization) throws Exception
    {

        String url = "http://nominatim.openstreetmap.org/search?q=" + localization + "&format=json&addressdetails=1";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        //request.addHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer coordinates = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null)
        {
            coordinates.append(line);
        }

        System.out.println(coordinates.toString());
        return coordinates;
    }

    @RolesAllowed("[admin, casal]")
    @Path("/{casalid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_CASAL)
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public Casal updateProfile(@PathParam("casalid") String casalid, @PathParam("email") String email, @PathParam("name") String name,
                               @PathParam("description") String description, @PathParam("localization") String localization, @PathParam("latitude") double latitude,
                               @PathParam("longitude") double longitude, @PathParam("validated") boolean validated, Casal casal)
    {
        if (casal == null)
            throw new BadRequestException("entity is null");
        if (!casalid.equals(casal.getCasalid()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(casalid))
            throw new ForbiddenException("operation not allowed");

        CasalDAO casalDAO = new CasalDAOImpl();
        try {
            casal = casalDAO.updateProfile(casalid, casal.getEmail(), casal.getName(), casal.getDescription(), casal.getLocalization(),casal.getLatitude(),casal.getLongitude(),casal.getValidated());
            if (casal == null)
                throw new NotFoundException("El casal con la id " + casalid + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return casal;
    }

    @Path("/{casalid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public Casal getCasalByCasalid(@PathParam("casalid") String casalid)
    {
        Casal casal;
        try
        {
            casal = (new CasalDAOImpl().getCasalByCasalid(casalid));
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (casal == null)
            throw new NotFoundException("Casal with id = " + casalid + "doesn't exist");
        return casal;
    }


    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL_COLLECTION)
    public CasalCollection getAllCasals() {
        CasalCollection CasalCollection;
        CasalDAO casalDAO = new CasalDAOImpl();
        try {
            CasalCollection = casalDAO.getAllCasals();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return CasalCollection;
    }

    @Path("/{casalid}")
    @RolesAllowed("[admin, casal]")
    @DELETE
    public void deleteCasal(@PathParam("casalid") String casalid) {
        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(casalid))
            throw new ForbiddenException("operation not allowed");

        CasalDAO casalDAO = new CasalDAOImpl();
        try {
            if (!casalDAO.deleteCasal(casalid))
                throw new NotFoundException("Casal with id = " + casalid + " doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /***********************************************************************************************************/
    /******************************************EVENTOS**********************************************************/
    /***********************************************************************************************************/

    @POST
    @Path("/{id}/events/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createEvent(@FormParam("casalid") String casalid, @FormParam("title") String title, @FormParam("description") String description,
                                @FormParam("localization") String localization, @FormParam("latitude") double latitude, @FormParam("longitude") double longitude,
                                @FormParam("eventdate") double eventdate, @Context UriInfo uriInfo) throws URISyntaxException
    {
        if (casalid == null || title == null || description == null || localization == null || latitude == 0 || longitude == 0 || eventdate == 0)
            throw new BadRequestException("all parameters are mandatory");
        EventoDAO EventoDAO = new EventoDAOImpl();
        Event Event;
        AuthToken authenticationToken;
        try
        {
            /*Aqui atacamos a la API externa para obtener las longitudes y latitudes*/


            Event = EventoDAO.createEvent(casalid, title, description, localization, latitude, longitude, eventdate);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(Event.getId());
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Event.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN).entity(authenticationToken).build();
    }

    @Path("/{id}/events/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    public Event updateProfile(@PathParam("id") String id, @PathParam("title") String title, @PathParam("description") String description,
                               @PathParam("eventdate") double eventdate, @PathParam("localization") String localization, @PathParam("latitude") double latitude,
                               @PathParam("longitude") double longitude, Event event)
    {
        if (event == null)
            throw new BadRequestException("entity is null");
        if (!id.equals(event.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");
        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(event.getCasalid()))
            throw new ForbiddenException("operation not allowed");
        EventoDAO EventoDAO = new EventoDAOImpl();
        try
        {
            event = EventoDAO.updateProfile(id, event.getTitle(), event.getDescription(), event.getEventdate(), event.getLocalization(), event.getLatitude(), event.getLongitude());
            if (event == null)
                throw new NotFoundException("El evento con la id " + id + " no existe");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return event;
    }

    @Path("{id}/events/{userid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)//Miramos la asistencia de un usuario a varios eventos
    public EventCollection getEventsByUserId(@PathParam("userid") String userid, @QueryParam("timestamp") long timestamp, @DefaultValue("true")
                                             @QueryParam("before") boolean before, @Context Request request)
    {
        EventCollection eventCollection;
        EventoDAO eventoDAO = new EventoDAOImpl();

        try
        {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            eventCollection = eventoDAO.getEventsByUserId(userid, timestamp, before);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return eventCollection;
    }

    @Path("/events/{id}")
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
    @Path("/{id}/events")
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

    /***********************************************************************************************************/
    /******************************COMMENTS**EVENTOS************************************************************/
    /***********************************************************************************************************/

    @Path("{id}/events/comments")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createEventComment(@FormParam("creatorid") String creatorid, @FormParam("eventoid") String eventoid,
                                       @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException
    {
        if (creatorid == null || eventoid == null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        Comments_Events Comments_Events;
        try
        {
            Comments_Events = Comments_EventosDAO.createComment(creatorid, eventoid, content);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Comments_Events.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS).entity(Comments_Events).build();
    }

    @Path("/events/comments")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS_COLLECTION)
    public Comments_EventsCollection getAllEventComments(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before)
    {
        Comments_EventsCollection Comments_EventsCollection;
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try
        {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_EventsCollection = Comments_EventosDAO.getAllComments(timestamp, before);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Comments_EventsCollection;
    }


    @Path("/{id}/events/{id}/comments/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_EventsCollection getCommentEventByCreatorId(@PathParam("creatorid") String creatorid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before)
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

    @Path("/{id}/events/{id}/comments/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_Events updateCommentEvent(@PathParam("id") String id, @PathParam("creatorid") String creatorid, @PathParam("content") String content, Comments_Events Comments_Events)
    {
        if (Comments_Events == null)
            throw new BadRequestException("entity is null");
        if (!id.equals(Comments_Events.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try
        {
            Comments_Events = Comments_EventosDAO.updateComment(id, Comments_Events.getCreatorid(), Comments_Events.getContent());
            if (Comments_Events == null)
                throw new NotFoundException("El comentario con la id " + id + " no existe");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Comments_Events;
    }

    @RolesAllowed("[admin, casal]")
    @Path("/{id}/events/{id}/comments/{id}")
    @DELETE
    public void deleteCommentEvent(@PathParam("id") String id)
    {
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try
        {
            if (!Comments_EventosDAO.deleteComment(id))
                throw new NotFoundException("El comentario con la id " + id + " no existe");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }

    /***********************************************************************************************************/
    /******************************COMMENTS**CASALS************************************************************/
    /***********************************************************************************************************/

    @POST
    @Path("/{id}/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createCasalComment(@FormParam("creatorid") String creatorid, @FormParam("casalid") String casalid,
                                       @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException
    {
        if (creatorid == null || casalid == null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        Comments_Casals Comments_Casals;
        try
        {
            Comments_Casals = Comments_CasalsDAO.createComment(creatorid, casalid, content);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Comments_Casals.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS).entity(Comments_Casals).build();
    }

    @Path("/{id}/comments")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS_COLLECTION)
    public Comments_CasalsCollection getAllCasalComments(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before)
    {
        Comments_CasalsCollection Comments_CasalsCollection;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try
        {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_CasalsCollection = Comments_CasalsDAO.getAllComments(timestamp, before);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Comments_CasalsCollection;
    }


    @Path("/{id}/comments/{casalid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_CasalsCollection getCommentByCasalId(@PathParam("casalid") String casalid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before)
    {
        Comments_CasalsCollection Comments_CasalsCollection;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try
        {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_CasalsCollection = Comments_CasalsDAO.getCommentByCasalId(casalid, timestamp, before);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Comments_CasalsCollection;
    }


    @Path("/{id}/comments/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals updateComment(@PathParam("id") String id, @PathParam("creatorid") String creatorid, @PathParam("content") String content, Comments_Casals Comments_Casals)
    {
        if (Comments_Casals == null)
            throw new BadRequestException("entity is null");
        if (!id.equals(Comments_Casals.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try
        {
            Comments_Casals = Comments_CasalsDAO.updateComment(id, Comments_Casals.getCreatorid(), Comments_Casals.getContent());
            if (Comments_Casals == null)
                throw new NotFoundException("El comentario con la id " + id + " no existe");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Comments_Casals;
    }

    @RolesAllowed("[admin, registered]")
    @Path("/{id}/comments/{id}")
    @DELETE
    public void deleteCommentCasal(@PathParam("id") String id)
    {
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try
        {
            if (!Comments_CasalsDAO.deleteComment(id))
                throw new NotFoundException("El comentario con la id " + id + " no existe");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }

    /***********************************************************************************************************/
    /*********************************Valoraciones**************************************************************/
    /***********************************************************************************************************/

    @Path("/{id}/valoracion")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createValoracion(@FormParam("loginid") String loginid, @FormParam("casalid") String casalid, @FormParam("valoracion") boolean valoracion, @Context UriInfo uriInfo) throws URISyntaxException
    {
        if (loginid == null ||casalid == null )
            throw new BadRequestException("all parameters are mandatory");
        ValoracionDAO ValoracionDAO = new ValoracionDAOImpl();
        Valoracion Valoracion = null;
        AuthToken authenticationToken;
        try
        {
        /*Aqui atacamos a la API externa para obtener las longitudes y latitudes*/
            try {
                Valoracion = ValoracionDAO.createValoracion(loginid, casalid, valoracion);
            } catch (UserAlreadyExistsException e) {
                e.printStackTrace();
            }
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(Valoracion.getId());
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Valoracion.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN).entity(authenticationToken).build();
    }

    @Path("/{id}/valoracion/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    public Valoracion updateValoracion(@PathParam("id") String id, @PathParam("loginid") String loginid, @PathParam("casalid") String casalid, @PathParam("valoracion") boolean valoracion, Valoracion Valoracion) {
        if (Valoracion == null)
            throw new BadRequestException("entity is null");
        if (!id.equals(Valoracion.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(Valoracion.getCasalid()))
            throw new ForbiddenException("operation not allowed");
        ValoracionDAO ValoracionDAO = new ValoracionDAOImpl();
        try
        {
            Valoracion = ValoracionDAO.updateValoracion(id, loginid, casalid, valoracion);
            if (Valoracion == null)
                throw new NotFoundException("La valoracion con la id " + id + " no existe");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Valoracion;
    }

    @Path("/{id}/valoracion/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)
    public Valoracion getValoracionByLoginid(@PathParam("loginid") String loginid)
    {
        Valoracion Valoracion;
        try
        {
            Valoracion = (new ValoracionDAOImpl().getValoracionByLoginid(loginid));
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (Valoracion == null)
            throw new NotFoundException("Event with id = " + loginid + "doesn't exist");
        return Valoracion;
    }

    @Path("/{id}/valoracion/")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)
    public ValoracionCollection getAllValoraciones()
    {
        ValoracionCollection ValoracionCollection;
        ValoracionDAO ValoracionDAO = new ValoracionDAOImpl();
        try
        {
            ValoracionCollection = ValoracionDAO.getAllValoraciones();
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return ValoracionCollection;
    }
    /****************************************************************************************************************/
    /****************************************************************************************************************/
        /*String url = "http://nominatim.openstreetmap.org/search?q=" + localization + "&format=json&addressdetails=1";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());*/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
}


    /*


    //Este va al UserResource
    @Path("/{id}/valoracion/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)
    public Valoracion getValoracionByCasalid(@PathParam("casalid") String casalid)
    {
        Valoracion Valoracion;
        try
        {
            Valoracion = (new ValoracionDAOImpl().getValoracionByCasalid(casalid));
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (Valoracion == null)
            throw new NotFoundException("Event with id = " + casalid + "doesn't exist");
        return Valoracion;
    }

    //Este va al UserResource
    @Path("/{id}/valoracion/{casalid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)
    public ValoracionCollection getValoracionesByCasalId(@PathParam("casalid") String casalid)
    {
        ValoracionCollection ValoracionCollection;
        ValoracionDAO ValoracionDAO = new ValoracionDAOImpl();
        try
        {
            ValoracionCollection = ValoracionDAO.getValoracionesByCasalId(casalid);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return ValoracionCollection;
    }


    @Path("/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals getCommentCasalById(@PathParam("id") String id)
    {
        Comments_Casals Comments_Casals = null;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try
        {
            Comments_Casals = Comments_CasalsDAO.getCommentById(id);
            if (Comments_Casals == null)
                throw new NotFoundException("No existe un comentario con id = " + id + " en este casal");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Comments_Casals;
    }*/


       /* @Path("/{id}/comments/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_CasalsCollection getCommentCasalsByCreatorId(@PathParam("creatorid") String creatorid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before)
    {

        Comments_CasalsCollection Comments_CasalsCollection = null;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try
        {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_CasalsCollection = Comments_CasalsDAO.getCommentByCreatorId(creatorid, timestamp, before);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Comments_CasalsCollection;
    }*/

       /*@Path("/{validated}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public CasalCollection getValidatedCasals()
    {
        CasalCollection casalCollection;
        CasalDAO casalDAO = new CasalDAOImpl();
        try
        {
            casalCollection = casalDAO.getValidatedCasals();
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return casalCollection;
    }

    @Path("/{novalidated}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public CasalCollection getNoValidatedCasals()
    {
        CasalCollection casalCollection;
        CasalDAO casalDAO = new CasalDAOImpl();
        try
        {
            casalCollection = casalDAO.getNoValidatedCasals();
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return casalCollection;
    }*/

       /* @Path("/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_Events getCommentEventById(@PathParam("id") String id) {
        Comments_Events Comments_Events = null;
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            Comments_Events = Comments_EventosDAO.getCommentById(id);
            if (Comments_Events == null)
                throw new NotFoundException("No existe un comentario con id = " + id + " en este evento");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_Events;
    }*/


    /*@Path("/{eventoid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_EventsCollection getCommentByEventoId(@PathParam("eventoid") String eventoid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {

        Comments_EventsCollection Comments_EventsCollection = null;
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_EventsCollection = Comments_EventosDAO.getCommentByEventoId(eventoid, timestamp, before);

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_EventsCollection;
    }*/


    /*@Path("/{id}/events/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)
    public EventCollection getEventsByCreatorId(@PathParam("userid") String casalid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        EventCollection eventCollection;
        EventoDAO eventoDAO = new EventoDAOImpl();

        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            eventCollection = eventoDAO.getEventsByCreatorId(casalid, timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return eventCollection;
    }*/


    /*@Path("/{id}/events/{id}")
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
    }*/



