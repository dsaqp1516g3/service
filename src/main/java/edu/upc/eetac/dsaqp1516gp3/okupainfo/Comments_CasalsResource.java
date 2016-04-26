package edu.upc.eetac.dsaqp1516gp3.okupainfo;


import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.Comments_CasalsDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.Comments_CasalsDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Casals;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_CasalsCollection;

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

@Path("comments_casals")
public class Comments_CasalsResource {

    @Context
    private SecurityContext securityContext;

/*
    Comments_Casals createComment(String creatorid, String casalid, String content) throws SQLException;
    Comments_Casals updateComment(String id, String content) throws SQLException;
    Comments_Casals getCommentById(String id) throws SQLException;
    Comments_Casals getCommentByCasalId(String casalid) throws SQLException;
    Comments_Casals getCommentByCreatorId(String creatorid) throws SQLException;
    Comments_Casals getAllComments() throws SQLException;
    boolean deleteComment(String id) throws SQLException;
*/

    @POST
    public Response createComment(@FormParam("creatorid") String creatorid, @FormParam("casalid") String casalid, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if (creatorid == null || casalid == null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        Comments_Casals Comments_Casals = null;
        AuthToken authenticationToken = null;
        try {
            Comments_Casals = Comments_CasalsDAO.createComment(creatorid, casalid, content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + Comments_Casals.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS).entity(Comments_Casals).build();
    }

    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS_COLLECTION)
    public Comments_CasalsCollection getAllComments(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        Comments_CasalsCollection Comments_CasalsCollection = null;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_CasalsCollection = Comments_CasalsDAO.getAllComments(timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_CasalsCollection;
    }


    @Path("/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals getCommentById(@PathParam("id") String id) {
        Comments_Casals Comments_Casals = null;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            Comments_Casals = Comments_CasalsDAO.getCommentById(id);
            if (Comments_Casals == null)
                throw new NotFoundException("No existe un comentario con id = " + id + " en este casal");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_Casals;
    }


    @Path("/{casalid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_CasalsCollection getCommentByCasalId(@PathParam("casalid") String casalid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {

        Comments_CasalsCollection Comments_CasalsCollection = null;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_CasalsCollection = Comments_CasalsDAO.getCommentByCasalId(casalid, timestamp, before);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Comments_CasalsCollection;
    }

    @Path("/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_CasalsCollection getCommentByCreatorId(@PathParam("creatorid") String creatorid, @QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {

        Comments_CasalsCollection Comments_CasalsCollection = null;
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            Comments_CasalsCollection = Comments_CasalsDAO.getCommentByCreatorId(creatorid, timestamp, before);
            }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return Comments_CasalsCollection;
    }


    @Path("/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals updateComment(@PathParam("id") String id, @PathParam("creatorid") String creatorid, @PathParam("content") String content, Comments_Casals Comments_Casals) {
        if(Comments_Casals == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(Comments_Casals.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            Comments_Casals = Comments_CasalsDAO.updateComment(id,Comments_Casals.getCreatorid(), Comments_Casals.getContent());
            if(Comments_Casals == null)
                throw new NotFoundException("El comentario con la id "+id+" no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return Comments_Casals;
    }

    @RolesAllowed("[admin, casal]")
    @Path("/{id}")
    @DELETE
    public void deleteComment(@PathParam("id") String id) {
        Comments_CasalsDAO Comments_CasalsDAO = new Comments_CasalsDAOImpl();
        try {
            if(!Comments_CasalsDAO.deleteComment(id))
                throw new NotFoundException("El comentario con la id "+id+" no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}


