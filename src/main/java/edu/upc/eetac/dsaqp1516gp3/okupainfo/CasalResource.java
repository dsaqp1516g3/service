package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.*;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Map;

@Path("casals")
public class CasalResource
{
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createCasal(@FormParam("adminid") String adminid, @FormParam("email") String email, @FormParam("name") String name, @FormParam("description") String description,
                                @FormParam("localization") String localization, @FormParam("validated") boolean validated, @Context UriInfo uriInfo) throws URISyntaxException
    {
        if (adminid == null || email == null || name == null || description == null || localization == null)
            throw new BadRequestException("all parameters are mandatory");
        CasalDAO casalDAO = new CasalDAOImpl();
        Casal casal = null;
        OpenStreetMapUtils openStreetMapUtils = new OpenStreetMapUtils();

        AuthToken authenticationToken = null;
        try
        {
            /**Aqui atacamos a la API externa para obtener las longitudes y latitudes**/
            Map<String, Double> coo = openStreetMapUtils.getCoordinates(localization);
            double lon = coo.get("lon");
            double lat = coo.get("lat");
            /**De alguna manera misteriosa obtenemos lat y long y los guardamos cada uno en una variable**/

            /**Asignaremos el valor devuelto por OpenStreetMap a nuestros valores de longitud y latitud**/
            //casal = casalDAO.createCasal(adminid, email, name, description, localization, coordinates.longitude, coordinates.latitude, validated);
            casal = casalDAO.createCasal(adminid, email, name, description, localization, lon, lat, validated);

            //authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(casal.getCasalid());
        }
        catch (CasalAlreadyExistsException e)
        {
            throw new WebApplicationException("Casalid already exists", Response.Status.CONFLICT);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + casal.getCasalid());
            return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN).entity(authenticationToken).build();
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

    //@GET
//    @Path("/validated")
//    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL_COLLECTION)
//    public CasalCollection getAllCasals() {
//        CasalCollection CasalCollection;
//        CasalDAO casalDAO = new CasalDAOImpl();
//        try {
//            CasalCollection = casalDAO.getAllCasals();
//        } catch (SQLException e) {
//            throw new InternalServerErrorException();
//        }
//        return CasalCollection;
//    }
//    @GET
//    @Path("/unvalildated")
//    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL_COLLECTION)
//    public CasalCollection g() {
//        CasalCollection CasalCollection;
//        CasalDAO casalDAO = new CasalDAOImpl();
//        try {
//            CasalCollection = casalDAO.getAllCasals();
//        } catch (SQLException e) {
//            throw new InternalServerErrorException();
//        }
//        return CasalCollection;
//    }

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

    @GET
    @Path("/{id}/events") /**Get All events of 1 casal**/
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

    @Path("/{id}/events/{id}")
    @RolesAllowed("[admin, casal]")
    @DELETE
    public void deleteEvent(@PathParam("id") String id)
    {
        EventoDAO EventoDAO = new EventoDAOImpl();
        try
        {
            if (!EventoDAO.deleteEvent(id))
                throw new NotFoundException("Event with id = " + id + " doesn't exist");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }

    @Path("/{id}/events/{id}") /**Get de un solo evento en concreto de un casal específico**/
    @RolesAllowed("[admin, casal]")
    @GET
    public EventCollection getEventsByCasalId(@PathParam("id") String id, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before, @Context Request request)
    {
        EventCollection eventCollection;
        EventoDAO eventoDAO = new EventoDAOImpl();

        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            eventCollection = eventoDAO.getEventsByCreatorId(id, timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return eventCollection;
    }

    @Path("{id}/events/{userid}") /**Comprobamos a que eventos asiste el usuario**/
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

    @Path("{id}/events/{id}/{userid}") /**Añadimos la asistencia del usuario al evento**/
    @POST
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)//Miramos la asistencia de un usuario a varios eventos
    public void addAssistanceToEvent(@FormParam("userid") String userid, @FormParam("eventid") String eventid, @Context UriInfo uriInfo) throws URISyntaxException
    {
        if (userid == null || eventid == null)
            throw new BadRequestException("all parameters are mandatory");
        EventoDAO eventoDAO = new EventoDAOImpl();

        try
        {
            eventoDAO.addUserAssistance(userid, eventid);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }

    @Path("{id}/events/{id}/{userid}") /**Eliminamos la asistencia del usuario al evento**/
    @DELETE
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)//Miramos la asistencia de un usuario a varios eventos
    public void deleteAssistanceToEvent(@PathParam("userid") String userid, @PathParam("eventid") String eventid)
    {
        EventoDAO eventoDAO = new EventoDAOImpl();

        try
        {
            if (!eventoDAO.deleteAssistanceEvent(userid, eventid));
            throw new NotFoundException("Event with id = " + eventid + " doesn't exist or user with id = " + userid + " doesn't exist");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }

    /***********************************************************************************************************/
    /******************************COMMENTS**EVENTOS************************************************************/
    /***********************************************************************************************************/

    @Path("{id}/events/{id}")
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

    @Path("{id}/events/{id}/comments")
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

    @Path("/comments")
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

    @Path("/{id}/comments/") /**Get  specific comment of casal**/
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

    @Path("/comments/{creatorid}") /**Get  all comments of a specific user in regards to this casal**/
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_CasalsCollection getCommentByCreatorId(@PathParam("creatorid") String creatorid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before)
    {
        Comments_CasalsCollection Comments_CasalsCollection;
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