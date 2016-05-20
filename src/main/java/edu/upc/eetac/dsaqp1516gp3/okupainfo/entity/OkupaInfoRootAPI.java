package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.LoginResource;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoMediaType;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoRootAPIResource;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.UserResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OkupaInfoRootAPI
{
    @InjectLinks({
            @InjectLink(resource = OkupaInfoRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "self bookmark home", title = "Rate Root API")
            ,@InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "login", title = "Login",  type= OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
            ,@InjectLink(resource = UserResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-user", title = "Register", type= OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
            ,@InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout", condition="${!empty resource.userid}")
            ,@InjectLink(resource = UserResource.class, method="getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", condition="${!empty resource.userid}", type= OkupaInfoMediaType.OKUPAINFO_USER, bindings = @Binding(name = "id", value = "${resource.userid}"))
    })
    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
