package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OkupaInfoRootAPI
{
    @InjectLinks({
            @InjectLink(resource = OkupaInfoRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "OkupaInfo Root API"),
            @InjectLink(resource = EventResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-events", title = "Current Events", type = OkupaInfoMediaType.OKUPAINFO_EVENTS_COLLECTION),
            @InjectLink(resource = CasalResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-casals", title = "Current Casals", type = OkupaInfoMediaType.OKUPAINFO_CASAL_COLLECTION),
            @InjectLink(resource = UserResource.class, method = "registerAndroidUser", style = InjectLink.Style.ABSOLUTE, rel = "create-userandroid", title = "Create Event", type = OkupaInfoMediaType.OKUPAINFO_USER),
            @InjectLink(resource = UserResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-users", title = "Current Users", type = OkupaInfoMediaType.OKUPAINFO_USER_COLLECTION),
            @InjectLink(resource = CasalResource.class, method = "getCommentByCasalId", style = InjectLink.Style.ABSOLUTE, rel = "current-commentcasals", title = "Current commentcasals", type = OkupaInfoMediaType.OKUPAINFO_COMMENTS_CASALS_COLLECTION, bindings = @Binding(name = "casalid", value = "${instance.id}")),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "login", title = "Login", type = OkupaInfoMediaType.OKUPAINFO_AUTH_TOKEN)
    })
    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
