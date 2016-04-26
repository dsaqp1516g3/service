package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.Comments_EventosDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.Comments_EventosDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Casals;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Events;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_EventsCollection;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Path("comments_events")
public class Comments_EventsResource {

    @Context
    private SecurityContext securityContext;

/*
    Comments_Events createComment(String creatorid, String eventoid, String content) throws SQLException;
    Comments_Events updateComment(String id, String content) throws SQLException;
    Comments_Events getCommentById(String id) throws SQLException;
    Comments_Events getCommentByEventoId(String eventoid) throws SQLException;
    Comments_Events getCommentByCreatorId(String creatorid) throws SQLException;
    Comments_Events getAllComments() throws SQLException;
    boolean deleteComment(String id) throws SQLException;
*/

    @POST
    public Response createComment(@FormParam("creatorid") String creatorid, @FormParam("eventoid") String eventoid, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if (creatorid == null || eventoid == null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        Comments_Events Comments_Events = null;
        AuthToken authenticationToken = null;
        try {
            Comments_Events = Comments_EventosDAO.createComment(creatorid, eventoid, content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Comments_Events.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS).entity(Comments_Events).build();
    }

    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS_COLLECTION)
    public Comments_EventsCollection getAllComments(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        Comments_EventsCollection Comments_EventsCollection = null;
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_EventsCollection = Comments_EventosDAO.getAllComments(timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_EventsCollection;
    }


    @Path("/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_Events getCommentById(@PathParam("id") String id) {
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
    }


    @Path("/{eventoid}")
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
    }

    @Path("/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_EventsCollection getCommentByCreatorId(@PathParam("creatorid") String creatorid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {

        Comments_EventsCollection Comments_EventsCollection = null;
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_EventsCollection = Comments_EventosDAO.getCommentByCreatorId(creatorid, timestamp, before);


        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_EventsCollection;
    }




    @Path("/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_EVENTS)
    public Comments_Events updateComment(@PathParam("id") String id, @PathParam("creatorid") String creatorid, @PathParam("content") String content, Comments_Events Comments_Events) {
        if(Comments_Events == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(Comments_Events.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            Comments_Events = Comments_EventosDAO.updateComment(id, Comments_Events.getCreatorid(), Comments_Events.getContent());
            if(Comments_Events == null)
                throw new NotFoundException("El comentario con la id "+id+" no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_Events;
    }

    @RolesAllowed("[admin, casal]")
    @Path("/{id}")
    @DELETE
    public void deleteComment(@PathParam("id") String id) {
        Comments_EventosDAO Comments_EventosDAO = new Comments_EventosDAOImpl();
        try {
            if(!Comments_EventosDAO.deleteComment(id))
                throw new NotFoundException("El comentario con la id "+id+" no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
