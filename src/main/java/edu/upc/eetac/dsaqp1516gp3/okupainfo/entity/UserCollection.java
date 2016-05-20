package edu.upc.eetac.dsaqp1516gp3.okupainfo.entity;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillermo on 20/05/2016.
 */
public class UserCollection {

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
