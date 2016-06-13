package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.*;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Map;

@Path("casals")
public class CasalResource {
    @Context
    private SecurityContext securityContext;

    /**
     * Creamos un casal
     **/
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createCasal(@FormDataParam("email") String email, @FormDataParam("name") String name, @FormDataParam("description") String description,
                                @FormDataParam("localization") String localization, @FormDataParam("validated") boolean validated, @FormDataParam("image") InputStream file, @FormDataParam("image") FormDataContentDisposition fileDetail, @Context UriInfo uriInfo) throws URISyntaxException {
        if (email == null || name == null || description == null || localization == null)
            throw new BadRequestException("all parameters are mandatory");
        CasalDAO casalDAO = new CasalDAOImpl();
        Casal casal = null;
        OpenStreetMapUtils openStreetMapUtils = new OpenStreetMapUtils();

        try {
            /**Aqui atacamos a la API externa para obtener las longitudes y latitudes**/
            Map<String, Double> coo = openStreetMapUtils.getCoordinates(localization);
            /**De alguna manera misteriosa obtenemos lat y long y los guardamos cada uno en una variable**/
            double lon = coo.get("lon");
            double lat = coo.get("lat");
            /**Asignaremos el valor devuelto por OpenStreetMap a nuestros valores de longitud y latitud**/
         String adminid = securityContext.getUserPrincipal().getName();
            casal = casalDAO.createCasal(adminid, email, name, description, localization, lon, lat, validated, file);
        } catch (CasalAlreadyExistsException e) {
            throw new WebApplicationException("Casalid already exists", Response.Status.CONFLICT);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + casal.getCasalid());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_CASAL).entity(casal).build();
    }

    /**
     * Creamos un casal
     **/
    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createAndroidCasal(@FormParam("email") String email, @FormParam("name") String name, @FormParam("description") String description,
                                @FormParam("localization") String localization, @FormParam("validated") boolean validated, @Context UriInfo uriInfo) throws URISyntaxException {
        if (email == null || name == null || description == null || localization == null)
            throw new BadRequestException("all parameters are mandatory");
        CasalDAO casalDAO = new CasalDAOImpl();
        Casal casal = null;
        OpenStreetMapUtils openStreetMapUtils = new OpenStreetMapUtils();

        try {
            /**Aqui atacamos a la API externa para obtener las longitudes y latitudes**/
            Map<String, Double> coo = openStreetMapUtils.getCoordinates(localization);
            /**De alguna manera misteriosa obtenemos lat y long y los guardamos cada uno en una variable**/
            double lon = coo.get("lon");
            double lat = coo.get("lat");
            /**Asignaremos el valor devuelto por OpenStreetMap a nuestros valores de longitud y latitud**/
            String adminid = securityContext.getUserPrincipal().getName();
            casal = casalDAO.createAndroidCasal(adminid, email, name, description, localization, lon, lat, validated);
        } catch (CasalAlreadyExistsException e) {
            throw new WebApplicationException("Casalid already exists", Response.Status.CONFLICT);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + casal.getCasalid());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_CASAL).entity(casal).build();
    }

    /**
     * Obtenemos una lista de todos los casales
     **/
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

    /**
     * Obtenemos una lista de todos los casales que SI esten validados
     **/
    @GET
    @Path("/validated")
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL_COLLECTION)
    public CasalCollection getAllValidatedCasals() {

        CasalCollection CasalCollection;
        CasalDAO casalDAO = new CasalDAOImpl();
        try {
            CasalCollection = casalDAO.getValidatedCasals();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return CasalCollection;
    }

    /**
     * Obtenemos una lista de todos los casales que NO esten validados
     **/
    @GET
    @Path("/unvalidated")
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL_COLLECTION)
    public CasalCollection getAllUnvalidatedCasals() {
        CasalCollection CasalCollection;
        CasalDAO casalDAO = new CasalDAOImpl();
        try {
            CasalCollection = casalDAO.getNoValidatedCasals();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return CasalCollection;
    }

    /**
     * Actualizamos el perfil de un casal
     **/
    @Path("/{casalid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_CASAL)
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public Casal updateCasalProfile(@PathParam("casalid") String casalid, Casal casal) {
        if (casal == null)
            throw new BadRequestException("entity is null");
        if (!casalid.equals(casal.getCasalid()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        /*String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(casal.getAdminid()))
            throw new ForbiddenException("operation not allowed");*/

        OpenStreetMapUtils openStreetMapUtils = new OpenStreetMapUtils();
        /**Aqui atacamos a la API externa para obtener las longitudes y latitudes**/
        Map<String, Double> coo = openStreetMapUtils.getCoordinates(casal.getLocalization());
        /**De alguna manera misteriosa obtenemos lat y long y los guardamos cada uno en una variable**/
        double lon = coo.get("lon");
        double lat = coo.get("lat");
        /**Asignaremos el valor devuelto por OpenStreetMap a nuestros valores de longitud y latitud**/
        try {
            casal = new CasalDAOImpl().updateProfile(casalid, casal.getEmail(), casal.getName(), casal.getDescription(), casal.getLocalization(), lat, lon, casal.getValidated());
            if (casal == null)
                throw new NotFoundException("El casal con la id " + casalid + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return casal;
    }

    /**
     * Obtenemos el perfil de un Casal
     **/
    @Path("/{casalid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public Casal getCasalByCasalid(@PathParam("casalid") String casalid) {
        Casal casal;
        try {
            casal = (new CasalDAOImpl().getCasalByCasalid(casalid));
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (casal == null)
            throw new NotFoundException("Casal with id = " + casalid + "doesn't exist");
        return casal;
    }

    /**
     * Eliminamos un casal
     **/
    @Path("/{casalid}")
    @DELETE
    public void deleteCasal(@PathParam("casalid") String casalid) {
        CasalDAO casalDAO = new CasalDAOImpl();
        String adminid;

        try {
            adminid = casalDAO.getAdminId(casalid);
            if (adminid == null || !adminid.equals(securityContext.getUserPrincipal().getName()))
                throw new ForbiddenException("operation not allowed");
            if (!casalDAO.deleteCasal(casalid))
                throw new NotFoundException("Casal with id = " + casalid + " doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /***********************************************************************************************************/
    /******************************************EVENTOS**********************************************************/
    /***********************************************************************************************************/

    /**
     * Creamos un evento siendo un casal
     **/
    @Path("/{casalid}")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createEvent(@PathParam("casalid") String casalid, @FormDataParam("title") String title, @FormDataParam("description") String description,
                                @FormDataParam("localization") String localization, @FormDataParam("eventdate") long eventdate, @FormDataParam("image") InputStream file, @FormDataParam("image") FormDataContentDisposition fileDetail, @Context UriInfo uriInfo) throws URISyntaxException {
        if (title == null || description == null || localization == null || eventdate == 0)
            throw new BadRequestException("all parameters are mandatory");
        CasalDAO casalDAO = new CasalDAOImpl();
        String adminid;
        try {
            adminid = casalDAO.getAdminId(casalid);
            if (adminid == null || !adminid.equals(securityContext.getUserPrincipal().getName()))
                throw new ForbiddenException("You are not allowed to create events for this casal");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        EventoDAO EventoDAO = new EventoDAOImpl();
        Event event;
        OpenStreetMapUtils openStreetMapUtils = new OpenStreetMapUtils();

        try {
            /**Aqui atacamos a la API externa para obtener las longitudes y latitudes**/
            Map<String, Double> coo = openStreetMapUtils.getCoordinates(localization);
            /**De alguna manera misteriosa obtenemos lat y long y los guardamos cada uno en una variable**/
            double lon = coo.get("lon");
            double lat = coo.get("lat");
            /**Asignaremos el valor devuelto por OpenStreetMap a nuestros valores de longitud y latitud**/
            event = EventoDAO.createEvent(casalid, title, description, localization, lat, lon, eventdate, file);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/events/" + event.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_EVENTS).entity(event).build();
    }

    /**
     * Lista de eventos ofrecidas por un casal
     **/
    @GET
    @Path("/{casalid}/events")
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)
    public EventCollection getAllEventsOfCasal(@PathParam("casalid") String casalid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        EventCollection EventCollection;
        EventoDAO EventoDAO = new EventoDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            EventCollection = EventoDAO.getEventsByCasalId(casalid, timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return EventCollection;
    }

    /**
     * Actualizamos el perfil de un evento
     **/
    @Path("/{casalid}/events/{eventid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    public Event updateEventProfile(@PathParam("eventid") String eventid, @PathParam("casalid") String casalid, Event event) {

        if (event == null)
            throw new BadRequestException("Entity is null");
        if (!eventid.equals(event.getId()))
            throw new BadRequestException("Casal ID doesn't match with Event ID");
        if (!casalid.equals(event.getCasalid()))
            throw new ForbiddenException("You are not the owner of this casal");

        OpenStreetMapUtils openStreetMapUtils = new OpenStreetMapUtils();
        /**Aqui atacamos a la API externa para obtener las longitudes y latitudes**/
        Map<String, Double> coo = openStreetMapUtils.getCoordinates(event.getLocalization());
        /**De alguna manera misteriosa obtenemos lat y long y los guardamos cada uno en una variable**/
        double lon = coo.get("lon");
        double lat = coo.get("lat");
        /**Asignaremos el valor devuelto por OpenStreetMap a nuestros valores de longitud y latitud**/

        EventoDAO EventoDAO = new EventoDAOImpl();
        try {
            event = EventoDAO.updateProfile(eventid, event.getTitle(), event.getDescription(), event.getEventdate(), event.getLocalization(), lat, lon);
            if (event == null)
                throw new NotFoundException("El evento con la id " + eventid + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return event;
    }

    /**
     * Eliminamos un evento
     **/
    @Path("/{casalid}/events/{eventid}")
    @DELETE
    public void deleteEvent(@PathParam("eventid") String eventid) {
        EventoDAO EventoDAO = new EventoDAOImpl();
        try {
            if (!EventoDAO.deleteEvent(eventid))
                throw new NotFoundException("Event with id = " + eventid + " doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Obtenemos el perfil de un evento
     **/
    @Path("/{casalid}/events/{eventid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    public Event getEventsByCasalId(@PathParam("eventid") String eventid) {
        Event event;
        EventoDAO eventoDAO = new EventoDAOImpl();

        try {
            event = eventoDAO.getEventById(eventid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return event;
    }

    /***********************************************************************************************************/
    /******************************COMMENTS**EVENTOS************************************************************/
    /***********************************************************************************************************/

    /**
     * Creamos un comentario acerca de un evento
     **/
    @Path("/{casalid}/events/{eventid}/comments")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createEventComment(@PathParam("casalid") String casalid, @PathParam("eventid") String eventid,
                                       @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if (content == null)
            throw new BadRequestException("all parameters are mandatory");

        CasalDAO casalDAO = new CasalDAOImpl();
        EventoDAO eventoDAO = new EventoDAOImpl();
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        Comments_Events Comments_Events;

        try {
            boolean belong = eventoDAO.checkEventsOfCasal(casalid);
            if (belong) {
                String creatorid = casalDAO.getAdminId(casalid);
                Comments_Events = Comments_EventosDAO.createComment(creatorid, eventid, content);
            } else {
                throw new BadRequestException("Casal and EventID doesn't match");
            }
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Comments_Events.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS).entity(Comments_Events).build();
    }

    /**
     * Vemos todos los comentarios de un evento
     **/
    @Path("{casalid}/events/{eventid}/comments")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS_COLLECTION)
    public Comments_EventsCollection getAllEventComments(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        Comments_EventsCollection Comments_EventsCollection;
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_EventsCollection = Comments_EventosDAO.getAllComments(timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_EventsCollection;
    }

    /**
     * Actualizamos el comentario de un evento
     **/
    @Path("/{casalid}/events/{eventid}/comments/{commentid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_Events updateCommentEvent(@PathParam("commentid") String commentid, Comments_Events Comments_Events) {
        if (Comments_Events == null)
            throw new BadRequestException("entity is null");
        if (!commentid.equals(Comments_Events.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            Comments_Events = Comments_EventosDAO.updateComment(commentid, Comments_Events.getContent());
            if (Comments_Events == null)
                throw new NotFoundException("El comentario con la id " + commentid + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_Events;
    }

    /**
     * Visualizamos el comentario de un evento
     **/
    @Path("/{casalid}/events/{eventid}/comments/{commentid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_Events getCommentEventById(@PathParam("commentid") String eventid) {
        Comments_Events comments_events;
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();

        try {
            comments_events = Comments_EventosDAO.getCommentById(eventid);
            if (comments_events == null)
                throw new NotFoundException("El comentario con la id " + eventid + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return comments_events;
    }

    /**
     * Eliminamos el comentario de un evento
     **/
    @Path("/{casalid}/events/{eventid}/comments/{commentid}")
    @DELETE
    public void deleteCommentEvent(@PathParam("commentid") String id) {
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            if (!Comments_EventosDAO.deleteComment(id))
                throw new NotFoundException("El comentario con la id " + id + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /***********************************************************************************************************/
    /******************************COMMENTS**CASALS************************************************************/
    /***********************************************************************************************************/

    /**
     * Obtenemos todos los comentarios hechos acerca de los casals
     **/
    @Path("/comments")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS_COLLECTION)
    public Comments_CasalsCollection getAllCasalComments(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        Comments_CasalsCollection Comments_CasalsCollection;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_CasalsCollection = Comments_CasalsDAO.getAllComments(timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_CasalsCollection;
    }

    /**
     * Comentamos sobre un casal
     **/
    @POST
    @Path("/{casalid}/comments")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createCasalComment(@FormParam("creatorid") String creatorid, @PathParam("casalid") String casalid, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if (casalid == null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        Comments_Casals Comments_Casals;
        CasalDAO casalDAO = new CasalDAOImpl();
        try {
            Comments_Casals = Comments_CasalsDAO.createComment(creatorid, casalid, content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Comments_Casals.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS).entity(Comments_Casals).build();
    }

    /**
     * Obtenemos los comentarios acerca de un casal
     **/
    @Path("/{casalid}/comments")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_CasalsCollection getCommentByCasalId(@PathParam("casalid") String casalid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        Comments_CasalsCollection Comments_CasalsCollection;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_CasalsCollection = Comments_CasalsDAO.getCommentsByCasalId(casalid, timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_CasalsCollection;
    }

    /**
     * Miramos un comentario
     **/
    @Path("/{casalid}/comments/{commentid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals getCommentByCommentId(@PathParam("commentid") String id, @PathParam("casalid") String creatorid) {
        Comments_Casals comments_casals;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            comments_casals = Comments_CasalsDAO.getCommentById(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return comments_casals;
    }

    /**
     * Modificamos un comentario
     **/
    @Path("/{casalid}/comments/{commentid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals updateComment(@PathParam("casalid") String casalid, @PathParam("commentid") String commentid, @FormParam("content") String content, Comments_Casals Comments_Casals) {
        if (Comments_Casals == null)
            throw new BadRequestException("entity is null");
        if (!commentid.equals(Comments_Casals.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            Comments_Casals = Comments_CasalsDAO.updateComment(casalid, Comments_Casals.getContent());
            if (Comments_Casals == null)
                throw new NotFoundException("El comentario con la id " + commentid + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_Casals;
    }

    @Path("/{casalid}/comments/{commentid}")
    @DELETE
    public void deleteCommentCasal(@PathParam("commentid") String id) {
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            if (!Comments_CasalsDAO.deleteComment(id))
                throw new NotFoundException("El comentario con la id " + id + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Obtenemos todos los comentarios a casals hechos por un usuario
     **/
    @Path("/comments/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_CasalsCollection getCommentByCreatorId(@PathParam("creatorid") String creatorid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        Comments_CasalsCollection Comments_CasalsCollection;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_CasalsCollection = Comments_CasalsDAO.getCommentByCreatorId(creatorid, timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_CasalsCollection;
    }

    /***********************************************************************************************************/
    /*********************************Valoraciones**************************************************************/
    /***********************************************************************************************************/

    /**
     * Creamos una valoración de un casal
     **/
    @Path("/{casalid}/valoracion")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createValoracion(@FormParam("loginid") String loginid, @PathParam("casalid") String casalid, @FormParam("valoracion") boolean valoracion, @Context UriInfo uriInfo) throws URISyntaxException {
        if (loginid == null || casalid == null)
            throw new BadRequestException("all parameters are mandatory");
        ValoracionDAO ValoracionDAO = new ValoracionDAOImpl();
        Valoracion Valoracion;
        try {
                Valoracion = ValoracionDAO.createValoracion(loginid, casalid, valoracion);

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/valoracion/" + Valoracion.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_CASALS_VALORACION).entity(Valoracion).build();
    }

    /**
     * Obtenemos todas las valoraciones de un casal
     **/
    @Path("/{casalid}/valoracion")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASALS_VALORACION_COLLECTION)
    public ValoracionCollection getAllValoracionesOfCasal(@PathParam("casalid") String casalid) {
        ValoracionCollection ValoracionCollection;
        ValoracionDAO ValoracionDAO = new ValoracionDAOImpl();
        try {
            ValoracionCollection = ValoracionDAO.getValoracionesByCasalId(casalid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return ValoracionCollection;
    }

    /**
     * Modificamos la valoración de un casal
     **/
    @Path("/{casalid}/valoracion/{valoracionid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_CASALS_VALORACION)
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASALS_VALORACION)
    public Valoracion updateValoracion(@PathParam("casalid") String id, @FormParam("loginid") String loginid, @PathParam("valoracionid") String casalid, @FormParam("valoracion") boolean valoracion, Valoracion Valoracion) {
        if (Valoracion == null)
            throw new BadRequestException("entity is null");
        if (!id.equals(Valoracion.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");
        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(Valoracion.getCasalid()))
            throw new ForbiddenException("operation not allowed");
        ValoracionDAO ValoracionDAO = new ValoracionDAOImpl();
        try {
            Valoracion = ValoracionDAO.updateValoracion(id, loginid, casalid, valoracion);
            if (Valoracion == null)
                throw new NotFoundException("La valoracion con la id " + id + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Valoracion;
    }

    /**
     * Obtenemos la valoración segun la ID
     **/
    @Path("/{casalid}/valoracion/{valoracionid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASALS_VALORACION)
    public Valoracion getValoracionById(@PathParam("valoracionid") String valoracionid) {
        Valoracion Valoracion;
        try {
            Valoracion = (new ValoracionDAOImpl().getValoracionById(valoracionid));
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (Valoracion == null)
            throw new NotFoundException("Event with id = " + valoracionid + "doesn't exist");
        return Valoracion;
    }

    /**
     * Eliminamos una Valoración
     **/
    @Path("/{casalid}/valoracion/{valoracionid}")
    @DELETE
    public void deleteValoracionCasal(@PathParam("valoracionid") String valoracionid) {
        ValoracionDAO valoracionDAO = new ValoracionDAOImpl();

        try {
            if (!valoracionDAO.deleteValoracion(valoracionid))
                throw new NotFoundException("La valoración con la id " + valoracionid + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Obtenemos todos las valoraciones a casals hechos por un usuario
     **/
    @Path("/{casalid}/valoracion/user/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASALS_VALORACION_COLLECTION)
    public ValoracionCollection getValoracionesByUserId(@PathParam("creatorid") String creatorid) {
        ValoracionCollection valoracionCollection;
        ValoracionDAO valoracionDAO = new ValoracionDAOImpl();

        try {
            valoracionCollection = valoracionDAO.getValoracionesByUserId(creatorid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return valoracionCollection;
    }
}