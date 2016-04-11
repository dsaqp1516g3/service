package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.CasalAlreadyExistsException;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.CasalDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.CasalDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Casal;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Path("casals")
public class CasalResource {
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
    
}
