package edu.upc.eetac.dsaqp1516gp3.okupainfo;


import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.Comments_CasalsDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.Comments_CasalsDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Comments_Casals;

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
        Comments_CasalsDAO stingDAO = new Comments_CasalsDAOImpl();
        Comments_Casals sting = null;
        AuthToken authenticationToken = null;
        try {
            sting = stingDAO.createComment(creatorid, casalid, content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + sting.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS).entity(sting).build();
    }

    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS_COLLECTION)
    public Comments_Casals getAllComments() {
        Comments_Casals stingCollection = null;
        Comments_CasalsDAO stingDAO = new Comments_CasalsDAOImpl();
        try {
            stingCollection = stingDAO.getAllComments();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return stingCollection;
    }


    @Path("/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals getCommentById(@PathParam("id") String id) {
        Comments_Casals sting = null;
        Comments_CasalsDAO stingDAO = new Comments_CasalsDAOImpl();
        try {
            sting = stingDAO.getCommentById(id);
            if (sting == null)
                throw new NotFoundException("No existe un comentario con id = " + id + " en este casal");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return sting;
    }


    @Path("/{casalid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals getCommentByCasalId(@PathParam("casalid") String casalid) {

        Comments_Casals sting = null;
        Comments_CasalsDAO stingDAO = new Comments_CasalsDAOImpl();
        try {
            sting = stingDAO.getCommentByCasalId(casalid);
            if (sting == null)
                throw new NotFoundException("El Casal " + casalid + " no tiene ningun comentario");

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return sting;
    }

    @Path("/{creatorid}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals getCommentByCreatorId(@PathParam("creatorid") String creatorid) {

        Comments_Casals sting = null;
        Comments_CasalsDAO stingDAO = new Comments_CasalsDAOImpl();
        try {
            sting = stingDAO.getCommentByCreatorId(creatorid);
            if (sting == null)
                throw new NotFoundException("El usuario " + creatorid + " no tiene ningun comentario");

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return sting;
    }




    @Path("/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    @Produces(OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS)
    public Comments_Casals updateComment(@PathParam("id") String id, @PathParam("content") String content, Comments_Casals sting) {
        if(sting == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(sting.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        Comments_CasalsDAO stingDAO = new Comments_CasalsDAOImpl();
        try {
            sting = stingDAO.updateComment(id, sting.getContent());
            if(sting == null)
                throw new NotFoundException("El comentario con la id "+id+" no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return sting;
    }

    @Path("/{id}")
    @DELETE
    public void deleteComment(@PathParam("id") String id) {
        Comments_CasalsDAO stingDAO = new Comments_CasalsDAOImpl();
        try {
            if(!stingDAO.deleteComment(id))
                throw new NotFoundException("El comentario con la id "+id+" no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}


