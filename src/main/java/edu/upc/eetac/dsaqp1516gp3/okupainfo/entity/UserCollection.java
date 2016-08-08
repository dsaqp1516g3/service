package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoMediaType;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.OkupaInfoRootAPIResource;
import edu.upc.eetac.dsaqp1516gp3.okupainfo.UserResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillermo on 20/05/2016.
 */
public class UserCollection {
    @InjectLinks({
            @InjectLink(resource = OkupaInfoRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "OkupaInfo Root API"),
            @InjectLink(resource = UserResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-users", title = "Current users", type = OkupaInfoMediaType.OKUPAINFO_USER_COLLECTION)
             })
    private List<Link> links;
    private List<User> Users = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<User> getUsers() {
        return Users;
    }

    public void setUsers(List<User> users) {
        Users = users;
    }
}
