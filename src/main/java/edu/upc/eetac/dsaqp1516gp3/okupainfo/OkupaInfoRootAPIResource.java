package edu.upc.eetac.dsaqp1516gp3.okupainfo;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.entity.OkupaInfoRootAPI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@Path("/")
public class OkupaInfoRootAPIResource
{
    @Context
    private SecurityContext securityContext;

    private String userid;

    @GET
    @Produces(OkupaInfoMediaType.OKUPAINFO_ROOT)
    public OkupaInfoRootAPI getRootAPI()
    {
        if(securityContext.getUserPrincipal()!=null)
            userid = securityContext.getUserPrincipal().getName();
        OkupaInfoRootAPI okupaInfoRootAPI = new OkupaInfoRootAPI();

        return okupaInfoRootAPI;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }
}
