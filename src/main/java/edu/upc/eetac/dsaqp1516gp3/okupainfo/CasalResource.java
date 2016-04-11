package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.CasalAlreadyExistsException;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.CasalDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.CasalDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Path("casals")
public class CasalResource
{
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response registerCasal(@FormParam("loginid") String loginid, @FormParam("password") String password, @FormParam("email") String email, @FormParam("fullname") String fullname, @FormParam("descripcion") String descripcion, @FormParam("localization") String localization, @Context UriInfo uriInfo) throws URISyntaxException
    {
        if(loginid == null || password == null || email == null || fullname == null || descripcion == null || localization == null)
            throw new BadRequestException("all parameters are mandatory");
        CasalDAO casalDAO = new CasalDAOImpl();
        Casal casal;
        AuthToken authenticationToken;
        try
        {
            casal = casalDAO.createCasal(loginid, password, email, fullname, descripcion, localization);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(casal.getId());
        }
        catch (CasalAlreadyExistsException e)
        {
            throw new WebApplicationException("loginid already exists", Response.Status.CONFLICT);
        }
        catch(SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + casal.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN).entity(authenticationToken).build();
    }

    @Path("/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public Casal getCasal(@PathParam("id") String id)
    {
        Casal casal;
        try
        {
            casal = (new CasalDAOImpl().getCasalById(id));
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (casal == null)
            throw  new NotFoundException("Casal with id = " + id + "doesn't exist");
        return casal;
    }

    @Context
    private SecurityContext securityContext;
    @Path("/{id}")
    @RolesAllowed("[admin, casal]")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_CASAL);
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL);
    public Casal updateCasal(@PathParam("id") String id, Casal casal)
    {
        if (casal == null)
            throw new BadRequestException("Entity is null");
        if(!id.equals(casal.getId()))
            throw new BadRequestException("Path parameter id and entity parameter id doesn't match");

        /*String casalid = securityContext.getCasalPrincipal().getName();
        if(!casalid.equals(id))
            throw new ForbiddenException("operation not allowed");*/ // Falta añadir seguridad de auth al casalid desde el AuthorizedRequestFilter

        CasalDAO casalDAO = new CasalDAOImpl();
        try
        {
            casal = casalDAO.updateProfile(casalid, casal.getEmail(), casal.getFullname(), casal.getDescription());
            if(casal == null)
                throw new NotFoundException("Casal with id = " + id + " doesn't exist");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return casal;

    }


    @Path("/{id}")
    @RolesAllowed("[admin, casal]")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_CASAL);
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL);
    public Casal updateValoracion(@PathParam("id") String id, Casal casal)
    {

    }


    @Path("/{id}")
    @RolesAllowed("[admin, casal]")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_CASAL);
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL);
    public Casal updateLocation(@PathParam("id") String id, Casal casal)
    {

    }

    @Path("({id}")
    @RolesAllowed("[admin, casal]")
    @DELETE
    public void deleteCasal(@PathParam("id") String id)
    {
        /*String casalid = securityContext.getCasalPrincipal().getName();
        if(!casalid.equals(id))
            throw new ForbiddenException("operation not allowed");*/ // Falta añadir seguridad de auth al casalid desde el AuthorizedRequestFilter

        CasalDAO casalDAO = new CasalDAOImpl();
        try
        {
            if(!casalDAO.deleteCasal(id))
                throw new NotFoundException("Casal with id = " + id + " doesn't exist");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }
}
