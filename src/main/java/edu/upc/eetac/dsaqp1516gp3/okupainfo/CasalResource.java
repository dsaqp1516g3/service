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
public class CasalResource {
    @Context
    private SecurityContext securityContext;

    /**Creamos un casal**/
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createCasal(@FormParam("adminid") String adminid, @FormParam("email") String email, @FormParam("name") String name, @FormParam("description") String description,
                                @FormParam("localization") String localization, @FormParam("validated") boolean validated, @Context UriInfo uriInfo) throws URISyntaxException {
        if (adminid == null || email == null || name == null || description == null || localization == null)
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
            casal = casalDAO.createCasal(adminid, email, name, description, localization, lon, lat, validated);
        } catch (CasalAlreadyExistsException e) {
            throw new WebApplicationException("Casalid already exists", Response.Status.CONFLICT);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + casal.getCasalid());
        return Response.created(uri).build();
    }

    /**Obtenemos una lista de todos los casales**/
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

    /**Obtenemos una lista de todos los casales que SI esten validados**/
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

    /**Obtenemos una lista de todos los casales que NO esten validados**/
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

    /**Actualizamos el perfil de un casal**/
    //  @RolesAllowed("[admin, casal]")
    @Path("/{casalid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_CASAL)
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public Casal updateCasalProfile(@PathParam("casalid") String casalid, Casal casal) {
        if (casal == null)
            throw new BadRequestException("entity is null");
        if (!casalid.equals(casal.getCasalid()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(casalid))
            throw new ForbiddenException("operation not allowed");

        CasalDAO casalDAO = new CasalDAOImpl();
        try {
            casal = casalDAO.updateProfile(casalid, casal.getEmail(), casal.getName(), casal.getDescription(), casal.getLocalization(), casal.getLatitude(), casal.getLongitude(), casal.getValidated());
            if (casal == null)
                throw new NotFoundException("El casal con la id " + casalid + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return casal;
    }

    /**Obtenemos el perfil de un Casal**/
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

    /**Eliminamos un casal**/
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

    /**Creamos un evento siendo un casal**/
    @POST
    @Path("/{casalid}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createEvent(@PathParam("casalid") String casalid, @FormParam("title") String title, @FormParam("description") String description,
                                @FormParam("localization") String localization, @FormParam("eventdate") long eventdate, @Context UriInfo uriInfo) throws URISyntaxException {
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
        Event Event;
        OpenStreetMapUtils openStreetMapUtils = new OpenStreetMapUtils();

        try {
            /**Aqui atacamos a la API externa para obtener las longitudes y latitudes**/
            Map<String, Double> coo = openStreetMapUtils.getCoordinates(localization);
            /**De alguna manera misteriosa obtenemos lat y long y los guardamos cada uno en una variable**/
            double lon = coo.get("lon");
            double lat = coo.get("lat");
            /**Asignaremos el valor devuelto por OpenStreetMap a nuestros valores de longitud y latitud**/
            Event = EventoDAO.createEvent(casalid, title, description, localization, lat, lon, eventdate);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Event.getId());
        return Response.created(uri).build();
    }

    /**Lista de eventos ofrecidas por un casal**/
    //@RolesAllowed("[registered, admin, casal]")
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

    /**Actualizamos el perfil de un evento**/
    @Path("/{casalid}/events/{eventid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
    public Event updateEventProfile(@PathParam("eventid") String eventid, Event event) {
        if (event == null)
            throw new BadRequestException("entity is null");
        if (!eventid.equals(event.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");
        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(event.getCasalid()))
            throw new ForbiddenException("operation not allowed");
        EventoDAO EventoDAO = new EventoDAOImpl();
        try {
            event = EventoDAO.updateProfile(eventid, event.getTitle(), event.getDescription(), event.getEventdate(), event.getLocalization(), event.getLatitude(), event.getLongitude());
            if (event == null)
                throw new NotFoundException("El evento con la id " + eventid + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return event;
    }

    /**Eliminamos un evento**/
    @Path("/{casalid}/events/{eventid}")
    @RolesAllowed("[admin, casal]")
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

    /**Obtenemos el perfil de un evento**/
    @Path("/{casalid}/events/{eventid}")
    @RolesAllowed("[admin, casal]")
    @GET
    public Event getEventsByCasalId(@PathParam("casalid") String casalid, @PathParam("eventid") String eventid) {
        Event event;
        EventoDAO eventoDAO = new EventoDAOImpl();

        try {
            event = eventoDAO.getEventByIdAndCasalId(casalid, eventid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return event;
    }

    /***********************************************************************************************************/
    /******************************COMMENTS**EVENTOS************************************************************/
    /***********************************************************************************************************/

    /**Creamos un comentario acerca de un evento**/
    @Path("{casalid}/events/{eventid}/comments")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createEventComment(@FormParam("creatorid") String creatorid, @FormParam("eventoid") String eventoid,
                                       @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if (creatorid == null || eventoid == null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        Comments_Events Comments_Events;
        try {
            Comments_Events = Comments_EventosDAO.createComment(creatorid, eventoid, content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Comments_Events.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS).entity(Comments_Events).build();
    }

    /**Vemos todos los comentarios de un evento**/
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

    /**Actualizamos el comentario de un evento**/
    @Path("/{casalid}/events/{eventid}/comments/{commentid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_Events updateCommentEvent(@PathParam("commentid") String commentid, @FormParam("content") String content, Comments_Events Comments_Events) {
        if (Comments_Events == null)
            throw new BadRequestException("entity is null");
        if (!commentid.equals(Comments_Events.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            Comments_Events = Comments_EventosDAO.updateComment(commentid, Comments_Events.getCreatorid(), Comments_Events.getContent());
            if (Comments_Events == null)
                throw new NotFoundException("El comentario con la id " + commentid + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_Events;
    }

    /**Visualizamos el comentario de un evento**/
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

    /**Eliminamos el comentario de un evento**/
    @RolesAllowed("[admin, casal]")
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

    /**Obtenemos todos los comentarios hechos acerca de los casals**/
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

    /**Comentamos sobre un casal**/
    @POST
    @Path("/{casalid}/comments")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createCasalComment(@FormParam("creatorid") String creatorid, @PathParam("casalid") String casalid,
                                       @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if (creatorid == null || casalid == null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        Comments_Casals Comments_Casals;
        try {
            Comments_Casals = Comments_CasalsDAO.createComment(creatorid, casalid, content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Comments_Casals.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS).entity(Comments_Casals).build();
    }

    /**Obtenemos los comentarios acerca de un casal**/
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

    /**Miramos un comentario**/
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

    /**Modificamos un comentario**/
    @Path("/{casalid}/comments/{commentid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals updateComment(@PathParam("casalid") String id, @PathParam("commentid") String creatorid, @FormParam("content") String content, Comments_Casals Comments_Casals) {
        if (Comments_Casals == null)
            throw new BadRequestException("entity is null");
        if (!id.equals(Comments_Casals.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            Comments_Casals = Comments_CasalsDAO.updateComment(id, Comments_Casals.getCreatorid(), Comments_Casals.getContent());
            if (Comments_Casals == null)
                throw new NotFoundException("El comentario con la id " + id + " no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_Casals;
    }

    @RolesAllowed("[admin, registered]")
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

    /**Obtenemos todos los comentarios a casals hechos por un usuario**/
    @Path("/comments/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_CasalsCollection getCommentByCreatorId(@PathParam("creatorid") String creatorid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        Comments_CasalsCollection Comments_CasalsCollection;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_CasalsCollection = Comments_CasalsDAO.getCommentByCreatorId( creatorid, timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_CasalsCollection;
    }

    /***********************************************************************************************************/
    /*********************************Valoraciones**************************************************************/
    /***********************************************************************************************************/
//
//    @Path("/{id}/valoracion")
//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
//    public Response createValoracion(@FormParam("loginid") String loginid, @FormParam("casalid") String casalid, @FormParam("valoracion") boolean valoracion, @Context UriInfo uriInfo) throws URISyntaxException {
//        if (loginid == null || casalid == null)
//            throw new BadRequestException("all parameters are mandatory");
//        ValoracionDAO ValoracionDAO = new ValoracionDAOImpl();
//        Valoracion Valoracion = null;
//        AuthToken authenticationToken;
//        try {
//        /*Aqui atacamos a la API externa para obtener las longitudes y latitudes*/
//            try {
//                Valoracion = ValoracionDAO.createValoracion(loginid, casalid, valoracion);
//            } catch (UserAlreadyExistsException e) {
//                e.printStackTrace();
//            }
//            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(Valoracion.getId());
//        } catch (SQLException e) {
//            throw new InternalServerErrorException();
//        }
//        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Valoracion.getId());
//        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN).entity(authenticationToken).build();
//    }
//
//    @Path("/{id}/valoracion/{id}")
//    @PUT
//    @Consumes(OkupaInfoMediaType.OKUPAINFO_EVENTS)
//    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS)
//    public Valoracion updateValoracion(@PathParam("id") String id, @PathParam("loginid") String loginid, @PathParam("casalid") String casalid, @PathParam("valoracion") boolean valoracion, Valoracion Valoracion) {
//        if (Valoracion == null)
//            throw new BadRequestException("entity is null");
//        if (!id.equals(Valoracion.getId()))
//            throw new BadRequestException("path parameter id and entity parameter id doesn't match");
//        String userid = securityContext.getUserPrincipal().getName();
//        if (!userid.equals(Valoracion.getCasalid()))
//            throw new ForbiddenException("operation not allowed");
//        ValoracionDAO ValoracionDAO = new ValoracionDAOImpl();
//        try {
//            Valoracion = ValoracionDAO.updateValoracion(id, loginid, casalid, valoracion);
//            if (Valoracion == null)
//                throw new NotFoundException("La valoracion con la id " + id + " no existe");
//        } catch (SQLException e) {
//            throw new InternalServerErrorException();
//        }
//        return Valoracion;
//    }
//
//    @Path("/{id}/valoracion/{id}")
//    @GET
//    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)
//    public Valoracion getValoracionByLoginid(@PathParam("loginid") String loginid) {
//        Valoracion Valoracion;
//        try {
//            Valoracion = (new ValoracionDAOImpl().getValoracionByLoginid(loginid));
//        } catch (SQLException e) {
//            throw new InternalServerErrorException(e.getMessage());
//        }
//        if (Valoracion == null)
//            throw new NotFoundException("Event with id = " + loginid + "doesn't exist");
//        return Valoracion;
//    }
//
//    @Path("/{id}/valoracion/")
//    @GET
//    @Produces(OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION)
//    public ValoracionCollection getAllValoraciones() {
//        ValoracionCollection ValoracionCollection;
//        ValoracionDAO ValoracionDAO = new ValoracionDAOImpl();
//        try {
//            ValoracionCollection = ValoracionDAO.getAllValoraciones();
//        } catch (SQLException e) {
//            throw new InternalServerErrorException();
//        }
//        return ValoracionCollection;
//    }
}