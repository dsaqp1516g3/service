package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @InjectLinks({
            @InjectLink(resource = OkupaInfoRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "OkupaInfo Root API"),
            @InjectLink(resource = CasalResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-casals", title = "Current casals", type = OkupaInfoMediaType.OKUPAINFO_CASAL_COLLECTION),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = EventResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-event", title = "Create Event", type = OkupaInfoMediaType.OKUPAINFO_EVENTS),
            @InjectLink(resource = UserResource.class, method = "getUser", style = InjectLink.Style.ABSOLUTE, rel = "self user-profile", title = "User profile", type = OkupaInfoMediaType.OKUPAINFO_USER, bindings = @Binding(name = "id", value = "${instance.id}"))
    })
    private List<Link> links;
    private String id;
    private String loginid;
    private String email;
    private String fullname;
    private String description;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
