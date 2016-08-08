package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.UserAlreadyExistsException;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.UserDAO;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.UserDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.AuthToken;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.User;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.UserCollection;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Path("users")
public class UserResource {
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response registerUser(@FormDataParam("loginid") String loginid, @FormDataParam("password") String password, @FormDataParam("email") String email,
                                 @FormDataParam("fullname") String fullname, @FormDataParam("description") String description, @FormDataParam("image") InputStream file, @FormDataParam("image") FormDataContentDisposition fileDetail, @Context UriInfo uriInfo) throws URISyntaxException {
        if (loginid == null || password == null || email == null || fullname == null || description == null)
            throw new BadRequestException("all parameters are mandatory");
        UserDAO userDAO = new UserDAOImpl();
        User user;
        AuthToken authenticationToken;
        try
        {
            user = userDAO.createUser(loginid, password, email, fullname, description, file);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(user.getId());
        }
        catch (UserAlreadyExistsException e)
        {
            throw new WebApplicationException("loginid already exists", Response.Status.CONFLICT);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + user.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN).entity(authenticationToken).build();
    }

    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    public Response registerAndroidUser(@FormParam("loginid") String loginid, @FormParam("password") String password, @FormParam("email") String email,
                                 @FormParam("fullname") String fullname, @FormParam("description") String description, @Context UriInfo uriInfo) throws URISyntaxException {
        if (loginid == null || password == null || email == null || fullname == null || description == null)
            throw new BadRequestException("all parameters are mandatory");
        UserDAO userDAO = new UserDAOImpl();
        User user;
        AuthToken authenticationToken;
        try
        {
            user = userDAO.createAndroidUser(loginid, password, email, fullname, description);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(user.getId());
        }
        catch (UserAlreadyExistsException e)
        {
            throw new WebApplicationException("loginid already exists", Response.Status.CONFLICT);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + user.getId());
        return Response.created(uri).type(OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN).entity(authenticationToken).build();
    }


    @Path("/{id}")
    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_USER)
    public User getUser(@PathParam("id") String id) {
        User user;
        try {
            user = (new UserDAOImpl()).getUserById(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (user == null)
            throw new NotFoundException("User with id = " + id + " doesn't exist");
        return user;
    }

    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_USER_COLLECTION)
    public UserCollection getAllUsers() {
        UserCollection UserCollection;
        UserDAO userDAO = new UserDAOImpl();
        try {
            UserCollection = userDAO.getAllUsers();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return UserCollection;
    }

    @Path("/{id}")
    @PUT
    @Consumes(OkupaInfoMediaType.OKUPAINFO_USER)
    @Produces(OkupaInfoMediaType.OKUPAINFO_USER)
    public User updateUser(@PathParam("id") String id, User user) {
        if (user == null)
            throw new BadRequestException("entity is null");
        if (!id.equals(user.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(id))
            throw new ForbiddenException("operation not allowed");

        UserDAO userDAO = new UserDAOImpl();
        try {
            user = userDAO.updateProfile(userid, user.getEmail(), user.getFullname(), user.getDescription());
            if (user == null)
                throw new NotFoundException("User with id = " + id + " doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return user;
    }

    @Path("/{id}")
    @DELETE
    public void deleteUser(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(id))
            throw new ForbiddenException("operation not allowed");
        UserDAO userDAO = new UserDAOImpl();
        try {
            if (!userDAO.deleteUser(id))
                throw new NotFoundException("User with id = " + id + " doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}