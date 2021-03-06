package edu.upc.eetac.dsaqp1516gp3.okupainfo.auth;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.Role;

import javax.annotation.Priority;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizedRequestFilter implements ContainerRequestFilter
{
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException
    {
        final boolean secure = requestContext.getUriInfo().getAbsolutePath().getScheme().equals("https");

        String token = requestContext.getHeaderString("X-Auth-Token");

        if(Authorized.getInstance().isAuthorized(requestContext))
            return;
        if (token == null)
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        try
        {
            final UserInfo principal = (new AuthTokenDAOImpl()).getUserByAuthToken(token);
            if(principal==null)
                throw new WebApplicationException("auth token doesn't exists", Response.Status.UNAUTHORIZED);
            requestContext.setSecurityContext(new SecurityContext()
            {
                @Override
                public Principal getUserPrincipal() {
                    return principal;
                }

                @Override
                public boolean isUserInRole(String role)
                {
                    List<Role> roles = null;
                    if (principal != null) roles = principal.getRoles();
                    return (roles.size() > 0 && roles.contains(Role.valueOf(role)));
                }

                @Override
                public boolean isSecure() {
                    return secure;
                }

                @Override
                public String getAuthenticationScheme() {
                    return "X-Auth-Token";
                }
            });
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }
}
/*
@Override
    public void filter(ContainerRequestContext requestContext) throws IOException
    {
        if(Authorized.getInstance().isAuthorized(requestContext))
            return;

        final boolean secure = requestContext.getUriInfo().getAbsolutePath().getScheme().equals("https");

        String token = requestContext.getHeaderString("X-Auth-Token");

        if (token == null)
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);

        try {
            final UserInfo principal = (new AuthTokenDAOImpl()).getUserByAuthToken(token);
            if(principal==null)
                throw new WebApplicationException("auth token doesn't exists", Response.Status.UNAUTHORIZED);

            requestContext.setSecurityContext(new SecurityContext()
            {
                @Override
                public Principal getUserPrincipal() {

                    return principal;
                }

                @Override
                public boolean isUserInRole(String role)
                {
                    List<Role> roles = null;
                    if (principal != null) roles = principal.getRoles();
                    return (roles.size() > 0 && roles.contains(Role.valueOf(role)));
                }

                @Override
                public boolean isSecure()
                {
                    return secure;
                }

                @Override
                public String getAuthenticationScheme()
                {
                    return "X-Auth-Token";
                }
            });
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
 */