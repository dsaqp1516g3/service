package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.*;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthToken
{
    @InjectLinks({
            @InjectLink(resource = OkupaInfoRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "OkupaInfo Root API"),
            @InjectLink(resource = CasalResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-event", title = "Create Event", type = OkupaInfoMediaType.OKUPAINFO_CASAL),
            @InjectLink(resource = CasalResource.class, method = "createAndroidCasal", style = InjectLink.Style.ABSOLUTE, rel = "create-casalandroid", title = "Create Casal", type = OkupaInfoMediaType.OKUPAINFO_CASAL),
            @InjectLink(resource = UserResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-users", title = "Current Users", type = OkupaInfoMediaType.OKUPAINFO_USER_COLLECTION),
            @InjectLink(resource = CasalResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-commentcasal", title = "Create CommentCasal", type = OkupaInfoMediaType.OKUPAINFO_CASAL),
            @InjectLink(resource = CasalResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-commentevent", title = "Create CommentEvent", type = OkupaInfoMediaType.OKUPAINFO_CASAL)
    })
    private List<Link> links;

    private String userid;
    private String token;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
