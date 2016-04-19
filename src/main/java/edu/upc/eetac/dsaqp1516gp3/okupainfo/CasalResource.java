package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.CasalAlreadyExistsException;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.CasalDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.CasalDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.CasalCollection;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.annotation.security.RolesAllowed;

/*
        Casal createCasal(String adminid, String email, String name, String description, String localization, double latitude,double longitude, boolean validadet) throws SQLException, CasalAlreadyExistsException;
        Casal updateProfile(String casalid, String email, String name, String description) throws SQLException;
        Casal updateLocation(String casalid, String localization, String latitude, String longitude) throws SQLException;
        Casal getCasalByCasalid(String casalid) throws SQLException;
        Casal getCasalByEmail(String email) throws SQLException;
        Casal getValidatedCasals() throws SQLException;
        Casal getNoValidatedCasals() throws SQLException;
        CasalCollection getAllCasals() throws SQLException;
        boolean deleteCasal(String casalid) throws SQLException;
*/


@Path("casals")
public class CasalResource
{
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response createCasal(@FormParam("adminid") String adminid, @FormParam("email") String email, @FormParam("name") String name, @FormParam("description") String description,
        @FormParam("localization") String localization, @FormParam("latitude") Double latitude, @FormParam("longitude") Double longitude, @FormParam("validadet") boolean validadet,  @Context UriInfo uriInfo) throws URISyntaxException
    {
        if(adminid == null ||  email == null || name == null || description == null || localization == null || latitude == null || longitude == null)
            throw new BadRequestException("all parameters are mandatory");
        CasalDAO casalDAO = new CasalDAOImpl();
        Casal casal;
        AuthToken authenticationToken;
        try
        {
            /*Aqui atacamos a la API externa para obtener las longitudes y latitudes*/
            casal = casalDAO.createCasal(adminid, email, name, description, localization, latitude, longitude, validadet);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(casal.getCasalid());
        }
        catch (CasalAlreadyExistsException e)
        {
            throw new WebApplicationException("Casalid already exists", Response.Status.CONFLICT);
        }
        catch(SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + casal.getCasalid());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN).entity(authenticationToken).build();
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
            throw  new NotFoundException("Casal with id = " + casalid + "doesn't exist");
        return casal;
    }


    @Path("/{email}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public Casal getCasalByEmail(@PathParam("email") String email)
    {
        Casal casal;
        try
        {
            casal = (new CasalDAOImpl().getCasalByEmail(email));
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (casal == null)
            throw  new NotFoundException("Casal with email = " + email + "doesn't exist");
        return casal;
    }

//String casalid, String email, String name, String description)
    /*@Context
    private SecurityContext securityContext;
    @Path("/{id}")
    @RolesAllowed("[admin, casal]")*/
    @Path("/{casalid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_CASAL)
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public Casal updateProfile(@PathParam("casalid") String casalid, @PathParam("email") String email, @PathParam("name") String name, @PathParam("description") String description, Casal casal) {
        if(casal == null)
            throw new BadRequestException("entity is null");
        if(!casalid.equals(casal.getCasalid()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        CasalDAO casalDAO = new CasalDAOImpl();
        try {
            casal = casalDAO.updateProfile(casalid, casal.getEmail(), casal.getName(), casal.getDescription());
            if(casal == null)
                throw new NotFoundException("El casal con la id "+casalid+" no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return casal;
    }

    //String casalid, String localization, String latitude, String longitude
    /*@Context
    private SecurityContext securityContext;
    @Path("/{id}")
    @RolesAllowed("[admin, casal]")*/
    @Path("/{casalid}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_CASAL)
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public Casal updateLocation(@PathParam("casalid") String casalid, @PathParam("localization") String localization, @PathParam("latitude") Double latitude, @PathParam("longitude") Double longitude, Casal casal) {
        if(casal == null)
            throw new BadRequestException("entity is null");
        if(!casalid.equals(casal.getCasalid()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        CasalDAO casalDAO = new CasalDAOImpl();
        try {
            casal = casalDAO.updateLocation(casalid, casal.getLocalization(), casal.getLatitude(), casal.getLongitude());
            if(casal == null)
                throw new NotFoundException("El casal con la id "+casalid+" no existe");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return casal;
    }



    @Path("/{casalid}")
    @RolesAllowed("[admin, casal]")
    @DELETE
    public void deleteCasal(@PathParam("casalid") String casalid)
    {
        /*String casalid = securityContext.getCasalPrincipal().getName();
        if(!casalid.equals(id))
            throw new ForbiddenException("operation not allowed");*/ // Falta añadir seguridad de auth al casalid desde el AuthorizedRequestFilter

        CasalDAO casalDAO = new CasalDAOImpl();
        try
        {
            if(!casalDAO.deleteCasal(casalid))
                throw new NotFoundException("Casal with id = " + casalid + " doesn't exist");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL)
    public CasalCollection getAllCasals() {
        CasalCollection CasalCollection = null;
        CasalDAO stingDAO = new CasalDAOImpl();
        try {
            CasalCollection = stingDAO.getAllCasals();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return CasalCollection;
    }

    /* @Path("/{id}")
    @RolesAllowed("[admin, casal]")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_CASAL);
    @Produces(OkupaInfoMediaType.OKUPAINFO_CASAL);
    public Casal updateValoracion(@PathParam("id") String id, Casal casal)
    {

    }*/

}



